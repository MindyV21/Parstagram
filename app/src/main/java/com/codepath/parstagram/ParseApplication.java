package com.codepath.parstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);

        // init parse
        Parse.initialize(new Parse.Configuration.Builder(this)
            .applicationId(getString(R.string.parse_application_id))
            .clientKey(getString(R.string.parse_client_key))
            .server("https://parseapi.back4app.com")
            .build()
        );
    }
}
