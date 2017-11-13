package com.mygdx.game.tiledworld

import com.badlogic.gdx.maps.MapObject

class GameObjectProvider(val world: TiledWorld) {

    var player: Player? = null

    fun returnPlayer(mapObject: MapObject) : Player {
        if (player == null)
            player = Player(mapObject, world)

        return player!!
    }

    //TODO what if invoked before returnPlayer(mapObject) ?
    fun returnPlayer () = player

    fun createNPC (mapObject: MapObject) = NPC (mapObject, world)

    fun createMoralNPC (mapObject: MapObject) = MoralNPC (mapObject, world)

    fun createMonologNPC (mapObject: MapObject) = MonologNPC (mapObject, world)

    fun createRandomTextNPC (mapObject: MapObject) = RandomTextNPC (mapObject, world)

    fun createFruit (mapObject: MapObject) = Fruit (mapObject, world)

    fun createSign (mapObject: MapObject) = Sign (mapObject, world)

    fun createSlotMachine (mapObject: MapObject) = SlotMachine (mapObject, world)

}