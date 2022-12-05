package com.sbfirebase.kiossku.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.databinding.ActivityLoginBinding
import com.sbfirebase.kiossku.ladingpage.LandingPageActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

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
    }

    private fun setSuccessfullLogin(){
        viewModel.isLoginSucceed.observe(this){ isLoginSucceed ->
            if (isLoginSucceed){
                startActivity(
                    Intent(this , LandingPageActivity::class.java)
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