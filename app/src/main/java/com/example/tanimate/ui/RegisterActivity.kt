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
import com.example.tanimate.data.local.UserSession
import com.example.tanimate.databinding.ActivityRegisterBinding
import com.example.tanimate.viewmodel.RegisterViewModel
import com.example.tanimate.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btRegist.setOnClickListener(this)
        binding.tvAdaAkun.setOnClickListener(this)

        setViewModel()
    }

    private fun setViewModel(){
        val pref = UserSession.getInstance(dataStore)
        registerViewModel = ViewModelProvider(
            this, ViewModelFactory(this,  pref)
        )[RegisterViewModel::class.java]

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        registerViewModel.isError.observe(this) {
            isError = it
        }
        registerViewModel.msg.observe(this){
            AlertDialog.Builder(this@RegisterActivity).apply {
                setTitle("TaniMate")
                setMessage(it)
                setPositiveButton("OK"){_,_ ->
                    if (!isError) finish()
                }
                create()
                show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbRegist.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        var isError = false
    }

    override fun onClick(id: View) {
        when(id) {
            binding.btRegist -> {
                val name = binding.edNama.text.toString()
                val email = binding.edEmail.text.toString()
                val password = binding.etPassword.text.toString()
                val konfirpassword = binding.etKonfirPassword.text.toString()
                when {
                    name.isEmpty() -> {
                        binding.layoutNama.error = "Field masih kosong"
                    }
                    email.isEmpty() -> {
                        binding.layoutEmail.error = "Field masih kosong"
                    }
                    password.isEmpty() -> {
                        binding.layoutPassword.error = "Field masih kosong"
                    }
                    konfirpassword.isEmpty() -> {
                        binding.layoutKonfirPassword.error = "Field masih kosong"
                    }
                    else -> {
                        registerViewModel.register(email, name, password, konfirpassword)
                        registerViewModel.isError.observe(this) {
                            if (!it) {
                                AlertDialog.Builder(this@RegisterActivity).apply {
                                    setTitle("WELCOME!")
                                    setMessage("Register Success")
                                    setPositiveButton("OK") {_,_ ->
                                        val intent = Intent(context, LoginActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                    create()
                                    show()
                                }
                            }
                        }
                    }
                }
            }
            binding.tvGoMasuk -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}