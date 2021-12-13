package com.ahmedsarmoum.qrcodetp.View;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedsarmoum.qrcodetp.Controller.DatabaseHandler;
import com.ahmedsarmoum.qrcodetp.Controller.ExportAdapter;
import com.ahmedsarmoum.qrcodetp.MainActivity;
import com.ahmedsarmoum.qrcodetp.R;
import com.ahmedsarmoum.qrcodetp.Utils.Utils;
import com.ajts.androidmads.library.SQLiteToExcel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ExportActivity extends AppCompatActivity {
    private static final int STORAGE_REQUEST_CODE_EXPORT = 1388473134;
    String[] storagePermissions;
    ActivityResultLauncher<Intent> activityResultLauncher;
    DatabaseHandler databaseHandler;
    RecyclerView recyclerListFolder;
    List<String> listFolders;
    static ExportAdapter exportAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        FloatingActionButton fabExport = findViewById(R.id.fabExport);
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        databaseHandler = new DatabaseHandler(this);
        databaseHandler = new DatabaseHandler(this);
        recyclerListFolder = findViewById(R.id.recyclerListFolder);
        listFolders = new ArrayList<>();

        if (checkStoragePermission() ) {
            File root = new File(Environment.getExternalStorageDirectory()+"/Student Data/");
            if (root.exists()){
                listDir(root);
            }
        }


        fabExport.setOnClickListener(view -> {
            View dialog = LayoutInflater.from(this).inflate(R.layout.logout_dialog, null, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(dialog);

            TextView oui = dialog.findViewById(R.id.oui);

            oui.setText(getString(R.string.save));

            AlertDialog alertDialog = builder.create();
            if (alertDialog.getWindow() != null)
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
            alertDialog.show();
            oui.setOnClickListener(view1 -> {
                if (checkStoragePermission()) {
                    exportExcel(this);
                    alertDialog.dismiss();
                } else {
                    requestStoragePermissionExport();
                }
            });
            dialog.findViewById(R.id.non).setOnClickListener(view1 ->  alertDialog.dismiss() );
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private void exportExcel(ExportActivity exportActivity) {

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = dateFormat.format(now);

        File f1 = new File(Environment.getExternalStorageDirectory()+ "/Student Data/");
        File folder = new File(Environment.getExternalStorageDirectory()+ "/Student Data/"+time);

        boolean isFolderCreated1 = false;
        boolean isFolderCreated2 = false;
        boolean isFolderCreated = false;
        if (!f1.exists()) {
            isFolderCreated1 = f1.mkdir(); // create folder if not exists
        }
        if (!folder.exists()) {
            isFolderCreated = folder.mkdir(); // create folder if not exists
        }

        String path = Environment.getExternalStorageDirectory()+ "/Student Data/"+time;

        if (databaseHandler.getAllStudent().size() == 0) {
            Toast.makeText(this, "Table student is empty", Toast.LENGTH_LONG).show();
        } else {
            SQLiteToExcel sqliteToExcel = new SQLiteToExcel(this, Utils.DATABASE_NAME, path);
            sqliteToExcel.exportSingleTable(Utils.STUDENT_TABLE, Utils.STUDENT_TABLE+".xls", new SQLiteToExcel.ExportListener() {
                @Override
                public void onStart() {
                }
                @Override
                public void onCompleted(String filePath) {
                    Toast.makeText(exportActivity, "export students table is complete with success", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onError(Exception e) {
                    Toast.makeText(exportActivity, "you have problem :(", Toast.LENGTH_LONG).show();
                }
            });
        }

        if (databaseHandler.getAllPresence().size() == 0) {
            Toast.makeText(this, "Table presences is empty", Toast.LENGTH_LONG).show();
        } else {
            SQLiteToExcel sqliteToExcel = new SQLiteToExcel(this, Utils.DATABASE_NAME, path);
            sqliteToExcel.exportSingleTable(Utils.PRESENCE_TABLE, Utils.PRESENCE_TABLE+".xls", new SQLiteToExcel.ExportListener() {
                @Override
                public void onStart() {
                }
                @Override
                public void onCompleted(String filePath) {
                    Toast.makeText(exportActivity, "export table presences is complete with success", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onError(Exception e) {
                    Toast.makeText(exportActivity, "you have problem :(", Toast.LENGTH_LONG).show();
                }
            });
        }

        if (databaseHandler.getAllNote().size() == 0) {
            Toast.makeText(this, "Table notes is empty", Toast.LENGTH_LONG).show();
        } else {
            SQLiteToExcel sqliteToExcel = new SQLiteToExcel(this, Utils.DATABASE_NAME, path);
            sqliteToExcel.exportSingleTable(Utils.NOTE_TABLE, Utils.NOTE_TABLE+".xls", new SQLiteToExcel.ExportListener() {
                @Override
                public void onStart() {
                }
                @Override
                public void onCompleted(String filePath) {
                    Toast.makeText(exportActivity, "export notes table is complete with success", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onError(Exception e) {
                    Toast.makeText(exportActivity, "you have problem :(", Toast.LENGTH_LONG).show();
                }
            });
        }

        if (checkStoragePermission() ) {
            File root = new File(Environment.getExternalStorageDirectory()+"/Student Data/");
            if (root.exists()){
                listDir(root);
            }
        }

    }


    private boolean checkStoragePermission() {
        boolean result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            result = Environment.isExternalStorageManager();
        } else {
             result = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        }
        return  result;

    }


    private void requestStoragePermissionExport() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageName()})));
                activityResultLauncher.launch(intent);
            } catch (Exception e) {
                Log.e("activityResultLauncher", e.getMessage());
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activityResultLauncher.launch(intent);
            }
        } else {
            ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE_EXPORT);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case STORAGE_REQUEST_CODE_EXPORT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                    exportExcel(this);
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
            }

        }


    }

    private void listDir(File root) {
        File[] files = root.listFiles();
        listFolders.clear();
        for (File file:files) {
            Log.d("File is", file.getName());
            listFolders.add(file.getName());
        }

        exportAdapter = new ExportAdapter(this, listFolders,databaseHandler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplication());
        recyclerListFolder.setLayoutManager(layoutManager);
        recyclerListFolder.setItemAnimator(new DefaultItemAnimator());
        recyclerListFolder.setAdapter(exportAdapter );
    }

    public static void notifyAdapter() {
        exportAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ExportActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
