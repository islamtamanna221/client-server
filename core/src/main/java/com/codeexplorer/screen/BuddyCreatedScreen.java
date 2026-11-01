//package com.codeexplorer.screen;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.Screen;
//
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//
//import com.badlogic.gdx.graphics.g2d.Animation;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//
//import com.badlogic.gdx.utils.Array;
//
//import com.codeexplorer.Main;
//
//public class BuddyCreatedScreen
//    implements Screen {
//
//    // =====================================
//    // MAIN GAME
//    // =====================================
//
//    private final Main game;
//
//    // =====================================
//    // BACKGROUND
//    // =====================================
//
//    private final Texture bg;
//
//    // =====================================
//    // TITLE IMAGE
//    // =====================================
//
//    private final Texture titleImage;
//
//    // =====================================
//    // BUDDY ANIMATION
//    // =====================================
//
//    private Animation<TextureRegion>
//        buddyAnimation;
//
//    // =====================================
//    // SPARK EFFECT
//    // =====================================
//
//    private Animation<TextureRegion>
//        sparkAnimation;
//
//    // =====================================
//    // TIME
//    // =====================================
//
//    private float stateTime = 0;
//
//    public BuddyCreatedScreen(
//
//        Main game,
//
//        String type
//
//    ) {
//
//        this.game = game;
//
//        // =====================================
//        // BACKGROUND
//        // =====================================
//
//        bg = new Texture(
//            "battle/background/battle-ground.jpg"
//        );
//
//        // =====================================
//        // TITLE IMAGE
//        // =====================================
//
//        titleImage = new Texture(
//            "ui/buddy_created.png"
//        );
//
//        // =====================================
//        // LOAD BUDDY SPRITES
//        // =====================================
//
//        Array<TextureRegion> buddyFrames =
//            new Array<>();
//
//        // =====================================
//        // PIKACHU
//        // =====================================
//
//        if (type.equals("pikachu")) {
//
//            for (int i = 0; i < 30; i++) {
//
//                String number =
//                    String.format(
//                        "%04d",
//                        i
//                    );
//
//                Texture texture =
//                    new Texture(
//
//                        "pikachu/sprite_" +
//
//                            number +
//
//                            ".png"
//                    );
//
//                buddyFrames.add(
//                    new TextureRegion(
//                        texture
//                    )
//                );
//            }
//        }
//
//        // =====================================
//        // DRAGON
//        // =====================================
//
//        else {
//
//            for (int i = 0; i < 15; i++) {
//
//                String number =
//                    String.format(
//                        "%04d",
//                        i
//                    );
//
//                Texture texture =
//                    new Texture(
//
//                        "dragon/sprite_" +
//
//                            number +
//
//                            ".png"
//                    );
//
//                buddyFrames.add(
//                    new TextureRegion(
//                        texture
//                    )
//                );
//            }
//        }
//
//        // =====================================
//        // CREATE BUDDY ANIMATION
//        // =====================================
//
//        buddyAnimation =
//            new Animation<>(
//
//                0.08f,
//
//                buddyFrames,
//
//                Animation.PlayMode.LOOP
//
//            );
//
//        // =====================================
//        // LOAD SPARK EFFECT
//        // =====================================
//
//        Array<TextureRegion> sparkFrames =
//            new Array<>();
//
//        for (int i = 0; i <= 8; i++) {
//
//            String number =
//                String.format(
//                    "%04d",
//                    i
//                );
//
//            Texture texture =
//                new Texture(
//
//                    "effect/sprite_" +
//
//                        number +
//
//                        ".png"
//                );
//
//            sparkFrames.add(
//                new TextureRegion(
//                    texture
//                )
//            );
//        }
//
//        // =====================================
//        // CREATE SPARK ANIMATION
//        // =====================================
//
//        sparkAnimation =
//            new Animation<>(
//
//                0.08f,
//
//                sparkFrames,
//
//                Animation.PlayMode.LOOP
//
//            );
//    }
//
//    @Override
//    public void render(float delta) {
//
//        // =====================================
//        // UPDATE TIME
//        // =====================================
//
//        stateTime += delta;
//
//        // =====================================
//        // CLEAR SCREEN
//        // =====================================
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
//        // =====================================
//        // CURRENT FRAMES
//        // =====================================
//
//        TextureRegion buddyFrame =
//
//            buddyAnimation.getKeyFrame(
//                stateTime
//            );
//
//        TextureRegion sparkFrame =
//
//            sparkAnimation.getKeyFrame(
//                stateTime
//            );
//
//        // =====================================
//        // FLOAT EFFECT
//        // =====================================
//
//        float floatY =
//
//            260 +
//
//                (float)Math.sin(
//                    stateTime * 3
//                ) * 15;
//
//        // =====================================
//        // SPARK SIZE EFFECT
//        // =====================================
//
//        float sparkSize =
//
//            250 +
//
//                (float)Math.sin(
//                    stateTime * 5
//                ) * 20;
//
//        // =====================================
//        // START DRAW
//        // =====================================
//
//        game.batch.begin();
//
//        // =====================================
//        // DRAW BACKGROUND
//        // =====================================
//
//        game.batch.draw(
//            bg,
//            0,
//            0,
//            1366,
//            768
//        );
//
//        // =====================================
//        // TITLE IMAGE
//        // =====================================
//
//        game.batch.draw(
//            titleImage,
//            360,
//            600,
//            650,
//            110
//        );
//
//        // =====================================
//        // LEFT SPARK
//        // =====================================
//
//        game.batch.draw(
//            sparkFrame,
//            350,
//            250,
//            170,
//            170
//        );
//
//        // =====================================
//        // RIGHT SPARK
//        // =====================================
//
//        game.batch.draw(
//            sparkFrame,
//            850,
//            250,
//            170,
//            170
//        );
//
//        // =====================================
//        // CENTER BIG SPARK
//        // =====================================
//
//        game.batch.draw(
//            sparkFrame,
//            520,
//            180,
//            sparkSize,
//            sparkSize
//        );
//
//        // =====================================
//        // DRAW BUDDY ANIMATION
//        // =====================================
//
//        game.batch.draw(
//
//            buddyFrame,
//
//            470,
//
//            floatY,
//
//            320,
//
//            320
//        );
//
//        // =====================================
//        // TEXT
//        // =====================================
//
//        game.font.draw(
//            game.batch,
//            "BUDDY CREATED!",
//            450,
//            210
//        );
//
//        game.font.draw(
//            game.batch,
//            "PRESS ENTER TO RETURN",
//            350,
//            150
//        );
//
//        // =====================================
//        // END DRAW
//        // =====================================
//
//        game.batch.end();
//
//        // =====================================
//        // RETURN
//        // =====================================
//
//        if (
//
//            Gdx.input.isKeyJustPressed(
//                Input.Keys.ENTER
//            )
//
//        ) {
//
//            game.setScreen(
//                new GameScreen(game)
//            );
//        }
//    }
//
//    @Override
//    public void show() {}
//
//    @Override
//    public void resize(
//        int width,
//        int height
//    ) {}
//
//    @Override
//    public void pause() {}
//
//    @Override
//    public void resume() {}
//
//    @Override
//    public void hide() {}
//
//    @Override
//    public void dispose() {
//
//        bg.dispose();
//
//        titleImage.dispose();
//    }
//}
//check
package com.codeexplorer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.utils.Array;

