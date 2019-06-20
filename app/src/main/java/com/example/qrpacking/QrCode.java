package com.example.qrpacking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


import static android.support.constraint.Constraints.TAG;

public class QrCode {


    private String name;
    private String uri;
    Bitmap qrImage;
    Context context;
    public QrCode(){

    }

    public QrCode(Context context){
        this.context = context;
    }

    public QrCode(String name, String uri){
        this.name = name;
        this.uri = uri;
        qrImage = makeQRCode(uri);


    }

    public QrCode(String name, String uri, Bitmap qrImage){
        this.name = name;
        this.uri = uri;
        this.qrImage=qrImage;
    }

    public QrCode(String name, String uri, Context context){
        this.name = name;
        this.uri = uri;
        this.context = context;
        qrImage = makeQRCode(uri);
    }

    public Bitmap makeQRCode(String uri){
        Bitmap bitmap = null;
        QRGEncoder qrgEncoder;
        if (uri.length() > 0) {
            WindowManager manager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    uri, null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();

                //qrImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                Log.v(TAG, e.toString());

            }
        } else {
            // Toast.makeText(QRImageActivity.this,"WRONG",Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }

    /*public QrCo
    de makeQRCode(String uri){
        QrCode qrCode = null;
        QRGEncoder qrgEncoder;
        if (uri.length() > 0) {
            WindowManager manager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    uri, null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            try {
                Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                qrCode = new QrCode(name,bitmap);

                //qrImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                Log.v(TAG, e.toString());

            }
        } else {
           // Toast.makeText(QRImageActivity.this,"WRONG",Toast.LENGTH_SHORT).show();
        }
        return qrCode;
    }
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getQrImage() {
        return qrImage;
    }

    public void setQrImage(Bitmap qrImage) {
        this.qrImage = qrImage;
    }
}
