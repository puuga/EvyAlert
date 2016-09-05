package com.appspace.evyalert.manager;


import com.appspace.evyalert.model.Comment;
import com.appspace.evyalert.model.Event;
import com.appspace.evyalert.model.Province;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by siwaweswongcharoen on 6/6/2016 AD.
 */
public interface APIService {

    @GET("service_events.php")
    Call<Event[]> loadAllEvents();

    @FormUrlEncoded
    @POST("service_event_post.php")
    Call<Event> postEvent(
            @Field("user_uid") String userUid,
            @Field("user_name") String userName,
            @Field("user_photo_url") String userPhotoUrl,
            @Field("title") String title,
            @Field("event_photo_url") String eventPhotoUrl,
            @Field("event_type_index") String eventTypeIndex,
            @Field("province_index") String provinceIndex,
            @Field("region_index") String regionIndex,
            @Field("lat") String lat,
            @Field("lng") String lng,
            @Field("address") String address,
            @Field("created_at_long") String createdAtLong
    );

    @GET("service_events.php")
    Call<Event[]> loadEvents(
            @Query("filter") String filterOption, // 0, 1
            @Query("lat") String lat,
            @Query("lng") String lng,
            @Query("event_filter") String eventFilter
    );

    @GET("service_events.php")
    Call<Event[]> loadEventsLast2Days(
            @Query("filter") String filterOption, // 2
            @Query("event_filter") String eventFilter
    );

    @GET("service_events.php")
    Call<Event[]> loadEventsByProvinces(
            @Query("filter") String filterOption, // 3
            @Query("province_id") String provinceId,
            @Query("event_filter") String eventFilter
    );

    @GET("service_event_delete.php")
    Call<Response<Void>> deleteEvent(
            @Query("event_uid") String eventId
    );

    @GET("service_provinces.php")
    Call<Province> loadProvince(@Query("id") int provinceId);

    @GET("service_comments_by_event.php")
    Call<Comment[]> loadComment(@Query("event_id") String eventId);

    @FormUrlEncoded
    @POST("service_comment_post.php")
    Call<Comment> postComment(
            @Field("event_id") String eventId,
            @Field("comment") String comment,
            @Field("user_uid") String userUid,
            @Field("user_name") String userName,
            @Field("user_photo_url") String userPhotoUrl,
            @Field("created_at_long") String createdAtLong
    );

    @GET("service_comment_delete.php")
    Call<Comment[]> deleteComment(@Query("comment_id") String commentId);
}
