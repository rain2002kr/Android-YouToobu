package com.rain2002kr.android_youtoobu.model

import retrofit2.Call
import retrofit2.http.GET

interface VideoService {
	@GET("/v3/6eb6bea3-8cdd-424a-a83a-ee3435355d87")
	//@GET("/api/users")

	fun getVideoList(): Call<VideoDto>
}



// /v3/1bd51bcf-0da2-4e97-9e84-5c623b859d66
//https://run.mocky.io/v3/6eb6bea3-8cdd-424a-a83a-ee3435355d87

// 짧은것
// https://run.mocky.io/v3/1bd51bcf-0da2-4e97-9e84-5c623b859d66