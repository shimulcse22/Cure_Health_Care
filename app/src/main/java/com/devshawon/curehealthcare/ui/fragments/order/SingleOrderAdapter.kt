package com.devshawon.curehealthcare.ui.fragments.order

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.databinding.ListItemOrderBinding
import com.devshawon.curehealthcare.models.SingleOrderProduct
import com.devshawon.curehealthcare.util.getInt
import java.lang.StringBuilder

class SingleOrderAdapter(private val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val list = ArrayList<SingleOrderProduct>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SingleItemViewHolder(
            ListItemOrderBinding.inflate(
                LayoutInflater.from(context),parent,false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SingleItemViewHolder).bind(position)
    }

    inner class SingleItemViewHolder(private val binding: ListItemOrderBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.apply {
                Glide.with(context).load(list[position].photo.previewUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.banner_background).apply(
                        RequestOptions.bitmapTransform(
                            RoundedCorners(
                                context.resources.getDimension(com.intuit.sdp.R.dimen._9sdp).toInt()
                            )
                        )
                    ).into(object : CustomTarget<Drawable>() {
                        override fun onLoadCleared(placeholder: Drawable?) {
                        }

                        override fun onResourceReady(
                            resource: Drawable, transition: Transition<in Drawable>?
                        ) {
                            medicineImage.background = resource
                        }
                    })
                val string = StringBuilder()
                list[position].let {
                    string.append(it.productForms.name).append(" ").append(it.commercialName).append("(").append(it.boxSize).append(" ")
                        .append(it.unitType).append(")")
                    medicineName.text = string
                    medicineCompany.text = it.manufacturingCompany.name
                    string.clear()
                    medicinePrice.text = string.append(it.mrp).append((getInt(it.mrp) - getInt(it.salePrice))).append("(").append(it.discount).append("%) = ")
                        .append(context.resources.getString(R.string.money_sign)).append(it.salePrice).append("*").append(it.quantity).toString()
                    string.clear()
                    deleteIcon.text = string.append(context.resources.getString(R.string.money_sign)).append((getInt(it.salePrice)* getInt(it.quantity)).toString()).toString()

                }
            }
        }
    }

    fun addList(list: ArrayList<SingleOrderProduct>){
        this.list.clear()
        this.list.addAll(list)
        notifyItemRangeChanged(0,list.size,list)
    }
}