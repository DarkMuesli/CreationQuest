package com.mygdx.game.tiledworld

import com.badlogic.gdx.math.Vector2
import java.awt.Point


//TODO: Vereinfachung der Aufrufe, nur noch World und entsprechendes Object, oder nur Obj....
class CoordinateHelper {

    companion object {

        private fun scalePoint (x: Float, y: Float, xFactor: Float, yFactor: Float) : Vector2 {
            return Vector2(x * xFactor, y * yFactor)
        }

        private fun convertVectorToPoint (v: Vector2) : Point{
            return Point(v.x.toInt(), v.y.toInt())
        }

        @JvmStatic
        fun getPixelFromCell (x: Int, y: Int, tileWidth: Float, tileHeight: Float) : Vector2 {
            return scalePoint(x.toFloat(), y.toFloat(), tileWidth, tileHeight)
        }

        @JvmStatic
        fun getCellFromPixel (x: Float, y: Float, tileWidth: Float, tileHeight: Float) : Point {
            return convertVectorToPoint(scalePoint(x, y, 1 / tileWidth, 1 / tileHeight))
        }


    }

}