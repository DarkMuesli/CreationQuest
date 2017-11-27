package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Constants;
import com.mygdx.game.MyGdxGame;


public class DesktopLauncher {
    public static void main(String[] arg) {


        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = Constants.APPLICATION_NAME;

        if (Constants.FULLSCREEN)
            config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
        else {
            config.height = Constants.VIEWPORT_HEIGHT * 2;
            config.width = Constants.VIEWPORT_WIDTH * 2;
            config.x = 100;
            config.y = 100;
        }

        new LwjglApplication(new MyGdxGame(), config);
    }
}
