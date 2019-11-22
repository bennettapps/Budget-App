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
import com.example.budget_app.view.AccountsAdapter
import kotlinx.android.synthetic.main.fragment_accounts.view.*

class AccountsPresenter(val context: Context, val myView: View) {

    private var db = DatabaseHandler(context, AccountDB())

    fun updateAdapter() {
        val list = db.readAll()
        list.reverse()

        val adapter = AccountsAdapter(list, context)
        myView.accountsRecyclerView.layoutManager = LinearLayoutManager(context)
        myView.accountsRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun deleteAccount(id: Int) {
        db.delete(id)
        updateAdapter()
    }

    fun editAccount(id: Int) {
        val popup = LayoutInflater.from(context).inflate(R.layout.new_category_popup, null)
        val dialogue = AlertDialog.Builder(context).setView(popup).create()
        dialogue.show()

        dialogue.findViewById<Button>(R.id.saveButton)!!.setOnClickListener {
            val input = popup.AddName.text.toString()

            if (!TextUtils.isEmpty(input)) {
                db.update(id, listOf(input, db.read(id - 1)[1]))
                updateAdapter()
                dialogue.dismiss()
            } else {
                Toast.makeText(context, "Please enter a name", Toast.LENGTH_LONG).show()
            }
        }
    }
}