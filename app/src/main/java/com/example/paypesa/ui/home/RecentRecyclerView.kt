package com.example.paypesa.ui.home

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.paypesa.R
import com.example.paypesa.data.model.TransactionModel
import com.example.paypesa.databinding.RecentActivityItemBinding

class RecentRecyclerView(private val transactionList: List<TransactionModel?>): RecyclerView.Adapter<RecentRecyclerView.RecentViewHolder>() {
    class RecentViewHolder(itemView: View): ViewHolder(itemView) {
        val binding = RecentActivityItemBinding.bind(itemView)

        @RequiresApi(Build.VERSION_CODES.O)
        fun setup(transaction: TransactionModel) {
            binding.transacterName.text = transaction.user
            binding.amountTransacted.text = "$${transaction.amount}"
            var dateValue = "${transaction.date.month} ${transaction.date.dayOfMonth}"
            if(transaction.amount > 1) {
                dateValue += ". Money recieved"
            } else {
                dateValue += ". Money sent"
            }
            binding.transactionDate.text = dateValue
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(
            R.layout.recent_activity_item, parent, false
        )
        return RecentViewHolder(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        val transaction = transactionList[position]
        transaction?.let {
            holder.setup(it)
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}