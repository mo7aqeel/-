package com.gatsby.health;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class GetDataActivity extends AppCompatActivity {

    private EditText date, month, year;
    private ProgressBar progressBar;

    public static ArrayList<DataInputs> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        year = findViewById(R.id.year_edit_text);
        month = findViewById(R.id.month_edit_text);
        date = findViewById(R.id.date_edit_text);
        progressBar = findViewById(R.id.progressBar4);

    }

    private void getData(String year, String month, String day){
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users data");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot yearShot = snapshot.child(year);
                DataSnapshot monthShot = yearShot.child(month);
                DataSnapshot dayShot = monthShot.child(day);

                if (monthShot.hasChildren() && dayShot.hasChildren()) {

                    for (DataSnapshot n : dayShot.getChildren()) {
                        for (DataSnapshot n2 : n.getChildren()){
                            DataInputs dataInputs = new DataInputs();
                            for (DataSnapshot n3 : n2.getChildren()){
                                switch (n3.getKey()) {
                                    case "admin":
                                        dataInputs.setAdmin(n3.getValue(String.class));
                                        break;
                                    case "blood_press":
                                        dataInputs.setBlood_press(n3.getValue(String.class));
                                        break;
                                    case "heart_beats":
                                        dataInputs.setHeart_beats(n3.getValue(String.class));
                                        break;
                                    case "o2_per":
                                        dataInputs.setO2_per(n3.getValue(String.class));
                                        break;
                                    case "date":
                                        dataInputs.setDate(n3.getValue(String.class));
                                        break;
                                }//end switch()
                            }//end for (n3)
                            list.add(dataInputs);
                        }//end for (n2)
                    }//end for (n)
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(GetDataActivity.this, UsersData.class);
                    startActivity(intent);
                }//end if()
                else{
                    Toast.makeText(GetDataActivity.this, "No data!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GetDataActivity.this, "Error!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getDataMonth(String years, String months) {
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users data");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot yearShot = snapshot.child(years);
                DataSnapshot monthShot = yearShot.child(months);

                if (monthShot.hasChildren()) {

                    for (DataSnapshot n : monthShot.getChildren()) {
                        for (DataSnapshot n2 : n.getChildren()){
                            for (DataSnapshot n3 : n2.getChildren()){
                                DataInputs dataInputs = new DataInputs();
                                for (DataSnapshot n4 : n3.getChildren()) {
                                    switch (n4.getKey()) {
                                        case "admin":
                                            dataInputs.setAdmin(n4.getValue(String.class));
                                            break;
                                        case "blood_press":
                                            dataInputs.setBlood_press(n4.getValue(String.class));
                                            break;
                                        case "heart_beats":
                                            dataInputs.setHeart_beats(n4.getValue(String.class));
                                            break;
                                        case "o2_per":
                                            dataInputs.setO2_per(n4.getValue(String.class));
                                            break;
                                        case "date":
                                            dataInputs.setDate(n4.getValue(String.class));
                                            break;
                                    }//end switch()
                                }//end for (n4)
                                list.add(dataInputs);
                            }//end for (n3)
                        }//end for (n2)
                    }//end for (n)
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(GetDataActivity.this, UsersData.class);
                    startActivity(intent);
                }//end if()
                else{
                    Toast.makeText(GetDataActivity.this, "No data!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GetDataActivity.this, "Error!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getDataYear(String years) {
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users data");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot yearShot = snapshot.child(years);

                if (yearShot.hasChildren()) {

                    for (DataSnapshot n : yearShot.getChildren()) {
                        for (DataSnapshot n2 : n.getChildren()){
                            for (DataSnapshot n3 : n2.getChildren()){
                                for (DataSnapshot n4 : n3.getChildren()) {
                                    DataInputs dataInputs = new DataInputs();
                                    for (DataSnapshot n5 : n4.getChildren()){
                                        switch (n5.getKey()) {
                                            case "admin":
                                                dataInputs.setAdmin(n5.getValue(String.class));
                                                break;
                                            case "blood_press":
                                                dataInputs.setBlood_press(n5.getValue(String.class));
                                                break;
                                            case "heart_beats":
                                                dataInputs.setHeart_beats(n5.getValue(String.class));
                                                break;
                                            case "o2_per":
                                                dataInputs.setO2_per(n5.getValue(String.class));
                                                break;
                                            case "date":
                                                dataInputs.setDate(n5.getValue(String.class));
                                                break;
                                        }//end switch()
                                    }//end for (n5)
                                    list.add(dataInputs);
                                }//end for (n4)
                            }//end for (n3)
                        }//end for (n2)
                    }//end for (n)
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(GetDataActivity.this, UsersData.class);
                    startActivity(intent);
                }//end if()
                else{
                    Toast.makeText(GetDataActivity.this, "No data!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GetDataActivity.this, "Error!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void checkTime(View view) {

        String years = year.getText().toString();
        String months = month.getText().toString();

        if (years.isEmpty()){
            year.setError("This field must be filled in");
            year.requestFocus();
            return;
        }

        if (!date.getText().toString().isEmpty() && !month.getText().toString().isEmpty()
                && !year.getText().toString().isEmpty())
            checkTimeOfDay(years, months);

        if (date.getText().toString().isEmpty() &&
                !month.getText().toString().isEmpty() && !year.getText().toString().isEmpty())
            checkTimeOfMonth(years, months);

        if (date.getText().toString().isEmpty() &&
                month.getText().toString().isEmpty() && !year.getText().toString().isEmpty())
            checkTimeOfYear(years);

    }

    private void checkTimeOfYear(String years) {
        list.clear();
        getDataYear(years);
    }

    private void checkTimeOfMonth(String years, String months){
        list.clear();
        getDataMonth(years, months);
    }

    private void checkTimeOfDay(String years, String months){
        list.clear();
        String day = date.getText().toString();
        getData(years, months, day);
    }
}