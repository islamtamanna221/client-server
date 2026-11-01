

package com.codeexplorer.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

public class LoginApi {


    private static final String BASE_URL =
        "https://pokemon-server-4.onrender.com/auth";

    // REGISTER
    public static void register(
        String json,
        Net.HttpResponseListener listener
    ) {
        sendRequest(
            Net.HttpMethods.POST,
            BASE_URL + "/register",
            json,
            listener
        );
    }

    // LOGIN
    public static void login(
        String json,
        Net.HttpResponseListener listener
    ) {
        sendRequest(
            Net.HttpMethods.POST,
            BASE_URL + "/login",
            json,
            listener
        );
    }

    // SAVE POKEMON
    public static void savePokemon(
        String json,
        Net.HttpResponseListener listener
    ) {
        sendRequest(
            Net.HttpMethods.POST,
            BASE_URL + "/save-pokemon",
            json,
            listener
        );
    }

    // USE POKEBALL
    public static void usePokeball(
        String json,
        Net.HttpResponseListener listener
    ) {
        sendRequest(
            Net.HttpMethods.POST,
            BASE_URL + "/use-pokeball",
            json,
            listener
        );
    }

    // COLLECT POKEBALL
    public static void collectPokeball(
        String json,
        Net.HttpResponseListener listener
    ) {
        sendRequest(
            Net.HttpMethods.POST,
            BASE_URL + "/collect-pokeball",
            json,
            listener
        );
    }

    // ONLINE PLAYERS
    public static void getOnlinePlayers(
        Net.HttpResponseListener listener
    ) {
        sendRequest(
            Net.HttpMethods.GET,
            BASE_URL + "/online-players",
            null,
            listener
        );
    }

    // COMMON HTTP REQUEST METHOD
    private static void sendRequest(
        String method,
        String url,
        String json,
        Net.HttpResponseListener listener
    ) {
        HttpRequestBuilder builder =
            new HttpRequestBuilder();

        Net.HttpRequest request =
            builder.newRequest()
                .method(method)
                .url(url)
                .header("Content-Type", "application/json")
                .content(json == null ? "" : json)
                .build();

        Gdx.net.sendHttpRequest(request, listener);
    }
}
