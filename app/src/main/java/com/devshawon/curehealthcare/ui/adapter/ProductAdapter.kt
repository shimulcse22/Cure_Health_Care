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
import com.devshawon.curehealthcare.databinding.ListItemMedicineBinding
import com.devshawon.curehealthcare.models.ProductData
import com.devshawon.curehealthcare.ui.fragments.OnItemClick

class ProductAdapter(private val onItemClick : OnItemClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val productList = ArrayList<ProductData>()
    private var itemViewType = 0
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(
            ListItemMedicineBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = productList.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProductViewHolder).productBindView(position)
    }

    override fun getItemViewType(position: Int): Int {
        return itemViewType
    }

    inner class ProductViewHolder (private val binding : ListItemMedicineBinding) : RecyclerView.ViewHolder(binding.root){
        fun productBindView(position: Int){
            binding.apply {
                medicineName.text = productList[position].commercialName
                medicineCompany.text = productList[position].manufacturingCompany.name
                medicinePrice.text = productList[position].salePrice
                Glide.with(context).load(productList[position].photo.previewUrl)
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

                plusIcon.setOnClickListener{
                    onItemClick.onPlusIconClick(0)
                    var d = binding.data
                    if(d == null || d ==""){
                        d = "0"
                    }
                    binding.data = ((d.toInt() + 1)).toString()
                }
            }
        }
    }

    fun updateContext(context: Context) {
        this.context = context
    }

    fun updateProductList(updateList: ArrayList<ProductData>, itemViewType: Int) {
        this.itemViewType = itemViewType
        productList.clear()
        productList.addAll(updateList)
        notifyItemChanged(0, updateList)
    }
}