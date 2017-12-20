package com.likeit.sous_vide.http.network.api_service;


import com.likeit.sous_vide.http.network.entity.EmptyEntity;
import com.likeit.sous_vide.http.network.entity.HttpResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface MyApiService {

    String BASE_URL = "http://semyooapp.wbteam.cn/";

    //用户注册接口
    @FormUrlEncoded
    @POST("?s=/api/member/register")
    Observable<HttpResult<EmptyEntity>> RegisterUser(@Field("mobile") String mobile, @Field("password") String password);


//    /**
//     *商品列表接口
//     */
//    @FormUrlEncoded
//    @POST("?s=/api/shop/getlist")
//    Observable<HttpResult<ArrayList<CategoryInfoEntity>>> GetShopList(@Field("ukey") String ukey,
//                                                                      @Field("cid") String cid
//
//    );

}
