package team.digitalfairy.fileselectortest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> ar = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), r -> {
       if(r.getResultCode() == Activity.RESULT_OK) {
           ParcelFileDescriptor pfd;
           if(r.getData() != null) {
               // do nothing
               Log.i("Result", "OK " + r.getData());
               Uri ri = r.getData().getData();
               Log.i("Result", "ri "+ri);

               // Try opening file
               try {
                   pfd = getContentResolver().openFileDescriptor(ri,"r");
               } catch (FileNotFoundException e) {
                   throw new RuntimeException(e);
               }

               int fd = pfd.getFd();

               TestCpp.testOpenFile(fd);
           }
       }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            System.loadLibrary("fileselectortest");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt1 = findViewById(R.id.button);

        bt1.setOnClickListener(view -> {
            // Open file selector
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");

            ar.launch(i);

        });
    }
}