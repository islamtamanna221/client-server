//
//
//package com.codeexplorer;
//
//import com.badlogic.gdx.Game;
//import com.badlogic.gdx.Gdx;
//
//import com.badlogic.gdx.Net.HttpMethods;
//import com.badlogic.gdx.Net.HttpRequest;
//import com.badlogic.gdx.Net.HttpResponse;
//import com.badlogic.gdx.Net.HttpResponseListener;
//
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//
//import com.codeexplorer.screen.LoginScreen; // LoginScreen ইমপোর্ট করা হলো
//import com.codeexplorer.network.GameSocket;
//
//public class Main extends Game {
//
//    // =====================================
//    // DRAW
//    // =====================================
//    public SpriteBatch batch;
//
//    // =====================================
//    // FONT
//    // =====================================
//    public BitmapFont font;
//
//    // =====================================
//    // CAMERA
//    // =====================================
//    public OrthographicCamera camera;
//
//    @Override
//    public void create() {
//        // =====================================
//        // SPRITE BATCH
//        // =====================================
//        batch = new SpriteBatch();
//
//        // =====================================
//        // FONT
//        // =====================================
//        font = new BitmapFont();
//
//        // =====================================
//        // CAMERA SETUP
//        // =====================================
//        camera = new OrthographicCamera();
//        camera.setToOrtho(false, 1366, 768);
//        camera.position.set(1366 / 2f, 768 / 2f, 0);
//        camera.update();
//
//        // =====================================
//        // NETWORK SOCKET CONNECTION
//        // =====================================
//        GameSocket.connect();
//
//        // =====================================
//        // BACKEND TEST (HTTP GET)
//        // =====================================
//        HttpRequest request = new HttpRequest(HttpMethods.GET);
//        request.setUrl("http://localhost:8080/auth/test");
//
//        Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
//            @Override
//            public void handleHttpResponse(HttpResponse httpResponse) {
//                String response = httpResponse.getResultAsString();
//                System.out.println("SERVER RESPONSE : " + response);
//            }
//
//            @Override
//            public void failed(Throwable t) {
//                System.out.println("FAILED TO CONNECT BACKEND");
//                t.printStackTrace();
//            }
//
//            @Override
//            public void cancelled() {
//                System.out.println("REQUEST CANCELLED");
//            }
//        });
//
//        // =====================================
//        // FIRST SCREEN
//        // =====================================
//        // ডুপ্লিকেট ক্রিয়েট মেথডটি রিমুভ করে সরাসরি এখানে LoginScreen সেট করা হলো
//        setScreen(new LoginScreen(this));
//    }
//
//    @Override
//    public void render() {
//        // =====================================
//        // UPDATE & APPLY CAMERA
//        // =====================================
//        camera.update();
//        batch.setProjectionMatrix(camera.combined);
//
//        super.render();
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        camera.setToOrtho(false, 1366, 768);
//        camera.update();
//    }
//
//    @Override
//    public void dispose() {
//        if (batch != null) batch.dispose();
//        if (font != null) font.dispose();
//    }
//}
package com.codeexplorer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.codeexplorer.screen.HomeScreen; // HomeScreen Import
import com.codeexplorer.network.GameSocket;

public class Main extends Game {

    // =====================================
    // DRAW
    // =====================================
    public SpriteBatch batch;

    // =====================================
    // FONT
    // =====================================
    public BitmapFont font;

    // =====================================
    // CAMERA
    // =====================================
    public OrthographicCamera camera;

    @Override
    public void create() {

        // =====================================
        // SPRITE BATCH
        // =====================================
        batch = new SpriteBatch();

        // =====================================
        // FONT
        // =====================================
        font = new BitmapFont();

        // =====================================
        // CAMERA SETUP
        // =====================================
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1366, 768);
        camera.position.set(1366 / 2f, 768 / 2f, 0);
        camera.update();

        // =====================================
        // NETWORK SOCKET CONNECTION
        // =====================================
        GameSocket.connect();

        // =====================================
        // BACKEND TEST (HTTP GET)
        // =====================================
        HttpRequest request = new HttpRequest(HttpMethods.GET);
        request.setUrl("http://localhost:8080/auth/test");

        Gdx.net.sendHttpRequest(request, new HttpResponseListener() {

            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                String response = httpResponse.getResultAsString();
                System.out.println("SERVER RESPONSE : " + response);
            }

            @Override
            public void failed(Throwable t) {
                System.out.println("FAILED TO CONNECT BACKEND");
                t.printStackTrace();
            }

            @Override
            public void cancelled() {
                System.out.println("REQUEST CANCELLED");
            }
        });

        // =====================================
        // FIRST SCREEN
        // =====================================
        setScreen(new HomeScreen(this));
    }

    @Override
    public void render() {

        // =====================================
        // UPDATE & APPLY CAMERA
        // =====================================
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        super.render();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 1366, 768);
        camera.update();
    }

    @Override
    public void dispose() {

        if (batch != null)
            batch.dispose();

        if (font != null)
            font.dispose();
    }
}
