package com.dailyreward.dailyrewardapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.dailyreward.dailyrewardapp.R;
import com.dailyreward.dailyrewardapp.utils.Constant;

public class GameActivity extends AppCompatActivity {

    GameActivity activity;
    LinearLayout promotion_height;
    ImageView mp_game1, mp_game2, game_image1, game_image2, game_image3, game_image4, game_image5, game_image6, game_image7, game_image8,
            game_image9, game_image10, game_image11, game_image12, game_image13, game_image14, game_image15, game_image16, game_image17,
            game_image18, promotion_1, promotion_2, promotion_3, promotion_4;
    TextView game_name1, game_name2, game_name3, game_name4, game_name5, game_name6, game_name7, game_name8,
            game_name9, game_name10, game_name11, game_name12, game_name13, game_name14, game_name15, game_name16, game_name17, game_name18,
            user_points_text_view, game_count_textView, total_game, mp_game1_title, mp_game2_title;

    CardView mp_game1_layout, mp_game2_layout, game1, game2, game3, game4, game5, game6, game7, game8, game9, game10, game11, game12, game13, game14, game15, game16, game17, game18;

    private final String TAG = GameActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        activity = this;
        user_points_text_view = findViewById(R.id.user_points_text_view);
        game_count_textView = findViewById(R.id.game_count_textView);
        total_game = findViewById(R.id.total_game);
        promotion_height = findViewById(R.id.promotion_height);
        promotion_1 = findViewById(R.id.promotion_1);
        promotion_2 = findViewById(R.id.promotion_2);
        promotion_3 = findViewById(R.id.promotion_3);
        promotion_4 = findViewById(R.id.promotion_4);
        mp_game1 = findViewById(R.id.mp_game1);
        mp_game2 = findViewById(R.id.mp_game2);
        mp_game1_layout = findViewById(R.id.mp_game1_layout);
        mp_game2_layout = findViewById(R.id.mp_game2_layout);
        mp_game1_title = findViewById(R.id.mp_game1_title);
        mp_game2_title = findViewById(R.id.mp_game2_title);
        game_image1 = findViewById(R.id.game_image1);
        game_image2 = findViewById(R.id.game_image2);
        game_image3 = findViewById(R.id.game_image3);
        game_image4 = findViewById(R.id.game_image4);
        game_image5 = findViewById(R.id.game_image5);
        game_image6 = findViewById(R.id.game_image6);
        game_image7 = findViewById(R.id.game_image7);
        game_image8 = findViewById(R.id.game_image8);
        game_image9 = findViewById(R.id.game_image9);
        game_image10 = findViewById(R.id.game_image10);
        game_image11 = findViewById(R.id.game_image11);
        game_image12 = findViewById(R.id.game_image12);
        game_image13 = findViewById(R.id.game_image13);
        game_image14 = findViewById(R.id.game_image14);
        game_image15 = findViewById(R.id.game_image15);
        game_image16 = findViewById(R.id.game_image16);
        game_image17 = findViewById(R.id.game_image17);
        game_image18 = findViewById(R.id.game_image18);
        game_name1 = findViewById(R.id.game_name1);
        game_name2 = findViewById(R.id.game_name2);
        game_name3 = findViewById(R.id.game_name3);
        game_name4 = findViewById(R.id.game_name4);
        game_name5 = findViewById(R.id.game_name5);
        game_name6 = findViewById(R.id.game_name6);
        game_name7 = findViewById(R.id.game_name7);
        game_name8 = findViewById(R.id.game_name8);
        game_name9 = findViewById(R.id.game_name9);
        game_name10 = findViewById(R.id.game_name10);
        game_name11 = findViewById(R.id.game_name11);
        game_name12 = findViewById(R.id.game_name12);
        game_name13 = findViewById(R.id.game_name13);
        game_name14 = findViewById(R.id.game_name14);
        game_name15 = findViewById(R.id.game_name15);
        game_name16 = findViewById(R.id.game_name16);
        game_name17 = findViewById(R.id.game_name17);
        game_name18 = findViewById(R.id.game_name18);
        game1 = findViewById(R.id.game1);
        game2 = findViewById(R.id.game2);
        game3 = findViewById(R.id.game3);
        game4 = findViewById(R.id.game4);
        game5 = findViewById(R.id.game5);
        game6 = findViewById(R.id.game6);
        game7 = findViewById(R.id.game7);
        game8 = findViewById(R.id.game8);
        game9 = findViewById(R.id.game9);
        game10 = findViewById(R.id.game10);
        game11 = findViewById(R.id.game11);
        game12 = findViewById(R.id.game12);
        game13 = findViewById(R.id.game13);
        game14 = findViewById(R.id.game14);
        game15 = findViewById(R.id.game15);
        game16 = findViewById(R.id.game16);
        game17 = findViewById(R.id.game17);
        game18 = findViewById(R.id.game18);

