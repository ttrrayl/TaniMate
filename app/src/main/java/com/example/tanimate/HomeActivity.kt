package com.example.tanimate

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tanimate.data.local.UserSession
import com.example.tanimate.databinding.ActivityHomeBinding
import com.example.tanimate.ui.LoginActivity
import com.example.tanimate.viewmodel.HomeViewModel
import com.example.tanimate.viewmodel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.calculate -> replaceFragment(CalculateFragment())
                R.id.textsms -> replaceFragment(TextsmsFragment())
                R.id.profile -> replaceFragment(ProfileFragment())

            else ->{

                }
            }

            true
        }

        setViewModel()

    }

    private fun setViewModel() {
        val pref = UserSession.getInstance(dataStore)
        homeViewModel = ViewModelProvider(this, ViewModelFactory(this, pref))[HomeViewModel::class.java]
        homeViewModel.getToken().observe(this) { user ->
            if (!user.isLogin){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()




    }
}