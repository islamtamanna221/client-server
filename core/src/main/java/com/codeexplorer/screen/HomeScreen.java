

package com.codeexplorer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.codeexplorer.Main;

public class HomeScreen extends ScreenAdapter {

    private final Main game;

    // =========================
    // TEXTURES
    // =========================

    private Texture background;
    private Texture loginButton;
    private Texture signupButton;

    // =========================
    // BUTTON AREA
    // =========================

    private Rectangle loginRect;
    private Rectangle signupRect;

    // =========================
    // CONSTRUCTOR
    // =========================

    public HomeScreen(Main game) {

        this.game = game;

        // LOAD TEXTURES

        background =
            new Texture(
                "ui/login_ui.jpg"
            );

        loginButton =
            new Texture(
                "ui/login_button.png"
            );

        signupButton =
            new Texture(
                "ui/signup_button.png"
            );

        // =========================
        // BUTTON POSITION
        // =========================

        // SIGNUP BUTTON (UPPER)

        signupRect =
            new Rectangle(
                500,
                320,
                250,
                100
            );

        // LOGIN BUTTON (LOWER)

        loginRect =
            new Rectangle(
                500,
                170,
                250,
                100
            );
    }

    // =========================
    // RENDER
    // =========================

    @Override
    public void render(float delta) {

        handleInput();

        Gdx.gl.glClearColor(
            0,
            0,
            0,
            1
        );

        Gdx.gl.glClear(
            GL20.GL_COLOR_BUFFER_BIT
        );

        game.batch.begin();

        // =========================
        // BACKGROUND
        // =========================

        game.batch.draw(
            background,
            0,
            0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        // =========================
        // TITLE
        // =========================

        game.font.draw(
            game.batch,
            "POKEMON AOOP",
            520,
            700
        );

        game.font.draw(
            game.batch,
            "WELCOME TRAINER",
            500,
            640
        );

        // =========================
        // SIGNUP BUTTON
        // =========================

        game.batch.draw(
            signupButton,
            500,
            320,
            250,
            100
        );

        // =========================
        // LOGIN BUTTON
        // =========================

        game.batch.draw(
            loginButton,
            500,
            170,
            250,
            100
        );

        // =========================
        // HELP TEXT
        // =========================

        game.font.draw(
            game.batch,
            "CLICK SIGNUP TO CREATE ACCOUNT",
            430,
            140
        );

        game.font.draw(
            game.batch,
            "CLICK LOGIN TO ENTER GAME",
            450,
            100
        );

        game.batch.end();
    }

    // =========================
    // INPUT
    // =========================

    private void handleInput() {

        if (

            Gdx.input.isButtonJustPressed(
                Input.Buttons.LEFT
            )

        ) {

            float x =
                Gdx.input.getX();

            float y =
                Gdx.graphics.getHeight()
                    -
                    Gdx.input.getY();

            // =========================
            // SIGNUP BUTTON CLICK
            // =========================

            if (

                signupRect.contains(
                    x,
                    y
                )

            ) {

                game.setScreen(

                    new SignUpScreen(
                        game
                    )
                );
            }

            // =========================
            // LOGIN BUTTON CLICK
            // =========================

            if (

                loginRect.contains(
                    x,
                    y
                )

            ) {

                game.setScreen(

                    new LoginScreen(
                        game
                    )
                );
            }
        }
    }

    // =========================
    // DISPOSE
    // =========================

    @Override
    public void dispose() {

        background.dispose();
        signupButton.dispose();
        loginButton.dispose();
    }
}
//package com.codeexplorer.screen;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.ScreenAdapter;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.math.Rectangle;
//import com.codeexplorer.Main;
//
//public class HomeScreen extends ScreenAdapter {
//
//    private final Main game;
//
//    // =========================
//    // TEXTURES
//    // =========================
//
//    private Texture background;
//    private Texture startButton;
//
//    // =========================
//    // BUTTON AREA
//    // =========================
//
//    private Rectangle startRect;
//
//    // =========================
//    // CONSTRUCTOR
//    // =========================
//
//    public HomeScreen(Main game) {
//
//        this.game = game;
//
//        // BACKGROUND
//
//        background =
//            new Texture(
//                "ui/login_ui.jpg"
//            );
//
//        // START BUTTON
//
//        startButton =
//            new Texture(
//                "ui/start_button.png"
//            );
//
//        // BUTTON POSITION
//
//        startRect =
//            new Rectangle(
//                500,
//                250,
//                280,
//                120
//            );
//    }
//
//    // =========================
//    // RENDER
//    // =========================
//
//    @Override
//    public void render(float delta) {
//
//        handleInput();
//
//        Gdx.gl.glClearColor(
//            0,
//            0,
//            0,
//            1
//        );
//
//        Gdx.gl.glClear(
//            GL20.GL_COLOR_BUFFER_BIT
//        );
//
//        game.batch.begin();
//
//        // =========================
//        // BACKGROUND
//        // =========================
//
//        game.batch.draw(
//            background,
//            0,
//            0,
//            Gdx.graphics.getWidth(),
//            Gdx.graphics.getHeight()
//        );
//
//        // =========================
//        // TITLE
//        // =========================
//
//        game.font.draw(
//            game.batch,
//            "POKEMON AOOP",
//            500,
//            700
//        );
//
//        game.font.draw(
//            game.batch,
//            "WELCOME TRAINER",
//            480,
//            650
//        );
//
//        // =========================
//        // START BUTTON
//        // =========================
//
//        game.batch.draw(
//            startButton,
//            500,
//            250,
//            280,
//            120
//        );
//
//        game.font.draw(
//            game.batch,
//            "CLICK START TO PLAY",
//            500,
//            200
//        );
//
//        game.batch.end();
//    }
//
//    // =========================
//    // INPUT
//    // =========================
//
//    private void handleInput() {
//
//        if (
//
//            Gdx.input.isButtonJustPressed(
//                Input.Buttons.LEFT
//            )
//
//        ) {
//
//            float x =
//                Gdx.input.getX();
//
//            float y =
//                Gdx.graphics.getHeight()
//                    -
//                    Gdx.input.getY();
//
//            // =========================
//            // START BUTTON CLICK
//            // =========================
//
//            if (
//
//                startRect.contains(
//                    x,
//                    y
//                )
//
//            ) {
//
//                game.setScreen(
//
//                    new MenuScreen(
//                        game
//                    )
//                );
//            }
//        }
//    }
//
//    // =========================
//    // DISPOSE
//    // =========================
//
//    @Override
//    public void dispose() {
//
//        background.dispose();
//        startButton.dispose();
//    }
//}
