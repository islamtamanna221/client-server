//
//package com.codeexplorer.screen;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.audio.Music;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.Animation;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.ScreenUtils;
//
//import com.codeexplorer.Main;
//
//public class StartScreen implements Screen {
//
//    private final Main game;
//
//    // BACKGROUND
//
//    private Texture background;
//
//    // BUTTON
//
//    private Texture normalButton;
//    private Texture hoverButton;
//
//    // BUTTON POSITION
//
//    private float buttonX = 540;
//    private float buttonY = 120;
//
//    // BUTTON SIZE
//
//    private float buttonWidth = 260;
//    private float buttonHeight = 100;
//
//    // FONT
//
//    private BitmapFont font;
//
//    // ANIMATION
//
//    private Animation<TextureRegion> pikachuAnimation;
//
//    private float stateTime;
//
//    // MUSIC
//
//    private Music bgMusic;
//
//    public StartScreen(Main game) {
//
//        this.game = game;
//    }
//
//    @Override
//    public void show() {
//
//        // BACKGROUND
//
//        background = new Texture(
//            "ui/start_bg.png"
//        );
//
//        // BUTTONS
//
//        normalButton = new Texture(
//            "ui/normal.png"
//        );
//
//        hoverButton = new Texture(
//            "ui/hover.png"
//        );
//
//        // FONT
//
//        font = new BitmapFont();
//
//        font.getData().setScale(3);
//
//        // LOAD PIKACHU SPRITES
//
//        Array<TextureRegion> frames =
//            new Array<>();
//
//        for (int i = 0; i < 30; i++) {
//
//            String number =
//                String.format("%04d", i);
//
//            Texture texture =
//                new Texture(
//                    "pikachu/sprite_" +
//                        number +
//                        ".png"
//                );
//
//            frames.add(
//                new TextureRegion(texture)
//            );
//        }
//
//        // CREATE ANIMATION
//
//        pikachuAnimation =
//            new Animation<>(
//                0.08f,
//                frames,
//                Animation.PlayMode.LOOP
//            );
//
//        stateTime = 0f;
//
//        // LOAD MUSIC
//
//        bgMusic = Gdx.audio.newMusic(
//            Gdx.files.internal(
//                "music/bg_music.mp3"
//            )
//        );
//
//        bgMusic.setLooping(true);
//
//        bgMusic.setVolume(0.5f);
//
//        bgMusic.play();
//    }
//
//    @Override
//    public void render(float delta) {
//
//        ScreenUtils.clear(0, 0, 0, 1);
//
//        stateTime += delta;
//
//        // MOUSE POSITION
//
//        float mouseX =
//            Gdx.input.getX();
//
//        float mouseY =
//            768 - Gdx.input.getY();
//
//        // BUTTON HOVER
//
//        boolean isHover =
//            mouseX >= buttonX &&
//                mouseX <= buttonX + buttonWidth &&
//                mouseY >= buttonY &&
//                mouseY <= buttonY + buttonHeight;
//
//        // CURRENT FRAME
//
//        TextureRegion currentFrame =
//            pikachuAnimation.getKeyFrame(
//                stateTime
//            );
//
//        // START DRAW
//
//        game.batch.begin();
//
//        // DRAW BACKGROUND
//
//        game.batch.draw(
//            background,
//            0,
//            0,
//            1366,
//            768
//        );
//
//        // DRAW PIKACHU
//
//        game.batch.draw(
//            currentFrame,
//            0,
//            100,
//            120,
//            100
//        );
//
//        // LEVEL TEXT
//
//        font.setColor(Color.YELLOW);
//
//        font.draw(
//            game.batch,
//            "LEVEL 1",
//            520,
//            700
//        );
//
//        // TASK TEXT
//
//        font.setColor(Color.WHITE);
//
//        float animatedY =
//            610 +
//                (float)Math.sin(stateTime * 4)
//                    * 10;
//
//        font.draw(
//            game.batch,
//            "TASK : MAKE BUDDY",
//            360,
//            animatedY
//        );
//
//        // DRAW BUTTON
//
//        if (isHover) {
//
//            game.batch.draw(
//                hoverButton,
//                buttonX,
//                buttonY,
//                buttonWidth,
//                buttonHeight
//            );
//
//        } else {
//
//            game.batch.draw(
//                normalButton,
//                buttonX,
//                buttonY,
//                buttonWidth,
//                buttonHeight
//            );
//        }
//
//        game.batch.end();
//
//        // BUTTON CLICK FIX
//
//        if (Gdx.input.justTouched()) {
//
//            float clickX =
//                Gdx.input.getX();
//
//            float clickY =
//                768 - Gdx.input.getY();
//
//            boolean isClicked =
//                clickX >= buttonX &&
//                    clickX <= buttonX + buttonWidth &&
//                    clickY >= buttonY &&
//                    clickY <= buttonY + buttonHeight;
//
//            if (isClicked) {
//
//                // STOP MUSIC
//
//                bgMusic.stop();
//
//                // CHANGE SCREEN
//
//                game.setScreen(
//                    new GameScreen(game)
//                );
//
//                dispose();
//            }
//        }
//    }
//
//    @Override
//    public void resize(int width, int height) {
//
//    }
//
//    @Override
//    public void pause() {
//
//    }
//
//    @Override
//    public void resume() {
//
//    }
//
//    @Override
//    public void hide() {
//
//    }
//
//    @Override
//    public void dispose() {
//
//        background.dispose();
//
//        normalButton.dispose();
//
//        hoverButton.dispose();
//
//        font.dispose();
//
//        bgMusic.dispose();
//    }
//}
//thik
//package com.codeexplorer.screen;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.audio.Music;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.Animation;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.ScreenUtils;
//
//import com.codeexplorer.Main;
//
//public class StartScreen implements Screen {
//
//    private final Main game;
//
//    // =====================================
//    // BACKGROUND
//    // =====================================
//
//    private Texture background;
//
//    // =====================================
//    // BUTTON
//    // =====================================
//
//    private Texture normalButton;
//    private Texture hoverButton;
//
//    // BUTTON POSITION
//
//    private float buttonX = 540;
//    private float buttonY = 120;
//
//    // BUTTON SIZE
//
//    private float buttonWidth = 260;
//    private float buttonHeight = 100;
//
//    // =====================================
//    // FONT
//    // =====================================
//
//    private BitmapFont font;
//
//    // =====================================
//    // PIKACHU ANIMATION
//    // =====================================
//
//    private Animation<TextureRegion> pikachuAnimation;
//
//    // =====================================
//    // DRAGON ANIMATION
//    // =====================================
//
//    private Animation<TextureRegion> dragonAnimation;
//
//    // =====================================
//    // TIME
//    // =====================================
//
//    private float stateTime;
//
//    // =====================================
//    // MUSIC
//    // =====================================
//
//    private Music bgMusic;
//
//    public StartScreen(Main game) {
//
//        this.game = game;
//    }
//
//    @Override
//    public void show() {
//
//        // =====================================
//        // BACKGROUND
//        // =====================================
//
//        background = new Texture(
//            "ui/start_bg.png"
//        );
//
//        // =====================================
//        // BUTTONS
//        // =====================================
//
//        normalButton = new Texture(
//            "ui/normal.png"
//        );
//
//        hoverButton = new Texture(
//            "ui/hover.png"
//        );
//
//        // =====================================
//        // FONT
//        // =====================================
//
//        font = new BitmapFont();
//
//        font.getData().setScale(3);
//
//        // =====================================
//        // LOAD PIKACHU SPRITES
//        // =====================================
//
//        Array<TextureRegion> pikachuFrames =
//            new Array<>();
//
//        for (int i = 0; i <= 30; i++) {
//
//            String number =
//                String.format("%04d", i);
//
//            Texture texture =
//                new Texture(
//                    "pikachu/sprite_" +
//                        number +
//                        ".png"
//                );
//
//            pikachuFrames.add(
//                new TextureRegion(texture)
//            );
//        }
//
//        // =====================================
//        // CREATE PIKACHU ANIMATION
//        // =====================================
//
//        pikachuAnimation =
//            new Animation<>(
//                0.08f,
//                pikachuFrames,
//                Animation.PlayMode.LOOP
//            );
//
//        // =====================================
//        // LOAD DRAGON SPRITES
//        // =====================================
//
//        Array<TextureRegion> dragonFrames =
//            new Array<>();
//
//        for (int i = 5; i <= 15; i++) {
//
//            String number =
//                String.format("%04d", i);
//
//            Texture texture =
//                new Texture(
//                    "dragon/sprite_" +
//                        number +
//                        ".png"
//                );
//
//            dragonFrames.add(
//                new TextureRegion(texture)
//            );
//        }
//
//        // =====================================
//        // CREATE DRAGON ANIMATION
//        // =====================================
//
//        dragonAnimation =
//            new Animation<>(
//                0.12f,
//                dragonFrames,
//                Animation.PlayMode.LOOP
//            );
//
//        stateTime = 0f;
//
//        // =====================================
//        // LOAD MUSIC
//        // =====================================
//
//        bgMusic = Gdx.audio.newMusic(
//            Gdx.files.internal(
//                "music/bg_music.mp3"
//            )
//        );
//
//        bgMusic.setLooping(true);
//
//        bgMusic.setVolume(0.5f);
//
//        bgMusic.play();
//    }
//
//    @Override
//    public void render(float delta) {
//
//        ScreenUtils.clear(0, 0, 0, 1);
//
//        stateTime += delta;
//
//        // =====================================
//        // MOUSE POSITION
//        // =====================================
//
//        float mouseX =
//            Gdx.input.getX();
//
//        float mouseY =
//            768 - Gdx.input.getY();
//
//        // =====================================
//        // BUTTON HOVER
//        // =====================================
//
//        boolean isHover =
//            mouseX >= buttonX &&
//                mouseX <= buttonX + buttonWidth &&
//                mouseY >= buttonY &&
//                mouseY <= buttonY + buttonHeight;
//
//        // =====================================
//        // CURRENT FRAMES
//        // =====================================
//
//        TextureRegion pikachuFrame =
//            pikachuAnimation.getKeyFrame(
//                stateTime
//            );
//
//        TextureRegion dragonFrame =
//            dragonAnimation.getKeyFrame(
//                stateTime
//            );
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
//            background,
//            0,
//            0,
//            1366,
//            768
//        );
//
//        // =====================================
//        // DRAW PIKACHU
//        // =====================================
//
//        game.batch.draw(
//            pikachuFrame,
//            40,
//            580,
//            70,
//            70
//        );
//
//        // =====================================
//        // DRAW DRAGON
//        // =====================================
//
//        float dragonMoveX =
//            1100 +
//                (float)Math.sin(stateTime * 2)
//                    * 8;
//
//        game.batch.draw(
//            dragonFrame,
//            dragonMoveX,
//            60,
//            150,
//            150
//        );
//
//        // =====================================
//        // LEVEL TEXT
//        // =====================================
//
//        font.setColor(Color.YELLOW);
//
//        font.draw(
//            game.batch,
//            "LEVEL 1",
//            520,
//            700
//        );
//
//        // =====================================
//        // TASK TEXT
//        // =====================================
//
//        font.setColor(Color.WHITE);
//
//        float textMove =
//            610 +
//                (float)Math.sin(stateTime * 4)
//                    * 6;
//
//        font.draw(
//            game.batch,
//            "TASK : MAKE BUDDY",
//            360,
//            textMove
//        );
//
//        // =====================================
//        // DRAW BUTTON
//        // =====================================
//
//        if (isHover) {
//
//            game.batch.draw(
//                hoverButton,
//                buttonX,
//                buttonY,
//                buttonWidth,
//                buttonHeight
//            );
//
//        } else {
//
//            game.batch.draw(
//                normalButton,
//                buttonX,
//                buttonY,
//                buttonWidth,
//                buttonHeight
//            );
//        }
//
//        game.batch.end();
//
//        // =====================================
//        // BUTTON CLICK
//        // =====================================
//
//        if (Gdx.input.justTouched()) {
//
//            float clickX =
//                Gdx.input.getX();
//
//            float clickY =
//                768 - Gdx.input.getY();
//
//            boolean isClicked =
//                clickX >= buttonX &&
//                    clickX <= buttonX + buttonWidth &&
//                    clickY >= buttonY &&
//                    clickY <= buttonY + buttonHeight;
//
//            if (isClicked) {
//
//                bgMusic.stop();
//
//                game.setScreen(
//                    new GameScreen(game)
//                );
//
//                dispose();
//            }
//        }
//    }
//
//    @Override
//    public void resize(int width, int height) {
//
//    }
//
//    @Override
//    public void pause() {
//
//    }
//
//    @Override
//    public void resume() {
//
//    }
//
//    @Override
//    public void hide() {
//
//    }
//
//    @Override
//    public void dispose() {
//
//        background.dispose();
//
//        normalButton.dispose();
//
//        hoverButton.dispose();
//
//        font.dispose();
//
//        bgMusic.dispose();
//    }
//}
//thik
package com.codeexplorer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import com.codeexplorer.Main;

