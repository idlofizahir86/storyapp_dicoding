package com.idlofi.storyappdicoding.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.idlofi.storyappdicoding.databinding.ActivityRegisterBinding
import com.idlofi.storyappdicoding.service.ApiConfig
import com.idlofi.storyappdicoding.service.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    companion object{
        private const val TAG ="RegisterActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //button signup
        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            when{
                name.isEmpty()->{
                    binding.edRegisterName.error = "Nama perlu diisi"
                }
                email.isEmpty() || !email.contains("@") ->{
                    binding.edRegisterEmail.error = "Email tidak valid"
                }
                password.isEmpty() || password.length < 8 ->{
                    binding.edRegisterPassword.error = "Password minimal 8 karakter"
                }else->{
                    showLoading(true)
                register(name,email,password)
            }
            }
        }

        binding.tvLoginNow.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !isLoading
        binding.edRegisterName.isEnabled = !isLoading
        binding.edRegisterEmail.isEnabled = !isLoading
        binding.edRegisterPassword.isEnabled = !isLoading
        binding.tvLoginNow.isEnabled = !isLoading
    }

    private fun register(name: String, email: String, password: String){
        val client = ApiConfig().getApiService().register(name,email,password)
        client.enqueue(object: Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody!=null && !responseBody.error){
                        Toast.makeText(this@RegisterActivity, "Register Beerhasil", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(this@RegisterActivity, "Register Gagal: ${responseBody?.message ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Register Gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@RegisterActivity, "Register Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

}