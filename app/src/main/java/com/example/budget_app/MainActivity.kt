package com.example.budget_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import com.crashlytics.android.answers.ContentViewEvent
import com.crashlytics.android.answers.Answers



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCenter.start(
            application, "019a5c56-13b5-4917-ae15-6e2155ac1873",
            Analytics::class.java, Crashes::class.java, Distribute::class.java
        )
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())

        setContentView(R.layout.content_main)

        // Get the widgets
        val textView = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)
        val inputField = findViewById<EditText>(R.id.editText)
        val enterButton = findViewById<Button>(R.id.button2)

        // Set a click listener for button widget
        button.setOnClickListener{
            textView.text = getString(R.string.text_clicked)
            Answers.getInstance().logContentView(
                ContentViewEvent()
                    .putContentName("Click")
                    .putContentType("Button")
                    .putContentId("0001")
            )
        }

        var input = ""
        enterButton.setOnClickListener{
            input = inputField.text.toString()
            enterButton.text = input
            Answers.getInstance().logContentView(
                ContentViewEvent()
                    .putContentName("Input")
                    .putContentType("Text")
                    .putContentId("0002")
                    .putCustomAttribute("Input Text", input)
            )
        }
    }
}
