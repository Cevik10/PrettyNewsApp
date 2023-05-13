package com.hakancevik.newsappbihaber

import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController

import com.hakancevik.newsappbihaber.common.NewsFragmentFactory

import com.hakancevik.newsappbihaber.databinding.ActivityMainBinding

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var fragmentFactory: NewsFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navController = findNavController(this, R.id.fragment)
        binding.bottomNavigationView.setupWithNavController(navController)


//        binding.toolbarSettings.apply {
//            setOnClickListener {
//                playAnimation()
//            }
//        }


    }

}