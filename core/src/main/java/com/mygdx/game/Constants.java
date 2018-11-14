package com.mygdx.game;

import com.badlogic.gdx.Application;

/**
 * All relevant constants with global visibility should be defined here.
 *
 * @author Matthias Gross
 */
public final class Constants {

    public static final int LOG_LVL = Application.LOG_DEBUG;

    public static final int FRAMERATE = 60;
    public static final float MS_PER_UPDATE = 1f / FRAMERATE;

    public static final int VIEWPORT_HEIGHT = 320;
    public static final int VIEWPORT_WIDTH = 600;
    public static final boolean FULLSCREEN = true;

    public static final String APPLICATION_NAME = "CreationQuest";

    public static final float MOVESPEEDMOD = 5f;

    private Constants() {
        throw new AssertionError();
    }
}
