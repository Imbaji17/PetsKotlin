package com.pets.app.initialsetup

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.pets.app.R
import com.pets.app.common.AppPreferenceManager
import com.pets.app.common.DialogManager
import kotlinx.android.synthetic.main.activity_landing.*

class LandingActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        isHeaderImage = true
        initializeToolbar("")
        initView()
    }

    private fun initView() {

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
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
                DialogManager.showDialogWithYesAndNo(this, this.getString(R.string.are_you_sure_you_want_to_exit), DialogInterface.OnClickListener { dialogInterface, i ->
                    AppPreferenceManager.saveUser(null)
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
