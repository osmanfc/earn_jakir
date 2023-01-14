package com.dailyreward.dailyrewardapp.activity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget;
import com.bluehomestudio.luckywheel.WheelItem;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SpinActivity extends AppCompatActivity implements IUnityAdsInitializationListener {
    List<WheelItem> wheelItems = new ArrayList<>();
    LuckyWheel luckyWheel;
    SpinActivity activity;
    private ImageView play_btn;
    private String points, points_ad;
    TextView user_points_text_view, spin_count_text_view, total_spin;
    private int spin_count = 0, counter_dialog = 0;
    private String TAG = "SpinActivity";
    public int poiints = 0;
    public boolean rewardShow = true, interstitialShow = true;
    public StartAppAd startAppAd;
    int adsClickCounter = 0;
    CountDownTimer adsClickTimer;
    boolean isClickTimerTrue = false;
    String currentDateInvalid, last_date_invalid;
    CountDownTimer collectClickTimer;
    int collectClickCounter = 0;
    boolean collectClickTimerTrue = false;
    long seconds;
    private MediaPlayer spinSound;
    private MediaPlayer popupSound;
    private MediaPlayer collectSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin);
        activity = this;

        luckyWheel = findViewById(R.id.lwv);
        play_btn = findViewById(R.id.play);
        user_points_text_view = findViewById(R.id.user_points_text_view);
        spin_count_text_view = findViewById(R.id.spin_count_textView);
        total_spin = findViewById(R.id.total_spin);


        popupSound = MediaPlayer.create(activity, R.raw.popup);
        collectSound = MediaPlayer.create(activity, R.raw.collect);
        spinSound = MediaPlayer.create(activity, R.raw.wheelsound);


        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.spin_bg_4, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), Constant.getString(activity, Constant.SPIN_POINT_1)));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.spin_bg_2, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), Constant.getString(activity, Constant.SPIN_POINT_2)));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.spin_bg_1, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), Constant.getString(activity, Constant.SPIN_POINT_3)));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.spin_bg_2, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), Constant.getString(activity, Constant.SPIN_POINT_4)));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.spin_bg_3, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), Constant.getString(activity, Constant.SPIN_POINT_5)));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.spin_bg_4, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), Constant.getString(activity, Constant.SPIN_POINT_6)));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.spin_bg_2, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), Constant.getString(activity, Constant.SPIN_POINT_7)));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.spin_bg_1, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), Constant.getString(activity, Constant.SPIN_POINT_8)));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.spin_bg_2, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), Constant.getString(activity, Constant.SPIN_POINT_9)));

        wheelItems.add(new WheelItem(ResourcesCompat.getColor(getResources(), R.color.spin_bg_3, null), BitmapFactory.decodeResource(getResources(), R.drawable.coin), Constant.getString(activity, Constant.SPIN_POINT_10)));
        luckyWheel.addWheelItems(wheelItems);


        seconds = (Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_TIME)) / 1000) % 60;

        Constant.adsShowingDialog(activity);
        total_spin.setText(Constant.getString(activity, Constant.DAILY_SPIN_COUNT));
        if (Constant.isNetworkAvailable(activity)) {
            String adType = Constant.getString(activity, Constant.AD_TYPE);
            if (adType.equalsIgnoreCase("startapp")) {
                LoadStartAppBanner();
                LoadStartAppInterstital();
                //loadApplovinInterstitialAd();
            } else if (adType.equalsIgnoreCase("applovin")){
               // loadApplovinBanner();
                //loadApplovinInterstitialAd();
                //loadApplovinRewardedAd();
            }
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }
        onClick();
        adsClickTimer();
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
                        int counter = Integer.parseInt(spin_count_text_view.getText().toString());
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


    private void onClick() {
        String user_points = Constant.getString(activity, Constant.USER_POINTS);
        if (user_points.equals("")) {
            user_points_text_view.setText("0");
        } else {
            user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
        }

        String spinCount = Constant.getString(activity, Constant.SPIN_COUNT);
        if (spinCount.equals("0")) {
            spinCount = "";
            Log.e("TAG", "onInit: spin card 0");
        }
        if (spinCount.equals("")) {
            Log.e("TAG", "onInit: spin card empty vala part");
            String currentDate = Constant.getString(activity, Constant.TODAY_DATE);
            Log.e("TAG", "onClick: Current Date" + currentDate);
            String last_date = Constant.getString(activity, Constant.LAST_DATE_SPIN);
            if (last_date.equalsIgnoreCase("0")) {
                last_date = "";
            }
            Log.e("TAG", "Last date" + last_date);
            if (last_date.equals("")) {
                Log.e("TAG", "onInit: last date empty part");
                spin_count_text_view.setText(Constant.getString(activity, Constant.DAILY_SPIN_COUNT));
                Constant.setString(activity, Constant.SPIN_COUNT, Constant.getString(activity, Constant.DAILY_SPIN_COUNT));
                Constant.setString(activity, Constant.LAST_DATE_SPIN, currentDate);
                Constant.addDate(activity, "spin", Constant.getString(activity, Constant.LAST_DATE_SPIN), Constant.getString(activity, Constant.DAILY_SPIN_COUNT));
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
                        Constant.setString(activity, Constant.LAST_DATE_SPIN, currentDate);
                        Constant.setString(activity, Constant.SPIN_COUNT, Constant.getString(activity, Constant.DAILY_SPIN_COUNT));
                        spin_count_text_view.setText(Constant.getString(activity, Constant.SPIN_COUNT));
                        Log.e("TAG", "onClick: today date added to preference" + currentDate);
                        Constant.addDate(activity, "spin", Constant.getString(activity, Constant.LAST_DATE_SPIN), Constant.getString(activity, Constant.DAILY_SPIN_COUNT));
                    } else {
                        spin_count_text_view.setText("0");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("TAG", "onInit: spin in preference part");
            spin_count_text_view.setText(spinCount);
        }

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinSound.start();
                if (Constant.isNetworkAvailable(activity) || Constant.isOnline(activity)) {
                    play_btn.setEnabled(false);
                    Random random = new Random();
                    String str = Constant.getString(activity, Constant.SPIN_PRICE_COIN);
                    String[] parts = str.split("-", 2);
                    int low = Integer.parseInt(parts[0]);
                    int high = Integer.parseInt(parts[1]);
                    int result = random.nextInt(high - low) + low;
                    points = String.valueOf(result);
                    if (points.equals("0")) {
                        points = String.valueOf(1);
                    }
                    Log.e(TAG, "onClick: " + points);
                    luckyWheel.rotateWheelTo(Integer.parseInt(points));
                } else {
                    Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
                }
            }
        });

        luckyWheel.setLuckyWheelReachTheTarget(new OnLuckyWheelReachTheTarget() {
            @Override
            public void onReachTarget() {
                try {
                    WheelItem item = wheelItems.get(Integer.parseInt(points) - 1);
                    String points_amount = item.text;
                    points_ad = item.text;
                    Log.e("TAG", "onReachTarget: " + points_amount);
                    String adType = Constant.getString(activity, Constant.AD_TYPE);
                    adsClickCounter ++;
                    int counter = Integer.parseInt(spin_count_text_view.getText().toString());
                    counter_dialog = Integer.parseInt(spin_count_text_view.getText().toString());
                    if (counter <= 0) {
                        showDialogPoints(0, "0", counter, true);
                    } else {
                        if (adType.equalsIgnoreCase("startapp")){
                            if (adsClickCounter == Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_AFTER_X_CLICK))){
                                adsClickDialog();
                            } else {
                                showDialogPoints(1, points_amount, counter, true);
                            }
                        } else {
                            showDialogPoints(1, points_amount, counter, true);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onReachTarget: " + e.getMessage().toString());
                }

            }
        });

    }

    private void showDialogPoints(final int addValueOrNoAdd, final String points, final int counter, boolean isShowDialog) {
        SweetAlertDialog sweetAlertDialog ;
        popupSound.start();
        if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
            if (addValueOrNoAdd == 1) {
                if (points.equals("0")) {
                    Log.e("TAG", "spinDialog: 0 points");
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
                    Log.e("TAG", "spinDialog: points");
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

            sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
                collectClickTimer.start();
                if (collectClickTimerTrue){
                    collectClickCounter++;
                }

                if (collectClickCounter >= 2){
                    putInvalidClickDate();
                    checkInvalidBlocked();
                }
                if (addValueOrNoAdd == 1) {
                    if (points.equals("0")) {

                    } else if (points.equals("no")){

                    } else {
                        collectSound.start();
                    }
                } else {
                }
                Constant.showAdsLoadingDialog();
                Constant.dismissAdsLoadingDialog();
                play_btn.setEnabled(true);
                poiints = 0;
                if (addValueOrNoAdd == 1) {
                    if (points.equals("0") || points.equalsIgnoreCase("no")) {
                        String current_counter = String.valueOf(counter - 1);
                        Constant.setString(activity, Constant.SPIN_COUNT, current_counter);
                        spin_count_text_view.setText(Constant.getString(activity, Constant.SPIN_COUNT));
                        sweetAlertDialog1.dismissWithAnimation();
                    } else {
                        String current_counter = String.valueOf(counter - 1);
                        Constant.setString(activity, Constant.SPIN_COUNT, current_counter);
                        spin_count_text_view.setText(Constant.getString(activity, Constant.SPIN_COUNT));
                        try {
                            int finalPoint;
                            if (points.equals("")) {
                                finalPoint = 0;
                            } else {
                                finalPoint = Integer.parseInt(points);
                            }
                            poiints = finalPoint;
                            Log.e(TAG, "onClick: " + poiints);
                            Constant.addPoints(activity, finalPoint, 0, "spin", Constant.getString(activity, Constant.SPIN_COUNT));
                            user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
                        } catch (NumberFormatException ex) {
                            Log.e("TAG", "onScratchComplete: " + ex.getMessage().toString());
                        }
                        sweetAlertDialog1.dismissWithAnimation();
                    }
                } else {
                    sweetAlertDialog1.dismissWithAnimation();
                }
                if (addValueOrNoAdd == 1) {
                    if (spin_count == Integer.parseInt(Constant.getString(activity, Constant.ADS_BEETWEEN))) {
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
                            spin_count = 0;
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
                            spin_count = 0;
                        }
                    } else {
                        spin_count++;
                    }
                } else {
                    Constant.dismissAdsLoadingDialog();
                }
            }).show();
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
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
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        unityStartAppInterstitialShow();
                        Constant.dismissAdsLoadingDialog();
                        sweetAlertDialog.dismiss();
                    }
                }, Constant.adsDialogTime);
            }
        }).show();
    }

    void adsClickTimer(){
        adsClickTimer = new CountDownTimer(Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_TIME)), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Toast.makeText(activity, "Timer left: " + millisUntilFinished / 1000, Toast.LENGTH_LONG).show();
                isClickTimerTrue = true;
            }

            @Override
            public void onFinish() {
                isClickTimerTrue = false;
                int counter = Integer.parseInt(spin_count_text_view.getText().toString());
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
                int counter = Integer.parseInt(spin_count_text_view.getText().toString());
                showDialogPoints(1, "no", counter, true);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Constant.saveInvalidCounter(activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}