////package com.codeexplorer.screen;
////
////import com.badlogic.gdx.Gdx;
////import com.badlogic.gdx.Input;
////import com.badlogic.gdx.Screen;
////import com.badlogic.gdx.graphics.GL20;
////import com.badlogic.gdx.graphics.Texture;
////import com.badlogic.gdx.graphics.g2d.BitmapFont;
////import com.badlogic.gdx.graphics.Color;
////import com.badlogic.gdx.utils.Array;
////import com.badlogic.gdx.utils.JsonReader;
////import com.badlogic.gdx.utils.JsonValue;
////import com.codeexplorer.Main;
////import com.codeexplorer.model.CurrentPlayer;
////import com.codeexplorer.network.GameSocket;
////
////import java.util.ArrayList;
////
////public class PokemonBattleScreen implements Screen {
////
////    private final Main game;
////    private Texture grassBg;
////    private Texture pokeball;
////    private BitmapFont font;
////    private BitmapFont uiFont;
////
////    private String playerName;
////    private String enemyName;
////    private Long enemyId;
////
////    // Enemy Trainer (Right Side)
////    private Texture[] enemyFrames;
////    private int enemyFrame;
////    private boolean enemyThrow;
////    private float enemyTimer;
////    private float enemyBallX;
////    private float enemyBallY;
////
////    // Player Trainer (Left Side)
////    private Texture[] playerFrames;
////    private int playerFrame;
////    private boolean playerThrow;
////    private float playerTimer;
////    private float playerBallX;
////    private float playerBallY;
////
////    private boolean isMyTurn;
////
////    // ⚔️ POKEMON DATA & LIVE ANIMATION TRACKER
////    private ArrayList<PokemonShowScreen.PokemonModel> myPokemons;
////    private int selectedPokemonIndex;
////    private String activeEnemyPokemonName = "";
////
////    // 📊 লাইভ এইচপি (HP) ট্র্যাকিং ভেরিয়েবলস
////    private int myPokemonHp = 100;
////    private int enemyPokemonHp = 100;
////
////    private Array<Texture> myActivePokemonFrames;
////    private Array<Texture> enemyActivePokemonFrames;
////    private int myPokemonCurrentFrame = 0;
////    private int enemyPokemonCurrentFrame = 0;
////    private float myPokemonAnimationTimer = 0;
////    private float enemyPokemonAnimationTimer = 0;
////
////    private boolean isMyPokemonSummoned = false;
////    private boolean isEnemyPokemonSummoned = false;
////
////    private float myPokemonX, myPokemonY;
////    private float enemyPokemonX, enemyPokemonY;
////    private String statusMessage;
////
////    // ⚡️ অ্যাটাক ভিজ্যুয়াল অ্যানিমেশন ট্র্যাক করার ভেরিয়েবলস
////    private Texture[] attackFrames;
////    private boolean isAnimatingAttack = false;
////    private float attackAnimationTimer = 0f;
////    private int currentAttackFrameIndex = 0;
////    private final float attackFrameDuration = 0.07f;
////    private float attackVisualX = 0;
////    private float attackVisualY = 0;
////
////    public PokemonBattleScreen(Main game, String playerName, String enemyName, Long enemyId,
////                               ArrayList<PokemonShowScreen.PokemonModel> myPokemons, int selectedPokemonIndex) {
////        this.game = game;
////        this.playerName = playerName != null ? playerName : "PLAYER";
////        this.enemyName = enemyName != null ? enemyName : "ENEMY";
////        this.enemyId = enemyId;
////        this.myPokemons = myPokemons;
////        this.selectedPokemonIndex = selectedPokemonIndex;
////
////        grassBg = new Texture("battle/background/grass.png");
////        pokeball = new Texture("battle/pokeball/pokeball.png");
////
////        font = new BitmapFont();
////        font.getData().setScale(1.5f);
////
////        uiFont = new BitmapFont();
////        uiFont.getData().setScale(1.4f);
////
////        if (CurrentPlayer.id != null && enemyId != null) {
////            this.isMyTurn = CurrentPlayer.id < enemyId;
////        } else {
////            this.isMyTurn = true;
////        }
////
////        myActivePokemonFrames = new Array<>();
////        enemyActivePokemonFrames = new Array<>();
////
////        // শো স্ক্রিন থেকে আসা সিলেক্টেড পোকেমনের HP সেট করা হচ্ছে
////        if (selectedPokemonIndex != -1 && !myPokemons.isEmpty()) {
////            this.myPokemonHp = myPokemons.get(selectedPokemonIndex).hp;
////        }
////
////        this.statusMessage = isMyTurn ? "⚔️ YOUR TURN! Press ENTER to THROW your Pokeball!" : "⚔️ OPPONENT'S TURN! Waiting for them to throw...";
////
////        // Trainer Animations
////        enemyFrames = new Texture[6];
////        enemyFrames[0] = new Texture("Trainer/battlegirl/sprite_0002.png");
////        enemyFrames[1] = new Texture("Trainer/battlegirl/sprite_0003.png");
////        enemyFrames[2] = new Texture("Trainer/battlegirl/sprite_0000.png");
////        enemyFrames[3] = new Texture("Trainer/battlegirl/sprite_0001.png");
////        enemyFrames[4] = new Texture("Trainer/battlegirl/sprite_0004.png");
////        enemyFrames[5] = new Texture("Trainer/battlegirl/sprite_0005.png");
////
////        playerFrames = new Texture[5];
////        playerFrames[0] = new Texture("Trainer/playerr/sprite_0004.png");
////        playerFrames[1] = new Texture("Trainer/playerr/sprite_0002.png");
////        playerFrames[2] = new Texture("Trainer/playerr/sprite_0001.png");
////        playerFrames[3] = new Texture("Trainer/playerr/sprite_0000.png");
////        playerFrames[4] = new Texture("Trainer/playerr/sprite_0003.png");
////
////        // লাইটনিং অ্যাটাক স্প্রাইট ফ্রেম লোড করা হচ্ছে
////        attackFrames = new Texture[10];
////        try {
////            for (int i = 0; i < 10; i++) {
////                String path = "battle/attack/sprite_" + String.format("%04d", i) + ".png";
////                attackFrames[i] = new Texture(Gdx.files.internal(path));
////            }
////        } catch (Exception e) {
////            System.err.println("❌ Attack assets mapping error: " + e.getMessage());
////        }
////
////        resetAnimation();
////        GameSocket.enemyThrow = false;
////
////        // সকেট লিসেনারে এই স্ক্রিনটিকে রেজিস্টার করে রাখা হচ্ছে
////        GameSocket.setActiveScreen(this);
////    }
////
////    private void loadPokemonAnimationFrames(String pokemonName, boolean isPlayer) {
////        if (isPlayer) {
////            for (Texture t : myActivePokemonFrames) t.dispose();
////            myActivePokemonFrames.clear();
////            myPokemonCurrentFrame = 0;
////        } else {
////            for (Texture t : enemyActivePokemonFrames) t.dispose();
////            enemyActivePokemonFrames.clear();
////            enemyPokemonCurrentFrame = 0;
////        }
////
////        try {
////            String folder = pokemonName.toLowerCase().trim();
////            int frameCount = folder.equals("pikachu") ? 30 : folder.equals("bulbasaur") ? 4 : folder.equals("charmander") ? 15 : folder.equals("greninja") ? 38 : 0;
////
////            for (int i = 0; i <= frameCount; i++) {
////                String file = folder.equals("pikachu") ? String.format("pikachu/sprite_%04d.png", i)
////                    : folder.equals("bulbasaur") ? "bulbasaur/frame_" + i + "_delay-0.1s.png"
////                    : folder.equals("charmander") ? String.format("charmander/sprite_%04d.png", i)
////                    : String.format("greninja/frame_%02d_delay-0.04s.png", i);
////
////                if (isPlayer) myActivePokemonFrames.add(new Texture(file));
////                else enemyActivePokemonFrames.add(new Texture(file));
////            }
////        } catch (Exception e) {
////            Gdx.app.log("PokemonBattleScreen", "Missing Assets for: " + pokemonName);
////        }
////    }
////
////    public void processSocketMessage(String rawJson) {
////        Gdx.app.postRunnable(() -> {
////            try {
////                JsonValue json = new JsonReader().parse(rawJson);
////                String action = json.getString("action", "");
////
////                if (action.equals("opponent_attack") || action.equals("battle_move")) {
////                    String moveName = json.getString("move", "Attack");
////                    int damage = json.has("damage") ? json.getInt("damage") : 20;
////
////                    this.myPokemonHp = Math.max(0, this.myPokemonHp - damage);
////
////                    if (isMyPokemonSummoned) {
////                        playAttackAnimation(myPokemonX, myPokemonY);
////                    }
////
////                    statusMessage = enemyName + "'s " + activeEnemyPokemonName + " used " + moveName + " (-" + damage + " HP)! Your Turn.";
////                    isMyTurn = true;
////                }
////            } catch (Exception e) {
////                System.err.println("JSON Parsing Failure: " + e.getMessage());
////            }
////        });
////    }
////
////    private void playAttackAnimation(float targetX, float targetY) {
////        this.attackVisualX = targetX - 50;
////        this.attackVisualY = targetY;
////        this.isAnimatingAttack = true;
////        this.attackAnimationTimer = 0f;
////        this.currentAttackFrameIndex = 0;
////    }
////
////    private void resetAnimation() {
////        enemyFrame = 0; enemyThrow = false; enemyTimer = 0;
////        enemyBallX = Gdx.graphics.getWidth() - 150; enemyBallY = 320;
////        playerThrow = false; playerFrame = 0; playerTimer = 0;
////        playerBallX = 220; playerBallY = 120;
////    }
////
////    @Override
////    public void render(float delta) {
////        Gdx.gl.glClearColor(0, 0, 0, 1);
////        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
////
////        float screenWidth = Gdx.graphics.getWidth();
////        float screenHeight = Gdx.graphics.getHeight();
////
////        // ⚡️ [MOVE & DAMAGE SYSTEM ENGINE]
////        if (isMyTurn && isMyPokemonSummoned && isEnemyPokemonSummoned && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
////            isMyTurn = false;
////            int damageDealt = 25;
////            enemyPokemonHp = Math.max(0, enemyPokemonHp - damageDealt);
////
////            statusMessage = "You used Lightning Bolt! (-" + damageDealt + " HP to Enemy). Waiting for " + enemyName + "...";
////
////            playAttackAnimation(enemyPokemonX, enemyPokemonY);
////
////            String attackPayload = "{"
////                + "\"action\":\"battle_move\","
////                + "\"senderId\":" + CurrentPlayer.id + ","
////                + "\"receiverId\":" + enemyId + ","
////                + "\"move\":\"Lightning Bolt\","
////                + "\"damage\":" + damageDealt
////                + "}";
////            GameSocket.emit(attackPayload);
////        }
////
////        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
////            if (isMyTurn && !playerThrow && !enemyThrow && !isMyPokemonSummoned) {
////                playerThrow = true;
////                playerFrame = 0; playerTimer = 0;
////                playerBallX = 220; playerBallY = 120;
////
////                String myChosenPokemon = myPokemons.get(selectedPokemonIndex).name;
////                loadPokemonAnimationFrames(myChosenPokemon, true);
////
////                try {
////                    GameSocket.sendThrow(myChosenPokemon);
////                } catch (Exception e) {
////                    try { GameSocket.sendThrow(); } catch (Exception ex) {}
////                }
////                statusMessage = "You threw " + myChosenPokemon.toUpperCase() + "!";
////            }
////        }
////
////        if (GameSocket.enemyThrow) {
////            if (!playerThrow && !enemyThrow && !isEnemyPokemonSummoned) {
////                enemyThrow = true;
////                enemyFrame = 0; enemyTimer = 0;
////                enemyBallX = screenWidth - 160; enemyBallY = 320;
////
////                activeEnemyPokemonName = (GameSocket.enemyPokemonName != null && !GameSocket.enemyPokemonName.isEmpty()) ? GameSocket.enemyPokemonName : "Pikachu";
////                loadPokemonAnimationFrames(activeEnemyPokemonName, false);
////                statusMessage = "Opponent threw " + activeEnemyPokemonName.toUpperCase() + "!";
////            }
////            GameSocket.enemyThrow = false;
////        }
////
////        // বল ট্র্যাকিং এনিমেশন (প্লেয়ার)
////        if (playerThrow) {
////            playerTimer += delta;
////            if (playerTimer > 0.12f) { playerTimer = 0; if (playerFrame < 4) playerFrame++; }
////            if (playerFrame >= 2) { playerBallX += 580 * delta; playerBallY += 120 * delta; }
////
////            if (playerBallX >= 400 && !isMyPokemonSummoned) {
////                isMyPokemonSummoned = true;
////                myPokemonX = 400; myPokemonY = 160;
////                playerThrow = false;
////
////                if (!isEnemyPokemonSummoned) {
////                    isMyTurn = false;
////                    statusMessage = "Opponent's Turn! Waiting for their move...";
////                }
////            }
////        }
////
////        // বল ট্র্যাকিং এনিমেশন (অপোনেন্ট)
////        if (enemyThrow) {
////            enemyTimer += delta;
////            if (enemyTimer > 0.12f) { enemyTimer = 0; if (enemyFrame < 5) enemyFrame++; }
////            if (enemyFrame >= 3) { enemyBallX -= 580 * delta; enemyBallY -= 100 * delta; }
////
////            if (enemyBallX <= 650 && !isEnemyPokemonSummoned) {
////                isEnemyPokemonSummoned = true;
////                enemyPokemonX = 650; enemyPokemonY = 260;
////                enemyThrow = false;
////
////                isMyTurn = true;
////                statusMessage = "Your Turn! Press SPACE to use Lightning Bolt!";
////            }
////        }
////
////        // পোকেমন ফ্রেম আপডেটার লুপ
////        if (isMyPokemonSummoned && myActivePokemonFrames.size > 0) {
////            myPokemonAnimationTimer += delta;
////            if (myPokemonAnimationTimer > 0.08f) {
////                myPokemonAnimationTimer = 0;
////                myPokemonCurrentFrame = (myPokemonCurrentFrame + 1) % myActivePokemonFrames.size;
////            }
////        }
////        if (isEnemyPokemonSummoned && enemyActivePokemonFrames.size > 0) {
////            enemyPokemonAnimationTimer += delta;
////            if (enemyPokemonAnimationTimer > 0.08f) {
////                enemyPokemonAnimationTimer = 0;
////                enemyPokemonCurrentFrame = (enemyPokemonCurrentFrame + 1) % enemyActivePokemonFrames.size;
////            }
////        }
////
////        // লাইটনিং অ্যাটাক টাইমার আপডেট
////        if (isAnimatingAttack) {
////            attackAnimationTimer += delta;
////            if (attackAnimationTimer >= attackFrameDuration) {
////                attackAnimationTimer = 0f;
////                currentAttackFrameIndex++;
////                if (currentAttackFrameIndex >= attackFrames.length) {
////                    isAnimatingAttack = false;
////                }
////            }
////        }
////
////        // ========================================================
////        // 🎨 RENDER LAYER
////        // ========================================================
////        game.batch.begin();
////        game.batch.draw(grassBg, 0, 0, screenWidth, screenHeight);
////
////        game.batch.draw(playerFrames[playerFrame], 30, 40, 220, 150);
////        game.batch.draw(enemyFrames[enemyFrame], screenWidth - 160, 280, 120, 170);
////
////        font.setColor(Color.WHITE);
////        if (playerName != null) font.draw(game.batch, playerName, 40, 230);
////        if (enemyName != null) font.draw(game.batch, enemyName, screenWidth - 160, 480);
////
////        if (playerThrow && playerFrame >= 2) {
////            game.batch.draw(pokeball, playerBallX, playerBallY, 45, 45);
////        }
////
////        // প্লেয়ারের পোকেমন ও লাইভ HP বার রেন্ডারিং
////        if (isMyPokemonSummoned && myActivePokemonFrames.size > 0) {
////            game.batch.draw(myActivePokemonFrames.get(myPokemonCurrentFrame), myPokemonX, myPokemonY, 150, 150);
////            uiFont.setColor(Color.GREEN);
////            uiFont.draw(game.batch, "HP: " + myPokemonHp + "/100", myPokemonX, myPokemonY + 180);
////        }
////
////        if (enemyThrow && enemyFrame >= 3) {
////            game.batch.draw(pokeball, enemyBallX, enemyBallY, 50, 50);
////        }
////
////        // অপোনেন্টের পোকেমন ও লাইভ HP বার রেন্ডারিং
////        if (isEnemyPokemonSummoned && enemyActivePokemonFrames.size > 0) {
////            game.batch.draw(enemyActivePokemonFrames.get(enemyPokemonCurrentFrame), enemyPokemonX, enemyPokemonY, 150, 150);
////            uiFont.setColor(Color.RED);
////            uiFont.draw(game.batch, "HP: " + enemyPokemonHp + "/100", enemyPokemonX, enemyPokemonY + 180);
////        }
////
////        uiFont.setColor(Color.WHITE);
////        uiFont.draw(game.batch, "MY FIELD: " + myPokemons.get(selectedPokemonIndex).name.toUpperCase(), 40, screenHeight - 40);
////        if (isEnemyPokemonSummoned) {
////            uiFont.draw(game.batch, "ENEMY FIELD: " + activeEnemyPokemonName.toUpperCase(), screenWidth - 280, screenHeight - 40);
////        }
////
////        // ⚡️ লাইটনিং অ্যাটাক স্পার্ক রেন্ডারিং লেয়ার
////        if (isAnimatingAttack && attackFrames[currentAttackFrameIndex] != null) {
////            game.batch.draw(attackFrames[currentAttackFrameIndex], attackVisualX, attackVisualY, 250, 200);
////        }
////
////        font.setColor(Color.YELLOW);
////        font.draw(game.batch, statusMessage, screenWidth / 2 - 280, 60);
////        game.batch.end();
////    }
////
////    @Override public void show() {}
////    @Override public void resize(int width, int height) {}
////    @Override public void pause() {}
////    @Override public void resume() {}
////    @Override public void hide() { GameSocket.setActiveScreen(null); }
////
////    @Override
////    public void dispose() {
////        grassBg.dispose();
////        pokeball.dispose();
////        font.dispose();
////        uiFont.dispose();
////        for (Texture t : enemyFrames) if (t != null) t.dispose();
////        for (Texture t : playerFrames) if (t != null) t.dispose();
////        for (Texture t : myActivePokemonFrames) if (t != null) t.dispose();
////        for (Texture t : enemyActivePokemonFrames) if (t != null) t.dispose();
////        if (attackFrames != null) {
////            for (Texture texture : attackFrames) {
////                if (texture != null) texture.dispose();
////            }
////        }
////    }
////}
////check for back button
////package com.codeexplorer.screen;
////
////import com.badlogic.gdx.Gdx;
////import com.badlogic.gdx.Input;
////import com.badlogic.gdx.Screen;
////import com.badlogic.gdx.graphics.GL20;
////import com.badlogic.gdx.graphics.Texture;
////import com.badlogic.gdx.graphics.g2d.BitmapFont;
////import com.badlogic.gdx.graphics.Color;
////import com.badlogic.gdx.math.Rectangle;
////import com.badlogic.gdx.utils.Array;
////import com.badlogic.gdx.utils.JsonReader;
////import com.badlogic.gdx.utils.JsonValue;
////import com.codeexplorer.Main;
////import com.codeexplorer.model.CurrentPlayer;
////import com.codeexplorer.network.GameSocket;
////
////import java.util.ArrayList;
////
////public class PokemonBattleScreen implements Screen {
////
////    private final Main game;
////    private Texture grassBg;
////    private Texture pokeball;
////    private BitmapFont font;
////    private BitmapFont uiFont;
////
////    private String playerName;
////    private String enemyName;
////    private Long enemyId;
////
////    // 🔙 [ব্যাক বাটন ভেরিয়েবলস]
////    private Texture backButtonTexture;
////    private Rectangle backButtonRect;
////
////    // Enemy Trainer (Right Side)
////    private Texture[] enemyFrames;
////    private int enemyFrame;
////    private boolean enemyThrow;
////    private float enemyTimer;
////    private float enemyBallX;
////    private float enemyBallY;
////
////    // Player Trainer (Left Side)
////    private Texture[] playerFrames;
////    private int playerFrame;
////    private boolean playerThrow;
////    private float playerTimer;
////    private float playerBallX;
////    private float playerBallY;
////
////    private boolean isMyTurn;
////
////    // ⚔️ POKEMON DATA & LIVE ANIMATION TRACKER
////    private ArrayList<PokemonShowScreen.PokemonModel> myPokemons;
////    private int selectedPokemonIndex;
////    private String activeEnemyPokemonName = "";
////
////    // 📊 লাইভ এইচপি (HP) ট্র্যাকিং ভেরিয়েবলস
////    private int myPokemonHp = 100;
////    private int enemyPokemonHp = 100;
////
////    private Array<Texture> myActivePokemonFrames;
////    private Array<Texture> enemyActivePokemonFrames;
////    private int myPokemonCurrentFrame = 0;
////    private int enemyPokemonCurrentFrame = 0;
////    private float myPokemonAnimationTimer = 0;
////    private float enemyPokemonAnimationTimer = 0;
////
////    private boolean isMyPokemonSummoned = false;
////    private boolean isEnemyPokemonSummoned = false;
////
////    private float myPokemonX, myPokemonY;
////    private float enemyPokemonX, enemyPokemonY;
////    private String statusMessage;
////
////    // ⚡️ অ্যাটাক ভিজ্যুয়াল অ্যানিমেশন ট্র্যাক করার ভেরিয়েবলস
////    private Texture[] attackFrames;
////    private boolean isAnimatingAttack = false;
////    private float attackAnimationTimer = 0f;
////    private int currentAttackFrameIndex = 0;
////    private final float attackFrameDuration = 0.07f;
////    private float attackVisualX = 0;
////    private float attackVisualY = 0;
////
////    public PokemonBattleScreen(Main game, String playerName, String enemyName, Long enemyId,
////                               ArrayList<PokemonShowScreen.PokemonModel> myPokemons, int selectedPokemonIndex) {
////        this.game = game;
////        this.playerName = playerName != null ? playerName : "PLAYER";
////        this.enemyName = enemyName != null ? enemyName : "ENEMY";
////        this.enemyId = enemyId;
////        this.myPokemons = myPokemons;
////        this.selectedPokemonIndex = selectedPokemonIndex;
////
////        grassBg = new Texture("battle/background/grass.png");
////        pokeball = new Texture("battle/pokeball/pokeball.png");
////
////        // ব্যাক বাটন টেক্সচার লোড এবং সাইজ/পজিশন নির্ধারণ (স্ক্রিনের উপরে বাম কোণায়)
////        backButtonTexture = new Texture("button/back-button.png");
////        backButtonRect = new Rectangle(20, Gdx.graphics.getHeight() - 70, 50, 50);
////
////        font = new BitmapFont();
////        font.getData().setScale(1.5f);
////
////        uiFont = new BitmapFont();
////        uiFont.getData().setScale(1.4f);
////
////        if (CurrentPlayer.id != null && enemyId != null) {
////            this.isMyTurn = CurrentPlayer.id < enemyId;
////        } else {
////            this.isMyTurn = true;
////        }
////
////        myActivePokemonFrames = new Array<>();
////        enemyActivePokemonFrames = new Array<>();
////
////        // শো স্ক্রিন থেকে আসা সিলেক্টেড পোকেমনের HP সেট করা হচ্ছে
////        if (selectedPokemonIndex != -1 && !myPokemons.isEmpty()) {
////            this.myPokemonHp = myPokemons.get(selectedPokemonIndex).hp;
////        }
////
////        this.statusMessage = isMyTurn ? "⚔️ YOUR TURN! Press ENTER to THROW your Pokeball!" : "⚔️ OPPONENT'S TURN! Waiting for them to throw...";
////
////        // Trainer Animations
////        enemyFrames = new Texture[6];
////        enemyFrames[0] = new Texture("Trainer/battlegirl/sprite_0002.png");
////        enemyFrames[1] = new Texture("Trainer/battlegirl/sprite_0003.png");
////        enemyFrames[2] = new Texture("Trainer/battlegirl/sprite_0000.png");
////        enemyFrames[3] = new Texture("Trainer/battlegirl/sprite_0001.png");
////        enemyFrames[4] = new Texture("Trainer/battlegirl/sprite_0004.png");
////        enemyFrames[5] = new Texture("Trainer/battlegirl/sprite_0005.png");
////
////        playerFrames = new Texture[5];
////        playerFrames[0] = new Texture("Trainer/playerr/sprite_0004.png");
////        playerFrames[1] = new Texture("Trainer/playerr/sprite_0002.png");
////        playerFrames[2] = new Texture("Trainer/playerr/sprite_0001.png");
////        playerFrames[3] = new Texture("Trainer/playerr/sprite_0000.png");
////        playerFrames[4] = new Texture("Trainer/playerr/sprite_0003.png");
////
////        // লাইটনিং অ্যাটাক স্প্রাইট ফ্রেম লোড করা হচ্ছে
////        attackFrames = new Texture[10];
////        try {
////            for (int i = 0; i < 10; i++) {
////                String path = "battle/attack/sprite_" + String.format("%04d", i) + ".png";
////                attackFrames[i] = new Texture(Gdx.files.internal(path));
////            }
////        } catch (Exception e) {
////            System.err.println("❌ Attack assets mapping error: " + e.getMessage());
////        }
////
////        resetAnimation();
////        GameSocket.enemyThrow = false;
////
////        // সকেট লিসেনারে এই স্ক্রিনটিকে রেজিস্টার করে রাখা হচ্ছে
////        GameSocket.setActiveScreen(this);
////    }
////
////    private void loadPokemonAnimationFrames(String pokemonName, boolean isPlayer) {
////        if (isPlayer) {
////            for (Texture t : myActivePokemonFrames) t.dispose();
////            myActivePokemonFrames.clear();
////            myPokemonCurrentFrame = 0;
////        } else {
////            for (Texture t : enemyActivePokemonFrames) t.dispose();
////            enemyActivePokemonFrames.clear();
////            enemyPokemonCurrentFrame = 0;
////        }
////
////        try {
////            String folder = pokemonName.toLowerCase().trim();
////            int frameCount = folder.equals("pikachu") ? 30 : folder.equals("bulbasaur") ? 4 : folder.equals("charmander") ? 15 : folder.equals("greninja") ? 38 : 0;
////
////            for (int i = 0; i <= frameCount; i++) {
////                String file = folder.equals("pikachu") ? String.format("pikachu/sprite_%04d.png", i)
////                    : folder.equals("bulbasaur") ? "bulbasaur/frame_" + i + "_delay-0.1s.png"
////                    : folder.equals("charmander") ? String.format("charmander/sprite_%04d.png", i)
////                    : String.format("greninja/frame_%02d_delay-0.04s.png", i);
////
////                if (isPlayer) myActivePokemonFrames.add(new Texture(file));
////                else enemyActivePokemonFrames.add(new Texture(file));
////            }
////        } catch (Exception e) {
////            Gdx.app.log("PokemonBattleScreen", "Missing Assets for: " + pokemonName);
////        }
////    }
////
////    public void processSocketMessage(String rawJson) {
////        Gdx.app.postRunnable(() -> {
////            try {
////                JsonValue json = new JsonReader().parse(rawJson);
////                String action = json.getString("action", "");
////
////                if (action.equals("opponent_attack") || action.equals("battle_move")) {
////                    String moveName = json.getString("move", "Attack");
////                    int damage = json.has("damage") ? json.getInt("damage") : 20;
////
////                    this.myPokemonHp = Math.max(0, this.myPokemonHp - damage);
////
////                    if (isMyPokemonSummoned) {
////                        playAttackAnimation(myPokemonX, myPokemonY);
////                    }
////
////                    statusMessage = enemyName + "'s " + activeEnemyPokemonName + " used " + moveName + " (-" + damage + " HP)! Your Turn.";
////                    isMyTurn = true;
////                }
////            } catch (Exception e) {
////                System.err.println("JSON Parsing Failure: " + e.getMessage());
////            }
////        });
////    }
////
////    private void playAttackAnimation(float targetX, float targetY) {
////        this.attackVisualX = targetX - 50;
////        this.attackVisualY = targetY;
////        this.isAnimatingAttack = true;
////        this.attackAnimationTimer = 0f;
////        this.currentAttackFrameIndex = 0;
////    }
////
////    private void resetAnimation() {
////        enemyFrame = 0; enemyThrow = false; enemyTimer = 0;
////        enemyBallX = Gdx.graphics.getWidth() - 150; enemyBallY = 320;
////        playerThrow = false; playerFrame = 0; playerTimer = 0;
////        playerBallX = 220; playerBallY = 120;
////    }
////
////    @Override
////    public void render(float delta) {
////        // 🔙 [ব্যাক বাটন ইনপুট হ্যান্ডলিং]
////        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
////            float mouseX = Gdx.input.getX();
////            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // LibGDX Y-axis inversion
////
////            if (backButtonRect.contains(mouseX, mouseY)) {
////                game.setScreen(new PokemonShowScreen(game, playerName, enemyName, enemyId));
////                return;
////            }
////        }
////
////        Gdx.gl.glClearColor(0, 0, 0, 1);
////        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
////
////        float screenWidth = Gdx.graphics.getWidth();
////        float screenHeight = Gdx.graphics.getHeight();
////
////        // ⚡️ [MOVE & DAMAGE SYSTEM ENGINE]
////        if (isMyTurn && isMyPokemonSummoned && isEnemyPokemonSummoned && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
////            isMyTurn = false;
////            int damageDealt = 25;
////            enemyPokemonHp = Math.max(0, enemyPokemonHp - damageDealt);
////
////            statusMessage = "You used Lightning Bolt! (-" + damageDealt + " HP to Enemy). Waiting for " + enemyName + "...";
////
////            playAttackAnimation(enemyPokemonX, enemyPokemonY);
////
////            String attackPayload = "{"
////                + "\"action\":\"battle_move\","
////                + "\"senderId\":" + CurrentPlayer.id + ","
////                + "\"receiverId\":" + enemyId + ","
////                + "\"move\":\"Lightning Bolt\","
////                + "\"damage\":" + damageDealt
////                + "}";
////            GameSocket.emit(attackPayload);
////        }
////
////        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
////            if (isMyTurn && !playerThrow && !enemyThrow && !isMyPokemonSummoned) {
////                playerThrow = true;
////                playerFrame = 0; playerTimer = 0;
////                playerBallX = 220; playerBallY = 120;
////
////                String myChosenPokemon = myPokemons.get(selectedPokemonIndex).name;
////                loadPokemonAnimationFrames(myChosenPokemon, true);
////
////                try {
////                    GameSocket.sendThrow(myChosenPokemon);
////                } catch (Exception e) {
////                    try { GameSocket.sendThrow(); } catch (Exception ex) {}
////                }
////                statusMessage = "You threw " + myChosenPokemon.toUpperCase() + "!";
////            }
////        }
////
////        if (GameSocket.enemyThrow) {
////            if (!playerThrow && !enemyThrow && !isEnemyPokemonSummoned) {
////                enemyThrow = true;
////                enemyFrame = 0; enemyTimer = 0;
////                enemyBallX = screenWidth - 160; enemyBallY = 320;
////
////                activeEnemyPokemonName = (GameSocket.enemyPokemonName != null && !GameSocket.enemyPokemonName.isEmpty()) ? GameSocket.enemyPokemonName : "Pikachu";
////                loadPokemonAnimationFrames(activeEnemyPokemonName, false);
////                statusMessage = "Opponent threw " + activeEnemyPokemonName.toUpperCase() + "!";
////            }
////            GameSocket.enemyThrow = false;
////        }
////
////        // বল ট্র্যাকিং এনিমেশন (প্লেয়ার)
////        if (playerThrow) {
////            playerTimer += delta;
////            if (playerTimer > 0.12f) { playerTimer = 0; if (playerFrame < 4) playerFrame++; }
////            if (playerFrame >= 2) { playerBallX += 580 * delta; playerBallY += 120 * delta; }
////
////            if (playerBallX >= 400 && !isMyPokemonSummoned) {
////                isMyPokemonSummoned = true;
////                myPokemonX = 400; myPokemonY = 160;
////                playerThrow = false;
////
////                if (!isEnemyPokemonSummoned) {
////                    isMyTurn = false;
////                    statusMessage = "Opponent's Turn! Waiting for their move...";
////                }
////            }
////        }
////
////        // বল ট্র্যাকিং এনিমেশন (অপোনেন্ট)
////        if (enemyThrow) {
////            enemyTimer += delta;
////            if (enemyTimer > 0.12f) { enemyTimer = 0; if (enemyFrame < 5) enemyFrame++; }
////            if (enemyFrame >= 3) { enemyBallX -= 580 * delta; enemyBallY -= 100 * delta; }
////
////            if (enemyBallX <= 650 && !isEnemyPokemonSummoned) {
////                isEnemyPokemonSummoned = true;
////                enemyPokemonX = 650; enemyPokemonY = 260;
////                enemyThrow = false;
////
////                isMyTurn = true;
////                statusMessage = "Your Turn! Press SPACE to use Lightning Bolt!";
////            }
////        }
////
////        // পোকেমন ফ্রেম আপডেটার লুপ
////        if (isMyPokemonSummoned && myActivePokemonFrames.size > 0) {
////            myPokemonAnimationTimer += delta;
////            if (myPokemonAnimationTimer > 0.08f) {
////                myPokemonAnimationTimer = 0;
////                myPokemonCurrentFrame = (myPokemonCurrentFrame + 1) % myActivePokemonFrames.size;
////            }
////        }
////        if (isEnemyPokemonSummoned && enemyActivePokemonFrames.size > 0) {
////            enemyPokemonAnimationTimer += delta;
////            if (enemyPokemonAnimationTimer > 0.08f) {
////                enemyPokemonAnimationTimer = 0;
////                enemyPokemonCurrentFrame = (enemyPokemonCurrentFrame + 1) % enemyActivePokemonFrames.size;
////            }
////        }
////
////        // লাইটনিং অ্যাটাক টাইমার আপডেট
////        if (isAnimatingAttack) {
////            attackAnimationTimer += delta;
////            if (attackAnimationTimer >= attackFrameDuration) {
////                attackAnimationTimer = 0f;
////                currentAttackFrameIndex++;
////                if (currentAttackFrameIndex >= attackFrames.length) {
////                    isAnimatingAttack = false;
////                }
////            }
////        }
////
////        // ========================================================
////        // 🎨 RENDER LAYER
////        // ========================================================
////        game.batch.begin();
////        game.batch.draw(grassBg, 0, 0, screenWidth, screenHeight);
////
////        // 🔙 ব্যাক বাটন স্ক্রিনে রেন্ডার করা হচ্ছে
////        game.batch.draw(backButtonTexture, backButtonRect.x, backButtonRect.y, backButtonRect.width, backButtonRect.height);
////
////        game.batch.draw(playerFrames[playerFrame], 30, 40, 220, 150);
////        game.batch.draw(enemyFrames[enemyFrame], screenWidth - 160, 280, 120, 170);
////
////        font.setColor(Color.WHITE);
////        if (playerName != null) font.draw(game.batch, playerName, 40, 230);
////        if (enemyName != null) font.draw(game.batch, enemyName, screenWidth - 160, 480);
////
////        if (playerThrow && playerFrame >= 2) {
////            game.batch.draw(pokeball, playerBallX, playerBallY, 45, 45);
////        }
////
////        // প্লেয়ারের পোকেমন ও লাইভ HP বার রেন্ডারিং
////        if (isMyPokemonSummoned && myActivePokemonFrames.size > 0) {
////            game.batch.draw(myActivePokemonFrames.get(myPokemonCurrentFrame), myPokemonX, myPokemonY, 150, 150);
////            uiFont.setColor(Color.GREEN);
////            uiFont.draw(game.batch, "HP: " + myPokemonHp + "/100", myPokemonX, myPokemonY + 180);
////        }
////
////        if (enemyThrow && enemyFrame >= 3) {
////            game.batch.draw(pokeball, enemyBallX, enemyBallY, 50, 50);
////        }
////
////        // অপোনেন্টের পোকেমন ও লাইভ HP বার রেন্ডারিং
////        if (isEnemyPokemonSummoned && enemyActivePokemonFrames.size > 0) {
////            game.batch.draw(enemyActivePokemonFrames.get(enemyPokemonCurrentFrame), enemyPokemonX, enemyPokemonY, 150, 150);
////            uiFont.setColor(Color.RED);
////            uiFont.draw(game.batch, "HP: " + enemyPokemonHp + "/100", enemyPokemonX, enemyPokemonY + 180);
////        }
////
////        uiFont.setColor(Color.WHITE);
////        uiFont.draw(game.batch, "MY FIELD: " + myPokemons.get(selectedPokemonIndex).name.toUpperCase(), 40, screenHeight - 40);
////        if (isEnemyPokemonSummoned) {
////            uiFont.draw(game.batch, "ENEMY FIELD: " + activeEnemyPokemonName.toUpperCase(), screenWidth - 280, screenHeight - 40);
////        }
////
////        // ⚡️ লাইটনিং অ্যাটাক স্পার্ক রেন্ডারিং লেয়ার
////        if (isAnimatingAttack && attackFrames[currentAttackFrameIndex] != null) {
////            game.batch.draw(attackFrames[currentAttackFrameIndex], attackVisualX, attackVisualY, 250, 200);
////        }
////
////        font.setColor(Color.YELLOW);
////        font.draw(game.batch, statusMessage, screenWidth / 2 - 280, 60);
////        game.batch.end();
////    }
////
////    @Override public void show() {}
////    @Override public void resize(int width, int height) {
////        // স্ক্রিন রিসাইজ হলে ব্যাক বাটনের পজিশন ঠিক রাখা হচ্ছে
////        backButtonRect.y = height - 70;
////    }
////    @Override public void pause() {}
////    @Override public void resume() {}
////    @Override public void hide() { GameSocket.setActiveScreen(null); }
////
////    @Override
////    public void dispose() {
////        grassBg.dispose();
////        pokeball.dispose();
////        backButtonTexture.dispose();
////        font.dispose();
////        uiFont.dispose();
////        for (Texture t : enemyFrames) if (t != null) t.dispose();
////        for (Texture t : playerFrames) if (t != null) t.dispose();
////        for (Texture t : myActivePokemonFrames) if (t != null) t.dispose();
////        for (Texture t : enemyActivePokemonFrames) if (t != null) t.dispose();
////        if (attackFrames != null) {
////            for (Texture texture : attackFrames) {
////                if (texture != null) texture.dispose();
////            }
////        }
////    }
////}
////check for manager
//package com.codeexplorer.screen;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.utils.Array;
//import com.codeexplorer.Main;
//import com.codeexplorer.network.GameSocket;
//import com.codeexplorer.world.BattleManager;
//
//import java.util.ArrayList;
//
//public class PokemonBattleScreen implements Screen {
//
//    private final Main game;
//    private Texture grassBg;
//    private Texture pokeball;
//    private BitmapFont font;
//    private BitmapFont uiFont;
//    private BitmapFont announcementFont;
//
//    private Texture backButtonTexture;
//    private Rectangle backButtonRect;
//
//    private final BattleManager battleManager;
//
//    private Texture[] enemyFrames;
//    private Texture[] playerFrames;
//    private Texture[] attackFrames;
//
//    private ArrayList<PokemonShowScreen.PokemonModel> myPokemons;
//    private int selectedPokemonIndex;
//
//    private Array<Texture> myActivePokemonFrames;
//    private Array<Texture> enemyActivePokemonFrames;
//    private int myPokemonCurrentFrame = 0;
//    private int enemyPokemonCurrentFrame = 0;
//    private float myPokemonAnimationTimer = 0;
//    private float enemyPokemonAnimationTimer = 0;
//
//    public PokemonBattleScreen(Main game, String playerName, String enemyName, Long enemyId,
//                               ArrayList<PokemonShowScreen.PokemonModel> myPokemons, int selectedPokemonIndex) {
//        this.game = game;
//        this.myPokemons = myPokemons;
//        this.selectedPokemonIndex = selectedPokemonIndex;
//
//        this.battleManager = new BattleManager(playerName, enemyName, enemyId, myPokemons, selectedPokemonIndex);
//
//        grassBg = new Texture("battle/background/grass.png");
//        pokeball = new Texture("battle/pokeball/pokeball.png");
//
//        backButtonTexture = new Texture("button/back-button.png");
//        backButtonRect = new Rectangle(20, Gdx.graphics.getHeight() - 70, 50, 50);
//
//        font = new BitmapFont();
//        font.getData().setScale(1.5f);
//
//        uiFont = new BitmapFont();
//        uiFont.getData().setScale(1.4f);
//
//        announcementFont = new BitmapFont();
//        announcementFont.getData().setScale(2.0f);
//
//        myActivePokemonFrames = new Array<>();
//        enemyActivePokemonFrames = new Array<>();
//
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
//        // 📸 আপনার ফোল্ডারের ১০টি অ্যাটাক স্প্রাইট লোড হচ্ছে
//        attackFrames = new Texture[10];
//        try {
//            for (int i = 0; i < 10; i++) {
//                String path = "battle/attack/sprite_" + String.format("%04d", i) + ".png";
//                attackFrames[i] = new Texture(Gdx.files.internal(path));
//            }
//            System.out.println("✅ Attack Assets Registered into VM Array Buffer");
//        } catch (Exception e) {
//            System.err.println("❌ Mapping Exception: " + e.getMessage());
//        }
//
//        GameSocket.enemyThrow = false;
//        GameSocket.setActiveScreen(this);
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
//            int frameCount = folder.equals("pikachu") ? 30 : folder.equals("bulbasaur") ? 4 : folder.equals("charmander") ? 15 : folder.equals("greninja") ? 38 : 0;
//
//            for (int i = 0; i <= frameCount; i++) {
//                String file = folder.equals("pikachu") ? String.format("pikachu/sprite_%04d.png", i)
//                    : folder.equals("bulbasaur") ? "bulbasaur/frame_" + i + "_delay-0.1s.png"
//                    : folder.equals("charmander") ? String.format("charmander/sprite_%04d.png", i)
//                    : String.format("greninja/frame_%02d_delay-0.04s.png", i);
//
//                if (isPlayer) myActivePokemonFrames.add(new Texture(file));
//                else enemyActivePokemonFrames.add(new Texture(file));
//            }
//        } catch (Exception e) {
//            Gdx.app.log("PokemonBattleScreen", "Missing Assets for: " + pokemonName);
//        }
//    }
//
//    public void processSocketMessage(String rawJson) {
//        Gdx.app.postRunnable(() -> {
//            battleManager.handleSocketMessage(rawJson);
//        });
//    }
//
//    @Override
//    public void render(float delta) {
//        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//            float mouseX = Gdx.input.getX();
//            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
//
//            if (backButtonRect.contains(mouseX, mouseY)) {
//                game.setScreen(new PokemonShowScreen(game, battleManager.playerName, battleManager.enemyName, battleManager.enemyId));
//                return;
//            }
//        }
//
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        float screenWidth = Gdx.graphics.getWidth();
//        float screenHeight = Gdx.graphics.getHeight();
//
//        // পোকেবল এন্টার কি লিসেনার
//        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
//            if (battleManager.isMyTurn && !battleManager.playerThrow && !battleManager.enemyThrow && !battleManager.isMyPokemonSummoned) {
//                battleManager.playerThrow = true;
//                battleManager.playerFrame = 0; battleManager.playerTimer = 0;
//                battleManager.playerBallX = 220; battleManager.playerBallY = 120;
//
//                String myChosenPokemon = myPokemons.get(selectedPokemonIndex).name;
//                loadPokemonAnimationFrames(myChosenPokemon, true);
//
//                try {
//                    GameSocket.sendThrow(myChosenPokemon);
//                } catch (Exception e) {
//                    try { GameSocket.sendThrow(); } catch (Exception ex) {}
//                }
//                battleManager.statusMessage = "You threw " + myChosenPokemon.toUpperCase() + "!";
//            }
//        }
//
//        if (GameSocket.enemyThrow) {
//            if (!battleManager.playerThrow && !battleManager.enemyThrow && !battleManager.isEnemyPokemonSummoned) {
//                battleManager.enemyThrow = true;
//                battleManager.enemyFrame = 0; battleManager.enemyTimer = 0;
//                battleManager.enemyBallX = screenWidth - 160; battleManager.enemyBallY = 320;
//
//                battleManager.activeEnemyPokemonName = (GameSocket.enemyPokemonName != null && !GameSocket.enemyPokemonName.isEmpty()) ? GameSocket.enemyPokemonName : "Pikachu";
//                loadPokemonAnimationFrames(battleManager.activeEnemyPokemonName, false);
//                battleManager.statusMessage = "Opponent threw " + battleManager.activeEnemyPokemonName.toUpperCase() + "!";
//            }
//            GameSocket.enemyThrow = false;
//        }
//
//        // ম্যানেজার মেকানিজম আপডেট লুপ
//        battleManager.update(delta, myActivePokemonFrames.size, enemyActivePokemonFrames.size, attackFrames.length);
//
//        // পোকেমন ফ্রেম লুপার
//        if (battleManager.isMyPokemonSummoned && myActivePokemonFrames.size > 0) {
//            myPokemonAnimationTimer += delta;
//            if (myPokemonAnimationTimer > 0.08f) {
//                myPokemonAnimationTimer = 0;
//                myPokemonCurrentFrame = (myPokemonCurrentFrame + 1) % myActivePokemonFrames.size;
//            }
//        }
//        if (battleManager.isEnemyPokemonSummoned && enemyActivePokemonFrames.size > 0) {
//            enemyPokemonAnimationTimer += delta;
//            if (enemyPokemonAnimationTimer > 0.08f) {
//                enemyPokemonAnimationTimer = 0;
//                enemyPokemonCurrentFrame = (enemyPokemonCurrentFrame + 1) % enemyActivePokemonFrames.size;
//            }
//        }
//
//        // ========================================================
//        // 🎨 RENDER LAYER
//        // ========================================================
//        game.batch.begin();
//        game.batch.draw(grassBg, 0, 0, screenWidth, screenHeight);
//
//        game.batch.draw(backButtonTexture, backButtonRect.x, backButtonRect.y, backButtonRect.width, backButtonRect.height);
//
//        game.batch.draw(playerFrames[battleManager.playerFrame], 30, 40, 220, 150);
//        game.batch.draw(enemyFrames[battleManager.enemyFrame], screenWidth - 160, 280, 120, 170);
//
//        font.setColor(Color.WHITE);
//        if (battleManager.playerName != null) font.draw(game.batch, battleManager.playerName, 40, 230);
//        if (battleManager.enemyName != null) font.draw(game.batch, battleManager.enemyName, screenWidth - 160, 480);
//
//        if (battleManager.playerThrow && battleManager.playerFrame >= 2) {
//            game.batch.draw(pokeball, battleManager.playerBallX, battleManager.playerBallY, 45, 45);
//        }
//
//        if (battleManager.isMyPokemonSummoned && myActivePokemonFrames.size > 0) {
//            game.batch.draw(myActivePokemonFrames.get(myPokemonCurrentFrame), battleManager.myPokemonX, battleManager.myPokemonY, 150, 150);
//            uiFont.setColor(Color.GREEN);
//            uiFont.draw(game.batch, "HP: " + battleManager.myPokemonHp + "/100", battleManager.myPokemonX, battleManager.myPokemonY + 180);
//        }
//
//        if (battleManager.enemyThrow && battleManager.enemyFrame >= 3) {
//            game.batch.draw(pokeball, battleManager.enemyBallX, battleManager.enemyBallY, 50, 50);
//        }
//
//        if (battleManager.isEnemyPokemonSummoned && enemyActivePokemonFrames.size > 0) {
//            game.batch.draw(enemyActivePokemonFrames.get(enemyPokemonCurrentFrame), battleManager.enemyPokemonX, battleManager.enemyPokemonY, 150, 150);
//            uiFont.setColor(Color.RED);
//            uiFont.draw(game.batch, "HP: " + battleManager.enemyPokemonHp + "/100", battleManager.enemyPokemonX, battleManager.enemyPokemonY + 180);
//        }
//
//        // ইউজার কন্ট্রোল প্যানেল
//        uiFont.setColor(Color.CYAN);
//        uiFont.draw(game.batch, "MY POKEMON: " + myPokemons.get(selectedPokemonIndex).name.toUpperCase(), 40, screenHeight - 30);
//
//        if (battleManager.isMyPokemonSummoned && battleManager.isEnemyPokemonSummoned) {
//            uiFont.setColor(Color.ORANGE);
//            uiFont.draw(game.batch, "MOVES: [Z] " + battleManager.move1Name.toUpperCase() + " (PWR: " + battleManager.move1Power + ")  |  [X] " + battleManager.move2Name.toUpperCase() + " (PWR: " + battleManager.move2Power + ")", 40, screenHeight - 65);
//        }
//
//        if (battleManager.isEnemyPokemonSummoned) {
//            uiFont.setColor(Color.WHITE);
//            uiFont.draw(game.batch, "ENEMY POKEMON: " + battleManager.activeEnemyPokemonName.toUpperCase(), screenWidth - 320, screenHeight - 30);
//        }
//
//        // 💥 রেন্ডারিং ১০-ফ্রেম মেমোরি ফায়ার ইফেক্ট (image_28b560.png)
//        if (battleManager.isAnimatingAttack && attackFrames[battleManager.currentAttackFrameIndex] != null) {
//            game.batch.draw(attackFrames[battleManager.currentAttackFrameIndex], battleManager.attackVisualX, battleManager.attackVisualY, 250, 200);
//        }
//
//        // 📣 [ক্লিন একক ব্যানার ও টার্ন এক্সচেঞ্জ লজিক]
//        if (battleManager.isMyPokemonSummoned && battleManager.isEnemyPokemonSummoned) {
//            if (battleManager.isMyTurn) {
//                if (battleManager.selectedMoveSlot == 0) {
//                    announcementFont.setColor(Color.GREEN);
//                    announcementFont.draw(game.batch, "👉 YOUR TURN: SELECT A MOVE [Z / X]", screenWidth / 2 - 270, screenHeight / 2 + 120);
//                } else {
//                    String selectedName = (battleManager.selectedMoveSlot == 1) ? battleManager.move1Name : battleManager.move2Name;
//                    announcementFont.setColor(Color.GOLD);
//                    announcementFont.draw(game.batch, "🔥 LOCKED: " + selectedName.toUpperCase() + "! PRESS SPACE TO ATTACK!", screenWidth / 2 - 340, screenHeight / 2 + 120);
//                }
//            } else {
//                announcementFont.setColor(Color.RED);
//                announcementFont.draw(game.batch, "⏳ OPPONENT'S TURN! WAITING...", screenWidth / 2 - 230, screenHeight / 2 + 120);
//            }
//        }
//
//        font.setColor(Color.YELLOW);
//        font.draw(game.batch, battleManager.statusMessage, screenWidth / 2 - 280, 60);
//        game.batch.end();
//    }
//
//    @Override public void show() {}
//    @Override public void resize(int width, int height) { backButtonRect.y = height - 70; }
//    @Override public void pause() {}
//    @Override public void resume() {}
//    @Override public void hide() { GameSocket.setActiveScreen(null); }
//
//    @Override
//    public void dispose() {
//        grassBg.dispose();
//        pokeball.dispose();
//        backButtonTexture.dispose();
//        font.dispose();
//        uiFont.dispose();
//        announcementFont.dispose();
//        for (Texture t : enemyFrames) if (t != null) t.dispose();
//        for (Texture t : playerFrames) if (t != null) t.dispose();
//        for (Texture t : myActivePokemonFrames) if (t != null) t.dispose();
//        for (Texture t : enemyActivePokemonFrames) if (t != null) t.dispose();
//        if (attackFrames != null) {
//            for (Texture texture : attackFrames) {
//                if (texture != null) texture.dispose();
//            }
//        }
//    }
//}
//cc
package com.codeexplorer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.codeexplorer.Main;
import com.codeexplorer.network.GameSocket;
import com.codeexplorer.world.BattleManager;

