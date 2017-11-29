package de.thkoeln.creationquest.tiledworld

import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapLayers
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import de.thkoeln.creationquest.utils.addDistinct

//TODO: Name: MapData?
class TiledWorldData(layers: MapLayers, gameObjectProvider: GameObjectProvider) {

    companion object {
        private const val OBJECTS_LAYER_KEY = "objects"
        private const val OBJECT_TYPE_KEY = "type"
    }

    private val objectTileLayer: MapLayer? = layers.get(OBJECTS_LAYER_KEY)

    var gameObjectList = mutableListOf<GameObject>()

    val tileLayersArray = layers.getByType(TiledMapTileLayer::class.java)

    val loadingZoneObjects = mutableListOf<MapObject>()
    val playerSpawnObjects = mutableListOf<MapObject>()
    val triggerObjects = mutableListOf<MapObject>()

    val fgTileLayers = mutableListOf<TiledMapTileLayer>()
    val bgTileLayers = mutableListOf<TiledMapTileLayer>()
    val collisionTileLayers = mutableListOf<TiledMapTileLayer>()

    val fgTileLayersIndices = mutableListOf<Int>()
    val bgTileLayersIndices = mutableListOf<Int>()
    val collisionTileLayersIndices = mutableListOf<Int>()

    init {
        if (objectTileLayer == null)
            throw Exception("No Object Tile Layer found")

        for (obj in objectTileLayer.objects)
            when (obj.properties.get(OBJECT_TYPE_KEY, String::class.java)) {
                "LoadingZone" -> loadingZoneObjects += obj
                "PlayerSpawn" -> playerSpawnObjects += obj
                "Trigger" -> triggerObjects += obj
                "PlayerCreate" -> gameObjectList.addDistinct(gameObjectProvider.returnPlayer(obj))
                "NPCCreate" -> gameObjectList.add(gameObjectProvider.createNPC(obj))
                "MoralNPCCreate" -> gameObjectList.add(gameObjectProvider.createMoralNPC(obj))
                "MonologNPCCreate" -> gameObjectList.add(gameObjectProvider.createMonologNPC(obj))
                "RandomTextNPCCreate" -> gameObjectList.add(gameObjectProvider.createRandomTextNPC(obj))
                "Fruit" -> gameObjectList.add(gameObjectProvider.createFruit(obj))
                "Sign" -> gameObjectList.add(gameObjectProvider.createSign(obj))
                "Door" -> gameObjectList.add(gameObjectProvider.createDoor(obj))
                "SlotMachine" -> gameObjectList.add(gameObjectProvider.createSlotMachine(obj))
                else -> throw Exception("Unexpected MapObject Type")
            }

        for ((index, layer) in tileLayersArray.withIndex())
            when (layer.name.substring(0..2)) {
                "fgr" -> {
                    fgTileLayers += layer
                    fgTileLayersIndices += index
                }
                "bgr" -> {
                    bgTileLayers += layer
                    bgTileLayersIndices += index
                }
                "col" -> {
                    collisionTileLayers += layer
                    collisionTileLayersIndices += index
                }
            }


    }


}
