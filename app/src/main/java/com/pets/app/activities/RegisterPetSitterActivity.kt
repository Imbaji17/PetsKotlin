package com.pets.app.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.pets.app.R
import com.pets.app.adapters.PhotosAdapter
import com.pets.app.common.ApplicationsConstants
import com.pets.app.initialsetup.SelectCountryCodeActivity
import com.pets.app.interfaces.AddPhotoCallback
import com.pets.app.model.PetsType
import com.pets.app.model.`object`.PhotosInfo
import com.pets.app.utilities.ImagePicker
import com.pets.app.utilities.Logger
import com.pets.app.utilities.Utils
import com.pets.app.widgets.RangeSeekBar

class RegisterPetSitterActivity : ImagePicker(), View.OnClickListener {

    private var edtContactPerson: EditText? = null
    private var edtCountryCode: EditText? = null
    private var edtContact: EditText? = null
    private var edtAddress: EditText? = null
    private var rsbDistance: RangeSeekBar<Number>? = null
    private var edtType: EditText? = null
    private var etDescription: EditText? = null
    private var btnSave: Button? = null
    private lateinit var latitude: String
    private lateinit var longitude: String
    private val RC_COUNTRY_CODE: Int = 100
    private val RC_AUTOCOMPLETE: Int = 200
    private val RC_TYPE: Int = 300
    private var petsTypeId: String? = ""
    private var adapter: PhotosAdapter? = null
    private var photoList: ArrayList<Any>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_pet_sitter)

        initializeToolbar(this.getString(R.string.register_as_pet_sitter))
        initView()
        clickListeners()
    }

    private fun initView() {

        edtContactPerson = findViewById(R.id.edtContactPerson)
        edtCountryCode = findViewById(R.id.edtCountryCode)
        edtContact = findViewById(R.id.edtContact)
        edtAddress = findViewById(R.id.edtAddress)
        rsbDistance = findViewById(R.id.rsbDistance)
        edtType = findViewById(R.id.edtType)
        etDescription = findViewById(R.id.etDescription)
        btnSave = findViewById(R.id.btnSave)

        mRecyclerView = findViewById(R.id.recyclerView)
        val mGridLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        mGridLayoutManager.orientation = GridLayoutManager.HORIZONTAL
        mRecyclerView?.layoutManager = mGridLayoutManager

        adapter = PhotosAdapter(this, photoList, false)
        mRecyclerView?.adapter = adapter
        adapter?.setItemClickListener(object : AddPhotoCallback {
            override fun onAddPhotoClick(position: Int) {
                showTakeImagePopup()
            }

            override fun onDeleteClick(position: Int) {
                if (position != -1 && position < photoList!!.size) {
                    val photo = photoList!!.get(position) as PhotosInfo
                    if (photoList!!.get(photoList!!.size - 1) is PhotosInfo) {
                        photoList!!.add(getString(R.string.add_photo))
                    }
                    if (photo.url.contains("http")) {
//                        if (Utils.isOnline(this@RegisterPetSitterActivity))
//                            deletePetImageApiCall(photo, position)
                    } else {
                        photoList!!.removeAt(position)
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }
        })

        rsbDistance!!.selectedMaxValue = 0

        photoList!!.add("")
        adapter!!.notifyDataSetChanged()
    }

    private fun clickListeners() {

        edtCountryCode?.setOnClickListener(this)
        edtAddress?.setOnClickListener(this)
        edtType?.setOnClickListener(this)
        btnSave?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.edtCountryCode -> {
                val countryCode = Intent(this, SelectCountryCodeActivity::class.java)
                this.startActivityForResult(countryCode, RC_COUNTRY_CODE)
            }
            R.id.edtAddress -> {
                openAutocompleteActivity()
            }
            R.id.edtType -> {
                SelectTypeActivity.startActivity(this, RC_TYPE, petsTypeId!!, 0, "")
            }
            R.id.btnSave -> {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_CROP_ACTIVITY -> {
                if (data != null) {
                    val result = com.theartofdev.edmodo.cropper.CropImage.getActivityResult(data)
                    val mCurrentPhotoPath = result.uri.path
                    val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
                    val photo = PhotosInfo()
                    photo.url = mCurrentPhotoPath
                    photoList?.size?.minus(1)?.let { photoList!!.add(it, photo) }
                    if (photoList!!.size >= 3) {
                        photoList!!.removeAt(photoList!!.size - 1)
                    }
                    adapter!!.notifyDataSetChanged()
                }
            }
            RC_AUTOCOMPLETE -> {
                if (resultCode == RESULT_OK) {
                    // Get the user's selected place from the Intent.
                    val place = PlaceAutocomplete.getPlace(this, data)
                    Log.i("TAG", "Place Selected: " + place.name)

                    val latLng = place.latLng
                    latitude = latLng.latitude.toString()
                    longitude = latLng.longitude.toString()
                    edtAddress?.setText(place.address)

                    Logger.errorLog(place.id + "\n" + place.placeTypes + "\n" + place.address + "\n" + place.locale)
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    val status = PlaceAutocomplete.getStatus(this, data)
                    Log.e("TAG", "Error: Status = " + status.toString())
                } else if (resultCode == RESULT_CANCELED) {
                    // Indicates that the activity closed before a selection was made. For example if
                    // the user pressed the back button.
                }
            }
            RC_COUNTRY_CODE -> {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        val countryCode: String = data.getStringExtra(ApplicationsConstants.DATA)
                        edtCountryCode?.setText("+".plus(countryCode))
                    }
                }
            }
            RC_TYPE -> {
                if (data != null) {
                    val petsType = data.getSerializableExtra(ApplicationsConstants.DATA) as PetsType
                    if (petsType != null) {
                        petsTypeId = petsType.petsTypeId
                        edtType!!.setText(petsType.typeName)
                    }
                }
            }
        }
    }

    private fun openAutocompleteActivity() {

        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this)
            startActivityForResult(intent, RC_AUTOCOMPLETE)
        } catch (e: GooglePlayServicesRepairableException) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.connectionStatusCode,
                    0 /* requestCode */).show()
        } catch (e: GooglePlayServicesNotAvailableException) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            val message = "Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode)

            Log.e("Tag", message)
            Utils.showToast(message)
        }
    }
}
