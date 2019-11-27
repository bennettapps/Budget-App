package com.example.budget_app.presenter

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.ContentViewEvent
import com.example.budget_app.R
import com.example.budget_app.model.CategoryDB
import com.example.budget_app.view.CategoryAdapter
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.new_category_popup.view.*
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.move_popup.*

class CategoryPresenter(val context: Context, val myView: View) {

    private var db = DatabaseHandler(context, CategoryDB())

    fun startUp(application: Application) {
        AppCenter.start( // start AppCenter
            application, "019a5c56-13b5-4917-ae15-6e2155ac1873",
            Analytics::class.java, Crashes::class.java, Distribute::class.java
        )

        Fabric.with(context, Crashlytics()) // start fabric.io

        Answers.getInstance().logContentView(
            ContentViewEvent()
                .putContentName("Start")
                .putContentId("0")
                .putCustomAttribute("Started Successfully", "true")
        )

        if(db.getCount() == 0) {
            db.create(listOf("To Be Budgeted", 0))
        }

        updateAdapter()
    }

    fun updateAdapter() {
        val list = db.readAll()
        list.removeAt(0)
        list.reverse()

        val adapter = CategoryAdapter(list, context)
        myView.categoryRecyclerView.layoutManager = LinearLayoutManager(context)
        myView.categoryRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        val toBeText = myView.findViewById<TextView>(R.id.toBeAmount)
        toBeText.text = "$${db.read(0)[1]}.00"
    }

    fun deleteCategory(id: Int) {
        db.delete(id)
        updateAdapter()
    }

    fun editCategory(id: Int) {
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

    fun moveCategory(listId: Int) {
        val popup = LayoutInflater.from(context).inflate(R.layout.move_popup, null)
        val dialogue = AlertDialog.Builder(context).setView(popup).create()
        dialogue.show()

        val fromSpinner = dialogue.findViewById<Spinner>(R.id.fromSpinner)
        val toSpinner = dialogue.findViewById<Spinner>(R.id.toSpinner)

        val dbItems = db.readAll()
        val showItems = dbItems
        val removed = showItems.removeAt(0)
        showItems.reverse()
        showItems.add(0, removed)

        val strings = mutableListOf<String>()
        for(item in showItems) {
            strings.add(item[0].toString())
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item, strings
        )

        fromSpinner!!.adapter = adapter
        fromSpinner.setSelection(0)
        toSpinner!!.adapter = adapter
        toSpinner.setSelection(listId + 1)

        dialogue.moveButton.setOnClickListener {
            val subIndex = fromSpinner.selectedItemPosition
            val addIndex = toSpinner.selectedItemPosition
            val amount = dialogue.moveAmount.text.toString().toInt()

            db.update(showItems[subIndex][2] as Int, listOf(showItems[subIndex][0], showItems[subIndex][1].toString().toInt() - amount))
            db.update(showItems[addIndex][2] as Int, listOf(showItems[addIndex][0], showItems[addIndex][1].toString().toInt() + amount))

            updateAdapter()
            dialogue.dismiss()
        }
    }
}