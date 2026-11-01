
//thik
//package com.codeexplorer.screen;
//
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.Net;
//import com.badlogic.gdx.math.Rectangle;
//import com.codeexplorer.Main;
//import com.codeexplorer.player.Player;
//import com.codeexplorer.model.CurrentPlayer;
//import com.codeexplorer.dto.OnlinePlayerResponse;
//import com.codeexplorer.network.LoginApi;
//import com.codeexplorer.world.*;
//
//public class GameScreen implements Screen {
//
//    private final Main game;
//    private final GrassManager grassManager;
//    private final TreeManager treeManager;
//    private final HouseManager houseManager;
//    private final OtherManager otherManager;
//    private final TileManager tileManager;
//    private final InteractionManager interactionManager;
//    private final PokeballManager pokeballManager;
//    private final OnlinePlayerManager onlinePlayerManager;
//    private final BitmapFont font;
//    private final BitmapFont uiFont;
//    private final CollisionManager collisionManager;
//    private final Player player;
//    private boolean battle = false;
//
//    public GameScreen(Main game) {
//        this.game = game;
//
//        collisionManager = new CollisionManager();
//        pokeballManager = new PokeballManager();
//
//        grassManager = new GrassManager();
//        treeManager = new TreeManager(collisionManager);
//        houseManager = new HouseManager(collisionManager);
//        otherManager = new OtherManager();
//        tileManager = new TileManager(collisionManager);
//        onlinePlayerManager = new OnlinePlayerManager();
//
//        player = new Player(collisionManager);
//        interactionManager = new InteractionManager();
//
//        font = new BitmapFont();
//        uiFont = new BitmapFont();
//        uiFont.setColor(Color.GOLD);
//    }
//
//    @Override
//    public void render(float delta) {
//        collisionManager.clear();
//
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        game.camera.update();
//        game.batch.setProjectionMatrix(game.camera.combined);
//
//        game.batch.begin();
//
//        // DRAW WORLD
//        grassManager.draw(game.batch, delta);
//        pokeballManager.draw(game.batch);
//        tileManager.draw(game.batch);
//        houseManager.draw(game.batch);
//        otherManager.draw(game.batch);
//        treeManager.draw(game.batch);
//
//        // ম্যাপের ওপর ট্রেইনারদের নাম ও ছবি ড্র করা হচ্ছে
//        onlinePlayerManager.draw(game.batch);
//
//        player.update(delta);
//
//        // ========================================================
//        // 🎯 TRAINER COLLISION: ট্রেইনারকে টাচ করলেই ব্যাটল স্ক্রিন ওপেন হবে
//        // ========================================================
//        Rectangle[] trainers = onlinePlayerManager.getTrainerBounds();
//        String[] names = onlinePlayerManager.getTrainerNames();
//        OnlinePlayerResponse[] activePlayers = onlinePlayerManager.getOnlinePlayersList();
//
//        if (trainers != null && names != null) {
//            for (int i = 0; i < trainers.length; i++) {
//                if (trainers[i] == null) continue;
//
//                if (player.getBounds().overlaps(trainers[i])) {
//                    String enemyName = "Unknown Trainer";
//                    Long enemyId = 0L;
//
//                    if (i < names.length && names[i] != null) {
//                        enemyName = names[i];
//                    }
//
//                    if (activePlayers != null && i < activePlayers.length && activePlayers[i] != null) {
//                        enemyId = activePlayers[i].getPlayerId();
//                    }
//
//                    String myName = (CurrentPlayer.username != null) ? CurrentPlayer.username : "Player";
//
//                    game.batch.end();
//                    game.setScreen(new NewBattleScreen(game, myName, enemyName, enemyId));
//                    return;
//                }
//            }
//        }
//
//        // POKEBALL COLLISION
//        for (int i = 0; i < 5; i++) {
//            if (!pokeballManager.getCollected()[i] && player.getBounds().overlaps(pokeballManager.getPokeballs()[i])) {
//                pokeballManager.getCollected()[i] = true;
//
//                if (CurrentPlayer.pokeballs < 5) {
//                    CurrentPlayer.pokeballs++;
//                    String json = "{\"playerId\":" + CurrentPlayer.id + "}";
//
//                    LoginApi.collectPokeball(json, new Net.HttpResponseListener() {
//                        @Override
//                        public void handleHttpResponse(Net.HttpResponse httpResponse) {}
//                        @Override
//                        public void failed(Throwable t) { t.printStackTrace(); }
//                        @Override
//                        public void cancelled() {}
//                    });
//                }
//            }
//        }
//
//        // TALL GRASS BATTLE
//        if (player.getX() > 520 && player.getX() < 850 && player.getY() > 260 && player.getY() < 420) {
//            if (Math.random() < 0.003) {
//                battle = true;
//            }
//        }
//
//        // DRAW PLAYER & USERNAME
//        player.draw(game.batch);
//
//        if (CurrentPlayer.username != null) {
//            font.draw(game.batch, CurrentPlayer.username, player.getX() - 10, player.getY() + 80);
//        }
//
//        // ELIGIBLE TRAINERS SCREEN CHART
//        float chartX = game.camera.position.x - (Gdx.graphics.getWidth() / 2f) + 20;
//        float chartY = game.camera.position.y + (Gdx.graphics.getHeight() / 2f) - 20;
//
//        uiFont.draw(game.batch, "=== ELIGIBLE TRAINERS (4 POKEMON CAUGHT) ===", chartX, chartY);
//
//        if (names != null && names.length > 0) {
//            int displayedCount = 0;
//            for (int i = 0; i < names.length; i++) {
//                if (names[i] != null && !names[i].isEmpty()) {
//                    displayedCount++;
//                    uiFont.draw(game.batch, displayedCount + ". " + names[i] + " [4/4 Pokemons Ready]", chartX, chartY - (displayedCount * 25));
//                }
//            }
//            if (displayedCount == 0) {
//                uiFont.draw(game.batch, "No trainers nearby.", chartX, chartY - 25);
//            }
//        } else {
//            uiFont.draw(game.batch, "No trainers nearby.", chartX, chartY - 25);
//        }
//
//        // ========================================================
//        // 🐾 WILD POKEMON ENCOUNTERS (এখন ৪টি পোকেমনই 'E' চাপলে কাজ করবে)
//        // ========================================================
//
//        // 1. Pikachu
//        if (interactionManager.nearPikachu(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE PIKACHU", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end();
//                game.setScreen(new BattleScreen(game, "pikachu"));
//                return;
//            }
//        }
//
//        // 2. Charmander
//        if (interactionManager.nearCharmander(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE CHARMANDER", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end();
//                game.setScreen(new BattleScreen(game, "charmander"));
//                return;
//            }
//        }
//
//        // 3. Bulbasaur (নতুন যুক্ত করা হলো)
//        if (interactionManager.nearBulbasaur(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE BULBASAUR", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end();
//                game.setScreen(new BattleScreen(game, "bulbasaur"));
//                return;
//            }
//        }
//
//        // 4. Greninja (নতুন যুক্ত করা হলো)
//        if (interactionManager.nearGreninja(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE GRENINJA", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end();
//                game.setScreen(new BattleScreen(game, "greninja"));
//                return;
//            }
//        }
//
//        game.batch.end();
//    }
//
//    @Override public void show() {}
//    @Override public void resize(int width, int height) {}
//    @Override public void pause() {}
//    @Override public void resume() {}
//    @Override public void hide() {}
//
//    @Override
//    public void dispose() {
//        grassManager.dispose();
//        treeManager.dispose();
//        houseManager.dispose();
//        otherManager.dispose();
//        tileManager.dispose();
//        pokeballManager.dispose();
//        font.dispose();
//        uiFont.dispose();
//        player.dispose();
//        onlinePlayerManager.dispose();
//    }
//}
//update
//package com.codeexplorer.screen;
//
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.Net;
//import com.badlogic.gdx.math.Rectangle;
//import com.codeexplorer.Main;
//import com.codeexplorer.player.Player;
//import com.codeexplorer.model.CurrentPlayer;
//import com.codeexplorer.dto.OnlinePlayerResponse;
//import com.codeexplorer.network.LoginApi;
//import com.codeexplorer.network.GameSocket; // 🚀 সকেট ডিসকানেক্টের জন্য ইমপোর্ট করা হলো
//import com.codeexplorer.world.*;
//
//public class GameScreen implements Screen {
//
//    private final Main game;
//    private final GrassManager grassManager;
//    private final TreeManager treeManager;
//    private final HouseManager houseManager;
//    private final OtherManager otherManager;
//    private final TileManager tileManager;
//    private final InteractionManager interactionManager;
//    private final PokeballManager pokeballManager;
//    private final OnlinePlayerManager onlinePlayerManager;
//    private final BitmapFont font;
//    private final BitmapFont uiFont;
//    private final CollisionManager collisionManager;
//    private final Player player;
//    private boolean battle = false;
//
//    // 🎯 [লগআউট বাটন ভেরিয়েবলস]
//    private final Texture logoutButton;
//    private final Rectangle logoutRect;
//
//    public GameScreen(Main game) {
//        this.game = game;
//
//        collisionManager = new CollisionManager();
//        pokeballManager = new PokeballManager();
//
//        grassManager = new GrassManager();
//        treeManager = new TreeManager(collisionManager);
//        houseManager = new HouseManager(collisionManager);
//        otherManager = new OtherManager();
//        tileManager = new TileManager(collisionManager);
//        onlinePlayerManager = new OnlinePlayerManager();
//
//        player = new Player(collisionManager);
//        interactionManager = new InteractionManager();
//
//        font = new BitmapFont();
//        uiFont = new BitmapFont();
//        uiFont.setColor(Color.GOLD);
//
//        // 🚀 [লগআউট বাটন সেটআপ]: স্ক্রিনের নিচে বাম কোণায় (X: 20, Y: 20) পজিশনে ৫০x৫০ সাইজ
//        logoutButton = new Texture("ui/logout.png");
//        logoutRect = new Rectangle(20, 20, 50, 50);
//    }
//
//    @Override
//    public void render(float delta) {
//        collisionManager.clear();
//
//        // 🚀 [ইনপুট হ্যান্ডলার]: লগআউট বাটনে ক্লিক করা হয়েছে কিনা চেক
//        handleLogoutInput();
//
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        game.camera.update();
//        game.batch.setProjectionMatrix(game.camera.combined);
//
//        game.batch.begin();
//
//        // DRAW WORLD
//        grassManager.draw(game.batch, delta);
//        pokeballManager.draw(game.batch);
//        tileManager.draw(game.batch);
//        houseManager.draw(game.batch);
//        otherManager.draw(game.batch);
//        treeManager.draw(game.batch);
//
//        // ম্যাপের ওপর ট্রেইনারদের নাম ও ছবি ড্র করা হচ্ছে
//        onlinePlayerManager.draw(game.batch);
//
//        player.update(delta);
//
//        // ========================================================
//        // 🎯 TRAINER COLLISION: ট্রেইনারকে টাচ করলেই ব্যাটল স্ক্রিন ওপেন হবে
//        // ========================================================
//        Rectangle[] trainers = onlinePlayerManager.getTrainerBounds();
//        String[] names = onlinePlayerManager.getTrainerNames();
//        OnlinePlayerResponse[] activePlayers = onlinePlayerManager.getOnlinePlayersList();
//
//        if (trainers != null && names != null) {
//            for (int i = 0; i < trainers.length; i++) {
//                if (trainers[i] == null) continue;
//
//                if (player.getBounds().overlaps(trainers[i])) {
//                    String enemyName = "Unknown Trainer";
//                    Long enemyId = 0L;
//
//                    if (i < names.length && names[i] != null) {
//                        enemyName = names[i];
//                    }
//
//                    if (activePlayers != null && i < activePlayers.length && activePlayers[i] != null) {
//                        enemyId = activePlayers[i].getPlayerId();
//                    }
//
//                    String myName = (CurrentPlayer.username != null) ? CurrentPlayer.username : "Player";
//
//                    game.batch.end();
//                    game.setScreen(new NewBattleScreen(game, myName, enemyName, enemyId));
//                    return;
//                }
//            }
//        }
//
//        // POKEBALL COLLISION
//        for (int i = 0; i < 5; i++) {
//            if (!pokeballManager.getCollected()[i] && player.getBounds().overlaps(pokeballManager.getPokeballs()[i])) {
//                pokeballManager.getCollected()[i] = true;
//
//                if (CurrentPlayer.pokeballs < 5) {
//                    CurrentPlayer.pokeballs++;
//                    String json = "{\"playerId\":" + CurrentPlayer.id + "}";
//
//                    LoginApi.collectPokeball(json, new Net.HttpResponseListener() {
//                        @Override
//                        public void handleHttpResponse(Net.HttpResponse httpResponse) {}
//                        @Override
//                        public void failed(Throwable t) { t.printStackTrace(); }
//                        @Override
//                        public void cancelled() {}
//                    });
//                }
//            }
//        }
//
//        // TALL GRASS BATTLE
//        if (player.getX() > 520 && player.getX() < 850 && player.getY() > 260 && player.getY() < 420) {
//            if (Math.random() < 0.003) {
//                battle = true;
//            }
//        }
//
//        // DRAW PLAYER & USERNAME
//        player.draw(game.batch);
//
//        if (CurrentPlayer.username != null) {
//            font.draw(game.batch, CurrentPlayer.username, player.getX() - 10, player.getY() + 80);
//        }
//
//        // ELIGIBLE TRAINERS SCREEN CHART
//        float chartX = game.camera.position.x - (Gdx.graphics.getWidth() / 2f) + 20;
//        float chartY = game.camera.position.y + (Gdx.graphics.getHeight() / 2f) - 20;
//
//        uiFont.draw(game.batch, "=== ELIGIBLE TRAINERS (4 POKEMON CAUGHT) ===", chartX, chartY);
//
//        if (names != null && names.length > 0) {
//            int displayedCount = 0;
//            for (int i = 0; i < names.length; i++) {
//                if (names[i] != null && !names[i].isEmpty()) {
//                    displayedCount++;
//                    uiFont.draw(game.batch, displayedCount + ". " + names[i] + " [4/4 Pokemons Ready]", chartX, chartY - (displayedCount * 25));
//                }
//            }
//            if (displayedCount == 0) {
//                uiFont.draw(game.batch, "No trainers nearby.", chartX, chartY - 25);
//            }
//        } else {
//            uiFont.draw(game.batch, "No trainers nearby.", chartX, chartY - 25);
//        }
//
//        // ========================================================
//        // 🐾 WILD POKEMON ENCOUNTERS
//        // ========================================================
//
//        // 1. Pikachu
//        if (interactionManager.nearPikachu(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE PIKACHU", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end();
//                game.setScreen(new BattleScreen(game, "pikachu"));
//                return;
//            }
//        }
//
//        // 2. Charmander
//        if (interactionManager.nearCharmander(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE CHARMANDER", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end();
//                game.setScreen(new BattleScreen(game, "charmander"));
//                return;
//            }
//        }
//
//        // 3. Bulbasaur
//        if (interactionManager.nearBulbasaur(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE BULBASAUR", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end();
//                game.setScreen(new BattleScreen(game, "bulbasaur"));
//                return;
//            }
//        }
//
//        // 4. Greninja
//        if (interactionManager.nearGreninja(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE GRENINJA", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end();
//                game.setScreen(new BattleScreen(game, "greninja"));
//                return;
//            }
//        }
//
//        // 🚀 [বাটন ড্রয়িং ট্রিক]: ক্যামেরা মুভ করলেও বাটন যেন স্ক্রিনের কোণায় ফিক্সড থাকে, সেজন্য ক্যামেরার পজিশন অনুযায়ী রিলেটিভ কোঅর্ডিনেট সেট করা হয়েছে
//        float buttonScreenX = game.camera.position.x - (Gdx.graphics.getWidth() / 2f) + logoutRect.x;
//        float buttonScreenY = game.camera.position.y - (Gdx.graphics.getHeight() / 2f) + logoutRect.y;
//
//        game.batch.draw(logoutButton, buttonScreenX, buttonScreenY, logoutRect.width, logoutRect.height);
//
//        game.batch.end();
//    }
//
//    // 🚀 [ক্লিক ডিটেকশন ও স্ক্রিন সুইপিং মেথড]
//    private void handleLogoutInput() {
//        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//            float mouseX = Gdx.input.getX();
//            // LibGDX Y-অ্যাক্সিস ফ্লিপ হ্যান্ডেল করার জন্য
//            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
//
//            // মাউস কি বাটনের বাউন্ডারির ভেতরে পড়েছে?
//            if (logoutRect.contains(mouseX, mouseY)) {
//                System.out.println("🔴 LOGOUT CLICKED! Disconnecting Socket & Going to HomeScreen...");
//
//                // ১. সকেট কানেকশন অফ করা হলো (এতে স্প্রিং বুট ডাটাবেজে online_status = 0 করে দেবে)
//                GameSocket.disconnect();
//
//                // ২. স্ক্রিন সোজা HomeScreen এ নিয়ে যাওয়া হলো
//                game.setScreen(new HomeScreen(game));
//            }
//        }
//    }
//
//    @Override public void show() {}
//    @Override public void resize(int width, int height) {}
//    @Override public void pause() {}
//    @Override public void resume() {}
//    @Override public void hide() {}
//
//    @Override
//    public void dispose() {
//        grassManager.dispose();
//        treeManager.dispose();
//        houseManager.dispose();
//        otherManager.dispose();
//        tileManager.dispose();
//        pokeballManager.dispose();
//        font.dispose();
//        uiFont.dispose();
//        player.dispose();
//        onlinePlayerManager.dispose();
//        logoutButton.dispose(); // 🚀 মেমোরি লিক বন্ধ করতে টেক্সচার ডিসপোজ করা হলো
//    }
//}
//check
//package com.codeexplorer.screen;
//
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.Net;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.utils.JsonReader;
//import com.badlogic.gdx.utils.JsonValue;
//import com.codeexplorer.Main;
//import com.codeexplorer.player.Player;
//import com.codeexplorer.model.CurrentPlayer;
//import com.codeexplorer.dto.OnlinePlayerResponse;
//import com.codeexplorer.network.LoginApi;
//import com.codeexplorer.network.GameSocket;
//import com.codeexplorer.world.*;
//
//public class GameScreen implements Screen {
//
//    private final Main game;
//    private final GrassManager grassManager;
//    private final TreeManager treeManager;
//    private final HouseManager houseManager;
//    private final OtherManager otherManager;
//    private final TileManager tileManager;
//    private final InteractionManager interactionManager;
//    private final PokeballManager pokeballManager;
//    private final OnlinePlayerManager onlinePlayerManager;
//    private final BitmapFont font;
//    private final BitmapFont uiFont;
//    private final CollisionManager collisionManager;
//    private final Player player;
//    private boolean battle = false;
//
//    // 🎯 [লগআউট বাটন ভেরিয়েবলস]
//    private final Texture logoutButton;
//    private final Rectangle logoutRect;
//
//    // ⚔️ [মাল্টিপ্লেয়ার রিয়েল-টাইম ব্যাটল স্টেট ভেরিয়েবলস]
//    private boolean waitingForOpponentAccept = false;
//    private boolean incomingBattleRequest = false;
//    private String challengerName = "";
//    private Long challengerId = 0L;
//    private String targetEnemyName = "";
//    private Long targetEnemyId = 0L;
//
//    public GameScreen(Main game) {
//        this.game = game;
//
//        collisionManager = new CollisionManager();
//        pokeballManager = new PokeballManager();
//
//        grassManager = new GrassManager();
//        treeManager = new TreeManager(collisionManager);
//        houseManager = new HouseManager(collisionManager);
//        otherManager = new OtherManager();
//        tileManager = new TileManager(collisionManager);
//        onlinePlayerManager = new OnlinePlayerManager();
//
//        player = new Player(collisionManager);
//        interactionManager = new InteractionManager();
//
//        font = new BitmapFont();
//        uiFont = new BitmapFont();
//        uiFont.setColor(Color.GOLD);
//
//        logoutButton = new Texture("ui/logout.png");
//        logoutRect = new Rectangle(20, 20, 50, 50);
//
//        // 📡 এই স্ক্রিনটিকে সকেটের সাথে লিংক করা হলো
//        GameSocket.setActiveScreen(this);
//    }
//
//    /**
//     * 📡 [সকেট মেসেজ প্রসেসর]: সার্ভার থেকে আসা রিয়েল-টাইম JSON মেসেজ প্রোসেস করার জন্য
//     */
//    public void processSocketMessage(String rawJson) {
//        Gdx.app.postRunnable(() -> {
//            try {
//                JsonValue json = new JsonReader().parse(rawJson);
//                String action = json.getString("action", "");
//
//                // ১. কেউ আমাকে চ্যালেঞ্জ করলে
//                if (action.equals("incoming_battle_request")) {
//                    this.challengerId = json.getLong("senderId");
//                    this.challengerName = json.getString("senderName");
//                    this.incomingBattleRequest = true;
//                }
//                // ২. চ্যালেঞ্জ এক্সেপ্ট হলে দুজনকেই ম্যাচ স্টার্ট কমান্ড পাঠাবে
//                else if (action.equals("start_battle")) {
//                    this.incomingBattleRequest = false;
//                    this.waitingForOpponentAccept = false;
//
//                    String myName = (CurrentPlayer.username != null) ? CurrentPlayer.username : "Player";
//
//                    Long enemyId = json.getLong("senderId");
//                    String enemyName = json.getString("senderName");
//                    if (enemyId.equals(CurrentPlayer.id)) {
//                        enemyId = json.getLong("receiverId");
//                        enemyName = json.getString("receiverName");
//                    }
//
//                    game.batch.end();
//                    game.setScreen(new NewBattleScreen(game, myName, enemyName, enemyId));
//                }
//                // ৩. অপোনেন্ট যদি রিজেক্ট বা 'N' প্রেস করে
//                else if (action.equals("decline_battle_request")) {
//                    this.waitingForOpponentAccept = false;
//                    System.out.println("❌ Opponent declined the battle request.");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    @Override
//    public void render(float delta) {
//        collisionManager.clear();
//        handleLogoutInput();
//
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        game.camera.update();
//        game.batch.setProjectionMatrix(game.camera.combined);
//
//        game.batch.begin();
//
//        // DRAW WORLD
//        grassManager.draw(game.batch, delta);
//        pokeballManager.draw(game.batch);
//        tileManager.draw(game.batch);
//        houseManager.draw(game.batch);
//        otherManager.draw(game.batch);
//        treeManager.draw(game.batch);
//        onlinePlayerManager.draw(game.batch);
//
//        player.update(delta);
//
//        // ========================================================
//        // 🎯 TRAINER COLLISION WITH FILTER (SPECIFIC TOUCH LOGIC)
//        // ========================================================
//        Rectangle[] trainers = onlinePlayerManager.getTrainerBounds();
//        OnlinePlayerResponse[] activePlayers = onlinePlayerManager.getOnlinePlayersList();
//
//        if (trainers != null && activePlayers != null && !waitingForOpponentAccept && !incomingBattleRequest) {
//            for (int i = 0; i < trainers.length; i++) {
//                if (trainers[i] == null || i >= activePlayers.length || activePlayers[i] == null) continue;
//
//                OnlinePlayerResponse p = activePlayers[i];
//                String currentUsername = (CurrentPlayer.username != null) ? CurrentPlayer.username : "";
//
//                // 🎯 [ফিল্টার]: নিজের আইডি বাদে, শুধুমাত্র অনলাইন স্ট্যাটাস ১ এবং ২+ পোকেমন থাকলেই টাচ কাজ করবে
//                if (!p.getUsername().equalsIgnoreCase(currentUsername) && p.getOnlineStatus() == 1 && p.getPokemons() >= 2) {
//
//                    if (player.getBounds().overlaps(trainers[i])) {
//                        targetEnemyName = p.getUsername();
//                        targetEnemyId = p.getPlayerId();
//
//                        System.out.println("⚔️ Target Locked! Sending battle request to: " + targetEnemyName);
//
//                        // নির্দিষ্ট ট্রেইনারের কাছে সকেট রিকোয়েস্ট পাঠানো হচ্ছে
//                        String jsonRequest = "{"
//                            + "\"action\":\"send_battle_request\","
//                            + "\"senderId\":" + CurrentPlayer.id + ","
//                            + "\"senderName\":\"" + currentUsername + "\","
//                            + "\"receiverId\":" + targetEnemyId + ","
//                            + "\"receiverName\":\"" + targetEnemyName + "\""
//                            + "}";
//                        GameSocket.emit(jsonRequest);
//
//                        waitingForOpponentAccept = true;
//                        break;
//                    }
//                }
//            }
//        }
//
//        // ========================================================
//        // 🖥️ UI PROMPTS RENDERING (রিয়েল-টাইম পপ-আপ মেসেজ)
//        // ========================================================
//
//        // কেস ১: আমি কাউকে রিকোয়েস্ট পাঠিয়ে উত্তরের অপেক্ষায় আছি
//        if (waitingForOpponentAccept) {
//            font.setColor(Color.ORANGE);
//            font.draw(game.batch, "Waiting for " + targetEnemyName + " to accept the battle...", player.getX() - 60, player.getY() + 90);
//            font.setColor(Color.WHITE);
//        }
//
//        // কেস ২: অন্য কেউ আমাকে রিকোয়েস্ট পাঠালে আমার স্ক্রিনে ভেসে উঠবে
//        if (incomingBattleRequest) {
//            font.setColor(Color.CYAN);
//            font.draw(game.batch, challengerName + " wants to battle with you!", player.getX() - 60, player.getY() + 110);
//            font.draw(game.batch, "Press Y to ACCEPT / N to DECLINE", player.getX() - 40, player.getY() + 80);
//            font.setColor(Color.WHITE);
//
//            if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
//                String jsonAccept = "{"
//                    + "\"action\":\"accept_battle_request\","
//                    + "\"senderId\":" + challengerId + ","
//                    + "\"senderName\":\"" + challengerName + "\","
//                    + "\"receiverId\":" + CurrentPlayer.id + ","
//                    + "\"receiverName\":\"" + CurrentPlayer.username + "\""
//                    + "}";
//                GameSocket.emit(jsonAccept);
//            }
//            else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
//                incomingBattleRequest = false;
//                String jsonDecline = "{"
//                    + "\"action\":\"decline_battle_request\","
//                    + "\"senderId\":" + challengerId + ","
//                    + "\"receiverId\":" + CurrentPlayer.id + ","
//                    + "\"receiverName\":\"" + CurrentPlayer.username + "\""
//                    + "}";
//                GameSocket.emit(jsonDecline);
//            }
//        }
//
//        // POKEBALL COLLISION
//        for (int i = 0; i < 5; i++) {
//            if (!pokeballManager.getCollected()[i] && player.getBounds().overlaps(pokeballManager.getPokeballs()[i])) {
//                pokeballManager.getCollected()[i] = true;
//
//                if (CurrentPlayer.pokeballs < 5) {
//                    CurrentPlayer.pokeballs++;
//                    String json = "{\"playerId\":" + CurrentPlayer.id + "}";
//
//                    LoginApi.collectPokeball(json, new Net.HttpResponseListener() {
//                        @Override public void handleHttpResponse(Net.HttpResponse httpResponse) {}
//                        @Override public void failed(Throwable t) { t.printStackTrace(); }
//                        @Override public void cancelled() {}
//                    });
//                }
//            }
//        }
//
//        // TALL GRASS BATTLE
//        if (player.getX() > 520 && player.getX() < 850 && player.getY() > 260 && player.getY() < 420) {
//            if (Math.random() < 0.003) {
//                battle = true;
//            }
//        }
//
//        // DRAW PLAYER & USERNAME
//        player.draw(game.batch);
//
//        if (CurrentPlayer.username != null) {
//            font.draw(game.batch, CurrentPlayer.username, player.getX() - 10, player.getY() + 80);
//        }
//
//        // ========================================================
//        // 📊 ELIGIBLE TRAINERS CHART (FILTERED: ONLINE STATUS 1 & POKEMON >= 2)
//        // ========================================================
//        float chartX = game.camera.position.x - (Gdx.graphics.getWidth() / 2f) + 20;
//        float chartY = game.camera.position.y + (Gdx.graphics.getHeight() / 2f) - 20;
//
//        uiFont.draw(game.batch, "=== ELIGIBLE TRAINERS (ONLINE & 2+ POKEMON) ===", chartX, chartY);
//
//        if (activePlayers != null && activePlayers.length > 0) {
//            int displayedCount = 0;
//            String currentUsername = (CurrentPlayer.username != null) ? CurrentPlayer.username : "";
//
//            for (int i = 0; i < activePlayers.length; i++) {
//                if (activePlayers[i] != null) {
//                    OnlinePlayerResponse p = activePlayers[i];
//
//                    // 🎯 চার্ট স্ক্রিন ফিল্টার: শুধুমাত্র যারা অনলাইন (1) এবং যাদের পোকেমন ২ বা তার বেশি আছে
//                    if (!p.getUsername().equalsIgnoreCase(currentUsername) && p.getOnlineStatus() == 1 && p.getPokemons() >= 2) {
//                        displayedCount++;
//                        uiFont.draw(game.batch, displayedCount + ". " + p.getUsername() + " [" + p.getPokemons() + " Pokemons Ready]", chartX, chartY - (displayedCount * 25));
//                    }
//                }
//            }
//            if (displayedCount == 0) {
//                uiFont.draw(game.batch, "No eligible trainers online.", chartX, chartY - 25);
//            }
//        } else {
//            uiFont.draw(game.batch, "No trainers nearby.", chartX, chartY - 25);
//        }
//
//        // WILD POKEMON ENCOUNTERS
//        handleWildPokemonEncounters();
//
//        // LOGOUT BUTTON DRAWING
//        float buttonScreenX = game.camera.position.x - (Gdx.graphics.getWidth() / 2f) + logoutRect.x;
//        float buttonScreenY = game.camera.position.y - (Gdx.graphics.getHeight() / 2f) + logoutRect.y;
//
//        game.batch.draw(logoutButton, buttonScreenX, buttonScreenY, logoutRect.width, logoutRect.height);
//
//        game.batch.end();
//    }
//
//    private void handleWildPokemonEncounters() {
//        if (interactionManager.nearPikachu(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE PIKACHU", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end(); game.setScreen(new BattleScreen(game, "pikachu"));
//                return;
//            }
//        }
//        if (interactionManager.nearCharmander(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE CHARMANDER", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end(); game.setScreen(new BattleScreen(game, "charmander"));
//                return;
//            }
//        }
//        if (interactionManager.nearBulbasaur(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE BULBASAUR", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end(); game.setScreen(new BattleScreen(game, "bulbasaur"));
//                return;
//            }
//        }
//        if (interactionManager.nearGreninja(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE GRENINJA", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end(); game.setScreen(new BattleScreen(game, "greninja"));
//                return;
//            }
//        }
//    }
//
//    private void handleLogoutInput() {
//        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//            float mouseX = Gdx.input.getX();
//            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
//
//            if (logoutRect.contains(mouseX, mouseY)) {
//                System.out.println("🔴 LOGOUT CLICKED! Disconnecting Socket & Going to HomeScreen...");
//                GameSocket.disconnect();
//                game.setScreen(new HomeScreen(game));
//            }
//        }
//    }
//
//    @Override public void show() {}
//    @Override public void resize(int width, int height) {}
//    @Override public void pause() {}
//    @Override public void resume() {}
//    @Override public void hide() {}
//
//    @Override
//    public void dispose() {
//        grassManager.dispose();
//        treeManager.dispose();
//        houseManager.dispose();
//        otherManager.dispose();
//        tileManager.dispose();
//        pokeballManager.dispose();
//        font.dispose();
//        uiFont.dispose();
//        player.dispose();
//        onlinePlayerManager.dispose();
//        logoutButton.dispose();
//    }
//}
//thik foe new abttle scrren
//package com.codeexplorer.screen;
//
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.Net;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.utils.JsonReader;
//import com.badlogic.gdx.utils.JsonValue;
//import com.codeexplorer.Main;
//import com.codeexplorer.player.Player;
//import com.codeexplorer.model.CurrentPlayer;
//import com.codeexplorer.dto.OnlinePlayerResponse;
//import com.codeexplorer.network.LoginApi;
//import com.codeexplorer.network.GameSocket;
//import com.codeexplorer.world.*;
//
//public class GameScreen implements Screen {
//
//    private final Main game;
//    private final GrassManager grassManager;
//    private final TreeManager treeManager;
//    private final HouseManager houseManager;
//    private final OtherManager otherManager;
//    private final TileManager tileManager;
//    private final InteractionManager interactionManager;
//    private final PokeballManager pokeballManager;
//    private final OnlinePlayerManager onlinePlayerManager;
//    private final BitmapFont font;
//    private final BitmapFont uiFont;
//    private final CollisionManager collisionManager;
//    private final Player player;
//    private boolean battle = false;
//
//    // 🎯 [লগআউট বাটন ভেরিয়েবলস]
//    private final Texture logoutButton;
//    private final Rectangle logoutRect;
//
//    // ⚔️ [মাল্টিপ্লেয়ার রিয়েল-টাইম ব্যাটল স্টেট ভেরিয়েবলস]
//    private boolean waitingForOpponentAccept = false;
//    private boolean incomingBattleRequest = false;
//    private String challengerName = "";
//    private Long challengerId = 0L;
//    private String targetEnemyName = "";
//    private Long targetEnemyId = 0L;
//
//    public GameScreen(Main game) {
//        this.game = game;
//
//        collisionManager = new CollisionManager();
//        pokeballManager = new PokeballManager();
//
//        grassManager = new GrassManager();
//        treeManager = new TreeManager(collisionManager);
//        houseManager = new HouseManager(collisionManager);
//        otherManager = new OtherManager();
//        tileManager = new TileManager(collisionManager);
//        onlinePlayerManager = new OnlinePlayerManager();
//
//        player = new Player(collisionManager);
//        interactionManager = new InteractionManager();
//
//        font = new BitmapFont();
//        uiFont = new BitmapFont();
//        uiFont.setColor(Color.GOLD);
//
//        logoutButton = new Texture("ui/logout.png");
//        logoutRect = new Rectangle(20, 20, 50, 50);
//
//        // 📡 এই স্ক্রিনটিকে সকেটের সাথে লিংক করা হলো
//        GameSocket.setActiveScreen(this);
//    }
//
//    /**
//     * 📡 [সকেট মেসেজ প্রসেসর]: সার্ভার থেকে আসা রিয়েল-টাইম JSON মেসেজ প্রোসেস করার জন্য
//     */
//    public void processSocketMessage(String rawJson) {
//        System.out.println("📥 GameScreen received socket message raw JSON: " + rawJson);
//        Gdx.app.postRunnable(() -> {
//            try {
//                JsonValue json = new JsonReader().parse(rawJson);
//                String action = json.getString("action", "");
//
//                // 🎯 ১. টার্গেট প্লেয়ারের স্ক্রিনে এই কন্ডিশনটি ট্রু হবে (ব্যাটেল পপ-আপ রিকোয়েস্ট)
//                if (action.equals("send_battle_request")) {
//                    this.challengerId = json.getLong("senderId");
//                    this.challengerName = json.getString("senderName");
//                    this.incomingBattleRequest = true;
//                    System.out.println("⚔️ POP-UP TRIGGERED! Challenge from: " + this.challengerName);
//                }
//                // ২. চ্যালেঞ্জ এক্সেপ্ট হলে দুজনকেই ম্যাচ স্টার্ট কমান্ড পাঠাবে
//                else if (action.equals("start_battle")) {
//                    this.incomingBattleRequest = false;
//                    this.waitingForOpponentAccept = false;
//
//                    String myName = (CurrentPlayer.username != null) ? CurrentPlayer.username : "Player";
//
//                    Long enemyId = json.getLong("senderId");
//                    String enemyName = json.getString("senderName");
//                    if (enemyId.equals(CurrentPlayer.id)) {
//                        enemyId = json.getLong("receiverId");
//                        enemyName = json.getString("receiverName");
//                    }
//
//                    // 🎯 [মেইন এরর ফিক্স]: গেম ক্র্যাশ করা আটকাতে এখান থেকে game.batch.end() রিমুভ করা হয়েছে।
//                    game.setScreen(new NewBattleScreen(game, myName, enemyName, enemyId));
//                }
//                // ৩. অপোনেন্ট যদি রিজেক্ট বা 'N' প্রেস করে
//                else if (action.equals("decline_battle_request")) {
//                    this.waitingForOpponentAccept = false;
//                    System.out.println("❌ Opponent declined the battle request.");
//                }
//            } catch (Exception e) {
//                System.err.println("Error parsing incoming socket message: " + e.getMessage());
//                e.printStackTrace();
//            }
//        });
//    }
//
//    @Override
//    public void render(float delta) {
//        collisionManager.clear();
//        handleLogoutInput();
//
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        game.camera.update();
//        game.batch.setProjectionMatrix(game.camera.combined);
//
//        game.batch.begin();
//
//        // DRAW WORLD
//        grassManager.draw(game.batch, delta);
//        pokeballManager.draw(game.batch);
//        tileManager.draw(game.batch);
//        houseManager.draw(game.batch);
//        otherManager.draw(game.batch);
//        treeManager.draw(game.batch);
//        onlinePlayerManager.draw(game.batch);
//
//        player.update(delta);
//
//        // ========================================================
//        // 🎯 TRAINER COLLISION WITH FILTER (SPECIFIC TOUCH LOGIC)
//        // ========================================================
//        Rectangle[] trainers = onlinePlayerManager.getTrainerBounds();
//        OnlinePlayerResponse[] activePlayers = onlinePlayerManager.getOnlinePlayersList();
//
//        if (trainers != null && activePlayers != null && !waitingForOpponentAccept && !incomingBattleRequest) {
//            for (int i = 0; i < trainers.length; i++) {
//                if (trainers[i] == null || i >= activePlayers.length || activePlayers[i] == null) continue;
//
//                OnlinePlayerResponse p = activePlayers[i];
//                String currentUsername = (CurrentPlayer.username != null) ? CurrentPlayer.username : "";
//
//                // 🎯 DTO মেথড অনুযায়ী getPokemonCount() ব্যবহার করা নিরাপদ
//                int pCount = 0;
//                try { pCount = p.getPokemonCount(); } catch(Exception e) { pCount = 2; }
//
//                if (!p.getUsername().equalsIgnoreCase(currentUsername) && p.getOnlineStatus() == 1 && pCount >= 2) {
//
//                    if (player.getBounds().overlaps(trainers[i])) {
//                        targetEnemyName = p.getUsername();
//                        targetEnemyId = p.getPlayerId();
//
//                        System.out.println("⚔️ Target Locked! Sending battle request to: " + targetEnemyName);
//
//                        String jsonRequest = "{"
//                            + "\"action\":\"send_battle_request\","
//                            + "\"senderId\":" + CurrentPlayer.id + ","
//                            + "\"senderName\":\"" + currentUsername + "\","
//                            + "\"receiverId\":" + targetEnemyId + ","
//                            + "\"receiverName\":\"" + targetEnemyName + "\""
//                            + "}";
//                        GameSocket.emit(jsonRequest);
//
//                        waitingForOpponentAccept = true;
//                        break;
//                    }
//                }
//            }
//        }
//
//        // ========================================================
//        // 🖥️ UI PROMPTS RENDERING (রিয়েল-টাইম পপ-আপ মেসেজ)
//        // ========================================================
//
//        // কেস ১: আমি কাউকে রিকোয়েস্ট পাঠিয়ে উত্তরের অপেক্ষায় আছি
//        if (waitingForOpponentAccept) {
//            font.setColor(Color.ORANGE);
//            font.draw(game.batch, "Waiting for " + targetEnemyName + " to accept the battle...", player.getX() - 100, player.getY() + 120);
//            font.setColor(Color.WHITE);
//        }
//
//        // কেস ২: অন্য কেউ আমাকে রিকোয়েস্ট পাঠালে আমার স্ক্রিনে ভেসে উঠবে (Y/N অপশন)
//        if (incomingBattleRequest) {
//            // 🎯 [পজিশন ফিক্স]: ক্যারেক্টারের মাথার উপর লেখা জ্যাম হওয়া আটকাতে ক্যামেরার সাথে লক করা হলো
//            float uiX = game.camera.position.x - 150;
//            float uiY = game.camera.position.y + 100;
//
//            font.setColor(Color.CYAN);
//            font.draw(game.batch, challengerName + " wants to battle with you!", uiX, uiY);
//
//            font.setColor(Color.GREEN);
//            font.draw(game.batch, "Press Y to ACCEPT / N to DECLINE", uiX, uiY - 30);
//            font.setColor(Color.WHITE);
//
//            // ⌨️ কিবোর্ড থেকে Y প্রেস করলে এক্সেপ্ট জেসন সার্ভারে চলে যাবে
//            if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
//                System.out.println("✅ Y Pressed! Accepting challenge...");
//                String jsonAccept = "{"
//                    + "\"action\":\"accept_battle_request\","
//                    + "\"senderId\":" + challengerId + ","
//                    + "\"senderName\":\"" + challengerName + "\","
//                    + "\"receiverId\":" + CurrentPlayer.id + ","
//                    + "\"receiverName\":\"" + CurrentPlayer.username + "\""
//                    + "}";
//                GameSocket.emit(jsonAccept);
//                incomingBattleRequest = false;
//            }
//            // ⌨️ কিবোর্ড থেকে N প্রেস করলে ডিক্লাইন জেসন সার্ভারে চলে যাবে
//            else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
//                System.out.println("❌ N Pressed! Declining challenge...");
//                incomingBattleRequest = false;
//                String jsonDecline = "{"
//                    + "\"action\":\"decline_battle_request\","
//                    + "\"senderId\":" + challengerId + ","
//                    + "\"receiverId\":" + CurrentPlayer.id + ","
//                    + "\"receiverName\":\"" + CurrentPlayer.username + "\""
//                    + "}";
//                GameSocket.emit(jsonDecline);
//            }
//        }
//
//        // POKEBALL COLLISION
//        for (int i = 0; i < 5; i++) {
//            if (!pokeballManager.getCollected()[i] && player.getBounds().overlaps(pokeballManager.getPokeballs()[i])) {
//                pokeballManager.getCollected()[i] = true;
//
//                if (CurrentPlayer.pokeballs < 5) {
//                    CurrentPlayer.pokeballs++;
//                    String json = "{\"playerId\":" + CurrentPlayer.id + "}";
//
//                    LoginApi.collectPokeball(json, new Net.HttpResponseListener() {
//                        @Override public void handleHttpResponse(Net.HttpResponse httpResponse) {}
//                        @Override public void failed(Throwable t) {}
//                        @Override public void cancelled() {}
//                    });
//                }
//            }
//        }
//
//        // TALL GRASS BATTLE
//        if (player.getX() > 520 && player.getX() < 850 && player.getY() > 260 && player.getY() < 420) {
//            if (Math.random() < 0.003) {
//                battle = true;
//            }
//        }
//
//        // DRAW PLAYER & USERNAME
//        player.draw(game.batch);
//
//        if (CurrentPlayer.username != null) {
//            font.draw(game.batch, CurrentPlayer.username, player.getX() - 10, player.getY() + 80);
//        }
//
//        // ========================================================
//        // 📊 ELIGIBLE TRAINERS CHART (FILTERED: ONLINE STATUS 1 & POKEMON >= 2)
//        // ========================================================
//        float chartX = game.camera.position.x - (Gdx.graphics.getWidth() / 2f) + 20;
//        float chartY = game.camera.position.y + (Gdx.graphics.getHeight() / 2f) - 20;
//
//        uiFont.draw(game.batch, "=== ELIGIBLE TRAINERS (ONLINE & 2+ POKEMON) ===", chartX, chartY);
//
//        if (activePlayers != null && activePlayers.length > 0) {
//            int displayedCount = 0;
//            String currentUsername = (CurrentPlayer.username != null) ? CurrentPlayer.username : "";
//
//            for (int i = 0; i < activePlayers.length; i++) {
//                if (activePlayers[i] != null) {
//                    OnlinePlayerResponse p = activePlayers[i];
//                    int count = 2;
//                    try { count = p.getPokemonCount(); } catch(Exception e){}
//
//                    if (!p.getUsername().equalsIgnoreCase(currentUsername) && p.getOnlineStatus() == 1 && count >= 2) {
//                        displayedCount++;
//                        uiFont.draw(game.batch, displayedCount + ". " + p.getUsername() + " [" + count + " Pokemons Ready]", chartX, chartY - (displayedCount * 25));
//                    }
//                }
//            }
//            if (displayedCount == 0) {
//                uiFont.draw(game.batch, "No eligible trainers online.", chartX, chartY - 25);
//            }
//        } else {
//            uiFont.draw(game.batch, "No trainers nearby.", chartX, chartY - 25);
//        }
//
//        // WILD POKEMON ENCOUNTERS
//        handleWildPokemonEncounters();
//
//        // LOGOUT BUTTON DRAWING
//        float buttonScreenX = game.camera.position.x - (Gdx.graphics.getWidth() / 2f) + logoutRect.x;
//        float buttonScreenY = game.camera.position.y - (Gdx.graphics.getHeight() / 2f) + logoutRect.y;
//
//        game.batch.draw(logoutButton, buttonScreenX, buttonScreenY, logoutRect.width, logoutRect.height);
//
//        game.batch.end();
//    }
//
//    private void handleWildPokemonEncounters() {
//        if (interactionManager.nearPikachu(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE PIKACHU", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end(); game.setScreen(new BattleScreen(game, "pikachu"));
//                return;
//            }
//        }
//        if (interactionManager.nearCharmander(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE CHARMANDER", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end(); game.setScreen(new BattleScreen(game, "charmander"));
//                return;
//            }
//        }
//        if (interactionManager.nearBulbasaur(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE BULBASAUR", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end(); game.setScreen(new BattleScreen(game, "bulbasaur"));
//                return;
//            }
//        }
//        if (interactionManager.nearGreninja(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE GRENINJA", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.batch.end(); game.setScreen(new BattleScreen(game, "greninja"));
//                return;
//            }
//        }
//    }
//
//    private void handleLogoutInput() {
//        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//            float mouseX = Gdx.input.getX();
//            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
//
//            if (logoutRect.contains(mouseX, mouseY)) {
//                System.out.println("🔴 LOGOUT CLICKED! Disconnecting Socket & Going to HomeScreen...");
//                GameSocket.disconnect();
//                game.setScreen(new HomeScreen(game));
//            }
//        }
//    }
//
//    @Override
//    public void show() {
//        GameSocket.setActiveScreen(this);
//        System.out.println("🖥️ GameScreen is now active and listening to WebSocket requests.");
//    }
//
//    @Override public void resize(int width, int height) {}
//    @Override public void pause() {}
//    @Override public void resume() {}
//    @Override public void hide() {}
//
//    @Override
//    public void dispose() {
//        grassManager.dispose();
//        treeManager.dispose();
//        houseManager.dispose();
//        otherManager.dispose();
//        tileManager.dispose();
//        pokeballManager.dispose();
//        font.dispose();
//        uiFont.dispose();
//        player.dispose();
//        onlinePlayerManager.dispose();
//        logoutButton.dispose();
//    }
//}
//thik pokmon catch + battle
//package com.codeexplorer.screen;

