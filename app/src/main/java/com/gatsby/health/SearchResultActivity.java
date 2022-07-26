package com.gatsby.health;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        ArrayList<DataInputs> list = UsersData.userList;

        Intent intent = getIntent();
        TextView nameText = findViewById(R.id.name_search_text_view);
        nameText.setText(intent.getStringExtra("search name"));

        TableLayout table = findViewById(R.id.table_search);
        for (int i=1; i<table.getChildCount(); i++){
            View child = table.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        table.setStretchAllColumns(true);

        for (int i = 0; i< list.size(); i++){

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView textViewBlood = new TextView(this);
            textViewBlood.setText(list.get(i).getBlood_press());
            textViewBlood.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            TextView textViewO2 = new TextView(this);
            textViewO2.setText(list.get(i).getO2_per());
            textViewO2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            TextView textViewHeart = new TextView(this);
            textViewHeart.setText(list.get(i).getHeart_beats());
            textViewHeart.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            TextView textViewDate = new TextView(this);
            textViewDate.setText(list.get(i).getDate());
            textViewDate.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            row.addView(textViewBlood);
            row.addView(textViewO2);
            row.addView(textViewHeart);
            row.addView(textViewDate);
            table.addView(row);
        }
    }
}