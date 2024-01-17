package com.example.wallpaperapp.Presentation.View

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallpaperapp.Presentation.ViewModel.wallPaperViewModel
import com.example.wallpaperapp.Presentation.WallPaperUiState
import com.example.wallpaperapp.Presentation.adapter.ImagesRecyclerViewAdapter
import com.example.wallpaperapp.Presentation.adapter.ItemOnClickListener
import com.example.wallpaperapp.R
import com.example.wallpaperapp.databinding.ActivityMainBinding
import com.example.wallpaperapp.domain.entity.WallpaperLink
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val wallpaperviewmodel: wallPaperViewModel by viewModels()
    // private lateinit var wallpaperAdapter: ImagesRecyclerViewAdapter
    private lateinit var wallpaperAdapter: ImagesRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViews()
        collectUIState()
        wallpaperviewmodel.fetchWallpapers()


    }

    fun setViews() {

        binding.imagesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainActivity,2)
        }



    }

    fun collectUIState() {
        //coroutine
        //Main heheh or ui
        //Defaukt
        //Io
        lifecycleScope.launch(Dispatchers.Main) {
            wallpaperviewmodel.wallpaperList.collect() { wallpaperUiState ->
                when (wallpaperUiState) {
                    is WallPaperUiState.Loading -> {
                        //progress bar
                        binding.progressBar.visibility = View.VISIBLE
                        Toast.makeText(
                            this@MainActivity,
                            "Wallpapers are currently Loading",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    is WallPaperUiState.EmptyList -> {
                        //empty
                        binding.progressBar.visibility = View.VISIBLE
                        Toast.makeText(
                            this@MainActivity,
                            "Wallpapers are currently empty",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    is WallPaperUiState.Success -> {
                        //update recyclerView
                        binding.progressBar.visibility = View.GONE
                        //update the recyclerView

                        populateDataInRecyclerView(wallpaperUiState.data)


                    }

                    is WallPaperUiState.Error -> {
                        //toast error
                        Toast.makeText(this@MainActivity, "Error Occured", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

            }


        }


    }

    fun populateDataInRecyclerView(list: List<WallpaperLink>) {
        wallpaperAdapter = ImagesRecyclerViewAdapter(list,this::onClickImage)
        binding.imagesRecyclerView.adapter = wallpaperAdapter


    }
    fun onClickImage(wallpaperLink: String) {
        // You need to convert the URL of the wallpaper to a Bitmap
        lifecycleScope.launch(Dispatchers.IO) {
            val bitmap = getBitmapFromUrl(wallpaperLink)
            if (bitmap != null) {
                setWallpaperTo(bitmap)
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to set wallpaper",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    private fun getBitmapFromUrl(wallpaperLink: String): Bitmap? {
        var inputStream: InputStream? = null
        var bitmap: Bitmap? = null
        try {
            val url = URL(wallpaperLink)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            inputStream = connection.inputStream
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return bitmap
    }

    private suspend fun setWallpaperTo(bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            val wallpaperManager = WallpaperManager.getInstance(this@MainActivity)
            try {
                wallpaperManager.setBitmap(bitmap)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Wallpaper set successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to set wallpaper",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                e.printStackTrace()
            }
        }
    }
}



