package com.thang.wesee.FCM;

import com.thang.wesee.Model.Notification;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIServices {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA5vQpgYY:APA91bEYX9HYdp_q7oTPXXwI-2qRYHYS2lvyBe6dw95U5RwCz0hY6DySde0TAGdKN2myDB2RmF6SRoT-hc_EalAFeALm3zj9B0417g4tPrWToQB17IOowzZ-FZhzOOJZroKfgsscE9XM"
    })

    @POST("fcm/send")
    Call<String> sendRemoteMesage(
            @Body Notification remotebody
            );
}
