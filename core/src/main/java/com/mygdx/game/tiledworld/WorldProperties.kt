package com.mygdx.game.tiledworld

import com.badlogic.gdx.maps.MapProperties

class WorldProperties(properties: MapProperties) {

    companion object {
        private const val TILE_WIDTH_KEY = "tilewidth"
        private const val TILE_HEIGHT_KEY = "tileheight"

        private const val MAP_WIDTH_IN_TILES_KEY = "width"
        private const val MAP_HEIGHT_IN_TILES_KEY = "height"
    }

    val tileWidth: Float = properties.get(TILE_WIDTH_KEY, Float::class.java)
    val tileHeight: Float = properties.get(TILE_HEIGHT_KEY, Float::class.java)

    val widthInTiles: Int = properties.get(MAP_WIDTH_IN_TILES_KEY, Int::class.java)
    val heightInTiles: Int = properties.get(MAP_HEIGHT_IN_TILES_KEY, Int::class.java)

    val widthInPixels: Float = tileWidth * widthInTiles
    val heightInPixels: Float = tileHeight * heightInTiles

}