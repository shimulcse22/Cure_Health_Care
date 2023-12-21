package com.devshawon.curehealthcare.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.databinding.ListItemBannerBinding
import com.devshawon.curehealthcare.databinding.ListItemMedicineBinding
import com.devshawon.curehealthcare.models.Form
import com.google.android.material.slider.Slider

class CommonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val bannerList = ArrayList<String>()
    private val productList =  ArrayList<Form>()
    private var itemViewType = 0
    lateinit var context : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            0->{
                BannerViewHolder(
                    ListItemBannerBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )
            }else->{
                ProductViewHolder(
                    ListItemMedicineBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )
            }
        }

    }

    override fun getItemCount(): Int = bannerList.size
    override fun getItemViewType(position: Int): Int {
        return itemViewType
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(itemViewType){
            0->(holder as BannerViewHolder).bindView(position)
            else->{
                (holder as ProductViewHolder).productBindView(position)
            }
        }

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

    inner class ProductViewHolder (private val binding : ListItemMedicineBinding) : RecyclerView.ViewHolder(binding.root){
        fun productBindView(position: Int){
            binding.apply {
                medicineName.text = productList[position].name
            }
        }
    }

    fun updateContext(context: Context){
        this.context = context
    }
    fun updateList(updateList: ArrayList<String>, itemViewType :Int) {
        this.itemViewType = itemViewType
        bannerList.clear()
        bannerList.addAll(updateList)
        notifyItemChanged(0,updateList)
    }

    fun updateProductList(updateList: ArrayList<Form>, itemViewType :Int) {
        this.itemViewType = itemViewType
        productList.clear()
        productList.addAll(updateList)
        notifyItemChanged(0,updateList)
    }
}