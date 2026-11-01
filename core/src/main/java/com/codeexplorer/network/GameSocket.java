////package com.codeexplorer.network;
////
////import org.java_websocket.client.WebSocketClient;
////import org.java_websocket.handshake.ServerHandshake;
////import java.net.URI;
////
////public class GameSocket {
////
////    private static WebSocketClient client;
////
////    // ⚡️ রিয়েল-টাইম ব্যাটল ট্র্যাকিং ভেরিয়েবলস
////    public static boolean enemyThrow = false;
////    public static String enemyPokemonName = ""; // 👈 নিউ ব্যাটল স্ক্রিন এখান থেকে লাইভ নাম রিড করবে
////
////    public static void connect() {
////        try {
////            client = new WebSocketClient(new URI("ws://localhost:9090/game")) {
////
////                @Override
////                public void onOpen(ServerHandshake handshake) {
////                    System.out.println("CONNECTED");
////                }
////
////                @Override
////                public void onMessage(String message) {
////                    System.out.println("RECEIVED : " + message);
////
////                    // 🌐 [রিসিভার ফিক্স]: চেক করা হচ্ছে মেসেজটি THROW দিয়ে শুরু হয়েছে কি না
////                    if (message != null && message.startsWith("THROW")) {
////                        // যদি মেসেজে নাম থাকে (যেমন "THROW:Bulbasaur") তবে নাম আলাদা করা হবে
////                        if (message.contains(":")) {
////                            String[] parts = message.split(":");
////                            if (parts.length > 1) {
////                                GameSocket.enemyPokemonName = parts[1].trim(); // ওপাশ থেকে আসা পোকেমনের নাম স্টোর হলো
////                            }
////                        } else {
////                            GameSocket.enemyPokemonName = "Pikachu"; // কোনো কারণে নাম মিস হলে সেফটি ফলব্যাক
////                        }
////
////                        // বল ছোঁড়ার ট্র্রিগার ট্রু করা হলো
////                        enemyThrow = true;
////                    }
////                }
////
////                @Override
////                public void onClose(int code, String reason, boolean remote) {
////                    System.out.println("SOCKET CLOSED");
////                }
////
////                @Override
////                public void onError(Exception ex) {
////                    ex.printStackTrace();
////                }
////            };
////
////            client.connect();
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
////
////    // 🎯 [সেন্ডার ফিক্স]: এখন থেকে এই মেথডটি সিলেক্টেড পোকেমনের নামসহ সার্ভারে ডাটা পাঠাবে
////    public static void sendThrow(String pokemonName) {
////        if (client != null && client.isOpen()) {
////            // ডাটা ফরম্যাট পাঠানো হচ্ছে: "THROW:Bulbasaur" বা "THROW:Pikachu"
////            String payload = "THROW:" + pokemonName;
////            client.send(payload);
////            System.out.println("SENT : " + payload);
////        }
////    }
////
////    // 🔄 পুরনো মেথডটি ওভারলোড হিসেবে রাখা হলো যাতে আগের কোনো কোডে এরর না আসে
////    public static void sendThrow() {
////        sendThrow("Pikachu");
////    }
////}
////update
////package com.codeexplorer.network;
////
////import org.java_websocket.client.WebSocketClient;
////import org.java_websocket.handshake.ServerHandshake;
////import java.net.URI;
////
////public class GameSocket {
////
////    private static WebSocketClient client;
////
////    // ⚡️ রিয়েল-টাইম ব্যাটল ট্র্যাকিং ভেরিয়েবলস
////    public static boolean enemyThrow = false;
////    public static String enemyPokemonName = ""; // 👈 নিউ ব্যাটল স্ক্রিন এখান থেকে লাইভ নাম রিড করবে
////
////    // 🎯 আপডেট করা মেইন কানেক্ট মেথড (প্যারামিটার সহ এবং থ্রেড সেফ)
////    public static void connect(long playerId) {
////        // 🚀 মেইন গেম থ্রেড যেন ব্লক বা ফ্রিজ না হয়, তাই নতুন একটি ব্যাকগ্রাউন্ড থ্রেড তৈরি করা হলো
////        new Thread(() -> {
////            try {
////                // ইউআরএল-এর সাথে playerId কোয়েরি প্যারামিটার যুক্ত করা হলো
////                String urlWithId = "ws://localhost:9090/game?playerId=" + playerId;
////                System.out.println("🎯 Attempting connection to: " + urlWithId);
////
////                client = new WebSocketClient(new URI(urlWithId)) {
////                    @Override
////                    public void onOpen(ServerHandshake handshake) {
////                        System.out.println("CONNECTED TO SERVER SUCCESSFULLY!");
////                    }
////
////                    @Override
////                    public void onMessage(String message) {
////                        System.out.println("RECEIVED : " + message);
////
////                        // 🌐 [রিসিভার ফিক্স]: চেক করা হচ্ছে মেসেজটি THROW দিয়ে শুরু হয়েছে কি না
////                        if (message != null && message.startsWith("THROW")) {
////                            // যদি মেসেজে নাম থাকে (যেমন "THROW:Bulbasaur") তবে নাম আলাদা করা হবে
////                            if (message.contains(":")) {
////                                String[] parts = message.split(":");
////                                if (parts.length > 1) {
////                                    GameSocket.enemyPokemonName = parts[1].trim(); // ওপাশ থেকে আসা পোকেমনের নাম স্টোর হলো
////                                }
////                            } else {
////                                GameSocket.enemyPokemonName = "Pikachu"; // কোনো কারণে নাম মিস হলে সেফটি ফলব্যাক
////                            }
////
////                            // বল ছোঁড়ার ট্র্রিগার ট্রু করা হলো
////                            enemyThrow = true;
////                        }
////                    }
////
////                    @Override
////                    public void onClose(int code, String reason, boolean remote) {
////                        System.out.println("SOCKET CLOSED. Reason: " + reason);
////                    }
////
////                    @Override
////                    public void onError(Exception ex) {
////                        System.out.println("SOCKET ERROR: " + ex.getMessage());
////                        ex.printStackTrace();
////                    }
////                };
////
////                // ব্যাকগ্রাউন্ড থ্রেডে কানেকশন ব্লক হলে গেম মেইন স্ক্রিন ফ্রিজ হবে না
////                // এবং এটি কানেকশন সফল হওয়া নিশ্চিত করবে
////                client.connectBlocking();
////
////            } catch (Exception e) {
////                System.err.println("❌ Connection failed in GameSocket Thread:");
////                e.printStackTrace();
////            }
////        }).start(); // 👈 থ্রেডটি চালু করা হলো
////    }
////
////    // 🔄 পুরনো মেথড ওভারলোড করে রাখা হলো যাতে কোনো স্ক্রিনে হঠাৎ এরর না আসে
////    public static void connect() {
////        // কোনো আইডি না পাওয়া গেলে ডিফল্ট হিসেবে ০ পাঠানো হচ্ছে
////        connect(0);
////    }
////
////    // 🎯 [সেন্ডার ফিক্স]: এখন থেকে এই মেথডটি সিলেক্টেড পোকেমনের নামসহ সার্ভারে ডাটা পাঠাবে
////    public static void sendThrow(String pokemonName) {
////        if (client != null && client.isOpen()) {
////            // ডাটা ফরম্যাট পাঠানো হচ্ছে: "THROW:Bulbasaur" বা "THROW:Pikachu"
////            String payload = "THROW:" + pokemonName;
////            client.send(payload);
////            System.out.println("SENT : " + payload);
////        }
////    }
////
////    // 🔄 পুরনো মেথডটি ওভারলোড হিসেবে রাখা হলো যাতে আগের কোনো কোডে এরর না আসে
////    public static void sendThrow() {
////        sendThrow("Pikachu");
////    }
////
////    // 🔌 সকেট ম্যানুয়ালি বন্ধ করার মেথড (যেমন প্লেয়ার লগআউট করলে বা গেম ক্লোজ করলে)
////    public static void disconnect() {
////        if (client != null && client.isOpen()) {
////            client.close();
////            System.out.println("Disconnected from WebSocket server.");
////        }
////    }
////}
////check
//package com.codeexplorer.network;
//
//import com.codeexplorer.screen.GameScreen; // 👈 নতুন যুক্ত হলো স্ক্রিনকে নোটিফাই করার জন্য
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.handshake.ServerHandshake;
//import java.net.URI;
//
//public class GameSocket {
//
//    private static WebSocketClient client;
//
//    // ⚡️ রিয়েল-টাইম ব্যাটল ট্র্যাকিং ভেরিয়েবলস
//    public static boolean enemyThrow = false;
//    public static String enemyPokemonName = ""; // 👈 নিউ ব্যাটল স্ক্রিন এখান থেকে লাইভ নাম রিড করবে
//
//    // 🎯 [নতুন ভেরিয়েবল]: বর্তমান সচল স্ক্রিনের ট্র্যাকিং রাখার জন্য (আগের কোডে কোনো প্রভাব ফেলবে না)
//    private static GameScreen activeScreen;
//
//    public static void setActiveScreen(GameScreen screen) {
//        activeScreen = screen;
//    }
//
//    // 🎯 আপডেট করা মেইন কানেক্ট মেথড (প্যারামিটার সহ এবং থ্রেড সেফ)
//    public static void connect(long playerId) {
//        // 🚀 মেইন গেম থ্রেড যেন ব্লক বা ফ্রিজ না হয়, তাই নতুন একটি ব্যাকগ্রাউন্ড থ্রেড তৈরি করা হলো
//        new Thread(() -> {
//            try {
//                // 🎯 [মেইন ফিক্স]: আপনার ব্যাকএন্ডের পোর্ট (9090) অনুযায়ী ইউআরএল পরিবর্তন করা হলো
//                String urlWithId = "ws://localhost:9090/game?playerId=" + playerId;
//                System.out.println("🎯 Attempting connection to: " + urlWithId);
//
//                client = new WebSocketClient(new URI(urlWithId)) {
//                    @Override
//                    public void onOpen(ServerHandshake handshake) {
//                        System.out.println("CONNECTED TO SERVER SUCCESSFULLY!");
//                    }
//
//                    @Override
//                    public void onMessage(String message) {
//                        System.out.println("RECEIVED : " + message);
//
//                        // 🌐 [রিসিভার ফিক্স]: চেক করা হচ্ছে মেসেজটি THROW দিয়ে শুরু হয়েছে কি না
//                        if (message != null && message.startsWith("THROW")) {
//                            // যদি মেসেজে নাম থাকে (যেমন "THROW:Bulbasaur") তবে নাম আলাদা করা হবে
//                            if (message.contains(":")) {
//                                String[] parts = message.split(":");
//                                if (parts.length > 1) {
//                                    GameSocket.enemyPokemonName = parts[1].trim(); // ওপাশ থেকে আসা পোকেমনের নাম স্টোর হলো
//                                }
//                            } else {
//                                GameSocket.enemyPokemonName = "Pikachu"; // কোনো কারণে নাম মিস হলে সেফটি ফলব্যাক
//                            }
//
//                            // বল ছোঁড়ার ট্র্রিগার ট্রু করা হলো
//                            enemyThrow = true;
//                        }
//
//                        // ⚔️ [নতুন অ্যাডিশন]: ইনকামিং ব্যাটল JSON মেসেজ হ্যান্ডেল করার লজিক (আগের লজিক ডিস্টার্ব করবে না)
//                        if (message != null && message.startsWith("{") && message.endsWith("}")) {
//                            if (activeScreen != null) {
//                                activeScreen.processSocketMessage(message);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onClose(int code, String reason, boolean remote) {
//                        System.out.println("SOCKET CLOSED. Reason: " + reason);
//                    }
//
//                    @Override
//                    public void onError(Exception ex) {
//                        System.out.println("SOCKET ERROR: " + ex.getMessage());
//                        ex.printStackTrace();
//                    }
//                };
//
//                // バックグラウンドスレッドで接続がブロックされてもゲームメイン画面はフリーズしない
//                // এবং এটি কানেকশন সফল হওয়া নিশ্চিত করবে
//                client.connectBlocking();
//
//            } catch (Exception e) {
//                System.err.println("❌ Connection failed in GameSocket Thread:");
//                e.printStackTrace();
//            }
//        }).start(); // 👈 থ্রেডটি চালু করা হলো
//    }
//
//    // 🔄 পুরনো মেথড ওভারলোড করে রাখা হলো যাতে কোনো স্ক্রিনে হঠাৎ এরর না আসে
//    public static void connect() {
//        // কোনো আইডি না পাওয়া গেলে ডিফল্ট হিসেবে ০ পাঠানো হচ্ছে
//        connect(0);
//    }
//
//    // 🎯 [সেন্ডার ফিক্স]: এখন থেকে এই মেথডটি সিলেক্টেড পোকেমনের নামসহ সার্ভারে ডাটা পাঠাবে
//    public static void sendThrow(String pokemonName) {
//        if (client != null && client.isOpen()) {
//            // ডাটা ফরম্যাট পাঠানো হচ্ছে: "THROW:Bulbasaur" বা "THROW:Pikachu"
//            String payload = "THROW:" + pokemonName;
//            client.send(payload);
//            System.out.println("SENT : " + payload);
//        }
//    }
//
//    // 🔄 পুরনো মেথডটি ওভারলোড হিসেবে রাখা হলো যাতে আগের কোনো কোডে এরর না আসে
//    public static void sendThrow() {
//        sendThrow("Pikachu");
//    }
//
//    // 🎯 [নতুন মেথড]: ব্যাটল রিকোয়েস্ট বা এক্সেপ্টেন্সের JSON স্ট্রিং সার্ভারে পাঠানোর জন্য
//    public static void emit(String jsonMessage) {
//        if (client != null && client.isOpen()) {
//            client.send(jsonMessage);
//            System.out.println("SENT JSON: " + jsonMessage);
//        }
//    }
//
//    // 🔌 সকেট ম্যানুয়ালি বন্ধ করার মেথড (যেমন প্লেয়ার লগআউট করলে বা গেম ক্লোজ করলে)
//    public static void disconnect() {
//        if (client != null && client.isOpen()) {
//            client.close();
//            System.out.println("Disconnected from WebSocket server.");
//        }
//    }
//}
//n
package com.codeexplorer.network;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;

