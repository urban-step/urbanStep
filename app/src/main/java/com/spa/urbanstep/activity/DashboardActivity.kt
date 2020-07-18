package com.spa.urbanstep.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.spa.carrythistoo.utils.DialogUtil
import com.spa.carrythistoo.utils.FragmentFactory
import com.spa.urbanstep.DashboardType
import com.spa.urbanstep.R
import com.spa.urbanstep.UserManager
import com.spa.urbanstep.fragments.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.nav_header_dashboared.view.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var toggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        tv_login_register.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })

        setupActionBar(toolbar)
        onNavigationItemSelected(nav_view.getMenu().getItem(0));
        nav_view.setCheckedItem(R.id.nav_home);
    }

    fun setupActionBar(toolbar: Toolbar?) {

        toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        toggle!!.isDrawerIndicatorEnabled = false
        toggle!!.setHomeAsUpIndicator(null)
        drawer_layout.addDrawerListener(toggle!!)
        toggle!!.syncState()

        toggle!!.setToolbarNavigationClickListener(View.OnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        })

        nav_view.setNavigationItemSelectedListener(this)

        val headerView: View = nav_view.getHeaderView(0)

        headerView.iv_close.setOnClickListener(View.OnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        })

        iv_menu_toggle.setOnClickListener(View.OnClickListener {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        })
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                replaceFragment(HomeFragment())
            }

            R.id.nav_dashboard -> {
                replaceFragment(DashboardFragment())
            }
            R.id.nav_my_profile -> {
                replaceFragment(MyProfileFragment())
            }

            R.id.nav_grievance -> {
                replaceFragment(ShowRecordFragment.newInstance(DashboardType.GRIEVANCE.ordinal))
            }

            R.id.nav_suggestion -> {
                replaceFragment(ShowRecordFragment.newInstance(DashboardType.SUGGESTION.ordinal))

            }

            R.id.nav_query -> {
                replaceFragment(ShowRecordFragment.newInstance(DashboardType.QUERY.ordinal))
            }

            R.id.nav_update_us -> {
                replaceFragment(ShowRecordFragment.newInstance(DashboardType.UPDATE_US.ordinal))
            }

            R.id.nav_discussion -> {
                replaceFragment(ShowRecordFragment.newInstance(DashboardType.DISCUSSION.ordinal))

            }

            R.id.nav_feedback -> {
                replaceFragment(PastFeedbackFragment())

            }

            R.id.nav_ongoing_project -> {
                replaceFragment(OngoingProjectsFragment())

            }
            R.id.nav_contact -> {
                replaceFragment(ContactUsFragment())

            }
            /*   R.id.nav_emergency_contact -> {
                   replaceFragment(EmergencyContactFragment())

               }*/
            R.id.nav_privacy_policy -> {
                replaceFragment(WebviewFragment.newInstance("PRIVACY_POLICY", ""))

            }
            R.id.nav_term_condition -> {
                replaceFragment(WebviewFragment.newInstance("TERM_AND_CONDITION", ""))

            }
            /*  R.id.nav_faq -> {
                  replaceFragment(FaqFragment())

              }*/

            R.id.nav_logout -> {
                DialogUtil.showTwoButtonDialog(this@DashboardActivity, getString(R.string.msg_logout), getString(R.string.txt_yes), getString(R.string.txt_no), object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                    override fun onPositiveButtonClick() {
                        UserManager.getInstance().logout()
                        startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
                        finish()
                    }

                    override fun onNegativeButtonClick() {

                    }
                })
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun replaceFragment(fragment: BaseFragment) {
        FragmentFactory.replaceFragment(fragment, R.id.container, this)
    }

    fun replaceFragmentWithTag(fragment: BaseFragment, tag: String) {
        FragmentFactory.replaceFragment(fragment, R.id.container, this, tag)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun setToolbar(title: String, isBack: Boolean) {
        tv_title.setText(title)
        if (isBack) {
            iv_back.visibility = View.VISIBLE
            iv_menu_toggle.visibility = View.GONE
        } else {
            iv_back.visibility = View.GONE
            iv_menu_toggle.visibility = View.VISIBLE
        }

        iv_back.setOnClickListener(View.OnClickListener {
            FragmentFactory.back(this)
        })

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            /* if (FragmentFactory.isFragmentStackIsEmpty(this)) {
                 DialogUtil.showTwoButtonDialog(this@DashboaredActivity, getString(R.string.msg_app_exit), getString(R.string.txt_yes), getString(R.string.txt_no), object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                     override fun onPositiveButtonClick() {
                         FragmentFactory.back(this@DashboaredActivity)
                     }

                     override fun onNegativeButtonClick() {

                     }
                 })
             } else {*/
            FragmentFactory.back(this)
            //}
        }

    }

}
