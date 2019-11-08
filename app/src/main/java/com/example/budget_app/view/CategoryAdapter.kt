package com.example.budget_app.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.budget_app.R
import com.example.budget_app.model.CategoryDB
import com.example.budget_app.presenter.DatabaseHandler

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
        private val name = itemView.findViewById<TextView>(R.id.categoryName)
        private val amount = itemView.findViewById<TextView>(R.id.categoryAmount)
        private val deleteButton = itemView.findViewById<Button>(R.id.deleteCategoryButton)

        fun bindViews(items: List<Any>) {
            name.text = items[0].toString()
            amount.text = "$${items[1]}.00"

            deleteButton.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            val db = DatabaseHandler(context, CategoryDB())

            when(view!!.id) {
                deleteButton.id -> {
                    db.delete(list[position][2] as Int)
                    notifyItemRemoved(position)
                    list.removeAt(position)
                }
            }
        }
    }
}
