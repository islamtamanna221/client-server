//
//
//package com.codeexplorer.screen;
////
//import com.badlogic.gdx.utils.Json;
//
//import com.codeexplorer.model.LoginResponse;
//
//import com.codeexplorer.model.CurrentPlayer;
// //
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.Net;
//
//import com.badlogic.gdx.ScreenAdapter;
//
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//
//import com.badlogic.gdx.math.Rectangle;
//
//import com.codeexplorer.Main;
//
//import com.codeexplorer.network.LoginApi;
//
//public class LoginScreen extends ScreenAdapter {
//
//    private final Main game;
//
//    private Texture background;
//
//    private Texture loginButton;
//
//    private Texture textField;
//
//    private Texture backButton;
//
//    private Rectangle loginRect;
//
//    private Rectangle backRect;
//
//    private String email = "";
//
//    private String password = "";
//
//    private String message = "";
//
//    // 0=email
//    // 1=password
//
//    private int selectedField = 0;
//
//    public LoginScreen(
//        Main game
//    ) {
//
//        this.game = game;
//
//        background =
//            new Texture(
//                "ui/login_ui.jpg"
//            );
//
//        loginButton =
//            new Texture(
//                "ui/login_button.png"
//            );
//
//        textField =
//            new Texture(
//                "ui/sprite_0006.png"
//            );
//
//        backButton =
//            new Texture(
//                "ui/back_button.png"
//            );
//
//        loginRect =
//            new Rectangle(
//                520,
//                180,
//                250,
//                100
//            );
//
//        backRect =
//            new Rectangle(
//                40,
//                650,
//                80,
//                80
//            );
//    }
//
//    @Override
//    public void render(
//        float delta
//    ) {
//
//        handleInput();
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
//        game.batch.begin();
//
//        // BACKGROUND
//
//        game.batch.draw(
//
//            background,
//
//            0,
//
//            0,
//
//            Gdx.graphics.getWidth(),
//
//            Gdx.graphics.getHeight()
//        );
//
//        // BACK BUTTON
//
//        game.batch.draw(
//
//            backButton,
//
//            40,
//
//            650,
//
//            80,
//
//            80
//        );
//
//        // TITLE
//
//        game.font.draw(
//
//            game.batch,
//
//            "LOGIN ACCOUNT",
//
//            520,
//
//            730
//        );
//
//        // EMAIL
//
//        game.font.draw(
//
//            game.batch,
//
//            "ENTER EMAIL",
//
//            420,
//
//            620
//        );
//
//        game.batch.draw(
//
//            textField,
//
//            420,
//
//            550,
//
//            420,
//
//            55
//        );
//
//        game.font.draw(
//
//            game.batch,
//
//            email,
//
//            450,
//
//            585
//        );
//
//        // PASSWORD
//
//        game.font.draw(
//
//            game.batch,
//
//            "ENTER PASSWORD",
//
//            420,
//
//            500
//        );
//
//        game.batch.draw(
//
//            textField,
//
//            420,
//
//            430,
//
//            420,
//
//            55
//        );
//
//        game.font.draw(
//
//            game.batch,
//
//            password.replaceAll(
//                ".",
//                "*"
//            ),
//
//            450,
//
//            465
//        );
//
//        // BUTTON
//
//        game.batch.draw(
//
//            loginButton,
//
//            520,
//
//            220,
//
//            250,
//
//            100
//        );
//
//        // HELP
//
//        game.font.draw(
//
//            game.batch,
//
//            "TAB = CHANGE FIELD",
//
//            480,
//
//            140
//        );
//
//        game.font.draw(
//
//            game.batch,
//
//            "CLICK LOGIN BUTTON",
//
//            500,
//
//            100
//        );
//
//        // MESSAGE
//
//        game.font.draw(
//
//            game.batch,
//
//            "MESSAGE : " + message,
//
//            420,
//
//            60
//        );
//
//        game.batch.end();
//    }
//
//    // =====================================
//    // INPUT
//    // =====================================
//
//    private void handleInput() {
//
//        // TAB
//
//        if(
//
//            Gdx.input.isKeyJustPressed(
//                Input.Keys.TAB
//            )
//
//        ) {
//
//            selectedField++;
//
//            if(selectedField > 1) {
//
//                selectedField = 0;
//            }
//        }
//
//        // CLICK
//
//        if(
//
//            Gdx.input.isButtonJustPressed(
//                Input.Buttons.LEFT
//            )
//
//        ) {
//
//            float x =
//                Gdx.input.getX();
//
//            float y =
//                Gdx.graphics.getHeight()
//                    -
//                    Gdx.input.getY();
//
//            // BACK
//
//            if(
//
//                backRect.contains(
//                    x,
//                    y
//                )
//
//            ) {
//
//                game.setScreen(
//
//                    new HomeScreen(
//                        game
//                    )
//                );
//            }
//
//            // LOGIN
//
//            if(
//
//                loginRect.contains(
//                    x,
//                    y
//                )
//
//            ) {
//
//                login();
//            }
//        }
//
//        // LETTERS
//
//        for(char c='a'; c<='z'; c++) {
//
//            int key =
//
//                Input.Keys.valueOf(
//
//                    ("" + c).toUpperCase()
//                );
//
//            if(
//
//                Gdx.input.isKeyJustPressed(
//                    key
//                )
//
//            ) {
//
//                addCharacter(c);
//            }
//        }
//
//        // NUMBERS
//
//        for(int i=0; i<=9; i++) {
//
//            int key =
//                Input.Keys.NUM_0 + i;
//
//            if(
//
//                Gdx.input.isKeyJustPressed(
//                    key
//                )
//
//            ) {
//
//                addCharacter(
//                    (char)('0'+i)
//                );
//            }
//        }
//
//        // @
//
//        if(
//
//            Gdx.input.isKeyJustPressed(
//                Input.Keys.AT
//            )
//
//        ) {
//
//            addCharacter('@');
//        }
//
//        // .
//
//        if(
//
//            Gdx.input.isKeyJustPressed(
//                Input.Keys.PERIOD
//            )
//
//        ) {
//
//            addCharacter('.');
//        }
//
//        // BACKSPACE
//
//        if(
//
//            Gdx.input.isKeyJustPressed(
//                Input.Keys.BACKSPACE
//            )
//
//        ) {
//
//            removeCharacter();
//        }
//    }
//
//    // =====================================
//    // ADD CHARACTER
//    // =====================================
//
//    private void addCharacter(
//        char c
//    ) {
//
//        if(selectedField == 0) {
//
//            email += c;
//        }
//
//        else {
//
//            password += c;
//        }
//    }
//
//    // =====================================
//    // REMOVE CHARACTER
//    // =====================================
//
//    private void removeCharacter() {
//
//        if(selectedField == 0) {
//
//            if(email.length() > 0) {
//
//                email =
//                    email.substring(
//                        0,
//                        email.length()-1
//                    );
//            }
//        }
//
//        else {
//
//            if(password.length() > 0) {
//
//                password =
//                    password.substring(
//                        0,
//                        password.length()-1
//                    );
//            }
//        }
//    }
//
//    // =====================================
//    // LOGIN
//    // =====================================
//
//    private void login() {
//
//        if(
//
//            email.isEmpty()
//                ||
//                password.isEmpty()
//
//        ) {
//
//            message =
//                "ENTER EMAIL & PASSWORD";
//
//            return;
//        }
//
//        String json =
//
//            "{"
//
//                + "\"email\":\""
//                + email
//                + "\","
//
//                + "\"password\":\""
//                + password
//                + "\""
//
//                + "}";
//
//        LoginApi.login(
//
//            json,
//
//            new Net.HttpResponseListener() {
//
//                @Override
//                public void handleHttpResponse(
//
//                    Net.HttpResponse httpResponse
//
//                ) {
//
//                    String response =
//                        httpResponse
//                            .getResultAsString();
//
//                    Gdx.app.postRunnable(() -> {
//
//                        if(
//                            response.equals(
//                                "USER_NOT_FOUND"
//                            )
//                        ) {
//
//                            message =
//                                "USER NOT FOUND";
//
//                            return;
//                        }
//
//                        if(
//                            response.equals(
//                                "WRONG_PASSWORD"
//                            )
//                        ) {
//
//                            message =
//                                "WRONG PASSWORD";
//
//                            return;
//                        }
//
//                        Json jsonParser =
//                            new Json();
//
//                        LoginResponse player =
//
//                            jsonParser.fromJson(
//                                LoginResponse.class,
//                                response
//                            );
//
//                        CurrentPlayer.id =
//                            player.getId();
//
//                        CurrentPlayer.username =
//                            player.getUsername();
//
//                        CurrentPlayer.pokeballs =
//                            player.getPokeballs();
//
//                        message =
//                            "LOGIN SUCCESS";
//
//                        game.setScreen(
//                            new StartScreen(game)
//                        );
//                    });
//                }
//
//                @Override
//                public void failed(
//                    Throwable t
//                ) {
//
//                    message =
//                        "SERVER OFF";
//                }
//
//                @Override
//                public void cancelled() {
//
//                }
//            }
//        );
//    }
//}
//update
package com.codeexplorer.screen;

