package com.gsss.meds;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    CheckBox cbMorning, cbAfternoon, cbNight;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cbMorning = findViewById(R.id.cb_morning);
        cbAfternoon = findViewById(R.id.cb_afternoon);
        cbNight = findViewById(R.id.cb_night);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    @Override
    protected void onStart() {
        super.onStart();
        ListenForChangeInData();
    }

    private void ListenForChangeInData() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Boolean> medsTaken = (Map<String, Boolean>) dataSnapshot.getValue();
                if (medsTaken != null) {
                    cbMorning.setChecked(medsTaken.get("morning"));
                    cbAfternoon.setChecked(medsTaken.get("afternoon"));
                    cbNight.setChecked(medsTaken.get("night"));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        mDatabase.child("meds_taken").addValueEventListener(listener);
    }
    public void onSaveButtonClick(View view) {
        saveDataToFirebase();
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }
    private void saveDataToFirebase() {
        Map<String, Boolean> medsTaken = new HashMap<>();
        medsTaken.put("morning", cbMorning.isChecked());
        medsTaken.put("afternoon", cbAfternoon.isChecked());
        medsTaken.put("night", cbNight.isChecked());

        mDatabase.child("meds_taken").setValue(medsTaken);
    }

}
