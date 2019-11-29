package com.example.budget_app.view

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.budget_app.R
import com.example.budget_app.model.AccountDB
import com.example.budget_app.model.CategoryDB
import com.example.budget_app.model.TransactionDB
import com.example.budget_app.presenter.DatabaseHandler
import com.example.budget_app.presenter.TransactionsPresenter
import kotlinx.android.synthetic.main.transaction_popup.view.*

class TransactionsFragment : Fragment() {
    private lateinit var accountDB: DatabaseHandler
    private lateinit var categoryDB: DatabaseHandler
    private lateinit var transactionsDB: DatabaseHandler
    private lateinit var transactionsPresenter: TransactionsPresenter
    private lateinit var myView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.fragment_transactions, container, false)

        accountDB = DatabaseHandler(context!!, AccountDB())
        categoryDB = DatabaseHandler(context!!, CategoryDB())
        transactionsDB = DatabaseHandler(context!!, TransactionDB())

        transactionsPresenter = TransactionsPresenter(context!!, myView)
        transactionsPresenter.updateAdapter()

        setHasOptionsMenu(true)

        if(transactionsDB.getCount() > 0) {
            for (item in transactionsDB.read(0)) {
                println(item)
            }
        }

        return myView
    }

    companion object {
        fun newInstance(): TransactionsFragment = TransactionsFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_menu_button -> {
                createPopup()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createPopup() {
        val view = layoutInflater.inflate(R.layout.transaction_popup, null)
        val dialogue = AlertDialog.Builder(context!!).setView(view).create()
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
            context!!,
            android.R.layout.simple_spinner_item, categoryStrings
        )

        val accountAdapter: ArrayAdapter<String> = ArrayAdapter(
            context!!,
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

                val categoryDBIndex = showCategoryItems[categoryIndex][2].toString().toInt() - 1
                val accountDBIndex = showAccountItems[accountIndex][2].toString().toInt() - 1

                categoryDB.update(categoryDBIndex, listOf(categoryDB.read(categoryDBIndex)[0], categoryDB.read(categoryDBIndex)[1].toString().toLong() - amount))
                accountDB.update(accountDBIndex, listOf(accountDB.read(accountDBIndex)[0], accountDB.read(accountDBIndex)[1].toString().toLong() - amount))
                transactionsDB.create(listOf(amount, vendor, categoryDBIndex, accountDBIndex))

                transactionsPresenter.updateAdapter()
                dialogue.dismiss()
            } else {
                Toast.makeText(context!!, "Please enter an amount", Toast.LENGTH_LONG).show()
            }
        }
    }
}
