package com.example.lab8;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button makeCadr = findViewById(R.id.button);
        makeCadr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                            Manifest.permission.CAMERA
                    },100);
                }else {
                    ScanOptions options = new ScanOptions();
                    options.setPrompt("Скан кода");
                    options.setBeepEnabled(true);
                    options.setOrientationLocked(true);
                    options.setCaptureActivity(CaptureAct.class);

                    barLauncher.launch(options);
                }
            }
        });
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->{
        if (result.getContents()!=null){
            if (result.getContents().equalsIgnoreCase("дом")){
                Intent intent = new Intent(this, HouseActivity.class);
                startActivity(intent);
            } else if (result.getContents().equalsIgnoreCase("человек")) {
                Intent intent = new Intent(this, HumanActivity.class);
                startActivity(intent);
            } else if (result.getContents().equalsIgnoreCase("телефон")){
                Intent intent = new Intent(this, PhoneActivity.class);
                startActivity(intent);
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Результат");
                builder.setMessage(result.getContents());
                builder.show();
            }
        }
    });
}