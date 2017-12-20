package vn.manroid.backupcontact;

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

public class MainActivity2 extends BaseActivity implements View.OnClickListener {

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
        permission();
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
                Log.d("TAG", "Contact " + (i + 1) + "VcF String is" + vCard.get(i));
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

    private void email() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, "content");
        email.putExtra(Intent.EXTRA_SUBJECT, "abc");
        email.putExtra(Intent.EXTRA_TEXT, "text");
        //need this to prompts email client only
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    @Override
    public void onClick(View view) {
        progressStatus = 0;
        getVcardString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus <= 100) {
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 300:
                String[] permissionRequestForCamera = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                List<String> listPermissionCameraRequire = checkPermissionMiss(permissionRequestForCamera);
//                if (listPermissionCameraRequire.isEmpty())
//                    onLaunchCamera();
//                else
//                    savePermissionAlwayDenied(permissionRequestForCamera);
                break;
            case 200:
                String[] permissionRequestForPhoto = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                List<String> listPermissionAlbumRequire = checkPermissionMiss(permissionRequestForPhoto);
//                if (listPermissionAlbumRequire.isEmpty())
//                    onLaunchAlbum();
//                else
//                    savePermissionAlwayDenied(permissionRequestForPhoto);
                break;
        }
    }

    private void test() {
        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), vfile);
        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        String to[] = {"asd@gmail.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void permission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> listPermissionNeedRequire = checkPermissionMiss(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
            if (!listPermissionNeedRequire.isEmpty()) {
//                                                 Check permission alway denied
                if (checkPermissionAlwayDenied(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                    handleUserDeninePermission();
                    return;
                }
                String[] listPermissionRequire = new String[listPermissionNeedRequire.size()];
                listPermissionRequire = listPermissionNeedRequire.toArray(listPermissionRequire);
                requestPermission(PERMISSION_REQUEST_WRITE_MEMORY, listPermissionRequire);
                return;
            }
        }
        // Check permission alway denied
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> listPermissionNeedRequire = checkPermissionMiss(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            if (!listPermissionNeedRequire.isEmpty()) {
                // Check permission alway denied
                if (checkPermissionAlwayDenied(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})) {
                    handleUserDeninePermission();
                    return;
                }
                String[] listPermissionRequire = new String[listPermissionNeedRequire.size()];
                listPermissionRequire = listPermissionNeedRequire.toArray(listPermissionRequire);
                requestPermission(PERMISSION_REQUEST_READ_MEMORY, listPermissionRequire);
                return;
            }
        }
    }

    public void handleUserDeninePermission() {
        showAlertDialog(null, "Quyền sử dụng ứng dụng đã bị cấm, bạn phải cấp quyền cho ứng dụng!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }, "Ok", null, null);
    }

    public List<String> checkPermissionMiss(String... listPermission) {
        List<String> result = new ArrayList<>();
        for (String permission : listPermission) {
            if (ContextCompat.checkSelfPermission(this,
                    permission)
                    != PackageManager.PERMISSION_GRANTED) {
                result.add(permission);
            }
        }
        return result;
    }
}
