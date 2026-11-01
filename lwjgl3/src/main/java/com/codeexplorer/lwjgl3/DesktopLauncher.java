
package com.codeexplorer.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import com.codeexplorer.Main;

public class DesktopLauncher {

    public static void main(
        String[] args
    ) {

        // =====================================
        // CONFIG
        // =====================================

        Lwjgl3ApplicationConfiguration config =

            new Lwjgl3ApplicationConfiguration();

        // =====================================
        // TITLEt   1
        // =====================================

        config.setTitle(
            "Pokemon AOOP"
        );

        // =====================================
        // WINDOW SIZE
        // =====================================

        config.setWindowedMode(

            1366,

            768
        );

        // =====================================
        // =====================================
        // RESIZABLE

        config.setResizable(
            true
        );

        // =====================================
        // FPS
        // =====================================

        config.setForegroundFPS(
            60
        );

        // =====================================
        // VSYNC
        // =====================================

        config.useVsync(
            true
        );

        // =====================================
        // WINDOW ICON
        // =====================================

        config.setWindowIcon(
            "libgdx.png"
        );

        // =====================================
        // AUDIO
        // =====================================

        config.disableAudio(
            false
        );

        // =====================================
        // MAXIMIZED
        // =====================================

        config.setMaximized(
            false
        );

        // =====================================
        // START GAME
        // =====================================

        new Lwjgl3Application(

            new Main(),

            config
        );
    }
}
