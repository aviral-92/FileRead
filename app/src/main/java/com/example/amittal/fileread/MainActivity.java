package com.example.amittal.fileread;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnClickMe;
    private int WRITE_CONTACT_PERMISSION_CODE = 1;
    TextView excelFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnClickMe = (Button) findViewById(R.id.button);
        btnClickMe.setOnClickListener(MainActivity.this);
        excelFile = (TextView) findViewById(R.id.excelName);
    }

    @Override
    public void onClick(View v) {

        if (isWriteContactAllowed() && isExternalStorageAllowed()) {
            Toast.makeText(MainActivity.this, "You already have the permission", Toast.LENGTH_LONG).show();
            Log.d("permission","You already have the permission");
            //getFile();
            test();
        } else {
            requestWriteContactPermission();
            requestExternalStoragePermission();
            Toast.makeText(MainActivity.this, "Giving You Permission", Toast.LENGTH_LONG).show();
            Log.d("permission","Giving you permission");
            //getFile();
            test();
        }
        /*try{
            ExcelDataRead.read("/storage/emulated/0/Download/myExcel.xls");
        }catch(Exception ex){
            ex.printStackTrace();
        }*/
        //test();
    }

    private boolean isExternalStorageExist() {

        if (Environment.isExternalStorageRemovable()) {
            String state = Environment.getExternalStorageState();
            return state.equals(Environment.MEDIA_MOUNTED) || state.equals(
                    Environment.MEDIA_MOUNTED_READ_ONLY);
        }
        //Boolean isSDPresent =android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        return false;
    }

    public void getFile() {

        // if (isExternalStorageExist()) {
        Intent chooser = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getDownloadCacheDirectory().getPath().toString());
        chooser.addCategory(Intent.CATEGORY_OPENABLE);
        chooser.setDataAndType(uri, "*/*");
        // startActivity(chooser);
        try {
            startActivityForResult(chooser, 1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }

        //}else{
        //Toast.makeText(this, dbStr,Toast.LENGTH_SHORT).show();
        //}/data/local/tmp/com.example.amittal.fileread
    }

    /*public void test(){

        Intent intent = new Intent();
        intent.setType("video*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), 1);
    }*/

    /*public void onActivityResult(int requestCode, int resultCode, Intent result) {

        String response = null;
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri data = result.getData();

                Toast.makeText(this, data.getPath(), Toast.LENGTH_SHORT).show();
                File downloadDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                //getFilesFromDir(downloadDir);

                if (data.getLastPathSegment().endsWith("xls")) {
                    String dbStr = Environment.getExternalStorageDirectory().getAbsolutePath() + "/contact.xls";
                    ExcelDataRead excelDataRead = new ExcelDataRead();
                    excelDataRead.readExcelFile(MainActivity.this, dbStr);

                    Toast.makeText(this, "valid file type", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Invalid file type", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No result found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No result found == -1", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void requestWriteContactPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, WRITE_CONTACT_PERMISSION_CODE);
    }

    private boolean isWriteContactAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        //If permission is not granted returning false
        return false;
    }


    private void requestExternalStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, WRITE_CONTACT_PERMISSION_CODE);
    }

    private boolean isExternalStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        //If permission is not granted returning false
        return false;
    }

    /*public void getFilesFromDir(File filesFromSD) {

        File listAllFiles[] = filesFromSD.listFiles();

        if (listAllFiles != null && listAllFiles.length > 0) {
            for (File currentFile : listAllFiles) {
                if (currentFile.isDirectory()) {
                    getFilesFromDir(currentFile);
                } else {
                    if (currentFile.getName().endsWith("")) {
                        // File absolute path
                        Log.e("File path", currentFile.getAbsolutePath());
                        // File Name
                        Log.e("File path", currentFile.getName());

                    }
                }
            }
        }
    }*/

    public void test() {

        ArrayList<String> FOLDERS = new ArrayList<String>();
        ArrayList<String> XLS_FILES = new ArrayList<String>();

        int CALL_COUNT = 0;
        String path = Environment.getExternalStorageDirectory().getPath() + "/";
        Log.d("Envn Path : ",path);
        try {

            File dirFolder = new File(path);
            File[] folders = dirFolder.listFiles();

            for (File file : folders) {
                if (file.isDirectory()) {
                    FOLDERS.add(path + file.getName() + "/");
                } else {

                    if (file.getName().contains(excelFile.getText() + ".xls")) {

                        XLS_FILES.add(path + file.getName());


                    } else {
                        // FILE_PATH = path + file.getName();
                        // Log.e("File Deatils", "" + FILE_PATH);
                    }
                }
            }
            Log.d("1", XLS_FILES.get(0));
            ExcelDataRead excelDataRead = new ExcelDataRead();
            excelDataRead.readExcelFile(this, XLS_FILES.get(0));

        } catch (Exception e) {
            e.printStackTrace();
        }

            /*while (CALL_COUNT < FOLDERS.size()) {
                String dirURL = FOLDERS.get(CALL_COUNT);
                CALL_COUNT++;

                // Log.e("Folders", "" + dirURL);
                getSdCardFolders(dirURL);
            }*/

    }
}
