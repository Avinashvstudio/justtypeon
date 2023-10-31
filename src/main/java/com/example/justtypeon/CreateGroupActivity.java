package com.example.justtypeon;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.justtypeon.Model.GroupChat;
import com.example.justtypeon.Model.User;
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
import java.util.Map;

public class CreateGroupActivity extends AppCompatActivity {
    private EditText editTextGroupName;
    private Button buttonCreateGroup;
    private LinearLayout memberListLayout;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference usersRef, groupsRef;

    private List<User> userList = new ArrayList<>();
    private List<String> selectedUserIds = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        editTextGroupName = findViewById(R.id.editTextGroupName);
        buttonCreateGroup = findViewById(R.id.buttonCreateGroup);
        memberListLayout = findViewById(R.id.memberListLayout);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        groupsRef = FirebaseDatabase.getInstance().getReference("Groups");

        // Load a list of users from Firebase and display them in a LinearLayout with checkboxes for selection.
        loadUserList();

        buttonCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroup();
            }
        });
    }

    private void loadUserList() {
        // Fetch the list of users from Firebase and populate the memberListLayout.
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                memberListLayout.removeAllViews();
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                }
                for (User user : userList) {
                    // Create a CheckBox for each user
                    CheckBox checkBox = new CheckBox(CreateGroupActivity.this);
                    checkBox.setText(user.getUsername());
                    checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            selectedUserIds.add(user.getId());
                        } else {
                            selectedUserIds.remove(user.getId());
                        }
                    });
                    memberListLayout.addView(checkBox);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors if necessary.
            }
        });
    }

    private void createGroup() {
        final String groupName = editTextGroupName.getText().toString().trim();

        if (groupName.isEmpty()) {
            editTextGroupName.setError("Group name is required");
            editTextGroupName.requestFocus();
            return;
        }

        if (selectedUserIds.isEmpty()) {
            // Inform the user to select at least one member (e.g., display a message or use a Snackbar).
            return;
        }

        // Generate a unique group ID
        String groupId = groupsRef.push().getKey();

        // Create a new group
        GroupChat group = new GroupChat(currentUser.getUid(), "Your Name", groupName, "Welcome to the group!", false);

        // Save the group to the database
        groupsRef.child(groupId).setValue(group);

        // Add the group to the list of groups for each member
        updateGroupListForMembers(groupId, selectedUserIds);

        // Inform the user that the group has been created (e.g., display a success message).
    }

    private void updateGroupListForMembers(final String groupId, final List<String> memberIds) {
        for (final String memberId : memberIds) {
            usersRef.child(memberId).child("groups").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> groupList = new ArrayList<>();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String existingGroupId = snapshot.getValue(String.class);
                            groupList.add(existingGroupId);
                        }
                    }
                    groupList.add(groupId);
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("groups", groupList);
                    usersRef.child(memberId).updateChildren(updates);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors if necessary.
                }
            });
        }
    }
}
