package com.surajhuljute.bloodbud;

/**
 * Created by Abhi Burk on 07/08/2017.
 */
public class Config {
    //URL to our login.php file
    public static final String LOGIN_URL = "https://huljutes.000webhostapp.com/login.php";
    public static final String FORGOT_URL = "https://huljutes.000webhostapp.com/sendforgot.php";
    public static final String FORGOT_URL_MOBNO = "https://huljutes.000webhostapp.com/sendforgotmobno.php";
    public static final String REGISTER_URL = "https://huljutes.000webhostapp.com/registration.php";

    //Keys for email and password as defined in our $_POST['key'] in login.php
     public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_MOBNO = "mobno";

    //If server response is equal to this that means login is successful
    public static final String SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences

    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    public static final String SEARCH_URL = "https://huljutes.000webhostapp.com/myjson.php";
    public static final String SEARCH_URL1 = "https://huljutes.000webhostapp.com/myjson2.php";
    public static final String KEY_SEARCH_VAL = "search_keyword";
    public static final String FETCH_USER = "https://huljutes.000webhostapp.com/fetch_user.php";




    //Keys for email and password as defined in our $_POST['key'] in login.php


    //If server response is equal to this that means login is successful


    //This would be used to store the email of current logged in user

    public static final String ISVERIFIED_SHARED_PREF = "is_verified";




    public static final String CONFIRM_URL = "https://huljutes.000webhostapp.com/check_otp.php";
    public static final String KEY_OTP = "otp";
    public static final String RESEND_OTP_URL = "https://huljutes.000webhostapp.com/resend_otp.php";

    public static final String LOGIN_USER_URL = "https://huljutes.000webhostapp.com/get_login_user.php";
    public static final String LOGIN_USER_KEY = "login_user_key";
    public static final String UPDATE_LOGIN_USER = "https://huljutes.000webhostapp.com/update_login_user.php";
    public static final String DONATED_BLOOD = "https://huljutes.000webhostapp.com/blood_donated.php";

    //JSON Tag from response from server
    public static final String TAG_RESPONSE= "ErrorMessage";


}