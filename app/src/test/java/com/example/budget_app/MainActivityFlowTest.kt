package com.example.budget_app

import android.app.Activity
import android.app.Dialog
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

    private fun clickThroughPopup(): Dialog {
        shadow.clickMenuItem(R.id.add_menu_button)

        val shadowDialog = ShadowAlertDialog.getLatestDialog()
        shadowDialog.categoryAddName.setText("Test")
        shadowDialog.categorySave.performClick()

        return shadowDialog
    }

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
        val popup = clickThroughPopup()
        assertFalse(popup.isShowing)
    }

    @Test
    fun saveAddsToDatabase() {
        val db = DatabaseHandler(activity, CategoryDB())
        val initialCount = db.getCount()

        clickThroughPopup()

        assert(db.getCount() == initialCount + 1)
    }

    @Test
    fun saveAddsToRecyclerView() {
        val recyclerViewChildren = shadow.contentView.categoryRecyclerView.childCount

        clickThroughPopup()

        val newChildCount = shadow.contentView.categoryRecyclerView.childCount
        assert(newChildCount == recyclerViewChildren + 1)
    }

    @Test
    fun titleIsSameInDB() {
        val recyclerViewCount = shadow.contentView.categoryRecyclerView.childCount
        val db = DatabaseHandler(activity, CategoryDB())

        clickThroughPopup()

        assert(db.readAll()[recyclerViewCount][0] == "Test")
    }
}
