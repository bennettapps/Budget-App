package com.example.budget_app.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.budget_app.R
import com.example.budget_app.model.AccountDB
import com.example.budget_app.model.CategoryDB
import com.example.budget_app.presenter.AccountsPresenter
import com.example.budget_app.presenter.DatabaseHandler
import com.example.budget_app.presenter.TransactionsPresenter

class TransactionsAdapter(private val list: ArrayList<List<Any>>, private val context: Context):
    RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.transaction_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val amount = itemView.findViewById<TextView>(R.id.transactionAmount)
        private val vendor = itemView.findViewById<TextView>(R.id.transactionVendor)
        private val category = itemView.findViewById<TextView>(R.id.transactionCategory)
        private val account = itemView.findViewById<TextView>(R.id.transactionAccount)

        private val deleteButton = itemView.findViewById<Button>(R.id.deleteTransactionButton)
        private val editButton = itemView.findViewById<Button>(R.id.editTransactionButton)

        fun bindViews(items: List<Any>) {
            amount.text = items[0].toString()
            vendor.text = items[1].toString()
            category.text = DatabaseHandler(context, CategoryDB()).read(0)[items[2] as Int].toString()
            account.text = DatabaseHandler(context, AccountDB()).read(0)[items[3] as Int].toString()

            deleteButton.setOnClickListener(this)
            editButton.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            val transactionsPresenter = TransactionsPresenter(context, itemView.rootView)
            val db = DatabaseHandler(context, AccountDB())
            val dbArray = db.readAll()
            dbArray.reverse()

            when(view!!.id) {
                deleteButton.id -> {
                    transactionsPresenter.deleteAccount(dbArray[position][2] as Int)
                }
                editButton.id -> {
                    transactionsPresenter.editAccount(dbArray[position][2] as Int)
                }
            }
        }
    }
}
