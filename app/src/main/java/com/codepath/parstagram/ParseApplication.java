package com.codepath.parstagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // init parse
        Parse.initialize(new Parse.Configuration.Builder(this)
            .applicationId("xjvA0zGNCVqVoiO0XLFMsxlbZ3P1hnn1fxMmsFqe")
            .clientKey("TWbzULcPv2pDxdsURRxs5iupOISScWIhPP66nydx")
            .server("https://parseapi.back4app.com")
            .build()
        );
    }
}
