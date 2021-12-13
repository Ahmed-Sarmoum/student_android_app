package com.ahmedsarmoum.qrcodetp.View;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedsarmoum.qrcodetp.Controller.DatabaseHandler;
import com.ahmedsarmoum.qrcodetp.Controller.PresonceAdapter;
import com.ahmedsarmoum.qrcodetp.MainActivity;
import com.ahmedsarmoum.qrcodetp.Model.Presence;
import com.ahmedsarmoum.qrcodetp.R;

public class PresonceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    static PresonceAdapter presenceAdapter;
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presonce);
        RecyclerView recyclerPresence= findViewById(R.id.recyclerPresence);
         ImageView btnQRCode = findViewById(R.id.imgQrcode);

        databaseHandler = new DatabaseHandler(this);
        presenceAdapter = new PresonceAdapter(this, databaseHandler.getAllPresence(), databaseHandler);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerPresence.setLayoutManager(layoutManager);
        recyclerPresence.setItemAnimator(new DefaultItemAnimator());
        recyclerPresence.setAdapter(presenceAdapter);

        btnQRCode.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ScannerQRCodeActivity.class));
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PresonceActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    public static void notifyAdapter() {
        presenceAdapter.notifyDataSetChanged();
    }
}
