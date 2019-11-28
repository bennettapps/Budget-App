package com.example.budget_app

import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.view.View
import android.widget.Button
import com.example.budget_app.model.CategoryDB
import com.example.budget_app.model.TransactionDB
import com.example.budget_app.presenter.DatabaseHandler
import com.example.budget_app.view.NavigationActivity
import kotlinx.android.synthetic.main.fragment_transactions.view.*
import kotlinx.android.synthetic.main.new_account_popup.AddAmount
import kotlinx.android.synthetic.main.new_category_popup.saveButton
import kotlinx.android.synthetic.main.transaction_popup.*
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
class TransactionsFlowTest {
    private var activity: Activity = Robolectric.setupActivity(NavigationActivity::class.java)
    private val shadow = shadowOf(activity)

    private fun clickThroughPopup(name: String): Dialog {
        shadow.clickMenuItem(R.id.add_menu_button)

        val shadowDialog = ShadowAlertDialog.getLatestDialog()
        shadowDialog.AddAmount.setText("10")
        shadowDialog.vendorName.setText(name)
        shadowDialog.saveButton.performClick()

        return shadowDialog
    }

    private fun switchFragment() {
        shadow.contentView.findViewById<View>(R.id.transactionButton).callOnClick()
    }

    @Test
    fun nothingGivesNoPopups() {
        switchFragment()
        assert(ShadowAlertDialog.getShownDialogs().isEmpty())
    }

    @Test
    fun addButtonClicks() {
        switchFragment()
        shadow.clickMenuItem(R.id.add_menu_button)
        assert(ShadowAlertDialog.getShownDialogs().isNotEmpty())
    }

    @Test
    fun saveRemovesPopup() {
        switchFragment()
        val popup = clickThroughPopup("Removes")
        assertFalse(popup.isShowing)
    }

    @Test
    fun saveAddsToDatabase() {
        switchFragment()
        val db = DatabaseHandler(activity, TransactionDB())
        val initialCount = db.getCount()

        clickThroughPopup("Saved")

        assert(db.getCount() == initialCount + 1)
    }

    @Test
    fun saveAddsToRecyclerView() {
        switchFragment()
        val recyclerViewChildren = shadow.contentView.transactionsRecyclerView.childCount

        clickThroughPopup("Added")

        val newChildCount = shadow.contentView.transactionsRecyclerView.childCount
        assert(newChildCount == recyclerViewChildren + 1)
    }

    @Test
    fun titleIsSameInDB() {
        switchFragment()
        val recyclerViewCount = shadow.contentView.transactionsRecyclerView.childCount
        val db = DatabaseHandler(activity, TransactionDB())

        clickThroughPopup("Test")

        assert(db.read(recyclerViewCount)[0] == "Test")
    }

    @Test
    fun deleteButtonWorks() {
        switchFragment()
        val db = DatabaseHandler(activity, TransactionDB())

        clickThroughPopup("Delete")

        val count = db.getCount()

        val view = shadow.contentView.transactionsRecyclerView.getChildAt(db.getCount() - 1)
        view.findViewById<Button>(R.id.deleteTransactionButton).callOnClick()

        assert(db.getCount() == count - 1)
    }

    @Test
    fun editButtonWorks() {
        switchFragment()
        val db = DatabaseHandler(activity, TransactionDB())

        clickThroughPopup("Edit")

        val view = shadow.contentView.transactionsRecyclerView.getChildAt(shadow.contentView.transactionsRecyclerView.childCount - 1)
        view.findViewById<Button>(R.id.editTransactionButton).callOnClick()

        clickThroughPopup("New")

        assert(db.read(db.getCount() - 1)[0] == "New")
    }

    @Test
    fun balanceAddsToCategory() {
        switchFragment()
        val categories = DatabaseHandler(activity, CategoryDB())
        val currentToBeAmount = categories.read(0)[1]

        clickThroughPopup("Account")

        assert(categories.read(0)[1] == currentToBeAmount as Int + 10)
    }
}
