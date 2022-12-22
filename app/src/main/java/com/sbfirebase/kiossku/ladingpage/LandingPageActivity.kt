package com.sbfirebase.kiossku.ladingpage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sbfirebase.kiossku.authentication.login.LoginActivity
import com.sbfirebase.kiossku.databinding.ActivityLandingPageBinding
import com.sbfirebase.kiossku.home.HomeActivity
import com.sbfirebase.kiossku.submitkios.SubmitKiosActivty

class LandingPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel = ViewModelProvider(
            this,
            LandingPageViewModelFactory(application)
        )[LandingPageViewModel::class.java]

        if (!viewModel.isLoggedIn){
            startActivity(
                Intent(this , LoginActivity::class.java)
            )
            finish()
        }

        super.onCreate(savedInstanceState)

        val binding = ActivityLandingPageBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.cariKiosButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )
        }

        binding.buttonPemilikKios.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SubmitKiosActivty::class.java
                )
            )
        }

        binding.logout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.logoutSucceed.observe(this){ logoutSucceed ->
            if (logoutSucceed){
                startActivity(
                    Intent(this , LoginActivity::class.java)
                )
                finish()
            }
        }
    }
}