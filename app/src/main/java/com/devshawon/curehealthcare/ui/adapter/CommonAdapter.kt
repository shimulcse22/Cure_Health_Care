package com.devshawon.curehealthcare.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.databinding.ListItemBannerBinding
import com.google.android.material.slider.Slider

class CommonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val bannerList = ArrayList<String>()
    lateinit var context : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BannerViewHolder(
            ListItemBannerBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = bannerList.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BannerViewHolder).bindView(position)
    }

    inner class BannerViewHolder(private val binding : ListItemBannerBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindView(position: Int){
            binding.apply {
                Glide.with(context).load(bannerList[position])
                    .placeholder(R.drawable.banner_background)
                    .apply(
                        RequestOptions.bitmapTransform(
                            RoundedCorners(context.resources.getDimension(com.intuit.sdp.R.dimen._9sdp)
                        .toInt())
                        ))
                    .into(object :
                        CustomTarget<Drawable>() {
                        override fun onLoadCleared(placeholder: Drawable?) {
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            bannerIt.background = resource
                        }
                    })
            }
        }
    }

    fun updateContext(context: Context){
        this.context = context
    }
    fun updateList(updateList: ArrayList<String>) {
        bannerList.clear()
        bannerList.addAll(updateList)
        notifyItemChanged(0,updateList)
    }
}