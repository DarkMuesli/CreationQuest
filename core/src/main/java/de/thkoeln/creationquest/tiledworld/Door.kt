package de.thkoeln.creationquest.tiledworld

import com.badlogic.gdx.maps.MapObject
import de.thkoeln.creationquest.utils.TimeHelper

class Door(mapObject: MapObject, world: TiledWorld) : GameObject(mapObject, world) {

    private val openFrom: Int = mapObject.properties.get("openFrom", Int::class.java)

    init {
        penetrable = TimeHelper.getDayOfDecember() >= openFrom
    }

    override fun onInteract(obj: GameObject?): Boolean {
        if (TimeHelper.getDayOfDecember() < openFrom)
            world.addText("Diese Tür scheint sich nicht öffnen zu lassen...", 5f, world.gameObjectProvider.returnPlayer())

        return true
    }
}