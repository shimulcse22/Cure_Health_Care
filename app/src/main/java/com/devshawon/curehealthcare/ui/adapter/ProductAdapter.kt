package com.devshawon.curehealthcare.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.Log
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
import java.lang.StringBuilder

class ProductAdapter(private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val productList = ArrayList<ProductData>()
    var listSize = false
    private var itemViewType = 0
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(
            ListItemMedicineBinding.inflate(
                LayoutInflater.from(context), parent, false
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

    inner class ProductViewHolder(private val binding: ListItemMedicineBinding) :
        RecyclerView.ViewHolder(binding.root) {
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
                        this.preorderText.visibility=View.GONE
                        this.dateText.visibility=View.GONE
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
                    if(getInt( binding.data) >= getInt( productList[position].productLimits)){
                        plusIcon.setBackgroundResource(R.drawable.plus_icon_bacground_limit)
                        plusIcon.isClickable = false
                    }
                    onItemClick.onPlusIconClick(productList[position])
                    executePendingBindings()
                }

                minusIcon.setOnClickListener {
                    binding.data = ((getInt(binding.data) - 1)).toString()
                    if (getInt(binding.data) == 0) {
                        removeItem(this,position,false)
                        return@setOnClickListener
                    }
                    productList[position].productCount = productList[position].productCount!! - 1
                    onItemClick.onMinusIconClick(productList[position],position,false)
                    if(getInt( binding.data) <= getInt( productList[position].productLimits)){
                        plusIcon.setBackgroundResource(R.drawable.plus_icon_background)
                        plusIcon.isClickable = true
                    }
                    executePendingBindings()
                }

                deleteIcon.setOnClickListener {
                    plusIcon.setBackgroundResource(R.drawable.plus_icon_background)
                    plusIcon.isClickable = true
                    removeItem(this,position,true)
                }
            }
        }
    }

    fun updateContext(context: Context) {
        this.context = context
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateProductList(updateList: ArrayList<ProductData>, itemViewType: Int) {
        this.itemViewType = itemViewType
        listSize = updateList.size == 0
        productList.clear()
        productList.addAll(updateList)
        notifyItemChanged(0, updateList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addProductList(updateList: ArrayList<ProductData>) {
        productList.addAll(updateList)
        listSize = updateList.size == 0
        notifyItemChanged(this.productList.size, updateList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeItem(binding : ListItemMedicineBinding, position: Int,isDelete : Boolean){
        binding.data = ""
        binding.minusIcon.visibility = View.INVISIBLE
        binding.deleteIcon.visibility = View.INVISIBLE
        binding.numberTitle.visibility = View.INVISIBLE
        val count = productList[position].productCount
        productList[position].productCount = 0
        onItemClick.onMinusIconClick(productList[position],count?:0,isDelete)
    }
}