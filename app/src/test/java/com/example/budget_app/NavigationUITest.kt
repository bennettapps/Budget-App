package com.example.budget_app

import android.app.Activity
import android.os.Build
import com.example.budget_app.view.NavigationActivity
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class NavigationUITest {
    private var activity: Activity = Robolectric.setupActivity(NavigationActivity::class.java)

    @Test
    fun activityNotNull() {
        Assert.assertNotNull(activity)
    }

    @Test
    fun addButtonNotNull() {
        Assert.assertNotNull(activity.findViewById(R.id.add_menu_button))
    }

    @Test
    fun homeButtonNotNull() {
        Assert.assertNotNull(activity.findViewById(R.id.homeButton))
    }

    @Test
    fun accountButtonNotNull() {
        Assert.assertNotNull(activity.findViewById(R.id.accountButton))
    }

    @Test
    fun transactionButtonNotNull() {
        Assert.assertNotNull(activity.findViewById(R.id.transactionButton))
    }
}