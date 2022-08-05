package ps.crossworking.common

import okhttp3.OkHttpClient
import okhttp3.Request

val authToken: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
    val newRequest: Request = chain.request().newBuilder()
        .addHeader("Authorization", "Bearer ${UserInstance.token}")
        .build()
    chain.proceed(newRequest)
}.build()