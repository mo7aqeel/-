package com.gatsby.health;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class UsersData extends AppCompatActivity {

    private EditText mSearchEditText;
    private ArrayList<DataInputs> list;
    public static ArrayList<DataInputs> userList = new ArrayList<>();

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Activity case", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Activity case", "onStop()");
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_data);

        mSearchEditText = findViewById(R.id.search_edit_text);
        list = GetDataActivity.list;

        TableLayout table = findViewById(R.id.table_data);
        for (int i=1; i<table.getChildCount(); i++){
            View child = table.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        table.setStretchAllColumns(true);

        for (int i = 0; i< list.size(); i++){

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView textViewAdmin = new TextView(this);
            textViewAdmin.setText(list.get(i).getAdmin());
            textViewAdmin.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
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
            row.addView(textViewAdmin);
            row.addView(textViewBlood);
            row.addView(textViewO2);
            row.addView(textViewHeart);
            row.addView(textViewDate);
            table.addView(row);
        }
    }

    public void getSearch(View view) {
        userList.clear();
        String name = mSearchEditText.getText().toString();
        for (int i=0; i<list.size(); i++){
            if (list.get(i).getAdmin().equals(name))
                userList.add(list.get(i));
        }
        Intent intent = new Intent(UsersData.this, SearchResultActivity.class);
        intent.putExtra("search name", name);
        startActivity(intent);
    }
}