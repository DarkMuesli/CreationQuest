package com.mygdx.game.tiledworld

import com.badlogic.gdx.maps.MapProperties

class WorldProperties(properties: MapProperties) {

    companion object {
        private const val TILE_WIDTH_KEY = "tilewidth"
        private const val TILE_HEIGHT_KEY = "tileheight"

        private const val MAP_WIDTH_IN_TILES_KEY = "width"
        private const val MAP_HEIGHT_IN_TILES_KEY = "height"
    }

    val tileWidth: Int = properties.get(TILE_WIDTH_KEY, Int::class.java)
    val tileHeight: Int = properties.get(TILE_HEIGHT_KEY, Int::class.java)

    val widthInTiles: Int = properties.get(MAP_WIDTH_IN_TILES_KEY, Int::class.java)
    val heightInTiles: Int = properties.get(MAP_HEIGHT_IN_TILES_KEY, Int::class.java)

    val widthInPixels: Int = tileWidth * widthInTiles
    val heightInPixels: Int = tileHeight * heightInTiles

}