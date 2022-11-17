package com.sbfirebase.kiossku.detailfragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel
    private lateinit var binding : FragmentDetailBinding

    private val openWhatsapp = registerForActivityResult(OpenWhatsapp()){}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater , container , false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.recyclerView.adapter = DetailAdapter().apply{
            submitList(List(5){R.drawable.kioss_home_placeholder})
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this ,
            DetailViewModelFactory(
                requireActivity().application ,
                DetailFragmentArgs.fromBundle(requireArguments()).currentKioss
            )
        )[DetailViewModel::class.java]

        binding.currentKioss = viewModel.currentKioss

        binding.hubungiSekarang.setOnClickListener {
            openWhatsapp.launch(viewModel.currentKioss.phone)
        }
    }
}

class OpenWhatsapp : ActivityResultContract<String , Boolean>(){
    override fun createIntent(context: Context, input: String) =
        Intent(Intent.ACTION_VIEW).apply {
            `package` = "com.whatsapp"
            data = Uri.parse(
                "https://api.whatsapp.com/send?phone=$input"
            )
        }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return true
    }
}