import java.util.ArrayList;

public class PokemonBattleScreen implements Screen {

    private final Main game;
    private Texture grassBg;
    private Texture pokeball;
    private BitmapFont font;
    private BitmapFont uiFont;
    private BitmapFont announcementFont;

    private Texture backButtonTexture;
    private Rectangle backButtonRect;

    // 🎯 [লগআউট বাতন ভেরিয়েবলস]
    private Texture logoutButtonTexture;
    private Rectangle logoutButtonRect;

    private final BattleManager battleManager;

    private Texture[] enemyFrames;
    private Texture[] playerFrames;
    private Texture[] attackFrames;

    private ArrayList<PokemonShowScreen.PokemonModel> myPokemons;
    private int selectedPokemonIndex;

    private Array<Texture> myActivePokemonFrames;
    private Array<Texture> enemyActivePokemonFrames;
    private int myPokemonCurrentFrame = 0;
    private int enemyPokemonCurrentFrame = 0;
    private float myPokemonAnimationTimer = 0;
    private float enemyPokemonAnimationTimer = 0;

    public PokemonBattleScreen(Main game, String playerName, String enemyName, Long enemyId,
                               ArrayList<PokemonShowScreen.PokemonModel> myPokemons, int selectedPokemonIndex) {
        this.game = game;
        this.myPokemons = myPokemons;
        this.selectedPokemonIndex = selectedPokemonIndex;

        this.battleManager = new BattleManager(playerName, enemyName, enemyId, myPokemons, selectedPokemonIndex);

        grassBg = new Texture("battle/background/grass.png");
        pokeball = new Texture("battle/pokeball/pokeball.png");

        backButtonTexture = new Texture("button/back-button.png");
        backButtonRect = new Rectangle(20, Gdx.graphics.getHeight() - 70, 50, 50);

        // 🎯 [লগআউট বাটন ইনিশিয়ালাইজেশন - ব্যাক বাটনের পাশে পজিশন দেওয়া হলো]
        logoutButtonTexture = new Texture("ui/logout.png");
        logoutButtonRect = new Rectangle(90, Gdx.graphics.getHeight() - 70, 50, 50);

        font = new BitmapFont();
        font.getData().setScale(1.5f);

        uiFont = new BitmapFont();
        uiFont.getData().setScale(1.4f);

        announcementFont = new BitmapFont();
        announcementFont.getData().setScale(2.0f);

        myActivePokemonFrames = new Array<>();
        enemyActivePokemonFrames = new Array<>();

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

        // 📸 আপনার ফোল্ডারের ১০টি অ্যাটাক স্প্রাইট লোড হচ্ছে
        attackFrames = new Texture[10];
        try {
            for (int i = 0; i < 10; i++) {
                String path = "battle/attack/sprite_" + String.format("%04d", i) + ".png";
                attackFrames[i] = new Texture(Gdx.files.internal(path));
            }
            System.out.println("✅ Attack Assets Registered into VM Array Buffer");
        } catch (Exception e) {
            System.err.println("❌ Mapping Exception: " + e.getMessage());
        }

        GameSocket.enemyThrow = false;
        GameSocket.setActiveScreen(this);
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
            Gdx.app.log("PokemonBattleScreen", "Missing Assets for: " + pokemonName);
        }
    }

    public void processSocketMessage(String rawJson) {
        Gdx.app.postRunnable(() -> {
            battleManager.handleSocketMessage(rawJson);
        });
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // ব্যাক বাটন ক্লিক হ্যান্ডেলার
            if (backButtonRect.contains(mouseX, mouseY)) {
                game.setScreen(new PokemonShowScreen(game, battleManager.playerName, battleManager.enemyName, battleManager.enemyId));
                return;
            }

            // 🎯 [লগআউট বাটন ক্লিক হ্যান্ডেলার]
            if (logoutButtonRect.contains(mouseX, mouseY)) {
                System.out.println("🔴 BATTLE LOGOUT CLICKED! Disconnecting Socket & Going to HomeScreen...");
                GameSocket.disconnect();
                game.setScreen(new HomeScreen(game));
                return;
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // পোকেবল এন্টার কি লিসেনার
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (battleManager.isMyTurn && !battleManager.playerThrow && !battleManager.enemyThrow && !battleManager.isMyPokemonSummoned) {
                battleManager.playerThrow = true;
                battleManager.playerFrame = 0; battleManager.playerTimer = 0;
                battleManager.playerBallX = 220; battleManager.playerBallY = 120;

                String myChosenPokemon = myPokemons.get(selectedPokemonIndex).name;
                loadPokemonAnimationFrames(myChosenPokemon, true);

                try {
                    GameSocket.sendThrow(myChosenPokemon);
                } catch (Exception e) {
                    try { GameSocket.sendThrow(); } catch (Exception ex) {}
                }
                battleManager.statusMessage = "You threw " + myChosenPokemon.toUpperCase() + "!";
            }
        }

        if (GameSocket.enemyThrow) {
            if (!battleManager.playerThrow && !battleManager.enemyThrow && !battleManager.isEnemyPokemonSummoned) {
                battleManager.enemyThrow = true;
                battleManager.enemyFrame = 0; battleManager.enemyTimer = 0;
                battleManager.enemyBallX = screenWidth - 160; battleManager.enemyBallY = 320;

                battleManager.activeEnemyPokemonName = (GameSocket.enemyPokemonName != null && !GameSocket.enemyPokemonName.isEmpty()) ? GameSocket.enemyPokemonName : "Pikachu";
                loadPokemonAnimationFrames(battleManager.activeEnemyPokemonName, false);
                battleManager.statusMessage = "Opponent threw " + battleManager.activeEnemyPokemonName.toUpperCase() + "!";
            }
            GameSocket.enemyThrow = false;
        }

        // ম্যানেজার মেকানিজম আপডেট লুপ
        battleManager.update(delta, myActivePokemonFrames.size, enemyActivePokemonFrames.size, attackFrames.length);

        // পোকেমন ফ্রেম লুপার
        if (battleManager.isMyPokemonSummoned && myActivePokemonFrames.size > 0) {
            myPokemonAnimationTimer += delta;
            if (myPokemonAnimationTimer > 0.08f) {
                myPokemonAnimationTimer = 0;
                myPokemonCurrentFrame = (myPokemonCurrentFrame + 1) % myActivePokemonFrames.size;
            }
        }
        if (battleManager.isEnemyPokemonSummoned && enemyActivePokemonFrames.size > 0) {
            enemyPokemonAnimationTimer += delta;
            if (enemyPokemonAnimationTimer > 0.08f) {
                enemyPokemonAnimationTimer = 0;
                enemyPokemonCurrentFrame = (enemyPokemonCurrentFrame + 1) % enemyActivePokemonFrames.size;
            }
        }

        // ========================================================
        // 🎨 RENDER LAYER
        // ========================================================
        game.batch.begin();
        game.batch.draw(grassBg, 0, 0, screenWidth, screenHeight);

        // বাটন ড্রইং লুপ
        game.batch.draw(backButtonTexture, backButtonRect.x, backButtonRect.y, backButtonRect.width, backButtonRect.height);

        // 🎯 [লগআউট বাটন স্ক্রিনে ড্র করা হলো]
        game.batch.draw(logoutButtonTexture, logoutButtonRect.x, logoutButtonRect.y, logoutButtonRect.width, logoutButtonRect.height);

        game.batch.draw(playerFrames[battleManager.playerFrame], 30, 40, 220, 150);
        game.batch.draw(enemyFrames[battleManager.enemyFrame], screenWidth - 160, 280, 120, 170);

        font.setColor(Color.WHITE);
        if (battleManager.playerName != null) font.draw(game.batch, battleManager.playerName, 40, 230);
        if (battleManager.enemyName != null) font.draw(game.batch, battleManager.enemyName, screenWidth - 160, 480);

        if (battleManager.playerThrow && battleManager.playerFrame >= 2) {
            game.batch.draw(pokeball, battleManager.playerBallX, battleManager.playerBallY, 45, 45);
        }

        if (battleManager.isMyPokemonSummoned && myActivePokemonFrames.size > 0) {
            game.batch.draw(myActivePokemonFrames.get(myPokemonCurrentFrame), battleManager.myPokemonX, battleManager.myPokemonY, 150, 150);
            uiFont.setColor(Color.GREEN);
            uiFont.draw(game.batch, "HP: " + battleManager.myPokemonHp + "/100", battleManager.myPokemonX, battleManager.myPokemonY + 180);
        }

        if (battleManager.enemyThrow && battleManager.enemyFrame >= 3) {
            game.batch.draw(pokeball, battleManager.enemyBallX, battleManager.enemyBallY, 50, 50);
        }

        if (battleManager.isEnemyPokemonSummoned && enemyActivePokemonFrames.size > 0) {
            game.batch.draw(enemyActivePokemonFrames.get(enemyPokemonCurrentFrame), battleManager.enemyPokemonX, battleManager.enemyPokemonY, 150, 150);
            uiFont.setColor(Color.RED);
            uiFont.draw(game.batch, "HP: " + battleManager.enemyPokemonHp + "/100", battleManager.enemyPokemonX, battleManager.enemyPokemonY + 180);
        }

        // ইউজার কন্ট্রোল প্যানেল
        uiFont.setColor(Color.CYAN);
        uiFont.draw(game.batch, "MY POKEMON: " + myPokemons.get(selectedPokemonIndex).name.toUpperCase(), 40, screenHeight - 30);

        // 🛠️ [এখানেই আপনার সিনট্যাক্স এররটি ফিক্স করা হয়েছে]
        if (battleManager.isMyPokemonSummoned && battleManager.isEnemyPokemonSummoned) {
            uiFont.setColor(Color.ORANGE);
            uiFont.draw(game.batch, "MOVES: [Z] " + battleManager.move1Name.toUpperCase() + " (PWR: " + battleManager.move1Power + ")  |  [X] " + battleManager.move2Name.toUpperCase() + " (PWR: " + battleManager.move2Power + ")", 40, screenHeight - 65);
        }

        if (battleManager.isEnemyPokemonSummoned) {
            uiFont.setColor(Color.WHITE);
            uiFont.draw(game.batch, "ENEMY POKEMON: " + battleManager.activeEnemyPokemonName.toUpperCase(), screenWidth - 320, screenHeight - 30);
        }

        // 💥 রেন্ডারিং ১০-ফ্রেম মেমোরি ফায়ার ইফেক্ট (image_28b560.png)
        if (battleManager.isAnimatingAttack && attackFrames[battleManager.currentAttackFrameIndex] != null) {
            game.batch.draw(attackFrames[battleManager.currentAttackFrameIndex], battleManager.attackVisualX, battleManager.attackVisualY, 250, 200);
        }

        // 📣 [ক্লিন একক ব্যানার ও টার্ন এক্সচেঞ্জ লজিক]
        if (battleManager.isMyPokemonSummoned && battleManager.isEnemyPokemonSummoned) {
            if (battleManager.isMyTurn) {
                if (battleManager.selectedMoveSlot == 0) {
                    announcementFont.setColor(Color.GREEN);
                    announcementFont.draw(game.batch, "👉 YOUR TURN: SELECT A MOVE [Z / X]", screenWidth / 2 - 270, screenHeight / 2 + 120);
                } else {
                    String selectedName = (battleManager.selectedMoveSlot == 1) ? battleManager.move1Name : battleManager.move2Name;
                    announcementFont.setColor(Color.GOLD);
                    announcementFont.draw(game.batch, "🔥 LOCKED: " + selectedName.toUpperCase() + "! PRESS SPACE TO ATTACK!", screenWidth / 2 - 340, screenHeight / 2 + 120);
                }
            } else {
                announcementFont.setColor(Color.RED);
                announcementFont.draw(game.batch, "⏳ OPPONENT'S TURN! WAITING...", screenWidth / 2 - 230, screenHeight / 2 + 120);
            }
        }

        font.setColor(Color.YELLOW);
        font.draw(game.batch, battleManager.statusMessage, screenWidth / 2 - 280, 60);
        game.batch.end();
    }

    @Override public void show() {}
    @Override
    public void resize(int width, int height) {
        backButtonRect.y = height - 70;
        logoutButtonRect.y = height - 70; // 🎯 স্ক্রিন রিসাইজ হলেও বাটন পজিশন ঠিক থাকবে
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { GameSocket.setActiveScreen(null); }

    @Override
    public void dispose() {
        grassBg.dispose();
        pokeball.dispose();
        backButtonTexture.dispose();

        // 🎯 [লগআউট বাটনের মেমোরি রিলিজ করা হলো]
        if (logoutButtonTexture != null) logoutButtonTexture.dispose();

        font.dispose();
        uiFont.dispose();
        announcementFont.dispose();
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
