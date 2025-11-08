package com.example.souqseller.activities.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.souqseller.databinding.ActivityHomeBinding
import com.example.souqseller.R
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnNav.setItemIconTintList(null)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment)as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = binding.btnNav
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

    }
}