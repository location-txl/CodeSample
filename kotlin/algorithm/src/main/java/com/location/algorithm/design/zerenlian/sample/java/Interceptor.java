package com.location.algorithm.design.zerenlian.sample.java;

import com.location.algorithm.design.zerenlian.Request;
import com.location.algorithm.design.zerenlian.Response;

public interface Interceptor {
    Response intercept(Chain chain);

    interface Chain{
        Request getRequest();
        Response proceed(Request request);
    }
}
