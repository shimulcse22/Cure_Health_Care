package com.devshawon.curehealthcare.ui.fragments.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.databinding.NotificationListItemDataBinding
import com.devshawon.curehealthcare.models.NotificationResponseData
import com.devshawon.curehealthcare.useCase.result.Event

class NotificationAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list :ArrayList<NotificationResponseData> = arrayListOf()
    var count  = 0
    var markCount = MutableLiveData(Event(0))
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotificationViewHolder(
            NotificationListItemDataBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NotificationViewHolder).bind(position)
    }

    inner class NotificationViewHolder(private val binding: NotificationListItemDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                list[position].let {
                    headerTitle.text = it.name
                    subHeaderTitle.text = it.description
                    dateTitle.text = it.createdAt
                    if(it.status == "Unread"){
                        if(count == 0){
                            count = 1
                            markCount.value = Event(1)
                        }
                        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_7))
                    }else{
                        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
                    }
                }
            }
        }
    }

    fun updateData(list : ArrayList<NotificationResponseData>){
        this.list.clear()
        this.list.addAll(list)
        notifyItemRangeChanged(0,list.size)
    }
    fun updateMoreData(list : ArrayList<NotificationResponseData>){
        this.list.addAll(list)
        notifyItemRangeChanged(this.list.size,list.size)
    }
}