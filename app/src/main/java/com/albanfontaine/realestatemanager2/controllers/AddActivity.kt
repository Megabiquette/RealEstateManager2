package com.albanfontaine.realestatemanager2.controllers

import android.app.Activity
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.models.Media
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.toolbar.*

class AddActivity : AppCompatActivity() {

    private lateinit var medias: List<Media>
    lateinit var mediaDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        configureToolbar()
        configureSpinner()
        configureDialog(this)

        val gson = Gson()

        val addMediaButton = add_activity_add_media_button
        val addPropertyButton = add_activity_add_property_button

        addMediaButton.setOnClickListener{mediaDialog.show()}

    }

    private fun setMediasText(medias: List<Media>){
        var numberMediasAdded = resources.getQuantityString(R.plurals.property_medias_added, medias.size)
        add_activity_media_added_textView.text = numberMediasAdded
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

    private fun configureDialog(activity: Activity){
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

    private fun getPhotoFromGallery(){
        Log.e("dialog", "from gallery")
    }

    private fun takePhoto(){
        Log.e("dialog", "take photo")
        if(!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            // No cam on device
            Toast.makeText(applicationContext, R.string.media_dialog_no_cam, Toast.LENGTH_LONG).show()
        }else{

        }
    }
}
