package com.devshawon.curehealthcare.ui.fragments.filter


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devshawon.curehealthcare.databinding.SingleItemViewWithTextBoxBinding
import com.devshawon.curehealthcare.models.Form
import java.util.Collections

class SingleItemAdapterForm(private val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
                    checkBox.isChecked = checkBox.isChecked
                    list[position].checkBox = checkBox.isChecked
                    execute.invoke(list[position],position,checkBox.isChecked)
                    executePendingBindings()
                }
            }
        }
    }

    fun updateList(form: ArrayList<Form>){
        list = form
        notifyItemRangeChanged(0,form.size)
    }

    companion object{
        var execute : (data: Form,position : Int,isSelected :Boolean) -> Unit = { form: Form, i: Int ,isSelected :Boolean-> }
    }
}