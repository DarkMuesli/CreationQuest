package com.mygdx.game.slotmachine;

import java.util.LinkedList;

class Reel {

	private enum State {
		MOVING, STARTING, STOPPING, STOPPED, LOCKED, ALIGNING, TILTED_UP
	}

	final int ELEMENTS;

	private final float MAX_SPEED;
	private static final float LERP_FRACTION = 0.05f;
	private static final float TILT_POS = 0.6f;

	private String[] wordPool;
	private LinkedList<String> words;
	private float vertPosition;
	private float speed;
	private State state;

	Reel(String[] wordPool, int elementCount, float maxSpeed) {

		if (wordPool.length < elementCount)
			throw new IllegalArgumentException("Array must be equal to or larger than Element count (defaults to 3).");
		if (maxSpeed <= 0f)
			throw new IllegalArgumentException("MaxSpeed must be larger than 0.");

		this.MAX_SPEED = maxSpeed * elementCount;
		this.ELEMENTS = elementCount;
		this.wordPool = wordPool;
		this.words = new LinkedList<>();

		for (int i = 0; i < ELEMENTS; i++) {
			String newWord;

			do
				newWord = wordPool[(int) (Math.random() * wordPool.length)].trim();
			while (words.contains(newWord) || newWord.isEmpty());

			words.addFirst(newWord);
		}

		this.vertPosition = 0.5f;
		this.speed = 0;
		this.state = State.STOPPED;

	}

	Reel(String[] wordPool) {
		this(wordPool, 3, 2);
	}

	Reel(String[] wordPool, int elementCount) {
		this(wordPool, elementCount, 2);
	}

	Reel(String[] wordPool, float maxSpeed) {
		this(wordPool, 3, maxSpeed);
	}

	float getVertPosition() {
		return vertPosition;
	}

	private void setVertPosition(float vertPosition) {
		if (vertPosition >= 1f)
			this.vertPosition = vertPosition - 1f;
		else if (vertPosition < 0f) {
			this.vertPosition = vertPosition + 1;
		} else
			this.vertPosition = vertPosition;
	}

	LinkedList<String> getWords() {
		return words;
	}

	String getCurrentWord() {
		return words.get(ELEMENTS / 2);
	}

	void update(float deltaTime) {
		switch (state) {

		case MOVING:
			updatePositionAndWords(deltaTime);
			break;

		case STARTING:
			speed = lerp(speed, MAX_SPEED, LERP_FRACTION);
			updatePositionAndWords(deltaTime);
			if (speed >= MAX_SPEED - 0.1f) {
				speed = MAX_SPEED;
				state = State.MOVING;
			}
			break;

		case STOPPING:
			if (vertPosition < 0.5f && vertPosition > 0.4f)
				speed = lerp(speed, 0, LERP_FRACTION * 8f);
			updatePositionAndWords(deltaTime);
			if (speed <= MAX_SPEED * 0.1f) {
				speed = 0f;
				state = State.ALIGNING;
			}

			break;

		case ALIGNING:
			vertPosition = lerp(vertPosition, 0.5f, LERP_FRACTION * 5f);
			if (Math.abs(0.5f - vertPosition) <= 0.01f) {
				vertPosition = 0.5f;
				speed = 0;
				state = State.STOPPED;
			}
			break;

		case TILTED_UP:
			vertPosition = lerp(vertPosition, TILT_POS, LERP_FRACTION * 5);

		case LOCKED:
		case STOPPED:

		}
	}

	private void updatePositionAndWords(float deltaTime) {
		float oldPos = vertPosition;
		setVertPosition(vertPosition - deltaTime * speed);
		float newPos = vertPosition;
		if (newPos > oldPos) {
			words.removeFirst();

			String newWord;
			do
				newWord = wordPool[(int) (Math.random() * wordPool.length)].trim();
			while (words.contains(newWord) || newWord.isEmpty());

			words.addLast(newWord);
		}

	}

	void tilt() {
		if (state == State.STOPPED)
			state = State.TILTED_UP;
	}

	void start() {
		if (state == State.TILTED_UP)
			state = State.STARTING;
	}

	void stop() {
		if (state == State.MOVING)
			state = State.STOPPING;
		else if (state == State.TILTED_UP) {
			state = State.ALIGNING;
		}
	}

	void toggleLock() {
		if (state == State.LOCKED)
			state = State.STOPPED;
		else if (state == State.STOPPED)
			state = State.LOCKED;
	}

	boolean isMoving() {
		return state == State.MOVING;
	}

	boolean isStopped() {
		return (state == State.STOPPED) || (state == State.LOCKED);
	}

	boolean isLocked() {
		return state == State.LOCKED;
	}

	private float lerp(float a, float b, float f) {
		return a + f * (b - a);
	}
}
