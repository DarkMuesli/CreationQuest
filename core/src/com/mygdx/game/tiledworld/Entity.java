package com.mygdx.game.tiledworld;

import java.awt.Point;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.mygdx.game.Constants;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;

/**
 * Abstract type for all entities, as in "living things". Characterized by being
 * able to move and/or interact with.
 * 
 * @author Matthias Gross
 *
 */
public abstract class Entity extends GameObject {

	enum State {
		IDLE, MOVING, WAITING, PULLING
	}

	protected State state = State.IDLE;

	protected CommandGenerator comGen;
	private Command onceCommand;
	private Command contCommand;

	private Texture sheet;
	private TextureRegion[] idleFrames;
	private Animation[] moveAnimations;
	private Animation[] pullAnimations;
	private boolean hasPullAnimation;
	private float animationTimer = 0;

	protected Point movement = new Point(0, -1);

	private float timer = 0;
	private float pullTimer = 0;
	private float waitTime = 0;
	private Fruit pulledFruit;

	private boolean pullingOdd;

	@SuppressWarnings("unused")
	private static final String TAG = Entity.class.getName();

	/**
	 * The movement speed of the {@link Entity}, 1 meaning 100% or "normal"
	 * movement speed.
	 */
	protected float moveSpeed;

	/**
	 * {@link Direction}, the {@link Entity} is facing.
	 */
	protected Direction facing;

	/**
	 * Instantiates an {@link Entity} with the given coordinates, movement
	 * speed, {@link Direction} to face, {@link Sprite} to be represented by and
	 * {@link TiledWorld} to be present in.
	 * 
	 * @param x
	 *            Cell-based x coordinate
	 * @param y
	 *            Cell-based y coordinate
	 * @param sprt
	 *            {@link Sprite} the {@link Entity} will be represented by
	 * @param moveSpeed
	 *            Speed the {@link Entity} will move with, 1 meaning 100% or
	 *            "normal" speed
	 * @param facing
	 *            {@link Direction} the {@link Entity} will be facing
	 * @param world
	 *            {@link TiledWorld} the {@link Entity} will be present in
	 */
	public Entity(int x, int y, Sprite sprt, float moveSpeed, Direction facing, TiledWorld world) {
		super(x, y, sprt, world);
		this.moveSpeed = moveSpeed;
		this.facing = facing;
	}

	/**
	 * Instantiates an {@link Entity} with 100% movement speed at the given
	 * coordinates, represented by the given {@link Sprite} in the given
	 * {@link TiledWorld}, facing down.
	 * 
	 * @param x
	 *            Cell-based x coordinate
	 * @param y
	 *            Cell-based y coordinate
	 * @param sprt
	 *            {@link Sprite} the {@link Entity} will be represented by
	 * @param world
	 *            {@link TiledWorld} the {@link Entity} will be present in
	 */
	public Entity(int x, int y, Sprite sprt, TiledWorld world) {
		this(x, y, sprt, 1, Direction.DOWN, world);
	}

	/**
	 * Instantiates an {@link Entity} with 100% movement speed at the given
	 * coordinates, represented by the given {@link Texture} in the given
	 * {@link TiledWorld}, facing down.
	 * 
	 * @param x
	 *            Cell-based x coordinate
	 * @param y
	 *            Cell-based y coordinate
	 * @param tex
	 *            {@link Texture} the {@link Entity} will be represented by
	 * @param world
	 *            {@link TiledWorld} the {@link Entity} will be present in
	 */
	public Entity(int x, int y, Texture tex, TiledWorld world) {
		this(x, y, new Sprite(tex), world);

		// TODO: FIX THIS not to be the only functioning constructor

		sheet = tex;

		TextureRegion[][] tmp = TextureRegion.split(sheet, 32, 36);

		moveAnimations = new Animation[4];
		idleFrames = new TextureRegion[4];
		for (int i = 0; i < 4; i++) {
			idleFrames[i] = tmp[i][1];
			moveAnimations[i] = new Animation(0.2f, tmp[i]);
			moveAnimations[i].setPlayMode(PlayMode.LOOP_PINGPONG);
		}

		updateSpriteRegion();
		sprt.setBounds(getPixelPosition().x, getPixelPosition().y + 5, getWorld().getTileWidth(),
				sprt.getRegionHeight() * getWorld().getTileWidth() / sprt.getRegionWidth());

	}

	/**
	 * Instantiates an {@link Entity} with 100% movement speed at the coordinate
	 * origin represented by the given {@link Sprite} in the given
	 * {@link TiledWorld}, facing down.
	 * 
	 * @param sprt
	 *            {@link Sprite}, the {@link Entity} will be represented by
	 * @param world
	 *            {@link TiledWorld}, the {@link Entity} will be present in
	 */
	public Entity(Sprite sprt, TiledWorld world) {
		this(0, 0, sprt, world);
	}

