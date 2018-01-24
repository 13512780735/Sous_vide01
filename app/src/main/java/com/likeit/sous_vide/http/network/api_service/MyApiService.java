package com.likeit.sous_vide.http.network.api_service;


import com.likeit.sous_vide.http.network.entity.EmptyEntity;
import com.likeit.sous_vide.http.network.entity.HttpResult;
import com.likeit.sous_vide.model.CateListModel;
import com.likeit.sous_vide.model.HomeFoodListModel;
import com.likeit.sous_vide.model.Loginmodel;
import com.likeit.sous_vide.model.Registermodel;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface MyApiService {

    String BASE_URL = "http://sousvide.wbteam.cn/";
    String BASE_URL_IMG = "http://sousvide.net./";

    String LoginUser = BASE_URL + "?s=/api/user/login";
    String RegisterUser = BASE_URL + "?s=/api/user/register";
    String GetCatelist = BASE_URL + "?s=/api/food/get_catelist";
    String food_cate_detail = BASE_URL + "?s=/api/food/food_cate_detail";
    String food_detail = BASE_URL + "?s=/api/food/food_detail";

    //用户登录接口
    @FormUrlEncoded
    @POST("?s=/api/user/login")
    Observable<HttpResult<Loginmodel>> LoginUser(@Field("mobile") String mobile, @Field("password") String password);

    //用户注册接口
    @FormUrlEncoded
    @POST("?s=/api/user/register")
    Observable<HttpResult<Registermodel>> RegisterUser(@Field("mobile") String mobile,
                                                       @Field("area") String area,
                                                       @Field("password") String password);

    //忘记密码接口
    @FormUrlEncoded
    @POST("?s=/api/user/passwordreset")
    Observable<HttpResult<EmptyEntity>> ResetPwd(@Field("mobile") String mobile,
                                                 @Field("password") String password);

    //首页菜品列表
    @FormUrlEncoded
    @POST("?s=/api/user/foodlist")
    Observable<HttpResult<ArrayList<HomeFoodListModel>>> HomeFoodList(@Field("ukey") String ukey);

    //首页菜品列表
    @FormUrlEncoded
    @POST("?s=/api/food/food_cate_detail")
    Observable<HttpResult<ArrayList<CateListModel>>> food_cate_detail(@Field("ukey") String ukey,
                                                                      @Field("catid") String catid
                                                                          );


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