public class GameSocket {

    private static WebSocketClient client;

    // ⚡️ রিয়েল-টাইম ব্যাটল ট্র্যাকিং ভেরিয়েবলস
    public static boolean enemyThrow = false;
    public static String enemyPokemonName = "";

    // 🎯 [আলটিমেট ফিক্স]: ডাটা টাইপ Object করা হলো যাতে যেকোনো স্ক্রিন এখানে সেট হতে পারে
    private static Object activeScreen;

    public static void setActiveScreen(Object screen) {
        activeScreen = screen;
    }

    // 🎯 আপডেট করা মেইন কানেক্ট মেথড (প্যারামিটার সহ এবং থ্রেড সেফ)
    public static void connect(long playerId) {
        // 🚀 মেইন গেম থ্রেড যেন ব্লক বা ফ্রিজ না হয়, তাই নতুন একটি ব্যাকগ্রাউন্ড থ্রেড তৈরি করা হলো
        new Thread(() -> {
            try {
                // 🎯 আপনার ব্যাকএন্ডের পোর্ট (9090) অনুযায়ী ইউআরএল
                String urlWithId = "wss://pokemon-server-4.onrender.com/game?playerId=" + playerId;
                System.out.println("🎯 Attempting connection to: " + urlWithId);

                client = new WebSocketClient(new URI(urlWithId)) {
                    @Override
                    public void onOpen(ServerHandshake handshake) {
                        System.out.println("CONNECTED TO SERVER SUCCESSFULLY!");
                    }

                    @Override
                    public void onMessage(String message) {
                        System.out.println("RECEIVED : " + message);

                        // 🌐 [রিসিভার ফিক্স]: চেক করা হচ্ছে মেসেজটি THROW দিয়ে শুরু হয়েছে কি না
                        if (message != null && message.startsWith("THROW")) {
                            if (message.contains(":")) {
                                String[] parts = message.split(":");
                                if (parts.length > 1) {
                                    GameSocket.enemyPokemonName = parts[1].trim();
                                }
                            } else {
                                GameSocket.enemyPokemonName = "Pikachu";
                            }
                            enemyThrow = true;
                        }

                        // ⚔️ JSON ব্যাটল মেসেজ হ্যান্ডেলিং (ডাইনামিক কাস্টিং)
                        if (message != null && message.startsWith("{") && message.endsWith("}")) {
                            if (activeScreen != null) {
                                // 🌟 স্ক্রিনের টাইপ চিনে নিয়ে সেই অনুযায়ী মেথড কল করা হচ্ছে
                                if (activeScreen instanceof com.codeexplorer.screen.NewBattleScreen) {
                                    ((com.codeexplorer.screen.NewBattleScreen) activeScreen).processSocketMessage(message);
                                } else if (activeScreen instanceof com.codeexplorer.screen.GameScreen) {
                                    ((com.codeexplorer.screen.GameScreen) activeScreen).processSocketMessage(message);
                                }
                            }
                        }
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        System.out.println("SOCKET CLOSED. Reason: " + reason);
                    }

                    @Override
                    public void onError(Exception ex) {
                        System.out.println("SOCKET ERROR: " + ex.getMessage());
                    }
                };

                client.connectBlocking();

            } catch (Exception e) {
                System.err.println("❌ Connection failed in GameSocket Thread:");
                e.printStackTrace();
            }
        }).start();
    }

    public static void connect() {
        connect(0);
    }

    // 🎯 [সেন্ডার ফিক্স]: সিলেক্টেড পোকেমনের নামসহ সার্ভারে ডাটা পাঠাবে
    public static void sendThrow(String pokemonName) {
        if (client != null && client.isOpen()) {
            String payload = "THROW:" + pokemonName;
            client.send(payload);
            System.out.println("SENT : " + payload);
        }
    }

    public static void sendThrow() {
        sendThrow("Pikachu");
    }

    // 🎯 [নতুন মেথড]: ব্যাটল রিকোয়েস্ট বা এক্সেপ্টেন্সের JSON স্ট্রিং সার্ভারে পাঠানোর জন্য
    public static void emit(String jsonMessage) {
        if (client != null && client.isOpen()) {
            client.send(jsonMessage);
            System.out.println("SENT JSON: " + jsonMessage);
        }
    }

    // 🔌 সকেট ম্যানুয়ালি বন্ধ করার মেথড
    public static void disconnect() {
        if (client != null && client.isOpen()) {
            client.close();
            System.out.println("Disconnected from WebSocket server.");
        }
    }
}
