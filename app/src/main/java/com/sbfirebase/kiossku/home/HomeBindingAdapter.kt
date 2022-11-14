package com.sbfirebase.kiossku.home

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sbfirebase.kiossku.data.Kioss

@BindingAdapter("recyclerViewData" , "navigateToDetail" , requireAll = true)
fun setRecyclerViewData(view : RecyclerView ,
                        data : List<Kioss>? ,
                        navigateToDetail : (Kioss) -> Unit
){
    data?.let{
        view.adapter = HomeAdapter(navigateToDetail).apply {
            submitList(it)
        }
    }
}