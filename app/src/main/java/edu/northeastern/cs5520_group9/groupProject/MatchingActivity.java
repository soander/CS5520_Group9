package edu.northeastern.cs5520_group9.groupProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.cs5520_group9.R;
import edu.northeastern.cs5520_group9.team.TeamNameActivity;

public class MatchingActivity extends AppCompatActivity {
    DatabaseReference roomRef;
    DatabaseReference roomsRef;
    Button createButton;
    Button joinButton;
    TextView title;
    TextView availableRooms;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        createButton = findViewById(R.id.createBTN);
        joinButton = findViewById(R.id.joinBTN);
        title = findViewById(R.id.textView);
        availableRooms = findViewById(R.id.textView2);

        database = FirebaseDatabase.getInstance();

        roomsRef = database.getReference("room");
        roomsRef.setValue("");

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createButton.setEnabled(false);
                roomRef = database.getReference("rooms player1");
                roomsRef = database.getReference("room");
                roomsRef.setValue("created");

                addRoomEventListener();
                roomRef.setValue("player1");
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference roomRef = database.getReference("rooms player2");
                addRoomEventListener();
                roomRef.setValue("player2");
            }
        });

        addRoomsEventListener();

    }

    private void addRoomEventListener(){
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                createButton.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(), GamePlayActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                createButton.setEnabled(true);
                Toast.makeText(MatchingActivity.this, "Error!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRoomsEventListener(){
        roomsRef = database.getReference("room");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if  (snapshot.getValue(String.class).equals("created")) {
                    availableRooms.setText("Play1's Room is available");
                }

                else{
                    availableRooms.setText("No existing room. Waiting for creating room.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //nothing
            }
        });
    }



}