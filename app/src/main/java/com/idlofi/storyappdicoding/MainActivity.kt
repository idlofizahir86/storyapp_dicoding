package com.idlofi.storyappdicoding

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.idlofi.storyappdicoding.adapter.StoriesAdapter
import com.idlofi.storyappdicoding.databinding.ActivityMainBinding
import com.idlofi.storyappdicoding.preferences.SharedPreferenceHelper
import com.idlofi.storyappdicoding.service.ApiConfig
import com.idlofi.storyappdicoding.service.GetAllStoryResponse
import com.idlofi.storyappdicoding.ui.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import com.idlofi.storyappdicoding.model.StoriesModel
import com.idlofi.storyappdicoding.ui.stories.AddStoryActivity
import com.idlofi.storyappdicoding.ui.stories.DetailStoryActivity

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefHelper: SharedPreferenceHelper
    companion object {
        const val SUCCESS_UPLOAD_STORY = "Story berhasil diupload"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefHelper = SharedPreferenceHelper(this)
        sharedPrefHelper.setStatusLogin(true)

        setSupportActionBar(binding.toolbar)

       binding.rvStories

        showLoading(true)
        showStories()
        getNewStories()

        binding.fabAdd.setOnClickListener {
            val addStoryIntent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(addStoryIntent)
        }
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
                    setPositiveButton("Ya") { dialogInterface, _ ->
                        sharedPrefHelper.clearUserLogin()
                        sharedPrefHelper.clearUserToken()
                        sharedPrefHelper.setStatusLogin(false)
                        Intent(this@MainActivity, LoginActivity::class.java).apply {
                            startActivity(this)
                            finish()
                        }

                    }
                    setNegativeButton("Batal"){ _, _ ->
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvStories.isEnabled = !isLoading
    }

    private fun showStories(){
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        scope.launch {
            val token = "Bearer ${sharedPrefHelper.getUserToken()}"
            withContext(Dispatchers.Main) {
                val client = ApiConfig().getApiService().getAllStories(token)
                client.enqueue(object : Callback<GetAllStoryResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<GetAllStoryResponse>,
                        response: Response<GetAllStoryResponse>
                    ) {
                        showLoading(false)
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error) {
                                val dataStories = responseBody.listStory
                                val storiesAdapter = StoriesAdapter(dataStories, object: StoriesAdapter.OnAdapterListener{
                                    override fun onClick(list: StoriesModel) {
                                        val intent = Intent(applicationContext, DetailStoryActivity::class.java).apply {
                                            putExtra("list_name",list.name)
                                            putExtra("list_image", list.photoUrl)
                                            putExtra("list_description", list.description)
                                        }
                                        val bundle = Bundle(ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, binding.rvStories,"photo").toBundle())
                                        startActivity(intent,bundle)


                                    }

                                })

                                binding.rvStories.apply {
                                    layoutManager = LinearLayoutManager(this@MainActivity)
                                    setHasFixedSize(true)
                                    storiesAdapter.notifyDataSetChanged()
                                    adapter = storiesAdapter
                                }

                            }
                        }
                    }

                    override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        }
    }

    private fun getNewStories() {
        binding.apply {
            if (intent != null) {
                val isNewStory = intent.extras?.getBoolean(SUCCESS_UPLOAD_STORY)
                if (isNewStory != null && isNewStory) {
                    showStories()
                }
            }
        }
    }
}

