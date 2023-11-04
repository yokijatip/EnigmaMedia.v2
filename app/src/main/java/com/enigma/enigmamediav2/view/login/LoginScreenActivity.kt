package com.enigma.enigmamediav2.view.login

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.enigma.enigmamediav2.databinding.ActivityLoginScreenBinding
import com.enigma.enigmamediav2.di.Injection
import com.enigma.enigmamediav2.helper.TokenPreferences
import com.enigma.enigmamediav2.helper.dataStore
import com.enigma.enigmamediav2.utils.CommonUtils
import com.enigma.enigmamediav2.view.main.MainActivity
import com.enigma.enigmamediav2.viewModel.login.LoginViewModel
import com.enigma.enigmamediav2.viewModel.login.ViewModelFactory
import kotlinx.coroutines.launch

class LoginScreenActivity : AppCompatActivity() {

    private lateinit var loginScreenBinding: ActivityLoginScreenBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginScreenBinding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(loginScreenBinding.root)

        val context = this
        val repository = Injection.provideRepository(context)
        val viewModelFactory = ViewModelFactory(repository)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        loginScreenBinding.apply {
//            Underline Forgot Password
            tvForgotPassword.paintFlags =
                loginScreenBinding.tvForgotPassword.paintFlags or Paint.UNDERLINE_TEXT_FLAG

//            Logic Password kalo kurang dari 8
            edtPassword.doOnTextChanged { text, _, _, _ ->
                if (text!!.length < 8) {
                    edtPasswordLayout.error = "Harus 8 Karakter"
                } else {
                    edtPasswordLayout.error = null
                }
            }

            btnLogin.setOnClickListener {
                onLogin()
            }
        }
    }

    private fun onLogin() {
        val email = loginScreenBinding.edtEmail.text.toString().trim()
        val password = loginScreenBinding.edtPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            CommonUtils.showToast(this, "Harus di isi Semua 🫵😞")
        } else if (password.length <= 8) {
            CommonUtils.showToast(this, "Password harus 8 Karakter atau lebih")
        } else {
            CommonUtils.showLoading(loginScreenBinding.loadingLogin, true)
            lifecycleScope.launch {
                try {
                    val result = loginViewModel.login(email, password)
                    val token = result.loginResult?.token
                    if (token != null) {
                        saveTokenDataStore(token)
                    } else {
                        onLoginFailed()
                    }
                    onLoginSuccess()
                } catch (e: Exception) {
                    onLoginFailed()
                }
            }
        }
    }

    private suspend fun onLoginSuccess() {
        CommonUtils.showLoading(loginScreenBinding.loadingLogin, false)
        val token = loadTokenDataStore()
        Log.d("TokenLogin", "Token: $token")
        startActivity(Intent(this@LoginScreenActivity, MainActivity::class.java))
        finish()
    }

    private fun onLoginFailed() {
        CommonUtils.showToast(this, "Gagal Login, password atau email salah 😁")
        CommonUtils.showLoading(loginScreenBinding.loadingLogin, false)
    }

    private suspend fun saveTokenDataStore(token: String) {
        val dataStore = TokenPreferences.getInstance(this.dataStore)
        dataStore.saveToken(token)
    }

    private suspend fun loadTokenDataStore() : String {
        val dataStore = TokenPreferences.getInstance(this.dataStore)
        return dataStore.getToken()
    }
}