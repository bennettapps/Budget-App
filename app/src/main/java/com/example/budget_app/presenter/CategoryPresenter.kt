package com.example.budget_app.presenter

import android.app.Application
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.ContentViewEvent
import com.example.budget_app.model.CategoryDB
import com.example.budget_app.view.CategoryAdapter
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute
import io.fabric.sdk.android.Fabric

class CategoryPresenter(val context: Context, val recyclerView: RecyclerView) {

    private lateinit var db: DatabaseHandler

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

        db = DatabaseHandler(context, CategoryDB())
    }

    fun updateAdapter() {
        val choreList = db.readAll()
        choreList.reverse()

        val adapter = CategoryAdapter(choreList, context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}