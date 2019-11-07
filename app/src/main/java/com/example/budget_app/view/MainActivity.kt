package com.example.budget_app.view

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.budget_app.R
import com.example.budget_app.model.CategoryDB
import com.example.budget_app.presenter.CategoryPresenter
import com.example.budget_app.presenter.DatabaseHandler
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.category_popup.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHandler
    private lateinit var categoryPresenter: CategoryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // start app
        setContentView(R.layout.activity_main) // set content view

        db = DatabaseHandler(this, CategoryDB())

        categoryPresenter = CategoryPresenter(this, categoryRecyclerView)
        categoryPresenter.startUp(application)

        categoryPresenter.updateAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
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
        val dialogue = AlertDialog.Builder(this).setView(view).create()
        dialogue.show()

        dialogue.findViewById<Button>(R.id.categorySave)!!.setOnClickListener {
            val input = view.categoryAddName.text.toString()

            if (!TextUtils.isEmpty(input)) {
                db.create(listOf(input, 0))
                dialogue.dismiss()
                categoryPresenter.updateAdapter()
            } else {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show()
            }
        }
    }
}
