package com.example.demo.bank;

import javax.servlet.http.HttpSession;

import com.fortanix.sdkms.v1.ApiClient;

public class LoginClient {

    // session ID value.
    public static void setSessionUserID(String ID, HttpSession session) {
        session.setAttribute("userID", ID);
    }
    public static String getSessionUserID(HttpSession session) {
        Object retVal = session.getAttribute("userID");
        if(retVal == null) return null;
        return (String) retVal;
    }


    // session ApiClient for Fortanix service.
    public static void setSessionApiClient(ApiClient client, HttpSession session) {
        session.setAttribute("ApiClient", client);
    }
    public static ApiClient getSessionApiClient(HttpSession session) {
        Object retVal = session.getAttribute("ApiClient");
        if(retVal == null) return null;
        return (ApiClient) retVal;
    }


    // session flag - 0: User, 1: Admin
    public static void setSessionFlag(int flag, HttpSession session) {
        session.setAttribute("flag", flag);
    }
    public static int getSessionFlag(HttpSession session) {
        Object retval = session.getAttribute("flag");
        if(retval == null) return -1;
        return (int) retval;
    }


    // session Availability checker.
    public static boolean isSessionAvailable(HttpSession session) {
        if (getSessionApiClient(session) == null || getSessionUserID(session) == null) return false;
        else if(getSessionFlag(session) != 0) return false;
        return true;
    }

    public static boolean isAdminSessionAvailable(HttpSession session) {
        if (getSessionApiClient(session) == null || getSessionUserID(session) == null) return false;
        else if(getSessionFlag(session) != 1) return false;
        return true;
    }
}