//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.Net;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.utils.JsonReader;
//import com.badlogic.gdx.utils.JsonValue;
//import com.codeexplorer.Main;
//import com.codeexplorer.player.Player;
//import com.codeexplorer.model.CurrentPlayer;
//import com.codeexplorer.dto.OnlinePlayerResponse;
//import com.codeexplorer.network.LoginApi;
//import com.codeexplorer.network.GameSocket;
//import com.codeexplorer.world.*;
//
//public class GameScreen implements Screen {
//
//    private final Main game;
//    private final GrassManager grassManager;
//    private final TreeManager treeManager;
//    private final HouseManager houseManager;
//    private final OtherManager otherManager;
//    private final TileManager tileManager;
//    private final InteractionManager interactionManager;
//    private final PokeballManager pokeballManager;
//    private final OnlinePlayerManager onlinePlayerManager;
//    private final BitmapFont font;
//    private final BitmapFont uiFont;
//    private final CollisionManager collisionManager;
//    private final Player player;
//    private boolean battle = false;
//
//    // 🎯 [লগআউট বাটন ভেরিয়েবলস]
//    private final Texture logoutButton;
//    private final Rectangle logoutRect;
//
//    // ⚔️ [মাল্টিপ্লেয়ার রিয়েল-টাইম ব্যাটল স্টেট ভেরিয়েবলস]
//    private boolean waitingForOpponentAccept = false;
//    private boolean incomingBattleRequest = false;
//    private String challengerName = "";
//    private Long challengerId = 0L;
//    private String targetEnemyName = "";
//    private Long targetEnemyId = 0L;
//
//    public GameScreen(Main game) {
//        this.game = game;
//
//        collisionManager = new CollisionManager();
//        pokeballManager = new PokeballManager();
//
//        grassManager = new GrassManager();
//        treeManager = new TreeManager(collisionManager);
//        houseManager = new HouseManager(collisionManager);
//        otherManager = new OtherManager();
//        tileManager = new TileManager(collisionManager);
//        onlinePlayerManager = new OnlinePlayerManager();
//
//        player = new Player(collisionManager);
//        interactionManager = new InteractionManager();
//
//        font = new BitmapFont();
//        uiFont = new BitmapFont();
//        uiFont.setColor(Color.GOLD);
//
//        logoutButton = new Texture("ui/logout.png");
//        logoutRect = new Rectangle(20, 20, 50, 50);
//
//        // 📡 এই স্ক্রিনটিকে সকেটের সাথে লিংক করা হলো
//        GameSocket.setActiveScreen(this);
//    }
//
//    /**
//     * 📡 [সকেট মেসেজ প্রসেসর]: সার্ভার থেকে আসা রিয়েল-টাইম JSON মেসেজ প্রোসেস করার জন্য
//     */
//    public void processSocketMessage(String rawJson) {
//        System.out.println("📥 GameScreen received socket message raw JSON: " + rawJson);
//        Gdx.app.postRunnable(() -> {
//            try {
//                JsonValue json = new JsonReader().parse(rawJson);
//                String action = json.getString("action", "");
//
//                // 🎯 ১. টার্গেট প্লেয়ারের স্ক্রিনে এই কন্ডিশনটি ট্রু হবে (ব্যাটেল পপ-আপ রিকোয়েস্ট)
//                if (action.equals("send_battle_request")) {
//                    this.challengerId = json.getLong("senderId");
//                    this.challengerName = json.getString("senderName");
//                    this.incomingBattleRequest = true;
//                    System.out.println("⚔️ POP-UP TRIGGERED! Challenge from: " + this.challengerName);
//                }
//                // ২. চ্যালেঞ্জ এক্সেপ্ট হলে দুজনকেই ম্যাচ স্টার্ট কমান্ড পাঠাবে
//                else if (action.equals("start_battle")) {
//                    this.incomingBattleRequest = false;
//                    this.waitingForOpponentAccept = false;
//
//                    String myName = (CurrentPlayer.username != null) ? CurrentPlayer.username : "Player";
//
//                    Long enemyId = json.getLong("senderId");
//                    String enemyName = json.getString("senderName");
//                    if (enemyId.equals(CurrentPlayer.id)) {
//                        enemyId = json.getLong("receiverId");
//                        enemyName = json.getString("receiverName");
//                    }
//
//                    game.setScreen(new NewBattleScreen(game, myName, enemyName, enemyId));
//                }
//                // ৩. অপোনেন্ট যদি রিজেক্ট বা 'N' প্রেস করে
//                else if (action.equals("decline_battle_request")) {
//                    this.waitingForOpponentAccept = false;
//                    System.out.println("❌ Opponent declined the battle request.");
//                }
//            } catch (Exception e) {
//                System.err.println("Error parsing incoming socket message: " + e.getMessage());
//                e.printStackTrace();
//            }
//        });
//    }
//
//    @Override
//    public void render(float delta) {
//        collisionManager.clear();
//        handleLogoutInput();
//
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        game.camera.update();
//        game.batch.setProjectionMatrix(game.camera.combined);
//
//        game.batch.begin();
//
//        // DRAW WORLD
//        grassManager.draw(game.batch, delta);
//        pokeballManager.draw(game.batch);
//        tileManager.draw(game.batch);
//        houseManager.draw(game.batch);
//        otherManager.draw(game.batch);
//        treeManager.draw(game.batch);
//        onlinePlayerManager.draw(game.batch);
//
//        player.update(delta);
//
//        // ========================================================
//        // 🎯 TRAINER COLLISION WITH FILTER (SPECIFIC TOUCH LOGIC)
//        // ========================================================
//        Rectangle[] trainers = onlinePlayerManager.getTrainerBounds();
//        OnlinePlayerResponse[] activePlayers = onlinePlayerManager.getOnlinePlayersList();
//
//        if (trainers != null && activePlayers != null && !waitingForOpponentAccept && !incomingBattleRequest) {
//            for (int i = 0; i < trainers.length; i++) {
//                if (trainers[i] == null || i >= activePlayers.length || activePlayers[i] == null) continue;
//
//                OnlinePlayerResponse p = activePlayers[i];
//                String currentUsername = (CurrentPlayer.username != null) ? CurrentPlayer.username : "";
//
//                int pCount = 0;
//                try { pCount = p.getPokemonCount(); } catch(Exception e) { pCount = 2; }
//
//                if (!p.getUsername().equalsIgnoreCase(currentUsername) && p.getOnlineStatus() == 1 && pCount >= 2) {
//
//                    if (player.getBounds().overlaps(trainers[i])) {
//                        targetEnemyName = p.getUsername();
//                        targetEnemyId = p.getPlayerId();
//
//                        System.out.println("⚔️ Target Locked! Sending battle request to: " + targetEnemyName);
//
//                        String jsonRequest = "{"
//                            + "\"action\":\"send_battle_request\","
//                            + "\"senderId\":" + CurrentPlayer.id + ","
//                            + "\"senderName\":\"" + currentUsername + "\","
//                            + "\"receiverId\":" + targetEnemyId + ","
//                            + "\"receiverName\":\"" + targetEnemyName + "\""
//                            + "}";
//                        GameSocket.emit(jsonRequest);
//
//                        waitingForOpponentAccept = true;
//                        break;
//                    }
//                }
//            }
//        }
//
//        // ========================================================
//        // 🖥️ UI PROMPTS RENDERING (রিয়েল-টাইম পপ-আপ মেসেজ)
//        // ========================================================
//
//        // কেস ১: আমি কাউকে রিকোয়েস্ট পাঠিয়ে উত্তরের অপেক্ষায় আছি
//        if (waitingForOpponentAccept) {
//            font.setColor(Color.ORANGE);
//            font.draw(game.batch, "Waiting for " + targetEnemyName + " to accept the battle...", player.getX() - 100, player.getY() + 120);
//            font.setColor(Color.WHITE);
//        }
//
//        // কেস ২: অন্য কেউ আমাকে রিকোয়েস্ট পাঠালে আমার স্ক্রিনে ভেসে উঠবে (Y/N অপশন)
//        if (incomingBattleRequest) {
//            float uiX = game.camera.position.x - 150;
//            float uiY = game.camera.position.y + 100;
//
//            font.setColor(Color.CYAN);
//            font.draw(game.batch, challengerName + " wants to battle with you!", uiX, uiY);
//
//            font.setColor(Color.GREEN);
//            font.draw(game.batch, "Press Y to ACCEPT / N to DECLINE", uiX, uiY - 30);
//            font.setColor(Color.WHITE);
//
//            // ⌨️ কিবোর্ড থেকে Y প্রেস করলে এক্সেপ্ট জেসন সার্ভারে চলে যাবে
//            if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
//                System.out.println("✅ Y Pressed! Accepting challenge...");
//                String jsonAccept = "{"
//                    + "\"action\":\"accept_battle_request\","
//                    + "\"senderId\":" + challengerId + ","
//                    + "\"senderName\":\"" + challengerName + "\","
//                    + "\"receiverId\":" + CurrentPlayer.id + ","
//                    + "\"receiverName\":\"" + CurrentPlayer.username + "\""
//                    + "}";
//                GameSocket.emit(jsonAccept);
//                incomingBattleRequest = false;
//            }
//            // ⌨️ কিবোর্ড থেকে N প্রেস করলে ডিক্লাইন জেসন সার্ভারে চলে যাবে
//            else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
//                System.out.println("❌ N Pressed! Declining challenge...");
//                incomingBattleRequest = false;
//                String jsonDecline = "{"
//                    + "\"action\":\"decline_battle_request\","
//                    + "\"senderId\":" + challengerId + ","
//                    + "\"receiverId\":" + CurrentPlayer.id + ","
//                    + "\"receiverName\":\"" + CurrentPlayer.username + "\""
//                    + "}";
//                GameSocket.emit(jsonDecline);
//            }
//        }
//
//        // POKEBALL COLLISION
//        for (int i = 0; i < 5; i++) {
//            if (!pokeballManager.getCollected()[i] && player.getBounds().overlaps(pokeballManager.getPokeballs()[i])) {
//                pokeballManager.getCollected()[i] = true;
//
//                if (CurrentPlayer.pokeballs < 5) {
//                    CurrentPlayer.pokeballs++;
//                    String json = "{\"playerId\":" + CurrentPlayer.id + "}";
//
//                    LoginApi.collectPokeball(json, new Net.HttpResponseListener() {
//                        @Override public void handleHttpResponse(Net.HttpResponse httpResponse) {}
//                        @Override public void failed(Throwable t) {}
//                        @Override public void cancelled() {}
//                    });
//                }
//            }
//        }
//
//        // TALL GRASS BATTLE
//        if (player.getX() > 520 && player.getX() < 850 && player.getY() > 260 && player.getY() < 420) {
//            if (Math.random() < 0.003) {
//                battle = true;
//            }
//        }
//
//        // DRAW PLAYER & USERNAME
//        player.draw(game.batch);
//
//        if (CurrentPlayer.username != null) {
//            font.draw(game.batch, CurrentPlayer.username, player.getX() - 10, player.getY() + 80);
//        }
//
//        // ========================================================
//        // 📊 ELIGIBLE TRAINERS CHART (FILTERED: ONLINE STATUS 1 & POKEMON >= 2)
//        // ========================================================
//        float chartX = game.camera.position.x - (Gdx.graphics.getWidth() / 2f) + 20;
//        float chartY = game.camera.position.y + (Gdx.graphics.getHeight() / 2f) - 20;
//
//        uiFont.draw(game.batch, "=== ELIGIBLE TRAINERS (ONLINE & 2+ POKEMON) ===", chartX, chartY);
//
//        if (activePlayers != null && activePlayers.length > 0) {
//            int displayedCount = 0;
//            String currentUsername = (CurrentPlayer.username != null) ? CurrentPlayer.username : "";
//
//            for (int i = 0; i < activePlayers.length; i++) {
//                if (activePlayers[i] != null) {
//                    OnlinePlayerResponse p = activePlayers[i];
//                    int count = 2;
//                    try { count = p.getPokemonCount(); } catch(Exception e){}
//
//                    if (!p.getUsername().equalsIgnoreCase(currentUsername) && p.getOnlineStatus() == 1 && count >= 2) {
//                        displayedCount++;
//                        uiFont.draw(game.batch, displayedCount + ". " + p.getUsername() + " [" + count + " Pokemons Ready]", chartX, chartY - (displayedCount * 25));
//                    }
//                }
//            }
//            if (displayedCount == 0) {
//                uiFont.draw(game.batch, "No eligible trainers online.", chartX, chartY - 25);
//            }
//        } else {
//            uiFont.draw(game.batch, "No trainers nearby.", chartX, chartY - 25);
//        }
//
//        // WILD POKEMON ENCOUNTERS
//        handleWildPokemonEncounters();
//
//        // LOGOUT BUTTON DRAWING
//        float buttonScreenX = game.camera.position.x - (Gdx.graphics.getWidth() / 2f) + logoutRect.x;
//        float buttonScreenY = game.camera.position.y - (Gdx.graphics.getHeight() / 2f) + logoutRect.y;
//
//        game.batch.draw(logoutButton, buttonScreenX, buttonScreenY, logoutRect.width, logoutRect.height);
//
//        game.batch.end();
//    }
//
//    /**
//     * 🐾 [বাগ ফিক্সড]: গেম ক্র্যাশ এড়াতে এখান থেকে game.batch.end() তুলে দিয়ে
//     * সরাসরি পুরনো রেগুলার BattleScreen-এ রিডাইরেক্ট করা হয়েছে।
//     */
//    private void handleWildPokemonEncounters() {
//        if (interactionManager.nearPikachu(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE PIKACHU", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.setScreen(new BattleScreen(game, "pikachu"));
//                return;
//            }
//        }
//        if (interactionManager.nearCharmander(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE CHARMANDER", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.setScreen(new BattleScreen(game, "charmander"));
//                return;
//            }
//        }
//        if (interactionManager.nearBulbasaur(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE BULBASAUR", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.setScreen(new BattleScreen(game, "bulbasaur"));
//                return;
//            }
//        }
//        if (interactionManager.nearGreninja(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE GRENINJA", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.setScreen(new BattleScreen(game, "greninja"));
//                return;
//            }
//        }
//    }
//
//    private void handleLogoutInput() {
//        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//            float mouseX = Gdx.input.getX();
//            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
//
//            if (logoutRect.contains(mouseX, mouseY)) {
//                System.out.println("🔴 LOGOUT CLICKED! Disconnecting Socket & Going to HomeScreen...");
//                GameSocket.disconnect();
//                game.setScreen(new HomeScreen(game));
//            }
//        }
//    }
//
//    @Override
//    public void show() {
//        GameSocket.setActiveScreen(this);
//        System.out.println("🖥️ GameScreen is now active and listening to WebSocket requests.");
//    }
//
//    @Override public void resize(int width, int height) {}
//    @Override public void pause() {}
//    @Override public void resume() {}
//
//    @Override
//    public void hide() {
//        // স্ক্রিন ব্যাকগ্রাউন্ডে বা ইনঅ্যাক্টিভ হলে সকেট লিসেনার সরিয়ে দেওয়া নিরাপদ
//        GameSocket.setActiveScreen(null);
//    }
//
//    @Override
//    public void dispose() {
//        grassManager.dispose();
//        treeManager.dispose();
//        houseManager.dispose();
//        otherManager.dispose();
//        tileManager.dispose();
//        pokeballManager.dispose();
//        font.dispose();
//        uiFont.dispose();
//        player.dispose();
//        onlinePlayerManager.dispose();
//        logoutButton.dispose();
//        System.out.println("🧹 GameScreen resources disposed successfully.");
//    }
//}
//n
//action..
//package com.codeexplorer.screen;
//
//
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.Net;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.utils.JsonReader;
//import com.badlogic.gdx.utils.JsonValue;
//import com.codeexplorer.Main;
//import com.codeexplorer.player.Player;
//import com.codeexplorer.model.CurrentPlayer;
//import com.codeexplorer.dto.OnlinePlayerResponse;
//import com.codeexplorer.network.LoginApi;
//import com.codeexplorer.network.GameSocket;
//import com.codeexplorer.world.*;
//
//public class GameScreen implements Screen {
//
//    private final Main game;
//    private final GrassManager grassManager;
//    private final TreeManager treeManager;
//    private final HouseManager houseManager;
//    private final OtherManager otherManager;
//    private final TileManager tileManager;
//    private final InteractionManager interactionManager;
//    private final PokeballManager pokeballManager;
//    private final OnlinePlayerManager onlinePlayerManager;
//    private final BitmapFont font;
//    private final BitmapFont uiFont;
//    private final CollisionManager collisionManager;
//    private final Player player;
//    private boolean battle = false;
//
//    // 🎯 [লগআউট বাটন ভেরিয়েবলস]
//    private final Texture logoutButton;
//    private final Rectangle logoutRect;
//
//    // ⚔️ [মাল্টিপ্লেয়ার রিয়েল-টাইম ব্যাটল স্টেট ভেরিয়েবলস]
//    private boolean waitingForOpponentAccept = false;
//    private boolean incomingBattleRequest = false;
//    private String challengerName = "";
//    private Long challengerId = 0L;
//    private String targetEnemyName = "";
//    private Long targetEnemyId = 0L;
//
//    public GameScreen(Main game) {
//        this.game = game;
//
//        collisionManager = new CollisionManager();
//        pokeballManager = new PokeballManager();
//
//        grassManager = new GrassManager();
//        treeManager = new TreeManager(collisionManager);
//        houseManager = new HouseManager(collisionManager);
//        otherManager = new OtherManager();
//        tileManager = new TileManager(collisionManager);
//        onlinePlayerManager = new OnlinePlayerManager();
//
//        player = new Player(collisionManager);
//        interactionManager = new InteractionManager();
//
//        font = new BitmapFont();
//        uiFont = new BitmapFont();
//        uiFont.setColor(Color.GOLD);
//
//        logoutButton = new Texture("ui/logout.png");
//        logoutRect = new Rectangle(20, 20, 50, 50);
//
//        // 📡 সকেটে কারেন্ট স্ক্রিন রেজিস্টার
//        GameSocket.setActiveScreen(this);
//    }
//
//    /**
//     * 📡 [সকেট মেসেজ প্রসেসর]: সার্ভার থেকে আসা রিয়েল-টাইম JSON মেসেজ প্রোসেস করার জন্য
//     */
//    public void processSocketMessage(String rawJson) {
//        System.out.println("📥 GameScreen received socket message raw JSON: " + rawJson);
//        Gdx.app.postRunnable(() -> {
//            try {
//                JsonValue json = new JsonReader().parse(rawJson);
//                String action = json.getString("action", "");
//
//                // 🎯 ১. টার্গেট প্লেয়ারের স্ক্রিনে এই কন্ডিশনটি ট্রু হবে (ব্যাটেল পপ-আপ রিকোয়েস্ট)
//                if (action.equals("send_battle_request")) {
//                    this.challengerId = json.getLong("senderId");
//                    this.challengerName = json.getString("senderName");
//                    this.incomingBattleRequest = true;
//                    System.out.println("⚔️ POP-UP TRIGGERED! Challenge from: " + this.challengerName);
//                }
//                // ২. চ্যালেঞ্জ এক্সেপ্ট হলে দুজনকেই ম্যাচ স্টার্ট কমান্ড পাঠাবে
//                else if (action.equals("start_battle")) {
//                    this.incomingBattleRequest = false;
//                    this.waitingForOpponentAccept = false;
//
//                    String myName = (CurrentPlayer.username != null) ? CurrentPlayer.username : "Player";
//
//                    Long enemyId = json.getLong("senderId");
//                    String enemyName = json.getString("senderName");
//                    if (enemyId.equals(CurrentPlayer.id)) {
//                        enemyId = json.getLong("receiverId");
//                        enemyName = json.getString("receiverName");
//                    }
//
//                    game.setScreen(new NewBattleScreen(game, myName, enemyName, enemyId));
//                }
//                // ৩. অপোনেন্ট যদি রিজেক্ট বা 'N' প্রেস করে
//                else if (action.equals("decline_battle_request")) {
//                    this.waitingForOpponentAccept = false;
//                    System.out.println("❌ Opponent declined the battle request.");
//                }
//            } catch (Exception e) {
//                System.err.println("Error parsing incoming socket message: " + e.getMessage());
//                e.printStackTrace();
//            }
//        });
//    }
//
//    @Override
//    public void render(float delta) {
//        collisionManager.clear();
//        handleLogoutInput();
//
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        game.camera.update();
//        game.batch.setProjectionMatrix(game.camera.combined);
//
//        game.batch.begin();
//
//        // DRAW WORLD
//        grassManager.draw(game.batch, delta);
//        pokeballManager.draw(game.batch);
//        tileManager.draw(game.batch);
//        houseManager.draw(game.batch);
//        otherManager.draw(game.batch);
//        treeManager.draw(game.batch);
//        onlinePlayerManager.draw(game.batch);
//
//        player.update(delta);
//
//        // ========================================================
//        // 🎯 TRAINER COLLISION WITH FILTER (SPECIFIC TOUCH LOGIC)
//        // ========================================================
//        Rectangle[] trainers = onlinePlayerManager.getTrainerBounds();
//        OnlinePlayerResponse[] activePlayers = onlinePlayerManager.getOnlinePlayersList();
//
//        if (trainers != null && activePlayers != null && !waitingForOpponentAccept && !incomingBattleRequest) {
//            for (int i = 0; i < trainers.length; i++) {
//                if (trainers[i] == null || i >= activePlayers.length || activePlayers[i] == null) continue;
//
//                OnlinePlayerResponse p = activePlayers[i];
//                String currentUsername = (CurrentPlayer.username != null) ? CurrentPlayer.username : "";
//
//                int pCount = 0;
//                try { pCount = p.getPokemonCount(); } catch(Exception e) { pCount = 2; }
//
//                if (!p.getUsername().equalsIgnoreCase(currentUsername) && p.getOnlineStatus() == 1 && pCount >= 2) {
//
//                    if (player.getBounds().overlaps(trainers[i])) {
//                        targetEnemyName = p.getUsername();
//                        targetEnemyId = p.getPlayerId();
//
//                        System.out.println("⚔️ Target Locked! Sending battle request to: " + targetEnemyName);
//
//                        String jsonRequest = "{"
//                            + "\"action\":\"send_battle_request\","
//                            + "\"senderId\":" + CurrentPlayer.id + ","
//                            + "\"senderName\":\"" + currentUsername + "\","
//                            + "\"receiverId\":" + targetEnemyId + ","
//                            + "\"receiverName\":\"" + targetEnemyName + "\""
//                            + "}";
//                        GameSocket.emit(jsonRequest);
//
//                        waitingForOpponentAccept = true;
//                        break;
//                    }
//                }
//            }
//        }
//
//        // ========================================================
//        // 🖥️ UI PROMPTS RENDERING (রিয়েল-টাইম পপ-আপ মেসেজ)
//        // ========================================================
//
//        if (waitingForOpponentAccept) {
//            font.setColor(Color.ORANGE);
//            font.draw(game.batch, "Waiting for " + targetEnemyName + " to accept the battle...", player.getX() - 100, player.getY() + 120);
//            font.setColor(Color.WHITE);
//        }
//
//        if (incomingBattleRequest) {
//            float uiX = game.camera.position.x - 150;
//            float uiY = game.camera.position.y + 100;
//
//            font.setColor(Color.CYAN);
//            font.draw(game.batch, challengerName + " wants to battle with you!", uiX, uiY);
//
//            font.setColor(Color.GREEN);
//            font.draw(game.batch, "Press Y to ACCEPT / N to DECLINE", uiX, uiY - 30);
//            font.setColor(Color.WHITE);
//
//            if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
//                System.out.println("✅ Y Pressed! Accepting challenge...");
//                String jsonAccept = "{"
//                    + "\"action\":\"accept_battle_request\","
//                    + "\"senderId\":" + challengerId + ","
//                    + "\"senderName\":\"" + challengerName + "\","
//                    + "\"receiverId\":" + CurrentPlayer.id + ","
//                    + "\"receiverName\":\"" + CurrentPlayer.username + "\""
//                    + "}";
//                GameSocket.emit(jsonAccept);
//                incomingBattleRequest = false;
//            }
//            else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
//                System.out.println("❌ N Pressed! Declining challenge...");
//                incomingBattleRequest = false;
//                String jsonDecline = "{"
//                    + "\"action\":\"decline_battle_request\","
//                    + "\"senderId\":" + challengerId + ","
//                    + "\"receiverId\":" + CurrentPlayer.id + ","
//                    + "\"receiverName\":\"" + CurrentPlayer.username + "\""
//                    + "}";
//                GameSocket.emit(jsonDecline);
//            }
//        }
//
//        // POKEBALL COLLISION
//        for (int i = 0; i < 5; i++) {
//            if (!pokeballManager.getCollected()[i] && player.getBounds().overlaps(pokeballManager.getPokeballs()[i])) {
//                pokeballManager.getCollected()[i] = true;
//
//                if (CurrentPlayer.pokeballs < 5) {
//                    CurrentPlayer.pokeballs++;
//                    String json = "{\"playerId\":" + CurrentPlayer.id + "}";
//
//                    LoginApi.collectPokeball(json, new Net.HttpResponseListener() {
//                        @Override public void handleHttpResponse(Net.HttpResponse httpResponse) {}
//                        @Override public void failed(Throwable t) {}
//                        @Override public void cancelled() {}
//                    });
//                }
//            }
//        }
//
//        // TALL GRASS BATTLE
//        if (player.getX() > 520 && player.getX() < 850 && player.getY() > 260 && player.getY() < 420) {
//            if (Math.random() < 0.003) {
//                battle = true;
//            }
//        }
//
//        // DRAW PLAYER & USERNAME
//        player.draw(game.batch);
//
//        if (CurrentPlayer.username != null) {
//            font.draw(game.batch, CurrentPlayer.username, player.getX() - 10, player.getY() + 80);
//        }
//
//        // ========================================================
//        // 📊 ELIGIBLE TRAINERS CHART
//        // ========================================================
//        float chartX = game.camera.position.x - (Gdx.graphics.getWidth() / 2f) + 20;
//        float chartY = game.camera.position.y + (Gdx.graphics.getHeight() / 2f) - 20;
//
//        uiFont.draw(game.batch, "=== ELIGIBLE TRAINERS (ONLINE & 2+ POKEMON) ===", chartX, chartY);
//
//        if (activePlayers != null && activePlayers.length > 0) {
//            int displayedCount = 0;
//            String currentUsername = (CurrentPlayer.username != null) ? CurrentPlayer.username : "";
//
//            for (int i = 0; i < activePlayers.length; i++) {
//                if (activePlayers[i] != null) {
//                    OnlinePlayerResponse p = activePlayers[i];
//                    int count = 2;
//                    try { count = p.getPokemonCount(); } catch(Exception e){}
//
//                    if (!p.getUsername().equalsIgnoreCase(currentUsername) && p.getOnlineStatus() == 1 && count >= 2) {
//                        displayedCount++;
//                        uiFont.draw(game.batch, displayedCount + ". " + p.getUsername() + " [" + count + " Pokemons Ready]", chartX, chartY - (displayedCount * 25));
//                    }
//                }
//            }
//            if (displayedCount == 0) {
//                uiFont.draw(game.batch, "No eligible trainers online.", chartX, chartY - 25);
//            }
//        } else {
//            uiFont.draw(game.batch, "No trainers nearby.", chartX, chartY - 25);
//        }
//
//        // WILD POKEMON ENCOUNTERS
//        handleWildPokemonEncounters();
//
//        // LOGOUT BUTTON DRAWING
//        float buttonScreenX = game.camera.position.x - (Gdx.graphics.getWidth() / 2f) + logoutRect.x;
//        float buttonScreenY = game.camera.position.y - (Gdx.graphics.getHeight() / 2f) + logoutRect.y;
//
//        game.batch.draw(logoutButton, buttonScreenX, buttonScreenY, logoutRect.width, logoutRect.height);
//
//        game.batch.end();
//    }
//
//    private void handleWildPokemonEncounters() {
//        if (interactionManager.nearPikachu(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE PIKACHU", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.setScreen(new BattleScreen(game, "pikachu"));
//                return;
//            }
//        }
//        if (interactionManager.nearCharmander(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE CHARMANDER", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.setScreen(new BattleScreen(game, "charmander"));
//                return;
//            }
//        }
//        if (interactionManager.nearBulbasaur(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE BULBASAUR", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.setScreen(new BattleScreen(game, "bulbasaur"));
//                return;
//            }
//        }
//        if (interactionManager.nearGreninja(player.getBounds())) {
//            font.draw(game.batch, "PRESS E TO BATTLE GRENINJA", player.getX() - 40, player.getY() + 70);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//                game.setScreen(new BattleScreen(game, "greninja"));
//                return;
//            }
//        }
//    }
//
//    private void handleLogoutInput() {
//        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//            float mouseX = Gdx.input.getX();
//            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
//
//            if (logoutRect.contains(mouseX, mouseY)) {
//                System.out.println("🔴 LOGOUT CLICKED! Disconnecting Socket & Going to HomeScreen...");
//                GameSocket.disconnect();
//                game.setScreen(new HomeScreen(game));
//            }
//        }
//    }
//
//    @Override
//    public void show() {
//        GameSocket.setActiveScreen(this);
//        System.out.println("🖥️ GameScreen is now active and listening to WebSocket requests.");
//    }
//
//    @Override public void resize(int width, int height) {}
//    @Override public void pause() {}
//    @Override public void resume() {}
//
//    @Override
//    public void hide() {
//        GameSocket.setActiveScreen(null);
//    }
//
//    @Override
//    public void dispose() {
//        grassManager.dispose();
//        treeManager.dispose();
//        houseManager.dispose();
//        otherManager.dispose();
//        tileManager.dispose();
//        pokeballManager.dispose();
//        font.dispose();
//        uiFont.dispose();
//        player.dispose();
//        onlinePlayerManager.dispose();
//        logoutButton.dispose();
//        System.out.println("🧹 GameScreen resources disposed successfully.");
//    }
//}
//check for 2 scerren
package com.codeexplorer.screen;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.codeexplorer.Main;
import com.codeexplorer.player.Player;
import com.codeexplorer.model.CurrentPlayer;
import com.codeexplorer.dto.OnlinePlayerResponse;
import com.codeexplorer.network.LoginApi;
import com.codeexplorer.network.GameSocket;
import com.codeexplorer.world.*;

