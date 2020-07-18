package com.spa.urbanstep.network;


import com.spa.carrythistoo.model.User;
import com.spa.urbanstep.DashboardType;
import com.spa.urbanstep.ListType;
import com.spa.urbanstep.UserManager;
import com.spa.urbanstep.model.LoginRequest;
import com.spa.urbanstep.model.request.RegisterRequest;
import com.spa.urbanstep.model.response.SaveRecord;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by kiwitech on 09/8/17.
 */

public class ApiHelper {

    private static final String TAG = ApiHelper.class.getSimpleName();

    /**
     * perform user loginRequest.
     *
     * @param loginRequest
     * @return
     */

    public static CZResponse performLogin(LoginRequest loginRequest) {

        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().performLogin(loginRequest));
    }

    public static CZResponse performRegister(RegisterRequest registerRequest) {

        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().performRegister(registerRequest));
    }

    public static CZResponse performRecordList(int recordType) {
        int userId = UserManager.getInstance().getUserID();
        if (recordType == DashboardType.GRIEVANCE.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getGrievance(userId));
        } else if (recordType == DashboardType.SUGGESTION.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getSuggestion(userId));
        } else if (recordType == DashboardType.QUERY.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getQuery(userId));
        } else if (recordType == DashboardType.UPDATE_US.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getUpdate(userId));
        } else if (recordType == DashboardType.FEEDBACK.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getFeedback(userId));
        } else if (recordType == DashboardType.DISCUSSION.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getDiscuss(userId));
        } else {
            return null;
        }
    }

    public static CZResponse performSaveRecord(int recordType, SaveRecord record) {
        if (recordType == DashboardType.GRIEVANCE.ordinal()) {
            return saveGrievance(record);
        } else if (recordType == DashboardType.SUGGESTION.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().saveSuggestion(record));
        } else if (recordType == DashboardType.QUERY.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().saveQuery(record));
        } else if (recordType == DashboardType.UPDATE_US.ordinal()) {
            return saveUpdateUs(record);
        } else if (recordType == DashboardType.FEEDBACK.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().saveFeedback(record));
        } else if (recordType == DashboardType.DISCUSSION.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().saveDiscuss(record));
        } else {
            return null;
        }
    }

    private static CZResponse saveUpdateUs(SaveRecord record) {
        MultipartBody.Part image = null;
        RequestBody address = null;
        RequestBody existingFloor = null;
        RequestBody floorAdded = null;
        RequestBody fromStilts = null;
        RequestBody froMBasement = null;
        RequestBody totalFloor = null;
        RequestBody maintanceRoad = null;
        RequestBody plannedParking = null;
        RequestBody nameIndustry = null;
        RequestBody genaddress = null;
        RequestBody plannedLoading = null;

        if (record.getMyfile() != null) {
            File file = new File(record.getMyfile());
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
            image = MultipartBody.Part.createFormData("myfile", file.getName(), requestFile);
        }

        RequestBody catId =
                RequestBody.create(MediaType.parse("multipart/form-data"), record.getCat_id() + "");

        RequestBody subCatId =
                RequestBody.create(MediaType.parse("multipart/form-data"), record.getSub_cat_id() + "");

        RequestBody updateUsId =
                RequestBody.create(MediaType.parse("multipart/form-data"), record.getUpdateus_id() + "");

        if (record.getExisting_floors() != null) {
            existingFloor =
                    RequestBody.create(MediaType.parse("multipart/form-data"), record.getExisting_floors());
        }

        if (record.getNew_floors_added() != null) {
            floorAdded =
                    RequestBody.create(MediaType.parse("multipart/form-data"), record.getNew_floors_added());
        }

        if (record.getFrom_stilts() != null) {
            fromStilts =
                    RequestBody.create(MediaType.parse("multipart/form-data"), record.getFrom_stilts());
        }

        if (record.getFrom_basement() != null) {
            froMBasement =
                    RequestBody.create(MediaType.parse("multipart/form-data"), record.getFrom_basement());
        }

        if (record.getTotal_no_of_floors() != null) {
            totalFloor =
                    RequestBody.create(MediaType.parse("multipart/form-data"), record.getTotal_no_of_floors());
        }

        if (record.getMaintenance_of_roads() != null) {
            maintanceRoad =
                    RequestBody.create(MediaType.parse("multipart/form-data"), record.getMaintenance_of_roads());
        }

        if (record.getPlanned_parking() != null) {
            plannedParking =
                    RequestBody.create(MediaType.parse("multipart/form-data"), record.getPlanned_parking());
        }

        if (record.getPlanned_loading() != null) {
            plannedLoading =
                    RequestBody.create(MediaType.parse("multipart/form-data"), record.getPlanned_loading());
        }

        if (record.getName_of_industry() != null) {
            nameIndustry =
                    RequestBody.create(MediaType.parse("multipart/form-data"), record.getName_of_industry());
        }

        if (record.getAddress_gen() != null) {
            genaddress =
                    RequestBody.create(MediaType.parse("multipart/form-data"), record.getAddress_gen());
        }

        if (record.getAddress() != null) {
            address =
                    RequestBody.create(MediaType.parse("multipart/form-data"), record.getAddress());
        }


        RequestBody userId =
                RequestBody.create(MediaType.parse("multipart/form-data"), UserManager.getInstance().getUserID() + "");


        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().saveUpdate(userId, catId, subCatId, updateUsId, existingFloor, floorAdded, fromStilts, froMBasement, totalFloor, maintanceRoad, plannedParking, plannedLoading, nameIndustry, genaddress, image, address));


    }

    private static CZResponse saveGrievance(SaveRecord record) {
        MultipartBody.Part image = null;
        MultipartBody.Part image1 = null;
        MultipartBody.Part image2 = null;

        if (record.getMyfile() != null) {
            File file = new File(record.getMyfile());
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
            image = MultipartBody.Part.createFormData("myfile", file.getName(), requestFile);
        }

        if (record.getMyfile1() != null) {
            File file = new File(record.getMyfile1());
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
            image1 = MultipartBody.Part.createFormData("myfile1", file.getName(), requestFile);
        }

        if (record.getMyfile2() != null) {
            File file = new File(record.getMyfile2());
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
            image2 = MultipartBody.Part.createFormData("myfile2", file.getName(), requestFile);
        }

// add another part within the multipart request
        RequestBody catId =
                RequestBody.create(MediaType.parse("multipart/form-data"), record.getCat_id() + "");

        RequestBody subCatId =
                RequestBody.create(MediaType.parse("multipart/form-data"), record.getSub_cat_id() + "");

        RequestBody zoneId =
                RequestBody.create(MediaType.parse("multipart/form-data"), record.getZone_id() + "");

        RequestBody wardId =
                RequestBody.create(MediaType.parse("multipart/form-data"), record.getWard_id() + "");

        RequestBody problemId =
                RequestBody.create(MediaType.parse("multipart/form-data"), record.getProblem_id() + "");

        RequestBody address =
                RequestBody.create(MediaType.parse("multipart/form-data"), record.getAddress());


        RequestBody userId =
                RequestBody.create(MediaType.parse("multipart/form-data"), UserManager.getInstance().getUserID() + "");

        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().saveGrievance(userId, zoneId, wardId, catId, subCatId, problemId, image, image1, image2, address));
    }

    public static CZResponse performList(int listType, int headId, int catId, int zoneId, int wardId, int subCatId) {
        if (listType == ListType.HEAD_LIST.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getHeadList());
        } else if (listType == ListType.SUB_HEAD_LIST.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getSubHeadList(headId));
        } else if (listType == ListType.CATEGORY.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getCategoryList());
        } else if (listType == ListType.SUB_CATEGORY.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getSubCategoryList(catId));
        } else if (listType == ListType.ZONE.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getZoneList());
        } else if (listType == ListType.WARD.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getWardList(zoneId));
        } else if (listType == ListType.TOPIC.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getTopicList(catId));
        } else if (listType == ListType.COLONY.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getColonyList(zoneId, wardId));
        } else if (listType == ListType.PROBLEM.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getProblemist(catId, subCatId));
        } else if (listType == ListType.AUTHORITY.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getAuthorityList());
        } else if (listType == ListType.UPDATE_US_CATEGORY.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getUpdateUsCategoryList());
        } else if (listType == ListType.UPDATE_US_SUB_CATEGORY.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getUpdateUsSubCategoryList(catId));
        } else if (listType == ListType.UPDATE_US_MASTER_LIST.ordinal()) {
            return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getUpdateUsMasterList(catId, subCatId));
        } else {
            return null;
        }
    }

    public static CZResponse performOngoingProjectList() {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().ongoingProject());
    }

    public static CZResponse performForgotPassword(String email) {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().forgotPassword(email));
    }

    public static CZResponse performGetProfile() {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getProfile(UserManager.getInstance().getUserID()));
    }

    public static CZResponse performEditProfile(User user) {

        MultipartBody.Part image = null;
        if (user.getImage() != null) {
            File file = new File(user.getImage());
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
            image = MultipartBody.Part.createFormData("profile_image", file.getName(), requestFile);
        }

        RequestBody name =
                RequestBody.create(MediaType.parse("multipart/form-data"), user.getName() + "");

        RequestBody email =
                RequestBody.create(MediaType.parse("multipart/form-data"), user.getEmail() + "");

        RequestBody phoneNumber =
                RequestBody.create(MediaType.parse("multipart/form-data"), user.getPhone_number());

        RequestBody gender =
                RequestBody.create(MediaType.parse("multipart/form-data"), user.getGender());

        RequestBody dob =
                RequestBody.create(MediaType.parse("multipart/form-data"), user.getDob());

        RequestBody state =
                RequestBody.create(MediaType.parse("multipart/form-data"), user.getState());

        RequestBody address =
                RequestBody.create(MediaType.parse("multipart/form-data"), user.getAddress());


        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().editProfile(user.getUser_id() + "", name, email, phoneNumber, gender, state, dob, image, address));
    }

    public static CZResponse performColonyList(@NotNull String colcny) {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().colonySearchList(colcny));
    }

    public static CZResponse performKnowYourAreaList() {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().knowYourAreaList());
    }

    public static CZResponse performKnowYourAreaViewList(int zone_id) {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().knowYourAreaViewList(zone_id));
    }

    public static CZResponse performHomeMapApi() {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().homeMapUrl());
    }

    public static CZResponse performGetFeedback() {
        return ConnectionUtil.execute(RestClient.getInstance().getRestAPIStore().getFeedback(UserManager.getInstance().getUserID()));
    }
}
