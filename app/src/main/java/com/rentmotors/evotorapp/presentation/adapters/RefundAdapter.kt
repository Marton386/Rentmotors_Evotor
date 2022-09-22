package com.rentmotors.evotorapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rentmotors.evotorapp.R
import com.rentmotors.evotorapp.domain.entities.BookReceipt
import com.rentmotors.evotorapp.presentation.utils.PriceFormatter

class RefundAdapter(private val context: Context, private val list: List<BookReceipt.Item>
):
    RecyclerView.Adapter<RefundAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val amount: TextView = view.findViewById<View>(R.id.tvAmount) as TextView
        val name: TextView = view.findViewById<View>(R.id.tvName) as TextView
        val price: TextView = view.findViewById<View>(R.id.tvPrice) as TextView
        val quantity: TextView = view.findViewById<View>(R.id.tvQuantity) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.item_check_position, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.amount.text = PriceFormatter.format(data.amount)
        holder.name.text = data.name
        holder.price.text = PriceFormatter.format(data.price)
        holder.quantity.text = context.resources.getString(R.string.quantity, data.quantity)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}