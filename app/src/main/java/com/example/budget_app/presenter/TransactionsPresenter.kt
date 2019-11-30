package com.example.budget_app.presenter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budget_app.R
import kotlinx.android.synthetic.main.new_category_popup.view.*
import com.example.budget_app.model.AccountDB
import com.example.budget_app.model.CategoryDB
import com.example.budget_app.model.TransactionDB
import com.example.budget_app.view.AccountsAdapter
import com.example.budget_app.view.TransactionsAdapter
import kotlinx.android.synthetic.main.fragment_accounts.view.*
import kotlinx.android.synthetic.main.fragment_transactions.view.*
import kotlinx.android.synthetic.main.transaction_popup.view.*

class TransactionsPresenter(val context: Context, val myView: View) {

    private var db = DatabaseHandler(context, TransactionDB())
    private var categoryDB = DatabaseHandler(context, CategoryDB())
    private var accountDB = DatabaseHandler(context, AccountDB())

    fun updateAdapter() {
        val list = db.readAll()
        list.reverse()

        val adapter = TransactionsAdapter(list, context)
        myView.transactionsRecyclerView.layoutManager = LinearLayoutManager(context)
        myView.transactionsRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun deleteAccount(id: Int) {
        db.delete(id)
        updateAdapter()
    }

    fun editAccount(id: Int) {
        val view = LayoutInflater.from(context).inflate(R.layout.transaction_popup, null)
        val dialogue = AlertDialog.Builder(context).setView(view).create()
        dialogue.show()

        val categorySpinner = view.categorySpinner
        val accountSpinner = view.accountSpinner

        val categoryItems = categoryDB.readAll()
        val showCategoryItems = categoryItems
        val removed = categoryItems.removeAt(0)
        showCategoryItems.reverse()
        showCategoryItems.add(0, removed)

        val accountItems = accountDB.readAll()
        val showAccountItems = accountItems
        showAccountItems.reverse()

        val categoryStrings = mutableListOf<String>()
        for(item in showCategoryItems) {
            categoryStrings.add(item[0].toString())
        }

        val accountStrings = mutableListOf<String>()
        for(item in showAccountItems) {
            accountStrings.add(item[0].toString())
        }

        val categoryAdapter: ArrayAdapter<String> = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item, categoryStrings
        )

        val accountAdapter: ArrayAdapter<String> = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item, accountStrings
        )

        categorySpinner!!.adapter = categoryAdapter
        categorySpinner.setSelection(0)
        accountSpinner!!.adapter = accountAdapter
        accountSpinner.setSelection(0)

        dialogue.findViewById<Button>(R.id.saveButton)!!.setOnClickListener {
            val amount = view.AddAmount.text.toString().toLong()
            val vendor = view.vendorName.text.toString()

            if (!TextUtils.isEmpty(amount.toString())) {
                val categoryIndex = categorySpinner.selectedItemPosition
                val accountIndex = accountSpinner.selectedItemPosition

                val categoryDBIndex = showCategoryItems[categoryIndex][2].toString().toInt()
                val accountDBIndex = showAccountItems[accountIndex][2].toString().toInt()

                categoryDB.update(categoryDBIndex, listOf(categoryDB.read(categoryDBIndex - 1)[0], categoryDB.read(categoryDBIndex - 1)[1].toString().toLong() - db.read(id - 1)[1].toString().toLong() + amount))
                accountDB.update(accountDBIndex, listOf(accountDB.read(accountDBIndex - 1)[0], accountDB.read(accountDBIndex - 1)[1].toString().toLong() - db.read(id - 1)[1].toString().toLong() + amount))

                db.update(id, listOf(amount, vendor, categoryDBIndex - 1, accountDBIndex - 1))

                updateAdapter()
                dialogue.dismiss()
            } else {
                Toast.makeText(context, "Please enter an amount", Toast.LENGTH_LONG).show()
            }
        }
    }
}