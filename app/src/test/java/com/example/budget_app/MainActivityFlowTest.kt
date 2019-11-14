package com.example.budget_app

import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.widget.Button
import com.example.budget_app.model.CategoryDB
import com.example.budget_app.presenter.DatabaseHandler
import com.example.budget_app.view.MainActivity
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.category_popup.*
import kotlinx.android.synthetic.main.move_popup.*
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

    private fun clickThroughPopup(name: String): Dialog {
        shadow.clickMenuItem(R.id.add_menu_button)

        val shadowDialog = ShadowAlertDialog.getLatestDialog()
        shadowDialog.categoryAddName.setText(name)
        shadowDialog.saveButton.performClick()

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
        val popup = clickThroughPopup("Removes")
        assertFalse(popup.isShowing)
    }

    @Test
    fun saveAddsToDatabase() {
        val db = DatabaseHandler(activity, CategoryDB())
        val initialCount = db.getCount()

        clickThroughPopup("Saved")

        assert(db.getCount() == initialCount + 1)
    }

    @Test
    fun saveAddsToRecyclerView() {
        val recyclerViewChildren = shadow.contentView.categoryRecyclerView.childCount

        clickThroughPopup("Added")

        val newChildCount = shadow.contentView.categoryRecyclerView.childCount
        assert(newChildCount == recyclerViewChildren + 1)
    }

    @Test
    fun titleIsSameInDB() {
        val recyclerViewCount = shadow.contentView.categoryRecyclerView.childCount
        val db = DatabaseHandler(activity, CategoryDB())

        clickThroughPopup("Test")

        assert(db.read(recyclerViewCount + 1)[0] == "Test")
    }

    @Test
    fun deleteButtonWorks() {
        val db = DatabaseHandler(activity, CategoryDB())

        clickThroughPopup("Delete")

        val count = db.getCount()

        val view = shadow.contentView.categoryRecyclerView.getChildAt(db.getCount() - 2)
        view.findViewById<Button>(R.id.deleteCategoryButton).callOnClick()

        assert(db.getCount() == count - 1)
    }

    @Test
    fun editButtonWorks() {
        val db = DatabaseHandler(activity, CategoryDB())

        clickThroughPopup("Edit")

        val view = shadow.contentView.categoryRecyclerView.getChildAt(shadow.contentView.categoryRecyclerView.childCount - 1)
        view.findViewById<Button>(R.id.editCategoryButton).callOnClick()

        clickThroughPopup("New")

        assert(db.read(db.getCount() - 1)[0] == "New")
    }

    @Test
    fun firstCategoryAlwaysToBeBudgeted() {
        val db = DatabaseHandler(activity, CategoryDB())

        assert(db.read(0)[0] == "To Be Budgeted")
    }

    @Test
    fun toBeBudgetedAmountTrue() {
        val db = DatabaseHandler(activity, CategoryDB())
        val toBeAmount = shadow.contentView.toBeAmount

        assert("$${db.read(0)[1]}.00" == toBeAmount.text)
    }

    @Test
    fun moveButtonWorks() {
        val db = DatabaseHandler(activity, CategoryDB())

        clickThroughPopup("Move")

        val fromAmount = db.read(0)[1] as Int
        val toAmount = db.read(db.getCount() - 1)[1] as Int

        val view = shadow.contentView.categoryRecyclerView.getChildAt(shadow.contentView.categoryRecyclerView.childCount - 1)
        view.findViewById<Button>(R.id.moveCategoryButton).callOnClick()

        val shadowDialog = ShadowAlertDialog.getLatestDialog()
        shadowDialog.moveAmount.setText("10")
        shadowDialog.moveButton.performClick()

        assert(db.read(0)[1] == (fromAmount - 10))
        assert(db.read(db.getCount() - 1)[1] == (toAmount + 10))
    }
}
