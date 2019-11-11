package com.example.budget_app.presenter

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.android.synthetic.main.category_popup.view.*

class CategoryPresenter(val context: Context, val recyclerView: RecyclerView) {

    private var db = DatabaseHandler(context, CategoryDB())

    fun startUp(application: Application, toBeBudgeted: View) {
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

        toBeBudgeted.findViewById<TextView>(R.id.toBeAmount).text = "$${db.readAll()[0][1]}.00"
    }

    fun updateAdapter() {
        val list = db.readAll()
        list.removeAt(0)
        list.reverse()

        val adapter = CategoryAdapter(list, context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun deleteCategory(id: Int) {
        db.delete(id)
        updateAdapter()
    }

    fun editCategory(id: Int) {
        val popup = LayoutInflater.from(context).inflate(R.layout.category_popup, null)
        val dialogue = AlertDialog.Builder(context).setView(popup).create()
        dialogue.show()

        dialogue.findViewById<Button>(R.id.categorySave)!!.setOnClickListener {
            val input = popup.categoryAddName.text.toString()

            if (!TextUtils.isEmpty(input)) {
                db.update(id, listOf(input, 0))
                updateAdapter()
                dialogue.dismiss()
            } else {
                Toast.makeText(context, "Please enter a name", Toast.LENGTH_LONG).show()
            }
        }
    }
}