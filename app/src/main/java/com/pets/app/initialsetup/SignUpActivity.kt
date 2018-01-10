package com.pets.app.initialsetup

import android.os.Bundle
import com.pets.app.R

class SignUpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initializeToolbar(this.getString(R.string.title_activity_sign_up))
    }

}
