package com.spa.urbanstep.network;


import com.spa.carrythistoo.model.User;
import com.spa.urbanstep.model.LoginRequest;
import com.spa.urbanstep.model.Project;
import com.spa.urbanstep.model.Zone;
import com.spa.urbanstep.model.request.RegisterRequest;
import com.spa.urbanstep.model.response.Colony;
import com.spa.urbanstep.model.response.FeedbackResponse;
import com.spa.urbanstep.model.response.HomeMapUrl;
import com.spa.urbanstep.model.response.ListItem;
import com.spa.urbanstep.model.response.LoginResponse;
import com.spa.urbanstep.model.response.RecordDetail;
import com.spa.urbanstep.model.response.SaveRecord;
import com.spa.urbanstep.model.response.SubmitResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface DayZeroRestApiStore {
    @POST("login")
    Call<ResponseBean<User>> performLogin(@Body LoginRequest loginRequest);

    @POST("register")
    Call<ResponseBean<LoginResponse>> performRegister(@Body RegisterRequest registerRequest);

    @POST("get-grievance")
    Call<ResponseBean<List<RecordDetail>>> getGrievance(@Query("user_id") int userId);

    @POST("get-suggestion")
    Call<ResponseBean<List<RecordDetail>>> getSuggestion(@Query("user_id") int userId);

    @POST("get-query")
    Call<ResponseBean<List<RecordDetail>>> getQuery(@Query("user_id") int userId);

    @POST("get-discuss")
    Call<ResponseBean<List<RecordDetail>>> getDiscuss(@Query("user_id") int userId);

    @POST("get-update")
    Call<ResponseBean<List<RecordDetail>>> getUpdate(@Query("user_id") int userId);

    @POST("get-feedback")
    Call<ResponseBean<FeedbackResponse>> getFeedback(@Query("user_id") int userId);

    @POST("save-grievance")
    @Multipart
    Call<ResponseBean<SubmitResponse>> saveGrievance(@Part("user_id") RequestBody userId, @Part("zone_id") RequestBody zoneId, @Part("ward_id") RequestBody wardId, @Part("cat_id") RequestBody catId, @Part("sub_cat_id") RequestBody subCatId, @Part("problem_id") RequestBody problemId, @Part MultipartBody.Part image, @Part MultipartBody.Part image1, @Part MultipartBody.Part image2, @Part("address") RequestBody address);

    @POST("save-suggestion")
    Call<ResponseBean<SubmitResponse>> saveSuggestion(@Body SaveRecord record);

    @POST("save-query")
    Call<ResponseBean<SubmitResponse>> saveQuery(@Body SaveRecord record);

    @POST("save-discuss")
    Call<ResponseBean<SubmitResponse>> saveDiscuss(@Body SaveRecord record);

    @POST("save-update")
    @Multipart
    Call<ResponseBean<SubmitResponse>> saveUpdate(@Part("user_id") RequestBody userId, @Part("cat_id") RequestBody catId, @Part("sub_cat_id") RequestBody subCatId, @Part("updateus_id") RequestBody updateusId,
                                                  @Part("existing_floors") RequestBody existing_floors,
                                                  @Part("new_floors_added") RequestBody new_floors_added,
                                                  @Part("from_stilts") RequestBody from_stilts,
                                                  @Part("from_basement") RequestBody from_basement,
                                                  @Part("total_no_of_floors") RequestBody total_no_of_floors,
                                                  @Part("maintenance_of_roads") RequestBody maintenance_of_roads,
                                                  @Part("planned_parking") RequestBody planned_parking,
                                                  @Part("planned_loading") RequestBody planned_loading,
                                                  @Part("name_of_industry") RequestBody name_of_industry,
                                                  @Part("address_gen") RequestBody address_gen,
                                                  @Part MultipartBody.Part image, @Part("address") RequestBody address);

    @POST("save-feedback")
    Call<ResponseBean<SubmitResponse>> saveFeedback(@Body SaveRecord record);

    @POST("head-list")
    Call<ResponseBean<List<ListItem>>> getHeadList();

    @POST("sub-head-list")
    Call<ResponseBean<List<ListItem>>> getSubHeadList(@Query("head_id") int headId);

    @POST("category-list")
    Call<ResponseBean<List<ListItem>>> getCategoryList();

    @POST("updateus-category-list")
    Call<ResponseBean<List<ListItem>>> getUpdateUsCategoryList();

    @POST("authority-list")
    Call<ResponseBean<List<ListItem>>> getAuthorityList();

    @POST("sub-category-list")
    Call<ResponseBean<List<ListItem>>> getSubCategoryList(@Query("cat_id") int categoryId);

    @POST("updateus-sub-category-list")
    Call<ResponseBean<List<ListItem>>> getUpdateUsSubCategoryList(@Query("cat_id") int categoryId);

    @POST("problem-list")
    Call<ResponseBean<List<ListItem>>> getProblemist(@Query("cat_id") int catId, @Query("sub_cat_id") int subCatId);

    @POST("updateus-master-list")
    Call<ResponseBean<List<ListItem>>> getUpdateUsMasterList(@Query("cat_id") int catId, @Query("sub_cat_id") int subCatId);

    @POST("topic-list")
    Call<ResponseBean<List<ListItem>>> getTopicList(@Query("cat_id") int catId);

    @POST("zone-list")
    Call<ResponseBean<List<ListItem>>> getZoneList();

    @POST("ward-list")
    Call<ResponseBean<List<ListItem>>> getWardList(@Query("zone_id") int zoneId);

    @POST("colony-list")
    Call<ResponseBean<List<ListItem>>> getColonyList(@Query("zone_id") int zoneId, @Query("ward_id") int wardId);


    @POST("ongoing-project")
    Call<ResponseBean<List<Project>>> ongoingProject();

    @POST("get-profile")
    Call<ResponseBean<User>> getProfile(@Query("user_id") int user_id);

    @POST("edit-profile")
    @Multipart
    Call<ResponseBean> editProfile(@Query("user_id") String userId, @Part("name") RequestBody name, @Part("email") RequestBody email, @Part("phone_number") RequestBody phoneNumber, @Part("gender") RequestBody gender, @Part("state") RequestBody state, @Part("dob") RequestBody dob, @Part MultipartBody.Part image, @Part("address") RequestBody address);

    @POST("forgot-password")
    Call<ResponseBean> forgotPassword(@Query("email") String email);

    @POST("colony-data")
    Call<ResponseBean<List<Colony>>> colonySearchList(@Query("colony") String colcny);

    @POST("know-your-area")
    Call<ResponseBean<List<Zone>>> knowYourAreaList();

    @POST("ward-list")
    Call<ResponseBean<List<Zone>>> knowYourAreaViewList(@Query("zone_id") int zoneID);

    @POST("home-map")
    Call<ResponseBean<HomeMapUrl>> homeMapUrl();

}