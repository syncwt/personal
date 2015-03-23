package com.yahoo.arrow;

import java.util.concurrent.ExecutionException;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ws.WebSocket;
import com.ning.http.client.ws.WebSocketListener;
import com.ning.http.client.ws.WebSocketUpgradeHandler;
import com.yahoo.arrow.pb.Chat;
import com.yahoo.arrow.pb.Chat.PBChatMessageNew;
import com.yahoo.arrow.pb.Chat.PBChatMessageNew.MessageType;
import com.yahoo.arrow.pb.Chat.PBChatMessageText;

public class WebSocketClient {

  private static final String GCM_KEY = "AIzaSyCHEqZzHGfhDOQ4-7Oz2cKivoQmK4wmVcQ";
  private static final String DEVICE_REG_ID =
      "APA91bFtBZm3o6kQhjFH4KOzsQTXV9iPL9INA8xZVmEIOkfti0wxfSE49zq1kEuqfEo5ry7YIXLnieYv6wq8SDkO5FwS7Jsq5SXrxU4cSMgEiE5oMOcQtpgY9gopKiGWd2bK4DqTnHoP6AImg1BT0-4hR6u5to0KAckw6sP1rB5_12NCS0iTg78";

  public static void main(String[] args) throws InterruptedException,
      ExecutionException {
    WebSocketClient a = new WebSocketClient();
    a.run();
  }

  private static final String HOST = "127.0.0.1"; // "10.73.213.206";
  private static final String URL_WEBSOCKET = "ws://" + HOST + ":8080/chat";

  private void run() throws InterruptedException, ExecutionException {
    Chat.PBChatEnvelope pbEnter = Chat.PBChatEnvelope.newBuilder()
        .setUserID(2L)
        .setRecipientUserID(1)
        .setType(Chat.PBChatEnvelope.EnvelopeType.ROOM_ENTER)
        .setRoomEnter(Chat.PBChatRoomEnter.newBuilder()
                .build())
        .build();
    Chat.PBChatEnvelope pbChat = Chat.PBChatEnvelope.newBuilder()
            .setUserID(2L)
            .setRecipientUserID(1)
            .setType(Chat.PBChatEnvelope.EnvelopeType.MESSAGE_NEW)
            .setMessageNew(PBChatMessageNew.newBuilder()
                .setType(MessageType.TEXT)
                .setText(PBChatMessageText.newBuilder()
                    .setBody("Hello from Eclipse")
                    .build())
                  .setTimestamp((int) System.currentTimeMillis())
                  .build())
            .build();
    final Chat.PBChatEnvelope pbPresence = Chat.PBChatEnvelope.newBuilder()
            .setUserID(2L)
            .setRecipientUserID(1)
            .setType(Chat.PBChatEnvelope.EnvelopeType.PRESENCE)
            .setPresence(Chat.PBChatPresence.newBuilder()
                    .setType(Chat.PBChatPresence.PresenceType.IDLE)
                    .build())
            .build();

    final Chat.PBChatEnvelope pb = pbPresence;
    System.out.println("Connecting to " + URL_WEBSOCKET);
    AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder()
            .setAcceptAnyCertificate(true)
//            .setReadTimeout(1000)
//            .setPooledConnectionIdleTimeout(1000)
//            .setConnectTimeout(1000)
//            .setRequestTimeout(1000)
            .setWebSocketTimeout(1000)
            .build();
    AsyncHttpClient c = new AsyncHttpClient(config);
    WebSocket websocket = c
        .prepareGet(URL_WEBSOCKET)
        .addHeader("Authorization", "Token token=\"4f15a264178f|a2c3992f2d8d4e608a563a67f1db5982\"")
        .execute(
            new WebSocketUpgradeHandler.Builder().addWebSocketListener(
                new WebSocketListener() {

                  @Override
                  public void onOpen(WebSocket websocket) {
                    System.out.println("onOpen websocket");
                    try {
                        Thread.sleep(5000);
                        websocket.sendMessage(pb.toByteArray());
                        websocket.sendMessage(pbPresence.toByteArray());
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                  }

                  @Override
                  public void onClose(WebSocket websocket) {
                    System.out.println("onClose websocket");
                  }

                  @Override
                  public void onError(Throwable t) {
                    System.out.println("onError");
                    t.printStackTrace();
                  }
                })
              .build())
          .get();
    System.out.println("Built websocket");
    websocket.close();
    c.close();
  }
}
