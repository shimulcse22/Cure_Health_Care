package com.devshawon.curehealthcare.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.FtsOptions.Order
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.databinding.OrderListItemBinding
import com.devshawon.curehealthcare.models.OrderData

class OrderAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val list = ArrayList<OrderData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderViewHolder(
            OrderListItemBinding.inflate(
                LayoutInflater.from(context),parent,false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OrderViewHolder).bind(position)
    }

    inner class OrderViewHolder(private val binding : OrderListItemBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(position: Int){
            binding.apply {
                list[position].let {
                    orderId.text = "Order ID : ${it.id}"
                    if(it.isPreOrder!!){
                        preorderTextId.text = "Pre-order"
                        preorderTextId.visibility = View.VISIBLE
                    }else{
                        preorderTextId.visibility = View.GONE
                    }
                    money.text = it.amount
                    date.text = it.orderPlacedAt
                    status.text = it.status
                    binding.root.setOnClickListener{d->
                        it.id?.let { it1 -> navigate.invoke(it1) }
                    }
                }
            }
        }
    }

    fun updateList(updateList : ArrayList<OrderData>){
        this.list.clear()
        this.list.addAll(updateList)
        notifyItemInserted(0)
    }

    fun addList(updateList : ArrayList<OrderData>){
        this.list.addAll(updateList)
        notifyItemRangeChanged(this.list.size,updateList.size)
    }

    companion object{
        var navigate :  (data : Int) ->Unit = {}
    }
}