        String is_g1_enabled = Constant.getString(activity, Constant.IS_G1_ENABLE);
        String is_g2_enabled = Constant.getString(activity, Constant.IS_G2_ENABLE);
        String is_g3_enabled = Constant.getString(activity, Constant.IS_G3_ENABLE);
        String is_g4_enabled = Constant.getString(activity, Constant.IS_G4_ENABLE);
        String is_g5_enabled = Constant.getString(activity, Constant.IS_G5_ENABLE);
        String is_g6_enabled = Constant.getString(activity, Constant.IS_G6_ENABLE);
        String is_g7_enabled = Constant.getString(activity, Constant.IS_G7_ENABLE);
        String is_g8_enabled = Constant.getString(activity, Constant.IS_G8_ENABLE);
        String is_g9_enabled = Constant.getString(activity, Constant.IS_G9_ENABLE);
        String is_g10_enabled = Constant.getString(activity, Constant.IS_G10_ENABLE);
        String is_g11_enabled = Constant.getString(activity, Constant.IS_G11_ENABLE);
        String is_g12_enabled = Constant.getString(activity, Constant.IS_G12_ENABLE);
        String is_g13_enabled = Constant.getString(activity, Constant.IS_G13_ENABLE);
        String is_g14_enabled = Constant.getString(activity, Constant.IS_G14_ENABLE);
        String is_g15_enabled = Constant.getString(activity, Constant.IS_G15_ENABLE);
        String is_g16_enabled = Constant.getString(activity, Constant.IS_G16_ENABLE);
        String is_g17_enabled = Constant.getString(activity, Constant.IS_G17_ENABLE);
        String is_g18_enabled = Constant.getString(activity, Constant.IS_G18_ENABLE);
        String is_g19_enabled = Constant.getString(activity, Constant.IS_G19_ENABLE);
        String is_g20_enabled = Constant.getString(activity, Constant.IS_G20_ENABLE);

        Constant.adsShowingDialog(activity);

        game_count_textView.setText(Constant.getString(activity, Constant.GAME_COUNT));
        total_game.setText(Constant.getString(activity, Constant.DAILY_GAME_COUNT));
        ViewGroup.LayoutParams params = promotion_height.getLayoutParams();
        params.height = Integer.parseInt(Constant.getString(activity, Constant.PG_BANNER_HEIGHT));
        promotion_height.setLayoutParams(params);
        promotion1();
        promotion2();
        promotion3();
        promotion4();

        if (Constant.isNetworkAvailable(activity) && Constant.isOnline(activity)){
            if (is_g1_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G1_IMAGE))
                        .into(mp_game1);

