package com.codepath.parstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject {
    public static final String KEY_PROFILE_IMAGE = "profileImage";

    public ParseFile getProfileImage() {
        return getParseFile(KEY_PROFILE_IMAGE);
    }

    public void setProfileImage(ParseFile parseFile) {
        put(KEY_PROFILE_IMAGE, parseFile);
    }
}
