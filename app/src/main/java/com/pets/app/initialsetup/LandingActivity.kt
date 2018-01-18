package com.pets.app.initialsetup

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.pets.app.R
import com.pets.app.activities.AddPetActivity
import com.pets.app.activities.FindHostelActivity
import com.pets.app.activities.FunZoneActivity
import com.pets.app.activities.ProfileActivity
import com.pets.app.activities.adoption.AdoptionListActivity
import com.pets.app.adapters.LandingImageAdapter
import com.pets.app.adapters.LandingMenuAdapter
import com.pets.app.common.*
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.model.PetResponse
import com.pets.app.model.`object`.LandingDetails
import com.pets.app.utilities.TimeStamp
import com.pets.app.utilities.Utils
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.app_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LandingActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SimpleItemClickListener {

    /*Drawer Menu*/
    private var tvUserName: TextView? = null
    private var imgProfile: ImageView? = null
    private var linHeader: LinearLayout? = null
    /*Landing*/
    private var btnRetry: Button? = null
    private var mainViewFlipper: ViewFlipper? = null
    private var viewFlipper: ViewFlipper? = null
    private var linAddPet: LinearLayout? = null
    private var viewPager: ViewPager? = null
    private var pageIndicator: CirclePageIndicator? = null
    private var tvName: TextView? = null
    private var tvBirthDate: TextView? = null
    private var mRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        isHeaderImage = true
        initializeToolbar("")
        setUpDrawerMenu()
        initView()
        checkValidations()
    }

    private fun checkValidations() {

        if (Utils.isOnline(this)) {
            myPetsApiCall()
        } else {
            mainViewFlipper?.displayedChild = 2
        }
    }

    private fun setUpDrawerMenu() {

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun initView() {

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val header = headerView.findViewById<LinearLayout>(R.id.nav_header)

        tvUserName = headerView.findViewById(R.id.tvName)
        imgProfile = headerView.findViewById(R.id.imageView)
        linHeader = nav_view.findViewById(R.id.nav_header)
        linAddPet = findViewById(R.id.linAddPet)

        /*Setting Data To View*/
        val user = AppPreferenceManager.getUser()
        if (!TextUtils.isEmpty(user.profile_image)) {
            ImageSetter.loadRoundedImage(this, user.profile_image, R.drawable.profile, imgProfile)
        }
        if (!TextUtils.isEmpty(user.name)) {
            tvUserName?.text = user.name
        }

        btnRetry = findViewById(R.id.btnRetry)
        mainViewFlipper = findViewById(R.id.mainViewFlipper)
        viewFlipper = findViewById(R.id.viewFlipper)
        viewPager = findViewById(R.id.viewPager)
        pageIndicator = findViewById(R.id.cvp)
        tvName = findViewById(R.id.tvName)
        tvBirthDate = findViewById(R.id.tvBirthDate)
        mRecyclerView = findViewById(R.id.recyclerView)

        val mGridLayoutManager = GridLayoutManager(this, 3)
        mGridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView?.isNestedScrollingEnabled = false
        mRecyclerView?.layoutManager = mGridLayoutManager

        val mList = ArrayList<LandingDetails>()
        mList.add(object : LandingDetails(this.getString(R.string.my_pets), R.drawable.mypets1) {})
        mList.add(object : LandingDetails(this.getString(R.string.fun_zone), R.drawable.funzone) {})
        mList.add(object : LandingDetails(this.getString(R.string.find_match), R.drawable.find_match) {})
        mList.add(object : LandingDetails(this.getString(R.string.find_hostel), R.drawable.find_hostel) {})
        mList.add(object : LandingDetails(this.getString(R.string.pet_sitters), R.drawable.pet_sitter) {})
        mList.add(object : LandingDetails(this.getString(R.string.find_vet), R.drawable.find_vet) {})
        mList.add(object : LandingDetails(this.getString(R.string.adoption), R.drawable.adoption) {})
        mList.add(object : LandingDetails(this.getString(R.string.buy_and_sell), R.drawable.buyandsell) {})
        mList.add(object : LandingDetails(this.getString(R.string.buddy_alert), R.drawable.alert1) {})

        val adapter = LandingMenuAdapter(this, mList)
        mRecyclerView?.adapter = adapter
        adapter.setItemClick(this)

        header?.setOnClickListener(this)
        imgHeader?.setOnClickListener(this)
        linAddPet?.setOnClickListener(this)
        btnRetry?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.nav_header -> {
                val mIntent = Intent(this, ProfileActivity::class.java)
                this.startActivity(mIntent)
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.imgHeader -> {
                val mIntent = Intent(this, AddPetActivity::class.java)
                this.startActivity(mIntent)
            }
            R.id.linAddPet -> {
                val mIntent = Intent(this, AddPetActivity::class.java)
                this.startActivity(mIntent)
            }
            R.id.btnRetry -> {
                checkValidations()
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_chat, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_chat -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {

            }
            R.id.nav_mypets -> {

            }
            R.id.nav_mymatches -> {

            }
            R.id.nav_my_interests -> {
//                MyInterestActivity.startActivity(this)
            }
            R.id.nav_sent_feedback -> {

            }
            R.id.nav_logout -> {
                DialogManager.showDialogWithYesAndNo(this, this.getString(R.string.are_you_sure_you_want_to_exit), { dialogInterface, i ->
                    AppPreferenceManager.saveUser(null)
                    AppPreferenceManager.setSignIn(false)
                    val mIntent = Intent(this, LoginActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    this.startActivity(mIntent)
                })
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onItemClick(`object`: Any?) {

        when (`object` as Int) {

            0 -> {
                val mIntent = Intent(this, ProfileActivity::class.java)
                this.startActivity(mIntent)
            }
            1 -> {
                FunZoneActivity.startActivity(this)
            }
            2 -> {
//                val mIntent = Intent(this, ProfileActivity::class.java)
//                this.startActivity(mIntent)
            }
            3 -> {
                val mIntent = Intent(this, FindHostelActivity::class.java)
                this.startActivity(mIntent)
            }
            4 -> {
                val mIntent = Intent(this, ProfileActivity::class.java)
                this.startActivity(mIntent)
            }
            5 -> {
//                val mIntent = Intent(this, ProfileActivity::class.java)
//                this.startActivity(mIntent)
            }
            6 -> {
                val mIntent = Intent(this, AdoptionListActivity::class.java)
                this.startActivity(mIntent)
            }
            7 -> {
//                val mIntent = Intent(this, ProfileActivity::class.java)
//                this.startActivity(mIntent)
            }
            8 -> {
//                val mIntent = Intent(this, ProfileActivity::class.java)
//                this.startActivity(mIntent)
            }
        }
    }

    private fun myPetsApiCall() {

        val offset = "0"
        val userId = AppPreferenceManager.getUserID()
        val language = Enums.Language.EN.name.toUpperCase()
        val timeStamp = TimeStamp.getTimeStamp()
        val key = TimeStamp.getMd5(timeStamp + userId + Constants.TIME_STAMP_KEY)

        showProgressBar()
        mainViewFlipper?.displayedChild = 0
        val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
        val call = apiClient.myPetsList(userId, timeStamp, key, language, offset)
        call.enqueue(object : Callback<PetResponse> {
            override fun onResponse(call: Call<PetResponse>, response: Response<PetResponse>?) {
                mainViewFlipper?.displayedChild = 1
                hideProgressBar()
                if (response != null) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            checkResponse(response.body())
                        }
                    } else {
                        Utils.showErrorToast(response.errorBody())
                    }
                }
            }

            override fun onFailure(call: Call<PetResponse>, t: Throwable) {
                mainViewFlipper?.displayedChild = 1
                hideProgressBar()
            }
        })
    }

    private fun checkResponse(petResponse: PetResponse?) {

        if (petResponse!!.list != null && petResponse.list.isNotEmpty()) {
            viewFlipper?.displayedChild = 1

            val adapter = LandingImageAdapter(this, petResponse.list)
            viewPager?.adapter = adapter
            pageIndicator?.setViewPager(viewPager)

            tvName?.text = petResponse.list[0].pet_name
            tvBirthDate?.text = this.getString(R.string.birthday).plus(petResponse.list[0].dob)

            viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    tvName?.text = petResponse.list[position].pet_name
                    tvBirthDate?.text = this@LandingActivity.getString(R.string.birthday).plus(petResponse.list[position].dob)
                }
            })

        } else {
            viewFlipper?.displayedChild = 0
        }

    }
}