	/**
	 * Instantiates an {@link Entity} with 100% movement speed at the coordinate
	 * origin represented by the given {@link Texture} in the given
	 * {@link TiledWorld}, facing down.
	 * 
	 * @param tex
	 *            {@link Texture}, the {@link Entity} will be represented by
	 * @param world
	 *            {@link TiledWorld}, the {@link Entity} will be present in
	 */
	public Entity(Texture tex, TiledWorld world) {
		this(0, 0, new Sprite(tex), world);
	}

	public Entity(MapObject mapObject, TiledWorld world) {
		super(mapObject, world);

		Texture tex = new Texture(mapObject.getProperties().get("path", String.class));

		sheet = tex;

		TextureRegion[][] tmp;
		Integer width, height;
		if ((width = mapObject.getProperties().get("tileWidth", Integer.class)) != null) {
			height = mapObject.getProperties().get("tileHeight", Integer.class);
			tmp = TextureRegion.split(sheet, width, height);
		} else {
			tmp = TextureRegion.split(sheet, 32, 36);
		}

		Integer[] rowsPull = new Integer[4];
		if ((rowsPull[0] = mapObject.getProperties().get("rowPullUp", Integer.class)) != null) {
			rowsPull[1] = mapObject.getProperties().get("rowPullRight", Integer.class);
			rowsPull[2] = mapObject.getProperties().get("rowPullDown", Integer.class);
			rowsPull[3] = mapObject.getProperties().get("rowPullLeft", Integer.class);
			hasPullAnimation = true;
		}

		Integer[] rowsMove = new Integer[4];
		if ((rowsMove[0] = mapObject.getProperties().get("rowUp", Integer.class)) != null) {
			rowsMove[1] = mapObject.getProperties().get("rowRight", Integer.class);
			rowsMove[2] = mapObject.getProperties().get("rowDown", Integer.class);
			rowsMove[3] = mapObject.getProperties().get("rowLeft", Integer.class);
		} else {
			for (int i = 0; i < rowsMove.length; i++)
				rowsMove[i] = i;
		}

		idleFrames = new TextureRegion[4];
		moveAnimations = new Animation[4];
		pullAnimations = new Animation[4];
		for (int i = 0; i < 4; i++) {
			idleFrames[i] = tmp[rowsMove[i]][1];
			moveAnimations[i] = new Animation(0.2f, tmp[rowsMove[i]]);
			moveAnimations[i].setPlayMode(PlayMode.LOOP_PINGPONG);
			if (hasPullAnimation) {
				pullAnimations[i] = new Animation(0.2f, tmp[rowsPull[i]]);
				pullAnimations[i].setPlayMode(PlayMode.LOOP_PINGPONG);
			}
		}
		String facingString = mapObject.getProperties().get("facing", String.class);
		if (facingString != null)
			facing = Direction.valueOf(facingString);
		else
			facing = Direction.DOWN;

		Float moveSpeed;
		if ((moveSpeed = mapObject.getProperties().get("moveSpeed", Float.class)) == null)
			this.moveSpeed = 1f;
		else
			this.moveSpeed = moveSpeed;

		updateSpriteRegion();
		sprt.setBounds(getPixelPosition().x, getPixelPosition().y + 5, getWorld().getTileWidth(),
				sprt.getRegionHeight() * getWorld().getTileWidth() / sprt.getRegionWidth());
	}

	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(State state) {
		this.state = state;
	}

	public CommandGenerator getComGen() {
		return comGen;
	}

	public void setComGen(CommandGenerator comGen) {
		this.comGen = comGen;
	}

	/**
	 * @return current movement sped of this {@link Entity}
	 */
	public float getMoveSpeed() {
		return moveSpeed;
	}

	/**
	 * Sets the current movement speed for this {@link Entity}
	 * 
	 * @param moveSpeed
	 */
	public void setMoveSpeed(float moveSpeed) {
		if (moveSpeed > 0)
			this.moveSpeed = moveSpeed;
	}

	/**
	 * Try to move this {@link Entity} one cell towards the specified
	 * {@link Direction}.
	 * 
	 * @param dir
	 *            {@link Direction} to move to
	 * @return <code>true</code> if the movement was successful,
	 *         <code>false</code> otherwise (e.g. when collision happened).
	 */
	public boolean move(Direction dir) {
		if (dir != facing) {
			animationTimer = 0;
			facing = dir;
		}

		switch (dir) {
		case UP:
			movement = new Point(0, 1);
			break;
		case RIGHT:
			movement = new Point(1, 0);
			break;
		case DOWN:
			movement = new Point(0, -1);
			break;
		case LEFT:
			movement = new Point(-1, 0);
			break;
		}

		if (!world.checkCollision(x + movement.x, y + movement.y)) {
			x += movement.x;
			y += movement.y;

			state = State.MOVING;
			timer = 0;

			return true;
		} else
			return false;
	}

