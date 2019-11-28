package com.example.budget_app

import android.app.Activity
import android.os.Build
import android.view.View
import com.example.budget_app.view.NavigationActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class TransactionsUITest {
    private var activity: Activity = Robolectric.setupActivity(NavigationActivity::class.java)
    private val shadow = Shadows.shadowOf(activity)

    @Test
    fun recyclerViewNotNull() {
        shadow.contentView.findViewById<View>(R.id.transactionButton).callOnClick()
        assertNotNull(activity.findViewById(R.id.transactionsRecyclerView))
    }
}