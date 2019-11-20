package com.example.budget_app.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.budget_app.R
import com.example.budget_app.model.CategoryDB
import com.example.budget_app.presenter.CategoryPresenter
import com.example.budget_app.presenter.DatabaseHandler
import kotlinx.android.synthetic.main.category_popup.view.*

class HomeFragment : Fragment() {
    private lateinit var db: DatabaseHandler
    private lateinit var categoryPresenter: CategoryPresenter
    private lateinit var myView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.fragment_home, container, false)

        db = DatabaseHandler(myView.context, CategoryDB())

        categoryPresenter = CategoryPresenter(myView.context, myView)
        categoryPresenter.startUp(activity!!.application)

        setHasOptionsMenu(true)

        return myView
    }

    companion object {
        fun newInstance(): HomeFragment =
            HomeFragment()
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
        val view = layoutInflater.inflate(R.layout.category_popup, null)
        val dialogue = AlertDialog.Builder(context!!).setView(view).create()
        dialogue.show()

        dialogue.findViewById<Button>(R.id.saveButton)!!.setOnClickListener {
            val input = view.categoryAddName.text.toString()

            if (!TextUtils.isEmpty(input)) {
                db.create(listOf(input, 0))
                dialogue.dismiss()
                categoryPresenter.updateAdapter()
            } else {
                Toast.makeText(context!!, "Please enter a name", Toast.LENGTH_LONG).show()
            }
        }
    }
}
