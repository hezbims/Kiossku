package com.sbfirebase.kiossku.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sbfirebase.kiossku.R
import com.sbfirebase.kiossku.data.Kioss
import com.sbfirebase.kiossku.databinding.ViewHolderKiosHomeBinding
import com.squareup.picasso.Picasso

class HomeAdapter(private val navigateToDetail : (Kioss) -> Unit) : ListAdapter<Kioss , HomeAdapter.ViewHolder>(HomeDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            binding = ViewHolderKiosHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            navigateToDetail = navigateToDetail
        )


    class ViewHolder(
        private val binding : ViewHolderKiosHomeBinding,
        private val navigateToDetail: (Kioss) -> Unit
        )
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(item : Kioss){
                Picasso.get()
                    .load("https://kiossku.com/wp-content/uploads/2022/11/a1-2-348x450.png")
                    .into(binding.gambarKios)
                binding.harga.text = binding.root.context
                    .getString(R.string.harga , item.harga)
                binding.alamat.text = item.alamat
                binding.judulKios.text = item.judul

                binding.cardView.setOnClickListener {
                    navigateToDetail(item)
                }
            }
    }
}

class HomeDiffCallback : DiffUtil.ItemCallback<Kioss>(){
    override fun areItemsTheSame(oldItem: Kioss, newItem: Kioss) =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: Kioss, newItem: Kioss) =
        oldItem == newItem
}