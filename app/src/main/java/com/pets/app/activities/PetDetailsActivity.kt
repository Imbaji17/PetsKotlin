package com.pets.app.activities

import android.os.Bundle
import com.pets.app.R
import com.pets.app.initialsetup.BaseActivity
import kotlinx.android.synthetic.main.app_toolbar.*

class PetDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_details)

        isHeaderImage = true
        imgHeader.setImageResource(R.drawable.logo_header)
        initializeToolbar("")

    }
}
