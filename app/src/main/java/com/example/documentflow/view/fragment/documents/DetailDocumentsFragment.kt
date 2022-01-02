package com.example.documentflow.view.fragment.documents

import android.Manifest
import android.Manifest.permission
import android.Manifest.permission.*
import android.R
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.documentflow.data.retrofit.RetrofitBuilder
import com.example.documentflow.databinding.FragmentDetailDocumentsBinding
import com.example.documentflow.viewmodel.MyViewModel
import com.example.documentflow.viewmodel.factory.ViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.net.Uri

import android.content.Intent
import android.provider.Settings
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.os.Build.VERSION

import android.os.Build.VERSION.SDK_INT





class DetailDocumentsFragment : Fragment() {
    private companion object {
        const val TAG = "Detail Documents Fragment"
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

    private var _binding: FragmentDetailDocumentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var myViewModel: MyViewModel
    private var letterId: Int = 0
    private val documentsDialog = DocumentsDialogFragment()

    // declaring width and height
    // for our PDF file.
    var pageHeight = 1120
    var pagewidth = 792


    // constant code for runtime permissions
    private val PERMISSION_REQUEST_CODE = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        log("onCreateView")
        setUpViewModel()
        letterId = arguments?.getInt("letterId")!!
        _binding = FragmentDetailDocumentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // below code is used for
        // checking our permissions.
        if (checkPermission()) {
            Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        binding.fragmentDetailProgressBar.visibility = View.VISIBLE
        binding.fragmentDetailDocumentsMainLayout.visibility = View.GONE
        myViewModel.getLetter(letterId)
        log("$letterId letterId")
        getLetterResponse()

        binding.fragmentDetailDocumentsSendToAgreement.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("letterIdForDoc", letterId)
            documentsDialog.arguments = bundle
            documentsDialog.show(parentFragmentManager, "documentsDialog")
        }

        /*binding.fragmentDetailDocumentsDownloadPdf.setOnClickListener {
            generatePDF()
        }*/

    }

    private fun getLetterResponse() {
        with(myViewModel) {
            letterResponse.observe(viewLifecycleOwner, {
                binding.fragmentDetailProgressBar.visibility = View.GONE
                binding.fragmentDetailDocumentsMainLayout.visibility = View.VISIBLE

                if (it.code == 200 && it.payload != null) {
                    binding.fragmentDetailDocumentsIdText.text = it.payload!!.id.toString()
                    binding.fragmentDetailDocumentsNameText.text = it.payload!!.name
                    binding.fragmentDetailDocumentsSenderText.text = it.payload!!.sender
                    binding.fragmentDetailDocumentsTypeText.text = it.payload!!.docType!!.type
                    binding.fragmentDetailDocumentsRegistrationNumberText.text =
                        it.payload!!.registrationNumber
                    binding.fragmentDetailDocumentsEntryDateText.text = it.payload!!.entryDate
                    binding.fragmentDetailDocumentsOutgoingNumberText.text =
                        it.payload!!.outgoingNumber
                    binding.fragmentDetailDocumentsDistributionDateText.text =
                        it.payload!!.distributionDate
                    binding.fragmentDetailDocumentsContentText.text = it.payload!!.content
                } else {
                    Toast.makeText(requireContext(), "код ${it.code}", Toast.LENGTH_SHORT).show()
                }
                log("$it")
            })
            errorLetter.observe(viewLifecycleOwner, {
                binding.fragmentDetailProgressBar.visibility = View.GONE
                binding.fragmentDetailDocumentsMainLayout.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
                log("$it")
            })
        }
    }

    private fun checkPermission(): Boolean {

            val result =
                ContextCompat.checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE)
            val result1 =
                ContextCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE)
           return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED

    }
    private fun requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    READ_EXTERNAL_STORAGE
                ),  PERMISSION_REQUEST_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    WRITE_EXTERNAL_STORAGE,
                    READ_EXTERNAL_STORAGE
                ),  PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (writeStorage && readStorage) {
                    Toast.makeText(requireContext(), "Permission Granted..", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "Permission Denined.", Toast.LENGTH_SHORT)
                        .show()
                    //finish()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        log("onDestroyView")
    }

    private fun setUpViewModel() {
        myViewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(RetrofitBuilder().getApiInterface(requireContext()))
            ).get(
                MyViewModel::class.java
            )
    }



    private fun generatePDF() {
        // creating an object variable
        // for our PDF document.
        val pdfDocument = PdfDocument()

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        val title = Paint()

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        val mypageInfo = PageInfo.Builder(pagewidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        val myPage = pdfDocument.startPage(mypageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        val canvas: Canvas = myPage.canvas


        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.textSize = 16f

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.color = ContextCompat.getColor(requireContext(), R.color.black)

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("A portal for IT professionals.", 209f, 100f, title)
        canvas.drawText("Geeks for Geeks", 209f, 80f, title)
        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.color = ContextCompat.getColor(requireContext(), R.color.black)
        title.textSize = 15f

        // below line is used for setting
        // our text to center of PDF.
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("This is sample document which we have created.", 396f, 560f, title)

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage)


        val file = File(Environment.getExternalStorageDirectory(), "Docis.pdf")
        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(FileOutputStream(file))

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(
                requireContext(),
                "PDF file generated successfully.",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: IOException) {
            // below line is used
            // to handle error
            e.printStackTrace()
        }

        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close()
    }


}