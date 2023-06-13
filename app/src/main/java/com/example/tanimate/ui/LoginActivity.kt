package com.example.tanimate.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.tanimate.HomeActivity
import com.example.tanimate.R
import com.example.tanimate.data.local.UserModel
import com.example.tanimate.data.local.UserSession
import com.example.tanimate.databinding.ActivityLoginBinding
import com.example.tanimate.viewmodel.LoginViewModel
import com.example.tanimate.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var user: UserModel
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.tvGoRegist.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btLogin.setOnClickListener {
            val username = binding.edUsername.text.toString()
            val password = binding.etPassword.text.toString()
            when {
                username.isEmpty() -> {
                    binding.layoutUsername.error = "Email masih kosong"
                }
                password.isEmpty() -> {
                    binding.layoutPassword.error = "Password masih kosng"
                }
                else -> {
                    loginViewModel.authenticate(username,password)
                }
            }

//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//            finish()
        }

        setViewModel()

    }

    private fun setViewModel(){
        val pref = UserSession.getInstance(dataStore)

        loginViewModel = ViewModelProvider(this, ViewModelFactory(this, pref))[LoginViewModel::class.java]
        loginViewModel.getToken().observe(this) { user ->
            this.user = user
        }
        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        loginViewModel.msg.observe(this) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.app_name))
                setMessage(it)
                setPositiveButton("OK") {_,_ ->
                }
                create()
                show()
            }
        }
        loginViewModel.isLogin.observe(this){
            if (it) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.pbLogin.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

}

