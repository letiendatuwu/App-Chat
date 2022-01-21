package com.example.frchat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.frchat.R;
import com.example.frchat.Utilities.Constants;
import com.example.frchat.Utilities.PreferenceManager;
import com.example.frchat.adapter.MenuAdapter;
import com.example.frchat.databinding.ActivityMenuBinding;
import com.example.frchat.listeners.MenuListener;
import com.example.frchat.models.Menu;
import com.example.frchat.models.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private ActivityMenuBinding binding;
    private List<Menu> menuAccounts,menuMores;
    private MenuAdapter menuAdapter;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListener();
        initAccount();
        initMore();
        loadUserDetails();
    }


    private void loadUserDetails() {
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imgProfile.setImageBitmap(bitmap);
        binding.nameUser.setText(preferenceManager.getString(Constants.KEY_NAME));
    }
    private void initAccount(){
        menuAccounts  = new ArrayList<>();

        Menu menuA1 = new Menu(R.string.profile,R.drawable.ic_baseline_person_24);
        Menu menuA2 = new Menu(R.string.account_setting,R.drawable.ic_baseline_settings_24);

        menuAccounts.add(menuA1);
        menuAccounts.add(menuA2);

        menuAdapter =new MenuAdapter(menuAccounts, new MenuListener() {
            @Override
            public void onClickItemMenu(Menu menu) {
                showToast(R.string.error);

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewAccount.setLayoutManager(linearLayoutManager);
        binding.recyclerViewAccount.setAdapter(menuAdapter);

    }

    private void initMore(){
        menuMores  = new ArrayList<>();

        Menu menuM1 = new Menu(R.string.report_technical_problem,R.drawable.ic_baseline_bug_report_24);
        Menu menuM2 = new Menu(R.string.help,R.drawable.ic_baseline_help_24);
        Menu menuM3 = new Menu(R.string.legal_and_polices,R.drawable.ic_baseline_local_police_24);

        menuMores.add(menuM1);
        menuMores.add(menuM2);
        menuMores.add(menuM3);

        menuAdapter = new MenuAdapter(menuMores, new MenuListener() {
            @Override
            public void onClickItemMenu(Menu menu) {
                showToast(R.string.error);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewMore.setLayoutManager(linearLayoutManager);
        binding.recyclerViewMore.setAdapter(menuAdapter);
    }
    private void setListener() {
        binding.imageBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        });
        binding.logOut.setOnClickListener(v -> signOut());


    }

    private void showToast(int message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signOut() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> update = new HashMap<>();
        update.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(update)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    showToast(R.string.log_out_successfully);
                    finish();
                })
                .addOnFailureListener(e -> showToast(R.string.log_out_fail));
    }

}
