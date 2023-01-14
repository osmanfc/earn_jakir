package com.dailyreward.dailyrewardapp.activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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


import cn.pedant.SweetAlert.SweetAlertDialog;


public class GameLoader extends AppCompatActivity implements IUnityAdsInitializationListener {

    GameLoader activity;

    TextView game_count_textView, user_points_text_view;
    TextView points_text, total_game, timerTv;
    boolean first_time = true;
    private int game_count = 0;
    private final String TAG = GameLoader.class.getSimpleName();
    private String winning_points;
    public int poiints = 0, counter_dialog = 0;
    public boolean rewardShow = true, interstitialShow = true;
    public StartAppAd startAppAd;
    CountDownTimer timer;
    boolean isGameTimerTrue = false;
    LottieAnimationView winningImage;
    TextView congText, wonText, pointText;
    LinearLayout btnLayout1, btnLayout2;
    AppCompatButton playAgainBtn, collectBtn, homeButton;


    int adsClickCounter = 0;
    CountDownTimer adsClickTimer;
    boolean isClickTimerTrue = false;
    int seconds;
    long minutes;
    private MediaPlayer popupSound;
    private MediaPlayer collectSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loader);
        activity = this;
        game_count_textView = findViewById(R.id.game_count_textView);
        points_text = findViewById(R.id.textView_points_show);
        user_points_text_view = findViewById(R.id.user_points_text_view);
        total_game = findViewById(R.id.total_game);
        timerTv = findViewById(R.id.timerTv);
        winningImage = findViewById(R.id.winningImage);
        congText = findViewById(R.id.congText);
        wonText = findViewById(R.id.wonText);
        pointText = findViewById(R.id.pointText);
        btnLayout1 = findViewById(R.id.btnLayout1);
        btnLayout2 = findViewById(R.id.btnLayout2);
        playAgainBtn = findViewById(R.id.playAgainBtn);
        collectBtn = findViewById(R.id.collectBtn);
        homeButton = findViewById(R.id.homeButton);
        popupSound = MediaPlayer.create(activity, R.raw.popup);
        collectSound = MediaPlayer.create(activity, R.raw.collect);
        minutes = (Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_TIME)) / 1000)  / 60;
        seconds = (int)((Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_TIME)) / 1000) % 60);
        Constant.adsShowingDialog(activity);
        winning_points = Constant.getString(activity, Constant.GAME_PRICE_COINS);
        String GameLink = getIntent().getStringExtra("GAME_PASSING");
        // initializing object for custom chrome tabs.
        CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
        customIntent.setToolbarColor(ContextCompat.getColor(activity, R.color.progressBarColor));
        openCustomTab(activity, customIntent.build(), Uri.parse(GameLink));
        total_game.setText(Constant.getString(activity, Constant.DAILY_GAME_COUNT));
        onInit();
        String adType = Constant.getString(activity, Constant.AD_TYPE);

        playAgainBtn.setOnClickListener(v -> {
            openCustomTab(activity, customIntent.build(), Uri.parse(GameLink));
            timer.start();
        });
        homeButton.setOnClickListener(v -> {
            if (Constant.getString(activity, Constant.REDIRECT_TO_ACTIVITY).equalsIgnoreCase("mainactivity")){
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(activity, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });
        collectBtn.setOnClickListener(v -> {
            if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
                if (first_time) {
                    first_time = false;
                    Log.e("onGame", "Complete");
                    Log.e("onGame", "Complete" + winning_points);
                    adsClickCounter++;
                    int counter = Integer.parseInt(game_count_textView.getText().toString());
                    counter_dialog = Integer.parseInt(game_count_textView.getText().toString());
                    if (counter <= 0) {
                        showDialogPoints(0, "0", counter, true);
                    } else {
                        if (adType.equalsIgnoreCase("startapp")){
                            if (adsClickCounter == Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_AFTER_X_CLICK))){
                                adsClickDialog();
                            } else {
                                showDialogPoints(1, winning_points, counter, true);
                            }
                        } else {
                            showDialogPoints(1, winning_points, counter, true);
                        }

                    }
                }
            }
        });
        if (Constant.isNetworkAvailable(activity)) {
            if (adType.equalsIgnoreCase("startapp")) {
                LoadStartAppBanner();
                LoadStartAppInterstital();
            }
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }


        resetTimer();
        adsClickTimer();
        Constant.loadInvalidCounter(activity);
        timer.start();

    }




    public static void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri) {
        // package name is the default package
        // for our custom chrome tab
        String packageName = "com.android.chrome";
        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity, uri);
        } else {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
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
            }
        }).show();
    }

    void adsClickTimer(){
        adsClickTimer = new CountDownTimer(Integer.parseInt(Constant.getString(activity, Constant.ADS_CLICK_TIME)), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Toast.makeText(activity, "Timer left: " + millisUntilFinished/1000, Toast.LENGTH_LONG).show();
                isClickTimerTrue = true;
            }

            @Override
            public void onFinish() {
                isClickTimerTrue = false;
                int counter = Integer.parseInt(game_count_textView.getText().toString());
                showDialogPoints(1, Constant.getString(activity, Constant.ADS_CLICK_COINS), counter, true);
            }
        };
    }




    void resetTimer(){
        timer = new CountDownTimer(Integer.parseInt(Constant.getString(activity, Constant.GAME_PLAY_TIME)), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isGameTimerTrue = true;
                timerTv.setText(String.valueOf(millisUntilFinished/1000));
                collectBtn.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                isGameTimerTrue = false;
                collectBtn.setVisibility(View.VISIBLE);
            }
        };
    }

    private void onInit() {
        String user_points = Constant.getString(activity, Constant.USER_POINTS);
        if (user_points.equals("")) {
            user_points_text_view.setText("0");
        } else {
            user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
        }
        String gameCount = Constant.getString(activity, Constant.GAME_COUNT);
        if (gameCount.equals("0")) {
            gameCount = "";
            Log.e("TAG", "onInit: Game Count 0");
        }
        if (gameCount.equals("")) {
            Log.e("TAG", "onInit: Game Count empty part");
            String currentDate = Constant.getString(activity, Constant.TODAY_DATE);
            Log.e("TAG", "onClick: Current Date" + currentDate);
            String last_date = Constant.getString(activity, Constant.LAST_DATE_GAME);
            Log.e("TAG", "Last date" + last_date);
            if (last_date.equalsIgnoreCase("0")) {
                last_date = "";
            }
            if (last_date.equals("")) {
                Log.e("TAG", "onInit: last date empty part");
                game_count_textView.setText(Constant.getString(activity, Constant.DAILY_GAME_COUNT));
                Constant.setString(activity, Constant.GAME_COUNT, Constant.getString(activity, Constant.DAILY_GAME_COUNT));
                Constant.setString(activity, Constant.LAST_DATE_GAME, currentDate);
                Constant.addDate(activity, "game", Constant.getString(activity, Constant.LAST_DATE_GAME), Constant.getString(activity, Constant.DAILY_GAME_COUNT));
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
                        Constant.setString(activity, Constant.LAST_DATE_GAME, currentDate);
                        Constant.setString(activity, Constant.GAME_COUNT, Constant.getString(activity, Constant.DAILY_GAME_COUNT));
                        game_count_textView.setText(Constant.getString(activity, Constant.GAME_COUNT));
                        Constant.addDate(activity, "game", Constant.getString(activity, Constant.LAST_DATE_GAME), Constant.getString(activity, Constant.DAILY_GAME_COUNT));
                        Log.e("TAG", "onClick: today date added to preference" + currentDate);
                    } else {
                        game_count_textView.setText("0");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("TAG", "onInit: game count in preference part");
            game_count_textView.setText(gameCount);
        }
    }


    private void showDialogPoints(final int addNoAddValueGame, final String points, final int counter, boolean isDialogShow) {
        String adType = Constant.getString(activity, Constant.AD_TYPE);
        popupSound.start();
        SweetAlertDialog sweetAlertDialog;


        if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
            if (addNoAddValueGame == 1) {
                if (points.equals("0")) {
                    Log.e("TAG", "showDialogPoints: 0 points");
                    sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setCancelable(false);
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
                    sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.you_won));
                    sweetAlertDialog.setContentText(points);
                    sweetAlertDialog.setConfirmText("Collect");
                }
            } else {
                Log.e("TAG", "showDialogPoints: chance over");
                sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setTitleText(getResources().getString(R.string.today_chance_over));
                sweetAlertDialog.setConfirmText("Ok");
            }

            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {


                    //my code
                    if (addNoAddValueGame == 1) {
                        if (points.equals("0")) {

                        } else if (points.equals("no")){

                        } else {
                            collectSound.start();
                        }
                    } else {
                    }

                    first_time = true;
                    poiints = 0;
                    if (addNoAddValueGame == 1) {
                        if (points.equals("0") || points.equalsIgnoreCase("no")) {
                            String current_counter = String.valueOf(counter - 1);
                            Constant.setString(activity, Constant.GAME_COUNT, current_counter);
                            game_count_textView.setText(Constant.getString(activity, Constant.GAME_COUNT));
                            sweetAlertDialog.dismiss();
                        } else {
                            String current_counter = String.valueOf(counter - 1);
                            Constant.setString(activity, Constant.GAME_COUNT, current_counter);
                            game_count_textView.setText(Constant.getString(activity, Constant.GAME_COUNT));
                            try {
                                int finalPoint;
                                if (points.equals("")) {
                                    finalPoint = 0;
                                } else {
                                    finalPoint = Integer.parseInt(points);
                                }
                                poiints = finalPoint;
                                Constant.addPoints(activity, finalPoint, 0, "game", Constant.getString(activity, Constant.GAME_COUNT));
                                user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
                            } catch (NumberFormatException ex) {
                                Log.e("TAG", "onGameComplete: " + ex.getMessage());
                            }
                            sweetAlertDialog.dismiss();
                        }
                    } else {
                        sweetAlertDialog.dismiss();
                    }
                    if (addNoAddValueGame == 1) {
                        if (game_count == Integer.parseInt(Constant.getString(activity, Constant.ADS_BEETWEEN))) {
                            if (rewardShow) {
                                Log.e(TAG, "onReachTarget: rewaded ads showing method");
                                unityStartAppRewardedShow();
                                rewardShow = false;
                                interstitialShow = true;
                                game_count = 0;
                            } else if (interstitialShow) {
                                Log.e(TAG, "onReachTarget: interstital ads showing method");
                                unityStartAppInterstitialShow();
                                rewardShow = true;
                                interstitialShow = false;
                                game_count = 0;
                            }
                        } else {
                            game_count++;
                        }
                    }
                    if (timer != null){
                        timer.cancel();
                    }
                    collectBtn.setVisibility(View.GONE);
                    playAgainBtn.setVisibility(View.VISIBLE);
                    homeButton.setVisibility(View.VISIBLE);
                }
            }).show();
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }
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
                        int counter = Integer.parseInt(game_count_textView.getText().toString());
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

    @Override
    public void onBackPressed() {
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
                adsClickCounter = 0;
                int counter = Integer.parseInt(game_count_textView.getText().toString());
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
        if (isGameTimerTrue){
            timer.cancel();
            timerTv.setText("Insufficient Playtime");
            winningImage.setAnimation("try_again.json");
            congText.setText("Oops!");
            wonText.setText(Constant.getString(activity, Constant.GAME_WARNING_MSG));
            pointText.setText("0");
        } else {
            timerTv.setVisibility(View.GONE);
            winningImage.setAnimation("win.json");
            congText.setText(R.string.congratulation);
            wonText.setText(R.string.you_ve_won);
            pointText.setText(winning_points);
        }
    }



}