/*
 * Copyright 2020 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.web.android_intent_urls.twa;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class LauncherActivity
        extends com.google.androidbrowserhelper.trusted.LauncherActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setting an orientation crashes the app due to the transparent background on Android 8.0
        // Oreo and below. We only set the orientation on Oreo and above. This only affects the
        // splash screen and Chrome will still respect the orientation.
        // See https://github.com/GoogleChromeLabs/bubblewrap/issues/496 for details.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        // Get the intent that started this activity
        Intent intent = getIntent();
        Log.d("THE INTENT ITSELF", String.valueOf(intent));
        Log.d("INTENT ACTION", intent.getAction());
        Log.d("Intent.ACTION_VIEW",Intent.ACTION_VIEW);
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            Log.d("INTENT DATA", String.valueOf(intent.getData()));
            Log.d("INTENT EXTRAS", String.valueOf(intent.getExtras()));
            Bundle bundle = intent.getExtras();
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d("INTENT BUNDLE LOOP", String.format("%s %s (%s)", key,
                        value.toString(), value.getClass().getName()));
            }
            Uri data = intent.getData();
            if (data != null) {
                // Extract and handle the custom data from the intent
                String customData = data.getQueryParameter("S.custom_data");
                if (customData != null) {
                    // Now you have the custom data, you can use it in your app
                    // For example, you can log it or display it in a Toast
                    Log.d("CustomData", "Received custom data: " + customData);
                    Toast.makeText(this, "Received custom data: " + customData, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    protected Uri getLaunchingUrl() {
        // Get the original launch Url.
        Uri uri = super.getLaunchingUrl();

        uri = uri.buildUpon().appendQueryParameter("data_from_android", "static_data").build();
        uri = uri.buildUpon().appendQueryParameter("data_from_web", "static_data").build();

        return uri;
    }
}
