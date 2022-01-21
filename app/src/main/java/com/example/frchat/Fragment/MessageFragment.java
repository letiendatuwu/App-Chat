package com.example.frchat.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.frchat.R;
import com.example.frchat.Utilities.Constants;
import com.example.frchat.Utilities.PreferenceManager;
//import com.example.frchat.adapter.RecentAdapter;
import com.example.frchat.activities.ChatActivity;
import com.example.frchat.adapter.RecentConversionAdapter;
import com.example.frchat.listeners.ConversionListener;
import com.example.frchat.models.ChatMessage;
import com.example.frchat.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageFragment extends Fragment implements ConversionListener {
    private PreferenceManager preferenceManager;
    RecyclerView conversionRecyclerView;
    ProgressBar progressBar;
    private List<ChatMessage> conversions;
    private RecentConversionAdapter conversionsAdapter;
    private FirebaseFirestore database;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        conversionRecyclerView = view.findViewById(R.id.conversionRecyclerView);
        preferenceManager = new PreferenceManager(getContext());
        init();
        listenConversion();
        return view;
    }
    private void init(){
        conversions= new ArrayList<>();
        conversionsAdapter = new RecentConversionAdapter(conversions,this);
        conversionRecyclerView.setAdapter(conversionsAdapter);
        database =FirebaseFirestore.getInstance();
    }

    private void listenConversion(){
        database.collection(Constants.KEY_COLLECTION_CONVERSIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
      if (error != null){
          return;
      }
      if (value!= null){
          for(DocumentChange documentChange : value.getDocumentChanges()){
              if (documentChange.getType() ==DocumentChange.Type.ADDED){
                  String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                  String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                  ChatMessage chatMessage= new ChatMessage();
                  chatMessage.senderId= senderId;
                  chatMessage.receiverId = receiverId;
                  if (preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)){
                      chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                      chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                      chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                  }else {
                      chatMessage.conversionImage= documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                      chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                      chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                  }
                  chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                  chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                  conversions.add(chatMessage);
              }else if(documentChange.getType()== DocumentChange.Type.MODIFIED){
                  for (int i = 0; i< conversions.size(); i++){
                      String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                      String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                      if (conversions.get(i).senderId.equals(senderId) && conversions.get(i).receiverId.equals(receiverId)){
                          conversions.get(i).message= documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                          conversions.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                          break;
                      }
                  }
              }
          }
          Collections.sort(conversions,(obj1, obj2)->obj2.dateObject.compareTo(obj1.dateObject));
          conversionsAdapter.notifyDataSetChanged();
          conversionRecyclerView.smoothScrollToPosition(0);
          conversionRecyclerView.setVisibility(View.VISIBLE);
          progressBar.setVisibility(View.GONE);
      }
    };
    @Override
    public void onConversionClicked(User user) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER,user);
        startActivity(intent);
    }
}