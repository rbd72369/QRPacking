package com.example.qrpacking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.encoder.QRCode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class QRImageActivity extends AddBoxActivity {

    String TAG = "GenerateQRCode";
    String name;
    QrCode qrCode;
    ImageView qrImage;
    Button printBtn, save, pdfBtn;
    Bitmap mainBitmap;
    QRGEncoder qrgEncoder;

    TextView nameTV;

    //TODO add progress bar for upload

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrimage);
        Bundle bundle = getIntent().getExtras();
        String uri = bundle.getString("uri");
        name = bundle.getString("name");
        final String underscoreName = name.replaceAll(" ", "_");

        qrImage = (ImageView) findViewById(R.id.QR_Image);
        save = (Button) findViewById(R.id.save);
        nameTV = findViewById((R.id.nameTV));
        printBtn = findViewById(R.id.printBtn);
        pdfBtn = findViewById(R.id.pdfBtn);


        //mainBitmap = makeQRCode(uri);
        qrCode = new QrCode(name,uri,this);
        mainBitmap = qrCode.getQrImage();
        nameTV.setText(name);
        qrImage.setImageBitmap(mainBitmap);
        /*
        if (inputValue.length() > 0) {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    inputValue, null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                qrImage.setImageBitmap(bitmap);

            } catch (WriterException e) {
                Log.v(TAG, e.toString());
            }
        } else {
            Toast.makeText(QRImageActivity.this,"WRONG",Toast.LENGTH_SHORT).show();
        }*/

        //saves qr code image to device
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean save;
                String result;

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(QRImageActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(QRImageActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.

                } else {
                    // Permission has already been granted
                }


                try {

                    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                    File myDir = new File(root + "/qrcodes");
                    if (!myDir.exists()) {
                        myDir.mkdir();
                    } else {
                        Log.v("QRGSaver", "Folder Exists");
                    }

                    String fname = System.currentTimeMillis() + "qr_" + underscoreName + ".jpg";
                    File file = new File(myDir, fname);
                    if (file.exists())
                        file.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        mainBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }


                    // Tell the media scanner about the new file so that it is
                    // immediately available to the user.
                    MediaScannerConnection.scanFile(QRImageActivity.this, new String[] { file.toString() }, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> uri=" + uri);
                                }
                            });

                    /*
                    //String path = Environment.getExternalStorageDirectory().toString();
                    FileOutputStream fOut = null;
                    File file = new File(savePath, underscoreName+".jpg");
                    //File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), name+".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                    if (!file.exists()) {
                        file.mkdir();
                    } else {
                        Log.v("QRGSaver", "Folder Exists");
                    }
                    fOut = new FileOutputStream(file);


                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    fOut.flush(); // Not really required
                    fOut.close(); // do not forget to close the stream

                    MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
                    Log.d(TAG,file.getName());*/


                    /*save = QRGSaver.save(savePath, name, bitmap, QRGContents.ImageType.IMAGE_JPEG);
                    result = save ? "Image Saved" : "Image Not Saved";
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //prints qrcode
        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintHelper photoPrinter = new PrintHelper(QRImageActivity.this);

                //Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*.1),(int)(bitmap.getHeight()*.1), true);
                photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                photoPrinter.printBitmap("qr code", mainBitmap);
            }
        });
        //creates pdf of qr code
        pdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createPdf(System.currentTimeMillis() + underscoreName);
                Pdf pdf = new Pdf(qrCode);
                pdf.openPdf(getApplicationContext());
            }
        });

    }
}
