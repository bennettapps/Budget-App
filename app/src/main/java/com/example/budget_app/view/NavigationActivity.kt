package com.example.budget_app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.example.budget_app.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationActivity : AppCompatActivity() {
    lateinit var toolbar: ActionBar
    lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.nav_view)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val home = HomeFragment.newInstance()
        openFragment(home)
        currentFragment = home
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.homeButton -> {
                toolbar.title = "Home"
                val home = HomeFragment.newInstance()
                openFragment(home)
                currentFragment = home
                return@OnNavigationItemSelectedListener true
            }
            R.id.accountButton -> {
                toolbar.title = "Accounts"
                val accounts = AccountsFragment.newInstance()
                openFragment(accounts)
                currentFragment = accounts
                return@OnNavigationItemSelectedListener true
            }
            R.id.transactionButton -> {
                toolbar.title = "Transactions"
                val transactions = TransactionsFragment.newInstance()
                openFragment(transactions)
                currentFragment = transactions
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }
}
