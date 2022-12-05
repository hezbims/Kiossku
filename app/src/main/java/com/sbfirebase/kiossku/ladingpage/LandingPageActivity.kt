package com.sbfirebase.kiossku.ladingpage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sbfirebase.kiossku.databinding.ActivityLandingPageBinding
import com.sbfirebase.kiossku.home.HomeActivity

class LandingPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://bit.ly/DaftarIklanGratis2022")
                }
            )
        }
    }
}