package com.dailyreward.dailyrewardapp.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;
import com.dailyreward.dailyrewardapp.App;
import com.dailyreward.dailyrewardapp.R;
import com.dailyreward.dailyrewardapp.utils.BaseUrl;
import com.dailyreward.dailyrewardapp.utils.Constant;
import com.dailyreward.dailyrewardapp.utils.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GiftCardActivity extends AppCompatActivity {

    GiftCardActivity activity;
    CardView payment_btn_1, payment_btn_2, payment_btn_3, payment_btn_4, payment_btn_5, payment_btn_6, payment_btn_7;
    TextView user_points_textView, payment_btn_1_name, payment_btn_1_desc, payment_btn_1_coins
            , payment_btn_2_name, payment_btn_2_desc, payment_btn_2_coins,
            payment_btn_3_name, payment_btn_3_desc, payment_btn_3_coins,
            payment_btn_4_name, payment_btn_4_desc, payment_btn_4_coins,
            payment_btn_5_name, payment_btn_5_desc, payment_btn_5_coins,
            payment_btn_6_name, payment_btn_6_desc, payment_btn_6_coins,
            payment_btn_7_name, payment_btn_7_desc, payment_btn_7_coins;
    ImageView payment_btn_1_logo, payment_btn_2_logo, payment_btn_3_logo, payment_btn_4_logo, payment_btn_5_logo,
            payment_btn_6_logo, payment_btn_7_logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_card);
        activity = this;
        user_points_textView = findViewById(R.id.user_points_textView);
        payment_btn_1 = findViewById(R.id.payment_btn_1);
        payment_btn_1_name = findViewById(R.id.payment_btn_1_name);
        payment_btn_1_logo = findViewById(R.id.payment_btn_1_logo);
        payment_btn_1_desc = findViewById(R.id.payment_btn_1_desc);
        payment_btn_1_coins = findViewById(R.id.payment_btn_1_coins);

        payment_btn_2 = findViewById(R.id.payment_btn_2);
        payment_btn_2_name = findViewById(R.id.payment_btn_2_name);
        payment_btn_2_logo = findViewById(R.id.payment_btn_2_logo);
        payment_btn_2_desc = findViewById(R.id.payment_btn_2_desc);
        payment_btn_2_coins = findViewById(R.id.payment_btn_2_coins);

        payment_btn_3 = findViewById(R.id.payment_btn_3);
        payment_btn_3_name = findViewById(R.id.payment_btn_3_name);
        payment_btn_3_logo = findViewById(R.id.payment_btn_3_logo);
        payment_btn_3_desc = findViewById(R.id.payment_btn_3_desc);
        payment_btn_3_coins = findViewById(R.id.payment_btn_3_coins);

        payment_btn_4 = findViewById(R.id.payment_btn_4);
        payment_btn_4_name = findViewById(R.id.payment_btn_4_name);
        payment_btn_4_logo = findViewById(R.id.payment_btn_4_logo);
        payment_btn_4_desc = findViewById(R.id.payment_btn_4_desc);
        payment_btn_4_coins = findViewById(R.id.payment_btn_4_coins);

        payment_btn_5 = findViewById(R.id.payment_btn_5);
        payment_btn_5_name = findViewById(R.id.payment_btn_5_name);
        payment_btn_5_logo = findViewById(R.id.payment_btn_5_logo);
        payment_btn_5_desc = findViewById(R.id.payment_btn_5_desc);
        payment_btn_5_coins = findViewById(R.id.payment_btn_5_coins);

        payment_btn_6 = findViewById(R.id.payment_btn_6);
        payment_btn_6_name = findViewById(R.id.payment_btn_6_name);
        payment_btn_6_logo = findViewById(R.id.payment_btn_6_logo);
        payment_btn_6_desc = findViewById(R.id.payment_btn_6_desc);
        payment_btn_6_coins = findViewById(R.id.payment_btn_6_coins);

        payment_btn_7 = findViewById(R.id.payment_btn_7);
        payment_btn_7_name = findViewById(R.id.payment_btn_7_name);
        payment_btn_7_logo = findViewById(R.id.payment_btn_7_logo);
        payment_btn_7_desc = findViewById(R.id.payment_btn_7_desc);
        payment_btn_7_coins = findViewById(R.id.payment_btn_7_coins);
        String user_points = Constant.getString(activity, Constant.USER_POINTS);
        if (user_points.equals("")) {
            user_points_textView.setText("0");
        } else {
            user_points_textView.setText(Constant.getString(activity, Constant.USER_POINTS));
        }
        Payment1();
        Payment2();
        Payment3();
        Payment4();
        Payment5();
        Payment6();
        Payment7();

    }




    private void Payment1() {
        String is_p_btn_1_enable = Constant.getString(activity, Constant.PAYMENT_BTN_1);
        String p_btn_1_type = Constant.getString(activity, Constant.PAYMENT_BTN_1_TYPE);
        String p_btn_1_name = Constant.getString(activity, Constant.PAYMENT_BTN_1_NAME);
        String p_btn_1_logo = Constant.getString(activity, Constant.PAYMENT_BTN_1_LOGO);
        String p_btn_1_desc = Constant.getString(activity, Constant.PAYMENT_BTN_1_DESC);
        String p_btn_1_coins = Constant.getString(activity, Constant.PAYMENT_BTN_1_COINS);

        if (is_p_btn_1_enable.equalsIgnoreCase("true")) {
            payment_btn_1_name.setText(p_btn_1_name);
            Glide.with(activity)
                    .load(p_btn_1_logo)
                    .into(payment_btn_1_logo);
            payment_btn_1_desc.setText(p_btn_1_desc);
            payment_btn_1_coins.setText(p_btn_1_coins);

            payment_btn_1.setOnClickListener(v -> {
                if (Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)) < Integer.parseInt(p_btn_1_coins)) {
                    Constant.showToastMessage(activity, "Insufficient Coins");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Enter Payment Info");

                    final EditText input = new EditText(activity);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHint("Enter" + " " + p_btn_1_name + " " + p_btn_1_type);
                    builder.setView(input);

                    builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
                                @SuppressLint("HardwareIds") String Device = Settings.Secure.getString(activity.getContentResolver(),
                                        Settings.Secure.ANDROID_ID);
                                if (Constant.getString(activity, Constant.USER_DEVICE).equals(Device)){
                                    String numberOrUpiId = input.getText().toString();
                                    if (numberOrUpiId.length() == 0) {
                                        Constant.showToastMessage(activity, getResources().getString(R.string.enterNumberOrUpi));
                                    } else {
                                        if (Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)) < Integer.parseInt(p_btn_1_coins)) {
                                            Constant.showToastMessage(activity, "You Have Not Enough Coins");
                                            return;
                                        }
                                        RedeemPointsDialog(numberOrUpiId, p_btn_1_coins, p_btn_1_name);
                                    }
                                } else {
                                    killProcess();
                                }
                            } else {
                                Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
        }
        else {
            payment_btn_1.setVisibility(View.GONE);
        }
    }

    private void Payment2() {
        String is_p_btn_2_enable = Constant.getString(activity, Constant.PAYMENT_BTN_2);
        String p_btn_2_type = Constant.getString(activity, Constant.PAYMENT_BTN_2_TYPE);
        String p_btn_2_name = Constant.getString(activity, Constant.PAYMENT_BTN_2_NAME);
        String p_btn_2_logo = Constant.getString(activity, Constant.PAYMENT_BTN_2_LOGO);
        String p_btn_2_desc = Constant.getString(activity, Constant.PAYMENT_BTN_2_DESC);
        String p_btn_2_coins = Constant.getString(activity, Constant.PAYMENT_BTN_2_COINS);

        if (is_p_btn_2_enable.equalsIgnoreCase("true")) {
            payment_btn_2_name.setText(p_btn_2_name);
            Glide.with(activity)
                    .load(p_btn_2_logo)
                    .into(payment_btn_2_logo);
            payment_btn_2_desc.setText(p_btn_2_desc);
            payment_btn_2_coins.setText(p_btn_2_coins);

            payment_btn_2.setOnClickListener(v -> {
                if (Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)) < Integer.parseInt(p_btn_2_coins)) {
                    Constant.showToastMessage(activity, "Insufficient Coins");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Enter Payment Info");

                    final EditText input = new EditText(activity);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHint("Enter" + " " + p_btn_2_name + " " + p_btn_2_type);
                    builder.setView(input);

                    builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
                                @SuppressLint("HardwareIds") String Device = Settings.Secure.getString(activity.getContentResolver(),
                                        Settings.Secure.ANDROID_ID);
                                if (Constant.getString(activity, Constant.USER_DEVICE).equals(Device)){
                                    String numberOrUpiId = input.getText().toString();
                                    if (numberOrUpiId.length() == 0) {
                                        Constant.showToastMessage(activity, getResources().getString(R.string.enterNumberOrUpi));
                                    } else {
                                        if (Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)) < Integer.parseInt(p_btn_2_coins)) {
                                            Constant.showToastMessage(activity, "You Have Not Enough Coins");
                                            return;
                                        }
                                        RedeemPointsDialog(numberOrUpiId, p_btn_2_coins, p_btn_2_name);
                                    }
                                } else {
                                    killProcess();
                                }
                            } else {
                                Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
        }
        else {
            payment_btn_2.setVisibility(View.GONE);
        }
    }

    private void Payment3() {
        String is_p_btn_3_enable = Constant.getString(activity, Constant.PAYMENT_BTN_3);
        String p_btn_3_type = Constant.getString(activity, Constant.PAYMENT_BTN_3_TYPE);
        String p_btn_3_name = Constant.getString(activity, Constant.PAYMENT_BTN_3_NAME);
        String p_btn_3_logo = Constant.getString(activity, Constant.PAYMENT_BTN_3_LOGO);
        String p_btn_3_desc = Constant.getString(activity, Constant.PAYMENT_BTN_3_DESC);
        String p_btn_3_coins = Constant.getString(activity, Constant.PAYMENT_BTN_3_COINS);

        if (is_p_btn_3_enable.equalsIgnoreCase("true")) {
            payment_btn_3_name.setText(p_btn_3_name);
            Glide.with(activity)
                    .load(p_btn_3_logo)
                    .into(payment_btn_3_logo);
            payment_btn_3_desc.setText(p_btn_3_desc);
            payment_btn_3_coins.setText(p_btn_3_coins);

            payment_btn_3.setOnClickListener(v -> {
                if (Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)) < Integer.parseInt(p_btn_3_coins)) {
                    Constant.showToastMessage(activity, "Insufficient Coins");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Enter Payment Info");

                    final EditText input = new EditText(activity);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHint("Enter" + " " + p_btn_3_name + " " + p_btn_3_type);
                    builder.setView(input);

                    builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
                                @SuppressLint("HardwareIds") String Device = Settings.Secure.getString(activity.getContentResolver(),
                                        Settings.Secure.ANDROID_ID);
                                if (Constant.getString(activity, Constant.USER_DEVICE).equals(Device)){
                                    String numberOrUpiId = input.getText().toString();
                                    if (numberOrUpiId.length() == 0) {
                                        Constant.showToastMessage(activity, getResources().getString(R.string.enterNumberOrUpi));
                                    } else {
                                        if (Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)) < Integer.parseInt(p_btn_3_coins)) {
                                            Constant.showToastMessage(activity, "You Have Not Enough Coins");
                                            return;
                                        }
                                        RedeemPointsDialog(numberOrUpiId, p_btn_3_coins, p_btn_3_name);
                                    }
                                } else {
                                    killProcess();
                                }
                            } else {
                                Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
        }
        else {
            payment_btn_3.setVisibility(View.GONE);
        }
    }

    private void Payment4() {
        String is_p_btn_4_enable = Constant.getString(activity, Constant.PAYMENT_BTN_4);
        String p_btn_4_type = Constant.getString(activity, Constant.PAYMENT_BTN_4_TYPE);
        String p_btn_4_name = Constant.getString(activity, Constant.PAYMENT_BTN_4_NAME);
        String p_btn_4_logo = Constant.getString(activity, Constant.PAYMENT_BTN_4_LOGO);
        String p_btn_4_desc = Constant.getString(activity, Constant.PAYMENT_BTN_4_DESC);
        String p_btn_4_coins = Constant.getString(activity, Constant.PAYMENT_BTN_4_COINS);

        if (is_p_btn_4_enable.equalsIgnoreCase("true")) {
            payment_btn_4_name.setText(p_btn_4_name);
            Glide.with(activity)
                    .load(p_btn_4_logo)
                    .into(payment_btn_4_logo);
            payment_btn_4_desc.setText(p_btn_4_desc);
            payment_btn_4_coins.setText(p_btn_4_coins);

            payment_btn_4.setOnClickListener(v -> {
                if (Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)) < Integer.parseInt(p_btn_4_coins)) {
                    Constant.showToastMessage(activity, "Insufficient Coins");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Enter Payment Info");

                    final EditText input = new EditText(activity);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHint("Enter" + " " + p_btn_4_name + " " + p_btn_4_type);
                    builder.setView(input);

                    builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
                                @SuppressLint("HardwareIds") String Device = Settings.Secure.getString(activity.getContentResolver(),
                                        Settings.Secure.ANDROID_ID);
                                if (!Constant.getString(activity, Constant.USER_DEVICE).equals(Device)){
                                    killProcess();
                                } else {
                                    String numberOrUpiId = input.getText().toString();
                                    if (numberOrUpiId.length() == 0) {
                                        Constant.showToastMessage(activity, getResources().getString(R.string.enterNumberOrUpi));
                                    } else {
                                        if (Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)) < Integer.parseInt(p_btn_4_coins)) {
                                            Constant.showToastMessage(activity, "You Have Not Enough Coins");
                                            return;
                                        }
                                        RedeemPointsDialog(numberOrUpiId, p_btn_4_coins, p_btn_4_name);
                                    }
                                }
                            } else {
                                Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
        }
        else {
            payment_btn_4.setVisibility(View.GONE);
        }
    }

    private void Payment5() {
        String is_p_btn_5_enable = Constant.getString(activity, Constant.PAYMENT_BTN_5);
        String p_btn_5_type = Constant.getString(activity, Constant.PAYMENT_BTN_5_TYPE);
        String p_btn_5_name = Constant.getString(activity, Constant.PAYMENT_BTN_5_NAME);
        String p_btn_5_logo = Constant.getString(activity, Constant.PAYMENT_BTN_5_LOGO);
        String p_btn_5_desc = Constant.getString(activity, Constant.PAYMENT_BTN_5_DESC);
        String p_btn_5_coins = Constant.getString(activity, Constant.PAYMENT_BTN_5_COINS);

        if (is_p_btn_5_enable.equalsIgnoreCase("true")) {
            payment_btn_5_name.setText(p_btn_5_name);
            Glide.with(activity)
                    .load(p_btn_5_logo)
                    .into(payment_btn_5_logo);
            payment_btn_5_desc.setText(p_btn_5_desc);
            payment_btn_5_coins.setText(p_btn_5_coins);

            payment_btn_5.setOnClickListener(v -> {
                if (Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)) < Integer.parseInt(p_btn_5_coins)) {
                    Constant.showToastMessage(activity, "Insufficient Coins");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Enter Payment Info");

                    final EditText input = new EditText(activity);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHint("Enter" + " " + p_btn_5_name + " " + p_btn_5_type);
                    builder.setView(input);

                    builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
                                @SuppressLint("HardwareIds") String Device = Settings.Secure.getString(activity.getContentResolver(),
                                        Settings.Secure.ANDROID_ID);
                                if (!Constant.getString(activity, Constant.USER_DEVICE).equals(Device)){
                                    killProcess();
                                } else {
                                    String numberOrUpiId = input.getText().toString();
                                    if (numberOrUpiId.length() == 0) {
                                        Constant.showToastMessage(activity, getResources().getString(R.string.enterNumberOrUpi));
                                    } else {
                                        if (Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)) < Integer.parseInt(p_btn_5_coins)) {
                                            Constant.showToastMessage(activity, "You Have Not Enough Coins");
                                            return;
                                        }
                                        RedeemPointsDialog(numberOrUpiId, p_btn_5_coins, p_btn_5_name);
                                    }
                                }
                            } else {
                                Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
        }
        else {
            payment_btn_5.setVisibility(View.GONE);
        }
    }

    private void Payment6() {
        String is_p_btn_6_enable = Constant.getString(activity, Constant.PAYMENT_BTN_6);
        String p_btn_6_type = Constant.getString(activity, Constant.PAYMENT_BTN_6_TYPE);
        String p_btn_6_name = Constant.getString(activity, Constant.PAYMENT_BTN_6_NAME);
        String p_btn_6_logo = Constant.getString(activity, Constant.PAYMENT_BTN_6_LOGO);
        String p_btn_6_desc = Constant.getString(activity, Constant.PAYMENT_BTN_6_DESC);
        String p_btn_6_coins = Constant.getString(activity, Constant.PAYMENT_BTN_6_COINS);

        if (is_p_btn_6_enable.equalsIgnoreCase("true")) {
            payment_btn_6_name.setText(p_btn_6_name);
            Glide.with(activity)
                    .load(p_btn_6_logo)
                    .into(payment_btn_6_logo);
            payment_btn_6_desc.setText(p_btn_6_desc);
            payment_btn_6_coins.setText(p_btn_6_coins);

            payment_btn_6.setOnClickListener(v -> {
                if (Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)) < Integer.parseInt(p_btn_6_coins)) {
                    Constant.showToastMessage(activity, "Insufficient Coins");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Enter Payment Info");

                    final EditText input = new EditText(activity);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHint("Enter" + " " + p_btn_6_name + " " + p_btn_6_type);
                    builder.setView(input);

                    builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
                                @SuppressLint("HardwareIds") String Device = Settings.Secure.getString(activity.getContentResolver(),
                                        Settings.Secure.ANDROID_ID);
                                if (!Constant.getString(activity, Constant.USER_DEVICE).equals(Device)){
                                    killProcess();
                                } else {
                                    String numberOrUpiId = input.getText().toString();
                                    if (numberOrUpiId.length() == 0) {
                                        Constant.showToastMessage(activity, getResources().getString(R.string.enterNumberOrUpi));
                                    } else {
                                        if (Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)) < Integer.parseInt(p_btn_6_coins)) {
                                            Constant.showToastMessage(activity, "You Have Not Enough Coins");
                                            return;
                                        }
                                        RedeemPointsDialog(numberOrUpiId, p_btn_6_coins, p_btn_6_name);
                                    }
                                }
                            } else {
                                Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
        }
        else {
            payment_btn_6.setVisibility(View.GONE);
        }
    }

    private void Payment7() {
        String is_p_btn_7_enable = Constant.getString(activity, Constant.PAYMENT_BTN_7);
        String p_btn_7_type = Constant.getString(activity, Constant.PAYMENT_BTN_7_TYPE);
        String p_btn_7_name = Constant.getString(activity, Constant.PAYMENT_BTN_7_NAME);
        String p_btn_7_logo = Constant.getString(activity, Constant.PAYMENT_BTN_7_LOGO);
        String p_btn_7_desc = Constant.getString(activity, Constant.PAYMENT_BTN_7_DESC);
        String p_btn_7_coins = Constant.getString(activity, Constant.PAYMENT_BTN_7_COINS);

        if (is_p_btn_7_enable.equalsIgnoreCase("true")) {
            payment_btn_7_name.setText(p_btn_7_name);
            Glide.with(activity)
                    .load(p_btn_7_logo)
                    .into(payment_btn_7_logo);
            payment_btn_7_desc.setText(p_btn_7_desc);
            payment_btn_7_coins.setText(p_btn_7_coins);

            payment_btn_7.setOnClickListener(v -> {
                if (Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)) < Integer.parseInt(p_btn_7_coins)) {
                    Constant.showToastMessage(activity, "Insufficient Coins");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Enter Payment Info");

                    final EditText input = new EditText(activity);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setHint("Enter" + " " + p_btn_7_name + " " + p_btn_7_type);
                    builder.setView(input);

                    builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
                                @SuppressLint("HardwareIds") String Device = Settings.Secure.getString(activity.getContentResolver(),
                                        Settings.Secure.ANDROID_ID);
                                if (!Constant.getString(activity, Constant.USER_DEVICE).equals(Device)){
                                    killProcess();
                                } else {
                                    String numberOrUpiId = input.getText().toString();
                                    if (numberOrUpiId.length() == 0) {
                                        Constant.showToastMessage(activity, getResources().getString(R.string.enterNumberOrUpi));
                                    } else {
                                        if (Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)) < Integer.parseInt(p_btn_7_coins)) {
                                            Constant.showToastMessage(activity, "You Have Not Enough Coins");
                                            return;
                                        }
                                        RedeemPointsDialog(numberOrUpiId, p_btn_7_coins, p_btn_7_name);
                                    }
                                }
                            } else {
                                Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
        }
        else {
            payment_btn_7.setVisibility(View.GONE);
        }
    }





    private void RedeemPointsDialog(final String numberOrUpiId, final String points, final String type) {
        SweetAlertDialog sweetAlertDialog ;
        String points_text_string = getResources().getString(R.string.redeem_tag_line_2) + " " + numberOrUpiId + " " + getResources().getString(R.string.redeem_tag_line_3) + " " + points + " " + getResources().getString(R.string.redeem_tag_line_4) + " " + type;
        sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.redeem_tag_line_1));
        sweetAlertDialog.setContentText(points_text_string);
        sweetAlertDialog.setConfirmText("Yes");
        sweetAlertDialog.setCancelText("Cancel");

        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog
                        .setTitleText("Redeem Successful!")
                        .setContentText("You will receive a reward within 24 hours!")
                        .setConfirmText("OK")
                        .setConfirmClickListener(null)
                        .showCancelButton(false)
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                if (Constant.getString(activity, Constant.IS_LOGIN).equalsIgnoreCase("true")) {
                    makeRedeemRequest(numberOrUpiId, points, type, Constant.getString(activity, Constant.REFER_CODE));
                } else {
                    Constant.showToastMessage(activity, "Login First");
                }
            }
        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }
        }).show();
    }
    private void killProcess()
    {
        finish();
        android.os.Process.killProcess( android.os.Process.myPid());
    }
    private void makeRedeemRequest(String numberOrUpiId, final String points, String type, String refer_by) {
        final String user_previous_points = Constant.getString(activity, Constant.USER_POINTS);
        final int current_points = Integer.parseInt(user_previous_points) - Integer.parseInt(points);
        Constant.setString(activity, Constant.USER_POINTS, String.valueOf(current_points));
        user_points_textView.setText(String.valueOf(current_points));
        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("redeem_point", "redeem");
        if (!refer_by.equalsIgnoreCase("")) {
            params.put("referraled_with", refer_by);
        }
        params.put("user_id", Constant.getString(activity, Constant.USER_ID));
        params.put("new_point", String.valueOf(current_points));
        params.put("redeemed_point", points);
        params.put("payment_mode", type);
        params.put("payment_info", numberOrUpiId);
        Log.e("TAG", "signupNewUser: " + params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.UPDATE_POINTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    boolean status = response.getBoolean("status");
                    if (status) {
                        Constant.addPoints(activity, Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)), 1, "redeem", "1");
                    } else {
                        Constant.showToastMessage(activity, response.getString("message"));
                        user_points_textView.setText(String.valueOf(user_previous_points));
                        Constant.setString(activity, Constant.USER_POINTS, String.valueOf(user_previous_points));
                        Constant.addPoints(activity, Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)), 1, "redeem", "1");
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
                user_points_textView.setText(String.valueOf(user_previous_points));
                Constant.setString(activity, Constant.USER_POINTS, String.valueOf(user_previous_points));
                Constant.addPoints(activity, Integer.parseInt(Constant.getString(activity, Constant.USER_POINTS)), 1, "redeem", "1");
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}