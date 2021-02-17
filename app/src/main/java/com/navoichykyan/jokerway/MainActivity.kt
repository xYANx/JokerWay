package com.navoichykyan.jokerway

import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

class MainActivity : AppCompatActivity(), EventInterface {

    var isEnd = false
    private var x1 = 0f
    private var x2 = 0f
    private var y1 = 0f
    private var y2 = 0f
    private var turn = 0
    private val fragment = ResultFragment()
    private val WIN_FRAGMENT = 0
    private val LOSE_FRAGMENT = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startButton.setOnClickListener {
            start()
        }
    }

    private fun addFragment(result: Int){
        val ar = Bundle()
        ar.putInt("result", result)
        fragment.arguments = ar
        supportFragmentManager
                .beginTransaction()
                .add(R.id.gameActivityFragment, fragment)
                .addToBackStack("result")
                .commit()
    }

    private fun setBitmap(drawable : Int){
        MyCustom.bitmapVector = BitmapFactory.decodeResource(
                resources,
                drawable)
    }

    private fun start(){
        startButton.visibility = View.INVISIBLE
        jokerImage.visibility = View.INVISIBLE
        turn = 0
        var isBound = false
        var findedRect : Rect? = null
        Thread(Runnable {
            kotlin.run {
                while (!isEnd){
                    if(customView.list.size > 0) {
                        if (findedRect == null) {
                            for (rect in customView.list) {
                                if ((customView.startPositionX >= rect.centerX() - MyCustom.rectSide && customView.startPositionX <= rect.centerX() + MyCustom.rectSide) && (customView.startPositionY >= rect.centerY() - MyCustom.rectSide && customView.startPositionY <= rect.centerY() + MyCustom.rectSide)) {
                                    isBound = true
                                    findedRect = rect
                                }
                            }
                            if(!isBound) isEnd = true
                        } else {
                            if ((customView.startPositionX >= findedRect!!.centerX() - MyCustom.rectSide && customView.startPositionX <= findedRect!!.centerX() + MyCustom.rectSide) && (customView.startPositionY >= findedRect!!.centerY() - MyCustom.rectSide && customView.startPositionY <= findedRect!!.centerY() + MyCustom.rectSide)) {
                            } else {
                                customView.list.remove(findedRect!!)
                                if(customView.clearRectList.size == 2){
                                    customView.clearRectList.removeAt(0)
                                    customView.clearRectList.add(findedRect!!)
                                } else customView.clearRectList.add(findedRect!!)
                                findedRect = null
                                isBound = false
                            }
                        }

                        Thread.sleep(3)
                        when (turn) {
                            0 -> customView.startPositionY--    //UP
                            1 -> customView.startPositionX++    //RIGHT
                            2 -> customView.startPositionY++    //DOWN
                            3 -> customView.startPositionX--    //LEFT
                        }
                        customView.postInvalidate()
                    } else isEnd = true
                }
                if(customView.list.size == 0){
                    showResult(WIN_FRAGMENT)
                } else showResult(LOSE_FRAGMENT)
            }}).start()
    }

    private fun showResult(result: Int){
        this.runOnUiThread(Runnable {
            addFragment(result)
            isEnd = false
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {

            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                y1 = event.y
            }
            MotionEvent.ACTION_UP -> {
                x2 = event.x
                y2 = event.y
                if((abs(x2-x1)) > (abs(y2-y1))) {
                    if (x2 > x1) {
                        turn = 1
                        setBitmap(R.drawable.vector_right)
                    } else if (x2 < x1) {
                        turn = 3
                        setBitmap(R.drawable.vector_left)
                    }
                } else if((abs(x2-x1)) < (abs(y2-y1))){
                    if (y2 > y1) {
                        turn = 2
                        setBitmap(R.drawable.vector_down)
                    } else if (y2 < y1) {
                        turn = 0
                        setBitmap(R.drawable.vector_up)
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun replayFunction() {
        startButton.visibility = View.VISIBLE
        jokerImage.visibility = View.VISIBLE
        customView.dynamicCreatingRects()
        customView.invalidate()
    }
}