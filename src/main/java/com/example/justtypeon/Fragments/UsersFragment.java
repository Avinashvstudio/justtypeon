//package com.example.justtypeon.Fragments;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//
//import com.example.justtypeon.Adapter.UserAdapter;
//import com.example.justtypeon.Model.User;
//import com.example.justtypeon.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class UsersFragment extends Fragment {
//    private RecyclerView recyclerView;
//    private UserAdapter userAdapter;
//    private List<User> mUsers;
//    EditText search_users;
//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_users, container, false);
//        recyclerView = view.findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mUsers = new ArrayList<>();
//        readUsers();
//        // searchUsers("");
//        search_users = view.findViewById(R.id.search_users);
//        search_users.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                searchUsers(charSequence.toString().toLowerCase());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        return view;
//    }
//    private  void  searchUsers(final  String s){
//
//        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
//        Query query = FirebaseDatabase.getInstance().getReference("User").orderByChild("search").startAt(s).endAt(s+"\uf8ff");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mUsers.clear();
//                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
//                    User user  = snapshot.getValue(User.class);
//                    assert  user!=null;
//                    assert fuser !=null;
//                    if(fuser.getUid().equalsIgnoreCase(user.getId())){
//                        mUsers.add(user);
//                    }
//                }
//                userAdapter = new UserAdapter(getContext(),mUsers,false);
//                recyclerView.setAdapter(userAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//    private void readUsers(){
//        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mUsers.clear();
//                if(search_users.getText().toString().equals("")){
//                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
//                        User user = snapshot.getValue(User.class);
//                        assert  user!=null;
//                        assert firebaseUser !=null;
//                        if(!firebaseUser.getUid().equalsIgnoreCase(user.getId())){
//                            mUsers.add(user);
//                        }
//
//                    }
//                    userAdapter = new UserAdapter(getContext(),mUsers,false);
//                    recyclerView.setAdapter(userAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }
//}
//
package com.example.justtypeon.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.justtypeon.Adapter.UserAdapter;
import com.example.justtypeon.Model.User;
import com.example.justtypeon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class UsersFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    EditText search_users;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers = new ArrayList<>();
        readUsers();
        search_users = view.findViewById(R.id.search_users);


        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<User> newList = new ArrayList<>();


                for( int j = 0 ; j < mUsers.size() ; j++ )
                {
                    if(mUsers.get(j).getUsername().toString().toLowerCase().contains(charSequence.toString().toLowerCase()))
                    {
                        newList.add(mUsers.get(j));
                    }
                }
                userAdapter = new UserAdapter(getContext(),newList,false);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    private void readUsers(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                if(search_users.getText().toString().equals("")){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        assert  user!=null;
                        assert firebaseUser !=null;
                        if(!firebaseUser.getUid().equalsIgnoreCase(user.getId())){
                            mUsers.add(user);
                        }

                    }
                    userAdapter = new UserAdapter(getContext(),mUsers,false);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}