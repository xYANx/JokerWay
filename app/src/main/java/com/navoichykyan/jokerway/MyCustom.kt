package com.navoichykyan.jokerway

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.absoluteValue
import kotlin.math.sqrt
import kotlin.random.Random


class MyCustom(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    val list = mutableListOf<Rect>()
    val clearRectList = mutableListOf<Rect>()
    var startPositionX = 0
    var startPositionY = 0

    val bitmapRect = BitmapFactory.decodeResource(
        resources,
        R.drawable.rect_full
    )
    val bitmapRectHalfClear = BitmapFactory.decodeResource(
        resources,
        R.drawable.rect_half_clear
    )
    val bitmapRectClear = BitmapFactory.decodeResource(
        resources,
        R.drawable.rect_clear
    )

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        var square = sqrt((width*height/4/20).toDouble()).toInt()
        if(square%2 != 0){
            square--
        }
         rectSize = square
         rectSide = square/2
         rectSumSize = rectSize + rectSide
        dynamicCreatingRects()
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for(i in 0 until list.size){
            canvas.drawBitmap(bitmapRect, null, list[i], null)
        }
        if(clearRectList.size == 1){
            canvas.drawBitmap(bitmapRectHalfClear, null, clearRectList[0], null)
        }
        if(clearRectList.size == 2){
            canvas.drawBitmap(bitmapRectHalfClear, null, clearRectList[1], null)
            canvas.drawBitmap(bitmapRectClear, null, clearRectList[0], null)
        }
        var rect = Rect()
        rect.set(0-rectSide/2, 0-rectSide/2, rectSide/2, rectSide/2)
        rect.offset(startPositionX, startPositionY)
        canvas.drawBitmap(bitmapVector!!, null, rect, null)
    }

    fun dynamicCreatingRects(){
        startPositionX = (width / 2)
        startPositionY = (height - (height / 10))
        bitmapVector = BitmapFactory.decodeResource(
                resources,
                R.drawable.vector_up)
        clearRectList.clear()
        list.clear()
        var rect = Rect()
        rect.set(0, 0, rectSize, rectSize)
        rect.offset((width / 2) - rectSide, (height - (height / 10)) - rectSide)
        list.add(rect)
        var rect2 = Rect()
        rect2.set(0, 0, rectSize, rectSize)
        rect2.offset(list[0].centerX() - rectSide, list[0].centerY() - rectSumSize)
        list.add(rect2)
        val count = Random.nextInt(10, 18)
        for(i in 1 .. count){
            val rectNew = Rect()
            rectNew.set(0, 0, rectSize, rectSize)
            var side = Random.nextInt(0, 3)
            val timeRect = list[list.size-1]
            var result = takeTurn(side, timeRect)
            var xNew = result[0]
            var yNew = result[1]
            var isExist = true
            var isEnter = 1
            val rememberSide = side.absoluteValue
            while (isExist) {
                isExist = false
                isEnter = 1
                for (rectInList in list) {
                    if ((xNew == rectInList.centerX() - rectSide && yNew == rectInList.centerY() - rectSide) && (xNew + rectSize == rectInList.centerX() + rectSide && yNew + rectSize == rectInList.centerY() + rectSide)) {
                        isExist = true
                        break
                    }
                }
                if(isExist || !checkEndOfBounds(xNew, yNew)){
                    if(side == 3){
                        side = 0
                    } else side++
                    if(side != rememberSide){
                        result = takeTurn(side, timeRect)
                        xNew = result[0]
                        yNew = result[1]
                        isExist = true
                    } else {
                        isExist = false
                        isEnter = 0
                    }
                }
            }
            if(!isExist && isEnter == 1) {
                if ((xNew in rectSide..width - rectSize) && (yNew in rectSide..height - rectSize)) {
                    rectNew.offset(xNew, yNew)
                    list.add(rectNew)
                }
            }
        }
    }

    private fun checkEndOfBounds(x:Int, y:Int) = (x in rectSide..width - rectSize) && (y in rectSide..height - rectSize)

    private fun takeTurn(side : Int, timeRect : Rect) : Array<Int>{
        var xNew = 0
        var yNew = 0
        when(side){
            0 -> {
                xNew = timeRect.centerX() - rectSide
                yNew = timeRect.centerY() - rectSumSize
            }    //UP
            1 -> {
                xNew = timeRect.centerX() + rectSide
                yNew = timeRect.centerY() - rectSide
            }    //RIGHT
            2 -> {
                xNew = timeRect.centerX() - rectSide
                yNew = timeRect.centerY() + rectSide
            }    //DOWN
            3 -> {
                xNew = timeRect.centerX() - rectSumSize
                yNew = timeRect.centerY() - rectSide
            }    //LEFT
        }
        return arrayOf(xNew, yNew)
    }

    companion object{
        var rectSide = 0
        var rectSize = 0
        var rectSumSize = 0
        var bitmapVector : Bitmap? = null
    }
}