package com.example.budget_app

import android.app.Activity
import android.os.Build
import com.example.budget_app.view.NavigationActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainActivityTest {
    private var activity: Activity = Robolectric.setupActivity(NavigationActivity::class.java)

    @Test
    fun recyclerViewNotNull() {
        assertNotNull(activity.findViewById(R.id.categoryRecyclerView))
    }

    @Test
    fun toBeBudgetedNotNull() {
        assertNotNull(activity.findViewById(R.id.toBeText))
    }
}