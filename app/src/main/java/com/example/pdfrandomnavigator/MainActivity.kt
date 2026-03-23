package com.example.pdfrandomnavigator

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Random

class MainActivity : AppCompatActivity(), OnPageChangeListener {

    private lateinit var pdfView: PDFView
    private lateinit var pageInfo: TextView
    private lateinit var noPdfText: TextView
    private lateinit var selectPdfButton: Button
    private lateinit var randomPageButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    private var currentPdfUri: Uri? = null
    private var totalPages: Int = 0
    private var currentPage: Int = 0
    private var lastClickTime: Long = 0
    private val random = Random()

    companion object {
        private const val PREFS_NAME = "PDFRandomNavigatorPrefs"
        private const val KEY_LAST_PDF_URI = "last_pdf_uri"
        private const val DOUBLE_CLICK_DELAY = 300
    }

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                loadPdfFromUri(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupClickListeners()
        setupDoubleClickListener()
        
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        
        // Try to load the last used PDF
        loadLastUsedPdf()
    }

    private fun initViews() {
        pdfView = findViewById(R.id.pdfView)
        pageInfo = findViewById(R.id.pageInfo)
        noPdfText = findViewById(R.id.noPdfText)
        selectPdfButton = findViewById(R.id.selectPdfButton)
        randomPageButton = findViewById(R.id.randomPageButton)
    }

    private fun setupClickListeners() {
        selectPdfButton.setOnClickListener {
            openFilePicker()
        }

        randomPageButton.setOnClickListener {
            jumpToRandomPage()
        }
    }

    private fun setupDoubleClickListener() {
        pdfView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime < DOUBLE_CLICK_DELAY) {
                        // Double click detected
                        jumpToRandomPage()
                        return@setOnTouchListener true
                    }
                    lastClickTime = currentTime
                }
            }
            false // Let the PDF view handle the touch event for scrolling
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        filePickerLauncher.launch(Intent.createChooser(intent, "Select PDF File"))
    }

    private fun loadPdfFromUri(uri: Uri) {
        try {
            currentPdfUri = uri
            val inputStream = contentResolver.openInputStream(uri)
            
            if (inputStream != null) {
                // Copy the file to app's internal storage for easier access
                val file = File(filesDir, "temp_pdf.pdf")
                copyInputStreamToFile(inputStream, file)
                
                // Load the PDF
                pdfView.fromFile(file)
                    .defaultPage(0)
                    .onPageChange(this)
                    .load()
                
                // Save the URI for next time
                saveLastPdfUri(uri)
                
                // Update UI
                showPdfView()
                
                // Jump to a random page initially
                pdfView.postDelayed({ jumpToRandomPage() }, 500)
            }
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.pdf_error), Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun copyInputStreamToFile(inputStream: InputStream, file: File) {
        inputStream.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
    }

    private fun jumpToRandomPage() {
        if (totalPages > 1) {
            val randomPage = random.nextInt(totalPages)
            pdfView.jumpTo(randomPage)
        }
    }

    private fun saveLastPdfUri(uri: Uri) {
        sharedPreferences.edit()
            .putString(KEY_LAST_PDF_URI, uri.toString())
            .apply()
    }

    private fun loadLastUsedPdf() {
        val uriString = sharedPreferences.getString(KEY_LAST_PDF_URI, null)
        if (uriString != null) {
            try {
                val uri = Uri.parse(uriString)
                loadPdfFromUri(uri)
            } catch (e: Exception) {
                // URI might be invalid, clear it
                sharedPreferences.edit()
                    .remove(KEY_LAST_PDF_URI)
                    .apply()
            }
        } else {
            showNoPdfMessage()
        }
    }

    private fun showPdfView() {
        pdfView.visibility = View.VISIBLE
        noPdfText.visibility = View.GONE
        randomPageButton.isEnabled = true
    }

    private fun showNoPdfMessage() {
        pdfView.visibility = View.GONE
        noPdfText.visibility = View.VISIBLE
        randomPageButton.isEnabled = false
        pageInfo.text = ""
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        currentPage = page + 1 // PDFView uses 0-based indexing
        totalPages = pageCount
        updatePageInfo()
    }

    private fun updatePageInfo() {
        if (totalPages > 0) {
            pageInfo.text = getString(R.string.current_page, currentPage, totalPages)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up temporary file
        val tempFile = File(filesDir, "temp_pdf.pdf")
        if (tempFile.exists()) {
            tempFile.delete()
        }
    }
}
