package com.example.budget_app

import android.os.Bundle
import android.view.Gravity
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
import android.view.ViewGroup
import android.view.View
import android.widget.RelativeLayout



class MainActivity : AppCompatActivity() {

    private lateinit var ll: LinearLayout
    private var creating = false

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
            if(!creating)
                createButton()
        }

        button2.setOnClickListener {
            if(!creating)
                deleteLastButton()
        }
    }

    private fun createButton() {
        creating = true

        val newLayout = LinearLayout(this)
        val nameInput = EditText(this)
        val createButton = Button(this)

        var name: String

        ll.addView(newLayout)

        newLayout.orientation = LinearLayout.HORIZONTAL
        newLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        newLayout.addView(nameInput)
        newLayout.addView(createButton)

        createButton.text = "Create"

        createButton.setOnClickListener {
            name = nameInput.text.toString()
            newLayout.removeAllViews()

            val categoryText = TextView(this)
            val priceText = TextView(this)

            newLayout.addView(categoryText)
            newLayout.addView(priceText)

            categoryText.text = name
            categoryText.textSize = 20f
            priceText.text = "$0.00"
            priceText.textSize = 20f

            creating = false
        }
    }

    private fun deleteLastButton() {
        if(ll.childCount > 0) {
            ll.removeViewAt(ll.childCount - 1)
        }
    }
}
