//package com.codeexplorer.screen;
//
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
//public class SignUpScreen extends ScreenAdapter {
//
//    private final Main game;
//
//    // =====================================
//    // TEXTURES
//    // =====================================
//
//    private Texture background;
//
//    private Texture signupButton;
//
//    private Texture loginButton;
//
//    private Texture textField;
//
//    private Texture backButton;
//
//    // =====================================
//    // BUTTON RECTANGLES
//    // =====================================
//
//    private Rectangle signupRect;
//
//    private Rectangle loginRect;
//
//    private Rectangle backRect;
//
//    // =====================================
//    // DATA
//    // =====================================
//
//    private String username = "";
//
//    private String email = "";
//
//    private String password = "";
//
//    private String message = "";
//
//    // 0=username
//    // 1=email
//    // 2=password
//
//    private int selectedField = 0;
//
//    // =====================================
//    // CONSTRUCTOR
//    // =====================================
//
//    public SignUpScreen(
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
//        signupButton =
//            new Texture(
//                "ui/signup_button.png"
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
//        signupRect =
//            new Rectangle(
//                350,
//                180,
//                250,
//                100
//            );
//
//        loginRect =
//            new Rectangle(
//                700,
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
//    // =====================================
//    // RENDER
//    // =====================================
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
//            "CREATE ACCOUNT",
//
//            520,
//
//            730
//        );
//
//        // USERNAME
//
//        game.font.draw(
//
//            game.batch,
//
//            "ENTER USERNAME",
//
//            420,
//
//            650
//        );
//
//        game.batch.draw(
//
//            textField,
//
//            420,
//
//            580,
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
//            username,
//
//            450,
//
//            615
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
//            540
//        );
//
//        game.batch.draw(
//
//            textField,
//
//            420,
//
//            470,
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
//            505
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
//            430
//        );
//
//        game.batch.draw(
//
//            textField,
//
//            420,
//
//            360,
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
//            395
//        );
//
//        // BUTTONS
//
//        game.batch.draw(
//
//            signupButton,
//
//            350,
//
//            180,
//
//            250,
//
//            100
//        );
//
//        game.batch.draw(
//
//            loginButton,
//
//            700,
//
//            180,
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
//            450,
//
//            120
//        );
//
//        game.font.draw(
//
//            game.batch,
//
//            "CLICK SIGNUP TO CREATE ACCOUNT",
//
//            420,
//
//            90
//        );
//
//        game.font.draw(
//
//            game.batch,
//
//            "CLICK LOGIN TO OPEN LOGIN PAGE",
//
//            400,
//
//            60
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
//            30
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
//            if(selectedField > 2) {
//
//                selectedField = 0;
//            }
//        }
//
//        // MOUSE CLICK
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
//            // BACK BUTTON
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
//            // SIGNUP
//
//            if(
//
//                signupRect.contains(
//                    x,
//                    y
//                )
//
//            ) {
//
//                register();
//            }
//
//            // LOGIN PAGE
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
//                game.setScreen(
//
//                    new LoginScreen(
//                        game
//                    )
//                );
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
//            username += c;
//        }
//
//        else if(selectedField == 1) {
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
//            if(username.length() > 0) {
//
//                username =
//                    username.substring(
//                        0,
//                        username.length()-1
//                    );
//            }
//        }
//
//        else if(selectedField == 1) {
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
//    // REGISTER
//    // =====================================
////
////    private void register() {
////
////        if(
////
////            username.isEmpty()
////                ||
////                email.isEmpty()
////                ||
////                password.isEmpty()
////
////        ) {
////
////            message =
////                "PLEASE FILL ALL FIELDS";
////
////            return;
////        }
////
////        String json =
////
////            "{"
////
////                + "\"username\":\""
////                + username
////                + "\","
////
////                + "\"email\":\""
////                + email
////                + "\","
////
////                + "\"password\":\""
////                + password
////                + "\""
////
////                + "}";
////
////        LoginApi.register(
////
////            json,
////
////            new Net.HttpResponseListener() {
////
////                @Override
////                public void handleHttpResponse(
////
////                    Net.HttpResponse httpResponse
////
////                ) {
////
////                    String response =
////                        httpResponse
////                            .getResultAsString();
////
////                    Gdx.app.postRunnable(() -> {
////
////                        message = response;
////
////                        if(
////
////                            response.equals(
////                                "REGISTER_SUCCESS"
////                            )
////
////                        ) {
////
////                            game.setScreen(
////
////                                new StartScreen(
////                                    game
////                                )
////                            );
////                        }
////                    });
////                }
////
////                @Override
////                public void failed(
////                    Throwable t
////                ) {
////
////                    message =
////                        "SERVER OFF";
////                }
////
////                @Override
////                public void cancelled() {
////
////                }
////            }
////        );
////    }
//    private void register() {
//
//        if(
//
//            username.isEmpty()
//                ||
//                email.isEmpty()
//                ||
//                password.isEmpty()
//
//        ) {
//
//            message =
//                "PLEASE FILL ALL FIELDS";
//
//            return;
//        }
//
//        String json =
//
//            "{"
//
//                + "\"username\":\""
//                + username
//                + "\","
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
//        LoginApi.register(
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
//                                "REGISTER_SUCCESS"
//                            )
//                        ) {
//
//                            game.setScreen(
//
//                                new LoginScreen(
//                                    game
//                                )
//                            );
//                        }
//
//                        else if(
//                            response.equals(
//                                "USERNAME_EXISTS"
//                            )
//                        ) {
//
//                            message =
//                                "USERNAME ALREADY EXISTS";
//                        }
//
//                        else if(
//                            response.equals(
//                                "EMAIL_EXISTS"
//                            )
//                        ) {
//
//                            message =
//                                "EMAIL ALREADY EXISTS";
//                        }
//
//                        else {
//
//                            message =
//                                response;
//                        }
//                    });
//                }
//
//                @Override
//                public void failed(
//                    Throwable t
//                ) {
//
//                    t.printStackTrace();
//
//                    Gdx.app.postRunnable(() -> {
//
//                        message =
//                            "SERVER OFF";
//                    });
//                }
//
//                @Override
//                public void cancelled() {
//
//                    Gdx.app.postRunnable(() -> {
//
//                        message =
//                            "REQUEST CANCELLED";
//                    });
//                }
//            }
//        );
//    }
//}
package com.codeexplorer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.codeexplorer.Main;
import com.codeexplorer.network.LoginApi;

