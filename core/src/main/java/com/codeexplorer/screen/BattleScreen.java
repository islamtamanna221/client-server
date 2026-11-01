
//check
//thik
package com.codeexplorer.screen;
//
import com.badlogic.gdx.Net;
//
import com.badlogic.gdx.Net;
import com.codeexplorer.network.LoginApi;
import com.codeexplorer.network.LoginApi;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.codeexplorer.Main;
import com.codeexplorer.model.CurrentPlayer;
public class BattleScreen
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
    // BASES
    // =====================================

    private final Texture playerBase;

    private final Texture enemyBase;

    // =====================================
    // BALL
    // =====================================

    private final Texture pokeball;

    private final Texture openBall;
    //
    private final Texture homeButton;

    // =====================================
    // ENEMY
    // =====================================

    private final Texture enemy;

    // =====================================
    // TRAINER
    // =====================================

    private final Texture[] trainerFrames;

    private final Animation<TextureRegion>
        trainerThrow;

    private boolean throwing =
        false;

    // =====================================
    // AUDIO
    // =====================================

    private Music bgMusic;

    private Sound throwSound;

    private Sound catchSound;

    // =====================================
    // BALL SYSTEM
    // =====================================

    private float ballX = 210;

    private float ballY = 190;

    private boolean throwBall =
        false;

    // =====================================
    // AIM SYSTEM
    // =====================================

    private float aimY = 320;

    // =====================================
    // GAME STATE
    // =====================================

