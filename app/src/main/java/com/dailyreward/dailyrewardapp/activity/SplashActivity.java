package com.dailyreward.dailyrewardapp.activity;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.dailyreward.dailyrewardapp.App;
import com.dailyreward.dailyrewardapp.R;
import com.dailyreward.dailyrewardapp.models.User;
import com.dailyreward.dailyrewardapp.utils.BaseUrl;
import com.dailyreward.dailyrewardapp.utils.Constant;
import com.dailyreward.dailyrewardapp.utils.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class SplashActivity extends AppCompatActivity {
    boolean LOGIN = false;
    private AppUpdateManager appUpdateManager;
    public static final int RC_APP_UPDATE = 101;
    SplashActivity activity;
    String user_name = null;
    private int retry_settings = 0, retry_details = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = this;


        String is_login = Constant.getString(activity, Constant.IS_LOGIN);
        if (is_login.equals("true")) {
            LOGIN = true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e("TAG", "onCreate:if part activarte ");
            appUpdateManager = AppUpdateManagerFactory.create(this);
            UpdateApp();
        } else {
            Log.e("TAG", "onCreate:else part activarte ");
            onInit();
        }
    }


    private void onInit() {
        if (Constant.isNetworkAvailable(activity)) {
            if (LOGIN) {
                try {
                    String tag_json_obj = "json_login_req";
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("get_login", "any");
                    params.put("email", Constant.getString(activity, Constant.USER_EMAIL));
                    params.put("password", Constant.getString(activity, Constant.USER_PASSWORD));
                    CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                            BaseUrl.LOGIN_API, params, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("TAG", response.toString());

                            try {
                                boolean status = response.getBoolean("status");
                                if (status) {
                                    JSONObject jsonObject = response.getJSONObject("0");
                                    Constant.setString(activity, Constant.USER_ID, jsonObject.getString("id"));
                                    final User user = new User(jsonObject.getString("name"), jsonObject.getString("number"), jsonObject.getString("email"), jsonObject.getString("device"), jsonObject.getString("points"), jsonObject.getString("referraled_with"), jsonObject.getString("status"), jsonObject.getString("referral_code"));

                                    if (response.has("date")) {
                                        Constant.setString(activity, Constant.TODAY_DATE, response.getString("date"));
                                    } else {
                                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                        Constant.setString(activity, Constant.TODAY_DATE, currentDate);
                                    }

                                    if (jsonObject.has("daily_check_in")) {
                                        Constant.setString(activity, Constant.LAST_DATE, jsonObject.getString("daily_check_in"));
                                    }
                                    if (jsonObject.has("last_date_watch")) {
                                        Constant.setString(activity, Constant.LAST_DATE_WATCH, jsonObject.getString("last_date_watch"));
                                    }
                                    if (jsonObject.has("last_date_invalid")) {
                                        Constant.setString(activity, Constant.LAST_DATE_INVALID, jsonObject.getString("last_date_invalid"));
                                    }
                                    if (jsonObject.has("scratch_date")) {
                                        Constant.setString(activity, Constant.LAST_DATE_SCRATCH, jsonObject.getString("scratch_date"));
                                    }
                                    if (jsonObject.has("scratch_count")) {
                                        Constant.setString(activity, Constant.SCRATCH_COUNT, jsonObject.getString("scratch_count"));
                                    }

                                    if (jsonObject.has("spin_date")) {
                                        Constant.setString(activity, Constant.LAST_DATE_SPIN, jsonObject.getString("spin_date"));
                                    }
                                    if (jsonObject.has("spin_count")) {
                                        Constant.setString(activity, Constant.SPIN_COUNT, jsonObject.getString("spin_count"));
                                    }
                                    if (jsonObject.has("captcha_count")) {
                                        Constant.setString(activity, Constant.CAPTCHA_COUNT, jsonObject.getString("captcha_count"));
                                    }
                                    if (jsonObject.has("captcha_date")) {
                                        Constant.setString(activity, Constant.LAST_DATE_CAPTCHA, jsonObject.getString("captcha_date"));
                                    }
                                    if (jsonObject.has("singup_bounus_date")) {
                                        Constant.setString(activity, Constant.SIGNUP_BOUNUS_DATE, jsonObject.getString("singup_bounus_date"));
                                    }
                                    if (jsonObject.has("quiz_count")) {
                                        Constant.setString(activity, Constant.QUIZ_COUNT, jsonObject.getString("quiz_count"));
                                    }
                                    if (jsonObject.has("quiz_date")) {
                                        Constant.setString(activity, Constant.LAST_DATE_QUIZ, jsonObject.getString("quiz_date"));
                                    }
                                    if (jsonObject.has("game_count")) {
                                        Constant.setString(activity, Constant.GAME_COUNT, jsonObject.getString("game_count"));
                                    }
                                    if (jsonObject.has("game_date")) {
                                        Constant.setString(activity, Constant.LAST_DATE_GAME, jsonObject.getString("game_date"));
                                    }
                                    if (user.getName() != null) {
                                        Constant.setString(activity, Constant.USER_NAME, user.getName());
                                        Log.e("TAG", "onDataChange: " + user.getName());
                                    }
                                    if (user.getNumber() != null) {
                                        Constant.setString(activity, Constant.USER_NUMBER, user.getNumber());
                                        Log.e("TAG", "onDataChange: " + user.getNumber());
                                    }
                                    if (user.getEmail() != null) {
                                        Constant.setString(activity, Constant.USER_EMAIL, user.getEmail());
                                        Log.e("TAG", "onDataChange: " + user.getEmail());
                                    }
                                    if (user.getDevice() != null) {
                                        Constant.setString(activity, Constant.USER_DEVICE, user.getDevice());
                                        Log.e("TAG", "onDataChange: " + user.getDevice());
                                    }
                                    if (user.getPoints() != null) {
//                                        if (!Constant.getString(activity, Constant.LAST_TIME_ADD_TO_SERVER).equals("")) {
//                                            Log.e("TAG", "onDataChange: Last time not empty");
//                                            if (!Constant.getString(activity, Constant.USER_POINTS).equals("")) {
//                                                Log.e("TAG", "onDataChange: user points not empty");
//                                                if (Constant.getString(activity, Constant.IS_UPDATE).equalsIgnoreCase("")) {
//                                                    Constant.setString(activity, Constant.USER_POINTS, Constant.getString(activity, Constant.USER_POINTS));
//                                                    Log.e("TAG", "onDataChange: " + user.getPoints());
//                                                    Constant.setString(activity, Constant.IS_UPDATE, "true");
//                                                } else {
//                                                    Constant.setString(activity, Constant.IS_UPDATE, "true");
//                                                    Constant.setString(activity, Constant.USER_POINTS, user.getPoints());
//                                                    Log.e("TAG", "onDataChange: " + user.getPoints());
//                                                }
//                                            }
//                                        }
                                        Constant.setString(activity, Constant.USER_POINTS, user.getPoints());
                                    }
                                    if (user.getReferCode() != null) {
                                        Constant.setString(activity, Constant.REFER_CODE, user.getReferCode());
                                        Log.e("TAG", "onDataChange: " + user.getReferCode());
                                    }
                                    if (user.getIsBLocked() != null) {
                                        Constant.setString(activity, Constant.USER_BLOCKED, user.getIsBLocked());
                                        Log.e("TAG", "onDataChange: " + user.getIsBLocked());
                                    }
                                    if (user.getUserReferCode() != null) {
                                        Constant.setString(activity, Constant.USER_REFFER_CODE, user.getUserReferCode());
                                        Log.e("TAG", "onDataChange: " + user.getUserReferCode());
                                    }

                                    if (Constant.getString(activity, Constant.USER_BLOCKED).equals("0")) {
                                        Constant.showBlockedDialog(activity, activity, getResources().getString(R.string.you_are_blocked));
                                    } else {
                                        Log.e("TAG", "onInit: login pART");
                                        getSettingsFromAdminPannel();
                                        getGameSettingsFromAdminPannel();
                                        getRedeemSettingsFromAdminPannel();
                                    }
                                } else {
//                                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//                                    Constant.setString(activity, Constant.TODAY_DATE, currentDate);
//                                    Log.e("TAG", "onInit: user_information from database");
//                                    Constant.setString(activity, Constant.IS_LOGIN, "");
//                                    Constant.GotoNextActivity(activity, LoginActivity.class, "");
//                                    overridePendingTransition(R.anim.enter, R.anim.exit);
//                                    finish();
                                    // here get ads beetween and daily scratch limit daily spin limit, daily check in points, coin to rupee,captcha count
                                    getSettingsFromAdminPannel();
                                    getGameSettingsFromAdminPannel();
                                    getRedeemSettingsFromAdminPannel();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            VolleyLog.d("TAG", "Error: " + error.getMessage());
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Constant.showToastMessage(activity, getResources().getString(R.string.slow_internet_connection));
                            }
                        }
                    });
                    jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                            1000 * 20,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    // Adding request to request queue
                    App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

                } catch (Exception e) {
                    Log.e("TAG", "onInit: excption " + e.getMessage().toString());
                }
            } else {
                if (Constant.getString(activity, Constant.USER_BLOCKED).equals("0")) {
                    Constant.showBlockedDialog(activity, activity, getResources().getString(R.string.you_are_blocked));
                    return;
                }
                getSettingsFromAdminPannel();
                getGameSettingsFromAdminPannel();
                getRedeemSettingsFromAdminPannel();
                // here get ads beetween and daily scratch limit daily spin limit, daily check in points, coin to rupee,captcha count
            }
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }
    }

    private void getSettingsFromAdminPannel() {
        if (Constant.isNetworkAvailable(activity)) {
            try {
                String tag_json_obj = "json_login_req";
                Map<String, String> map = new HashMap<>();
                map.put("get_settings", "any");
                CustomVolleyJsonRequest customVolleyJsonRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.ADMIN_SETTINGS, map, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {
                                JSONObject jb = response.getJSONObject("0");
                                Constant.setString(activity, Constant.ADS_BEETWEEN, jb.getString("ads_between"));
                                Constant.setString(activity, Constant.DAILY_SCRATCH_COUNT, jb.getString("daily_scratch_limit"));
                                Constant.setString(activity, Constant.DAILY_SPIN_COUNT, jb.getString("daily_spin_limit"));
                                Constant.setString(activity, Constant.DAILY_CHECK_IN_POINTS, jb.getString("daily_check_in_points"));
                                Constant.setString(activity, Constant.ADS_CLICK_COINS, jb.getString("ads_click_coins"));
                                Constant.setString(activity, Constant.ADS_CLICK_AFTER_X_CLICK, jb.getString("ads_click_after_x_click"));
                                Constant.setString(activity, Constant.ADS_CLICK_TIME, jb.getString("ads_click_time"));
                                Constant.setString(activity, Constant.DAILY_WATCH_POINTS, jb.getString("daily_watch_points"));
                                Constant.setString(activity, Constant.COIN_TO_RUPEE, jb.getString("coin_to_rupee_text"));
                                Constant.setString(activity, Constant.DAILY_CAPTCHA_COUNT, jb.getString("daily_captcha_limit"));
                                Constant.setString(activity, Constant.DAILY_QUIZ_COUNT, jb.getString("daily_quiz_limit"));
                                Constant.setString(activity, Constant.MINIMUM_REDEEM_POINTS, jb.getString("minimum_redeem_points"));
                                Constant.setString(activity, Constant.AD_TYPE, jb.getString("ad_type"));
                                Constant.setString(activity, Constant.ADMOB_BANNER_ID, jb.getString("admob_banner_id"));
                                Constant.setString(activity, Constant.ADMOB_INTERSTITAL_ID, jb.getString("admob_interstital_id"));
                                Constant.setString(activity, Constant.ADMOB_REWARDED_ID, jb.getString("admob_rewarded_id"));

                                Constant.setString(activity, Constant.UNITY_BANNER_ID, jb.getString("unity_banner_id"));
                                Constant.setString(activity, Constant.UNITY_INTERSTITAL_ID, jb.getString("unity_interstital_id"));
                                Constant.setString(activity, Constant.UNITY_REWARDED_ID, jb.getString("unity_rewarded_id"));

                                Constant.setString(activity, Constant.STARTAPP_BANNER_ID, jb.getString("startapp_banner_id"));
                                Constant.setString(activity, Constant.STARTAPP_INTERSTITAL_ID, jb.getString("startapp_interstital_id"));
                                Constant.setString(activity, Constant.VPN_ENABLE, jb.getString("is_vpn_enable"));
                                Constant.setString(activity, Constant.VPN_LOGO, jb.getString("vpn_logo"));
                                Constant.setString(activity, Constant.VPN_MSG, jb.getString("vpn_msg"));
                                Constant.setString(activity, Constant.VPN_LINK, jb.getString("vpn_link"));
                                Constant.setString(activity, Constant.IS_P_UPDATE_ENABLED, jb.getString("is_p_update_enabled"));
                                Constant.setString(activity, Constant.REFER_TEXT, jb.getString("refer_text"));


                                Constant.setString(activity, Constant.SPIN_PRICE_COIN, jb.getString("spin_price_coins"));
                                Constant.setString(activity, Constant.SCRATCH_PRICE_COIN, jb.getString("scratch_price_coins"));
                                Constant.setString(activity, Constant.CAPTCHA_PRICE_COIN, jb.getString("captcha_price_coins"));
                                Constant.setString(activity, Constant.SIGNUP_BOUNUS, jb.getString("signup_points"));
                                Constant.setString(activity, Constant.UNITY_GAME_ID, jb.getString("unity_game_id"));
                                Constant.setString(activity, Constant.STARTAPP_APP_ID, jb.getString("startapp_app_id"));
                                Constant.setString(activity, Constant.ADMOB_APP_ID, jb.getString("admob_app_id"));
                                Constant.setString(activity, Constant.SHARE_TEXT, jb.getString("share_text"));
                                Constant.setString(activity, Constant.IS_PROMOTION_DIALOG_ENABLE, jb.getString("is_promotion_dialog_enable"));
                                Constant.setString(activity, Constant.PROMOTION_TEXT, jb.getString("promotion_text"));
                                Constant.setString(activity, Constant.PROMOTION_LINK, jb.getString("promotion_link"));
                                Constant.setString(activity, Constant.SPIN_POINT_1, jb.getString("spin_point_1"));
                                Constant.setString(activity, Constant.SPIN_POINT_2, jb.getString("spin_point_2"));
                                Constant.setString(activity, Constant.SPIN_POINT_3, jb.getString("spin_point_3"));
                                Constant.setString(activity, Constant.SPIN_POINT_4, jb.getString("spin_point_4"));
                                Constant.setString(activity, Constant.SPIN_POINT_5, jb.getString("spin_point_5"));
                                Constant.setString(activity, Constant.SPIN_POINT_6, jb.getString("spin_point_6"));
                                Constant.setString(activity, Constant.SPIN_POINT_7, jb.getString("spin_point_7"));
                                Constant.setString(activity, Constant.SPIN_POINT_8, jb.getString("spin_point_8"));
                                Constant.setString(activity, Constant.SPIN_POINT_9, jb.getString("spin_point_9"));
                                Constant.setString(activity, Constant.SPIN_POINT_10, jb.getString("spin_point_10"));
                                Constant.setString(activity, Constant.IS_P1_ENABLED, jb.getString("is_p1_enable"));
                                Constant.setString(activity, Constant.P1_IMAGE, jb.getString("p1_image"));
                                Constant.setString(activity, Constant.P1_LINK, jb.getString("p1_link"));
                                Constant.setString(activity, Constant.IS_INAPP_OPEN1, jb.getString("is_inapp_open1"));
                                Constant.setString(activity, Constant.IS_P2_ENABLED, jb.getString("is_p2_enable"));
                                Constant.setString(activity, Constant.P2_IMAGE, jb.getString("p2_image"));
                                Constant.setString(activity, Constant.P2_LINK, jb.getString("p2_link"));
                                Constant.setString(activity, Constant.IS_INAPP_OPEN2, jb.getString("is_inapp_open2"));
                                Constant.setString(activity, Constant.IS_P3_ENABLED, jb.getString("is_p3_enable"));
                                Constant.setString(activity, Constant.P3_IMAGE, jb.getString("p3_image"));
                                Constant.setString(activity, Constant.P3_LINK, jb.getString("p3_link"));
                                Constant.setString(activity, Constant.IS_INAPP_OPEN3, jb.getString("is_inapp_open3"));
                                Constant.setString(activity, Constant.IS_P4_ENABLED, jb.getString("is_p4_enable"));
                                Constant.setString(activity, Constant.P4_IMAGE, jb.getString("p4_image"));
                                Constant.setString(activity, Constant.P4_LINK, jb.getString("p4_link"));
                                Constant.setString(activity, Constant.IS_INAPP_OPEN4, jb.getString("is_inapp_open4"));
                                Constant.setString(activity, Constant.P_BANNER_HEIGHT, jb.getString("p_banner_height"));
                                Constant.setString(activity, Constant.QUIZ_PLAY_TIME, jb.getString("quiz_play_time"));
                                Constant.setString(activity, Constant.PER_QUIZ_COIN, jb.getString("per_quiz_coin"));
                                Constant.setString(activity, Constant.QUIZ_QUESTION_LIMIT, jb.getString("quiz_question_limit"));
                                Constant.setString(activity, Constant.INVALID_CLICK_COUNT, jb.getString("invalid_click_count"));
                            } else {
                                Constant.showToastMessage(activity, "No Settings Found In Admin Pannel");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Constant.showToastMessage(activity, "Something Went Wrong Try Again");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Constant.showToastMessage(activity, getResources().getString(R.string.slow_internet_connection));
                        }
                    }
                });
                customVolleyJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                        1000 * 20,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // Adding request to request queue
                App.getInstance().addToRequestQueue(customVolleyJsonRequest, tag_json_obj);

            } catch (Exception e) {
                Log.e("TAG", "Admin Settings: excption " + e.getMessage().toString());
            }
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }
    }



    private void getGameSettingsFromAdminPannel() {
        if (Constant.isNetworkAvailable(activity)) {
            try {
                String tag_json_obj = "json_login_req";
                Map<String, String> map = new HashMap<>();
                map.put("get_game_settings", "any");
                CustomVolleyJsonRequest customVolleyJsonRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.GAME_SETTINGS, map, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {
                                JSONObject jb = response.getJSONObject("0");
                                Constant.setString(activity, Constant.DAILY_GAME_COUNT, jb.getString("daily_game_limit"));
                                Constant.setString(activity, Constant.GAME_PRICE_COINS, jb.getString("game_price_coins"));
                                Constant.setString(activity, Constant.GAME_PLAY_TIME, jb.getString("game_play_time"));
                                Constant.setString(activity, Constant.GAME_WARNING_MSG, jb.getString("game_warning_msg"));
                                Constant.setString(activity, Constant.REDIRECT_TO_ACTIVITY, jb.getString("redirect_to_activity"));
                                Constant.setString(activity, Constant.PG_BANNER_HEIGHT, jb.getString("pg_banner_height"));
                                Constant.setString(activity, Constant.HOME_GAME_BANNER, jb.getString("home_game_banner"));
                                Constant.setString(activity, Constant.GAME_HOME_BANNER_HIDE, jb.getString("game_home_banner_hide"));
                                Constant.setString(activity, Constant.GAME_HOME_BANNER_GAME_ACTIVITY, jb.getString("game_home_banner_game_activity"));
                                Constant.setString(activity, Constant.GAME_HOME_BANNER_LINK, jb.getString("game_home_banner_link"));
                                Constant.setString(activity, Constant.IS_PG1_ENABLED, jb.getString("is_pg1_enable"));
                                Constant.setString(activity, Constant.PG1_IMAGE, jb.getString("pg1_image"));
                                Constant.setString(activity, Constant.PG1_LINK, jb.getString("pg1_link"));
                                Constant.setString(activity, Constant.PG1_OPEN_WITH, jb.getString("pg1_open_with"));
                                Constant.setString(activity, Constant.IS_PG2_ENABLED, jb.getString("is_pg2_enable"));
                                Constant.setString(activity, Constant.PG2_IMAGE, jb.getString("pg2_image"));
                                Constant.setString(activity, Constant.PG2_LINK, jb.getString("pg2_link"));
                                Constant.setString(activity, Constant.PG2_OPEN_WITH, jb.getString("pg2_open_with"));
                                Constant.setString(activity, Constant.IS_PG3_ENABLED, jb.getString("is_pg3_enable"));
                                Constant.setString(activity, Constant.PG3_IMAGE, jb.getString("pg3_image"));
                                Constant.setString(activity, Constant.PG3_LINK, jb.getString("pg3_link"));
                                Constant.setString(activity, Constant.PG3_OPEN_WITH, jb.getString("pg3_open_with"));
                                Constant.setString(activity, Constant.IS_PG4_ENABLED, jb.getString("is_pg4_enable"));
                                Constant.setString(activity, Constant.PG4_IMAGE, jb.getString("pg4_image"));
                                Constant.setString(activity, Constant.PG4_LINK, jb.getString("pg4_link"));
                                Constant.setString(activity, Constant.PG4_OPEN_WITH, jb.getString("pg4_open_with"));
                                Constant.setString(activity, Constant.IS_G1_ENABLE, jb.getString("is_g1_enable"));
                                Constant.setString(activity, Constant.G1_IMAGE, jb.getString("g1_image"));
                                Constant.setString(activity, Constant.G1_TITLE, jb.getString("g1_title"));
                                Constant.setString(activity, Constant.G1_LINK, jb.getString("g1_link"));
                                Constant.setString(activity, Constant.IS_G2_ENABLE, jb.getString("is_g2_enable"));
                                Constant.setString(activity, Constant.G2_IMAGE, jb.getString("g2_image"));
                                Constant.setString(activity, Constant.G2_TITLE, jb.getString("g2_title"));
                                Constant.setString(activity, Constant.G2_LINK, jb.getString("g2_link"));
                                Constant.setString(activity, Constant.IS_G3_ENABLE, jb.getString("is_g3_enable"));
                                Constant.setString(activity, Constant.G3_IMAGE, jb.getString("g3_image"));
                                Constant.setString(activity, Constant.G3_TITLE, jb.getString("g3_title"));
                                Constant.setString(activity, Constant.G3_LINK, jb.getString("g3_link"));
                                Constant.setString(activity, Constant.IS_G4_ENABLE, jb.getString("is_g4_enable"));
                                Constant.setString(activity, Constant.G4_IMAGE, jb.getString("g4_image"));
                                Constant.setString(activity, Constant.G4_TITLE, jb.getString("g4_title"));
                                Constant.setString(activity, Constant.G4_LINK, jb.getString("g4_link"));
                                Constant.setString(activity, Constant.IS_G5_ENABLE, jb.getString("is_g5_enable"));
                                Constant.setString(activity, Constant.G5_IMAGE, jb.getString("g5_image"));
                                Constant.setString(activity, Constant.G5_TITLE, jb.getString("g5_title"));
                                Constant.setString(activity, Constant.G5_LINK, jb.getString("g5_link"));
                                Constant.setString(activity, Constant.IS_G6_ENABLE, jb.getString("is_g6_enable"));
                                Constant.setString(activity, Constant.G6_IMAGE, jb.getString("g6_image"));
                                Constant.setString(activity, Constant.G6_TITLE, jb.getString("g6_title"));
                                Constant.setString(activity, Constant.G6_LINK, jb.getString("g6_link"));
                                Constant.setString(activity, Constant.IS_G7_ENABLE, jb.getString("is_g7_enable"));
                                Constant.setString(activity, Constant.G7_IMAGE, jb.getString("g7_image"));
                                Constant.setString(activity, Constant.G7_TITLE, jb.getString("g7_title"));
                                Constant.setString(activity, Constant.G7_LINK, jb.getString("g7_link"));
                                Constant.setString(activity, Constant.IS_G8_ENABLE, jb.getString("is_g8_enable"));
                                Constant.setString(activity, Constant.G8_IMAGE, jb.getString("g8_image"));
                                Constant.setString(activity, Constant.G8_TITLE, jb.getString("g8_title"));
                                Constant.setString(activity, Constant.G8_LINK, jb.getString("g8_link"));
                                Constant.setString(activity, Constant.IS_G9_ENABLE, jb.getString("is_g9_enable"));
                                Constant.setString(activity, Constant.G9_IMAGE, jb.getString("g9_image"));
                                Constant.setString(activity, Constant.G9_TITLE, jb.getString("g9_title"));
                                Constant.setString(activity, Constant.G9_LINK, jb.getString("g9_link"));
                                Constant.setString(activity, Constant.IS_G10_ENABLE, jb.getString("is_g10_enable"));
                                Constant.setString(activity, Constant.G10_IMAGE, jb.getString("g10_image"));
                                Constant.setString(activity, Constant.G10_TITLE, jb.getString("g10_title"));
                                Constant.setString(activity, Constant.G10_LINK, jb.getString("g10_link"));
                                Constant.setString(activity, Constant.IS_G11_ENABLE, jb.getString("is_g11_enable"));
                                Constant.setString(activity, Constant.G11_IMAGE, jb.getString("g11_image"));
                                Constant.setString(activity, Constant.G11_TITLE, jb.getString("g11_title"));
                                Constant.setString(activity, Constant.G11_LINK, jb.getString("g11_link"));
                                Constant.setString(activity, Constant.IS_G12_ENABLE, jb.getString("is_g12_enable"));
                                Constant.setString(activity, Constant.G12_IMAGE, jb.getString("g12_image"));
                                Constant.setString(activity, Constant.G12_TITLE, jb.getString("g12_title"));
                                Constant.setString(activity, Constant.G12_LINK, jb.getString("g12_link"));
                                Constant.setString(activity, Constant.IS_G13_ENABLE, jb.getString("is_g13_enable"));
                                Constant.setString(activity, Constant.G13_IMAGE, jb.getString("g13_image"));
                                Constant.setString(activity, Constant.G13_TITLE, jb.getString("g13_title"));
                                Constant.setString(activity, Constant.G13_LINK, jb.getString("g13_link"));
                                Constant.setString(activity, Constant.IS_G14_ENABLE, jb.getString("is_g14_enable"));
                                Constant.setString(activity, Constant.G14_IMAGE, jb.getString("g14_image"));
                                Constant.setString(activity, Constant.G14_TITLE, jb.getString("g14_title"));
                                Constant.setString(activity, Constant.G14_LINK, jb.getString("g14_link"));
                                Constant.setString(activity, Constant.IS_G15_ENABLE, jb.getString("is_g15_enable"));
                                Constant.setString(activity, Constant.G15_IMAGE, jb.getString("g15_image"));
                                Constant.setString(activity, Constant.G15_TITLE, jb.getString("g15_title"));
                                Constant.setString(activity, Constant.G15_LINK, jb.getString("g15_link"));
                                Constant.setString(activity, Constant.IS_G16_ENABLE, jb.getString("is_g16_enable"));
                                Constant.setString(activity, Constant.G16_IMAGE, jb.getString("g16_image"));
                                Constant.setString(activity, Constant.G16_TITLE, jb.getString("g16_title"));
                                Constant.setString(activity, Constant.G16_LINK, jb.getString("g16_link"));
                                Constant.setString(activity, Constant.IS_G17_ENABLE, jb.getString("is_g17_enable"));
                                Constant.setString(activity, Constant.G17_IMAGE, jb.getString("g17_image"));
                                Constant.setString(activity, Constant.G17_TITLE, jb.getString("g17_title"));
                                Constant.setString(activity, Constant.G17_LINK, jb.getString("g17_link"));
                                Constant.setString(activity, Constant.IS_G18_ENABLE, jb.getString("is_g18_enable"));
                                Constant.setString(activity, Constant.G18_IMAGE, jb.getString("g18_image"));
                                Constant.setString(activity, Constant.G18_TITLE, jb.getString("g18_title"));
                                Constant.setString(activity, Constant.G18_LINK, jb.getString("g18_link"));
                                Constant.setString(activity, Constant.IS_G19_ENABLE, jb.getString("is_g19_enable"));
                                Constant.setString(activity, Constant.G19_IMAGE, jb.getString("g19_image"));
                                Constant.setString(activity, Constant.G19_TITLE, jb.getString("g19_title"));
                                Constant.setString(activity, Constant.G19_LINK, jb.getString("g19_link"));
                                Constant.setString(activity, Constant.IS_G20_ENABLE, jb.getString("is_g20_enable"));
                                Constant.setString(activity, Constant.G20_IMAGE, jb.getString("g20_image"));
                                Constant.setString(activity, Constant.G20_TITLE, jb.getString("g20_title"));
                                Constant.setString(activity, Constant.G20_LINK, jb.getString("g20_link"));
                            } else {
                                Constant.showToastMessage(activity, "No Settings Found In Admin Pannel");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Constant.showToastMessage(activity, "Something Went Wrong From Game Settings");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Constant.showToastMessage(activity, getResources().getString(R.string.slow_internet_connection));
                        }
                    }
                });
                customVolleyJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                        1000 * 20,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // Adding request to request queue
                App.getInstance().addToRequestQueue(customVolleyJsonRequest, tag_json_obj);

            } catch (Exception e) {
                Log.e("TAG", "Game Settings: excption " + e.getMessage().toString());
            }
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }
    }






    private void getRedeemSettingsFromAdminPannel() {
        if (Constant.isNetworkAvailable(activity)) {
            try {
                String tag_json_obj = "json_login_req";
                Map<String, String> map = new HashMap<>();
                map.put("get_redeem_settings", "any");
                CustomVolleyJsonRequest customVolleyJsonRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.REDEEM_SETTINGS, map, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {
                                JSONObject jb = response.getJSONObject("0");
                                Constant.setString(activity, Constant.PAYMENT_BTN_1, jb.getString("payment_btn_1"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_1_TYPE, jb.getString("payment_btn_1_type"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_1_NAME, jb.getString("payment_btn_1_name"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_1_LOGO, jb.getString("payment_btn_1_logo"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_1_DESC, jb.getString("payment_btn_1_desc"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_1_COINS, jb.getString("payment_btn_1_coins"));

                                Constant.setString(activity, Constant.PAYMENT_BTN_2, jb.getString("payment_btn_2"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_2_TYPE, jb.getString("payment_btn_2_type"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_2_NAME, jb.getString("payment_btn_2_name"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_2_LOGO, jb.getString("payment_btn_2_logo"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_2_DESC, jb.getString("payment_btn_2_desc"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_2_COINS, jb.getString("payment_btn_2_coins"));

                                Constant.setString(activity, Constant.PAYMENT_BTN_3, jb.getString("payment_btn_3"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_3_TYPE, jb.getString("payment_btn_3_type"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_3_NAME, jb.getString("payment_btn_3_name"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_3_LOGO, jb.getString("payment_btn_3_logo"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_3_DESC, jb.getString("payment_btn_3_desc"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_3_COINS, jb.getString("payment_btn_3_coins"));

                                Constant.setString(activity, Constant.PAYMENT_BTN_4, jb.getString("payment_btn_4"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_4_TYPE, jb.getString("payment_btn_4_type"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_4_NAME, jb.getString("payment_btn_4_name"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_4_LOGO, jb.getString("payment_btn_4_logo"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_4_DESC, jb.getString("payment_btn_4_desc"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_4_COINS, jb.getString("payment_btn_4_coins"));

                                Constant.setString(activity, Constant.PAYMENT_BTN_5, jb.getString("payment_btn_5"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_5_TYPE, jb.getString("payment_btn_5_type"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_5_NAME, jb.getString("payment_btn_5_name"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_5_LOGO, jb.getString("payment_btn_5_logo"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_5_DESC, jb.getString("payment_btn_5_desc"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_5_COINS, jb.getString("payment_btn_5_coins"));

                                Constant.setString(activity, Constant.PAYMENT_BTN_6, jb.getString("payment_btn_6"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_6_TYPE, jb.getString("payment_btn_6_type"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_6_NAME, jb.getString("payment_btn_6_name"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_6_LOGO, jb.getString("payment_btn_6_logo"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_6_DESC, jb.getString("payment_btn_6_desc"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_6_COINS, jb.getString("payment_btn_6_coins"));

                                Constant.setString(activity, Constant.PAYMENT_BTN_7, jb.getString("payment_btn_7"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_7_TYPE, jb.getString("payment_btn_7_type"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_7_NAME, jb.getString("payment_btn_7_name"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_7_LOGO, jb.getString("payment_btn_7_logo"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_7_DESC, jb.getString("payment_btn_7_desc"));
                                Constant.setString(activity, Constant.PAYMENT_BTN_7_COINS, jb.getString("payment_btn_7_coins"));
                                gotoLoginActivity();
                            } else {
                                Constant.showToastMessage(activity, "No Settings Found In Admin Pannel");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Constant.showToastMessage(activity, "Something Went Wrong From Withdraw Settings");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Constant.showToastMessage(activity, getResources().getString(R.string.slow_internet_connection));
                        }
                    }
                });
                customVolleyJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                        1000 * 20,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // Adding request to request queue
                App.getInstance().addToRequestQueue(customVolleyJsonRequest, tag_json_obj);

            } catch (Exception e) {
                Log.e("TAG", "Withdraw Settings: excption " + e.getMessage().toString());
            }
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }
    }

    private void gotoLoginActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (LOGIN) {
                    Constant.GotoNextActivity(activity, MainActivity.class, "");
                } else {
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    Constant.setString(activity, Constant.TODAY_DATE, currentDate);
                    Log.e("TAG", "onInit: else part of no login");
                    Constant.GotoNextActivity(activity, LoginActivity.class, "");
                }
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }
        }, 1000);
    }




    public void UpdateApp() {
        try {
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo, IMMEDIATE, activity, RC_APP_UPDATE);
                            Log.e("TAG", "onCreate:startUpdateFlowForResult part activarte ");
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo, IMMEDIATE, activity, RC_APP_UPDATE);
                            Log.e("TAG", "onCreate:startUpdateFlowForResult part activarte ");
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("TAG", "onCreate:startUpdateFlowForResult else part activarte ");
                        activity.onInit();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "onCreate:addOnFailureListener else part activarte ");
                    activity.onInit();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE) {
            if (resultCode == RESULT_OK) {
                onInit();
            } else if (resultCode == RESULT_CANCELED) {
                UpdateApp();
            }
        }
    }

}