public class StartScreen implements Screen {

    private final Main game;

    // =====================================
    // BACKGROUND
    // =====================================

    private Texture background;

    // =====================================
    // BUTTON
    // =====================================

    private Texture normalButton;

    private Texture hoverButton;

    // =====================================
    // BACK BUTTON
    // =====================================

    private Texture backButton;

    private Rectangle backRect;

    // =====================================
    // BUTTON POSITION
    // =====================================

    private float buttonX = 540;

    private float buttonY = 120;

    // =====================================
    // BUTTON SIZE
    // =====================================

    private float buttonWidth = 260;

    private float buttonHeight = 100;

    // =====================================
    // FONT
    // =====================================

    private BitmapFont font;

    // =====================================
    // PIKACHU ANIMATION
    // =====================================

    private Animation<TextureRegion>
        pikachuAnimation;

    // =====================================
    // DRAGON ANIMATION
    // =====================================

    private Animation<TextureRegion>
        dragonAnimation;

    // =====================================
    // TIME
    // =====================================

    private float stateTime;

    // =====================================
    // MUSIC
    // =====================================

    private Music bgMusic;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public StartScreen(
        Main game
    ) {

        this.game = game;
    }

    // =====================================
    // SHOW
    // =====================================

    @Override
    public void show() {

        // =====================================
        // BACKGROUND
        // =====================================

        background = new Texture(
            "ui/start_bg.png"
        );

        // =====================================
        // BUTTONS
        // =====================================

        normalButton = new Texture(
            "ui/normal.png"
        );

        hoverButton = new Texture(
            "ui/hover.png"
        );

        // =====================================
        // BACK BUTTON
        // =====================================

        backButton = new Texture(
            "ui/back_button.png"
        );

        backRect = new Rectangle(
            30,
            650,
            80,
            80
        );

        // =====================================
        // FONT
        // =====================================

        font = new BitmapFont();

        font.getData().setScale(3);

        // =====================================
        // LOAD PIKACHU
        // =====================================

        Array<TextureRegion>
            pikachuFrames =
            new Array<>();

        for(int i=0; i<=30; i++) {

            String number =
                String.format(
                    "%04d",
                    i
                );

            Texture texture =
                new Texture(

                    "pikachu/sprite_"
                        +
                        number
                        +
                        ".png"
                );

            pikachuFrames.add(

                new TextureRegion(
                    texture
                )
            );
        }

        // =====================================
        // CREATE PIKACHU ANIMATION
        // =====================================

        pikachuAnimation =

            new Animation<>(

                0.08f,

                pikachuFrames,

                Animation.PlayMode.LOOP
            );

        // =====================================
        // LOAD DRAGON
        // =====================================

        Array<TextureRegion>
            dragonFrames =
            new Array<>();

        for(int i=5; i<=15; i++) {

            String number =
                String.format(
                    "%04d",
                    i
                );

            Texture texture =
                new Texture(

                    "charmander/sprite_"
                        +
                        number
                        +
                        ".png"
                );

            dragonFrames.add(

                new TextureRegion(
                    texture
                )
            );
        }

        // =====================================
        // CREATE DRAGON ANIMATION
        // =====================================

        dragonAnimation =

            new Animation<>(

                0.12f,

                dragonFrames,

                Animation.PlayMode.LOOP
            );

        stateTime = 0f;

        // =====================================
        // MUSIC
        // =====================================

        bgMusic = Gdx.audio.newMusic(

            Gdx.files.internal(
                "music/bg_music.mp3"
            )
        );

        bgMusic.setLooping(true);

        bgMusic.setVolume(0.5f);

        bgMusic.play();
    }

