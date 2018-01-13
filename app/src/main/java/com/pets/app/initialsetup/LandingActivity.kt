package com.pets.app.initialsetup

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.pets.app.R
import com.pets.app.activities.ProfileActivity
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.DialogManager
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.app_toolbar.*

class LandingActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private var tvUserName: TextView? = null
    private var imgProfile: ImageView? = null
    private var linHeader: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        isHeaderImage = true
        initializeToolbar("")
        setUpDrawerMenu()
        initView()
    }

    private fun setUpDrawerMenu() {

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun initView() {

        tvUserName = findViewById(R.id.tvName)
        imgProfile = findViewById(R.id.imageView)
        linHeader = nav_view.findViewById(R.id.nav_header)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val header = headerView.findViewById<LinearLayout>(R.id.nav_header)

        header?.setOnClickListener(this)
        imgHeader?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.nav_header -> {
                val mIntent = Intent(this, ProfileActivity::class.java)
                this.startActivity(mIntent)
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.imgHeader -> {
                val mIntent = Intent(this, ProfileActivity::class.java)
                this.startActivity(mIntent)
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
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_mypets -> {

            }
            R.id.nav_mymatches -> {

            }
            R.id.nav_my_interests -> {

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
}
