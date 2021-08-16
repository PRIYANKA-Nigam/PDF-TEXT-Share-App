package com.example.pdfandtextshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private EditText e;
    String strFile= Environment.getExternalStorageDirectory().getPath()+ File.separator+"myPDf.pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e=(EditText)findViewById(R.id.editTextTextPersonName);
        ActivityCompat.requestPermissions(this,new String[]{READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();StrictMode.setVmPolicy(builder.build());
    }

    public void pdfShare(View view) {
        File file=new File(strFile);if(!file.exists()){
            Toast.makeText(this,"File doesn't exists",Toast.LENGTH_SHORT).show();return;
        }
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));
        startActivity(intent.createChooser(intent,"Sharing the PDF ......"));
    }

    public void textShare(View view) {
        Intent intent=new Intent(Intent.ACTION_SEND);
          intent.setType("text/plain");
          intent.putExtra(Intent.EXTRA_SUBJECT,"My Subject here ....");
          intent.putExtra(Intent.EXTRA_TEXT,"My text of message goes here....");
          startActivity(Intent.createChooser(intent,"Sharing the file ...."));
    }

    public void CreatePdfs(View view) {
        PdfDocument pdfDocument=new PdfDocument();
        PdfDocument.PageInfo myPageInfo=new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage=pdfDocument.startPage(myPageInfo);
        Paint paint=new Paint();
        String mystring=e.getText().toString();int x=10,y=25;
        myPage.getCanvas().drawText(mystring,x,y,paint);
        pdfDocument.finishPage(myPage);
        String myFilePath=Environment.getExternalStorageDirectory().getPath()+ "/myPDf.pdf";
        File file=new File(myFilePath);
        try{
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            e.setText("ERROR !!!");
        }
        pdfDocument.close();
    }
}