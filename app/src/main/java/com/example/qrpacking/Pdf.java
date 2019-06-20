package com.example.qrpacking;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Pdf {

    private List<QrCode> qrCodeList;

    public Pdf(){

    }

    public Pdf(List<QrCode> qrCodeList){
        this.qrCodeList = qrCodeList;

    }

/*
    public void createPdf(String sometext){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
/*
        Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(0, 0, 0));
        myPaint.setStrokeWidth(10);
        canvas.drawRect(100, 100, 200, 200, myPaint);
        */
/*
        Rect rect = new Rect(50,50,200,200);
        canvas.drawBitmap(mainBitmap,null,rect,null);
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        int xPos = (int)(rect.width() - paint.getTextSize() * name.length() / 2) / 2;
        canvas.drawText(name, xPos, 35, paint);

        // finish the page
        document.finishPage(page);

        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+ sometext+"_qr"+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }*/


    public void createPdf(){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();


        Bitmap mainBitmap = null;
        String name = null;
        for (int i = 0; i < qrCodeList.size() ; i++) {
            QrCode qrCode = qrCodeList.get(i);
            mainBitmap = qrCode.getQrImage();
            name = qrCode.getName();

            Rect rect = new Rect(50,50 + (200 * i),200,200 + (200 * i));
            canvas.drawBitmap(mainBitmap,null,rect,null);
            paint.setColor(Color.BLACK);
            paint.setTextSize(20);
            int xPos = (int)(rect.width() - paint.getTextSize() * name.length() / 2) / 2;
            canvas.drawText(name, xPos, 35 + (200*i), paint);
        }
/*
        Rect rect = new Rect(50,50,200,200);
        canvas.drawBitmap(mainBitmap,null,rect,null);
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        int xPos = (int)(rect.width() - paint.getTextSize() * name.length() / 2) / 2;
        canvas.drawText(name, xPos, 35, paint);
*/

        // finish the page
        document.finishPage(page);

        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+ name+"_qr"+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            //Toast.makeText(UploadsRecyclerAdapter.this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            //Toast.makeText(UploadsActivity.this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }

}
