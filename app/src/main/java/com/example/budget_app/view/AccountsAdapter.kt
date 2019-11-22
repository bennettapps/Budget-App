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
import com.example.budget_app.presenter.AccountsPresenter
import com.example.budget_app.presenter.DatabaseHandler

class AccountsAdapter(private val list: ArrayList<List<Any>>, private val context: Context):
    RecyclerView.Adapter<AccountsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.accounts_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val name = itemView.findViewById<TextView>(R.id.accountName)
        private val amount = itemView.findViewById<TextView>(R.id.accountBalance)
        private val deleteButton = itemView.findViewById<Button>(R.id.deleteAccountButton)
        private val editButton = itemView.findViewById<Button>(R.id.editAccountButton)

        fun bindViews(items: List<Any>) {
            name.text = items[0].toString()
            amount.text = "$${items[1]}.00"

            deleteButton.setOnClickListener(this)
            editButton.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            val accountsPresenter = AccountsPresenter(context, itemView.rootView)
            val db = DatabaseHandler(context, AccountDB())
            val dbArray = db.readAll()
            dbArray.reverse()

            when(view!!.id) {
                deleteButton.id -> {
                    accountsPresenter.deleteAccount(dbArray[position][2] as Int)
                }
                editButton.id -> {
                    accountsPresenter.editAccount(dbArray[position][2] as Int)
                }
            }
        }
    }
}
