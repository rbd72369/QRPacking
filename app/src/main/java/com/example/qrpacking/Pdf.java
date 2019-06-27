package com.example.qrpacking;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Pdf {

    public final String TAG = "PDF";

    private List<QrCode> qrCodeList = new ArrayList<>();
    private String uri;

    public Pdf(){

    }

    public Pdf(List<QrCode> qrCodeList){
        this.qrCodeList = qrCodeList;
        uri = null;
        createPdf();

    }

    public Pdf(QrCode qrCode){
        qrCodeList.add(qrCode);
        createPdf();
    }

    public Pdf(String uri){
        this.uri = uri;

    }

    /**
     * creates the pdf
     */
    public void createPdf(){
        int size = qrCodeList.size();
        if(size==0){
            //qrCodeList
        }
        int numOfPages = (int) Math.ceil((double)size/4);
        Log.d(TAG, "list size: " + size);
        Log.d(TAG, "numofpages: " + numOfPages);

        boolean isRound = false;
        if(qrCodeList.size()%4==0) isRound = true;

        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = null;
        // start a page
        PdfDocument.Page page = null;
        Canvas canvas;
        Paint paint;
        Bitmap mainBitmap = null;
        String name = null;

        //creates certain number of pdf pages depending on how many qr codes are selected
        for (int i = 0; i < numOfPages; i++) {

            // crate a page description
            pageInfo = new PdfDocument.PageInfo.Builder(595, 842, i).create();
            // start a page
            page = document.startPage(pageInfo);
            canvas = page.getCanvas();
            paint = new Paint();

            //if adding qr codes to the last page and the last page has less than 4 qr codes
            //then size = the number of qr codes to be printed to the page
            //else size = the maximum number of qr codes that the page can hold
            if(numOfPages == i+1 && !isRound){
                size = qrCodeList.size() % 4;
            }
            else {
                size = 4;
            }
            //count helps to space the qr codes
            int count = 0;

            for (int j = 4*i; j < 4*i+size; j++) {
                QrCode qrCode = qrCodeList.get(j);
                Log.d(TAG, "name: " + qrCode.getName());
                mainBitmap = qrCode.getQrImage();
                name = qrCode.getName();

                //draws qrcode inside a rect
                Rect rect = new Rect(50,50 + (200 * count),200,200 + (200 * count));
                canvas.drawBitmap(mainBitmap,null,rect,null);
                paint.setColor(Color.BLACK);
                paint.setTextSize(20);
                int xPos = (int)(rect.width() - paint.getTextSize() * name.length() / 2) / 2;//TODO this is wack
                //draws the name
                canvas.drawText(name, xPos, 35 + (200*count), paint);

                Log.d(TAG, "j: " +j);
                count ++;
            }
            Log.d(TAG, "size: " + (4*i+size) );
            // finish the page
            document.finishPage(page);
        }

        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+ System.currentTimeMillis()+"_qrCodes"+".pdf";
        uri = targetPdf;
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
    /**
     * opens pdf
     * @param context the context
     */
    public void openPdf(Context context) {

        try {
            File file = new File(uri);
            Uri pdfUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            //use the flag FLAG_UPDATE_CURRENT to override any notification already there
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        }
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
