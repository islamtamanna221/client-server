//
//package com.codeexplorer.screen;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.utils.Array;
//import com.codeexplorer.Main;
//import com.codeexplorer.model.CurrentPlayer;
//import com.codeexplorer.network.GameSocket;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//
//public class NewBattleScreen implements Screen {
//
//    private final Main game;
//    private Texture grassBg;
//    private Texture pokeball;
//    private BitmapFont font;
//    private BitmapFont uiFont;
//
//    private String playerName;
//    private String enemyName;
//
//    // Enemy Trainer (Right Side)
//    private Texture[] enemyFrames;
//    private int enemyFrame;
//    private boolean enemyThrow;
//    private float enemyTimer;
//    private float enemyBallX;
//    private float enemyBallY;
//
//    // Player Trainer (Left Side)
//    private Texture[] playerFrames;
//    private int playerFrame;
//    private boolean playerThrow;
//    private float playerTimer;
//    private float playerBallX;
//    private float playerBallY;
//
//    private boolean isMyTurn;
//
//    private enum BattleState {
//        SELECTION_MODE,
//        BATTLE_MODE
//    }
//    private BattleState currentState = BattleState.SELECTION_MODE;
//
//    // ⚔️ POKEMON DATA & LIVE ANIMATION TRACKER
//    private ArrayList<PokemonModel> myPokemons;
//    private int selectedPokemonIndex = -1;
//    private String activeEnemyPokemonName = "";
//
//    private Array<Texture> myActivePokemonFrames;
//    private Array<Texture> enemyActivePokemonFrames;
//    private int myPokemonCurrentFrame = 0;
//    private int enemyPokemonCurrentFrame = 0;
//    private float myPokemonAnimationTimer = 0;
//    private float enemyPokemonAnimationTimer = 0;
//
//    private boolean isMyPokemonSummoned = false;
//    private boolean isEnemyPokemonSummoned = false;
//
//    private float myPokemonX, myPokemonY;
//    private float enemyPokemonX, enemyPokemonY;
//    private String statusMessage = "👉 [SELECTION] Press number keys to select your Caught Pokemon, then press ENTER!";
//
//    // পোকেমন মডেল ক্লাস
//    public static class PokemonModel {
//        public int id;
//        public String name;
//        public int hp;
//
//        public PokemonModel(int id, String name, int hp) {
//            this.id = id;
//            this.name = name;
//            this.hp = hp;
//        }
//    }
//
//    public NewBattleScreen(Main game, String playerName, String enemyName, Long enemyId) {
//        this.game = game;
//        this.playerName = playerName != null ? playerName : "PLAYER";
//        this.enemyName = enemyName != null ? enemyName : "ENEMY";
//
//        grassBg = new Texture("battle/background/grass.png");
//        pokeball = new Texture("battle/pokeball/pokeball.png");
//
//        font = new BitmapFont();
//        font.getData().setScale(1.5f);
//
//        uiFont = new BitmapFont();
//        uiFont.getData().setScale(1.4f);
//
//        if (CurrentPlayer.id != null && enemyId != null) {
//            this.isMyTurn = CurrentPlayer.id < enemyId;
//        } else {
//            this.isMyTurn = true;
//        }
//
//        myPokemons = new ArrayList<>();
//        myActivePokemonFrames = new Array<>();
//        enemyActivePokemonFrames = new Array<>();
//
//        // 📊 স্ক্রিন ওপেন হওয়ার সাথে সাথেই ডাটাবেজ থেকে ডাটা লোড হবে
//        loadPokemonDataFromDatabase();
//
//        // Trainer Animation Frames
//        enemyFrames = new Texture[6];
//        enemyFrames[0] = new Texture("Trainer/battlegirl/sprite_0002.png");
//        enemyFrames[1] = new Texture("Trainer/battlegirl/sprite_0003.png");
//        enemyFrames[2] = new Texture("Trainer/battlegirl/sprite_0000.png");
//        enemyFrames[3] = new Texture("Trainer/battlegirl/sprite_0001.png");
//        enemyFrames[4] = new Texture("Trainer/battlegirl/sprite_0004.png");
//        enemyFrames[5] = new Texture("Trainer/battlegirl/sprite_0005.png");
//
//        playerFrames = new Texture[5];
//        playerFrames[0] = new Texture("Trainer/playerr/sprite_0004.png");
//        playerFrames[1] = new Texture("Trainer/playerr/sprite_0002.png");
//        playerFrames[2] = new Texture("Trainer/playerr/sprite_0001.png");
//        playerFrames[3] = new Texture("Trainer/playerr/sprite_0000.png");
//        playerFrames[4] = new Texture("Trainer/playerr/sprite_0003.png");
//
//        resetAnimation();
//        GameSocket.enemyThrow = false;
//    }
//
//    // 🎯 ডাটাবেজ আইডি অনুযায়ী নিখুঁত পোকেমন নেম ম্যাপিং
//    private String getPokemonNameById(int pokemonId) {
//        switch (pokemonId) {
//            case 1: return "Charmander";
//            case 2: return "Greninja";
//            case 3: return "Bulbasaur";
//            case 4: return "Pikachu";
//            default: return "Pokemon";
//        }
//    }
//
//    // 🗄️ সম্পূর্ণ ডাইনামিক ডাটাবেজ কুয়েরি মেথড (XAMPP 'pokemongame' ম্যাচিং)
//    private void loadPokemonDataFromDatabase() {
//        myPokemons.clear();
//
//        // 🛠️ রানটাইমে ড্রাইভার ফোর্স লোড করার সেফটি মেকানিজম
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            Gdx.app.log("Database_Error", "MySQL Driver Class missing!");
//        }
//
//        // 🔗 XAMPP MySQL URL, User, Password কনফিগারেশন
//        String url = "jdbc:mysql://localhost:3306/pokemongame?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
//        String user = "root";
//        String password = ""; // XAMPP ডিফল্ট পাসওয়ার্ড ফাঁকা থাকে
//
//        // 🎯 কুয়েরি আপডেট: 'player_pokemon' টেবিল থেকে প্লেয়ারের ডাটা রিড করা হচ্ছে
//        String query = "SELECT pokemon_id, current_hp FROM player_pokemon WHERE player_id = ?";
//        long currentPlayerId = (CurrentPlayer.id != null) ? CurrentPlayer.id : 21;
//
//        try {
//            Connection conn = DriverManager.getConnection(url, user, password);
//            PreparedStatement stmt = conn.prepareStatement(query);
//            stmt.setLong(1, currentPlayerId);
//
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                int pId = rs.getInt("pokemon_id");
//                int hp = rs.getInt("current_hp");
//                String name = getPokemonNameById(pId);
//
//                // ডাটাবেজের রিয়েল ডাটা মডেল লিস্টে অ্যাড হচ্ছে
//                myPokemons.add(new PokemonModel(pId, name, hp));
//            }
//
//            rs.close();
//            stmt.close();
//            conn.close();
//
//            Gdx.app.log("Database_Success", "Successfully loaded " + myPokemons.size() + " pokemon(s) from XAMPP DB for player: " + currentPlayerId);
//
//        } catch (Exception e) {
//            Gdx.app.log("Database_Error", "Failed to load live data from 'pokemongame'. Using offline safety fallback.");
//            e.printStackTrace();
//
//            // ডাটাবেজ কানেকশনে সমস্যা হলে গেম যেন ক্র্যাশ না করে, তাই ফলব্যাক ব্যাকআপ
//            if (myPokemons.isEmpty()) {
//                myPokemons.add(new PokemonModel(4, getPokemonNameById(4), 100)); // Pikachu
//                myPokemons.add(new PokemonModel(3, getPokemonNameById(3), 100)); // Bulbasaur
//            }
//        }
//    }
//
//    private void loadPokemonAnimationFrames(String pokemonName, boolean isPlayer) {
//        if (isPlayer) {
//            for (Texture t : myActivePokemonFrames) t.dispose();
//            myActivePokemonFrames.clear();
//            myPokemonCurrentFrame = 0;
//        } else {
//            for (Texture t : enemyActivePokemonFrames) t.dispose();
//            enemyActivePokemonFrames.clear();
//            enemyPokemonCurrentFrame = 0;
//        }
//
//        try {
//            String folder = pokemonName.toLowerCase().trim();
//
//            if (folder.equals("pikachu")) {
//                for (int i = 0; i <= 30; i++) {
//                    String file = String.format("pikachu/sprite_%04d.png", i);
//                    if (isPlayer) myActivePokemonFrames.add(new Texture(file));
//                    else enemyActivePokemonFrames.add(new Texture(file));
//                }
//            }
//            else if (folder.equals("bulbasaur")) {
//                for (int i = 0; i <= 4; i++) {
//                    String file = "bulbasaur/frame_" + i + "_delay-0.1s.png";
//                    if (isPlayer) myActivePokemonFrames.add(new Texture(file));
//                    else enemyActivePokemonFrames.add(new Texture(file));
//                }
//            }
//            else if (folder.equals("charmander")) {
//                for (int i = 0; i <= 15; i++) {
//                    String file = String.format("charmander/sprite_%04d.png", i);
//                    if (isPlayer) myActivePokemonFrames.add(new Texture(file));
//                    else enemyActivePokemonFrames.add(new Texture(file));
//                }
//            }
//            else if (folder.equals("greninja")) {
//                for (int i = 0; i <= 38; i++) {
//                    String file = String.format("greninja/frame_%02d_delay-0.04s.png", i);
//                    if (isPlayer) myActivePokemonFrames.add(new Texture(file));
//                    else enemyActivePokemonFrames.add(new Texture(file));
//                }
//            }
//        } catch (Exception e) {
//            Gdx.app.log("NewBattleScreen", "Missing Assets for: " + pokemonName);
//        }
//    }
//
//    private void resetAnimation() {
//        enemyFrame = 0;
//        enemyThrow = false;
//        enemyTimer = 0;
//        enemyBallX = Gdx.graphics.getWidth() - 150;
//        enemyBallY = 320;
//
//        playerThrow = false;
//        playerFrame = 0;
//        playerTimer = 0;
//        playerBallX = 220;
//        playerBallY = 120;
//    }
//
//    @Override public void show() {}
//
//    @Override
//    public void render(float delta) {
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        float screenWidth = Gdx.graphics.getWidth();
//        float screenHeight = Gdx.graphics.getHeight();
//
//        // ========================================================
//        // 🔄 STATE 1: SELECTION MODE
//        // ========================================================
//        if (currentState == BattleState.SELECTION_MODE) {
//
//            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && myPokemons.size() > 0) {
//                selectedPokemonIndex = 0;
//            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && myPokemons.size() > 1) {
//                selectedPokemonIndex = 1;
//            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) && myPokemons.size() > 2) {
//                selectedPokemonIndex = 2;
//            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) && myPokemons.size() > 3) {
//                selectedPokemonIndex = 3;
//            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5) && myPokemons.size() > 4) {
//                selectedPokemonIndex = 4;
//            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6) && myPokemons.size() > 5) {
//                selectedPokemonIndex = 5;
//            }
//
//            if (selectedPokemonIndex != -1) {
//                statusMessage = "🎯 SELECTED: " + myPokemons.get(selectedPokemonIndex).name.toUpperCase() + "! Press ENTER to Lock!";
//            }
//
//            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
//                if (selectedPokemonIndex == -1) {
//                    statusMessage = "❌ ERROR: Choose a Pokemon from your DB caught list first!";
//                } else {
//                    currentState = BattleState.BATTLE_MODE;
//                    if (isMyTurn) {
//                        statusMessage = "⚔️ YOUR TURN! Press ENTER to THROW your Pokeball!";
//                    } else {
//                        statusMessage = "⚔️ OPPONENT'S TURN! Waiting for them to throw...";
//                    }
//                }
//            }
//        }
//
//        // ========================================================
//        // ⚔️ STATE 2: BATTLE MODE
//        // ========================================================
//        else if (currentState == BattleState.BATTLE_MODE) {
//
//            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
//                if (isMyTurn && !playerThrow && !enemyThrow && !isMyPokemonSummoned) {
//                    playerThrow = true;
//                    playerFrame = 0;
//                    playerTimer = 0;
//                    playerBallX = 220;
//                    playerBallY = 120;
//
//                    String myChosenPokemon = myPokemons.get(selectedPokemonIndex).name;
//                    loadPokemonAnimationFrames(myChosenPokemon, true);
//
//                    try {
//                        GameSocket.sendThrow(myChosenPokemon);
//                    } catch (Exception e) {
//                        try { GameSocket.sendThrow(); } catch (Exception ex) {}
//                    }
//
//                    statusMessage = "You threw " + myChosenPokemon.toUpperCase() + "!";
//                }
//            }
//
//            if (GameSocket.enemyThrow) {
//                if (!playerThrow && !enemyThrow && !isEnemyPokemonSummoned) {
//                    enemyThrow = true;
//                    enemyFrame = 0;
//                    enemyTimer = 0;
//                    enemyBallX = screenWidth - 160;
//                    enemyBallY = 320;
//
//                    if (GameSocket.enemyPokemonName != null && !GameSocket.enemyPokemonName.isEmpty()) {
//                        activeEnemyPokemonName = GameSocket.enemyPokemonName;
//                    } else {
//                        activeEnemyPokemonName = "Pikachu";
//                    }
//
//                    loadPokemonAnimationFrames(activeEnemyPokemonName, false);
//                    statusMessage = "Opponent threw " + activeEnemyPokemonName.toUpperCase() + "!";
//                }
//                GameSocket.enemyThrow = false;
//            }
//
//            if (playerThrow) {
//                playerTimer += delta;
//                if (playerTimer > 0.12f) {
//                    playerTimer = 0;
//                    if (playerFrame < 4) playerFrame++;
//                }
//                if (playerFrame >= 2) {
//                    playerBallX += 580 * delta;
//                    playerBallY += 120 * delta;
//                }
//
//                if (playerBallX >= 400 && !isMyPokemonSummoned) {
//                    isMyPokemonSummoned = true;
//                    myPokemonX = 400;
//                    myPokemonY = 160;
//                    playerThrow = false;
//
//                    if (!isEnemyPokemonSummoned) {
//                        isMyTurn = false;
//                        statusMessage = "Opponent's Turn! Waiting for their move...";
//                    }
//                }
//            }
//
//            if (enemyThrow) {
//                enemyTimer += delta;
//                if (enemyTimer > 0.12f) {
//                    enemyTimer = 0;
//                    if (enemyFrame < 5) enemyFrame++;
//                }
//                if (enemyFrame >= 3) {
//                    enemyBallX -= 580 * delta;
//                    enemyBallY -= 100 * delta;
//                }
//
//                if (enemyBallX <= 650 && !isEnemyPokemonSummoned) {
//                    isEnemyPokemonSummoned = true;
//                    enemyPokemonX = 650;
//                    enemyPokemonY = 260;
//                    enemyThrow = false;
//
//                    isMyTurn = true;
//                    statusMessage = "Your Turn! Attack or Choose next action!";
//                }
//            }
//
//            if (isMyPokemonSummoned && myActivePokemonFrames.size > 0) {
//                myPokemonAnimationTimer += delta;
//                if (myPokemonAnimationTimer > 0.08f) {
//                    myPokemonAnimationTimer = 0;
//                    myPokemonCurrentFrame = (myPokemonCurrentFrame + 1) % myActivePokemonFrames.size;
//                }
//            }
//            if (isEnemyPokemonSummoned && enemyActivePokemonFrames.size > 0) {
//                enemyPokemonAnimationTimer += delta;
//                if (enemyPokemonAnimationTimer > 0.08f) {
//                    enemyPokemonAnimationTimer = 0;
//                    enemyPokemonCurrentFrame = (enemyPokemonCurrentFrame + 1) % enemyActivePokemonFrames.size;
//                }
//            }
//        }
//
//        // ========================================================
//        // 🎨 RENDER LAYER
//        // ========================================================
//        game.batch.begin();
//        game.batch.draw(grassBg, 0, 0, screenWidth, screenHeight);
//
//        if (currentState == BattleState.SELECTION_MODE) {
//            uiFont.draw(game.batch, "=== MY CAUGHT POKEMON (FROM LIVE DB) ===", screenWidth / 2 - 210, screenHeight - 180);
//
//            if(myPokemons.isEmpty()) {
//                uiFont.draw(game.batch, "⚠️ No caught pokemon found in database for you!", screenWidth / 2 - 170, screenHeight - 240);
//            } else {
//                for (int i = 0; i < myPokemons.size(); i++) {
//                    String mark = (selectedPokemonIndex == i) ? "👉 [READY] " : "   [" + (i + 1) + "] ";
//                    uiFont.draw(game.batch, mark + myPokemons.get(i).name.toUpperCase() + " (HP: " + myPokemons.get(i).hp + ")", screenWidth / 2 - 140, screenHeight - 240 - (i * 45));
//                }
//            }
//        }
//        else if (currentState == BattleState.BATTLE_MODE) {
//            game.batch.draw(playerFrames[playerFrame], 30, 40, 220, 150);
//            game.batch.draw(enemyFrames[enemyFrame], screenWidth - 160, 280, 120, 170);
//
//            if (playerName != null) font.draw(game.batch, playerName, 40, 230);
//            if (enemyName != null) font.draw(game.batch, enemyName, screenWidth - 160, 480);
//
//            if (playerThrow && playerFrame >= 2) {
//                game.batch.draw(pokeball, playerBallX, playerBallY, 45, 45);
//            }
//            if (isMyPokemonSummoned && myActivePokemonFrames.size > 0) {
//                game.batch.draw(myActivePokemonFrames.get(myPokemonCurrentFrame), myPokemonX, myPokemonY, 150, 150);
//            }
//
//            if (enemyThrow && enemyFrame >= 3) {
//                game.batch.draw(pokeball, enemyBallX, enemyBallY, 50, 50);
//            }
//            if (isEnemyPokemonSummoned && enemyActivePokemonFrames.size > 0) {
//                game.batch.draw(enemyActivePokemonFrames.get(enemyPokemonCurrentFrame), enemyPokemonX, enemyPokemonY, 150, 150);
//            }
//
//            uiFont.draw(game.batch, "MY FIELD: " + myPokemons.get(selectedPokemonIndex).name.toUpperCase(), 40, screenHeight - 40);
//            if (isEnemyPokemonSummoned) {
//                uiFont.draw(game.batch, "ENEMY FIELD: " + activeEnemyPokemonName.toUpperCase(), screenWidth - 280, screenHeight - 40);
//            }
//        }
//
//        font.draw(game.batch, statusMessage, screenWidth / 2 - 280, 60);
//        game.batch.end();
//    }
//
//    @Override public void resize(int width, int height) {}
//    @Override public void pause() {}
//    @Override public void resume() {}
//    @Override public void hide() {}
//
//    @Override
//    public void dispose() {
//        grassBg.dispose();
//        pokeball.dispose();
//        font.dispose();
//        uiFont.dispose();
//        for (Texture t : enemyFrames) if (t != null) t.dispose();
//        for (Texture t : playerFrames) if (t != null) t.dispose();
//        for (Texture t : myActivePokemonFrames) if (t != null) t.dispose();
//        for (Texture t : enemyActivePokemonFrames) if (t != null) t.dispose();
//    }
//}
//n
package com.codeexplorer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.codeexplorer.Main;
import com.codeexplorer.model.CurrentPlayer;
import com.codeexplorer.network.GameSocket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class NewBattleScreen implements Screen {

    private final Main game;
    private Texture grassBg;
    private Texture pokeball;
    private BitmapFont font;
    private BitmapFont uiFont;

    private String playerName;
    private String enemyName;
    private Long enemyId;

    // Enemy Trainer (Right Side)
    private Texture[] enemyFrames;
    private int enemyFrame;
    private boolean enemyThrow;
    private float enemyTimer;
    private float enemyBallX;
    private float enemyBallY;

    // Player Trainer (Left Side)
    private Texture[] playerFrames;
    private int playerFrame;
    private boolean playerThrow;
    private float playerTimer;
    private float playerBallX;
    private float playerBallY;

    private boolean isMyTurn;

    private enum BattleState {
        SELECTION_MODE,
        BATTLE_MODE
    }
    private BattleState currentState = BattleState.SELECTION_MODE;

    // ⚔️ POKEMON DATA & LIVE ANIMATION TRACKER
    private ArrayList<PokemonModel> myPokemons;
    private int selectedPokemonIndex = -1;
    private String activeEnemyPokemonName = "";

    // 📊 লাইভ এইচপি (HP) ট্র্যাকিং ভেরিয়েবলস
    private int myPokemonHp = 100;
    private int enemyPokemonHp = 100;

    private Array<Texture> myActivePokemonFrames;
    private Array<Texture> enemyActivePokemonFrames;
    private int myPokemonCurrentFrame = 0;
    private int enemyPokemonCurrentFrame = 0;
    private float myPokemonAnimationTimer = 0;
    private float enemyPokemonAnimationTimer = 0;

    private boolean isMyPokemonSummoned = false;
    private boolean isEnemyPokemonSummoned = false;

    private float myPokemonX, myPokemonY;
    private float enemyPokemonX, enemyPokemonY;
    private String statusMessage = "👉 [SELECTION] Press number keys to select your Caught Pokemon, then press ENTER!";

    // ⚡️ অ্যাটাক ভিজ্যুয়াল অ্যানিমেশন ট্র্যাক করার ভেরিয়েবলস
    private Texture[] attackFrames;
    private boolean isAnimatingAttack = false;
    private float attackAnimationTimer = 0f;
    private int currentAttackFrameIndex = 0;
    private final float attackFrameDuration = 0.07f;
    private float attackVisualX = 0;
    private float attackVisualY = 0;

    public static class PokemonModel {
        public int id;
        public String name;
        public int hp;

        public PokemonModel(int id, String name, int hp) {
            this.id = id;
            this.name = name;
            this.hp = hp;
        }
    }

    public NewBattleScreen(Main game, String playerName, String enemyName, Long enemyId) {
        this.game = game;
        this.playerName = playerName != null ? playerName : "PLAYER";
        this.enemyName = enemyName != null ? enemyName : "ENEMY";
        this.enemyId = enemyId;

        grassBg = new Texture("battle/background/grass.png");
        pokeball = new Texture("battle/pokeball/pokeball.png");

        font = new BitmapFont();
        font.getData().setScale(1.5f);

        uiFont = new BitmapFont();
        uiFont.getData().setScale(1.4f);

        if (CurrentPlayer.id != null && enemyId != null) {
            this.isMyTurn = CurrentPlayer.id < enemyId;
        } else {
            this.isMyTurn = true;
        }

        myPokemons = new ArrayList<>();
        myActivePokemonFrames = new Array<>();
        enemyActivePokemonFrames = new Array<>();

        loadPokemonDataFromDatabase();

        // Trainer Animations
        enemyFrames = new Texture[6];
        enemyFrames[0] = new Texture("Trainer/battlegirl/sprite_0002.png");
        enemyFrames[1] = new Texture("Trainer/battlegirl/sprite_0003.png");
        enemyFrames[2] = new Texture("Trainer/battlegirl/sprite_0000.png");
        enemyFrames[3] = new Texture("Trainer/battlegirl/sprite_0001.png");
        enemyFrames[4] = new Texture("Trainer/battlegirl/sprite_0004.png");
        enemyFrames[5] = new Texture("Trainer/battlegirl/sprite_0005.png");

        playerFrames = new Texture[5];
        playerFrames[0] = new Texture("Trainer/playerr/sprite_0004.png");
        playerFrames[1] = new Texture("Trainer/playerr/sprite_0002.png");
        playerFrames[2] = new Texture("Trainer/playerr/sprite_0001.png");
        playerFrames[3] = new Texture("Trainer/playerr/sprite_0000.png");
        playerFrames[4] = new Texture("Trainer/playerr/sprite_0003.png");

        // লাইটনিং অ্যাটাক স্প্রাইট ফ্রেম লোড করা হচ্ছে
        attackFrames = new Texture[10];
        try {
            for (int i = 0; i < 10; i++) {
                String path = "battle/attack/sprite_" + String.format("%04d", i) + ".png";
                attackFrames[i] = new Texture(Gdx.files.internal(path));
            }
        } catch (Exception e) {
            System.err.println("❌ Attack assets mapping error: " + e.getMessage());
        }

        resetAnimation();
        GameSocket.enemyThrow = false;

        // সকেট লিসেনারে এই স্ক্রিনটিকে রেজিস্টার করে রাখা হচ্ছে (যদি আপনার সকেটে এই আর্কিটেকচার থাকে)
        GameSocket.setActiveScreen(this);
    }

    private String getPokemonNameById(int pokemonId) {
        switch (pokemonId) {
            case 1: return "Charmander";
            case 2: return "Greninja";
            case 3: return "Bulbasaur";
            case 4: return "Pikachu";
            default: return "Pokemon";
        }
    }

    private void loadPokemonDataFromDatabase() {
        myPokemons.clear();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Gdx.app.log("Database_Error", "Driver Class missing!");
        }

        String url = "jdbc:mysql://localhost:3306/pokemongame?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "";

        String query = "SELECT pokemon_id, current_hp FROM player_pokemon WHERE player_id = ?";
        long currentPlayerId = (CurrentPlayer.id != null) ? CurrentPlayer.id : 21;

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, currentPlayerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int pId = rs.getInt("pokemon_id");
                int hp = rs.getInt("current_hp");
                myPokemons.add(new PokemonModel(pId, getPokemonNameById(pId), hp));
            }
            rs.close(); stmt.close(); conn.close();
        } catch (Exception e) {
            if (myPokemons.isEmpty()) {
                myPokemons.add(new PokemonModel(4, "Pikachu", 100));
                myPokemons.add(new PokemonModel(2, "Greninja", 100));
            }
        }
    }

    private void loadPokemonAnimationFrames(String pokemonName, boolean isPlayer) {
        if (isPlayer) {
            for (Texture t : myActivePokemonFrames) t.dispose();
            myActivePokemonFrames.clear();
            myPokemonCurrentFrame = 0;
        } else {
            for (Texture t : enemyActivePokemonFrames) t.dispose();
            enemyActivePokemonFrames.clear();
            enemyPokemonCurrentFrame = 0;
        }

        try {
            String folder = pokemonName.toLowerCase().trim();
            int frameCount = folder.equals("pikachu") ? 30 : folder.equals("bulbasaur") ? 4 : folder.equals("charmander") ? 15 : folder.equals("greninja") ? 38 : 0;

            for (int i = 0; i <= frameCount; i++) {
                String file = folder.equals("pikachu") ? String.format("pikachu/sprite_%04d.png", i)
                    : folder.equals("bulbasaur") ? "bulbasaur/frame_" + i + "_delay-0.1s.png"
                    : folder.equals("charmander") ? String.format("charmander/sprite_%04d.png", i)
                    : String.format("greninja/frame_%02d_delay-0.04s.png", i);

                if (isPlayer) myActivePokemonFrames.add(new Texture(file));
                else enemyActivePokemonFrames.add(new Texture(file));
            }
        } catch (Exception e) {
            Gdx.app.log("NewBattleScreen", "Missing Assets for: " + pokemonName);
        }
    }

    /**
     * 📡 সকেট নেটওয়ার্ক থেকে রিসিভ করা ড্যামেজ বা অ্যাটাক মেসেজ হ্যান্ডল করার মেথড
     */
    public void processSocketMessage(String rawJson) {
        Gdx.app.postRunnable(() -> {
            try {
                JsonValue json = new JsonReader().parse(rawJson);
                String action = json.getString("action", "");

                // অপোনেন্ট আক্রমণ করলে
                if (action.equals("opponent_attack") || action.equals("battle_move")) {
                    String moveName = json.getString("move", "Attack");
                    int damage = json.has("damage") ? json.getInt("damage") : 20;

                    this.myPokemonHp = Math.max(0, this.myPokemonHp - damage);

                    // প্লেয়ারের পোকেমনের ওপর লাইটনিং অ্যানিমেশন ট্রিগার হবে
                    if (isMyPokemonSummoned) {
                        playAttackAnimation(myPokemonX, myPokemonY);
                    }

                    statusMessage = enemyName + "'s " + activeEnemyPokemonName + " used " + moveName + " (-" + damage + " HP)! Your Turn.";
                    isMyTurn = true;
                }
            } catch (Exception e) {
                System.err.println("JSON Parsing Failure: " + e.getMessage());
            }
        });
    }

    private void playAttackAnimation(float targetX, float targetY) {
        this.attackVisualX = targetX - 50;
        this.attackVisualY = targetY;
        this.isAnimatingAttack = true;
        this.attackAnimationTimer = 0f;
        this.currentAttackFrameIndex = 0;
    }

    private void resetAnimation() {
        enemyFrame = 0; enemyThrow = false; enemyTimer = 0;
        enemyBallX = Gdx.graphics.getWidth() - 150; enemyBallY = 320;
        playerThrow = false; playerFrame = 0; playerTimer = 0;
        playerBallX = 220; playerBallY = 120;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // ========================================================
        // 🔄 STATE 1: SELECTION MODE
        // ========================================================
        if (currentState == BattleState.SELECTION_MODE) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && myPokemons.size() > 0) selectedPokemonIndex = 0;
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && myPokemons.size() > 1) selectedPokemonIndex = 1;
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) && myPokemons.size() > 2) selectedPokemonIndex = 2;
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) && myPokemons.size() > 3) selectedPokemonIndex = 3;
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5) && myPokemons.size() > 4) selectedPokemonIndex = 4;
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6) && myPokemons.size() > 5) selectedPokemonIndex = 5;

            if (selectedPokemonIndex != -1) {
                myPokemonHp = myPokemons.get(selectedPokemonIndex).hp;
                statusMessage = "🎯 SELECTED: " + myPokemons.get(selectedPokemonIndex).name.toUpperCase() + "! Press ENTER to Lock!";
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                if (selectedPokemonIndex == -1) {
                    statusMessage = "❌ ERROR: Choose a Pokemon from your DB caught list first!";
                } else {
                    currentState = BattleState.BATTLE_MODE;
                    statusMessage = isMyTurn ? "⚔️ YOUR TURN! Press ENTER to THROW your Pokeball!" : "⚔️ OPPONENT'S TURN! Waiting for them to throw...";
                }
            }
        }

        // ========================================================
        // ⚔️ STATE 2: BATTLE MODE
        // ========================================================
        else if (currentState == BattleState.BATTLE_MODE) {

            // ⚡️ [MOVE & DAMAGE SYSTEM ENGINE]
            // দুটি পোকেমনই যখন মাঠে থাকবে এবং প্লেয়ারের টার্ন থাকবে, তখন SPACE চাপলে অ্যাটাক হবে
            if (isMyTurn && isMyPokemonSummoned && isEnemyPokemonSummoned && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                isMyTurn = false;
                int damageDealt = 25; // প্রতি মুভে ২৫ ড্যামেজ কমবে
                enemyPokemonHp = Math.max(0, enemyPokemonHp - damageDealt);

                statusMessage = "You used Lightning Bolt! (-" + damageDealt + " HP to Enemy). Waiting for " + enemyName + "...";

                // অপোনেন্টের ওপর ভিজ্যুয়াল লাইটনিং অ্যানিমেশন প্লে হবে
                playAttackAnimation(enemyPokemonX, enemyPokemonY);

                // সকেটে অ্যাটাক অ্যাকশন ডাটা এমিট করা হচ্ছে
                String attackPayload = "{"
                    + "\"action\":\"battle_move\","
                    + "\"senderId\":" + CurrentPlayer.id + ","
                    + "\"receiverId\":" + enemyId + ","
                    + "\"move\":\"Lightning Bolt\","
                    + "\"damage\":" + damageDealt
                    + "}";
                GameSocket.emit(attackPayload);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                if (isMyTurn && !playerThrow && !enemyThrow && !isMyPokemonSummoned) {
                    playerThrow = true;
                    playerFrame = 0; playerTimer = 0;
                    playerBallX = 220; playerBallY = 120;

                    String myChosenPokemon = myPokemons.get(selectedPokemonIndex).name;
                    loadPokemonAnimationFrames(myChosenPokemon, true);

                    try {
                        GameSocket.sendThrow(myChosenPokemon);
                    } catch (Exception e) {
                        try { GameSocket.sendThrow(); } catch (Exception ex) {}
                    }
                    statusMessage = "You threw " + myChosenPokemon.toUpperCase() + "!";
                }
            }

            if (GameSocket.enemyThrow) {
                if (!playerThrow && !enemyThrow && !isEnemyPokemonSummoned) {
                    enemyThrow = true;
                    enemyFrame = 0; enemyTimer = 0;
                    enemyBallX = screenWidth - 160; enemyBallY = 320;

                    activeEnemyPokemonName = (GameSocket.enemyPokemonName != null && !GameSocket.enemyPokemonName.isEmpty()) ? GameSocket.enemyPokemonName : "Pikachu";
                    loadPokemonAnimationFrames(activeEnemyPokemonName, false);
                    statusMessage = "Opponent threw " + activeEnemyPokemonName.toUpperCase() + "!";
                }
                GameSocket.enemyThrow = false;
            }

            // বল ট্র্যাকিং এনিমেশন (প্লেয়ার)
            if (playerThrow) {
                playerTimer += delta;
                if (playerTimer > 0.12f) { playerTimer = 0; if (playerFrame < 4) playerFrame++; }
                if (playerFrame >= 2) { playerBallX += 580 * delta; playerBallY += 120 * delta; }

                if (playerBallX >= 400 && !isMyPokemonSummoned) {
                    isMyPokemonSummoned = true;
                    myPokemonX = 400; myPokemonY = 160;
                    playerThrow = false;

                    if (!isEnemyPokemonSummoned) {
                        isMyTurn = false;
                        statusMessage = "Opponent's Turn! Waiting for their move...";
                    }
                }
            }

            // বল ট্র্যাকিং এনিমেশন (অপোনেন্ট)
            if (enemyThrow) {
                enemyTimer += delta;
                if (enemyTimer > 0.12f) { enemyTimer = 0; if (enemyFrame < 5) enemyFrame++; }
                if (enemyFrame >= 3) { enemyBallX -= 580 * delta; enemyBallY -= 100 * delta; }

                if (enemyBallX <= 650 && !isEnemyPokemonSummoned) {
                    isEnemyPokemonSummoned = true;
                    enemyPokemonX = 650; enemyPokemonY = 260;
                    enemyThrow = false;

                    isMyTurn = true;
                    statusMessage = "Your Turn! Press SPACE to use Lightning Bolt!";
                }
            }

            // পোকেমন ফ্রেম আপডেটার লুপ
            if (isMyPokemonSummoned && myActivePokemonFrames.size > 0) {
                myPokemonAnimationTimer += delta;
                if (myPokemonAnimationTimer > 0.08f) {
                    myPokemonAnimationTimer = 0;
                    myPokemonCurrentFrame = (myPokemonCurrentFrame + 1) % myActivePokemonFrames.size;
                }
            }
            if (isEnemyPokemonSummoned && enemyActivePokemonFrames.size > 0) {
                enemyPokemonAnimationTimer += delta;
                if (enemyPokemonAnimationTimer > 0.08f) {
                    enemyPokemonAnimationTimer = 0;
                    enemyPokemonCurrentFrame = (enemyPokemonCurrentFrame + 1) % enemyActivePokemonFrames.size;
                }
            }

            // লাইটনিং অ্যাটাক টাইমার আপডেট
            if (isAnimatingAttack) {
                attackAnimationTimer += delta;
                if (attackAnimationTimer >= attackFrameDuration) {
                    attackAnimationTimer = 0f;
                    currentAttackFrameIndex++;
                    if (currentAttackFrameIndex >= attackFrames.length) {
                        isAnimatingAttack = false;
                    }
                }
            }
        }

        // ========================================================
        // 🎨 RENDER LAYER
        // ========================================================
        game.batch.begin();
        game.batch.draw(grassBg, 0, 0, screenWidth, screenHeight);

        if (currentState == BattleState.SELECTION_MODE) {
            uiFont.draw(game.batch, "=== MY CAUGHT POKEMON (FROM LIVE DB) ===", screenWidth / 2 - 210, screenHeight - 180);

            if(myPokemons.isEmpty()) {
                uiFont.draw(game.batch, "⚠️ No caught pokemon found in database for you!", screenWidth / 2 - 170, screenHeight - 240);
            } else {
                for (int i = 0; i < myPokemons.size(); i++) {
                    String mark = (selectedPokemonIndex == i) ? "👉 [READY] " : "   [" + (i + 1) + "] ";
                    uiFont.draw(game.batch, mark + myPokemons.get(i).name.toUpperCase() + " (HP: " + myPokemons.get(i).hp + ")", screenWidth / 2 - 140, screenHeight - 240 - (i * 45));
                }
            }
        }
        else if (currentState == BattleState.BATTLE_MODE) {
            game.batch.draw(playerFrames[playerFrame], 30, 40, 220, 150);
            game.batch.draw(enemyFrames[enemyFrame], screenWidth - 160, 280, 120, 170);

            font.setColor(Color.WHITE);
            if (playerName != null) font.draw(game.batch, playerName, 40, 230);
            if (enemyName != null) font.draw(game.batch, enemyName, screenWidth - 160, 480);

            if (playerThrow && playerFrame >= 2) {
                game.batch.draw(pokeball, playerBallX, playerBallY, 45, 45);
            }

            // প্লেয়ারের পোকেমন ও লাইভ HP বার রেন্ডারিং
            if (isMyPokemonSummoned && myActivePokemonFrames.size > 0) {
                game.batch.draw(myActivePokemonFrames.get(myPokemonCurrentFrame), myPokemonX, myPokemonY, 150, 150);
                uiFont.setColor(Color.GREEN);
                uiFont.draw(game.batch, "HP: " + myPokemonHp + "/100", myPokemonX, myPokemonY + 180);
            }

            if (enemyThrow && enemyFrame >= 3) {
                game.batch.draw(pokeball, enemyBallX, enemyBallY, 50, 50);
            }

            // অপোনেন্টের পোকেমন ও লাইভ HP বার রেন্ডারিং
            if (isEnemyPokemonSummoned && enemyActivePokemonFrames.size > 0) {
                game.batch.draw(enemyActivePokemonFrames.get(enemyPokemonCurrentFrame), enemyPokemonX, enemyPokemonY, 150, 150);
                uiFont.setColor(Color.RED);
                uiFont.draw(game.batch, "HP: " + enemyPokemonHp + "/100", enemyPokemonX, enemyPokemonY + 180);
            }

            uiFont.setColor(Color.WHITE);
            uiFont.draw(game.batch, "MY FIELD: " + myPokemons.get(selectedPokemonIndex).name.toUpperCase(), 40, screenHeight - 40);
            if (isEnemyPokemonSummoned) {
                uiFont.draw(game.batch, "ENEMY FIELD: " + activeEnemyPokemonName.toUpperCase(), screenWidth - 280, screenHeight - 40);
            }

            // ⚡️ লাইটনিং অ্যাটাক স্পার্ক রেন্ডারিং লেয়ার
            if (isAnimatingAttack && attackFrames[currentAttackFrameIndex] != null) {
                game.batch.draw(attackFrames[currentAttackFrameIndex], attackVisualX, attackVisualY, 250, 200);
            }
        }

        font.setColor(Color.YELLOW);
        font.draw(game.batch, statusMessage, screenWidth / 2 - 280, 60);
        game.batch.end();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { GameSocket.setActiveScreen(null); }

    @Override
    public void dispose() {
        grassBg.dispose();
        pokeball.dispose();
        font.dispose();
        uiFont.dispose();
        for (Texture t : enemyFrames) if (t != null) t.dispose();
        for (Texture t : playerFrames) if (t != null) t.dispose();
        for (Texture t : myActivePokemonFrames) if (t != null) t.dispose();
        for (Texture t : enemyActivePokemonFrames) if (t != null) t.dispose();
        if (attackFrames != null) {
            for (Texture texture : attackFrames) {
                if (texture != null) texture.dispose();
            }
        }
    }
}
