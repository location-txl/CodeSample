package com.location.algorithm.design.zerenlian.sample

import com.location.algorithm.design.zerenlian.Handler
import com.location.algorithm.design.zerenlian.Request
import com.location.algorithm.design.zerenlian.Response

class BHandler: Handler() {
    override fun handlerEvent(request: Request): Response? {
        println("Test Handler ${hashCode()}")
        if(request.code <200){
            return Response("params=[${request.params}] ${javaClass.simpleName} 已经处理")
        }else{
            request.params+="|${javaClass.simpleName}"
        }
        return next?.handlerEvent(request)
    }
}