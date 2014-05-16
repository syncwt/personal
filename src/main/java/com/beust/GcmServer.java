package com.beust;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

public class GcmServer {

    private static final long serialVersionUID = 1L;
    
    // The SENDER_ID here is the "Browser Key" that was generated when I
    // created the API keys for my Google APIs project.
    private static final String SENDER_ID = "AIzaSyDG17ndLdksEBIjfY3-I9ud5_XF-LyBpGk";
     
    // This is a *cheat*  It is a hard-coded registration ID from an Android device
    // that registered itself with GCM using the same project id shown above.
    private static final String REG_ID = "APA91bGDHrdx2fryLj7KlCCnPJitvvHtH6VCFHrEQyIoewnFEU_wEOK_SkB9HZ78dE9VgcBkok0hTG-vLI6GNgAgt9_AdVjdsJy_omxLzBdUsN3MkaDkYaSQhH7NqxqcxtgwALrJI3vPmZejtQBJhnDY8-3MC9POK-Tj7BKAQCb55ke8CUGh-5U";
         
    // This array will hold all the registration ids used to broadcast a message.
    // for this demo, it will only have the DROID_BIONIC id that was captured 
    // when we ran the Android client app through Eclipse.
    private List<String> androidTargets = new ArrayList<String>();
        
    public GcmServer() {
        androidTargets.add(REG_ID);
    }
     
    public void run() {
         
        String userMessage = "Example message";
        String collapseKey = "collapseKey";
 
        // Instance of com.android.gcm.server.Sender, that does the
        // transmission of a Message to the Google Cloud Messaging service.
        Sender sender = new Sender(SENDER_ID);
         
        // This Message object will hold the data that is being transmitted
        // to the Android client devices.  For this demo, it is a simple text
        // string, but could certainly be a JSON object.
        Message message = new Message.Builder()
         
        // If multiple messages are sent using the same .collapseKey()
        // the android target device, if it was offline during earlier message
        // transmissions, will only receive the latest message for that key when
        // it goes back on-line.
            .collapseKey(collapseKey)
            .timeToLive(30)
            .delayWhileIdle(true)
            .addData("message", userMessage)
            .build();
         
        try {
            // use this for multicast messages.  The second parameter
            // of sender.send() will need to be an array of register ids.
            MulticastResult result = sender.send(message, androidTargets, 1);
             
            if (result.getResults() != null) {
                int canonicalRegId = result.getCanonicalIds();
                if (canonicalRegId != 0) {
                     
                }
            } else {
                int error = result.getFailure();
                System.out.println("Broadcast failure: " + error);
            }
             
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        // We'll pass the CollapseKey and Message values back to index.jsp, only so
        // we can display it in our form again.
//        request.setAttribute("CollapseKey", collapseKey);
//        request.setAttribute("Message", userMessage);
//         
//        request.getRequestDispatcher("index.jsp").forward(request, response);
                 
    }

    public static void main(String[] args) {
        new GcmServer().run();
    }
}
