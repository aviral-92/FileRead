package com.example.amittal.fileread;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by amittal on 12/28/2016.
 */

public class ExcelDataRead {

    static String TAG = "ExelLog";
    private List<String> name = new ArrayList<>();
    private List<String> mobile = new ArrayList<>();
    private Map<String,String> contacts = new HashMap<>();

    public void readExcelFile(Context context, String filename) {
        try {
            // Creating Input Stream
            Log.d(TAG,filename);
            File file = new File(filename);
            //getFilesFromDir(file);
            FileInputStream myInput = new FileInputStream(file);
            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            /** We now need something to iterate through the cells.**/
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                iterating(cellIter,context);
            }
            for (Map.Entry<String, String> entry : contacts.entrySet()){
                Log.d(entry.getKey(),entry.getValue());
                insertContact(context.getContentResolver(),entry.getValue(),entry.getKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }

    private void iterating(Iterator cellIterator, Context context){

        int i=1;
        FileReadPojo fileReadPojo = new FileReadPojo();
        while (cellIterator.hasNext()){
            HSSFCell myCell = (HSSFCell) cellIterator.next();
            Log.d(TAG, "Cell Value: " + myCell.toString());
            Toast.makeText(context, "cell Value: " + myCell.toString(), Toast.LENGTH_SHORT).show();
            if(i%2!=0){
                fileReadPojo.setName(myCell.toString());
            }else{
                fileReadPojo.setMobile(myCell.toString());
                contacts.put(fileReadPojo.getMobile(),fileReadPojo.getName());
            }
            i++;
        }
    }

    public boolean insertContact(ContentResolver contactAdder, String firstName, String mobileNumber) {

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstName).build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber).withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
        try {
            contactAdder.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            MainActivity activity = new MainActivity();
            Toast.makeText(activity, "In Catch Block", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
