package com.rain2002kr.android_youtoobu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.rain2002kr.android_youtoobu.databinding.ActivityMainBinding
import com.rain2002kr.android_youtoobu.model.VideoAdapter
import com.rain2002kr.android_youtoobu.model.VideoDto
import com.rain2002kr.android_youtoobu.model.VideoModel
import com.rain2002kr.android_youtoobu.model.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

	private val videoAdapter by lazy { VideoAdapter(itemClickListener = { videoModle->
		supportFragmentManager.fragments.find {it is PlayerFragment}?.let{
			(it as PlayerFragment).play(videoModle.sources, videoModle.title)
		}
	}) }

	private val binding  by lazy { ActivityMainBinding.inflate(layoutInflater)}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		supportFragmentManager.beginTransaction()
			.replace(R.id.fragmentContainer, PlayerFragment())
			.commit()
		binding.mainRecyclerView.adapter = videoAdapter
		binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)

		getVideoListFromAPI()
	}

	private fun getVideoListFromAPI() {
		val retrofit = Retrofit.Builder()
			.baseUrl("https://run.mocky.io")
			//.baseUrl("http://192.168.1.22:3000")
			.addConverterFactory(GsonConverterFactory.create())
			.build()

		retrofit.create(VideoService::class.java).also {
			it.getVideoList()
				.enqueue(object : Callback<VideoDto> {
					override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
						if(!response.isSuccessful){
							Log.d(TAG, "데이터 읽어오기 실패")
							return
						}
						response.body()?.let{ dto->
							dto.videos.forEach {
								Log.d(TAG, "dto : ${it.title}")
								Log.d(TAG, "dto : ${it.description}")
								Log.d(TAG, "dto : ${it.thumb}")
								Log.d(TAG, "dto : ${it.subtitle}")
								Log.d(TAG, "dto : ${it.sources}")

							}
						videoAdapter.submitList(dto.videos)

						}
					}

					override fun onFailure(call: Call<VideoDto>, t: Throwable) {

					}
				})
		}
	}
	companion object{
		const val TAG = "MainActivity"
	}


}

