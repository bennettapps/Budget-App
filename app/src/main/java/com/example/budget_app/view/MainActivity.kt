package com.example.budget_app.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHandler

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

        val choreList = db.readAll()
        choreList.reverse()

        val adapter = CategoryAdapter(choreList, this)
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

        dialogue.findViewById<Button>(R.id.categorySave)!!.setOnClickListener{
            db.create(listOf("Name", 100))
            dialogue.dismiss()
            finish()
        }
    }
}