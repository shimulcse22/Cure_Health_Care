package com.devshawon.curehealthcare.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.databinding.ListItemMedicineBinding
import com.devshawon.curehealthcare.models.ProductData
import com.devshawon.curehealthcare.ui.fragments.OnItemClick
import com.devshawon.curehealthcare.util.getInt

class ProductCartAdapter (private val onItemClick : OnItemClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
        @SuppressLint("SetTextI18n")
        fun productBindView(position: Int) {
            binding.apply {
                val string = StringBuilder()

                productList[position].let {
                    string.append(it.productForms.name).append(" ").append(it.commercialName).append("(").append(it.boxSize).append(" ")
                        .append(it.unitType).append(")")
                    this.medicineName.text = string
                    this.medicineCompany.text = it.manufacturingCompany.name
                    this.medicinePrice.text=  "${context.resources.getString(R.string.money_sign)} ${it.salePrice}"
                    this.mrpPrice.text = it.mrp
                    this.mrpPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    this.discountPrice.text = it.discount+"%"
                    if(it.estimatedDelivery?.isNotEmpty() == true){
                        this.preorderText.text = "Pre-order"
                        this.dateText.text = it.estimatedDelivery
                    }else{
                        this.preorderText.visibility= View.GONE
                        this.dateText.visibility= View.GONE
                    }
                }
                if(productList[position].productCount == 0){
                    deleteIcon.visibility = View.INVISIBLE
                    minusIcon.visibility = View.INVISIBLE
                    binding.data =""
                }else{
                    productList[position].productCount.toString()
                    deleteIcon.visibility = View.VISIBLE
                    minusIcon.visibility = View.VISIBLE
                    binding.data = productList[position].productCount.toString()
                }
                Glide.with(context).load(productList[position].photo.previewUrl)
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

                plusIcon.setOnClickListener {
                    binding.data = ((getInt(binding.data) + 1)).toString()
                    if (getInt(binding.data) > 0) {
                        minusIcon.visibility = View.VISIBLE
                        deleteIcon.visibility = View.VISIBLE
                        numberTitle.visibility = View.VISIBLE
                    }
                    productList[position].productCount = productList[position].productCount!! + 1
                    onItemClick.onPlusIconClick(productList[position])
                    executePendingBindings()
                }

                minusIcon.setOnClickListener {
                    binding.data = ((getInt(binding.data) - 1)).toString()
                    if (getInt(binding.data) == 0) {
                        removeItem(this,position)
                        return@setOnClickListener
                    }
                    productList[position].productCount = productList[position].productCount!! - 1
                    onItemClick.onMinusIconClick(productList[position],position)
                    executePendingBindings()
                }
                deleteIcon.setOnClickListener {
                    removeItem(this,position)
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

    @SuppressLint("NotifyDataSetChanged")
    private fun removeItem(binding : ListItemMedicineBinding, position: Int){
        productList[position].productCount = 0
        onItemClick.onMinusIconClick(productList[position],position)
        productList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, productList.size)
        notifyDataSetChanged()
    }
}