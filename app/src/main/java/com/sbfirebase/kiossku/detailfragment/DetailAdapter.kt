package com.sbfirebase.kiossku.detailfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sbfirebase.kiossku.databinding.ViewHolderDetailImageBinding
import com.squareup.picasso.Picasso

class DetailAdapter : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    private var links = listOf<String>()

    fun submitList(newLinks : List<String>){
        links = newLinks
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding : ViewHolderDetailImageBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(link : String){
            try{
                Picasso.get()
                    .load(link)
                    .into(binding.gambarKios)
            }catch (e : IllegalArgumentException){
                e.printStackTrace()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ViewHolderDetailImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(links[position])

    override fun getItemCount() = links.size
}