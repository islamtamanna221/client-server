//package com.codeexplorer.screen;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.codeexplorer.Main;
//import com.codeexplorer.model.CurrentPlayer;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//
//public class PokemonShowScreen implements Screen {
//
//    private final Main game;
//    private Texture grassBg;
//    private BitmapFont font;
//    private BitmapFont uiFont;
//
//    private String playerName;
//    private String enemyName;
//    private Long enemyId;
//
//    private ArrayList<PokemonModel> myPokemons;
//    private int selectedPokemonIndex = -1;
//    private String statusMessage = "👉 [SELECTION] Press number keys to select your Caught Pokemon, then press ENTER!";
//
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
//    public PokemonShowScreen(Main game, String playerName, String enemyName, Long enemyId) {
//        this.game = game;
//        this.playerName = playerName != null ? playerName : "PLAYER";
//        this.enemyName = enemyName != null ? enemyName : "ENEMY";
//        this.enemyId = enemyId;
//
//        grassBg = new Texture("battle/background/grass.png");
//
//        font = new BitmapFont();
//        font.getData().setScale(1.5f);
//
//        uiFont = new BitmapFont();
//        uiFont.getData().setScale(1.4f);
//
//        myPokemons = new ArrayList<>();
//        loadPokemonDataFromDatabase();
//    }
//
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
//    private void loadPokemonDataFromDatabase() {
//        myPokemons.clear();
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            Gdx.app.log("Database_Error", "Driver Class missing!");
//        }
//
//        String url = "jdbc:mysql://localhost:3306/pokemongame?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
//        String user = "root";
//        String password = "";
//
//        String query = "SELECT pokemon_id, current_hp FROM player_pokemon WHERE player_id = ?";
//        long currentPlayerId = (CurrentPlayer.id != null) ? CurrentPlayer.id : 21;
//
//        try {
//            Connection conn = DriverManager.getConnection(url, user, password);
//            PreparedStatement stmt = conn.prepareStatement(query);
//            stmt.setLong(1, currentPlayerId);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                int pId = rs.getInt("pokemon_id");
//                int hp = rs.getInt("current_hp");
//                myPokemons.add(new PokemonModel(pId, getPokemonNameById(pId), hp));
//            }
//            rs.close(); stmt.close(); conn.close();
//        } catch (Exception e) {
//            if (myPokemons.isEmpty()) {
//                myPokemons.add(new PokemonModel(4, "Pikachu", 100));
//                myPokemons.add(new PokemonModel(2, "Greninja", 100));
//            }
//        }
//    }
//
//    @Override
//    public void render(float delta) {
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        float screenWidth = Gdx.graphics.getWidth();
//        float screenHeight = Gdx.graphics.getHeight();
//
//        // পোকেমন সিলেকশন লজিক
//        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && myPokemons.size() > 0) selectedPokemonIndex = 0;
//        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && myPokemons.size() > 1) selectedPokemonIndex = 1;
//        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) && myPokemons.size() > 2) selectedPokemonIndex = 2;
//        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) && myPokemons.size() > 3) selectedPokemonIndex = 3;
//        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5) && myPokemons.size() > 4) selectedPokemonIndex = 4;
//        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6) && myPokemons.size() > 5) selectedPokemonIndex = 5;
//
//        if (selectedPokemonIndex != -1) {
//            statusMessage = "🎯 SELECTED: " + myPokemons.get(selectedPokemonIndex).name.toUpperCase() + "! Press ENTER to Lock!";
//        }
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
//            if (selectedPokemonIndex == -1) {
//                statusMessage = "❌ ERROR: Choose a Pokemon from your DB caught list first!";
//            } else {
//                // লক করার সাথে সাথে ব্যাটল স্ক্রিনে মুভ করবে এবং সিলেক্টেড ডাটা পাঠিয়ে দেবে
//                game.setScreen(new PokemonBattleScreen(game, playerName, enemyName, enemyId, myPokemons, selectedPokemonIndex));
//                return;
//            }
//        }
//
//        // রেন্ডারিং পার্ট
//        game.batch.begin();
//        game.batch.draw(grassBg, 0, 0, screenWidth, screenHeight);
//
//        uiFont.draw(game.batch, "=== MY CAUGHT POKEMON (FROM LIVE DB) ===", screenWidth / 2 - 210, screenHeight - 180);
//
//        if (myPokemons.isEmpty()) {
//            uiFont.draw(game.batch, "⚠️ No caught pokemon found in database for you!", screenWidth / 2 - 170, screenHeight - 240);
//        } else {
//            for (int i = 0; i < myPokemons.size(); i++) {
//                String mark = (selectedPokemonIndex == i) ? "👉 [READY] " : "   [" + (i + 1) + "] ";
//                uiFont.draw(game.batch, mark + myPokemons.get(i).name.toUpperCase() + " (HP: " + myPokemons.get(i).hp + ")", screenWidth / 2 - 140, screenHeight - 240 - (i * 45));
//            }
//        }
//
//        font.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
//        font.draw(game.batch, statusMessage, screenWidth / 2 - 280, 60);
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
//        grassBg.dispose();
//        font.dispose();
//        uiFont.dispose();
//    }
//}
//check
package com.codeexplorer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.codeexplorer.Main;
import com.codeexplorer.model.CurrentPlayer;
import com.codeexplorer.network.GameSocket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PokemonShowScreen implements Screen {

    private final Main game;
    private Texture grassBg;
    private BitmapFont font;
    private BitmapFont uiFont;

    private String playerName;
    private String enemyName;
    private Long enemyId;

    private ArrayList<PokemonModel> myPokemons;
    private int selectedPokemonIndex = -1;
    private String statusMessage = "👉 [SELECTION] Press number keys to select your Caught Pokemon, then press ENTER!";

    // 🎯 মুভ ডাটা মডেল ক্লাস
    public static class MoveModel {
        public String name;
        public int power;

        public MoveModel(String name, int power) {
            this.name = name;
            this.power = power;
        }
    }

    // 🎯 পোকেমন ডাটা মডেল ক্লাস (moves ভেরিয়েবলটি এখানে সম্পূর্ণ পাবলিক করা আছে)
    public static class PokemonModel {
        public int id;
        public String name;
        public int hp;
        public ArrayList<MoveModel> moves;

        public PokemonModel(int id, String name, int hp) {
            this.id = id;
            this.name = name;
            this.hp = hp;
            this.moves = new ArrayList<>();
        }
    }

    public PokemonShowScreen(Main game, String playerName, String enemyName, Long enemyId) {
        this.game = game;
        this.playerName = playerName != null ? playerName : "PLAYER";
        this.enemyName = enemyName != null ? enemyName : "ENEMY";
        this.enemyId = enemyId;

        grassBg = new Texture("battle/background/grass.png");
        font = new BitmapFont();
        font.getData().setScale(1.5f);
        uiFont = new BitmapFont();
        uiFont.getData().setScale(1.4f);

        myPokemons = new ArrayList<>();
        loadPokemonDataFromDatabase();
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
        String url = "jdbc:mysql://localhost:3306/pokemongame?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "";

        String pokemonQuery = "SELECT pokemon_id, current_hp FROM player_pokemon WHERE player_id = ?";
        long currentPlayerId = (CurrentPlayer.id != null) ? CurrentPlayer.id : 21;

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(pokemonQuery);
            stmt.setLong(1, currentPlayerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int pId = rs.getInt("pokemon_id");
                int hp = rs.getInt("current_hp");
                PokemonModel poke = new PokemonModel(pId, getPokemonNameById(pId), hp);

                // আপনার ডাটাবেজ স্কিমা অনুযায়ী কাস্টম ইনার জয়েন কুয়েরি
                String moveQuery = "SELECT m.name, m.power FROM moves m " +
                    "JOIN pokemon_moves pm ON m.id = pm.move_id " +
                    "WHERE pm.pokemon_id = ?";
                PreparedStatement moveStmt = conn.prepareStatement(moveQuery);
                moveStmt.setInt(1, pId);
                ResultSet moveRs = moveStmt.executeQuery();
                while (moveRs.next()) {
                    poke.moves.add(new MoveModel(moveRs.getString("name"), moveRs.getInt("power")));
                }
                moveRs.close();
                moveStmt.close();

                myPokemons.add(poke);
            }
            rs.close(); stmt.close(); conn.close();
        } catch (Exception e) {
            // ডাটাবেজ কানেকশন না পেলে সেফটি ফলব্যাক
            if (myPokemons.isEmpty()) {
                PokemonModel pikachu = new PokemonModel(4, "Pikachu", 100);
                pikachu.moves.add(new MoveModel("Thunder Shock", 20));
                pikachu.moves.add(new MoveModel("Thunderbolt", 50));
                myPokemons.add(pikachu);
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && myPokemons.size() > 0) selectedPokemonIndex = 0;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && myPokemons.size() > 1) selectedPokemonIndex = 1;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) && myPokemons.size() > 2) selectedPokemonIndex = 2;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) && myPokemons.size() > 3) selectedPokemonIndex = 3;

        if (selectedPokemonIndex != -1) {
            statusMessage = "🎯 SELECTED: " + myPokemons.get(selectedPokemonIndex).name.toUpperCase() + "! Press ENTER to Lock!";
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (selectedPokemonIndex == -1) {
                statusMessage = "❌ ERROR: Choose a Pokemon from your DB caught list first!";
            } else {
                PokemonModel mySelectedPokemon = myPokemons.get(selectedPokemonIndex);
                String lockPayload = "{"
                    + "\"action\":\"lock_pokemon\","
                    + "\"receiverId\":" + this.enemyId + ","
                    + "\"pokemonId\":" + mySelectedPokemon.id + ","
                    + "\"pokemonName\":\"" + mySelectedPokemon.name + "\""
                    + "}";
                GameSocket.emit(lockPayload);

                // ব্যাটল স্ক্রিনে পুরো অবজেক্ট লিস্ট পাস করা হচ্ছে
                game.setScreen(new PokemonBattleScreen(game, playerName, enemyName, enemyId, myPokemons, selectedPokemonIndex));
                return;
            }
        }

        game.batch.begin();
        game.batch.draw(grassBg, 0, 0, screenWidth, screenHeight);

        uiFont.draw(game.batch, "=== MY CAUGHT POKEMON (FROM LIVE DB) ===", screenWidth / 2 - 210, screenHeight - 180);

        if (myPokemons.isEmpty()) {
            uiFont.draw(game.batch, "⚠️ No caught pokemon found in database for you!", screenWidth / 2 - 170, screenHeight - 240);
        } else {
            for (int i = 0; i < myPokemons.size(); i++) {
                String mark = (selectedPokemonIndex == i) ? "👉 [READY] " : "   [" + (i + 1) + "] ";
                uiFont.draw(game.batch, mark + myPokemons.get(i).name.toUpperCase() + " (HP: " + myPokemons.get(i).hp + ")", screenWidth / 2 - 140, screenHeight - 240 - (i * 45));
            }
        }

        font.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
        font.draw(game.batch, statusMessage, screenWidth / 2 - 280, 60);
        game.batch.end();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { grassBg.dispose(); font.dispose(); uiFont.dispose(); }
}
