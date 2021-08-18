package com.rain2002kr.android_youtoobu

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBindings
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.rain2002kr.android_youtoobu.databinding.FragmentPlayerBinding
import com.rain2002kr.android_youtoobu.model.VideoAdapter
import com.rain2002kr.android_youtoobu.model.VideoDto
import com.rain2002kr.android_youtoobu.model.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.abs

class PlayerFragment : Fragment(R.layout.fragment_player) {

	private var binding: FragmentPlayerBinding? = null
	//private lateinit var binding: FragmentPlayerBinding
	private var player :SimpleExoPlayer? = null

	private val videoAdapter by lazy { VideoAdapter(itemClickListener = { videoModel ->


		play(videoModel.sources, videoModel.title)

	}) }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
		binding = fragmentPlayerBinding

		initMotionLayoutEvent(fragmentPlayerBinding)
		initRecyclerView(fragmentPlayerBinding)
		initPlayer(fragmentPlayerBinding)
		initControlButton(fragmentPlayerBinding)
		getVideoListFromAPI()




	}




	private fun initMotionLayoutEvent(fragmentPlayerBinding: FragmentPlayerBinding){
		fragmentPlayerBinding.playerMotionLayout.setTransitionListener(object :
			MotionLayout.TransitionListener {
			override fun onTransitionStarted(motionLayout: MotionLayout?,startId: Int,endId: Int) {

			}

			override fun onTransitionChange(motionLayout: MotionLayout?,startId: Int,endId: Int,progress: Float
			) {
				binding?.let {
					// 붙인 액티비티 메인엑티비티 인지 검사
					(activity as MainActivity).also { mainActivity ->
						mainActivity.findViewById<MotionLayout>(R.id.mainMotionLayout).progress = abs(progress)
					}
				}
			}

			override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

			}

			override fun onTransitionTrigger(motionLayout: MotionLayout?,triggerId: Int,positive: Boolean,progress: Float) {

			}
		})
	}

	private fun initRecyclerView(fragmentPlayerBinding: FragmentPlayerBinding) {
		fragmentPlayerBinding.fragmentRecyclerView.apply {
			adapter = videoAdapter
			layoutManager = LinearLayoutManager(context)
		}
	}

	fun play(url:String, title:String){
		context?.let{ context ->
			val dataSourceFactory = DefaultDataSourceFactory(context)
			val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
				.createMediaSource(MediaItem.fromUri(Uri.parse(url)))
			player?.setMediaSource(mediaSource)
			player?.prepare()
			player?.play()
		}

		binding?.let{
			it.playerMotionLayout.transitionToEnd()
			it.bottomTitleTextView.text = title
		}
	}

	private fun initPlayer(fragmentPlayerBinding: FragmentPlayerBinding) {
		context?.let{ context->
			player = SimpleExoPlayer.Builder(context).build()
		}
		fragmentPlayerBinding.playerView.player = player

		binding?.let { it ->
			player?.addListener(object : Player.Listener{
				override fun onIsPlayingChanged(isPlaying: Boolean) {
					super.onIsPlayingChanged(isPlaying)
					when {
						isPlaying -> { it.bottomPlayerControlButton.setImageResource(R.drawable.ic_baseline_pause_24) }
						else -> {it.bottomPlayerControlButton.setImageResource(R.drawable.ic_baseline_play_arrow_24) }
					}
				}
				override fun onEvents(player: Player, events: Player.Events) {
					super.onEvents(player, events)
				}
			})
		}
	}

	private fun initControlButton(fragmentPlayerBinding: FragmentPlayerBinding) {
		fragmentPlayerBinding.bottomPlayerControlButton.setOnClickListener {
			val player = this.player?: return@setOnClickListener

			when {
				player.isPlaying -> { player.pause()}
				else -> { player.play() }
			}
		}
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
							Log.d(MainActivity.TAG, "데이터 읽어오기 실패")
							return
						}
						response.body()?.let{ dto->
							dto.videos.forEach {
								Log.d(MainActivity.TAG, "dto : ${it.title}")
								Log.d(MainActivity.TAG, "dto : ${it.description}")
								Log.d(MainActivity.TAG, "dto : ${it.thumb}")
								Log.d(MainActivity.TAG, "dto : ${it.subtitle}")
								Log.d(MainActivity.TAG, "dto : ${it.sources}")

							}
							videoAdapter.submitList(dto.videos)

						}
					}

					override fun onFailure(call: Call<VideoDto>, t: Throwable) {

					}
				})
		}
	}

	override fun onStop() {
		super.onStop()
		player?.pause()
	}

	override fun onDestroy() {
		super.onDestroy()
		binding = null
		player?.release()

	}



}



