package com.example.paypesa.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.paypesa.R
import com.example.paypesa.databinding.ActivityHomeBinding
import com.google.android.material.bottomappbar.BottomAppBar

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.fragmentContainerView)


        val bottomAppBar: BottomAppBar = binding.bottomAppBar
        bottomAppBar.setupWithNavController(navController)

        binding.sendMoneyFab.setOnClickListener {
            openSendMoneyFragment()
        }
     }

    private fun openSendMoneyFragment() {
        navController.navigate(R.id.sendAmountFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_bottom_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.dashboardFragment -> {
                Toast.makeText(applicationContext, "Dashboard Clicked", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.dashboardFragment)
                true
            }
            R.id.transactionFragment -> {
                Toast.makeText(applicationContext, "Transaction Log Clicked", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.transactionFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}