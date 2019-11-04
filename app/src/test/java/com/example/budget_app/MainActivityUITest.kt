package com.example.budget_app

import android.app.Activity
import android.os.Build
import com.example.budget_app.view.MainActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainActivityTest {
    private var activity: Activity = Robolectric.setupActivity(MainActivity::class.java)

    @Test
    fun activityNotNull() {
        assertNotNull(activity)
    }

    @Test
    fun recyclerViewNotNull() {
        assertNotNull(activity.findViewById(R.id.categoryRecyclerView))
    }

    @Test
    fun addButtonNotNull() {
        assertNotNull(activity.findViewById(R.id.add_menu_button))
    }

    @Test
    fun homeButtonNotNull() {
        assertNotNull(activity.findViewById(R.id.homeButton))
    }

    @Test
    fun accountButtonNotNull() {
        assertNotNull(activity.findViewById(R.id.accountButton))
    }

    @Test
    fun transactionButtonNotNull() {
        assertNotNull(activity.findViewById(R.id.transactionButton))
    }

    @Test
    fun toBeBudgetedNotNull() {
        assertNotNull(activity.findViewById(R.id.toBeText))
    }
}