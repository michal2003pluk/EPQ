package com.thundercandy.epq;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundercandy.epq.data.SessionCard;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SessionResultsActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView txtSuccess, txtFail;
    RecyclerView recViewSessionResults;
    TextView txtCardsFlickedThrough, txtCardsLearnt, txtSuccessRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_results);
        btnBack = findViewById(R.id.btnBack);
        txtSuccess = findViewById(R.id.txtSuccess);
        txtFail = findViewById(R.id.txtFail);
        recViewSessionResults = findViewById(R.id.recViewSessionResults);
        txtCardsFlickedThrough = findViewById(R.id.txtCardsFlickedThrough);
        txtCardsLearnt = findViewById(R.id.txtCardsLearnt);
        txtSuccessRate = findViewById(R.id.txtSuccessRate);

        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<SessionCard>>() {
        }.getType();

        Intent receivedIntent = getIntent();
        ArrayList<SessionCard> cards = gson.fromJson(receivedIntent.getStringExtra("SessionCards"), type);

        SessionResultsRecViewAdapter adapter = new SessionResultsRecViewAdapter(this);
        recViewSessionResults.setAdapter(adapter);
        recViewSessionResults.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCards(cards);
        adapter.setLearntThreshold(Utils.getLearntThresholdPercent(this));

        setSessionStats(cards);
    }

    private void setSessionStats(ArrayList<SessionCard> cards) {
        String[] stats = {"", "", ""};
        if (cards.size() == 0) {
            stats[0] = "N/A";
            stats[1] = "N/A";
            stats[2] = "N/A";
        } else {
            int total_flicked = 0, total_known = 0, total_learnt = 0;
            for (SessionCard sc : cards) {
                Log.d("LOGLOGLOG", sc.toShortString());
                total_flicked += sc.getTimesSeen();
                total_known += sc.getTimesKnown();
                total_learnt += sc.isLearnt() ? 1 : 0;
            }
            stats[0] = String.valueOf(total_flicked);
            stats[1] = String.valueOf(Math.round((double) total_learnt / cards.size() * 100));
            stats[2] = String.valueOf(Math.round((double) total_known / total_flicked * 100));

            if (total_learnt == cards.size()) {
                txtSuccess.setVisibility(View.VISIBLE);
                txtFail.setVisibility(View.GONE);
            } else {
                txtFail.setVisibility(View.VISIBLE);
                txtSuccess.setVisibility(View.GONE);
            }
        }
        txtCardsFlickedThrough.setText(stats[0]);
        txtCardsLearnt.setText(stats[1]);
        txtSuccessRate.setText(stats[2]);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SessionResultsActivity.this);

        builder.setTitle("Are you sure?");
        builder.setMessage("This will take you back to the home screen");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
            super.onBackPressed();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.cancel();
        });
        builder.setCancelable(true);
        builder.show();
    }
}
