package com.devshawon.curehealthcare.ui.fragments.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devshawon.curehealthcare.databinding.SingleItemViewWithTextBoxBinding
import com.devshawon.curehealthcare.models.Form

class SingleItemAdapter(private val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val list : ArrayList<Form> = arrayListOf()
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
                    checkBox.isChecked = !checkBox.isChecked
                    val data = Form()
                    data.id = list[position].id
                    data.name = list[position].name
                    data.checkBox = checkBox.isChecked
                    execute.invoke(data,position)
                    executePendingBindings()
                }
            }
        }
    }

    fun updateList(form: ArrayList<Form>){
        this.list.clear()
        this.list.addAll(form)
        notifyItemRangeChanged(0,form.size)
    }

    companion object{
        var execute : (data: Form,position : Int) -> Unit = { form: Form, i: Int -> }
    }
}