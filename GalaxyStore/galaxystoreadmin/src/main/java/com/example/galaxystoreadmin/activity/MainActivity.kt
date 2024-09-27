package com.example.galaxystoreadmin.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.galaxystoreadmin.R
import com.example.galaxystoreadmin.databinding.ActivityMainBinding
import com.example.galaxystoreadmin.utils.Utils
import com.example.galaxystoreadmin.viewmodels.ProductViewModel
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFA500")))

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)


        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.home, R.id.addProduct, R.id.order), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {
            R.id.action_logout-> {  logout() }
            R.id.action_profile -> { userAccount()}
        }

        return super.onOptionsItemSelected(item)


    }

    @SuppressLint("SuspiciousIndentation")
    private fun logout() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val alertDialog: AlertDialog = builder.create()

           builder.setIcon(R.drawable.logout_icon)
            .setTitle("Logout")
            .setMessage("Are you sure want to be logout?")

            .setPositiveButton("Yes") { _, _ ->
            FirebaseAuth.getInstance().signOut()
            Utils.showToast(this,"You have been logout successfully")
            startActivity(Intent(this,AdminAuthActivity::class.java))
            finish() }

        .setNegativeButton("No"){_,_ ->
          alertDialog.dismiss()
        }
        .show()
        .setCancelable(false)
    }

    private fun userAccount(){
       productViewModel.getAdminAccount {
           Utils.showToast(this,it!!) }
    }
}