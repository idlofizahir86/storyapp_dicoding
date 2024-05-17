package com.idlofi.storyappdicoding

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.idlofi.storyappdicoding.databinding.ActivityMainBinding
import com.idlofi.storyappdicoding.preferences.SharedPreferenceHelper
import com.idlofi.storyappdicoding.ui.LoginActivity

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefHelper: SharedPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefHelper = SharedPreferenceHelper(this)
        sharedPrefHelper.setStatusLogin(true)

        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.btn_logout -> {
                val builder = AlertDialog.Builder(this)
                with(builder)
                {
                    setTitle("Log Out")
                    setMessage("Apakah anda yakin akan log out?")
                    setPositiveButton("Ya") { dialogInterface, which ->
                        sharedPrefHelper.clearUserLogin()
                        sharedPrefHelper.clearUserToken()
                        sharedPrefHelper.setStatusLogin(false)
                        Intent(this@MainActivity, LoginActivity::class.java).apply {
                            startActivity(this)
                            finish()
                        }

                    }
                    setNegativeButton("Batal"){ dialogInterface, which ->
                        Toast.makeText(this@MainActivity, "Terima kasih", Toast.LENGTH_SHORT).show()
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}

