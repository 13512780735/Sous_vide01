package com.likeit.sous_vide.http.network;


import com.likeit.sous_vide.http.network.entity.BaseHttpMethods;

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
//    public void RegisterUser(MySubscriber<EmptyEntity> subscriber, String mobile, String password) {
//        Observable<HttpResult<EmptyEntity>> observable = myApiService.RegisterUser(mobile, password);
//        toSubscribe(observable, subscriber);
//    }
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
