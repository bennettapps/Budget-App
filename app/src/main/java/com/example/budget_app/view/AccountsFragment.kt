package com.example.budget_app.view

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.budget_app.R
import com.example.budget_app.model.AccountDB
import com.example.budget_app.model.CategoryDB
import com.example.budget_app.presenter.AccountsPresenter
import com.example.budget_app.presenter.DatabaseHandler
import kotlinx.android.synthetic.main.new_account_popup.view.*
import kotlinx.android.synthetic.main.new_category_popup.view.AddName

class AccountsFragment : Fragment() {
    private lateinit var accountDB: DatabaseHandler
    private lateinit var categoryDB: DatabaseHandler
    private lateinit var accountsPresenter: AccountsPresenter
    private lateinit var myView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.fragment_accounts, container, false)

        accountDB = DatabaseHandler(myView.context, AccountDB())
        categoryDB = DatabaseHandler(HomeFragment.newInstance().requireView().context, CategoryDB())

        accountsPresenter = AccountsPresenter(myView.context, myView)
        accountsPresenter.updateAdapter()

        setHasOptionsMenu(true)

        return myView
    }

    companion object {
        fun newInstance(): AccountsFragment = AccountsFragment()
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
        val view = layoutInflater.inflate(R.layout.new_account_popup, null)
        val dialogue = AlertDialog.Builder(context!!).setView(view).create()
        dialogue.show()

        dialogue.findViewById<Button>(R.id.saveButton)!!.setOnClickListener {
            val input = view.AddName.text.toString()
            val balance = view.AddAmount.text.toString().toInt()

            if (!TextUtils.isEmpty(input)) {
                val toBeBudgeted = categoryDB.read(0)

                accountDB.create(listOf(input, balance))
                categoryDB.update(0, listOf(toBeBudgeted[0], (toBeBudgeted[1] as Int) + balance))

                dialogue.dismiss()
                accountsPresenter.updateAdapter()
            } else {
                Toast.makeText(context!!, "Please enter a name", Toast.LENGTH_LONG).show()
            }
        }
    }
}