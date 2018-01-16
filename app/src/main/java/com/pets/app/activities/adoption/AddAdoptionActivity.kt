package com.pets.app.activities.adoption

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.pets.app.R
import com.pets.app.initialsetup.BaseActivity

class AddAdoptionActivity : BaseActivity(), View.OnClickListener {


    private var ivProfile: ImageView? = null
    private var etPetName: EditText? = null
    private var rlType: RelativeLayout? = null
    private var tvType: TextView? = null
    private var rlBreed: RelativeLayout? = null
    private var tvBreed: TextView? = null
    private var etAge: EditText? = null
    private var rgGender: RadioGroup? = null
    private var btnUpload: Button? = null
    private var etDescription: EditText? = null
    private var btnAddAdoption: Button? = null

    companion object {
        private val TAG = AddAdoptionActivity::class.java.simpleName
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, AddAdoptionActivity::class.java)
//            intent.putExtra(ApplicationsConstants.PETS_TYPE_ID, petsTypeId)
//            intent.putExtra(ApplicationsConstants.PETS_TYPE_NAME, petsTypeStr)
//            intent.putExtra(ApplicationsConstants.BREED_ID, breedId)
//            intent.putExtra(ApplicationsConstants.BREED_NAME, breedStr)
//            intent.putExtra(ApplicationsConstants.GENDER, gender)
//            intent.putExtra(ApplicationsConstants.DISTANCE, distance)
//            intent.putExtra(ApplicationsConstants.LATITUDE, latitude)
//            intent.putExtra(ApplicationsConstants.LONGITUDE, longitude)
//            intent.putExtra(ApplicationsConstants.LOCATION, location)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_adoption)
        initializeToolbar(getString(R.string.add_adoption))
        initView()
    }

    fun initView() {
        ivProfile = findViewById(R.id.ivProfile)
        etPetName = findViewById(R.id.etPetName)
        rlType = findViewById(R.id.rlType)
        tvType = findViewById(R.id.tvType)
        rlBreed = findViewById(R.id.rlBreed)
        tvBreed = findViewById(R.id.tvBreed)
        etAge = findViewById(R.id.etAge)
        rgGender = findViewById(R.id.rgGender)
        btnUpload = findViewById(R.id.btnUpload)
        etDescription = findViewById(R.id.etDescription)
        btnAddAdoption = findViewById(R.id.btnAddAdoption)

        btnAddAdoption!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnAddAdoption -> {

            }
        }
    }
}
