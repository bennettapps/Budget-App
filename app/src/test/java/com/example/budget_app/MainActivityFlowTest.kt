package com.example.budget_app

import android.app.Activity
import android.os.Build
import com.example.budget_app.model.CategoryDB
import com.example.budget_app.presenter.DatabaseHandler
import com.example.budget_app.view.MainActivity
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.category_popup.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainActivityFlowTest {
    private var activity: Activity = Robolectric.setupActivity(MainActivity::class.java)
    private val shadow = shadowOf(activity)

    @Test
    fun nothingGivesNoPopups() {
        assert(ShadowAlertDialog.getShownDialogs().isEmpty())
    }

    @Test
    fun addButtonClicks() {
        shadow.clickMenuItem(R.id.add_menu_button)
        assert(ShadowAlertDialog.getShownDialogs().isNotEmpty())
    }

    @Test
    fun saveRemovesPopup() {
        shadow.clickMenuItem(R.id.add_menu_button)

        val shadowDialog = ShadowAlertDialog.getLatestDialog()
        shadowDialog.categorySave.performClick()

        assertFalse(shadowDialog.isShowing)
    }

    @Test
    fun saveAddsToDatabase() {
        val db = DatabaseHandler(activity, CategoryDB())
        val initialCount = db.getCount()
        shadow.clickMenuItem(R.id.add_menu_button)

        val shadowDialog = ShadowAlertDialog.getLatestDialog()
        shadowDialog.categorySave.performClick()

        assert(db.getCount() == initialCount + 1)
    }

    @Test
    fun saveAddsToRecyclerView() {
        val recyclerViewChildren = shadow.contentView.categoryRecyclerView.childCount
        shadow.clickMenuItem(R.id.add_menu_button)

        val shadowDialog = ShadowAlertDialog.getLatestDialog()
        shadowDialog.categorySave.performClick()

        val newChildCount = shadow.contentView.categoryRecyclerView.childCount
        assert(newChildCount == recyclerViewChildren + 1)
    }
}