public class GameScreen implements Screen {

    private final Main game;
    private final GrassManager grassManager;
    private final TreeManager treeManager;
    private final HouseManager houseManager;
    private final OtherManager otherManager;
    private final TileManager tileManager;
    private final InteractionManager interactionManager;
    private final PokeballManager pokeballManager;
    private final OnlinePlayerManager onlinePlayerManager;
    private final BitmapFont font;
    private final BitmapFont uiFont;
    private final CollisionManager collisionManager;
    private final Player player;
    private boolean battle = false;

    // 🎯 [লগআউট বাটন ভেরিয়েবলস]
    private final Texture logoutButton;
    private final Rectangle logoutRect;

    // ⚔️ [মাল্টিপ্লেয়ার রিয়েল-টাইম ব্যাটল স্টেট ভেরিয়েবলস]
    private boolean waitingForOpponentAccept = false;
    private boolean incomingBattleRequest = false;
    private String challengerName = "";
    private Long challengerId = 0L;
    private String targetEnemyName = "";
    private Long targetEnemyId = 0L;

    public GameScreen(Main game) {
        this.game = game;

        collisionManager = new CollisionManager();
        pokeballManager = new PokeballManager();

        grassManager = new GrassManager();
        treeManager = new TreeManager(collisionManager);
        houseManager = new HouseManager(collisionManager);
        otherManager = new OtherManager();
        tileManager = new TileManager(collisionManager);
        onlinePlayerManager = new OnlinePlayerManager();

        player = new Player(collisionManager);
        interactionManager = new InteractionManager();

        font = new BitmapFont();
        uiFont = new BitmapFont();
        uiFont.setColor(Color.GOLD);

        logoutButton = new Texture("ui/logout.png");
        logoutRect = new Rectangle(20, 20, 50, 50);

        // 📡 সকেটে কারেন্ট স্ক্রিন রেজিস্টার
        GameSocket.setActiveScreen(this);
    }

