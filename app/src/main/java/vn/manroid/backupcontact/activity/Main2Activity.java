package vn.manroid.backupcontact.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.property.StructuredName;
import vn.manroid.backupcontact.R;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private Cursor cursor;
    private ArrayList<String> vCard;
    private String vfile;
    private Button btnBackup;
    private ProgressBar prg;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private TextView txtShowProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        btnBackup.setOnClickListener(this);
        vfile = "Contacts" + "_" + System.currentTimeMillis() + ".vcf";
    }

    private void initView() {
        btnBackup = (Button) findViewById(R.id.btn_submit);
        txtShowProgress = (TextView) findViewById(R.id.txt_prg);
        prg = (ProgressBar) findViewById(R.id.progress);
    }


    private void getVcardString() {
        // TODO Auto-generated method stub
        vCard = new ArrayList<String>();
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                get(cursor);
//                Log.d("TAG", "Contact " + (i + 1) + "VcF String is" + vCard.get(i));
                cursor.moveToNext();
            }
        } else {
            Log.d("TAG", "No Contacts in Your Phone");
        }
    }

    public void get(Cursor cursor) {
        //cursor.moveToFirst();
        String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
        AssetFileDescriptor fd;
        try {
            fd = this.getContentResolver().openAssetFileDescriptor(uri, "r");
            // Your Complex Code and you used function without loop so how can you get all Contacts Vcard.??
            FileInputStream fis = fd.createInputStream();
            byte[] buf = new byte[(int) fd.getDeclaredLength()];
            fis.read(buf);
            String vcardstring = new String(buf);
            vCard.add(vcardstring);
            String storage_path = Environment.getExternalStorageDirectory().toString() + File.separator + vfile;
            FileOutputStream mFileOutputStream = new FileOutputStream(storage_path, false);
            mFileOutputStream.write(vcardstring.toString().getBytes());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        progressStatus = 0;
        getVcardString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            prg.setProgress(progressStatus);
//                             Show the progress on TextView
                            txtShowProgress.setText(progressStatus + " % ");
                            if (progressStatus == 99) {
                                prg.setProgress(0);
                                txtShowProgress.setText("0%");
                                test();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    private void test() {
        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), vfile);
        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        String to[] = {"email@gmail.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