                mp_game1_title.setText(Constant.getString(activity, Constant.G1_TITLE));
                mp_game1_layout.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G1_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                mp_game1_layout.setVisibility(View.GONE);
            }

            if (is_g2_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G2_IMAGE))
                        .into(mp_game2);

                mp_game2_title.setText(Constant.getString(activity, Constant.G2_TITLE));
                mp_game2_layout.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G2_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                mp_game2_layout.setVisibility(View.GONE);
            }

            if (is_g3_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G3_IMAGE))
                        .into(game_image1);

                game_name1.setText(Constant.getString(activity, Constant.G3_TITLE));
                game1.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G3_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game1.setVisibility(View.GONE);
            }

            if (is_g4_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G4_IMAGE))
                        .into(game_image2);

                game_name2.setText(Constant.getString(activity, Constant.G4_TITLE));
                game2.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G4_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game2.setVisibility(View.GONE);
            }

            if (is_g5_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G5_IMAGE))
                        .into(game_image3);

                game_name3.setText(Constant.getString(activity, Constant.G5_TITLE));
                game3.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G5_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game3.setVisibility(View.GONE);
            }

            if (is_g6_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G6_IMAGE))
                        .into(game_image4);

                game_name4.setText(Constant.getString(activity, Constant.G6_TITLE));
                game4.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G6_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game4.setVisibility(View.GONE);
            }

            if (is_g7_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G7_IMAGE))
                        .into(game_image5);

                game_name5.setText(Constant.getString(activity, Constant.G7_TITLE));
                game5.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G7_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game5.setVisibility(View.GONE);
            }

            if (is_g8_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G8_IMAGE))
                        .into(game_image6);

                game_name6.setText(Constant.getString(activity, Constant.G8_TITLE));
                game6.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G8_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game6.setVisibility(View.GONE);
            }

            if (is_g9_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G9_IMAGE))
                        .into(game_image7);

                game_name7.setText(Constant.getString(activity, Constant.G9_TITLE));
                game7.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G9_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game7.setVisibility(View.GONE);
            }

            if (is_g10_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G10_IMAGE))
                        .into(game_image8);

                game_name8.setText(Constant.getString(activity, Constant.G10_TITLE));
                game8.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G10_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game8.setVisibility(View.GONE);
            }

            if (is_g11_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G11_IMAGE))
                        .into(game_image9);

                game_name9.setText(Constant.getString(activity, Constant.G11_TITLE));
                game9.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G11_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game9.setVisibility(View.GONE);
            }

            if (is_g12_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G12_IMAGE))
                        .into(game_image10);

                game_name10.setText(Constant.getString(activity, Constant.G12_TITLE));
                game10.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G12_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game10.setVisibility(View.GONE);
            }

            if (is_g13_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G13_IMAGE))
                        .into(game_image11);

                game_name11.setText(Constant.getString(activity, Constant.G13_TITLE));
                game11.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G13_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game11.setVisibility(View.GONE);
            }

            if (is_g14_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G14_IMAGE))
                        .into(game_image12);

                game_name12.setText(Constant.getString(activity, Constant.G14_TITLE));
                game12.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G14_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game12.setVisibility(View.GONE);
            }

            if (is_g15_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G15_IMAGE))
                        .into(game_image13);

                game_name13.setText(Constant.getString(activity, Constant.G15_TITLE));
                game13.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G15_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game13.setVisibility(View.GONE);
            }

            if (is_g16_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G16_IMAGE))
                        .into(game_image14);

                game_name14.setText(Constant.getString(activity, Constant.G16_TITLE));
                game14.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G16_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game14.setVisibility(View.GONE);
            }

            if (is_g17_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G17_IMAGE))
                        .into(game_image15);

                game_name15.setText(Constant.getString(activity, Constant.G17_TITLE));
                game15.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G17_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game15.setVisibility(View.GONE);
            }

            if (is_g18_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G18_IMAGE))
                        .into(game_image16);

                game_name16.setText(Constant.getString(activity, Constant.G18_TITLE));
                game16.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G18_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game16.setVisibility(View.GONE);
            }

            if (is_g19_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G19_IMAGE))
                        .into(game_image17);

                game_name17.setText(Constant.getString(activity, Constant.G19_TITLE));
                game17.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G19_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game17.setVisibility(View.GONE);
            }

            if (is_g20_enabled.equalsIgnoreCase("true")){
                Glide.with(activity)
                        .load(Constant.getString(activity, Constant.G20_IMAGE))
                        .into(game_image18);

                game_name18.setText(Constant.getString(activity, Constant.G20_TITLE));
                game18.setOnClickListener(v -> {
                    Intent intent = new Intent(activity,GameLoader.class);
                    intent.putExtra("GAME_PASSING", Constant.getString(activity, Constant.G20_LINK));
                    startActivity(intent);
                    finish();
                });
            } else {
                game18.setVisibility(View.GONE);
            }
        } else {
            Constant.showInternetErrorDialog(activity, "Please Check your Internet Connection");
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        user_points_text_view.setText(Constant.getString(activity, Constant.USER_POINTS));
    }



    private void promotion1() {
        String isPromotion1True = Constant.getString(activity, Constant.IS_PG1_ENABLED);
        String pg1OpenWith = Constant.getString(activity, Constant.PG1_OPEN_WITH);

        if (isPromotion1True.equalsIgnoreCase("true")) {
            Glide.with(activity)
                    .load(Constant.getString(activity, Constant.PG1_IMAGE))
                    .into(promotion_1);

            promotion_1.setOnClickListener(v -> {
                if (pg1OpenWith.equalsIgnoreCase("chrome_earning_tab")) {
                    downloadAction(Constant.getString(activity, Constant.PG1_LINK));
                } else if (pg1OpenWith.equalsIgnoreCase("external_browser")){
                    externalAction(Constant.getString(activity, Constant.PG1_LINK));
                }
            });
        }
        else {
            promotion_1.setVisibility(View.GONE);
        }
    }

    private void promotion2() {
        String isPromotion2True = Constant.getString(activity, Constant.IS_PG2_ENABLED);
        String pg2OpenWith = Constant.getString(activity, Constant.PG2_OPEN_WITH);

        if (isPromotion2True.equalsIgnoreCase("true")) {
            Glide.with(activity)
                    .load(Constant.getString(activity, Constant.PG2_IMAGE))
                    .into(promotion_2);

            promotion_2.setOnClickListener(v -> {
                if (pg2OpenWith.equalsIgnoreCase("chrome_earning_tab")) {
                    downloadAction(Constant.getString(activity, Constant.PG2_LINK));
                } else if (pg2OpenWith.equalsIgnoreCase("external_browser")){
                    externalAction(Constant.getString(activity, Constant.PG2_LINK));
                }
            });
        }
        else {
            promotion_2.setVisibility(View.GONE);
        }
    }

    private void promotion3() {
        String isPromotion3True = Constant.getString(activity, Constant.IS_PG3_ENABLED);
        String pg3OpenWith = Constant.getString(activity, Constant.PG3_OPEN_WITH);

        if (isPromotion3True.equalsIgnoreCase("true")) {
            Glide.with(activity)
                    .load(Constant.getString(activity, Constant.PG3_IMAGE))
                    .into(promotion_3);

            promotion_3.setOnClickListener(v -> {
                if (pg3OpenWith.equalsIgnoreCase("chrome_earning_tab")) {
                    downloadAction(Constant.getString(activity, Constant.PG3_LINK));
                } else if (pg3OpenWith.equalsIgnoreCase("external_browser")){
                    externalAction(Constant.getString(activity, Constant.PG3_LINK));
                }
            });
        }
        else {
            promotion_3.setVisibility(View.GONE);
        }
    }

    private void promotion4() {
        String isPromotion4True = Constant.getString(activity, Constant.IS_PG4_ENABLED);
        String pg4OpenWith = Constant.getString(activity, Constant.PG4_OPEN_WITH);

        if (isPromotion4True.equalsIgnoreCase("true")) {
            Glide.with(activity)
                    .load(Constant.getString(activity, Constant.PG4_IMAGE))
                    .into(promotion_4);

            promotion_4.setOnClickListener(v -> {
                if (pg4OpenWith.equalsIgnoreCase("chrome_earning_tab")) {
                    downloadAction(Constant.getString(activity, Constant.PG4_LINK));
                } else if (pg4OpenWith.equalsIgnoreCase("external_browser")){
                    externalAction(Constant.getString(activity, Constant.PG4_LINK));
                }
            });
        }
        else {
            promotion_4.setVisibility(View.GONE);
        }
    }


    private void downloadAction(String url) {
        Intent intent = new Intent(activity,GameLoader.class);
        intent.putExtra("GAME_PASSING", url);
        startActivity(intent);
        finish();
    }

    private void externalAction(String url){
        try {
            Uri webpage = Uri.parse(url);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Failed to load.",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        finish();
    }


}