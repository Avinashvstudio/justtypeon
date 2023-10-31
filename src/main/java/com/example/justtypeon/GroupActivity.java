package com.example.justtypeon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justtypeon.Adapter.GroupAdapter;
import com.example.justtypeon.Model.GroupChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GroupActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    GroupAdapter groupAdapter;
    List<GroupChat> mGroupChat;

    FirebaseUser fuser;
    DatabaseReference reference;

    ImageButton btn_send;
    EditText text_send;

    Intent intent;
    String groupID; // The ID of the group
    String currentUserId; // The ID of the current user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Group Chat Title"); // Set the group chat title

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        groupID = intent.getStringExtra("groupID");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the current user's ID

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Groups").child(groupID); // Update with your group database reference

        btn_send.setOnClickListener(view -> {
            String msg = text_send.getText().toString();
            if (!msg.equals("")) {
                sendMessage(currentUserId, msg);
            } else {
                Toast.makeText(getApplicationContext(), "You can't send empty message", Toast.LENGTH_LONG).show();
            }
            text_send.setText("");
        });

        readMessages();
    }

    private void sendMessage(String sender, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups").child(groupID).child("Messages"); // Update with your group message database reference
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("message", message);

        reference.push().setValue(hashMap);
    }

    private void readMessages() {
        mGroupChat = new ArrayList<>();
        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupID).child("Messages"); // Update with your group message database reference
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGroupChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GroupChat groupChat = snapshot.getValue(GroupChat.class);
                    if (groupChat != null) {
                        mGroupChat.add(groupChat);
                    }
                }
                groupAdapter = new GroupAdapter(GroupActivity.this, mGroupChat, "", currentUserId);
                recyclerView.setAdapter(groupAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}