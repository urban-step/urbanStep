package com.spa.urbanstep.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.craterzone.media.images.ImageUtil
import com.cz.imagelib.CameraUtil
import com.cz.imagelib.Crop
import com.spa.carrythistoo.executer.BackgroundExecutor
import com.spa.carrythistoo.permissions.PermissionRequest
import com.spa.carrythistoo.permissions.PermissionRequestHandler
import com.spa.carrythistoo.utils.CommonMethods
import com.spa.carrythistoo.utils.DialogUtil
import com.spa.carrythistoo.utils.Utility
import com.spa.urbanstep.DashboardType
import com.spa.urbanstep.ListType

import com.spa.urbanstep.R
import com.spa.urbanstep.activity.DashboardActivity
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.response.ListItem
import com.spa.urbanstep.model.response.SaveRecord
import com.spa.urbanstep.model.response.SubmitResponse
import com.spa.urbanstep.permissions.PermissionsUtil
import com.spa.urbanstep.requester.ListRequester
import com.spa.urbanstep.requester.SaveRecordRequester
import com.spa.urbanstep.utils.FileUtil
import com.spa.urbanstep.utils.ZoneLocationList
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.form_view.*
import kotlinx.android.synthetic.main.fragment_update_us.*
import kotlinx.android.synthetic.main.progress_bar.*
import org.greenrobot.eventbus.Subscribe
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class UpdateUsFragment : BaseFragment(), LocationEngineListener, ThanksFragment.DialogDismiss, PermissionRequest.RequestCustomPermissionGroup, PermissionRequest.RequestStorage, PermissionRequest.RequestLocation {
    override fun onDismiss() {
        (activity as DashboardActivity).onBackPressed()
    }

    var fragmentView: View? = null

    var zoneList: ArrayList<ListItem>? = null
    var wardList: ArrayList<ListItem>? = null
    var categoryList: ArrayList<ListItem>? = null
    var subCategoryList: ArrayList<ListItem>? = null
    var masterList: ArrayList<ListItem>? = null

    var mapView: MapboxMap? = null
    var locationEngine: LocationEngine? = null
    var lat: Double? = 0.0
    var long: Double? = 0.0

    var marker: Marker? = null

    var zoneid: Int? = null
    var wardId: Int? = null
    var catId: Int? = null
    var subCatId: Int? = null
    var updateusId: Int? = null
    var formId: Int? = null

    var image1: String? = null
    var imageUri: Uri? = null
    var imagePath: String? = null

    companion object {
        var TAG: String = UpdateUsFragment::class.java.simpleName
    }

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

    override fun onLocationPermissionGranted() {
        map.getMapAsync(object : OnMapReadyCallback {
            override fun onMapError(p0: Int, p1: String?) {

            }

            override fun onMapReady(p0: MapboxMap?) {
                mapView = p0
                //showCurrentLoc()
            }
        })
    }

    override fun onLocationPermissionDenied() {
    }


    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                ListType.ZONE.ordinal -> {

                    zoneList = ZoneLocationList.getInstance().mergeList(eventObject.`object` as ArrayList<ListItem>)

                    rl_select_wards.visibility = View.VISIBLE
                }
                ListType.WARD.ordinal -> {
                    wardList = eventObject.`object` as ArrayList<ListItem>
                }

                ListType.UPDATE_US_CATEGORY.ordinal -> {
                    categoryList = eventObject.`object` as ArrayList<ListItem>

                    rl_select_sub_category.visibility = View.VISIBLE
                }
                ListType.UPDATE_US_SUB_CATEGORY.ordinal -> {
                    subCategoryList = eventObject.`object` as ArrayList<ListItem>

                    rl_select_update_us.visibility = View.VISIBLE
                }
                ListType.UPDATE_US_MASTER_LIST.ordinal -> {
                    masterList = eventObject.`object` as ArrayList<ListItem>
                }
                EventConstant.ALL_LIST_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
                EventConstant.SAVE_RECORD_SUCCESS -> {
                    val ft = fragmentManager!!.beginTransaction()
                    val prev = fragmentManager!!.findFragmentByTag("dialog")
                    if (prev != null) {
                        ft.remove(prev)
                    }
                    ft.addToBackStack(null)
                    val dialogFragment = ThanksFragment.newInstance(DashboardType.UPDATE_US.ordinal, eventObject.`object` as SubmitResponse)
                    dialogFragment.setTargetFragment(this, 100)
                    dialogFragment.show(ft, "dialog")
                    //(activity as DashboardActivity).replaceFragmentWithTag(ThanksFragment.newInstance(DashboardType.UPDATE_US.ordinal,eventObject.`object` as SubmitResponse), ThanksFragment.TAG)
                }
                EventConstant.SAVE_RECORD_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (context is DashboardActivity) {
            (context as DashboardActivity).setToolbar(getString(R.string.title_update_us), true)
        }

        if (locationEngine != null) {
            locationEngine!!.removeLocationEngineListener(this);
            locationEngine!!.addLocationEngineListener(this);
        }
    }

    override fun onPause() {
        super.onPause()
        if (locationEngine != null)
            locationEngine!!.removeLocationEngineListener(this);
    }

    override fun onStop() {
        super.onStop()
        if (locationEngine != null) {
            locationEngine!!.removeLocationEngineListener(this);
            locationEngine!!.removeLocationUpdates();
        }
    }

    private fun resetView() {
        tv_select_zone.text = ""
        tv_select_ward.text = ""
        tv_select_category.text = ""
        tv_select_sub_category.text = ""
        tv_address.setText(" ")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentView = view

        PermissionRequestHandler.requestLocation(this@UpdateUsFragment, "")

        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().execute(ListRequester(ListType.ZONE.ordinal, 0, 0, 0, 0, 0))

        BackgroundExecutor().execute(ListRequester(ListType.UPDATE_US_CATEGORY.ordinal, 0, 0, 0, 0, 0))

        tv_select_zone.setOnClickListener {
            DialogUtil.showZoneListing(context!!, zoneList!!, object : DialogUtil.AlertDialogInterface.ZoneListItemDialogClickListener {
                override fun onItemSelect(id: Int, name: String, lat: Double, long: Double) {
                    zoneid = id
                    tv_select_zone.setText(name)
                    updateZoneLatLong(lat, long)
                    Utility.showProgressBarSmall(rl_progress_bar)
                    BackgroundExecutor().execute(ListRequester(ListType.WARD.ordinal, 0, 0, zoneid!!, 0, 0))
                }
            })
        }

        tv_select_ward.setOnClickListener {
            if (TextUtils.isEmpty(tv_select_zone.text.toString())) {
                CommonMethods.showShortToast(context!!, "Please select Zone")
            } else {
                DialogUtil.showListing(context!!, wardList!!, object : DialogUtil.AlertDialogInterface.ListItemDialogClickListener {
                    override fun onItemSelect(id: Int, name: String) {
                        wardId = id
                        tv_select_ward.setText(name)
                    }
                })
            }
        }

        tv_select_category.setOnClickListener {
            DialogUtil.showListing(context!!, categoryList!!, object : DialogUtil.AlertDialogInterface.ListItemDialogClickListener {
                override fun onItemSelect(id: Int, name: String) {
                    catId = id
                    tv_select_category.setText(name)
                    Utility.showProgressBarSmall(rl_progress_bar)
                    BackgroundExecutor().execute(ListRequester(ListType.UPDATE_US_SUB_CATEGORY.ordinal, 0, catId!!, 0, 0, 0))
                }
            })
        }

        tv_select_sub_category.setOnClickListener {
            if (TextUtils.isEmpty(tv_select_category.text.toString())) {
                CommonMethods.showShortToast(context!!, "Please select Category")
            } else {
                DialogUtil.showListing(context!!, subCategoryList!!, object : DialogUtil.AlertDialogInterface.ListItemDialogClickListener {
                    override fun onItemSelect(id: Int, name: String) {
                        subCatId = id
                        tv_select_sub_category.setText(name)
                        Utility.showProgressBarSmall(rl_progress_bar)
                        BackgroundExecutor().execute(ListRequester(ListType.UPDATE_US_MASTER_LIST.ordinal, 0, catId!!, 0, 0, subCatId!!))
                    }
                })
            }
        }

        tv_select_update_us.setOnClickListener {
            if (TextUtils.isEmpty(tv_select_sub_category.text.toString())) {
                CommonMethods.showShortToast(context!!, "Please select Sub Category")
            } else {
                DialogUtil.showMasterListing(context!!, masterList!!, object : DialogUtil.AlertDialogInterface.ListItemMasterDialogClickListener {
                    override fun onItemSelect(id: Int, name: String, is_form: Int) {
                        updateusId = id
                        tv_select_update_us.setText(name)
                        formId = is_form
                        showhideFormView(is_form)
                    }
                })
            }
        }

        tv_select_photo.setOnClickListener {
            selectedImage()
        }

        tv_submit.setOnClickListener {
            sendSuggestionToServer()
        }
    }

    private fun showhideFormView(is_form: Int) {
        when (is_form) {
            1 -> {
                ll_addition_of_floors.visibility = View.VISIBLE
                ll_new_development.visibility = View.GONE
                rg_yes_no.visibility = View.GONE
                ll_generation_waste.visibility = View.GONE
            }
            2 -> {
                ll_addition_of_floors.visibility = View.GONE
                ll_new_development.visibility = View.VISIBLE
                rg_yes_no.visibility = View.GONE
                ll_generation_waste.visibility = View.GONE
            }
            3, 4, 5 -> {
                ll_addition_of_floors.visibility = View.GONE
                ll_new_development.visibility = View.GONE
                rg_yes_no.visibility = View.VISIBLE
                ll_generation_waste.visibility = View.GONE
            }
            6 -> {
                ll_addition_of_floors.visibility = View.GONE
                ll_new_development.visibility = View.GONE
                rg_yes_no.visibility = View.GONE
                ll_generation_waste.visibility = View.VISIBLE
            }
            else -> {
                ll_addition_of_floors.visibility = View.GONE
                ll_new_development.visibility = View.GONE
                rg_yes_no.visibility = View.GONE
                ll_generation_waste.visibility = View.GONE
            }
        }
    }

    private fun updateZoneLatLong(lat: Double, long: Double) {
        if (mapView != null) {
            if (marker != null) {
                marker!!.remove()
            }
            this.lat = lat
            this.long = long
            mapView!!.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    LatLng(lat, long), 10.0))
            var markerOptions = MarkerOptions().position(LatLng(lat, long))
            marker = mapView!!.addMarker(markerOptions)
        }
    }

    override fun onLocationChanged(location: Location?) {
        lat = location!!.getLatitude()
        long = location!!.getLongitude()
        mapView!!.animateCamera(CameraUpdateFactory.newLatLngZoom(
                LatLng(location!!.getLatitude(), location!!.getLongitude()), 21.0))
        locationEngine!!.removeLocationEngineListener(this)
    }

    @SuppressLint("MissingPermission")
    override fun onConnected() {
        locationEngine!!.requestLocationUpdates();
    }

    private fun sendSuggestionToServer() {
        if (valid()) {
            val grievance = SaveRecord()
            grievance.zone_id = zoneid
            grievance.ward_id = wardId
            grievance.cat_id = catId
            grievance.sub_cat_id = subCatId
            grievance.myfile = image1
            grievance.address = tv_address.text.toString()
            grievance.updateus_id = updateusId
            if (formId == 1) {
                grievance.existing_floors = edt_existing_floors.text.toString();
                grievance.new_floors_added = edt_new_floors_added.text.toString();
            } else if (formId == 2) {
                grievance.from_stilts = edt_from_stilts.text.toString();
                grievance.from_basement = edt_from_basement.text.toString();
                grievance.total_no_of_floors = edt_total_no_of_floor.text.toString();
            } else if (formId == 3) {
                grievance.maintenance_of_roads = (fragmentView!!.findViewById<RadioButton>(rg_yes_no.checkedRadioButtonId) as RadioButton).text.toString()
            } else if (formId == 4) {
                grievance.planned_parking = (fragmentView!!.findViewById<RadioButton>(rg_yes_no.checkedRadioButtonId) as RadioButton).text.toString()
            } else if (formId == 5) {
                grievance.planned_loading = (fragmentView!!.findViewById<RadioButton>(rg_yes_no.checkedRadioButtonId) as RadioButton).text.toString()
            } else if (formId == 6) {
                grievance.name_of_industry = edt_name_of_the_industry.text.toString();
                grievance.address_gen = edt_address.text.toString();
            }

            Utility.showProgressBarSmall(rl_progress_bar)
            BackgroundExecutor().execute(SaveRecordRequester(DashboardType.UPDATE_US.ordinal, grievance))
        }
    }

    private fun valid(): Boolean {
        if (TextUtils.isEmpty(tv_select_zone.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_zone_empty))
            return false
        } else if (TextUtils.isEmpty(tv_select_ward.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_ward_empty))
            return false
        } else if (TextUtils.isEmpty(tv_select_category.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_category_empty))
            return false
        } else if (TextUtils.isEmpty(tv_select_sub_category.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_subcategory))
            return false
        } else if (TextUtils.isEmpty(tv_select_update_us.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, "Please select UpdateUs ")
            return false
        } else if (TextUtils.isEmpty(tv_select_photo.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_photo_empty))
            return false
        } else if (TextUtils.isEmpty(tv_address.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_address_empty))
            return false
        } else {
            return true
        }
    }

    private fun selectedImage() {
        DialogUtil.openChooseMediaDialog(context!!, object : DialogUtil.AlertDialogInterface.OpenCameraDialogListener {
            override fun onCameraClick() {
                PermissionRequestHandler.requestCustomPermissionGroup(this@UpdateUsFragment, "", Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            override fun onGalleryClick() {
                PermissionRequestHandler.requestStorage(this@UpdateUsFragment, "")
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
          /*  val fileSize = file.length() / 1024
            if (fileSize > 1024) {
                CommonMethods.showShortToast(context!!, "File size must be less than 1 MB")
                return
            }*/
            image1 = imageUri!!.path
            tv_select_photo.setText(file.name)

        } else if (resultCode == UCrop.RESULT_ERROR && result != null) {
            Toast.makeText(context, UCrop.getError(result)!!.message, Toast.LENGTH_SHORT).show()
        } else {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        resetView()
    }
}
