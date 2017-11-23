package com.mygdx.game.utils

import java.time.*

class TimeHelper {

    companion object {
        fun getHourOfDay(): Int = LocalDateTime.now().hour

        fun getDayOfDecember(): Int {
            return if (LocalDateTime.now().month != Month.DECEMBER)
                -1
            else
                LocalDateTime.now().dayOfMonth
        }
    }

}