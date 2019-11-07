package com.example.budget_app.view

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import com.crashlytics.android.answers.ContentViewEvent
import com.crashlytics.android.answers.Answers
import com.example.budget_app.R
import com.example.budget_app.model.CategoryDB
import com.example.budget_app.presenter.DatabaseHandler
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.category_popup.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHandler
    private lateinit var choreList: ArrayList<List<Any>>
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCenter.start( // start AppCenter
            application, "019a5c56-13b5-4917-ae15-6e2155ac1873",
            Analytics::class.java, Crashes::class.java, Distribute::class.java
        )
        super.onCreate(savedInstanceState) // start app
        Fabric.with(this, Crashlytics()) // start fabric.io

        setContentView(R.layout.activity_main) // set content view

        Answers.getInstance().logContentView(
            ContentViewEvent()
                .putContentName("Start")
                .putContentId("0")
                .putCustomAttribute("Started Successfully", "true")
        )

        db = DatabaseHandler(this, CategoryDB())

        choreList = db.readAll()
        choreList.reverse()

        adapter = CategoryAdapter(choreList, this)

        categoryRecyclerView.layoutManager = LinearLayoutManager(this)
        categoryRecyclerView.adapter = adapter

        adapter.notifyDataSetChanged()
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
                choreList = db.readAll()
                choreList.reverse()
                adapter = CategoryAdapter(choreList, this)
                categoryRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show()
            }
        }
    }
}