    // =====================================
    // RENDER
    // =====================================

    @Override
    public void render(
        float delta
    ) {

        ScreenUtils.clear(
            0,
            0,
            0,
            1
        );

        stateTime += delta;

        // =====================================
        // MOUSE
        // =====================================

        float mouseX =
            Gdx.input.getX();

        float mouseY =
            768 - Gdx.input.getY();

        // =====================================
        // BUTTON HOVER
        // =====================================

        boolean isHover =

            mouseX >= buttonX
                &&
                mouseX <=
                    buttonX
                        +
                        buttonWidth

                &&

                mouseY >= buttonY
                &&
                mouseY <=
                    buttonY
                        +
                        buttonHeight;

        // =====================================
        // CURRENT FRAMES
        // =====================================

        TextureRegion pikachuFrame =

            pikachuAnimation
                .getKeyFrame(
                    stateTime
                );

        TextureRegion dragonFrame =

            dragonAnimation
                .getKeyFrame(
                    stateTime
                );

        // =====================================
        // START DRAW
        // =====================================

        game.batch.begin();

        // =====================================
        // DRAW BACKGROUND
        // =====================================

        game.batch.draw(

            background,

            0,

            0,

            1366,

            768
        );

        // =====================================
        // DRAW BACK BUTTON
        // =====================================

        game.batch.draw(

            backButton,

            30,

            650,

            80,

            80
        );

        // =====================================
        // DRAW PIKACHU
        // =====================================

        game.batch.draw(

            pikachuFrame,

            40,

            580,

            70,

            70
        );

        // =====================================
        // DRAW DRAGON
        // =====================================

        float dragonMoveX =

            1100

                +

                (float)Math.sin(
                    stateTime * 2
                ) * 8;

        game.batch.draw(

            dragonFrame,

            dragonMoveX,

            60,

            150,

            150
        );

        // =====================================
        // LEVEL TEXT
        // =====================================

        font.setColor(
            Color.YELLOW
        );

        font.draw(

            game.batch,

            "LEVEL 1",

            520,

            700
        );

        // =====================================
        // TASK TEXT
        // =====================================

        font.setColor(
            Color.WHITE
        );

        float textMove =

            610

                +

                (float)Math.sin(
                    stateTime * 4
                ) * 6;

        font.draw(

            game.batch,

            "TASK : MAKE BUDDY",

            360,

            textMove
        );

        // =====================================
        // DRAW BUTTON
        // =====================================

        if(isHover) {

            game.batch.draw(

                hoverButton,

                buttonX,

                buttonY,

                buttonWidth,

                buttonHeight
            );
        }

        else {

            game.batch.draw(

                normalButton,

                buttonX,

                buttonY,

                buttonWidth,

                buttonHeight
            );
        }

        game.batch.end();

        // =====================================
        // CLICK
        // =====================================

        if(Gdx.input.justTouched()) {

            float clickX =
                Gdx.input.getX();

            float clickY =
                768 - Gdx.input.getY();

            // =====================================
            // BACK BUTTON
            // =====================================

            boolean backClicked =

                clickX >= 30
                    &&
                    clickX <= 110

                    &&

                    clickY >= 650
                    &&
                    clickY <= 730;

            if(backClicked) {

                bgMusic.stop();

                game.setScreen(

                    new HomeScreen(
                        game
                    )
                );

                dispose();

                return;
            }

            // =====================================
            // START BUTTON
            // =====================================

            boolean isClicked =

                clickX >= buttonX
                    &&
                    clickX <=
                        buttonX
                            +
                            buttonWidth

                    &&

                    clickY >= buttonY
                    &&
                    clickY <=
                        buttonY
                            +
                            buttonHeight;

            if(isClicked) {

                bgMusic.stop();

                game.setScreen(

                    new GameScreen(
                        game
                    )
                );

                dispose();
            }
        }
    }

    @Override
    public void resize(
        int width,
        int height
    ) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    // =====================================
    // DISPOSE
    // =====================================

    @Override
    public void dispose() {

        background.dispose();

        normalButton.dispose();

        hoverButton.dispose();

        backButton.dispose();

        font.dispose();

        bgMusic.dispose();
    }
}
