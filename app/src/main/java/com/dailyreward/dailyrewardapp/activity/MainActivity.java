package com.dailyreward.dailyrewardapp.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;

import android.content.Intent;

import android.media.MediaPlayer;
import android.net.Uri;

import android.os.Bundle;

import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.dailyreward.dailyrewardapp.R;
import com.dailyreward.dailyrewardapp.utils.Constant;
import com.scottyab.rootbeer.RootBeer;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {
    MainActivity activity;
    LinearLayout promotion_height, game_layout, scratch_layout, spin_layout, quiz_layout, captcha_earn;
    RelativeLayout check_in_layout, refer_layout, profile_layout, rating_layout, watch_layout, gameBtn, reward_btn;
    private TextView user_name_text_view;
    private TextView user_points_text_view;
    private ImageView  game_banner, promotion_1, promotion_2, promotion_3, promotion_4;
    private ProgressDialog alertDialog;
    String isGhbNotExternalOpen;
    String currentDateInvalid, last_date_invalid;
    private MediaPlayer popupSound;
    private MediaPlayer collectSound;
    LinearLayout helpBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        Constant.setString(activity, Constant.LAST_TIME_ADD_TO_SERVER, "");
        user_name_text_view = findViewById(R.id.user_name_text_view);
        user_points_text_view = findViewById(R.id.user_points_text_view);
        check_in_layout = findViewById(R.id.daily_check_in_layout);
        scratch_layout = findViewById(R.id.scratch_card_layout);
        spin_layout = findViewById(R.id.spin_wheel_layout);
        refer_layout = findViewById(R.id.refer_and_earn_layout);
        profile_layout = findViewById(R.id.profile_layout);
        rating_layout = findViewById(R.id.rating_app_layout);
        helpBtn = findViewById(R.id.helpBtn);
        gameBtn = findViewById(R.id.gameBtn);
        reward_btn = findViewById(R.id.reward_btn);
        captcha_earn = findViewById(R.id.captcha_earn);
        quiz_layout = findViewById(R.id.quiz_card_layout);
        game_layout = findViewById(R.id.game_layout);
        watch_layout = findViewById(R.id.watch_layout);
        game_banner = findViewById(R.id.game_banner);
        promotion_height = findViewById(R.id.promotion_height);
        promotion_1 = findViewById(R.id.promotion_1);
        promotion_2 = findViewById(R.id.promotion_2);
        promotion_3 = findViewById(R.id.promotion_3);
        promotion_4 = findViewById(R.id.promotion_4);
        isGhbNotExternalOpen = Constant.getString(activity, Constant.GAME_HOME_BANNER_GAME_ACTIVITY);
        ViewGroup.LayoutParams params = promotion_height.getLayoutParams();

        popupSound = MediaPlayer.create(activity, R.raw.popup);
        collectSound = MediaPlayer.create(activity, R.raw.collect);

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadAction("https://t.me/+XznHYkwsBMQwM2Y1");
            }
        });

        try {
            params.height = Integer.parseInt(Constant.getString(activity, Constant.P_BANNER_HEIGHT));
        } catch (NumberFormatException ex){
            Constant.showToastMessage(activity, "Invalid Format");
        }
        promotion_height.setLayoutParams(params);
        promotion1();
        promotion2();
        promotion3();
        promotion4();
        gameBanner();

        RootBeer rootBeer = new RootBeer(activity);
        if (rootBeer.isRooted()) {
            Constant.appsCheckerDialog(activity, activity, "You can't use this app because your device is rooted.");
        }
        onClick();
        Constant.loadInvalidCounter(activity);
    }
    private void gameBanner() {
        Glide.with(activity)
                .load(Constant.getString(activity, Constant.HOME_GAME_BANNER))
                .into(game_banner);
        String isGameBannerShow = Constant.getString(activity, Constant.GAME_HOME_BANNER_HIDE);
        if (!isGameBannerShow.equalsIgnoreCase("true")){
            game_layout.setVisibility(View.GONE);
        }
    }
    private void promotion1() {
        String isPromotion1True = Constant.getString(activity, Constant.IS_P1_ENABLED);
        String isInAppOpen1 = Constant.getString(activity, Constant.IS_INAPP_OPEN1);

        if (isPromotion1True.equalsIgnoreCase("true")) {
            Glide.with(activity)
                    .load(Constant.getString(activity, Constant.P1_IMAGE))
                    .into(promotion_1);
            promotion_1.setOnClickListener(v -> {
                if (isInAppOpen1.equalsIgnoreCase("chrome_tab")){
                    CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
                    customIntent.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
                    openCustomTab(MainActivity.this, customIntent.build(), Uri.parse(Constant.getString(activity, Constant.P1_LINK)));
                } else if (isInAppOpen1.equalsIgnoreCase("chrome_earning_tab")) {
                    inAppAction(Constant.getString(activity, Constant.P1_LINK));
                } else if (isInAppOpen1.equalsIgnoreCase("external_browser")){
                    downloadAction(Constant.getString(activity, Constant.P1_LINK));
                } else if (isInAppOpen1.equalsIgnoreCase("game_activity")){
                    Constant.GotoNextActivity(activity, GameActivity.class, "");
                }
            });
        }
        else {
            promotion_1.setVisibility(View.GONE);
        }
    }
    private void promotion2() {
        String isPromotion2True = Constant.getString(activity, Constant.IS_P2_ENABLED);
        String isInAppOpen2 = Constant.getString(activity, Constant.IS_INAPP_OPEN2);

        if (isPromotion2True.equalsIgnoreCase("true")) {
            Glide.with(activity)
                    .load(Constant.getString(activity, Constant.P2_IMAGE))
                    .into(promotion_2);
            promotion_2.setOnClickListener(v -> {
                if (isInAppOpen2.equalsIgnoreCase("chrome_tab")){
                    CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
                    customIntent.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
                    openCustomTab(MainActivity.this, customIntent.build(), Uri.parse(Constant.getString(activity, Constant.P2_LINK)));
                } else if (isInAppOpen2.equalsIgnoreCase("chrome_earning_tab")) {
                    inAppAction(Constant.getString(activity, Constant.P2_LINK));
                } else if (isInAppOpen2.equalsIgnoreCase("external_browser")){
                    downloadAction(Constant.getString(activity, Constant.P2_LINK));
                } else if (isInAppOpen2.equalsIgnoreCase("game_activity")){
                    Constant.GotoNextActivity(activity, GameActivity.class, "");
                }
            });
        }
        else {
            promotion_2.setVisibility(View.GONE);
        }
    }
    private void promotion3() {
        String isPromotion3True = Constant.getString(activity, Constant.IS_P3_ENABLED);
        String isInAppOpen3 = Constant.getString(activity, Constant.IS_INAPP_OPEN3);
        if (isPromotion3True.equalsIgnoreCase("true")) {
            Glide.with(activity)
                    .load(Constant.getString(activity, Constant.P3_IMAGE))
                    .into(promotion_3);
            promotion_3.setOnClickListener(v -> {
                if (isInAppOpen3.equalsIgnoreCase("chrome_tab")){
                    CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
                    customIntent.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
                    openCustomTab(MainActivity.this, customIntent.build(), Uri.parse(Constant.getString(activity, Constant.P3_LINK)));
                } else if (isInAppOpen3.equalsIgnoreCase("chrome_earning_tab")) {
                    inAppAction(Constant.getString(activity, Constant.P3_LINK));
                } else if (isInAppOpen3.equalsIgnoreCase("external_browser")){
                    downloadAction(Constant.getString(activity, Constant.P3_LINK));
                } else if (isInAppOpen3.equalsIgnoreCase("game_activity")){
                    Constant.GotoNextActivity(activity, GameActivity.class, "");
                }
            });
        }
        else {
            promotion_3.setVisibility(View.GONE);
        }
    }
    private void promotion4() {
        String isPromotion4True = Constant.getString(activity, Constant.IS_P4_ENABLED);
        String isInAppOpen4 = Constant.getString(activity, Constant.IS_INAPP_OPEN4);
        if (isPromotion4True.equalsIgnoreCase("true")) {
            Glide.with(activity)
                    .load(Constant.getString(activity, Constant.P4_IMAGE))
                    .into(promotion_4);
            promotion_4.setOnClickListener(v -> {
                if (isInAppOpen4.equalsIgnoreCase("chrome_tab")){
                    CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
                    customIntent.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
                    openCustomTab(MainActivity.this, customIntent.build(), Uri.parse(Constant.getString(activity, Constant.P4_LINK)));
                } else if (isInAppOpen4.equalsIgnoreCase("chrome_earning_tab")) {
                    inAppAction(Constant.getString(activity, Constant.P4_LINK));
                } else if (isInAppOpen4.equalsIgnoreCase("external_browser")){
                    downloadAction(Constant.getString(activity, Constant.P4_LINK));
                } else if (isInAppOpen4.equalsIgnoreCase("game_activity")){
                    Constant.GotoNextActivity(activity, GameActivity.class, "");
                }
            });
        }
        else {
            promotion_4.setVisibility(View.GONE);
        }
    }
    public static void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri) {
        String packageName = "com.android.chrome";
        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity, uri);
        } else {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }



    //Download dialog
    private void downloadDialog(String msg) {
        popupSound.start();
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitle("Download");
        sweetAlertDialog.setContentText(msg);
        sweetAlertDialog.setConfirmText("Ok");
        sweetAlertDialog.setCancelText("No");
        sweetAlertDialog.setConfirmClickListener(sweetAlertDialog12 -> {
            downloadAction(Constant.getString(activity, Constant.PROMOTION_LINK));
        }).setCancelClickListener(sweetAlertDialog1 -> {
            sweetAlertDialog1.dismissWithAnimation();
        });
    }
    public void downloadAction(String url) {
        try {
            Uri webpage = Uri.parse(url);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Failed to load.",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void inAppAction(String url) {
        Intent intent = new Intent(activity,GameLoader.class);
        intent.putExtra("GAME_PASSING", url);
        startActivity(intent);
    }
    private void onClick() {
        checkIsBlocked();
        rating_layout.setOnClickListener(view -> Constant.ratingApp(activity));
        check_in_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
                    String currentDate = Constant.getString(activity, Constant.TODAY_DATE);
                    Log.e("TAG", "onClick: Current Date" + currentDate);
                    String last_date = Constant.getString(activity, Constant.LAST_DATE);
                    if (last_date.equalsIgnoreCase("0")) {
                        last_date = "";
                    }
                    Log.e("TAG", "onClick: last_date Date" + last_date);
                    if (last_date.equals("")) {
                        Constant.setString(activity, Constant.LAST_DATE, currentDate);
                        Constant.addPoints(activity, Integer.parseInt(Constant.getString(activity, Constant.DAILY_CHECK_IN_POINTS)), 0, "daily", currentDate);
                        user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
                        showDialogOfPoints(Integer.parseInt(Constant.getString(activity, Constant.DAILY_CHECK_IN_POINTS)));
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            Date pastDAte = sdf.parse(last_date);
                            Date currentDAte = sdf.parse(currentDate);
                            long diff = currentDAte.getTime() - pastDAte.getTime();
                            long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
                            Log.e("TAG", "onClick: Days Diffrernce" + difference_In_Days);
                            if (difference_In_Days > 0) {
                                Constant.setString(activity, Constant.LAST_DATE, currentDate);
                                Constant.addPoints(activity, Integer.parseInt(Constant.getString(activity, Constant.DAILY_CHECK_IN_POINTS)), 0, "daily", currentDate);
                                user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
                                showDialogOfPoints(Integer.parseInt(Constant.getString(activity, Constant.DAILY_CHECK_IN_POINTS)));
                            } else {
                                showDialogOfPoints(0);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
                }
            }
        });
        scratch_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, ScratchActivity.class, "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        spin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, SpinActivity.class, "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        captcha_earn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, CaptchaActivity.class, "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        quiz_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, QuizActivity.class, "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        game_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGhbNotExternalOpen.equalsIgnoreCase("external")) {
                    downloadAction(Constant.getString(activity, Constant.GAME_HOME_BANNER_LINK));
                } else if (isGhbNotExternalOpen.equalsIgnoreCase("inapp")){
                    inAppAction(Constant.getString(activity, Constant.GAME_HOME_BANNER_LINK));
                } else {
                    Constant.GotoNextActivity(activity, GameActivity.class, "");
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }
        });
        profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoProfile();
            }
        });

        refer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, ReferAndEarn.class, "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        watch_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, WatchAndEarnActivity.class, "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        gameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, GameActivity.class, "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        reward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, GiftCardActivity.class, "");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }
    private void gotoProfile() {
        if (Constant.getString(activity, Constant.IS_LOGIN).equals("true")) {
            Constant.GotoNextActivity(activity, ProfileActivity.class, "msg");
            overridePendingTransition(R.anim.enter, R.anim.exit);
        } else {
            Constant.showToastMessage(activity, getResources().getString(R.string.login_first));
            Constant.GotoNextActivity(activity, LoginActivity.class, "msg");
            overridePendingTransition(R.anim.enter, R.anim.exit);
            finish();
        }
    }
    private void setUSerName() {
        user_name_text_view.setText(Constant.getString(activity, Constant.USER_NAME));
        String user_points = Constant.getString(activity, Constant.USER_POINTS);
        if (user_points.equals("")) {
            user_points_text_view.setText("0");
        } else {
            user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
        }
    }
    private void checkIsBlocked() {
        if (Constant.getString(activity, Constant.USER_BLOCKED).equals("0")) {
            Constant.showBlockedDialog(activity, activity, getResources().getString(R.string.you_are_blocked));
            return;
        }
        checkUserInfo();
        setUSerName();
    }
    private void checkUserInfo() {
        String user_refer_code = Constant.getString(activity, Constant.USER_REFFER_CODE);
        String user_name = Constant.getString(activity, Constant.USER_NAME);
        String user_number = Constant.getString(activity, Constant.USER_NUMBER);
        if (user_refer_code.equals("") || user_name.equals("") || user_number.equals("")) {
            showUpdateProfileDialog();
        }
    }
    private void showUpdateProfileDialog() {
        popupSound.start();
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitle(getResources().getString(R.string.incomplite_profile));
        sweetAlertDialog.setConfirmText(getResources().getString(R.string.update_profile));
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gotoProfile();
                    }
                }, 1000);
            }
        }).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (Constant.invalidClickCount >= Integer.parseInt(Constant.getString(activity, Constant.INVALID_CLICK_COUNT))){
            putInvalidClickDate();
            Constant.deleteInvalidCounter(activity);
            Log.i("checkInvalid", "Total Invalid Click is: " + Constant.invalidClickCount);
        }
        checkInvalidBlocked();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Constant.saveInvalidCounter(activity);
    }
    private void checkInvalidBlocked(){
        if (Constant.getString(activity, Constant.LAST_DATE_INVALID).equals(Constant.getString(activity, Constant.TODAY_DATE))){
            Constant.showBlockedDialog(activity,activity,"You are Blocked for today! Reason is: Invalid Clicks");
        } else {
            Constant.blockedHackingApps(activity, activity);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
        checkIsBlocked();
        String isVpnRecommended = Constant.getString(activity, Constant.VPN_ENABLE);
        String isPromotionDialogShow = Constant.getString(activity, Constant.IS_PROMOTION_DIALOG_ENABLE);

        if (isPromotionDialogShow.equalsIgnoreCase("true")) {
            downloadDialog(Constant.getString(activity, Constant.PROMOTION_TEXT));
        }
        if (isVpnRecommended.equalsIgnoreCase("true")) {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(activity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://ip-api.com/json", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("countryCode").equals("US") || response.getString("countryCode").equals("GB")){
                                Log.i("VpnCheck", "VPN is Connected");
                                String last_date = Constant.getString(activity, Constant.SIGNUP_BOUNUS_DATE);
                                if (last_date.equalsIgnoreCase("0")) {
                                    collectBonusDialog();
                                }
                            } else {
                                vpnDialog();
                                Log.i("VpnCheck", "VPN is not Connected");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
        } else {
            if (isVpnConnectionActive()) {
                Log.i("VpnCheck", "VPN is Connected");
                vpnDialog2();
            } else {
                Log.i("VpnCheck", "VPN is not Connected");
                String last_date = Constant.getString(activity, Constant.SIGNUP_BOUNUS_DATE);
                if (last_date.equalsIgnoreCase("0")) {
                    collectBonusDialog();
                }
            }
        }
    }
    private void putInvalidClickDate(){
        if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
            currentDateInvalid = Constant.getString(activity, Constant.TODAY_DATE);
            Log.e("TAG", "onClick: Current Date" + currentDateInvalid);
            last_date_invalid = Constant.getString(activity, Constant.LAST_DATE_INVALID);
            if (last_date_invalid.equalsIgnoreCase("0")) {
                last_date_invalid = "";
            }
            Log.e("TAG", "onClick: last_date Date" + last_date_invalid);
            if (last_date_invalid.equals("")) {
                Constant.setString(activity, Constant.LAST_DATE_INVALID, currentDateInvalid);
                Constant.addPoints(activity, 0, 0, "invalid", currentDateInvalid);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date pastDAte = sdf.parse(last_date_invalid);
                    Date currentDAte = sdf.parse(currentDateInvalid);
                    long diff = currentDAte.getTime() - pastDAte.getTime();
                    long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
                    Log.e("TAG", "onClick: Days Diffrernce" + difference_In_Days);
                    if (difference_In_Days > 0) {
                        Constant.setString(activity, Constant.LAST_DATE_INVALID, currentDateInvalid);
                        Constant.addPoints(activity, 0, 0, "invalid", currentDateInvalid);
                    } else {
                        //showDialogOfPoints(0);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }
    }
    //Security dialog
    private void vpnDialog() {
        popupSound.start();
        SweetAlertDialog sweetAlertDialog;
        sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitle("VPN ALERT");
        sweetAlertDialog.setContentText(Constant.getString(activity, Constant.VPN_MSG));
        sweetAlertDialog.setConfirmText("Ok");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                downloadAction(Constant.getString(activity, Constant.VPN_LINK));
                finish();
            }
        }).show();
    }
    //VPN Dialog 2
    private void vpnDialog2() {
        popupSound.start();
        SweetAlertDialog sweetAlertDialog;
        sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitle("VPN ALERT");
        sweetAlertDialog.setContentText("Please disconnect the VPN and then use this app");
        sweetAlertDialog.setConfirmText("Ok");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                finish();
            }
        }).show();
    }
    // Check VPN Connected or not
    public static boolean isVpnConnectionActive() {
        List<String> networkList = new ArrayList<>();
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    networkList.add(networkInterface.getName());
            }
        } catch (Exception ex) {

        }

        return networkList.contains("tun0");
    }
    public void showDialogOfPoints(int points) {
        popupSound.start();
        if (points == Integer.parseInt(Constant.getString(activity, Constant.DAILY_CHECK_IN_POINTS))) {
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Congratulation!")
                    .setContentText("You won "+ points + " " + "coins")
                    .show();
        } else {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Today Chance is Over!")
                    .show();
        }
    }
    private void collectBonusDialog(){
        SweetAlertDialog sweetAlertDialog;
        sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitle("Sign Up Bonus!");
        sweetAlertDialog.setContentText("Collect Your Sign Up Bonus.");
        sweetAlertDialog.setConfirmText("Ok");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
                    String currentDate = Constant.getString(activity, Constant.TODAY_DATE);
                    Log.e("TAG", "onClick: Current Date" + currentDate);
                    String last_date = Constant.getString(activity, Constant.SIGNUP_BOUNUS_DATE);
                    if (last_date.equalsIgnoreCase("0")) {
                        last_date = "";
                    }
                    Log.e("TAG", "onClick: last_date Date" + last_date);
                    if (last_date.equals("")) {
                        Constant.setString(activity, Constant.SIGNUP_BOUNUS_DATE, currentDate);
                        Constant.addPoints(activity, Integer.parseInt(Constant.getString(activity, Constant.SIGNUP_BOUNUS)), 0, "bonus", currentDate);
                        user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            Date pastDAte = sdf.parse(last_date);
                            Date currentDAte = sdf.parse(currentDate);
                            long diff = currentDAte.getTime() - pastDAte.getTime();
                            long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
                            Log.e("TAG", "onClick: Days Diffrernce" + difference_In_Days);
                            if (difference_In_Days > 0) {
                                Constant.setString(activity, Constant.SIGNUP_BOUNUS_DATE, currentDate);
                                Constant.addPoints(activity, Integer.parseInt(Constant.getString(activity, Constant.SIGNUP_BOUNUS)), 0, "bonus", currentDate);
                                user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
                }
            }
        }).show();
    }
    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        popupSound.start();
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure?")
            .setContentText("You want to exit?")
            .setConfirmText("Yes")
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();
                    finish();
                }
            })
            .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();
                }
            })
            .show();
    }

}