package com.appspace.evyalert.manager;


import com.appspace.evyalert.model.Event;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
}
