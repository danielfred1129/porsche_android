package com.pos.porschetower.network;

import com.pos.porschetower.datamodel.UserObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface API {

    @POST("/index.php/mobile/Mobile/login")
    Call<ResponseBody> login(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/get_car_information")
    Call<ResponseBody> get_car_information(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/get_scheduled_pickups")
    Call<ResponseBody> get_scheduled_pickups(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/get_elevator_queue_size")
    Call<ResponseBody> get_elevator_queue_size(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/get_pickup")
    Call<ResponseBody> get_pickup(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/request_car_elevator")
    Call<ResponseBody> request_car_elevator(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/cancel_car_elevator")
    Call<ResponseBody> cancel_car_elevator(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/get_time_increase")
    Call<ResponseBody> get_time_increase(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/send_schedule_request")
    Call<ResponseBody> send_schedule_request(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/send_restaurant_request")
    Call<ResponseBody> send_restaurant_request(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/get_restaurant_menu")
    Call<ResponseBody> get_restaurant_menu(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/get_spa")
    Call<ResponseBody> get_spa(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/success_car_elevator")
    Call<ResponseBody> success_car_elevator(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/schedule_car_elevator")
    Call<ResponseBody> schedule_car_elevator(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/get_spa")
    Call<ResponseBody> get_spa(@Body UserObject userObject);

    @POST("/index.php/mobile/Mobile/{gym}")
    Call<ResponseBody> gym_trainers(@Path(value = "gym") String gym, @Body UserObject userObject);

    @POST("/index.php/mobile/Mobile/{path}")
    Call<ResponseBody> community_roomOrracing_simOrgolf_sim(@Path(value = "path") String path, @Body UserObject user);

    @POST("/index.php/mobile/Mobile/{path}")
    Call<ResponseBody> dynamicPathApi(@Path(value = "path") String path, @Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/get_restaurants_in_house")
    Call<ResponseBody> get_restaurants_in_house(@Body UserObject userObject);

    @POST("/index.php/mobile/Mobile/get_document")
    Call<ResponseBody> get_document(@Body UserObject userObject);

    @POST("/index.php/mobile/Mobile/get_unit_manual")
    Call<ResponseBody> get_unit_manual(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/get_dry_cleaning")
    Call<ResponseBody> get_dry_cleaning(@Body UserObject userObject);

    @POST("/index.php/mobile/Mobile/cancel_scheduled_car_elevator")
    Call<ResponseBody> cancel_scheduled_car_elevator(@Body Map<String, String> user);

    @POST("/index.php/mobile/Mobile/send_schedule_request_for_pool_beach")
    Call<ResponseBody> send_schedule_request_for_pool_beach(@Body Map<String, String> user);

    @POST("/api/{weatherAPIKey}/conditions/q/FL/Miami.json")
    Call<ResponseBody> getWeatherCondition(@Path(value = "weatherAPIKey") String weatherAPIKey);

}
