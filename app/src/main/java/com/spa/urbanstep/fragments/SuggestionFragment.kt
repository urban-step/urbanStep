package com.spa.urbanstep.fragments


import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spa.carrythistoo.executer.BackgroundExecutor
import com.spa.carrythistoo.permissions.PermissionRequest
import com.spa.carrythistoo.permissions.PermissionRequestHandler
import com.spa.carrythistoo.utils.CommonMethods
import com.spa.carrythistoo.utils.DialogUtil
import com.spa.carrythistoo.utils.Utility
import com.spa.urbanstep.DashboardType
import com.spa.urbanstep.ListType

import com.spa.urbanstep.R
import com.spa.urbanstep.UserManager
import com.spa.urbanstep.activity.DashboardActivity
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.response.ListItem
import com.spa.urbanstep.model.response.SaveRecord
import com.spa.urbanstep.model.response.SubmitResponse
import com.spa.urbanstep.permissions.PermissionsUtil
import com.spa.urbanstep.requester.ListRequester
import com.spa.urbanstep.requester.SaveRecordRequester
import com.spa.urbanstep.utils.ZoneLocationList
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.fragment_suggestion.*
import kotlinx.android.synthetic.main.progress_bar.*
import org.greenrobot.eventbus.Subscribe

class SuggestionFragment : BaseFragment(), LocationEngineListener, ThanksFragment.DialogDismiss, PermissionRequest.RequestLocation {

    override fun onDismiss() {
        (activity as DashboardActivity).onBackPressed()
    }

    var zoneList: ArrayList<ListItem>? = null
    var categoryList: ArrayList<ListItem>? = null
    var subCategoryList: ArrayList<ListItem>? = null
    var wardList: ArrayList<ListItem>? = null

    var mapView: MapboxMap? = null
    var locationEngine: LocationEngine? = null
    var lat: Double? = 0.0
    var long: Double? = 0.0

    var marker: Marker? = null

    var zoneid: Int? = null
    var catId: Int? = null
    var subCatId: Int? = null
    var wardId: Int? = null


    companion object {
        var TAG: String = SuggestionFragment::class.java.simpleName
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
                }
                ListType.ZONE.ordinal -> {
                    zoneList = ZoneLocationList.getInstance().mergeList(eventObject.`object` as ArrayList<ListItem>)

                    rl_select_wards.visibility = View.VISIBLE
                }
                ListType.WARD.ordinal -> {
                    wardList = eventObject.`object` as ArrayList<ListItem>
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
                    val dialogFragment = ThanksFragment.newInstance(DashboardType.SUGGESTION.ordinal, eventObject.`object` as SubmitResponse)
                    dialogFragment.setTargetFragment(this, 100)
                    dialogFragment.show(ft, "dialog")
                    // (activity as DashboardActivity).replaceFragmentWithTag(ThanksFragment.newInstance(DashboardType.SUGGESTION.ordinal,eventObject.`object` as SubmitResponse), ThanksFragment.TAG)
                }
                EventConstant.SAVE_RECORD_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)

                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggestion, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (context is DashboardActivity) {
            (context as DashboardActivity).setToolbar(getString(R.string.title_suggestion), true)
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
        tv_suggestion.setText(" ")
        tv_address.setText("")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PermissionRequestHandler.requestLocation(this@SuggestionFragment, "")

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
                        rl_select_others.visibility = View.VISIBLE
                    } else {
                        rl_select_sub_category.visibility = View.VISIBLE
                        rl_select_others.visibility = View.GONE

                        Utility.showProgressBarSmall(rl_progress_bar)
                        BackgroundExecutor().execute(ListRequester(ListType.SUB_CATEGORY.ordinal, 0, catId!!, 0, 0, 0))
                    }
                }
            })
        }

        tv_select_sub_category.setOnClickListener {
            if (TextUtils.isEmpty(tv_select_category.text.toString())) {
                CommonMethods.showShortToast(context!!, "Please Select Category")
            } else {
                DialogUtil.showListing(context!!, subCategoryList!!, object : DialogUtil.AlertDialogInterface.ListItemDialogClickListener {
                    override fun onItemSelect(id: Int, name: String) {
                        subCatId = id
                        tv_select_sub_category.setText(name)
                    }
                })
            }
        }

        tv_submit.setOnClickListener {
            sendSuggestionToServer()
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
        val position = CameraPosition.Builder()
                .target(LatLng(location!!.getLatitude(), location!!.getLongitude())) // Sets the new camera position
                .zoom(20.0) // Sets the zoom to level 14
                .tilt(45.0) // Set the camera tilt to 45 degrees
                .build()
        mapView!!.animateCamera(CameraUpdateFactory.newCameraPosition(position))
        /* mapView!!.animateCamera(CameraUpdateFactory.newLatLngZoom(
                 LatLng(location!!.getLatitude(), location!!.getLongitude()), 21.0))*/
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions as Array<String>, grantResults, this)
    }

    private fun sendSuggestionToServer() {
        if (valid()) {
            val grievance = SaveRecord()
            grievance.user_id = UserManager.getInstance().userID
            grievance.zone_id = zoneid
            grievance.ward_id = wardId
            grievance.cat_id = catId
            if (catId == 1000) {
                grievance.others = tv_select_others.text.toString()
            } else {

                grievance.sub_cat_id = subCatId
            }
            grievance.lat = lat
            grievance.lon = long
            grievance.address = tv_address.text.toString()
            grievance.suggestion = tv_suggestion.text.toString()

            Utility.showProgressBarSmall(rl_progress_bar)
            BackgroundExecutor().execute(SaveRecordRequester(DashboardType.SUGGESTION.ordinal, grievance))
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
        } else if (rl_select_others.visibility == View.VISIBLE && TextUtils.isEmpty(tv_select_others.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_other))
            return false
        } else if (TextUtils.isEmpty(tv_suggestion.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_suggestion_empty))
            return false
        } else if (TextUtils.isEmpty(tv_address.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_address_empty))
            return false
        } else {
            return true
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
