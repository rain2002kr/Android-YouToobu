package com.rain2002kr.android_youtoobu.model

data class VideoModel(
	val title:String,
	val subtitle: String,
	val description: String,
	val sources: String,
	val thumb: String,

)
{
	constructor():this("","","","","")
}

//"description": "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! In the typical cartoon tradition he prepares the nasty rodents a comical revenge.\n\nLicensed under the Creative Commons Attribution license\nhttps://www.bigbuckbunny.org",
//"sources": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
//"subtitle": "By Blender Foundation",
//"thumb": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg",
//"title": "Big Buck Bunny"