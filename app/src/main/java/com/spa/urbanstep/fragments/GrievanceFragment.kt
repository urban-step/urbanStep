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
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_grievance.*
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
class GrievanceFragment : BaseFragment(), LocationEngineListener, ThanksFragment.DialogDismiss, PermissionRequest.RequestCustomPermissionGroup, PermissionRequest.RequestStorage, PermissionRequest.RequestLocation {


    override fun onDismiss() {
        (activity as DashboardActivity).onBackPressed()
    }

    var zoneList: ArrayList<ListItem>? = null
    var categoryList: ArrayList<ListItem>? = null
    var subCategoryList: ArrayList<ListItem>? = null
    var wardList: ArrayList<ListItem>? = null
    var problemList: ArrayList<ListItem>? = null

    //var mLocationOverlay: UserLocationOverlay? = null
    var mapView: MapboxMap? = null
    var locationEngine: LocationEngine? = null
    var lat: Double? = 0.0
    var long: Double? = 0.0

    var marker: Marker? = null


    var zoneid: Int? = null
    var catId: Int? = null
    var subCatId: Int? = null
    var wardId: Int? = null
    var problemId: Int? = null
    var image1: String? = null
    var image2: String? = null
    var image3: String? = null

    var imageNumber: Int? = null

    var imageUri: Uri? = null
    var imagePath: String? = null


