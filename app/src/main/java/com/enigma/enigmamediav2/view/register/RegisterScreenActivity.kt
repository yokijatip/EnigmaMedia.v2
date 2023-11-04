package com.enigma.enigmamediav2.view.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.enigma.enigmamediav2.databinding.ActivityRegisterScreenBinding
import com.enigma.enigmamediav2.di.Injection
import com.enigma.enigmamediav2.utils.CommonUtils
import com.enigma.enigmamediav2.view.login.LoginScreenActivity
import com.enigma.enigmamediav2.viewModel.register.RegisterViewModel
import com.enigma.enigmamediav2.viewModel.register.ViewModelFactory
import kotlinx.coroutines.launch

class RegisterScreenActivity : AppCompatActivity() {

    private lateinit var registerScreenBinding: ActivityRegisterScreenBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerScreenBinding = ActivityRegisterScreenBinding.inflate(layoutInflater)
        setContentView(registerScreenBinding.root)

        val context = this
        val repository = Injection.provideRepository(context)
        val viewModelFactory = ViewModelFactory(repository)
        registerViewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]

        registerScreenBinding.apply {
//            Logic Password kalo kurang dari 8
            edtPassword.doOnTextChanged { text, _, _, _ ->
                if (text!!.length < 8) {
                    edtPasswordLayout.error = "Harus 8 Karakter"
                } else {
                    edtPasswordLayout.error = null
                }
            }
            btnLogin.setOnClickListener {
                onRegister()
            }
        }
    }

    private fun onRegister() {
        val username = registerScreenBinding.edtUsername.text.toString().trim()
        val email = registerScreenBinding.edtEmail.text.toString().trim()
        val password = registerScreenBinding.edtPassword.text.toString().trim()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            CommonUtils.showToast(this, "Harus di isi Semua🙌")
        } else if (password.length <= 8) {
            CommonUtils.showToast(this, "Password Harus 8 atau Lebih")
        } else {
            CommonUtils.showLoading(registerScreenBinding.loadingRegister, true)
            lifecycleScope.launch {
                try {
                    registerViewModel.register(username, email, password)
                    onRegisterSuccess()
                } catch (e: Exception) {
                    onRegisterFailed()
                }
            }
        }
    }

    private fun onRegisterSuccess() {
        CommonUtils.showLoading(registerScreenBinding.loadingRegister, false)
        startActivity(Intent(this@RegisterScreenActivity, LoginScreenActivity::class.java))
        finish()
    }

    private fun onRegisterFailed() {
        CommonUtils.showToast(this, "Gagal Membuat Akun, Atau akun sudah ada 😁")
        CommonUtils.showLoading(registerScreenBinding.loadingRegister, false)
    }
}