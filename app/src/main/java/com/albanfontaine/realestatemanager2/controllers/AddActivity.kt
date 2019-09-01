package com.albanfontaine.realestatemanager2.controllers

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.models.Media
import com.albanfontaine.realestatemanager2.models.Property
import com.albanfontaine.realestatemanager2.utils.Constants
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {

    private var medias: ArrayList<Media> = ArrayList()
    private lateinit var mediaDialog: AlertDialog
    private lateinit var currentMediaPath: String

    // Form
    private var propType: String? = null
    private var propPrice: Int? = null
    private var propSurface: Double? = null
    private var propRoomNb: Int? = null
    private var propDescription: String? = null
    private var propAddress: String? = null
    private var propPOI: String? = null
    private var propAvailable: Boolean? = null
    private var propMarketEntryDate: String? = null
    private var propSellDate: String? = null
    private var propAgent: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)


        configureToolbar()
        configureSpinner()
        configureMediaDialog(this)
        setMediasText()

        val addMediaButton = add_activity_add_media_button
        val addPropertyButton = add_activity_add_property_button

        addMediaButton.setOnClickListener{mediaDialog.show()}
        addPropertyButton.setOnClickListener{addProperty()}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var imageUri: Uri? = null
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                Constants.GALLERY_REQUEST_CODE -> {
                    imageUri = data?.data
                    currentMediaPath = imageUri.toString()
                }
                Constants.CAMERA_REQUEST_CODE -> {
                    imageUri = data?.data
                    // Save to gallery
                    Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also{
                        mediaScanIntent ->
                        val f = File(currentMediaPath)
                        mediaScanIntent.data = imageUri
                        sendBroadcast(mediaScanIntent)
                    }
                }
            }

            // Add description to media
            lateinit var mediaDescription: String
            showMediaDescriptionDialog(this, imageUri)
        }
    }

    private fun showMediaDescriptionDialog(activity: Activity, imageUri: Uri?){
        lateinit var mediaDescription: String

        val inflater: LayoutInflater = LayoutInflater.from(activity)
        val view: View = inflater.inflate(R.layout.media_description, null)
        val input: EditText = view.findViewById(R.id.media_description_editText)
        val image: ImageView = view.findViewById(R.id.media_description_image)
        image.setImageURI(imageUri)

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity, R.style.DialogTheme)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(view)
        builder.apply {
            setPositiveButton(R.string.media_dialog_ok,
                DialogInterface.OnClickListener { dialog, which -> mediaDescription = input.text.toString()
                    medias.add(Media(0, currentMediaPath, mediaDescription, null))
                    setMediasText()})
            setNegativeButton(R.string.media_dialog_cancel,
                DialogInterface.OnClickListener { dialog, which ->  dialog.cancel() })
        }
        builder.setTitle(R.string.media_dialog_add_description)
        builder.show()
    }

    private fun getPhotoFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type ="image/*"
        val mimeTypes = arrayOf("images/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE)
    }

    private fun takePhoto(){
        if(!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            // No cam on device
            Toast.makeText(applicationContext, R.string.media_dialog_no_cam, Toast.LENGTH_LONG).show()
        }else{
            try{
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, "com.albanfontaine.realestatemanager2.fileprovider", createImageFile()))
                startActivityForResult(intent, Constants.CAMERA_REQUEST_CODE)
            } catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File{
        val timestamp: String? = SimpleDateFormat("ddMMyyyy", Locale.FRANCE).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timestamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentMediaPath = absolutePath
        }
    }

    private fun checkForm(): Boolean{
       return true
    }

    private fun addProperty(){
        if(checkForm()){
            val property = Property(0, propType, propPrice, propSurface, propRoomNb, propDescription, propAddress, propPOI, propAvailable, propMarketEntryDate, propSellDate, propAgent)
        }
    }

    ///////////////////
    // CONFIGURATION //
    ///////////////////

    private fun configureToolbar(){
        setSupportActionBar(toolbar as Toolbar)
        val ab : ActionBar? = getSupportActionBar()
        ab?.setDisplayHomeAsUpEnabled(true)
    }

    private fun configureSpinner(){
        val spinner: Spinner = add_activity_type_spinner
        ArrayAdapter.createFromResource(this, R.array.property_types, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
    }

    private fun configureMediaDialog(activity: Activity){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity, R.style.DialogTheme)
        builder.apply {
            setPositiveButton(R.string.media_dialog_gallery,
                DialogInterface.OnClickListener { dialog, which -> getPhotoFromGallery() })
            setNeutralButton(R.string.media_dialog_take_photo,
                DialogInterface.OnClickListener { dialog, which ->  takePhoto()})
        }
        builder.setMessage(R.string.media_dialog_message)
            .setTitle(R.string.media_dialog_title)
        mediaDialog = builder.create()
    }

    private fun setMediasText(){
        add_activity_media_added_textView.text = resources.getQuantityString(R.plurals.property_medias_added, medias.size, medias.size)
    }
}
