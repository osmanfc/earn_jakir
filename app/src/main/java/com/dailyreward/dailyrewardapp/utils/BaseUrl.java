package com.dailyreward.dailyrewardapp.utils;

import android.util.Base64;

import com.dailyreward.dailyrewardapp.BuildConfig;


public class BaseUrl {
    public static final String BASE_URL = new String(Base64.decode(BuildConfig.ApiKey, Base64.DEFAULT));
    public static final String LOGIN_API = BASE_URL + BuildConfig.LoginKey;
    public static final String REGISTER_API = BASE_URL + BuildConfig.RegKey;
    public static final String UPDATE_PROFILE = BASE_URL + BuildConfig.UprofileKey;
    public static final String FORGOT_PASSWORD = BASE_URL + BuildConfig.FpKey;
    public static final String UPDATE_POINTS = BASE_URL + BuildConfig.UpointsKey;
    public static final String RESET_PASSWORD = BASE_URL + BuildConfig.RpKey;
    public static final String UPDATE_DATE = BASE_URL + BuildConfig.UdKey;
    public static final String ADMIN_SETTINGS = BASE_URL + BuildConfig.ASettingsKey;
    public static final String GAME_SETTINGS = BASE_URL + BuildConfig.GSettingsKey;
    public static final String REDEEM_SETTINGS = BASE_URL + BuildConfig.RSettingsKey;
}
