package com.example.budget_app.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.budget_app.R

class CategoryAdapter(private val list: ArrayList<List<Any>>, private val context: Context):
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun bindViews(items: List<Any>) {
            val name = itemView.findViewById<TextView>(R.id.categoryName)
            val amount = itemView.findViewById<TextView>(R.id.categoryAmount)

            name.text = items[0].toString()
            amount.text = "$${items[1]}.00"
        }

        override fun onClick(p0: View?) {
            println("Clicked")
        }
    }
}
