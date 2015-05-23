package com.beust.retro;

import com.google.gson.JsonObject;

import retrofit.RestAdapter;
import rx.Subscriber;

public class RetroTest {

    private static final String HOST = "http://api.openweathermap.org_";

    public static void main(String[] args) {
        new RetroTest().run();
    }

    private void run() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(HOST)
                .build();

        Weather service = adapter.create(Weather.class);

        service.weather()
            .subscribe(new Subscriber<JsonObject>() {
                @Override
                public void onNext(JsonObject o) {
                    System.out.println("Received: " + o);
                }

                @Override
                public void onCompleted() {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void onError(Throwable e) {
                    System.out.println("Error: " + e);
                    e.printStackTrace();
                }
            });
    }
}
