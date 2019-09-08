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
import com.albanfontaine.realestatemanager2.database.AppDatabase
import com.albanfontaine.realestatemanager2.database.MediaDAO
import com.albanfontaine.realestatemanager2.database.PropertyDAO
import com.albanfontaine.realestatemanager2.models.Media
import com.albanfontaine.realestatemanager2.models.Property
import com.albanfontaine.realestatemanager2.utils.Constants
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AddActivity : AppCompatActivity() {
    private var mMedias: ArrayList<Media> = ArrayList()
    private lateinit var mMediaDialog: AlertDialog
    private lateinit var mCurrentMediaPath: String

    // DB
    private var mDb: AppDatabase? = null
    private var mPropertyDAO: PropertyDAO? = null
    private var mMediaDAO: MediaDAO? = null

    // Form
    private var mPropType: String? = null
    private var mPropPrice: Int? = null
    private var mPropSurface: Double? = null
    private var mPropRoomNb: Int? = null
    private var mPropDescription: String? = null
    private var mPropLocation: String? = null
    private var mPropPOI: String? = null
    private var mPropAvailable: Boolean? = null
    private var mPropMarketEntryDate: String? = null
    private var mPropSellDate: String? = null
    private var mPropAgent: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        configureToolbar()
        configureSpinner()
        configureMediaDialog(this)
        configureDatabase()
        setMediasText()

        val addMediaButton = add_activity_add_media_button
        val addPropertyButton = add_activity_add_property_button

        addMediaButton.setOnClickListener{mMediaDialog.show()}
        addPropertyButton.setOnClickListener{addProperty()}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var imageUri: Uri? = null
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                Constants.GALLERY_REQUEST_CODE -> {
                    imageUri = data?.data
                    mCurrentMediaPath = imageUri.toString()
                }
                Constants.CAMERA_REQUEST_CODE -> {
                    imageUri = data?.data
                    // Save to gallery
                    Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also{
                        mediaScanIntent ->
                        val f = File(mCurrentMediaPath)
                        mediaScanIntent.data = imageUri
                        sendBroadcast(mediaScanIntent)
                    }
                }
            }

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
                    mMedias.add(Media(0, mCurrentMediaPath, mediaDescription, null))
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
            mCurrentMediaPath = absolutePath
        }
    }

    private fun addProperty(){
        if(mMedias.size > 0){
            getForm()
            val property = Property(0, mPropType, mPropPrice, mPropSurface, mPropRoomNb, mPropDescription, mPropLocation, mPropPOI, mPropAvailable, mPropMarketEntryDate, mPropSellDate, mPropAgent)
            mDb = AppDatabase.getInstance(this)
            val executor: Executor = Executors.newSingleThreadExecutor()

            executor.execute{
                Log.e("executor", "Executed")
                val propId: Long? = mDb?.propertyDAO()?.insertProperty(property)
                Log.e("propId", propId.toString())
                for(media: Media in mMedias){
                    val mediaToAdd: Media = Media(0, media.uri, media.description, propId)
                    mMediaDAO?.insertMedia(mediaToAdd)
                }
            }
        } else{
            Toast.makeText(applicationContext, R.string.property_error_0_media, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getForm(){
        mPropType = add_activity_type_spinner.selectedItem.toString().trim()
        mPropPrice = add_activity_price_editText.text.toString().trim().toInt()
        mPropSurface = add_activity_surface_editText.text.toString().trim().toDouble()
        mPropRoomNb = add_activity_numberRooms_editText.text.toString().trim().toInt()
        mPropDescription = add_activity_description_editText.text.toString().trim()
        mPropLocation = add_activity_location_editText.text.toString().trim()
        mPropPOI = add_activity_POIs_editText.text.toString().trim()
        mPropAvailable = add_activity_available_checkBox.isChecked
        mPropMarketEntryDate = add_activity_entry_date_editText.text.toString().trim()
        mPropSellDate = add_activity_sale_date_editText.text.toString().trim()
        mPropAgent = add_activity_agent_editText.text.toString().trim()
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
        mMediaDialog = builder.create()
    }

    private fun configureDatabase(){
        mDb = AppDatabase.getInstance(this)
        mPropertyDAO = mDb?.propertyDAO()
        mMediaDAO =mDb?.mediaDAO()
    }

    private fun setMediasText(){
        add_activity_media_added_textView.text = resources.getQuantityString(R.plurals.property_medias_added, mMedias.size, mMedias.size)
    }
}
