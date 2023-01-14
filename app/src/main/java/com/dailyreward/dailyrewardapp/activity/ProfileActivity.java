package com.dailyreward.dailyrewardapp.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.dailyreward.dailyrewardapp.App;
import com.dailyreward.dailyrewardapp.R;
import com.dailyreward.dailyrewardapp.models.User;
import com.dailyreward.dailyrewardapp.utils.BaseUrl;
import com.dailyreward.dailyrewardapp.utils.Constant;
import com.dailyreward.dailyrewardapp.utils.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.dailyreward.dailyrewardapp.utils.Constant.hideKeyboard;

public class ProfileActivity extends AppCompatActivity {

    ProfileActivity activity;
    private EditText name_editText, email_editText, number_editText;
    private AppCompatButton update_profile_btn, privacy_policy_btn, terms_btn;
    private ProgressDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        activity = this;

        name_editText = findViewById(R.id.profile_name_edit_text);
        email_editText = findViewById(R.id.profile_email_edit_text);
        number_editText = findViewById(R.id.profile_number_edit_text);
        update_profile_btn = findViewById(R.id.update_profile_btn);
        privacy_policy_btn = findViewById(R.id.againBtn);
        terms_btn = findViewById(R.id.shareBtn);

        onClick();
    }

    public void telegramJoin(String url) {
        try {
            Uri webpage = Uri.parse(url);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Please install Telegram to join our group.",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void showProgressDialog() {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private void onClick() {
        name_editText.setText(Constant.getString(activity, Constant.USER_NAME));
        email_editText.setText(Constant.getString(activity, Constant.USER_EMAIL));
        number_editText.setText(Constant.getString(activity, Constant.USER_NUMBER));

        update_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)) {
                    if (name_editText.getText().toString().length() == 0) {
                        name_editText.setError(getResources().getString(R.string.enter_name));
                        name_editText.requestFocus();
                    } else if (number_editText.getText().toString().length() == 0) {
                        number_editText.setError(getResources().getString(R.string.enter_number));
                        number_editText.requestFocus();
                    } else if (number_editText.getText().toString().length() < 10) {
                        number_editText.setError(getResources().getString(R.string.enter_valid_number));
                        number_editText.requestFocus();
                    } else {
                        alertDialog = new ProgressDialog(activity);
                        alertDialog.setTitle(getResources().getString(R.string.updateing_profile));
                        alertDialog.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_account, null));
                        alertDialog.setMessage(getResources().getString(R.string.please_wait));
                        alertDialog.setCancelable(false);
                        hideKeyboard(activity);
                        showProgressDialog();
                        updateProfile(name_editText.getText().toString(), email_editText.getText().toString(), number_editText.getText().toString());
                    }
                } else {
                    Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
                }
            }
        });

        privacy_policy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, PrivacyActivity.class, "privacy");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        terms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.GotoNextActivity(activity, PrivacyActivity.class, "terms");
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    private void updateProfile(String name, String email, String number) {
        String points = Constant.getString(activity, Constant.USER_POINTS);
        String user_email = Constant.getString(activity, Constant.USER_EMAIL);
        String userId = Constant.getString(activity, Constant.USER_ID);

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("update_profile", "any");
        params.put("email", email);
        params.put("name", name);
        params.put("password", "");
        params.put("img", "");
        params.put("user_id", userId);
        params.put("number", number);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.UPDATE_PROFILE, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    hideProgressDialog();
                    boolean status = response.getBoolean("status");
                    if (status) {
                        JSONObject jsonObject = response.getJSONObject("0");
                        Constant.setString(activity, Constant.USER_ID, jsonObject.getString("id"));
                        final User user = new User(jsonObject.getString("name"), jsonObject.getString("number"), jsonObject.getString("email"), jsonObject.getString("device"), jsonObject.getString("points"), jsonObject.getString("referraled_with"), jsonObject.getString("status"), jsonObject.getString("referral_code"));
                        hideProgressDialog();

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
                        if (user.getPoints() != null) {
                            Constant.setString(activity, Constant.USER_POINTS, user.getPoints());
                            Log.e("TAG", "onDataChange: " + user.getPoints());
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

                        hideProgressDialog();
                        Constant.setString(activity, Constant.IS_LOGIN, "true");
                        Constant.showToastMessage(activity, getResources().getString(R.string.update_successfully));
                    } else {
                        Constant.showToastMessage(activity, "Not Updated Try Again...");
                    }
                } catch (JSONException e) {
                    hideProgressDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                hideProgressDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constant.showToastMessage(activity, getResources().getString(R.string.slow_internet_connection));
                }
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String isProfileUpdateEnabled = Constant.getString(activity, Constant.IS_P_UPDATE_ENABLED);

        if (isProfileUpdateEnabled.equalsIgnoreCase("true")){
            update_profile_btn.setEnabled(true);
            name_editText.setEnabled(true);
            email_editText.setEnabled(true);
            number_editText.setEnabled(true);
        } else {
            update_profile_btn.setEnabled(false);
            name_editText.setEnabled(false);
            email_editText.setEnabled(false);
            number_editText.setEnabled(false);
        }
    }

}