import com.codeexplorer.Main;

public class BuddyCreatedScreen
    implements Screen {

    // =====================================
    // MAIN GAME
    // =====================================

    private final Main game;

    // =====================================
    // BACKGROUND
    // =====================================

    private final Texture bg;

    // =====================================
    // TITLE IMAGE
    // =====================================

    private final Texture titleImage;

    // =====================================
    // BUDDY ANIMATION
    // =====================================

    private Animation<TextureRegion>
        buddyAnimation;

    // =====================================
    // SPARK EFFECT
    // =====================================

    private Animation<TextureRegion>
        sparkAnimation;

    // =====================================
    // MUSIC
    // =====================================

    private Music victoryMusic;

    // =====================================
    // TIME
    // =====================================

    private float stateTime = 0;

    public BuddyCreatedScreen(

        Main game,

        String type

    ) {

        this.game = game;

        // =====================================
        // LOAD MUSIC
        // =====================================

        victoryMusic = Gdx.audio.newMusic(

            Gdx.files.internal(
                "music/vicory.mp3"
            )
        );

        victoryMusic.setLooping(true);

        victoryMusic.setVolume(0.7f);

        victoryMusic.play();

        // =====================================
        // BACKGROUND
        // =====================================

        bg = new Texture(
            "battle/background/battle.jpg"
        );

        // =====================================
        // TITLE IMAGE
        // =====================================

        titleImage = new Texture(
            "ui/buddy_created.png"
        );

        // =====================================
        // LOAD BUDDY SPRITES
        // =====================================

        Array<TextureRegion> buddyFrames =
            new Array<>();

        // =====================================
        // PIKACHU
        // =====================================

        if (type.equals("pikachu")) {

            for (int i = 0; i < 30; i++) {

                String number =
                    String.format("%04d", i);

                Texture texture =
                    new Texture(
                        Gdx.files.internal(
                            "pikachu/sprite_" +
                                number +
                                ".png"
                        )
                    );

                buddyFrames.add(
                    new TextureRegion(texture)
                );
            }
        }

        else if (type.equals("charmander")) {

            for (int i = 0; i < 12; i++) {

                String number =
                    String.format("%04d", i);

                Texture texture =
                    new Texture(
                        Gdx.files.internal(
                            "charmander/sprite_" +
                                number +
                                ".png"
                        )
                    );

                buddyFrames.add(
                    new TextureRegion(texture)
                );
            }
        }

        else if (type.equals("greninja")) {

            for (int i = 0; i < 39; i++) {

                String fileName =
                    String.format(
                        "greninja/frame_%02d_delay-0.04s.png",
                        i
                    );

                Texture texture =
                    new Texture(
                        Gdx.files.internal(fileName)
                    );

                buddyFrames.add(
                    new TextureRegion(texture)
                );
            }
        }

        else if (type.equals("bulbasaur")) {

            for (int i = 0; i < 5; i++) {

                String fileName =
                    String.format(
                        "bulbasaur/frame_%d_delay-0.1s.png",
                        i
                    );

                Texture texture =
                    new Texture(
                        Gdx.files.internal(fileName)
                    );

                buddyFrames.add(
                    new TextureRegion(texture)
                );
            }
        }
        // =====================================
        // CREATE BUDDY ANIMATION
        // =====================================

        buddyAnimation =
            new Animation<>(

                0.08f,

                buddyFrames,

                Animation.PlayMode.LOOP

            );

        // =====================================
        // LOAD SPARK EFFECT
        // =====================================

        Array<TextureRegion> sparkFrames =
            new Array<>();

        for (int i = 0; i <= 8; i++) {

            String number =
                String.format(
                    "%04d",
                    i
                );

            Texture texture =
                new Texture(

                    Gdx.files.internal(

                        "effect/sprite_" +

                            number +

                            ".png"
                    )
                );

            sparkFrames.add(
                new TextureRegion(
                    texture
                )
            );
        }

        // =====================================
        // CREATE SPARK ANIMATION
        // =====================================

        sparkAnimation =
            new Animation<>(

                0.08f,

                sparkFrames,

                Animation.PlayMode.LOOP

            );
    }

    @Override
    public void render(float delta) {

        // =====================================
        // UPDATE TIME
        // =====================================

        stateTime += delta;

        // =====================================
        // CLEAR SCREEN
        // =====================================

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
        // CURRENT FRAMES
        // =====================================

        TextureRegion buddyFrame =

            buddyAnimation.getKeyFrame(
                stateTime
            );

        TextureRegion sparkFrame =

            sparkAnimation.getKeyFrame(
                stateTime
            );

        // =====================================
        // FLOAT EFFECT
        // =====================================

        float floatY =

            250 +

                (float)Math.sin(
                    stateTime * 3
                ) * 15;

        // =====================================
        // START DRAW
        // =====================================

        game.batch.begin();

        // =====================================
        // DRAW BACKGROUND
        // =====================================

        game.batch.draw(
            bg,
            0,
            0,
            1366,
            768
        );

        // =====================================
        // TITLE IMAGE
        // =====================================

        game.batch.draw(
            titleImage,
            360,
            600,
            650,
            110
        );

        // =====================================
        // ROUND SPARK EFFECTS
        // =====================================

        // LEFT TOP

        game.batch.draw(
            sparkFrame,
            430,
            250,
            90,
            90
        );

        // RIGHT TOP

        game.batch.draw(
            sparkFrame,
            780,
            250,
            90,
            90
        );

        // LEFT BOTTOM

        game.batch.draw(
            sparkFrame,
            470,
            420,
            80,
            80
        );

        // RIGHT BOTTOM

        game.batch.draw(
            sparkFrame,
            730,
            420,
            80,
            80
        );

        // =====================================
        // DRAW BUDDY
        // =====================================

        game.batch.draw(

            buddyFrame,

            520,

            floatY,

            260,

            260
        );

        // =====================================
        // TEXT
        // =====================================

        game.font.draw(
            game.batch,
            "BUDDY CREATED!",
            470,
            210
        );

        game.font.draw(
            game.batch,
            "PRESS ENTER TO RETURN",
            390,
            150
        );

        // =====================================
        // END DRAW
        // =====================================

        game.batch.end();

        // =====================================
        // RETURN
        // =====================================

        if (

            Gdx.input.isKeyJustPressed(
                Input.Keys.ENTER
            )

        ) {

            victoryMusic.stop();

            game.setScreen(
                new GameScreen(game)
            );
        }
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

        bg.dispose();

        titleImage.dispose();

        victoryMusic.dispose();
    }
}
