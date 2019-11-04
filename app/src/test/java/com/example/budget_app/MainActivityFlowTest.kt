package com.example.budget_app

import android.app.Activity
import android.os.Build
import com.example.budget_app.view.MainActivity
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainActivityFlowTest {
    private var activity: Activity = Robolectric.setupActivity(MainActivity::class.java)

    @Test
    fun addButtonClicks() {
        val shadow = shadowOf(activity)
        shadow.clickMenuItem(R.id.add_menu_button)
        assertNotNull(ShadowAlertDialog.getShownDialogs())
    }
}