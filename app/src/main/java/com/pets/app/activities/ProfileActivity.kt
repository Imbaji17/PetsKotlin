package com.pets.app.activities

import android.os.Bundle
import com.pets.app.R
import com.pets.app.initialsetup.BaseActivity

class ProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initializeToolbar(this.getString(R.string.profile))
    }
}
