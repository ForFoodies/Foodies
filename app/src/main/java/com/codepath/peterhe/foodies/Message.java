package com.codepath.peterhe.foodies;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String USER_ID_KEY = "userId";
    public static final String BODY_KEY = "body";
    public static final String GROUP_ID_KEY = "groupId";

    public String getUserId() {
        return getString(USER_ID_KEY);
    }
    public String getGroupId() {
        return getString(GROUP_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setUserId(String userId) {
        put(USER_ID_KEY, userId);
    }
    public void setGroupId(String groupId) {
        put(GROUP_ID_KEY, groupId);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }
}
