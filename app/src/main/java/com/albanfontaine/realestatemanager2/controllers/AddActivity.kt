package com.albanfontaine.realestatemanager2.controllers

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.database.AppDatabase
import com.albanfontaine.realestatemanager2.models.Media
import com.albanfontaine.realestatemanager2.models.Property
import com.albanfontaine.realestatemanager2.utils.Constants
import com.albanfontaine.realestatemanager2.utils.Utils
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.media_description.view.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AddActivity : BaseActivity() {
    private var mMedias: ArrayList<Media> = ArrayList()
    private lateinit var mMediaDialog: AlertDialog
    private lateinit var mDeleteMediasDialog: AlertDialog
    private lateinit var mCurrentMediaPath: String

    // Property to be EDITED only
    private var mPropertyId: Long? = null
    private var mProperty: Property? = null

    private var mDb: AppDatabase? = null

    private var mPropType: String? = null
    private var mPropPrice: Int? = null
    private var mPropSurface: Int? = null
    private var mPropRoomNb: Int? = null
    private var mPropDescription: String? = null
    private var mPropAddress: String? = null
    private var mPropNeighborhood: String? = null
    private var mPropPOI: String? = null
    private var mPropAvailable: Boolean = true
    private var mPropMarketEntryDate: Int? = null
    private var mPropSellDate: Int? = null
    private var mPropAgent: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        configureToolbar()
        configureSpinner()
        configureMediaDialogs(this)

        val addMediaButton = add_activity_add_media_button
        val deleteMediasButton = add_activity_delete_medias_button
        val addPropertyButton = add_activity_add_property_button

        addMediaButton.setOnClickListener{mMediaDialog.show()}
        deleteMediasButton.setOnClickListener{mDeleteMediasDialog.show()}
        addPropertyButton.setOnClickListener{addProperty()}

        // Checks if the user wants to edit a property
        val extras: Bundle? = intent.extras
        if(extras != null){
            mPropertyId = intent.getLongExtra(Constants.PROPERTY_ID, 0)
            title = resources.getString(R.string.edit_property)
            addPropertyButton.text = this.resources.getString(R.string.edit_property)
            getPropertyToEdit()
        }
        setMediasText()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            var imageUri: Uri? = null
            when(requestCode){
                Constants.GALLERY_REQUEST_CODE -> {
                    imageUri = data?.data
                    mCurrentMediaPath = imageUri.toString()
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                        baseContext.contentResolver.takePersistableUriPermission(imageUri!!, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    }
                }
                Constants.CAMERA_REQUEST_CODE -> {
                    // Save to gallery
                    Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also{
                        mediaScanIntent ->
                        val f = File(mCurrentMediaPath)
                        imageUri = Uri.fromFile(f)
                        mCurrentMediaPath = imageUri.toString()
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
        val input: EditText = view.media_description_editText
        val image: ImageView = view.media_description_image
        image.setImageURI(imageUri)

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity, R.style.DialogTheme)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(view)
        builder.apply {
            setPositiveButton(R.string.media_dialog_ok,
                DialogInterface.OnClickListener { dialog, which ->
                    if(!input.text.toString().trim().equals("")){
                        mediaDescription = input.text.toString()
                        mMedias.add(Media(0, mCurrentMediaPath, mediaDescription, null))
                        setMediasText()
                    }else{
                        Toast.makeText(applicationContext, applicationContext.resources.getString(R.string.media_dialog_error_no_description), Toast.LENGTH_LONG).show()
                    }})
            setNegativeButton(R.string.media_dialog_cancel,
                DialogInterface.OnClickListener { dialog, which ->  dialog.cancel() })
        }
        builder.setTitle(R.string.media_dialog_add_description)
        builder.show()
    }

    private fun getPhotoFromGallery(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
			type = "image/*"
			addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
			addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
			addCategory(Intent.CATEGORY_OPENABLE)
		}
        startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE)
    }

    private fun takePhoto(){
        if(!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            // No cam on device
            Toast.makeText(applicationContext, R.string.media_dialog_no_cam, Toast.LENGTH_LONG).show()
        }else{
            try{
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
					addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
					addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
					putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(baseContext, "com.albanfontaine.realestatemanager2.provider", createImageFile()))
                }
                startActivityForResult(intent, Constants.CAMERA_REQUEST_CODE)
            } catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File{
        val timestamp: String? = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.FRANCE).format(Date())
        val storageDir: File? = Environment.getExternalStorageDirectory()
        return File.createTempFile(
            "JPEG_${timestamp}_",
            ".jpg",
            storageDir
        ).apply {
            mCurrentMediaPath = absolutePath
        }
    }

    private fun deleteMedias(){
        if(mPropertyId != null){
            // For EDIT only
            val executor: Executor = Executors.newSingleThreadExecutor()
            executor.execute{
				mDb = AppDatabase.getInstance(this)
				val i:Int? = mDb?.mediaDAO()?.deleteAllMedias(mPropertyId!!)
            }
        }
        mMedias = ArrayList()
        setMediasText()
    }

    private fun addProperty(){
        if(mMedias.size <= 0){
            // ERROR: no media added
            Toast.makeText(applicationContext, R.string.property_error_0_media, Toast.LENGTH_SHORT).show()
        }else{
            mDb = AppDatabase.getInstance(this)
            val executor: Executor = Executors.newSingleThreadExecutor()
            getForm()
            if (mPropertyId == null){
                // ADD the property
                val property = Property(0, mPropType, mPropPrice, mPropSurface, mPropRoomNb, mPropDescription, mPropAddress, mPropNeighborhood, mPropPOI, mPropAvailable, mPropMarketEntryDate, mPropSellDate, mPropAgent)
                executor.execute{
                    val propId: Long? = mDb?.propertyDAO()?.insertProperty(property)
                    for(media: Media in mMedias){
                        val mediaToAdd = Media(0, media.uri, media.description, propId)
                        mDb?.mediaDAO()?.insertMedia(mediaToAdd)
                    }
                    runOnUiThread{
                        Toast.makeText(applicationContext, R.string.property_added, Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            } else {
                // EDIT the property
                val property = Property(mProperty?.id!!, mPropType, mPropPrice, mPropSurface, mPropRoomNb, mPropDescription, mPropAddress,mPropNeighborhood, mPropPOI, mPropAvailable, mPropMarketEntryDate, mPropSellDate, mPropAgent)
                executor.execute{
                    mDb?.propertyDAO()?.updateProperty(property)
                    for(media: Media in mMedias){
                        if(media.associatedPropertyId == null){
                            val mediaToAdd = Media(0, media.uri, media.description, mProperty?.id!!)
                            mDb?.mediaDAO()?.insertMedia(mediaToAdd)
                        }
                    }
                    runOnUiThread{
                        Toast.makeText(applicationContext, R.string.property_edited, Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }
        }
    }

    private fun getForm(){
        mPropType = add_activity_type_spinner.selectedItem.toString().trim()
        mPropDescription = add_activity_description_editText.text.toString().trim()
        mPropAddress = add_activity_address_editText.text.toString().trim()
        mPropNeighborhood = add_activity_neighborhood_editText.text.toString().trim()
        mPropPOI = add_activity_POIs_editText.text.toString().trim()
        mPropAvailable = add_activity_available_checkBox.isChecked
        mPropAgent = add_activity_agent_editText.text.toString().trim()

        if(!add_activity_price_editText.text.toString().trim().equals("")){mPropPrice = (add_activity_price_editText.text.toString().trim()).toInt()}
        if(!add_activity_surface_editText.text.toString().trim().equals("")){mPropSurface = (add_activity_surface_editText.text.toString().trim()).toInt()}
        if(!add_activity_numberRooms_editText.text.toString().trim().equals("")){mPropRoomNb = (add_activity_numberRooms_editText.text.toString().trim()).toInt()}
        if(!add_activity_entry_date_editText.text.toString().trim().equals("")){mPropMarketEntryDate = (Utils.formatDateForDB(add_activity_entry_date_editText.text.toString().trim()))}
        if(!add_activity_sale_date_editText.text.toString().trim().equals("")){mPropSellDate = (Utils.formatDateForDB(add_activity_sale_date_editText.text.toString().trim())).toInt()}
    }

    ///////////////////
    // CONFIGURATION //
    ///////////////////

    // For EDIT only
    private fun getPropertyToEdit(){
        val db = AppDatabase.getInstance(this)
        val executor = Executors.newSingleThreadExecutor()
        executor.execute{
            mProperty = db?.propertyDAO()?.getProperty(mPropertyId!!)
            val listMedias: List<Media>? = db?.mediaDAO()?.getMedias(mPropertyId!!)
            mMedias = ArrayList(listMedias!!)
            this.runOnUiThread{
                setupFields()
                setMediasText()
            }
        }
    }

    // For EDIT only
    private fun setupFields(){
        val type: Int = when(mProperty?.type){
            this.resources.getString(R.string.flat) -> 0
            this.resources.getString(R.string.house) -> 1
            this.resources.getString(R.string.loft) -> 2
            else -> 3
        }
        add_activity_type_spinner.setSelection(type)

        if(!mProperty?.description.equals("")){ add_activity_description_editText.setText(mProperty?.description)}
        if(!mProperty?.pointsOfInterest.equals("")){ add_activity_POIs_editText.setText(mProperty?.pointsOfInterest)}
        if(!mProperty?.available!!){ add_activity_available_checkBox.isChecked = false}
        if(!mProperty?.agent.equals("")){ add_activity_agent_editText.setText(mProperty?.agent)}
        if(!mProperty?.address.equals("")){ add_activity_address_editText.setText(mProperty?.address)}
        if(!mProperty?.neighborhood.equals("")){ add_activity_neighborhood_editText.setText(mProperty?.neighborhood)}

        if(mProperty?.price != null){ add_activity_price_editText.setText(mProperty?.price.toString())}
        if(mProperty?.surface != null){ add_activity_surface_editText.setText(mProperty?.surface.toString())}
        if(mProperty?.roomNumber != null){ add_activity_numberRooms_editText.setText(mProperty?.roomNumber.toString())}
        if(mProperty?.marketEntryDate != null){ add_activity_entry_date_editText.setText(Utils.formatDateToText(mProperty?.marketEntryDate!!))}
        if(mProperty?.sellDate != null){add_activity_sale_date_editText.setText(Utils.formatDateToText(mProperty?.sellDate!!))}

    }

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

    private fun configureMediaDialogs(activity: Activity){
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

        val builder2: AlertDialog.Builder = AlertDialog.Builder(activity, R.style.DialogTheme)
        builder2.apply {
            setPositiveButton(R.string.media_dialog_ok,
                DialogInterface.OnClickListener{ dialog, which ->  deleteMedias() })
            setNegativeButton(R.string.media_dialog_cancel,
                DialogInterface.OnClickListener{dialog, which ->  dialog.cancel() })
        }
        builder2.setTitle(R.string.media_dialog_delete_medias_title)
        mDeleteMediasDialog = builder2.create()
    }

    private fun setMediasText(){
        add_activity_media_added_textView.text = resources.getQuantityString(R.plurals.property_medias_added, mMedias.size, mMedias.size)
    }
}