public class SignUpScreen extends ScreenAdapter {

    private final Main game;

    // =====================================
    // TEXTURES
    // =====================================
    private Texture background;
    private Texture signupButton;
    private Texture loginButton;
    private Texture textField;
    private Texture backButton;

    // =====================================
    // BUTTON RECTANGLES
    // =====================================
    private Rectangle signupRect;
    private Rectangle loginRect;
    private Rectangle backRect;

    // =====================================
    // DATA
    // =====================================
    private String username = "";
    private String email = "";
    private String password = "";
    private String message = "";

    // 0=username, 1=email, 2=password
    private int selectedField = 0;

    // =====================================
    // CONSTRUCTOR
    // =====================================
    public SignUpScreen(Main game) {
        this.game = game;

        background = new Texture("ui/login_ui.jpg");
        signupButton = new Texture("ui/signup_button.png");
        loginButton = new Texture("ui/login_button.png");
        textField = new Texture("ui/sprite_0006.png");
        backButton = new Texture("ui/back_button.png");

        signupRect = new Rectangle(350, 180, 250, 100);
        loginRect = new Rectangle(700, 180, 250, 100);
        backRect = new Rectangle(40, 650, 80, 80);
    }

    // =====================================
    // RENDER
    // =====================================
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
        game.font.draw(game.batch, "CREATE ACCOUNT", 520, 730);

        // USERNAME
        game.font.draw(game.batch, "ENTER USERNAME", 420, 650);
        game.batch.draw(textField, 420, 580, 420, 55);
        game.font.draw(game.batch, username, 450, 615);

        // EMAIL
        game.font.draw(game.batch, "ENTER EMAIL", 420, 540);
        game.batch.draw(textField, 420, 470, 420, 55);
        game.font.draw(game.batch, email, 450, 505);

        // PASSWORD
        game.font.draw(game.batch, "ENTER PASSWORD", 420, 430);
        game.batch.draw(textField, 420, 360, 420, 55);
        game.font.draw(game.batch, password.replaceAll(".", "*"), 450, 395);

        // BUTTONS
        game.batch.draw(signupButton, 350, 180, 250, 100);
        game.batch.draw(loginButton, 700, 180, 250, 100);

        // HELP
        game.font.draw(game.batch, "TAB = CHANGE FIELD", 450, 120);
        game.font.draw(game.batch, "CLICK SIGNUP TO CREATE ACCOUNT & GO TO LOGIN", 360, 90);
        game.font.draw(game.batch, "CLICK LOGIN TO OPEN LOGIN PAGE", 400, 60);

        // MESSAGE
        game.font.draw(game.batch, "MESSAGE : " + message, 420, 30);

        game.batch.end();
    }

    // =====================================
    // INPUT
    // =====================================
    private void handleInput() {
        // TAB
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            selectedField++;
            if (selectedField > 2) {
                selectedField = 0;
            }
        }

        // MOUSE CLICK
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();

            // BACK BUTTON
            if (backRect.contains(x, y)) {
                game.setScreen(new HomeScreen(game));
            }

            // SIGNUP
            if (signupRect.contains(x, y)) {
                register();
            }

            // LOGIN PAGE
            if (loginRect.contains(x, y)) {
                game.setScreen(new LoginScreen(game));
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
            username += c;
        } else if (selectedField == 1) {
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
            if (username.length() > 0) {
                username = username.substring(0, username.length() - 1);
            }
        } else if (selectedField == 1) {
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
    // REGISTER
    // =====================================
    private void register() {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            message = "PLEASE FILL ALL FIELDS";
            return;
        }

        // JSON data string creation
        String json = "{"
            + "\"username\":\"" + username + "\","
            + "\"email\":\"" + email + "\","
            + "\"password\":\"" + password + "\""
            + "}";

        LoginApi.register(json, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String response = httpResponse.getResultAsString();

                // LibGDX-এর UI থ্রেডে রেসপন্স হ্যান্ডেল করার জন্য postRunnable ব্যবহার করা হয়েছে
                Gdx.app.postRunnable(() -> {
                    if (response.equals("REGISTER_SUCCESS")) {
                        // সফলভাবে সাইন-আপ হলে ডাটাবেজে সেভ হয়ে যাবে এবং সরাসরি LoginScreen-এ নিয়ে যাবে
                        game.setScreen(new LoginScreen(game));
                    } else if (response.equals("USERNAME_EXISTS")) {
                        message = "USERNAME ALREADY EXISTS";
                    } else if (response.equals("EMAIL_EXISTS")) {
                        message = "EMAIL ALREADY EXISTS";
                    } else {
                        message = response;
                    }
                });
            }

            @Override
            public void failed(Throwable t) {
                t.printStackTrace();
                Gdx.app.postRunnable(() -> {
                    message = "SERVER OFF";
                });
            }

            @Override
            public void cancelled() {
                Gdx.app.postRunnable(() -> {
                    message = "REQUEST CANCELLED";
                });
            }
        });
    }
}
