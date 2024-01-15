package com.devshawon.curehealthcare.ui.fragments.filter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devshawon.curehealthcare.databinding.SingleItemViewWithTextBoxBinding
import com.devshawon.curehealthcare.models.Form
import java.util.Collections

class SingleItemAdapter(private val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list : ArrayList<Form> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SingleItemViewHolder(
            SingleItemViewWithTextBoxBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SingleItemViewHolder).bind(position)
    }

    inner class SingleItemViewHolder(private val binding: SingleItemViewWithTextBoxBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.apply {
                checkBox.text = list[position].name
                checkBox.isChecked = list[position].checkBox?:false
                checkBox.setOnClickListener {
                    Log.d("THE DATA IS ","${checkBox.isChecked}")
                    checkBox.isChecked = checkBox.isChecked
                    list[position].checkBox = checkBox.isChecked
//                    Collections.swap(list,position,0)
//                    notifyItemMoved(position,0)
//                    notifyItemMoved(1,position)
                    execute.invoke(list[position],position,list)
                    executePendingBindings()
                }
            }
        }
    }

    fun updateList(form: ArrayList<Form>){
//        this.list.clear()
//        this.list.addAll(form)
        list = form
        notifyItemRangeChanged(0,form.size)
    }

    companion object{
        var execute : (data: Form,position : Int,list : ArrayList<Form>) -> Unit = { form: Form, i: Int ,list :ArrayList<Form>-> }
    }
}