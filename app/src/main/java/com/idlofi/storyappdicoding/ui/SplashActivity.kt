package com.idlofi.storyappdicoding.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.idlofi.storyappdicoding.databinding.ActivitySplashBinding
import com.idlofi.storyappdicoding.preferences.SharedPreferenceHelper
import com.idlofi.storyappdicoding.ui.LoginActivity
import com.idlofi.storyappdicoding.ui.RegisterActivity
import com.idlofi.storyappdicoding.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sharedPrefHelper: SharedPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefHelper = SharedPreferenceHelper(this)

        // Check login status and navigate accordingly
        if (sharedPrefHelper.getStatusLogin()) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
            return
        }

        // Set onClickListeners for login and register buttons
        binding.btnLogin.setOnClickListener {
            val loginIntent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        binding.btnRegister.setOnClickListener {
            val registerIntent = Intent(this@SplashActivity, RegisterActivity::class.java)
            startActivity(registerIntent)
        }
    }
}
