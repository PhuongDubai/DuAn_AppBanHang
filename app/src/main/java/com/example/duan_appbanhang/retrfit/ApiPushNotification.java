package com.example.duan_appbanhang.retrfit;


import com.example.duan_appbanhang.mode.NotiResponse;
import com.example.duan_appbanhang.mode.NotiSendData;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNotification {
    @Headers(
            {
                    "Content-Type: application/json",
                    "Authorization: key=AAAAYhNiUBc:APA91bF-wEpz1XgX_5lAYN7IjeKSmK6QRvGD3ZKLW1HC-z1yQejlkGg52tFY2ZL43eZ3OD-pKsLW_Y5FNp2Oqzsy4VSqqoCqYiDoKX3YmjgwZtTn1iv_GDMGcrmgNaRnknWSr3vTjcr8"
            }
    )
    @POST("fcm/send")
    Observable<NotiResponse> sendNotification(@Body NotiSendData data);
}