//    private int chances = 3;
//
private int chances;
    private boolean captured =
        false;

    private boolean gameOver =
        false;

    private String resultText =
        "";

    // =====================================
    // SUCCESS TIMER
    // =====================================

    private float successTimer =
        0;

    // =====================================
    // ENEMY POSITION
    // =====================================

    private float enemyX = 850;

    private final float enemyY = 320;

    // =====================================
    // ENEMY MOVE
    // =====================================

    private boolean moveRight =
        true;

    // =====================================
    // TIME
    // =====================================

    private float stateTime = 0;

    // =====================================
    // ENEMY TYPE
    // =====================================

    private final String enemyType;

    public BattleScreen(

        Main game,

        String type

    ) {

        this.game = game;

        this.enemyType = type;
        chances = CurrentPlayer.pokeballs;

        // =====================================
        // BACKGROUND
        // =====================================

        bg = new Texture(
            "battle/background/battle-ground.jpg"
        );

        // =====================================
        // BASES
        // =====================================

        playerBase = new Texture(
            "battle/base/sprite_0004.png"
        );

        enemyBase = new Texture(
            "battle/base/sprite_0006.png"
        );

        // =====================================
        // BALL
        // =====================================

        pokeball = new Texture(
            "battle/pokeball/pokeball.png"
        );

        openBall = new Texture(
            "battle/pokeball/openpokeball.png"
        );
        homeButton = new Texture(
            "ui/home.png"
        );

        // =====================================
        // ENEMY
        // =====================================

        if (type.equals("pikachu")) {

            enemy = new Texture(
                "buddy/pikachu-buddy.png"
            );
        }

        else if (type.equals("charmander")) {

            enemy = new Texture(
                "buddy/dragon-buddy.png"
            );
        }

        else if (type.equals("greninja")) {

            enemy = new Texture(
                "buddy/greninja-buddy.png"
            );
        }

        else if (type.equals("bulbasaur")) {

            enemy = new Texture(
                "buddy/bulbasaur-buddy.png"
            );
        }

        else {

            enemy = new Texture(
                "buddy/pikachu-buddy.png"
            );
        }

        // =====================================
        // TRAINER SPRITES
        // =====================================

        trainerFrames =
            new Texture[5];

        TextureRegion[] regions =
            new TextureRegion[5];

        for (int i = 0; i < 5; i++) {

            String fileName =
                "Trainer/throw/sprite_000"
                    + i
                    + ".png";

            trainerFrames[i] =
                new Texture(fileName);

            regions[i] =
                new TextureRegion(
                    trainerFrames[i]
                );
        }

        trainerThrow =
            new Animation<>(

                0.08f,

                regions
            );

        trainerThrow.setPlayMode(
            Animation.PlayMode.NORMAL
        );

        // =====================================
        // LOAD AUDIO
        // =====================================

        bgMusic = Gdx.audio.newMusic(
            Gdx.files.internal(
                "music/pokemon-alert.mp3"
            )
        );

        bgMusic.setLooping(true);

        bgMusic.setVolume(0.3f);

        bgMusic.play();

        // =====================================
        // THROW SOUND
        // =====================================

        throwSound =
            Gdx.audio.newSound(
                Gdx.files.internal(
                    "music/pok-plink.mp3"
                )
            );

        // =====================================
        // CATCH SOUND
        // =====================================

        catchSound =
            Gdx.audio.newSound(
                Gdx.files.internal(
                    "music/catching-pokamon.mp3"
                )
            );
    }

    @Override
    public void render(float delta) {
        if (

            Gdx.input.isButtonJustPressed(
                Input.Buttons.LEFT
            )

        ) {

            float mouseX =
                Gdx.input.getX();

            float mouseY =
                Gdx.graphics.getHeight()
                    -
                    Gdx.input.getY();

            if (

                mouseX >= 20 &&
                    mouseX <= 110 &&

                    mouseY >= 650 &&
                    mouseY <= 740

            ) {

                bgMusic.stop();

                game.setScreen(

                    new GameScreen(
                        game
                    )
                );
            }
        }

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
        // AIM CONTROL
        // =====================================

        if (

            Gdx.input.isKeyPressed(
                Input.Keys.UP
            )

        ) {

            aimY += 4;
        }

        if (

            Gdx.input.isKeyPressed(
                Input.Keys.DOWN
            )

        ) {

            aimY -= 4;
        }

        // AIM LIMIT

        if (aimY < 120) {

            aimY = 120;
        }

        if (aimY > 520) {

            aimY = 520;
        }

        // =====================================
        // ENEMY MOVE
        // =====================================

        if (!captured &&
            !gameOver) {

            if (moveRight) {

                enemyX += 2;
            }

            else {

                enemyX -= 2;
            }

            // RIGHT LIMIT

            if (enemyX > 900) {

                moveRight = false;
            }

            // LEFT LIMIT

            if (enemyX < 595) {

                moveRight = true;
            }
        }

        // =====================================
        // THROW BALL
        // =====================================

        if (

            Gdx.input.isKeyJustPressed(
                Input.Keys.SPACE
            )

        ) {

            if (

                !throwBall &&
                    !captured &&
                    !gameOver &&
                    chances > 0

            ) {

                // PLAY THROW SOUND

                throwSound.play();

                chances--;
                CurrentPlayer.pokeballs = chances;


                throwBall = true;
                if(CurrentPlayer.id != null) {

                    String json =

                        "{"

                            + "\"playerId\":"

                            + CurrentPlayer.id

                            + "}";

                    LoginApi.usePokeball(

                        json,

                        new Net.HttpResponseListener() {

                            @Override
                            public void handleHttpResponse(

                                Net.HttpResponse httpResponse

                            ) {

                            }

                            @Override
                            public void failed(
                                Throwable t
                            ) {

                                t.printStackTrace();
                            }

                            @Override
                            public void cancelled() {

                            }
                        }
                    );
                }

                throwing = true;

                stateTime = 0;

                // RESET BALL

                ballX = 210;

                ballY = aimY;

                resultText = "";
            }
        }

        // =====================================
        // BALL MOVE
        // =====================================

        if (throwBall) {

            ballX += 10;

            // =====================================
            // HITBOX
            // =====================================

            boolean hitX =

                ballX >= enemyX - 20 &&

                    ballX <= enemyX + 180;

            boolean hitY =

                ballY >= enemyY - 20 &&

                    ballY <= enemyY + 180;

            // =====================================
            // SUCCESS
            // =====================================

            if (hitX && hitY) {

                // PLAY CATCH SOUND

                catchSound.play();

                captured = true;
                if(CurrentPlayer.id != null) {

                    String json =

                        "{"

                            + "\"playerId\":"

                            + CurrentPlayer.id

                            + ","

                            + "\"pokemonName\":\""

                            + enemyType

                            + "\""

                            + "}";

                    LoginApi.savePokemon(

                        json,

                        new Net.HttpResponseListener() {

                            @Override
                            public void handleHttpResponse(
                                Net.HttpResponse httpResponse
                            ) {

                            }

                            @Override
                            public void failed(
                                Throwable t
                            ) {

                                t.printStackTrace();
                            }

                            @Override
                            public void cancelled() {

                            }
                        }
                    );
                }

                throwBall = false;

                throwing = false;

                resultText =
                    "EXCELLENT!";

                successTimer = 0;
            }

            // =====================================
            // MISS
            // =====================================

            if (ballX > 1366) {

                throwBall = false;

                throwing = false;

                resultText =
                    "MISS!";
            }
        }

        // =====================================
        // FAIL
        // =====================================

        if (

            chances <= 0 &&
                !captured &&
                !throwBall

        ) {

            gameOver = true;

            resultText = "FAIL!";
        }

        // =====================================
        // AUTO NEXT SCREEN
        // =====================================

        if (captured) {

            successTimer += delta;

            if (successTimer > 2f) {

                bgMusic.stop();

                game.setScreen(

                    new BuddyCreatedScreen(

                        game,

                        enemyType

                    )
                );
            }
        }

        // =====================================
        // START DRAW
        // =====================================

        game.batch.begin();

        // =====================================
        // BACKGROUND
        // =====================================

        game.batch.draw(
            bg,
            0,
            0,
            1366,
            768
        );

        // =====================================
        // BASES
        // =====================================

        game.batch.draw(
            enemyBase,
            720,
            230,
            300,
            100
        );

        game.batch.draw(
            playerBase,
            80,
            80,
            300,
            100
        );

        // =====================================
        // TRAINER
        // =====================================

        TextureRegion trainerFrame;

        if (throwing) {

            trainerFrame =
                trainerThrow.getKeyFrame(
                    stateTime,
                    false
                );
        }

        else {

            trainerFrame =
                new TextureRegion(
                    trainerFrames[0]
                );
        }

        game.batch.draw(

            trainerFrame,

            120,

            100,

            180,

            180
        );
        game.batch.draw(

            homeButton,

            20,

            650,

            90,

            90
        );

        // =====================================
        // AIM POINTER
        // =====================================

        game.font.draw(
            game.batch,
            ">",
            260,
            aimY + 20
        );

        // =====================================
        // ENEMY
        // =====================================

        if (!captured) {

            game.batch.draw(
                enemy,
                enemyX,
                enemyY,
                220,
                220
            );
        }

        // =====================================
        // BALL
        // =====================================

        if (throwBall) {

            game.batch.draw(
                pokeball,
                ballX,
                ballY,
                40,
                40
            );
        }

        // =====================================
        // TOP RIGHT BALL COUNT
        // =====================================

        for (int i = 0; i < chances; i++) {

            game.batch.draw(
                pokeball,
                1180 + (i * 55),
                690,
                45,
                45
            );
        }

        // =====================================
        // RESULT TEXT
        // =====================================

        if (!resultText.equals("")) {

            game.font.draw(
                game.batch,
                resultText,
                500,
                650
            );
        }

        // =====================================
        // SUCCESS TEXT
        // =====================================

        if (captured) {

            game.batch.draw(
                openBall,
                enemyX,
                enemyY,
                100,
                100
            );

            game.font.draw(
                game.batch,
                "BUDDY CREATED!",
                430,
                590
            );
        }

        // =====================================
        // FAIL SCREEN
        // =====================================

        if (gameOver) {

            game.font.draw(
                game.batch,
                "YOU FAILED!",
                470,
                590
            );

            game.font.draw(
                game.batch,
                "PRESS ENTER TO RETURN",
                340,
                530
            );

            if (

                Gdx.input.isKeyJustPressed(
                    Input.Keys.ENTER
                )

            ) {

                bgMusic.stop();

                game.setScreen(
                    new GameScreen(game)
                );
            }
        }

        // =====================================
        // UI TEXT
        // =====================================

        game.font.draw(
            game.batch,
            "UP / DOWN TO AIM",
            30,
            730
        );

        game.font.draw(
            game.batch,
            "SPACE TO THROW",
            30,
            690
        );

        game.font.draw(
            game.batch,
            "POKEBALLS : " + chances,
            30,
            650
        );

        // =====================================
        // END DRAW
        // =====================================
        //


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

        bg.dispose();

        playerBase.dispose();

        enemyBase.dispose();

        pokeball.dispose();

        openBall.dispose();
        homeButton.dispose();

        enemy.dispose();

        bgMusic.dispose();

        throwSound.dispose();

        catchSound.dispose();

        for (Texture texture
            : trainerFrames) {

            texture.dispose();
        }
    }
}
//check
