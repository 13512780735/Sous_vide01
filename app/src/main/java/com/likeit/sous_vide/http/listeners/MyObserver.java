package com.likeit.sous_vide.http.listeners;


public interface MyObserver<T> {

        void onHttpCompleted(T t);
        void onHttpError(Throwable e);

}