	/**
	 * Starts an interaction with the specified {@link GameObject}.
	 * 
	 * @param obj
	 *            the {@link GameObject} to interact with
	 * @return <code>true</code>, if the interaction did happen,
	 *         <code>false</code> otherwise.
	 */
	public boolean interact(GameObject obj) {
		return obj.onInteract(this);
	}

	/**
	 * @return <code>true</code>, if this {@link Entity} is the player character
	 */
	public abstract boolean isPlayer();

	/**
	 * Starts an interaction with the {@link GameObject}, this {@link Entity} is
	 * currently facing.
	 * 
	 * @return <code>true</code>, if the interaction did happen,
	 *         <code>false</code> otherwise.
	 */
	public boolean interactWithFacing() {
		Point facingCell = getCellPosition();
		switch (facing) {
		case UP:
			facingCell.y += 1;
			break;
		case DOWN:
			facingCell.y -= 1;
			break;
		case RIGHT:
			facingCell.x += 1;
			break;
		case LEFT:
			facingCell.x -= 1;
			break;
		}

		for (Entity e : world.getEntityList()) {
			if (e.getCellPosition().equals(facingCell))
				return interact(e);
		}
		for (GameObject o : world.getGameObjectList()) {
			if (o.getCellPosition().equals(facingCell))
				return interact(o);
		}
		return false;
	}

	public void updateSpriteRegion() {

		int dirMap;

		switch (facing) {
		case DOWN:
			dirMap = 2;
			break;
		case LEFT:
			dirMap = 3;
			break;
		case UP:
			dirMap = 0;
			break;
		case RIGHT:
			dirMap = 1;
			break;
		default:
			dirMap = 2;
		}

		switch (state) {
		case IDLE:
		case WAITING:
			sprt.setRegion(idleFrames[dirMap]);
			break;
		case MOVING:
			sprt.setRegion(moveAnimations[dirMap].getKeyFrame(animationTimer));
			break;
		case PULLING:
			sprt.setRegion(pullAnimations[dirMap].getKeyFrame(animationTimer));
			break;
		}
	}

	public void reset() {
		sprt.setPosition(getPixelPosition().x, getPixelPosition().y + 5);
		state = State.IDLE;
		movement = new Point(0, 0);
		facing = Direction.DOWN;
		animationTimer = 0;
		timer = 0;
	}

	public void waitFor(float seconds) {
		movement = new Point(0, 0);
		state = State.WAITING;
		waitTime = seconds;
	}

	public void pull(boolean isOdd) {
		if (pulledFruit == null)
			return;
		else if (state == State.PULLING) {
			if (pullingOdd == isOdd) {
				timer = 0f;
				pullingOdd = !pullingOdd;
			}
		}
	}
	
	public void pull(Fruit fruit) {
		if (state == State.IDLE) {
			pulledFruit = fruit;
			state = State.PULLING;
			timer = 0f;
			pullTimer = 0f;
			Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/squeeze" + (int)(Math.random() * 2) + ".wav"));
			long id = sound.play();
			sound.setPitch(id, 1f + ((float) Math.random() * 0.2f) - 0.1f);
		}
		
	}

	public void update(float deltaTime) {

		if (comGen != null) {
			onceCommand = comGen.updateOnceCommand(deltaTime);
			contCommand = comGen.updateContCommand(deltaTime);
		}

		if (state == State.IDLE) {
			if (onceCommand != null)
				onceCommand.execute(this);
			if (contCommand != null)
				contCommand.execute(this);
		}

		updateSpriteRegion();

		switch (state) {
		case MOVING:
			animationTimer += deltaTime;

			float movedDistance = deltaTime * Constants.MOVESPEEDMOD * moveSpeed;

			float stepx = getWorld().getTileWidth() % movedDistance;
			float stepy = getWorld().getTileHeight() % movedDistance;

			sprt.translate(movement.x * getWorld().getTileWidth() * movedDistance - (movement.x * stepx),
					movement.y * getWorld().getTileHeight() * movedDistance - (movement.y * stepy));
			if ((timer += movedDistance) >= 1) {
				timer = 0;
				state = State.IDLE;
				sprt.setPosition(getPixelPosition().x, getPixelPosition().y + 5);
			}
			break;

		case IDLE:
			sprt.setPosition(getPixelPosition().x, getPixelPosition().y + 5);
			break;

		case WAITING:
			sprt.setPosition(getPixelPosition().x, getPixelPosition().y + 5);
			if ((timer += deltaTime) >= waitTime) {
				state = State.IDLE;
			}
			break;

		case PULLING:
			animationTimer += deltaTime;
			pullTimer += deltaTime;
			if ((timer += deltaTime) >= 0.2f) {
				state = State.IDLE;
				pullTimer = 0f;
			} else if (pullTimer >= 1f) {
				pulledFruit.removeFruit();
				pulledFruit = null;
				state = State.IDLE;
			}
		}

	}

	public void draw(SpriteBatch spriteBatch) {

		spriteBatch.begin();
		sprt.draw(spriteBatch);
		spriteBatch.end();
	}

}
