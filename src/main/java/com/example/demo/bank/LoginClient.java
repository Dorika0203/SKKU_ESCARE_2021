package com.example.demo.bank;

import javax.servlet.http.HttpSession;

import com.fortanix.sdkms.v1.ApiClient;

public class LoginClient {

    public static void setSessionUserID(String ID, HttpSession session) {
        session.setAttribute("userID", ID);
    }

    public static String getSessionUserID(HttpSession session) {
        Object retVal = session.getAttribute("userID");
        if(retVal == null) return null;
        return (String) retVal;
    }

    public static void setSessionApiClient(ApiClient client, HttpSession session) {
        session.setAttribute("ApiClient", client);
    }

    public static ApiClient getSessionApiClient(HttpSession session) {
        Object retVal = session.getAttribute("ApiClient");
        if(retVal == null) return null;
        return (ApiClient) retVal;
    }

}