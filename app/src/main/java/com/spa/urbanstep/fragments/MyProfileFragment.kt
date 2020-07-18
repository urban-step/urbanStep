package com.spa.urbanstep.fragments


import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.spa.carrythistoo.executer.BackgroundExecutor
import com.spa.carrythistoo.model.User
import com.spa.carrythistoo.utils.CommonMethods
import com.spa.carrythistoo.utils.DialogUtil
import com.spa.carrythistoo.utils.Utility

import com.spa.urbanstep.R
import com.spa.urbanstep.UserManager
import com.spa.urbanstep.activity.DashboardActivity
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.response.ListItem
import com.spa.urbanstep.requester.EditProfileRequester
import com.spa.urbanstep.requester.GetProfileRequester
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.android.synthetic.main.progress_bar.*
import org.greenrobot.eventbus.Subscribe
import android.app.Activity
import android.app.DatePickerDialog
import android.net.Uri
import android.widget.DatePicker
import android.widget.Toast
import com.craterzone.media.images.ImageUtil
import com.cz.imagelib.CameraUtil
import com.cz.imagelib.Crop
import com.spa.carrythistoo.permissions.PermissionRequest
import com.spa.carrythistoo.permissions.PermissionRequestHandler
import com.spa.urbanstep.permissions.PermissionsUtil
import com.spa.urbanstep.utils.FileUtil
import com.spa.urbanstep.utils.GlideUtil
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MyProfileFragment : BaseFragment(), DatePickerDialog.OnDateSetListener, PermissionRequest.RequestCustomPermissionGroup, PermissionRequest.RequestStorage {


    var genderList = ArrayList<ListItem>()
    var genderId: Int? = null

    var imageUri: Uri? = null
    var imagePath: String? = null

    var parentView: View? = null

    override fun onAllCustomPermissionGroupGranted() {
        //  CommonMethods.showShortToast(context!!, "All custom permission granted")
        Crop.captureImage(activity)
    }

    override fun onCustomPermissionGroupDenied() {
        //CommonMethods.showShortToast(context!!, "All custom permission denied")
    }

    override fun onStoragePermissionGranted() {
        // CommonMethods.showShortToast(context!!, "Storage permission granted")
        Crop.pickImage(activity)
    }

    override fun onStoragePermissionDenied() {
        //CommonMethods.showShortToast(context!!, "Storage permission denied")
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.GET_PROFILE_SUCCESS -> {
                    setDefualtUI()
                }

                EventConstant.GET_PROFILE_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }

                EventConstant.EDIT_PROFILE_SUCCESS -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }

                EventConstant.EDIT_PROFILE_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentView = view

        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().execute(GetProfileRequester())

        setupUI(view, false)

        tv_edit_profile.setOnClickListener {
            if (tv_edit_profile.getText().equals("Edit Profile")) {
                setupUI(view, true)
                tv_edit_profile.setText("Save Profile")
            } else if (tv_edit_profile.getText().equals("Save Profile")) {
                setupUI(view, false)
                if (valid()) {
                    tv_edit_profile.setText("Edit Profile")
                    val user = User()
                    user.user_id = UserManager.getInstance().userID
                    user.name = edt_name.text.toString()
                    user.email = edt_user_email.text.toString()
                    user.phone_number = edt_mobile_number.text.toString()
                    user.gender = edt_gender.text.toString()
                    user.dob = edt_date_of_birth.text.toString()
                    user.state = edt_state.text.toString()
                    user.address = edt_address.text.toString()
                    user.image = imagePath

                    Utility.showProgressBarSmall(rl_progress_bar)
                    BackgroundExecutor().execute(EditProfileRequester(user))
                }
            }
        }

        genderList.add(ListItem(1, "Male"))
        genderList.add(ListItem(2, "Female"))
        genderList.add(ListItem(3, "Other"))

        edt_gender.setOnClickListener {
            DialogUtil.showListing(context!!, genderList!!, object : DialogUtil.AlertDialogInterface.ListItemDialogClickListener {
                override fun onItemSelect(id: Int, name: String) {
                    genderId = id
                    edt_gender.setText(name)
                }
            })
        }

        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
                context, this, mYear, mMonth, mDay)

        edt_date_of_birth.setOnClickListener {
            datePickerDialog.show()
        }

        iv_profile_pic.setOnClickListener {
            selectedImage()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var m = month
        m += 1
        edt_date_of_birth.setText("" + year + "-" + dayOfMonth + "-" + m)
    }

    private fun valid(): Boolean {
        if (TextUtils.isEmpty(edt_name.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_full_name_empty))
            return false
        } else if (TextUtils.isEmpty(edt_user_email.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.validation_email_empty))
            return false
        } else if (!CommonMethods.isEmailValid(edt_user_email.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.validation_email_format))
            return false
        } else if (TextUtils.isEmpty(edt_mobile_number.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_photo_empty))
            return false
        } /*else if (!Utility.isValidMobileNumber(edt_mobile_number.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_mobile_number_valid))
            return false
        } */ else if (TextUtils.isEmpty(edt_state.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_state_empty))
            return false
        } else if (TextUtils.isEmpty(edt_date_of_birth.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_date_of_birth))
            return false
        } else if (TextUtils.isEmpty(edt_gender.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_gender_empty))
            return false
        } else if (TextUtils.isEmpty(edt_address.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_address_empty))
            return false
        } else {
            return true
        }
    }

    private fun setDefualtUI() {
        var user = UserManager.getInstance().user as User

        edt_name.setText(user.name)
        edt_user_email.setText(user!!.email)
        edt_mobile_number.setText("" + user!!.phone_number)
        edt_state.setText(user!!.state)
        edt_gender.setText(user.gender)
        edt_date_of_birth.setText(user.dob)
        edt_address.setText(user.address)
        GlideUtil.loadCircleImage(context, iv_profile_pic, user.profile_image, R.drawable.profile_pic)
    }

    fun setupUI(view: View, editable: Boolean) {
        if (editable) {
            edt_gender.isEnabled = true
            edt_date_of_birth.isEnabled = true
        } else {
            edt_gender.isEnabled = false
            edt_date_of_birth.isEnabled = false
        }

        if (view is EditText) {
            (view as EditText).setEnabled(editable)
            //Here you can add any other code that needed to be done while changing focus of a particular edit text
            return
        }
        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView, editable)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if (context is DashboardActivity) {
            (context as DashboardActivity).setToolbar(getString(R.string.title_my_profile), false)
        }
    }

    private fun selectedImage() {
        DialogUtil.openChooseMediaDialog(context!!, object : DialogUtil.AlertDialogInterface.OpenCameraDialogListener {
            override fun onCameraClick() {
                PermissionRequestHandler.requestCustomPermissionGroup(this@MyProfileFragment, "", Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            override fun onGalleryClick() {
                PermissionRequestHandler.requestStorage(this@MyProfileFragment, "")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Crop.REQUEST_PICK -> {
                if (resultCode == Activity.RESULT_OK) {
                    pickImageFromGallery(data!!.getData())
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    //Toast.makeText(context, "User cancelled image pick", Toast.LENGTH_SHORT).show();
                }
            }
            Crop.REQUEST_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    captureImageFromCamera()
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // user cancelled Image capture
                } else {
                    // failed to capture image
                }
            }
            UCrop.REQUEST_CROP -> {
                handleCrop(resultCode, data)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions as Array<String>, grantResults, this)
    }

    private fun pickImageFromGallery(uri: Uri?) {
        var imagePath = ImageUtil.getPath(context, uri!!)
        imagePath = CameraUtil.roateImageIfRequired(imagePath)
        val copyImageFile = FileUtil.copyImageFile(context, uri, imagePath)
        UCrop.of(uri, Uri.fromFile(copyImageFile))
                .start(activity!!)
    }

    private fun captureImageFromCamera() {
        val imageCaptureUri = com.cz.imagelib.CameraUtil.getLastImageUri()
        var imagePath = imageCaptureUri.path
        //String imagePath = ImageUtil.getPath(context, imageCaptureUri);
        imagePath = com.cz.imagelib.CameraUtil.roateImageIfRequired(imagePath)
        val copyImagePath = FileUtil.copyImageFile(context, imageCaptureUri, imagePath)
        UCrop.of(imageCaptureUri, Uri.fromFile(copyImagePath))
                .start(activity!!)
    }

    private fun handleCrop(resultCode: Int, result: Intent?) {
        if (resultCode == Activity.RESULT_OK && result != null) {
            imageUri = UCrop.getOutput(result)
            imagePath = imageUri!!.getPath()
            val file = File(imageUri!!.getPath())
            //file size in KB
            /* val fileSize = file.length() / 1024
             if (fileSize > 1024) {
                 CommonMethods.showShortToast(context!!, "File size must be less than 1 MB")
                 return
             }*/

            GlideUtil.loadCircleImage(context, iv_profile_pic, imageUri, R.drawable.profile_pic)
            setupUI(parentView!!, true)
            tv_edit_profile.setText("Save Profile")


        } else if (resultCode == UCrop.RESULT_ERROR && result != null) {
            Toast.makeText(context, UCrop.getError(result)!!.message, Toast.LENGTH_SHORT).show()
        } else {

        }
    }

}
