//package de.thkoeln.creationquest.tiledworld
//
//import com.badlogic.gdx.Gdx
//import com.badlogic.gdx.audio.Sound
//import com.badlogic.gdx.graphics.Texture
//import com.badlogic.gdx.graphics.g2d.Sprite
//import com.badlogic.gdx.graphics.g2d.TextureRegion
//import com.badlogic.gdx.maps.MapObject
//import com.badlogic.gdx.math.Vector2
//import de.thkoeln.creationquest.utils.CoordinateHelper
//
//
//class Fruit internal constructor(mapObject: MapObject, world: TiledWorld) : GameObject(mapObject, world) {
//
//    private var plucked: Boolean = false
//    private var counter: Float = 0.toFloat()
//
//    private val text: String
//
//    private var pluckedSprt: Sprite? = null
//
//    init {
//
//        var row = 0
//        var col = 0
//        var tileSize = world.mapProp.tileHeight
//        try {
//            row = mapObject.properties.get("row", Int::class.java)
//            col = mapObject.properties.get("column", Int::class.java)
//            tileSize = mapObject.properties.get("tileSize", Int::class.java)
//        } catch (ignored: NullPointerException) {
//            Gdx.app.debug(TAG, "No parameters for texture detected; using defaults.")
//            row = 0
//            col = 0
//            tileSize = world.mapProp.tileHeight
//        } finally {
//            val split = TextureRegion.split(this.sprt.texture, tileSize, tileSize)
//            sprt = Sprite(split[row][col])
//        }
//
//        val testString = mapObject.properties.get("testString", String::class.java)
//        Gdx.app.debug(TAG, "String is: " + testString)
//
//
//        try {
//            val pluckedTex = Texture(mapObject.properties.get("pluckedPath", String::class.java))
//            var pluckedRow = 0
//            var pluckedCol = 0
//            var pluckedTileSize = pluckedTex.height
//            try {
//                pluckedRow = mapObject.properties.get("pluckedRow", Int::class.java)
//                pluckedCol = mapObject.properties.get("pluckedColumn", Int::class.java)
//                pluckedTileSize = mapObject.properties.get("pluckedTileSize", Int::class.java)
//            } catch (ignored: NullPointerException) {
//                Gdx.app.debug(TAG, "No parameters for plucked texture detected; using defaults.")
//                pluckedRow = 0
//                pluckedCol = 0
//                pluckedTileSize = getWorld().mapProp.tileHeight
//            } finally {
//                val split = TextureRegion.split(pluckedTex, pluckedTileSize, pluckedTileSize)
//                pluckedSprt = Sprite(split[pluckedRow][pluckedCol])
//            }
//        } catch (ignored: NullPointerException) {
//            Gdx.app.debug(TAG, "No plucked texture detected; using normal texture.")
//            pluckedSprt = sprt
//        }
//
//        val v = CoordinateHelper.getPixelFromCell(x, y, world.mapProp.tileWidth.toFloat(), world.mapProp.tileHeight.toFloat())
//        sprt.setPosition(v.x, v.y)
//
//        counter = 0f
//
//        text = words[(Math.random() * words.size).toInt()].trim { it <= ' ' }
//    }
//
//    override fun onInteract(obj: GameObject): Boolean {
//        if (!plucked && obj is Entity) {
//            obj.pull(this)
//        }
//        return true
//    }
//
//    override fun update(deltaTime: Float) {
//        if (plucked) {
//            if (counter <= FADE_TIME / 10f)
//                sprt.translate(0f, 200f / FADE_TIME * deltaTime)
//
//            val newSpriteAlpha = Math.max(0f, sprt.color.a - 2 / FADE_TIME * deltaTime)
//            sprt.setAlpha(Math.max(0f, newSpriteAlpha))
//
//            counter += deltaTime
//            if ((counter) >= FADE_TIME) {
//                removeFruit()
//            }
//        }
//    }
//
//    fun pluckFruit() {
//
//        plucked = true
//        penetrable = true
//        counter = 0f
//        Gdx.app.log(TAG, "Fruit is plucked.")
//
//        val id = sound.play()
//        sound.setPitch(id, 1f + Math.random().toFloat() * PITCH_RANGE - PITCH_RANGE / 2)
//
//        world.addText(text, FADE_TIME, FADE_TIME, this)
//
//        sprt.setRegion(pluckedSprt!!)
//    }
//
//    private fun removeFruit() {
//        world.removeGameObject(this)
//    }
//
//    companion object {
//
//        private val TAG = Fruit::class.java.name
//
//        private val FADE_TIME = 3f
//        private val PITCH_RANGE = 0.8f
//        private val sound = Gdx.audio.newSound(Gdx.files.internal("sounds/plop.wav"))
//        private val words = Gdx.files.internal("text/test.txt").readString().trim { it <= ' ' }.split("\n?\r".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//    }
//
//}
