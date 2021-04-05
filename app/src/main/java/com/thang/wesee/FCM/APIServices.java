package com.thang.wesee.FCM;

import com.thang.wesee.Model.Notification;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIServices {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAIdhVqDM:APA91bERixWz5DoHiCv33RJaMWLHb9lbjib9BoWHmbRrLEcScVCXYe_IuKEudF_UCh3YTFJ1ToYoEwh1AG86YUUAHzSIJdUy7Utybq1AILLq-47-dK6ATWYFeUik2liC78pfPXpF5EM5"
    })

    @POST("fcm/send")
    Call<String> sendRemoteMesage(
            @Body Notification remotebody
            );
}
