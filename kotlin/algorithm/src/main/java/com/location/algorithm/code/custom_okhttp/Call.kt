package com.location.algorithm.code.custom_okhttp

import jdk.internal.joptsimple.internal.Messages.message
import jdk.internal.net.http.common.Log.headers
import java.net.Socket
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toJavaDuration

class Call(
    val request: Request,
    val customOkhttp: CustomOkhttp,
){
    fun execute(): Response {
        val client = HttpClient.newBuilder()
            .connectTimeout(customOkhttp.connectTimeout.toDuration(DurationUnit.MILLISECONDS).toJavaDuration())
            .build()
        val respose = client.send<String>(
             HttpRequest.newBuilder()
                 .uri(URI.create(request.url))
                 .GET()
                .build(),
            HttpResponse.BodyHandlers.ofString()
        )

        return Response(
            code = 200,
            message = "ok",
            headers = Headers(),
            body = respose.body().toByteArray()
        )
    }
}