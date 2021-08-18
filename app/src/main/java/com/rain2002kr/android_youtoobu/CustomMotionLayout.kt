package com.rain2002kr.android_youtoobu

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout

class CustomMotionLayout(context: Context, attributeSet: AttributeSet?=null): MotionLayout(context,attributeSet) {

	private var motionTouchStarted = false // 스타트값을 터치했을때만 작업이 이루어지도록...
	private val mainContainerView by lazy { findViewById<View>(R.id.mainContainerLayout) }
	private val hitRect = Rect()

	// 모션 레이아웃 최초 실행
	init {
		setTransitionListener(object : TransitionListener{
			override fun onTransitionStarted(motionLayout: MotionLayout?,startId: Int,endId: Int) {}

			override fun onTransitionChange(motionLayout: MotionLayout?,startId: Int,endId: Int,progress: Float) {}

			override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
				// 모션 터치 스타트 체크
				motionTouchStarted = false
			}
			override fun onTransitionTrigger(motionLayout: MotionLayout?,triggerId: Int,positive: Boolean,progress: Float) {}
		})

	}

	// onTouchEvent 오버라이드후, event null 지우기
	override fun onTouchEvent(event: MotionEvent): Boolean {

		// 액션 다운과 움직일때
		when(event.actionMasked){
			MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
				motionTouchStarted = false
				return super.onTouchEvent(event)

			}
		}
		if (!motionTouchStarted){
			// hitRect 안에다가 값을 넣어서 반환해줌.
			mainContainerView.getHitRect(hitRect)
			// x,y 좌표값이 hitRect 안에서 일어났느냐 체크 해줌.
			motionTouchStarted = hitRect.contains(event.x.toInt(), event.y.toInt())
		}
		return super.onTouchEvent(event) && motionTouchStarted
	}


	private val gestureListener by lazy {
		object : GestureDetector.SimpleOnGestureListener(){
			override fun onScroll(e1: MotionEvent,e2: MotionEvent,distanceX: Float,distanceY: Float): Boolean {
				// hitRect 안에다가 값을 넣어서 반환해줌.
				mainContainerView.getHitRect(hitRect)
				return hitRect.contains(e1.x.toInt(), e1.y.toInt())
			}
		}
	}

	private val gestureDetector by lazy {
		GestureDetector(context, gestureListener)
	}

	override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
		return gestureDetector.onTouchEvent(event)
	}

}