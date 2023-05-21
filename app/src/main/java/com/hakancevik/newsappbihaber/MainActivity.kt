package com.hakancevik.newsappbihaber

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController

import com.hakancevik.newsappbihaber.common.NewsFragmentFactory

import com.hakancevik.newsappbihaber.databinding.ActivityMainBinding
import com.hakancevik.newsappbihaber.util.customToast


import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var fragmentFactory: NewsFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            installSplashScreen()
            delay(2600)
        }

        supportFragmentManager.fragmentFactory = fragmentFactory
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)


        val navController = findNavController(this, R.id.fragment)
        binding.bottomNavigationView.setupWithNavController(navController)



        binding.toolbarSettings.apply {
            setOnClickListener {
                playAnimation()
                customToast("Settings bottom sheet will be added later.")
            }
        }


    }

}