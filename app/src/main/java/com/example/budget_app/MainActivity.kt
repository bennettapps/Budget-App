package com.example.budget_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import android.widget.LinearLayout
import android.widget.TextView
import android.view.ViewGroup
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var ll: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCenter.start(
            application, "019a5c56-13b5-4917-ae15-6e2155ac1873",
            Analytics::class.java, Crashes::class.java, Distribute::class.java
        )
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())

        setContentView(R.layout.content_main)

        val button = findViewById<View>(R.id.floatingActionButton)
        val button2 = findViewById<View>(R.id.floatingActionButton2)

        ll = findViewById(R.id.linearLayout)

        button.setOnClickListener {
            createButton("New Category", "$60.00")
        }

        button2.setOnClickListener {
            deleteLastButton()
        }
    }

    private fun createButton(categoryName: String, price: String) {
        val newLayout = LinearLayout(this)
        val categoryText = TextView(this)
        val priceText = TextView(this)

        ll.addView(newLayout)
        newLayout.addView(categoryText)
        newLayout.addView(priceText)

        newLayout.orientation = LinearLayout.HORIZONTAL
        newLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        categoryText.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        priceText.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        priceText.textAlignment = View.TEXT_ALIGNMENT_TEXT_END

        categoryText.text = categoryName
        priceText.text = price
    }

    private fun deleteLastButton() {
        if(ll.childCount > 0) {
            ll.removeViewAt(ll.childCount - 1)
        }
    }
}
