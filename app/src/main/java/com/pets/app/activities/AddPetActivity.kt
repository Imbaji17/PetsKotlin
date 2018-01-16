package com.pets.app.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import com.pets.app.R
import com.pets.app.adapters.PhotosAdapter
import com.pets.app.common.ApplicationsConstants
import com.pets.app.common.ImageSetter
import com.pets.app.interfaces.AddPhotoCallback
import com.pets.app.model.`object`.PhotosInfo
import com.pets.app.utilities.DateFormatter
import com.pets.app.utilities.DatePickerDialogFragment
import com.pets.app.utilities.ImagePicker
import java.io.File

class AddPetActivity : ImagePicker(), View.OnClickListener {

    private var imgPet: ImageView? = null
    private var edtName: EditText? = null
    private var edtType: EditText? = null
    private var edtBreed: EditText? = null
    private var edtDOB: EditText? = null
    private var radioGender: RadioGroup? = null
    private var btnUpload: Button? = null
    private var edtDesc: EditText? = null
    private var mRecyclerView: RecyclerView? = null
    private var btnAddPet: Button? = null
    private var adapter: PhotosAdapter? = null
    private var photoList: ArrayList<Any>? = null
    private var selectedType: Int = 0
    private val RC_BREED: Int = 200
    private val RC_TYPE: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        initializeToolbar(this.getString(R.string.add_pet))
        initView()
        clickListeners()
    }

    private fun initView() {

        photoList = ArrayList()

        imgPet = findViewById(R.id.imgView)
        edtName = findViewById(R.id.edtName)
        edtType = findViewById(R.id.edtType)
        edtBreed = findViewById(R.id.edtBreed)
        edtDOB = findViewById(R.id.edtDOB)
        radioGender = findViewById(R.id.radioGender)
        btnUpload = findViewById(R.id.btnUpload)
        edtDesc = findViewById(R.id.edtDescription)
        mRecyclerView = findViewById(R.id.recyclerView)
        btnAddPet = findViewById(R.id.btnAddPet)

        val mGridLayoutManager = GridLayoutManager(this, 3)
        mGridLayoutManager.orientation = GridLayoutManager.VERTICAL
        mRecyclerView?.layoutManager = mGridLayoutManager

        adapter = PhotosAdapter(this, photoList, false)
        mRecyclerView?.adapter = adapter
        adapter?.setItemClickListener(object : AddPhotoCallback {
            override fun onAddPhotoClick(position: Int) {
                selectedType = 2
                showTakeImagePopup()
            }

            override fun onDeleteClick(position: Int) {
                if (position != -1 && position < photoList!!.size) {
                    val photo = photoList!!.get(position) as PhotosInfo
                    if (photoList!!.get(photoList!!.size - 1) is PhotosInfo) {
                        photoList!!.add(getString(R.string.add_photo))
                    }
                    if (photo.url.contains("http")) {
//                        if (Utils.isOnline(this@AddPetActivity))
//                            deleteImageApiCall(position)
                    } else {
                        photoList!!.removeAt(position)
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }
        })

        photoList!!.add("")
        adapter!!.notifyDataSetChanged()
    }

    private fun clickListeners() {

        imgPet?.setOnClickListener(this)
        edtType?.setOnClickListener(this)
        edtBreed?.setOnClickListener(this)
        edtDOB?.setOnClickListener(this)
        btnUpload?.setOnClickListener(this)
        btnAddPet?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.imgView -> {
                selectedType = 1
                showTakeImagePopup()
            }
            R.id.edtType -> {
                val mIntent = Intent(this, SelectTypeActivity::class.java)
                mIntent.putExtra(ApplicationsConstants.NAVIGATION_TYPE, true)
                this.startActivityForResult(mIntent, RC_TYPE)
            }
            R.id.edtBreed -> {
                val mIntent = Intent(this, SelectTypeActivity::class.java)
                mIntent.putExtra(ApplicationsConstants.NAVIGATION_TYPE, false)
                this.startActivityForResult(mIntent, RC_BREED)
            }
            R.id.edtDOB -> {
                DatePickerDialogFragment(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val date = "$dayOfMonth-$month-$year"
                    val strDate = DateFormatter.getFormattedDate(DateFormatter.dd_MM_yyyy_str, date, DateFormatter.dd_MMM_yyyy_str)
                    edtDOB?.setText(strDate)
                }).show(supportFragmentManager, this.getString(R.string.select_date))
            }
            R.id.btnUpload -> {
                selectedType = 3
                showTakeImagePopup()
            }
            R.id.btnAddPet -> {
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
                    updatedImageFile = File(mCurrentPhotoPath)
                    if (updatedImageFile.exists()) {
                        imageFlag = 1
                        if (selectedType == 1) {
                            ImageSetter.loadRoundedImage(this, updatedImageFile, R.drawable.profile, imgPet)
                        } else if (selectedType == 2) {
                            val photo = PhotosInfo()
                            photo.url = mCurrentPhotoPath
                            photoList?.size?.minus(1)?.let { photoList!!.add(it, photo) }
                            if (photoList!!.size >= 3) {
                                photoList!!.removeAt(photoList!!.size - 1)
                            }
                            adapter!!.notifyDataSetChanged()
                        } else {

                        }
                    }
                }
            }
            RC_TYPE -> {

            }
            RC_BREED -> {

            }
        }
    }
}
