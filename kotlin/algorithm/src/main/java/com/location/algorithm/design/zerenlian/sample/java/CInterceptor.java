package com.location.algorithm.design.zerenlian.sample.java;

import com.location.algorithm.design.zerenlian.Request;
import com.location.algorithm.design.zerenlian.Response;

public class CInterceptor implements Interceptor{


    @Override
    public Response intercept(Chain chain) {
        Request request = chain.getRequest();
        if(request.getCode() < 300){
            return new Response(request.getParams() + " " +getClass().getSimpleName()+" 已经处理");
        }else {
            request.setParams(request.getParams()+"| 经过"+getClass().getSimpleName());
        }
        return chain.proceed(request);
    }
}
