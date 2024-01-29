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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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
                    dateTitle.text = dateToHowManyAgo(it.createdAt?.replace(".000000Z","",false)!!)
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

    fun dateToHowManyAgo(stringDate: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val currDate = LocalDateTime.now()
        val pastDate = LocalDateTime.parse(stringDate, formatter)
        val diffSeconds = ChronoUnit.SECONDS.between(pastDate, currDate)

        if (diffSeconds < 60)
            return "$diffSeconds second${if (diffSeconds != 1L) "s" else ""} ago"

        val diffMinutes = diffSeconds / 60
        if (diffMinutes < 60)
            return "$diffMinutes minute${if (diffMinutes != 1L) "s" else ""} ago"

        val diffHours = diffMinutes / 60
        if (diffHours < 24)
            return "$diffHours hour${if (diffHours != 1L) "s" else ""} ago"

        val diffDays = diffHours / 24
        if (diffDays < 30)
            return "$diffDays day${if (diffDays != 1L) "s" else ""} ago"

        val diffMonths = diffDays / 30
        if (diffMonths < 12)
            return "$diffMonths month${if (diffMonths != 1L) "s" else ""} ago"

        val diffYears = diffMonths / 12
        return "$diffYears year${if (diffYears != 1L) "s" else ""} ago"
    }
}