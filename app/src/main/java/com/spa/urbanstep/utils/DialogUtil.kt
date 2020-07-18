package com.spa.carrythistoo.utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.Window

import android.widget.PopupWindow
import com.spa.urbanstep.R
import com.spa.urbanstep.model.Project
import com.spa.urbanstep.model.response.ListItem
import kotlinx.android.synthetic.main.dialog_open_gallary.*
import java.util.ArrayList


object DialogUtil {
    private val TAG = DialogUtil::class.java.name
    var mDialog: Dialog? = null
    var mWindow: PopupWindow? = null

    interface AlertDialogInterface {
        interface TwoButtonDialogClickListener {
            fun onPositiveButtonClick()

            fun onNegativeButtonClick()
        }

        interface OneButtonDialogClickListener {
            fun onButtonClick()
        }

        interface ListItemDialogClickListener {
            fun onItemSelect(id: Int, name: String)
        }

        interface ZoneListItemDialogClickListener {
            fun onItemSelect(id: Int, name: String, lat: Double, long: Double)
        }

        interface ListItemMasterDialogClickListener {
            fun onItemSelect(id: Int, name: String, is_form: Int)
        }

        interface OpenCameraDialogListener {
            fun onCameraClick()
            fun onGalleryClick()
        }

        interface OpenchooserDialogListener {
            fun onColonySearchClick()
            fun onZoneClick()
        }
    }

    fun dismiss() {
        if (mDialog != null && mDialog!!.isShowing) {
            mDialog!!.dismiss()
            mDialog = null
        }
    }


    fun showTwoButtonDialog(context: Context, message: String, positiveBtnString: String, negBtnString: String, dialogInterface: AlertDialogInterface.TwoButtonDialogClickListener) {
        if (mDialog != null && mDialog!!.isShowing) {
            //return;
            dismiss()
        }
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
                .setPositiveButton(positiveBtnString) { dialog, id ->
                    mDialog!!.dismiss()
                    dialogInterface.onPositiveButtonClick()
                }
                .setNegativeButton(negBtnString) { dialog, id ->
                    mDialog!!.dismiss()
                    dialogInterface.onNegativeButtonClick()
                }
        mDialog = builder.create()
        mDialog!!.show()
    }

    fun showListing(context: Context, stateList: ArrayList<ListItem>, listener: AlertDialogInterface.ListItemDialogClickListener) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        var selectedState = stateList.get(0).id!!.toInt();
        var selectName = stateList.get(0).name!!

// add a radio button list
        val states: Array<String?> = stateList.map { it.name }.toTypedArray();

        val checkedItem = 0
        builder.setSingleChoiceItems(states, checkedItem, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                selectedState = stateList.get(p1).id!!.toInt()
                selectName = stateList.get(p1).name!!
            }
        })

// add OK and Cancel buttons
        builder.setPositiveButton("OK") { dialog, which ->
            listener.onItemSelect(selectedState, selectName)
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
// create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    fun showZoneListing(context: Context, stateList: ArrayList<ListItem>, listener: AlertDialogInterface.ZoneListItemDialogClickListener) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        var selectedState = stateList.get(0).id!!.toInt();
        var selectName = stateList.get(0).name!!
        var lat = stateList.get(0).lat!!
        var long = stateList.get(0).long!!

// add a radio button list
        val states: Array<String?> = stateList.map { it.name }.toTypedArray();

        val checkedItem = 0
        builder.setSingleChoiceItems(states, checkedItem, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                selectedState = stateList.get(p1).id!!.toInt()
                selectName = stateList.get(p1).name!!
                lat = stateList.get(p1).lat!!
                long = stateList.get(p1).long!!
            }
        })

// add OK and Cancel buttons
        builder.setPositiveButton("OK") { dialog, which ->
            listener.onItemSelect(selectedState, selectName,lat,long)
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
// create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    fun showMasterListing(context: Context, stateList: ArrayList<ListItem>, listener: AlertDialogInterface.ListItemMasterDialogClickListener) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        var selectedState = stateList.get(0).id!!.toInt();
        var selectName = stateList.get(0).name!!
        var isForm = stateList.get(0).is_form!!

// add a radio button list
        val states: Array<String?> = stateList.map { it.name }.toTypedArray();

        val checkedItem = 0
        builder.setSingleChoiceItems(states, checkedItem, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                selectedState = stateList.get(p1).id!!.toInt()
                selectName = stateList.get(p1).name!!
                isForm = stateList.get(p1).is_form!!
            }
        })

// add OK and Cancel buttons
        builder.setPositiveButton("OK") { dialog, which ->
            listener.onItemSelect(selectedState, selectName, isForm)
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
// create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    fun showProjectList(context: Context, stateList: ArrayList<Project>, listener: AlertDialogInterface.ListItemDialogClickListener) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        var selectedState = stateList.get(0).id!!.toInt();
        var selectName = stateList.get(0).name!!

// add a radio button list
        val states: Array<String?> = stateList.map { it.name }.toTypedArray();

        val checkedItem = 0
        builder.setSingleChoiceItems(states, checkedItem, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                selectedState = stateList.get(p1).id!!.toInt()
                selectName = stateList.get(p1).name!!
            }
        })

// add OK and Cancel buttons
        builder.setPositiveButton("OK") { dialog, which ->
            listener.onItemSelect(selectedState, selectName)
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
// create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }


    fun openChooseMediaDialog(context: Context, cameraListener: AlertDialogInterface.OpenCameraDialogListener) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_open_gallary)
        val v = dialog.window!!.decorView
        v.setBackgroundResource(android.R.color.transparent)
        val camLayout = dialog.tv_camera
        val galLayout = dialog.tv_gallery

        camLayout.setOnClickListener {
            cameraListener.onCameraClick()
            dialog.dismiss()
        }

        galLayout.setOnClickListener {
            cameraListener.onGalleryClick()
            dialog.dismiss()
        }

        dialog.show()
    }

    fun openChooseKnowAreaDialog(context: Context, chooseListener: AlertDialogInterface.OpenchooserDialogListener) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.chooser_know_area)
        val v = dialog.window!!.decorView
        v.setBackgroundResource(android.R.color.transparent)
        val camLayout = dialog.tv_camera
        val galLayout = dialog.tv_gallery

        camLayout.setOnClickListener {
            chooseListener.onColonySearchClick()
            dialog.dismiss()
        }

        galLayout.setOnClickListener {
            chooseListener.onZoneClick()
            dialog.dismiss()
        }

        dialog.show()
    }
}
