package com.codeexplorer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import com.codeexplorer.Main;

public class SecondScreen implements Screen {

    private final Main game;

    // =====================================
    // SPRITES
    // =====================================

    private Texture house;

    private Texture ground;

    private Texture tree;

    // =====================================
    // PLAYER
    // =====================================

    private float playerX = 700;

    private float playerY = 500;

    public SecondScreen(Main game) {

        this.game = game;

        // =====================================
        // LOAD SPRITES
        // =====================================

        house =
            new Texture(
                "second/sprite_0000.png"
            );

        ground =
            new Texture(
                "second/sprite_0001.png"
            );

        tree =
            new Texture(
                "second/sprite_0002.png"
            );
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(
            0,
            0,
            0,
            1
        );

        Gdx.gl.glClear(
            GL20.GL_COLOR_BUFFER_BIT
        );

        // =====================================
        // CAMERA
        // =====================================

        game.camera.position.set(

            playerX,

            playerY,

            0
        );

        game.camera.update();

        game.batch.setProjectionMatrix(
            game.camera.combined
        );

        game.batch.begin();

        // =====================================
        // DRAW GROUND
        // =====================================

        for (
            int x = -3000;
            x < 3000;
            x += 64
        ) {

            for (
                int y = -3000;
                y < 3000;
                y += 64
            ) {

                game.batch.draw(

                    ground,

                    x,

                    y,

                    64,

                    64
                );
            }
        }

        // =====================================
        // LEFT HOUSE
        // =====================================

        game.batch.draw(

            house,

            300,

            700,

            220,

            220
        );

        // =====================================
        // RIGHT HOUSE
        // =====================================

        game.batch.draw(

            house,

            1100,

            700,

            220,

            220
        );

        // =====================================
        // CENTER TREE
        // =====================================

        game.batch.draw(

            tree,

            850,

            720,

            120,

            120
        );

        // =====================================
        // LEFT TREE
        // =====================================

        game.batch.draw(

            tree,

            650,

            500,

            120,

            120
        );

        game.font.draw(

            game.batch,

            "SECOND SCREEN",

            700,

            1000
        );

        game.batch.end();
    }

    @Override
    public void show() {}

    @Override
    public void resize(
        int width,
        int height
    ) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {

        house.dispose();

        ground.dispose();

        tree.dispose();
    }
}
