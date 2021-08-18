package com.rain2002kr.android_youtoobu.model

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.rain2002kr.android_youtoobu.databinding.FragmentPlayerBinding
import com.rain2002kr.android_youtoobu.databinding.ItemVideoBinding

class VideoAdapter(val itemClickListener: (VideoModel)->Unit):ListAdapter<VideoModel,VideoAdapter.ViewHolder>(diffUtil) {
	inner class ViewHolder(private val  binding: ItemVideoBinding):RecyclerView.ViewHolder(binding.root){
		fun bind(videoModel: VideoModel){
			binding.titleTextView.text = videoModel.title
			binding.descriptionTextView.text = videoModel.description

			Glide
				.with(binding.videoThumb.context)
				.load(videoModel.thumb)
				.transform(CenterCrop())
				.into(binding.videoThumb)

			binding.root.setOnClickListener {
				itemClickListener(videoModel)
			}

		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(ItemVideoBinding.inflate(LayoutInflater.from(parent.context),parent,false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(currentList[position])
	}

	companion object {
		val diffUtil = object:DiffUtil.ItemCallback<VideoModel>(){
			override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
				return oldItem.title == newItem.title
			}

			override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
				return oldItem == newItem
			}
		}
	}
}