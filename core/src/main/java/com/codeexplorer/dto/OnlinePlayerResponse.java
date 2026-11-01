//package com.codeexplorer.dto;
//
//public class OnlinePlayerResponse {
//
//    private Long playerId;
//    private String username;
//    private int pokemonCount;
//
//    public Long getPlayerId() {
//        return playerId;
//    }
//
//    public void setPlayerId(Long playerId) {
//        this.playerId = playerId;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public int getPokemonCount() {
//        return pokemonCount;
//    }
//
//    public void setPokemonCount(int pokemonCount) {
//        this.pokemonCount = pokemonCount;
//    }
//}
//package com.codeexplorer.dto;
//
//public class OnlinePlayerResponse {
//
//    private Long playerId;
//    private String username;
//    private int pokemonCount;
//
//    // 💡 গেম ম্যাপে ট্রেইনারের পজিশন ট্র্যাক করার জন্য ২টা নতুন ভেরিয়েবল
//    private float x;
//    private float y;
//
//    // 💡 ১. ডিফল্ট কনস্ট্রাক্টর (স্প্রিং বুটের জ্যাকসন লাইব্রেরির JSON ম্যাপিংয়ের জন্য এটি দরকার)
//    public OnlinePlayerResponse() {
//    }
//
//    // 💡 ২. প্যারামিটারাইজড কনস্ট্রাক্টর (আপনার AuthService-এর নিউ অবজেক্ট তৈরির জন্য)
//    public OnlinePlayerResponse(Long playerId, String username, int pokemonCount) {
//        this.playerId = playerId;
//        this.username = username;
//        this.pokemonCount = pokemonCount;
//
//        // ম্যাপে দেখানোর জন্য ট্রেইনারদের ইনিশিয়াল ডামি পজিশন (আপনার প্রয়োজন মতো পরে চেঞ্জ করতে পারবেন)
//        this.x = 300f;
//        this.y = 400f;
//    }
//
//    // ========================================================
//    // GETTERS & SETTERS
//    // ========================================================
//
//    public Long getPlayerId() {
//        return playerId;
//    }
//
//    public void setPlayerId(Long playerId) {
//        this.playerId = playerId;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public int getPokemonCount() {
//        return pokemonCount;
//    }
//
//    public void setPokemonCount(int pokemonCount) {
//        this.pokemonCount = pokemonCount;
//    }
//
//    public float getX() {
//        return x;
//    }
//
//    public void setX(float x) {
//        this.x = x;
//    }
//
//    public float getY() {
//        return y;
//    }
//
//    public void setY(float y) {
//        this.y = y;
//    }
//}
//check
package com.codeexplorer.dto;

public class OnlinePlayerResponse {

    private Long playerId;
    private String username;
    private int pokemonCount;

    // 💡 গেম ম্যাপে ট্রেইনারের পজিশন ট্র্যাক করার জন্য ভেরিয়েবল
    private float x;
    private float y;

    // 🎯 [নতুন সংযোজন]: অনলাইন স্ট্যাটাস চেক ফিল্ড
    private int onlineStatus = 1;

    // 💡 ১. ডিফল্ট কনস্ট্রাক্টর (JSON ম্যাপিংয়ের জন্য এটি দরকার)
    public OnlinePlayerResponse() {
    }

    // 💡 ২. প্যারামিটারাইজড কনস্ট্রাক্টর
    public OnlinePlayerResponse(Long playerId, String username, int pokemonCount) {
        this.playerId = playerId;
        this.username = username;
        this.pokemonCount = pokemonCount;

        // ম্যাপে দেখানোর জন্য ট্রেইনারদের ইনিশিয়াল ডামি পজিশন
        this.x = 300f;
        this.y = 400f;
    }

    // ========================================================
    // GETTERS & SETTERS
    // ========================================================

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPokemonCount() {
        return pokemonCount;
    }

    public void setPokemonCount(int pokemonCount) {
        this.pokemonCount = pokemonCount;
    }

    // 🎯 GameScreen-এর p.getPokemons() এরর দূর করার ব্রিজ মেথড
    public int getPokemons() {
        return pokemonCount;
    }

    // 🎯 GameScreen-এর p.getOnlineStatus() এরর দূর করার মেথড
    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
