package com.likeit.sous_vide.http.network;


import com.likeit.sous_vide.http.network.entity.BaseHttpMethods;
import com.likeit.sous_vide.http.network.entity.EmptyEntity;
import com.likeit.sous_vide.http.network.entity.HttpResult;
import com.likeit.sous_vide.http.subscriber.MySubscriber;
import com.likeit.sous_vide.model.CateListModel;
import com.likeit.sous_vide.model.HomeFoodListModel;
import com.likeit.sous_vide.model.Loginmodel;
import com.likeit.sous_vide.model.Registermodel;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HttpMethods extends BaseHttpMethods {


    private HttpMethods() {
        super();
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //
    public void LoginUser(MySubscriber<Loginmodel> subscriber, String mobile, String password) {
        Observable<HttpResult<Loginmodel>> observable = myApiService.LoginUser(mobile, password);
        toSubscribe(observable, subscriber);
    }

    public void RegisterUser(MySubscriber<Registermodel> subscriber, String mobile, String area, String password) {
        Observable<HttpResult<Registermodel>> observable = myApiService.RegisterUser(mobile, area, password);
        toSubscribe(observable, subscriber);
    }

    public void ResetPwd(MySubscriber<EmptyEntity> subscriber, String mobile, String password) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.ResetPwd(mobile, password);
        toSubscribe(observable, subscriber);
    }

    public void HomeFoodList(MySubscriber<ArrayList<HomeFoodListModel>> subscriber, String ukey) {
        Observable<HttpResult<ArrayList<HomeFoodListModel>>> observable = myApiService.HomeFoodList(ukey);
        toSubscribe(observable, subscriber);
    }

    public void food_cate_detail(MySubscriber<ArrayList<CateListModel>> subscriber, String ukey, String catid) {
        Observable<HttpResult<ArrayList<CateListModel>>> observable = myApiService.food_cate_detail(ukey, catid);
        toSubscribe(observable, subscriber);
    }
    public void Upload(MySubscriber<EmptyEntity> subscriber, String ukey, String avatar) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.Upload(ukey, avatar);
        toSubscribe(observable, subscriber);
    }
    public void Upfood(MySubscriber<EmptyEntity> subscriber, String ukey, String imgs,String name,String wendu,String time,String description) {
        Observable<HttpResult<EmptyEntity>> observable = myApiService.Upfood(ukey, imgs,name,wendu,time,description);
        toSubscribe(observable, subscriber);
    }

//
//
//    public void GetShopHomeList(MySubscriber<ArrayList<ShopHomeInfoEntity>> subscriber, String ukey, String cid) {
//        Observable<HttpResult<ArrayList<ShopHomeInfoEntity>>> observable = myApiService.GetShopHomeList(ukey, cid);
//        toSubscribe(observable, subscriber);
//    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }


}