    /**
     * 📡 [সকেট মেসেজ প্রসেসর]: সার্ভার থেকে আসা রিয়েল-টাইম JSON মেসেজ প্রোসেস করার জন্য
     */
    public void processSocketMessage(String rawJson) {
        System.out.println("📥 GameScreen received socket message raw JSON: " + rawJson);
        Gdx.app.postRunnable(() -> {
            try {
                JsonValue json = new JsonReader().parse(rawJson);
                String action = json.getString("action", "");

                // 🎯 ১. টার্গেট প্লেয়ারের স্ক্রিনে এই কন্ডিশনটি ট্রু হবে (ব্যাটেল পপ-আপ রিকোয়েস্ট)
                if (action.equals("send_battle_request")) {
                    this.challengerId = json.getLong("senderId");
                    this.challengerName = json.getString("senderName");
                    this.incomingBattleRequest = true;
                    System.out.println("⚔️ POP-UP TRIGGERED! Challenge from: " + this.challengerName);
                }
                // ২. চ্যালেঞ্জ এক্সেপ্ট হলে দুজনকেই ম্যাচ স্টার্ট কমান্ড পাঠাবে
                else if (action.equals("start_battle")) {
                    this.incomingBattleRequest = false;
                    this.waitingForOpponentAccept = false;

                    String myName = (CurrentPlayer.username != null) ? CurrentPlayer.username : "Player";

                    Long enemyId = json.getLong("senderId");
                    String enemyName = json.getString("senderName");
                    if (enemyId.equals(CurrentPlayer.id)) {
                        enemyId = json.getLong("receiverId");
                        enemyName = json.getString("receiverName");
                    }

                    // 🔁 এখানে NewBattleScreen এর পরিবর্তে PokemonShowScreen এ পাঠানো হচ্ছে
                    game.setScreen(new PokemonShowScreen(game, myName, enemyName, enemyId));
                }
                // ৩. অপোনেন্ট যদি রিজেক্ট বা 'N' প্রেস করে
                else if (action.equals("decline_battle_request")) {
                    this.waitingForOpponentAccept = false;
                    System.out.println("❌ Opponent declined the battle request.");
                }
            } catch (Exception e) {
                System.err.println("Error parsing incoming socket message: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Override
    public void render(float delta) {
        collisionManager.clear();
        handleLogoutInput();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();

        // DRAW WORLD
        grassManager.draw(game.batch, delta);
        pokeballManager.draw(game.batch);
        tileManager.draw(game.batch);
        houseManager.draw(game.batch);
        otherManager.draw(game.batch);
        treeManager.draw(game.batch);
        onlinePlayerManager.draw(game.batch);

        player.update(delta);

        // ========================================================
        // 🎯 TRAINER COLLISION WITH FILTER (SPECIFIC TOUCH LOGIC)
        // ========================================================
        Rectangle[] trainers = onlinePlayerManager.getTrainerBounds();
        OnlinePlayerResponse[] activePlayers = onlinePlayerManager.getOnlinePlayersList();

        if (trainers != null && activePlayers != null && !waitingForOpponentAccept && !incomingBattleRequest) {
            for (int i = 0; i < trainers.length; i++) {
                if (trainers[i] == null || i >= activePlayers.length || activePlayers[i] == null) continue;

                OnlinePlayerResponse p = activePlayers[i];
                String currentUsername = (CurrentPlayer.username != null) ? CurrentPlayer.username : "";

                int pCount = 0;
                try { pCount = p.getPokemonCount(); } catch(Exception e) { pCount = 2; }

                if (!p.getUsername().equalsIgnoreCase(currentUsername) && p.getOnlineStatus() == 1 && pCount >= 2) {

                    if (player.getBounds().overlaps(trainers[i])) {
                        targetEnemyName = p.getUsername();
                        targetEnemyId = p.getPlayerId();

                        System.out.println("⚔️ Target Locked! Sending battle request to: " + targetEnemyName);

                        String jsonRequest = "{"
                            + "\"action\":\"send_battle_request\","
                            + "\"senderId\":" + CurrentPlayer.id + ","
                            + "\"senderName\":\"" + currentUsername + "\","
                            + "\"receiverId\":" + targetEnemyId + ","
                            + "\"receiverName\":\"" + targetEnemyName + "\""
                            + "}";
                        GameSocket.emit(jsonRequest);

                        waitingForOpponentAccept = true;
                        break;
                    }
                }
            }
        }

        // ========================================================
        // 🖥️ UI PROMPTS RENDERING (রিয়েল-টাইম পপ-আপ মেসেজ)
        // ========================================================

        if (waitingForOpponentAccept) {
            font.setColor(Color.ORANGE);
            font.draw(game.batch, "Waiting for " + targetEnemyName + " to accept the battle...", player.getX() - 100, player.getY() + 120);
            font.setColor(Color.WHITE);
        }

        if (incomingBattleRequest) {
            float uiX = game.camera.position.x - 150;
            float uiY = game.camera.position.y + 100;

            font.setColor(Color.CYAN);
            font.draw(game.batch, challengerName + " wants to battle with you!", uiX, uiY);

            font.setColor(Color.GREEN);
            font.draw(game.batch, "Press Y to ACCEPT / N to DECLINE", uiX, uiY - 30);
            font.setColor(Color.WHITE);

            if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
                System.out.println("✅ Y Pressed! Accepting challenge...");
                String jsonAccept = "{"
                    + "\"action\":\"accept_battle_request\","
                    + "\"senderId\":" + challengerId + ","
                    + "\"senderName\":\"" + challengerName + "\","
                    + "\"receiverId\":" + CurrentPlayer.id + ","
                    + "\"receiverName\":\"" + CurrentPlayer.username + "\""
                    + "}";
                GameSocket.emit(jsonAccept);
                incomingBattleRequest = false;
            }
            else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                System.out.println("❌ N Pressed! Declining challenge...");
                incomingBattleRequest = false;
                String jsonDecline = "{"
                    + "\"action\":\"decline_battle_request\","
                    + "\"senderId\":" + challengerId + ","
                    + "\"receiverId\":" + CurrentPlayer.id + ","
                    + "\"receiverName\":\"" + CurrentPlayer.username + "\""
                    + "}";
                GameSocket.emit(jsonDecline);
            }
        }

        // POKEBALL COLLISION
        for (int i = 0; i < 5; i++) {
            if (!pokeballManager.getCollected()[i] && player.getBounds().overlaps(pokeballManager.getPokeballs()[i])) {
                pokeballManager.getCollected()[i] = true;

                if (CurrentPlayer.pokeballs < 5) {
                    CurrentPlayer.pokeballs++;
                    String json = "{\"playerId\":" + CurrentPlayer.id + "}";

                    LoginApi.collectPokeball(json, new Net.HttpResponseListener() {
                        @Override public void handleHttpResponse(Net.HttpResponse httpResponse) {}
                        @Override public void failed(Throwable t) {}
                        @Override public void cancelled() {}
                    });
                }
            }
        }

        // TALL GRASS BATTLE
        if (player.getX() > 520 && player.getX() < 850 && player.getY() > 260 && player.getY() < 420) {
            if (Math.random() < 0.003) {
                battle = true;
            }
        }

        // DRAW PLAYER & USERNAME
        player.draw(game.batch);

        if (CurrentPlayer.username != null) {
            font.draw(game.batch, CurrentPlayer.username, player.getX() - 10, player.getY() + 80);
        }

        // ========================================================
        // 📊 ELIGIBLE TRAINERS CHART
        // ========================================================
        float chartX = game.camera.position.x - (Gdx.graphics.getWidth() / 2f) + 20;
        float chartY = game.camera.position.y + (Gdx.graphics.getHeight() / 2f) - 20;

        uiFont.draw(game.batch, "=== ELIGIBLE TRAINERS (ONLINE & 2+ POKEMON) ===", chartX, chartY);

        if (activePlayers != null && activePlayers.length > 0) {
            int displayedCount = 0;
            String currentUsername = (CurrentPlayer.username != null) ? CurrentPlayer.username : "";

            for (int i = 0; i < activePlayers.length; i++) {
                if (activePlayers[i] != null) {
                    OnlinePlayerResponse p = activePlayers[i];
                    int count = 2;
                    try { count = p.getPokemonCount(); } catch(Exception e){}

                    if (!p.getUsername().equalsIgnoreCase(currentUsername) && p.getOnlineStatus() == 1 && count >= 2) {
                        displayedCount++;
                        uiFont.draw(game.batch, displayedCount + ". " + p.getUsername() + " [" + count + " Pokemons Ready]", chartX, chartY - (displayedCount * 25));
                    }
                }
            }
            if (displayedCount == 0) {
                uiFont.draw(game.batch, "No eligible trainers online.", chartX, chartY - 25);
            }
        } else {
            uiFont.draw(game.batch, "No trainers nearby.", chartX, chartY - 25);
        }

        // WILD POKEMON ENCOUNTERS
        handleWildPokemonEncounters();

        // LOGOUT BUTTON DRAWING
        float buttonScreenX = game.camera.position.x - (Gdx.graphics.getWidth() / 2f) + logoutRect.x;
        float buttonScreenY = game.camera.position.y - (Gdx.graphics.getHeight() / 2f) + logoutRect.y;

        game.batch.draw(logoutButton, buttonScreenX, buttonScreenY, logoutRect.width, logoutRect.height);

        game.batch.end();
    }

    private void handleWildPokemonEncounters() {
        if (interactionManager.nearPikachu(player.getBounds())) {
            font.draw(game.batch, "PRESS E TO BATTLE PIKACHU", player.getX() - 40, player.getY() + 70);
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                game.setScreen(new BattleScreen(game, "pikachu"));
                return;
            }
        }
        if (interactionManager.nearCharmander(player.getBounds())) {
            font.draw(game.batch, "PRESS E TO BATTLE CHARMANDER", player.getX() - 40, player.getY() + 70);
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                game.setScreen(new BattleScreen(game, "charmander"));
                return;
            }
        }
        if (interactionManager.nearBulbasaur(player.getBounds())) {
            font.draw(game.batch, "PRESS E TO BATTLE BULBASAUR", player.getX() - 40, player.getY() + 70);
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                game.setScreen(new BattleScreen(game, "bulbasaur"));
                return;
            }
        }
        if (interactionManager.nearGreninja(player.getBounds())) {
            font.draw(game.batch, "PRESS E TO BATTLE GRENINJA", player.getX() - 40, player.getY() + 70);
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                game.setScreen(new BattleScreen(game, "greninja"));
                return;
            }
        }
    }

    private void handleLogoutInput() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (logoutRect.contains(mouseX, mouseY)) {
                System.out.println("🔴 LOGOUT CLICKED! Disconnecting Socket & Going to HomeScreen...");
                GameSocket.disconnect();
                game.setScreen(new HomeScreen(game));
            }
        }
    }

    @Override
    public void show() {
        GameSocket.setActiveScreen(this);
        System.out.println("🖥️ GameScreen is now active and listening to WebSocket requests.");
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
        GameSocket.setActiveScreen(null);
    }

    @Override
    public void dispose() {
        grassManager.dispose();
        treeManager.dispose();
        houseManager.dispose();
        otherManager.dispose();
        tileManager.dispose();
        pokeballManager.dispose();
        font.dispose();
        uiFont.dispose();
        player.dispose();
        onlinePlayerManager.dispose();
        logoutButton.dispose();
        System.out.println("🧹 GameScreen resources disposed successfully.");
    }
}
