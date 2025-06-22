package com.example.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class GameView(var c :Context,var gameTask :GameTask):View(c)
{
    private var myPaint :Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var myRocketPosition = 0
    private val asteroid = ArrayList<HashMap<String,Any>>()

    var viewWidth = 0
    var viewHeight = 0
    init{
        myPaint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if(time % 700 < 10 + speed){
            val map = HashMap<String,Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            asteroid.add(map)
        }
        time = time + 10 + speed
        val rocketWidth = viewWidth / 5
        val rocketHeight = rocketWidth + 10
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.rocket,null)

        d.setBounds(
            myRocketPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight - 2 - rocketHeight,
            myRocketPosition * viewWidth / 3 + viewWidth / 15 + rocketWidth - 25 ,
            viewHeight - 2
        )
        d.draw(canvas!!)
        myPaint!!.color = Color.GREEN
        var highScore = 0

        for(i in asteroid.indices){
            try {
                val asteroidX = asteroid[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                var asteroidY = time - asteroid[i]["startTime"] as Int
                val d2 = resources.getDrawable(R.drawable.rock,null)

                d2.setBounds(
                    asteroidX + 25 , asteroidY - rocketHeight , asteroidX + rocketWidth - 25 , asteroidY
                )
                d2.draw(canvas)
                if(asteroid[i]["lane"] as Int == myRocketPosition){
                    if(asteroidY > viewHeight - 2 - rocketHeight && asteroidY < viewHeight - 2 ){

                        gameTask.closeGame(score)
                    }
                }
                if(asteroidY > viewHeight + rocketHeight){
                    asteroid.removeAt(i)
                    score++
                    speed = 1 + Math.abs(score / 8)
                    if(score > highScore){
                        highScore = score
                    }
                }

            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score : $score",80f,80f,myPaint!!)
        canvas.drawText("Speed : $speed",380f,80f,myPaint!!)
        invalidate()

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN ->{
                val x1 = event.x
                if(x1 < viewWidth/2){
                    if(myRocketPosition > 0){
                        myRocketPosition--
                    }
                }
                if(x1 > viewWidth / 2){
                    if(myRocketPosition<2){
                        myRocketPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP ->{}
        }
        return true
    }
}