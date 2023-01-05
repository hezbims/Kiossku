package com.sbfirebase.kiossku.ui.screen.authentication.login

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.databinding.ActivityLoginBinding
import com.sbfirebase.kiossku.ui.MainActivity
import com.sbfirebase.kiossku.ui.screen.authentication.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private val viewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@LoginActivity
        }

        setContentView(binding.root)

        setGoToRegisterSpanningText()
        setSuccessfullLogin()
        setSubmitData()
    }

    private fun setGoToRegisterSpanningText(){
        val spannedText = getString(R.string.go_to_register_spannable_text)
        val noAccountText = SpannableString(getString(R.string.go_to_register , spannedText)).apply {
            val clickableSpan = object : ClickableSpan(){
                override fun onClick(widget: View) {
                    startActivity(
                        Intent(this@LoginActivity , RegisterActivity::class.java)
                    )
                    this@LoginActivity.finish()
                }
            }

            setSpan(
                clickableSpan,
                length - spannedText.length,
                length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        binding.belumPunyaAkunText.text = noAccountText
        binding.belumPunyaAkunText.movementMethod = LinkMovementMethod()
    }

    private fun setSuccessfullLogin(){
        viewModel.isLoginSucceed.observe(this){ isLoginSucceed ->
            if (isLoginSucceed){
                startActivity(
                    Intent(this , MainActivity::class.java)
                )
                finish()
            }
        }
    }

    private fun setSubmitData(){
        binding.submitButton.setOnClickListener {
            viewModel.authenticate(
                email = binding.email.text.toString(),
                password = binding.password.text.toString()
            )
        }
    }
}