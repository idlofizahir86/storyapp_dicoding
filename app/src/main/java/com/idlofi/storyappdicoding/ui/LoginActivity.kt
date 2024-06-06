package com.idlofi.storyappdicoding.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import com.idlofi.storyappdicoding.MainActivity
import com.idlofi.storyappdicoding.databinding.ActivityLoginBinding
import com.idlofi.storyappdicoding.preferences.SharedPreferenceHelper
import com.idlofi.storyappdicoding.service.ApiConfig.Companion.getApiService
import com.idlofi.storyappdicoding.service.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPrefHelper: SharedPreferenceHelper

    companion object{
        private const val TAG = "LoginActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val motionLayout = binding.motionLayout
        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                // Transition completed, do something if needed
            }
        })


        sharedPrefHelper = SharedPreferenceHelper(this)
        if (sharedPrefHelper.getStatusLogin()){
            val main = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(main)
            finish()
        }

        binding.tvRegisterNow.setOnClickListener {
            val register = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(register)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when{
                email.isEmpty() || !email.contains("@") ->{
                    binding.edLoginEmail.error = "Email tidak valid"
                }
                password.isEmpty() || password.length < 8 ->{
                    binding.edLoginPassword.error = "Password minimal 8 karakter"
                }
                else->{
                    showLoading(true)
                    login(email,password)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !isLoading
        binding.edLoginEmail.isEnabled = !isLoading
        binding.edLoginPassword.isEnabled = !isLoading
        binding.tvRegisterNow.isEnabled = !isLoading
    }


    private fun login(email: String, password: String){
        val client = getApiService().login(email, password);
        client.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                showLoading(false)
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody!=null && !responseBody.error){
                        sharedPrefHelper.saveUserToken(responseBody.loginResult.token)
                        sharedPrefHelper.setStatusLogin(true)
                        Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
                        val main = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(main)
                        finishAffinity()
                    }else {
                        Toast.makeText(this@LoginActivity, "Login Gagal: ${responseBody?.message ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login Gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@LoginActivity, "Login Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}