import com.badlogic.gdx.utils.Json;
import com.codeexplorer.model.LoginResponse;
import com.codeexplorer.model.CurrentPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.codeexplorer.Main;
import com.codeexplorer.network.LoginApi;
import com.codeexplorer.network.GameSocket; // 🚀 GameSocket ইম্পোর্ট করা হলো

public class LoginScreen extends ScreenAdapter {

    private final Main game;
    private Texture background;
    private Texture loginButton;
    private Texture textField;
    private Texture backButton;

    private Rectangle loginRect;
    private Rectangle backRect;

    private String email = "";
    private String password = "";
    private String message = "";

    // 0=email, 1=password
    private int selectedField = 0;

    public LoginScreen(Main game) {
        this.game = game;

        background = new Texture("ui/login_ui.jpg");
        loginButton = new Texture("ui/login_button.png");
        textField = new Texture("ui/sprite_0006.png");
        backButton = new Texture("ui/back_button.png");

        // 🎯 [ফিক্স]: বাটন ড্রয়িং পজিশন ২২০ এর সাথে ম্যাচ করে রেকট্যাঙ্গেল সেট করা হলো
        loginRect = new Rectangle(520, 220, 250, 100);
        backRect = new Rectangle(40, 650, 80, 80);
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        // BACKGROUND
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // BACK BUTTON
        game.batch.draw(backButton, 40, 650, 80, 80);

        // TITLE
        game.font.draw(game.batch, "LOGIN ACCOUNT", 520, 730);

        // EMAIL
        game.font.draw(game.batch, "ENTER EMAIL", 420, 620);
        game.batch.draw(textField, 420, 550, 420, 55);
        game.font.draw(game.batch, email, 450, 585);

        // PASSWORD
        game.font.draw(game.batch, "ENTER PASSWORD", 420, 500);
        game.batch.draw(textField, 420, 430, 420, 55);
        game.font.draw(game.batch, password.replaceAll(".", "*"), 450, 465);

        // BUTTON
        game.batch.draw(loginButton, 520, 220, 250, 100);

        // HELP
        game.font.draw(game.batch, "TAB = CHANGE FIELD", 480, 140);
        game.font.draw(game.batch, "CLICK LOGIN BUTTON", 500, 100);

        // MESSAGE
        game.font.draw(game.batch, "MESSAGE : " + message, 420, 60);

        game.batch.end();
    }

