package com.example.budget_app

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.crashlytics.android.answers.ContentViewEvent
import com.crashlytics.android.answers.Answers
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tabian.saveanddisplaysql.DatabaseHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var ll: LinearLayout
    private lateinit var nDatabaseHelper: DatabaseHelper
    private var creating = false

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                setTitle(R.string.title_budget)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                setTitle(R.string.title_accounts)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                setTitle(R.string.title_transaction)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCenter.start( // start AppCenter
            application, "019a5c56-13b5-4917-ae15-6e2155ac1873",
            Analytics::class.java, Crashes::class.java, Distribute::class.java
        )
        super.onCreate(savedInstanceState) // start app
        Fabric.with(this, Crashlytics()) // start fabric.io

        setContentView(R.layout.activity_main) // set content view

        nDatabaseHelper = DatabaseHelper(this) // initialize database

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val toolbar = findViewById<Toolbar>(R.id.app_bar)
        setSupportActionBar(toolbar)

        val newLayout = LinearLayout(this)
        ll = findViewById(R.id.linearLayout)
        ll.addView(newLayout)

        newLayout.orientation = LinearLayout.HORIZONTAL
        newLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        createCategory(newLayout, "To Be Budgeted", 100.00f)
//        nDatabaseHelper.addData("")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!creating) {
            when (item.getItemId()) {
                R.id.add -> {
                    Answers.getInstance().logContentView(
                        ContentViewEvent()
                            .putContentName("Add Category to List")
                            .putContentType("Click")
                            .putContentId("1001")
                    )
                    createButton()
                }
                R.id.edit -> {
                    Answers.getInstance().logContentView(
                        ContentViewEvent()
                            .putContentName("Remove Category from List")
                            .putContentType("Click")
                            .putContentId("1002")
                    )
                    deleteLastButton()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createButton() {
        creating = true

        val newLayout = LinearLayout(this)
        val nameInput = EditText(this)
        val button = Button(this)

        var name: String

        ll.addView(newLayout)

        newLayout.orientation = LinearLayout.HORIZONTAL
        newLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        nameInput.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        (nameInput.layoutParams as LinearLayout.LayoutParams).weight = 1f
        button.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        newLayout.addView(nameInput)
        newLayout.addView(button)

        button.text = "Create"

        button.setOnClickListener {
            name = nameInput.text.toString()

            Answers.getInstance().logContentView(
                ContentViewEvent()
                    .putContentName("Confirmed added Category")
                    .putContentType("Click")
                    .putContentId("1003")
                    .putCustomAttribute("Category Name", name)
            )

            createCategory(newLayout, name, 0.00f)
        }
    }

    private fun createCategory(newLayout: LinearLayout, name: String, amount: Float) {
        newLayout.removeAllViews()

        val categoryText = TextView(this)
        val priceText = TextView(this)

        newLayout.addView(categoryText)
        newLayout.addView(priceText)

        categoryText.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        (categoryText.layoutParams as LinearLayout.LayoutParams).weight = 1f
        priceText.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        categoryText.text = name
        categoryText.textSize = 20f
        priceText.text = "$$amount"
        priceText.textSize = 20f

        creating = false

        priceText.setOnClickListener {
            moveMoney()
        }
    }

    private fun moveMoney() {
        println("moving money!")
//        move from the first one
//        currentAmount: Int = ll.findViewWithTag<LinearLayout>(0).findViewWithTag<TextView>(1).text
    }

    private fun deleteLastButton() {
        if(ll.childCount > 1) { // can't delete the final category "to be budgeted"
            ll.removeViewAt(ll.childCount - 1)
        }
    }
}
