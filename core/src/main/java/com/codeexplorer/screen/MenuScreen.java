package com.codeexplorer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.codeexplorer.Main;

public class MenuScreen extends ScreenAdapter {

    private final Main game;

    // =========================
    // MAPS
    // =========================

    private Texture[] maps;

    private int currentMap = 0;

    // =========================
    // PLAYER
    // =========================

    private Texture[] playerFrames;

    private int currentFrame = 0;

    private float animationTimer = 0;

    // =========================
    // CAMERA
    // =========================

    private OrthographicCamera camera;

    // =========================
    // PLAYER POSITION
    // =========================

    private float playerX;
    private float playerY;

    // =========================
    // PLAYER RECTANGLE
    // =========================

    private Rectangle playerRect;

    // =========================
    // COLLISION
    // =========================

    private Rectangle leftWall;
    private Rectangle topWall;
    private Rectangle bottomWall;
    private Rectangle rightWall;

    // =========================
    // SPEED
    // =========================

    private final float speed = 220;

    // =========================
    // WORLD SIZE
    // =========================

    private final float worldWidth = 3000;

    private final float worldHeight = 3000;

    // =========================
    // CONSTRUCTOR
    // =========================

    public MenuScreen(Main game) {

        this.game = game;

        // =========================
        // LOAD MAPS
        // =========================

        maps = new Texture[37];

        for(int i = 0; i <= 36; i++) {

            String number =
                String.format(
                    "%04d",
                    i
                );

            maps[i] =
                new Texture(
                    "maps/sprite_" +
                        number +
                        ".png"
                );
        }

        // =========================
        // LOAD PLAYER
        // =========================

        playerFrames = new Texture[16];

        for(int i = 0; i < 16; i++) {

            String number =
                String.format(
                    "%04d",
                    i
                );

            playerFrames[i] =
                new Texture(
                    "tiles/player/sprite_" +
                        number +
                        ".png"
                );
        }

        // =========================
        // CAMERA
        // =========================

        camera =
            new OrthographicCamera();

        camera.setToOrtho(
            false,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        // =========================
        // PLAYER START
        // =========================

        playerX = 1000;
        playerY = 1000;

        playerRect =
            new Rectangle(
                playerX,
                playerY,
                48,
                48
            );

        // =========================
        // WORLD COLLISION
        // =========================

        leftWall =
            new Rectangle(
                -100,
                0,
                100,
                worldHeight
            );

        rightWall =
            new Rectangle(
                worldWidth,
                0,
                100,
                worldHeight
            );

        topWall =
            new Rectangle(
                0,
                worldHeight,
                worldWidth,
                100
            );

        bottomWall =
            new Rectangle(
                0,
                -100,
                worldWidth,
                100
            );
    }

    // =========================
    // RENDER
    // =========================

    @Override
    public void render(float delta) {

        handleInput(delta);

        // =========================
        // UPDATE PLAYER RECT
        // =========================

        playerRect.setPosition(
            playerX,
            playerY
        );

        // =========================
        // CAMERA FOLLOW
        // =========================

        camera.position.set(
            playerX,
            playerY,
            0
        );

        camera.update();

        game.batch.setProjectionMatrix(
            camera.combined
        );

        // =========================
        // CLEAR SCREEN
        // =========================

        Gdx.gl.glClearColor(
            0,
            0,
            0,
            1
        );

        Gdx.gl.glClear(
            GL20.GL_COLOR_BUFFER_BIT
        );

        // =========================
        // DRAW
        // =========================

        game.batch.begin();

        // MAP

        game.batch.draw(
            maps[currentMap],
            0,
            0,
            worldWidth,
            worldHeight
        );

        // PLAYER

        game.batch.draw(
            playerFrames[currentFrame],
            playerX,
            playerY,
            64,
            64
        );

        game.batch.end();
    }

    // =========================
    // INPUT
    // =========================

    private void handleInput(float delta) {

        float oldX = playerX;

        float oldY = playerY;

        boolean moving = false;

        // LEFT

        if (

            Gdx.input.isKeyPressed(
                Input.Keys.LEFT
            )

        ) {

            playerX -= speed * delta;

            moving = true;
        }

        // RIGHT

        if (

            Gdx.input.isKeyPressed(
                Input.Keys.RIGHT
            )

        ) {

            playerX += speed * delta;

            moving = true;
        }

        // UP

        if (

            Gdx.input.isKeyPressed(
                Input.Keys.UP
            )

        ) {

            playerY += speed * delta;

            moving = true;
        }

        // DOWN

        if (

            Gdx.input.isKeyPressed(
                Input.Keys.DOWN
            )

        ) {

            playerY -= speed * delta;

            moving = true;
        }

        // =========================
        // UPDATE RECTANGLE
        // =========================

        playerRect.setPosition(
            playerX,
            playerY
        );

        // =========================
        // COLLISION
        // =========================

        if (

            playerRect.overlaps(
                leftWall
            )

                ||

                playerRect.overlaps(
                    rightWall
                )

                ||

                playerRect.overlaps(
                    topWall
                )

                ||

                playerRect.overlaps(
                    bottomWall
                )

        ) {

            playerX = oldX;
            playerY = oldY;
        }

        // =========================
        // PLAYER ANIMATION
        // =========================

        if(moving) {

            animationTimer += delta;

            if(animationTimer > 0.08f) {

                currentFrame++;

                if(currentFrame >= 16) {

                    currentFrame = 0;
                }

                animationTimer = 0;
            }
        }

        // =========================
        // MAP CHANGE
        // =========================

        if(playerX > worldWidth - 80) {

            currentMap++;

            if(currentMap > 36) {

                currentMap = 0;
            }

            playerX = 100;
        }

        if(playerX < 0) {

            currentMap--;

            if(currentMap < 0) {

                currentMap = 36;
            }

            playerX = worldWidth - 120;
        }
    }

    // =========================
    // DISPOSE
    // =========================

    @Override
    public void dispose() {

        for(Texture texture : maps) {

            texture.dispose();
        }

        for(Texture texture : playerFrames) {

            texture.dispose();
        }
    }
}
