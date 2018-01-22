package com.pets.app.utilities;

import android.content.Context;
import android.content.Intent;

/**
 * Created by admin on 22/01/18.
 */

public class ShareUtils {
    public static void shareData(Context context, String body) {
        String shareBody = body +
                "\n\n";
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        shareBody += "Sent from Android";
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