    companion object {
        var TAG: String = GrievanceFragment::class.java.simpleName
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
                ListType.CATEGORY.ordinal -> {
                    categoryList = eventObject.`object` as ArrayList<ListItem>

                    rl_select_sub_category.visibility = View.VISIBLE
                }
                ListType.SUB_CATEGORY.ordinal -> {
                    subCategoryList = eventObject.`object` as ArrayList<ListItem>

                    rl_select_problem.visibility = View.VISIBLE
                }
                ListType.ZONE.ordinal -> {

                    zoneList = ZoneLocationList.getInstance().mergeList(eventObject.`object` as ArrayList<ListItem>)

                    rl_select_wards.visibility = View.VISIBLE
                }
                ListType.WARD.ordinal -> {
                    wardList = eventObject.`object` as ArrayList<ListItem>
                }
                ListType.PROBLEM.ordinal -> {
                    problemList = eventObject.`object` as ArrayList<ListItem>
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
                    val dialogFragment = ThanksFragment.newInstance(DashboardType.GRIEVANCE.ordinal, eventObject.`object` as SubmitResponse)
                    dialogFragment.setTargetFragment(this, 100)
                    dialogFragment.show(ft, "dialog")
                    //(activity as DashboardActivity).replaceFragmentWithTag(ThanksFragment.newInstance(DashboardType.GRIEVANCE.ordinal,eventObject.`object` as SubmitResponse), ThanksFragment.TAG)
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
            (context as DashboardActivity).setToolbar(getString(R.string.title_grievance), true)
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
        tv_select_problem.text = ""
        tv_select_photo_1.text = ""
        tv_select_photo_2.text = ""
        tv_select_photo3.text = ""
        tv_address.setText("")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grievance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PermissionRequestHandler.requestLocation(this@GrievanceFragment, "")

        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().execute(ListRequester(ListType.CATEGORY.ordinal, 0, 0, 0, 0, 0))

        BackgroundExecutor().execute(ListRequester(ListType.ZONE.ordinal, 0, 0, 0, 0, 0))


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
                    if (name.equals("others", true)) {
                        rl_select_sub_category.visibility = View.GONE
                        rl_select_problem.visibility = View.GONE
                        rl_select_others.visibility = View.VISIBLE
                    } else {
                        rl_select_sub_category.visibility = View.VISIBLE
                        rl_select_problem.visibility = View.VISIBLE
                        rl_select_others.visibility = View.GONE

                        Utility.showProgressBarSmall(rl_progress_bar)
                        BackgroundExecutor().execute(ListRequester(ListType.SUB_CATEGORY.ordinal, 0, catId!!, 0, 0, 0))
                    }
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
                        BackgroundExecutor().execute(ListRequester(ListType.PROBLEM.ordinal, 0, catId!!, 0, 0, subCatId!!))
                    }
                })
            }
        }

        tv_select_problem.setOnClickListener {
            if (TextUtils.isEmpty(tv_select_sub_category.text.toString())) {
                CommonMethods.showShortToast(context!!, "Please select SubCategory")
            } else {
                DialogUtil.showListing(context!!, problemList!!, object : DialogUtil.AlertDialogInterface.ListItemDialogClickListener {
                    override fun onItemSelect(id: Int, name: String) {
                        problemId = id
                        tv_select_problem.setText(name)
                    }
                })
            }
        }

        tv_select_photo_1.setOnClickListener {
            imageNumber = 1
            selectedImage()
        }

        tv_select_photo_2.setOnClickListener {
            imageNumber = 2
            selectedImage()
        }

        tv_select_photo3.setOnClickListener {
            imageNumber = 3
            selectedImage()
        }

        tv_submit.setOnClickListener {
            sendGrievanceToServer()
            //(activity as DashboardActivity).replaceFragmentWithTag(ThanksFragment.newInstance(DashboardType.GRIEVANCE.ordinal), ThanksFragment.TAG)
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

    @SuppressLint("MissingPermission")
    private fun showCurrentLoc() {
        val options = LocationComponentOptions.builder(context)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(context!!, R.color.colorAccent))
                .build()
        var locationComponent = mapView!!.getLocationComponent();

        locationComponent.activateLocationComponent(context!!, options);

        locationComponent.setLocationComponentEnabled(true);

        locationEngine = locationComponent.getLocationEngine();

        locationEngine!!.addLocationEngineListener(this);

        locationComponent.setCameraMode(CameraMode.TRACKING);
        locationComponent.setRenderMode(RenderMode.COMPASS);

    }

    private fun sendGrievanceToServer() {
        if (valid()) {
            val grievance = SaveRecord()
            grievance.zone_id = zoneid
            grievance.ward_id = wardId
            grievance.cat_id = catId
            if (catId == 1000) {
                grievance.others = tv_select_others.text.toString()
            } else {
                grievance.sub_cat_id = subCatId
                grievance.problem_id = problemId
            }
            grievance.lat = lat
            grievance.lon = long
            grievance.myfile = image1
            grievance.myfile1 = image2
            grievance.myfile2 = image3
            grievance.address = tv_address.text.toString()

            Utility.showProgressBarSmall(rl_progress_bar)
            BackgroundExecutor().execute(SaveRecordRequester(DashboardType.GRIEVANCE.ordinal, grievance))
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
        } else if (rl_select_sub_category.visibility == View.VISIBLE && TextUtils.isEmpty(tv_select_sub_category.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_subcategory))
            return false
        } else if (rl_select_problem.visibility == View.VISIBLE && TextUtils.isEmpty(tv_select_problem.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_problem_empty))
            return false
        } else if (rl_select_others.visibility == View.VISIBLE && TextUtils.isEmpty(tv_select_others.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_other))
            return false
        } else if (TextUtils.isEmpty(tv_select_photo_1.text.toString().trim())) {
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
                PermissionRequestHandler.requestCustomPermissionGroup(this@GrievanceFragment, "", Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            override fun onGalleryClick() {
                PermissionRequestHandler.requestStorage(this@GrievanceFragment, "")
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
            if (imageNumber == 1) {
                tv_select_photo_1.setText(file.name)
                image1 = imageUri!!.path
            } else if (imageNumber == 2) {
                tv_select_photo_2.setText(file.name)
                image2 = imageUri!!.path
            } else if (imageNumber == 3) {
                tv_select_photo3.setText(file.name)
                image3 = imageUri!!.path
            }


        } else if (resultCode == UCrop.RESULT_ERROR && result != null) {
            Toast.makeText(context, UCrop.getError(result)!!.message, Toast.LENGTH_SHORT).show()
        } else {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        resetView()

        if (locationEngine != null) {
            locationEngine!!.deactivate();
        }
    }
}
