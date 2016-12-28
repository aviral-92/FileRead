package com.example.amittal.fileread;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnClickMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnClickMe = (Button) findViewById(R.id.button);
        btnClickMe.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        getFile();
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

    public void onActivityResult(int requestCode, int resultCode, Intent result) {

        String response = null;
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri data = result.getData();

                Toast.makeText(this, data.getPath(), Toast.LENGTH_SHORT).show();
                File downloadDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                getFilesFromDir(downloadDir);

                if(data.getLastPathSegment().endsWith("xls")){
                    String dbStr = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/myExcel.xls";
                    ExcelDataRead.readExcelFile(MainActivity.this, dbStr);

                    Toast.makeText(this, "valid file type", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Invalid file type", Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(this, "No result found", Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText(this, "No result found == -1", Toast.LENGTH_SHORT).show();
        }
    }
    public void getFilesFromDir(File filesFromSD) {

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
    }
}
