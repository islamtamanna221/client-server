
package com.codeexplorer.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import com.codeexplorer.world.CollisionManager;

public class Player {

    // =========================
    // SPRITES
    // =========================

    private final Texture[] up;
    private final Texture[] down;
    private final Texture[] left;
    private final Texture[] right;

    // =========================
    // CURRENT FRAME
    // =========================

    private Texture currentFrame;

    // =========================
    // POSITION
    // =========================

    private float x;
    private float y;

    // =========================
    // SPEED
    // =========================

    private final float speed = 200;

    // =========================
    // ANIMATION
    // =========================

    private float animationTimer = 0;

    private int frame = 0;

    // =========================
    // HUGE MAP LIMIT
    // =========================

    private final float MIN_X = -5000;
    private final float MAX_X = 5000;

    private final float MIN_Y = -5000;
    private final float MAX_Y = 5000;

    // =========================
    // COLLISION
    // =========================

    private final Rectangle bounds;

    private final CollisionManager collisionManager;

    // =========================
    // CONSTRUCTOR
    // =========================

    public Player(
        CollisionManager collisionManager
    ) {

        this.collisionManager =
            collisionManager;

        // =========================
        // UP
        // =========================

        up = new Texture[] {

            new Texture(
                "tiles/player/sprite_0010.png"
            ),

            new Texture(
                "tiles/player/sprite_0011.png"
            ),

            new Texture(
                "tiles/player/sprite_0013.png"
            ),

            new Texture(
                "tiles/player/sprite_0014.png"
            )
        };

        // =========================
        // DOWN
        // =========================

        down = new Texture[] {

            new Texture(
                "tiles/player/sprite_0000.png"
            ),

            new Texture(
                "tiles/player/sprite_0001.png"
            ),

            new Texture(
                "tiles/player/sprite_0003.png"
            ),

            new Texture(
                "tiles/player/sprite_0004.png"
            )
        };

        // =========================
        // LEFT
        // =========================

        left = new Texture[] {

            new Texture(
                "tiles/player/sprite_0005.png"
            ),

            new Texture(
                "tiles/player/sprite_0006.png"
            ),

            new Texture(
                "tiles/player/sprite_0002.png"
            )
        };

        // =========================
        // RIGHT
        // =========================

        right = new Texture[] {

            new Texture(
                "tiles/player/sprite_0007.png"
            ),

            new Texture(
                "tiles/player/sprite_0008.png"
            ),

            new Texture(
                "tiles/player/sprite_0012.png"
            ),

            new Texture(
                "tiles/player/sprite_0015.png"
            )
        };

        // =========================
        // START FRAME
        // =========================

        currentFrame = down[0];

        // =========================
        // START POSITION
        // =========================

        x = 500;
        y = 300;

        // =========================
        // HITBOX
        // =========================

        bounds = new Rectangle(
            x,
            y,
            40,
            40
        );
    }

    // =========================
    // UPDATE
    // =========================

    public void update(float delta) {

        boolean moving = false;

        float oldX = x;
        float oldY = y;

        // =========================
        // UP
        // =========================

        if (

            Gdx.input.isKeyPressed(
                Input.Keys.UP
            )

                ||

                Gdx.input.isKeyPressed(
                    Input.Keys.W
                )

        ) {

            y += speed * delta;

            animate(up, delta);

            moving = true;
        }

        // =========================
        // DOWN
        // =========================

        if (

            Gdx.input.isKeyPressed(
                Input.Keys.DOWN
            )

                ||

                Gdx.input.isKeyPressed(
                    Input.Keys.S
                )

        ) {

            y -= speed * delta;

            animate(down, delta);

            moving = true;
        }

        // =========================
        // LEFT
        // =========================

        if (

            Gdx.input.isKeyPressed(
                Input.Keys.LEFT
            )

                ||

                Gdx.input.isKeyPressed(
                    Input.Keys.A
                )

        ) {

            x -= speed * delta;

            animate(left, delta);

            moving = true;
        }

        // =========================
        // RIGHT
        // =========================

        if (

            Gdx.input.isKeyPressed(
                Input.Keys.RIGHT
            )

                ||

                Gdx.input.isKeyPressed(
                    Input.Keys.D
                )

        ) {

            x += speed * delta;

            animate(right, delta);

            moving = true;
        }

        // =========================
        // UPDATE HITBOX
        // =========================

        bounds.setPosition(x, y);

        // =========================
        // COLLISION
        // =========================

        if (

            collisionManager.isColliding(
                bounds
            )

        ) {

            x = oldX;
            y = oldY;

            bounds.setPosition(
                x,
                y
            );
        }

        // =========================
        // HUGE MAP LIMIT
        // =========================

        if (x < MIN_X) {

            x = MIN_X;
        }

        if (x > MAX_X) {

            x = MAX_X;
        }

        if (y < MIN_Y) {

            y = MIN_Y;
        }

        if (y > MAX_Y) {

            y = MAX_Y;
        }

        // =========================
        // IDLE
        // =========================

        if (!moving) {

            animationTimer = 0;

            frame = 0;
        }
    }

    // =========================
    // ANIMATION
    // =========================

    private void animate(
        Texture[] direction,
        float delta
    ) {

        animationTimer += delta;

        if (animationTimer >= 0.15f) {

            frame++;

            if (frame >= direction.length) {

                frame = 0;
            }

            currentFrame =
                direction[frame];

            animationTimer = 0;
        }
    }

    // =========================
    // DRAW
    // =========================

    public void draw(
        SpriteBatch batch
    ) {

        batch.draw(

            currentFrame,

            x,

            y,

            48,

            48
        );
    }

    // =========================
    // GETTERS
    // =========================

    public float getX() {

        return x;
    }

    public float getY() {

        return y;
    }

    public Rectangle getBounds() {

        return bounds;
    }

    // =========================
    // DISPOSE
    // =========================

    public void dispose() {

        for (Texture texture : up) {

            texture.dispose();
        }

        for (Texture texture : down) {

            texture.dispose();
        }

        for (Texture texture : left) {

            texture.dispose();
        }

        for (Texture texture : right) {

            texture.dispose();
        }
    }
}