    // =====================================
    // INPUT
    // =====================================
    private void handleInput() {
        // TAB
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            selectedField++;
            if (selectedField > 1) {
                selectedField = 0;
            }
        }

        // CLICK
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();

            // BACK
            if (backRect.contains(x, y)) {
                game.setScreen(new HomeScreen(game));
            }

            // LOGIN
            if (loginRect.contains(x, y)) {
                login();
            }
        }

        // LETTERS
        for (char c = 'a'; c <= 'z'; c++) {
            int key = Input.Keys.valueOf(("" + c).toUpperCase());
            if (Gdx.input.isKeyJustPressed(key)) {
                addCharacter(c);
            }
        }

        // NUMBERS
        for (int i = 0; i <= 9; i++) {
            int key = Input.Keys.NUM_0 + i;
            if (Gdx.input.isKeyJustPressed(key)) {
                addCharacter((char) ('0' + i));
            }
        }

        // @
        if (Gdx.input.isKeyJustPressed(Input.Keys.AT)) {
            addCharacter('@');
        }

        // .
        if (Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) {
            addCharacter('.');
        }

        // BACKSPACE
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            removeCharacter();
        }
    }

    // =====================================
    // ADD CHARACTER
    // =====================================
    private void addCharacter(char c) {
        if (selectedField == 0) {
            email += c;
        } else {
            password += c;
        }
    }

    // =====================================
    // REMOVE CHARACTER
    // =====================================
    private void removeCharacter() {
        if (selectedField == 0) {
            if (email.length() > 0) {
                email = email.substring(0, email.length() - 1);
            }
        } else {
            if (password.length() > 0) {
                password = password.substring(0, password.length() - 1);
            }
        }
    }

    // =====================================
    // LOGIN
    // =====================================
    private void login() {
        if (email.isEmpty() || password.isEmpty()) {
            message = "ENTER EMAIL & PASSWORD";
            return;
        }

        String json = "{"
            + "\"email\":\"" + email + "\","
            + "\"password\":\"" + password + "\""
            + "}";

        LoginApi.login(json, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String response = httpResponse.getResultAsString();

                Gdx.app.postRunnable(() -> {
                    if (response.equals("USER_NOT_FOUND")) {
                        message = "USER NOT FOUND";
                        return;
                    }

                    if (response.equals("WRONG_PASSWORD")) {
                        message = "WRONG PASSWORD";
                        return;
                    }

                    Json jsonParser = new Json();
                    LoginResponse player = jsonParser.fromJson(LoginResponse.class, response);

                    CurrentPlayer.id = player.getId();
                    CurrentPlayer.username = player.getUsername();
                    CurrentPlayer.pokeballs = player.getPokeballs();

                    // 🚀 [সকেট কানেকশন]: আইডি পাওয়ার পর এখানে গেম সকেট কানেক্ট করা হলো
                    GameSocket.connect(CurrentPlayer.id);

                    message = "LOGIN SUCCESS";

                    game.setScreen(new StartScreen(game));
                });
            }

            @Override
            public void failed(Throwable t) {
                message = "SERVER OFF";
            }

            @Override
            public void cancelled() {
                // হ্যান্ডেল করার প্রয়োজন নেই
            }
        });
    }
}
