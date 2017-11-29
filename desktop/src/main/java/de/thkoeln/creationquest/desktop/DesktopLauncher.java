package de.thkoeln.creationquest.desktop;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import de.thkoeln.creationquest.Constants;
import de.thkoeln.creationquest.CreationQuestGame;


public class DesktopLauncher {
    public static void main(String[] arg) {

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle(Constants.APPLICATION_NAME);

        Monitor monitors[] = Lwjgl3ApplicationConfiguration.getMonitors();
        DisplayMode mode;

        if (monitors.length >= 3)
            mode = Lwjgl3ApplicationConfiguration.getDisplayMode(monitors[2]);
        else
            mode = Lwjgl3ApplicationConfiguration.getDisplayMode();

        if (Constants.FULLSCREEN)
            config.setFullscreenMode(mode);
        else {
            config.setWindowedMode(Constants.VIEWPORT_WIDTH * 2, Constants.VIEWPORT_HEIGHT * 2);
            config.setWindowPosition(100, 100);
        }

        new Lwjgl3Application(new CreationQuestGame(), config);
    }
}
