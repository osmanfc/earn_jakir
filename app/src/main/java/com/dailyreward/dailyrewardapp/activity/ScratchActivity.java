package com.dailyreward.dailyrewardapp.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.dailyreward.dailyrewardapp.App;
import com.dailyreward.dailyrewardapp.R;
import com.dailyreward.dailyrewardapp.utils.Constant;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.BannerListener;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;

public class ScratchActivity extends AppCompatActivity implements ScratchListener, IUnityAdsInitializationListener {
    TextView scratch_count_textView, user_points_text_view;
    ScratchActivity activity;
    ScratchCardLayout scratchCardLayout;
    TextView points_text, total_scratch;
    boolean first_time = true;
    private int scratch_count = 0;
    private final String TAG = ScratchActivity.class.getSimpleName();
    private String random_points, points_ad;
    public int poiints = 0, counter_dialog = 0;
    public boolean rewardShow = true, interstitialShow = true;
    public StartAppAd startAppAd;
    int adsClickCounter = 0;
    CountDownTimer adsClickTimer;
    String currentDateInvalid, last_date_invalid;
    CountDownTimer collectClickTimer;
    int collectClickCounter = 0;
    boolean collectClickTimerTrue = false;
    boolean isClickTimerTrue = false;
    int seconds;
    long minutes;
    private MediaPlayer scratchSound;
    private MediaPlayer popupSound;
    private MediaPlayer collectSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);
        activity = this;

        scratch_count_textView = findViewById(R.id.scratch_count_textView);
        points_text = findViewById(R.id.textView_points_show);
        scratchCardLayout = findViewById(R.id.scratch_view_layout);
        scratchCardLayout.setScratchListener(activity);
        user_points_text_view = findViewById(R.id.user_points_text_view);
        total_scratch = findViewById(R.id.total_scratch);
        scratchCardLayout.setRevealFullAtPercent(90);
        onInit();

        scratchSound = MediaPlayer.create(activity, R.raw.eraser);
        popupSound = MediaPlayer.create(activity, R.raw.popup);
        collectSound = MediaPlayer.create(activity, R.raw.collect);

        minutes = (Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_TIME)) / 1000)  / 60;
        seconds = (int)((Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_TIME)) / 1000) % 60);

        Constant.adsShowingDialog(activity);
        if (Constant.isNetworkAvailable(activity)) {
            String adType = Constant.getString(activity, Constant.AD_TYPE);
            if (adType.equalsIgnoreCase("startapp")) {
                LoadStartAppBanner();
                LoadStartAppInterstital();
            }
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }
        total_scratch.setText(Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
        resetTimer();
        collectTimer();
        Constant.loadInvalidCounter(activity);
    }


    private void LoadStartAppInterstital() {
        if (startAppAd == null) {
            startAppAd = new StartAppAd(App.getContext());
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
        } else {
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
        }
    }

    private void ShowStartappInterstital() {
        if (startAppAd != null && startAppAd.isReady()) {
            startAppAd.showAd(new AdDisplayListener() {
                @Override
                public void adHidden(Ad ad) {
                    if (adsClickCounter == Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_AFTER_X_CLICK))){
                        adsClickCounter = 0;
                        int counter = Integer.parseInt(scratch_count_textView.getText().toString());
                        showDialogPoints(1, "no", counter, true);
                    }
                }
                @Override
                public void adDisplayed(Ad ad) {
                    if (adsClickCounter == Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_AFTER_X_CLICK))){
                        Constant.showToastMessage(activity, "Click on this Ads to win " + Constant.getString(activity, Constant.ADS_CLICK_COINS) + " coins");
                    }
                }
                @Override
                public void adClicked(Ad ad) {
                    if (adsClickCounter == Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_AFTER_X_CLICK))){
                        adsClickCounter = 0;
                        adsClickTimer.start();
                    } else {
                        Constant.invalidClickCount++;
                        Log.i("checkInvalidClick", String.valueOf(Constant.invalidClickCount));
                    }
                }
                @Override
                public void adNotDisplayed(Ad ad) {
                    LoadStartAppInterstital();
                    DisplayInterstitialAd();
                }
            });
        } else {
            LoadStartAppInterstital();
        }
    }


    private void onInit() {
        String user_points = Constant.getString(activity, Constant.USER_POINTS);
        if (user_points.equals("")) {
            user_points_text_view.setText("0");
        } else {
            user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
        }
        String scratchCount = Constant.getString(activity, Constant.SCRATCH_COUNT);
        if (scratchCount.equals("0")) {
            scratchCount = "";
            Log.e("TAG", "onInit: scratch card 0");
        }
        if (scratchCount.equals("")) {
            Log.e("TAG", "onInit: scratch card empty vala part");
            String currentDate = Constant.getString(activity, Constant.TODAY_DATE);
            Log.e("TAG", "onClick: Current Date" + currentDate);
            String last_date = Constant.getString(activity, Constant.LAST_DATE_SCRATCH);
            Log.e("TAG", "Lat date" + last_date);
            if (last_date.equalsIgnoreCase("0")) {
                last_date = "";
            }
            if (last_date.equals("")) {
                Log.e("TAG", "onInit: last date empty part");
                scratch_count_textView.setText(Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
                Constant.setString(activity, Constant.SCRATCH_COUNT, Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
                Constant.setString(activity, Constant.LAST_DATE_SCRATCH, currentDate);
                Constant.addDate(activity, "scratch", Constant.getString(activity, Constant.LAST_DATE_SCRATCH), Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
            } else {
                Log.e("TAG", "onInit: last date not empty part");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date current_date = sdf.parse(currentDate);
                    Date lastDate = sdf.parse(last_date);
                    long diff = current_date.getTime() - lastDate.getTime();
                    long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
                    Log.e("TAG", "onClick: Days Difference" + difference_In_Days);
                    if (difference_In_Days > 0) {
                        Constant.setString(activity, Constant.LAST_DATE_SCRATCH, currentDate);
                        Constant.setString(activity, Constant.SCRATCH_COUNT, Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
                        scratch_count_textView.setText(Constant.getString(activity, Constant.SCRATCH_COUNT));
                        Constant.addDate(activity, "scratch", Constant.getString(activity, Constant.LAST_DATE_SCRATCH), Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
                        Log.e("TAG", "onClick: today date added to preference" + currentDate);
                    } else {
                        scratch_count_textView.setText("0");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("TAG", "onInit: scracth card in preference part");
            scratch_count_textView.setText(scratchCount);
        }
    }

    private void LoadStartAppBanner() {
        final LinearLayout layout = findViewById(R.id.banner_container);
        Banner banner = new Banner(activity, new BannerListener() {
            @Override
            public void onReceiveAd(View view) {
                layout.addView(view);
            }

            @Override
            public void onFailedToReceiveAd(View view) {

            }

            @Override
            public void onImpression(View view) {

            }

            @Override
            public void onClick(View view) {
                Constant.invalidClickCount++;
                Log.i("checkInvalidClick", String.valueOf(Constant.invalidClickCount));
            }
        });
        banner.loadAd(300, 50);
    }


    private void showDialogPoints(final int addOrNoAddValue, final String points, final int counter, boolean isDialogShow) {
        popupSound.start();
        SweetAlertDialog sweetAlertDialog ;

        if (Constant.isNetworkAvailable(activity)) {
            if (addOrNoAddValue == 1) {
                if (points.equals("0")) {
                    Log.e("TAG", "showDialogPoints: 0 points");
                    sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setTitleText("Oops!");
                    sweetAlertDialog.setContentText(getResources().getString(R.string.better_luck));
                    sweetAlertDialog.setConfirmText("Ok");
                } else if (points.equals("no")){
                    Log.e("TAG", "showDialogPoints: no points");
                    sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setTitleText("Oops!");
                    sweetAlertDialog.setContentText("You missed " + Constant.getString(activity, Constant.ADS_CLICK_COINS) + " coins");
                    sweetAlertDialog.setConfirmText("Ok");
                } else {
                    Log.e("TAG", "showDialogPoints: points");
                    Log.e("TAG", "showDialogPoints: rewared cancel");
                    sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.you_won));
                    sweetAlertDialog.setContentText(points);
                    sweetAlertDialog.setConfirmText("Collect");
                }
            } else {
                Log.e("TAG", "showDialogPoints: chance over");
                sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setTitleText(getResources().getString(R.string.today_chance_over));
                sweetAlertDialog.setConfirmText("Ok");
            }

            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    collectClickTimer.start();
                    if (collectClickTimerTrue){
                        collectClickCounter++;
                    }

                    if (collectClickCounter >= 2){
                        putInvalidClickDate();
                        checkInvalidBlocked();
                    }


                    if (addOrNoAddValue == 1) {
                        if (points.equals("0")) {
                        } else if (points.equals("no")){

                        }else {
                            collectSound.start();
                        }
                    } else {
                    }

                    first_time = true;
                    Constant.showAdsLoadingDialog();
                    Constant.dismissAdsLoadingDialog();
                    scratchCardLayout.resetScratch();
                    poiints = 0;
                    if (addOrNoAddValue == 1) {
                        if (points.equals("0") || points.equalsIgnoreCase("no")) {
                            String current_counter = String.valueOf(counter - 1);
                            Constant.setString(activity, Constant.SCRATCH_COUNT, current_counter);
                            scratch_count_textView.setText(Constant.getString(activity, Constant.SCRATCH_COUNT));
                            sweetAlertDialog.dismissWithAnimation();
                        } else {
                            String current_counter = String.valueOf(counter - 1);
                            Constant.setString(activity, Constant.SCRATCH_COUNT, current_counter);
                            scratch_count_textView.setText(Constant.getString(activity, Constant.SCRATCH_COUNT));
                            try {
                                int finalPoint;
                                if (points.equals("")) {
                                    finalPoint = 0;
                                } else {
                                    finalPoint = Integer.parseInt(points);
                                }
                                poiints = finalPoint;
                                Constant.addPoints(activity, finalPoint, 0, "scratch", Constant.getString(activity, Constant.SCRATCH_COUNT));
                                user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
                            } catch (NumberFormatException ex) {
                                Log.e("TAG", "onScratchComplete: " + ex.getMessage());
                            }
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    } else {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                    if (addOrNoAddValue == 1) {
                        if (scratch_count == Integer.parseInt(Constant.getString(activity, Constant.ADS_BEETWEEN))) {
                            if (rewardShow) {
                                Log.e(TAG, "onReachTarget: rewaded ads showing method");
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        unityStartAppRewardedShow();
                                    }
                                }, Constant.adsDialogTime);
                                rewardShow = false;
                                interstitialShow = true;
                                scratch_count = 0;
                            } else if (interstitialShow) {
                                Log.e(TAG, "onReachTarget: interstital ads showing method");
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        unityStartAppInterstitialShow();
                                    }
                                }, Constant.adsDialogTime);
                                rewardShow = true;
                                interstitialShow = false;
                                scratch_count = 0;
                            }
                        } else {
                            scratch_count++;
                        }
                    }
                }
            }).show();
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }
    }



    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        Log.e("onScratch", "onScratchProgress");
    }

    @Override
    public void onScratchStarted() {

        Log.e("onScratch", "onScratchStarted");
        if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
            if (Integer.parseInt(scratch_count_textView.getText().toString().trim()) <= 0) {
                showDialogPoints(0, "0", 0, true);
                return;
            }
            random_points = "";
            Random random = new Random();
            String str = Constant.getString(activity, Constant.SCRATCH_PRICE_COIN);
            String[] parts = str.split("-", 2);
            int low = Integer.parseInt(parts[0]);
            int high = Integer.parseInt(parts[1]);
            int result = random.nextInt(high - low) + low;
            random_points = String.valueOf(result);
            points_text.setText(random_points);
            scratchSound.start();
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }
    }




    @Override
    public void onScratchComplete() {
        if (first_time) {
            first_time = false;
            Log.e("onScratch", "Complete");
            Log.e("onScratch", "Complete" + random_points);
            String adType = Constant.getString(activity, Constant.AD_TYPE);
            adsClickCounter ++;
            int counter = Integer.parseInt(scratch_count_textView.getText().toString());
            counter_dialog = Integer.parseInt(scratch_count_textView.getText().toString());

            if (counter <= 0) {
                showDialogPoints(0, "0", counter, true);
            } else {
                if (adType.equalsIgnoreCase("startapp")){
                    if (adsClickCounter == Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_AFTER_X_CLICK))){
                        adsClickDialog();
                    } else {
                        showDialogPoints(1, points_text.getText().toString(), counter, true);
                    }
                } else {
                    showDialogPoints(1, points_text.getText().toString(), counter, true);
                }
            }
        }
    }


    private void adsClickDialog() {
        popupSound.start();
        SweetAlertDialog sweetAlertDialog;

        sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitle("Wow! You won " + Constant.getString(activity, Constant.ADS_CLICK_COINS) + " coins");
        sweetAlertDialog.setContentText("Click on this ads & wait " + seconds + " sec" + " to win " + Constant.getString(activity, Constant.ADS_CLICK_COINS) + " coins");
        sweetAlertDialog.setConfirmText("Ok");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Constant.showAdsLoadingDialog();
                Constant.dismissAdsLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        unityStartAppInterstitialShow();
                        sweetAlertDialog.dismiss();
                    }
                }, Constant.adsDialogTime);
            }
        }).show();
    }

    void resetTimer(){
        adsClickTimer = new CountDownTimer(Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_TIME)), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Toast.makeText(activity, "Timer left: " + millisUntilFinished/1000, Toast.LENGTH_LONG).show();
                isClickTimerTrue = true;
            }

            @Override
            public void onFinish() {
                isClickTimerTrue = false;
                int counter = Integer.parseInt(scratch_count_textView.getText().toString());
                showDialogPoints(1, Constant.getString(activity, Constant.ADS_CLICK_COINS), counter, true);
            }
        };
    }


    void collectTimer(){
        collectClickTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                collectClickTimerTrue = true;
            }

            @Override
            public void onFinish() {
                collectClickTimerTrue = false;
            }
        };
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

    private void checkInvalidBlocked(){
        if (Constant.getString(activity, Constant.LAST_DATE_INVALID).equals(Constant.getString(activity, Constant.TODAY_DATE))){
            Constant.showBlockedDialog(activity,activity,"You are Blocked for today! Reason is: Invalid Clicks");
        }
    }

    @Override
    public void onBackPressed() {
        startAppAd.onBackPressed();
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        finish();
    }

    private void unityStartAppInterstitialShow() {
        String adType = Constant.getString(activity, Constant.AD_TYPE);
        if (adType.equalsIgnoreCase("startapp")) {
            ShowStartappInterstital();
        } else if (adType.equalsIgnoreCase("unity")){
            DisplayInterstitialAd();
        }
    }

    private void unityStartAppRewardedShow() {
        String adType = Constant.getString(activity, Constant.AD_TYPE);
        if (adType.equalsIgnoreCase("startapp")) {
            ShowStartappInterstital();
        } else if (adType.equalsIgnoreCase("unity")){
            DisplayInterstitialAd();
        }
    }


    private IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {
            UnityAds.show((Activity)getApplicationContext(), Constant.getString(activity, Constant.UNITY_INTERSTITAL_ID), new UnityAdsShowOptions(), showListener);
        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
        }
    };

    private IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message);
        }

        @Override
        public void onUnityAdsShowStart(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowStart: " + placementId);
        }

        @Override
        public void onUnityAdsShowClick(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: " + placementId);
        }

        @Override
        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
            Log.v("UnityAdsExample", "onUnityAdsShowComplete: " + placementId);
        }
    };

    @Override
    public void onInitializationComplete() {

    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
        Log.e("UnityAdsExample", "Unity Ads initialization failed with error: [" + error + "] " + message);
    }

    // Implement a function to load an interstitial ad. The ad will start to show once the ad has been loaded.
    public void DisplayInterstitialAd () {
        UnityAds.load(Constant.getString(activity, Constant.UNITY_INTERSTITAL_ID), loadListener);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (adsClickTimer != null) {
            adsClickTimer.cancel();
            if (isClickTimerTrue){
                isClickTimerTrue = false;
                int counter = Integer.parseInt(scratch_count_textView.getText().toString());
                showDialogPoints(1, "no", counter, true);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constant.saveInvalidCounter(activity);
    }


}