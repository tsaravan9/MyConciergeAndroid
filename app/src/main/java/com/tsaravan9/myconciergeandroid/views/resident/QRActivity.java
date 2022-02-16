package com.tsaravan9.myconciergeandroid.views.resident;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.tsaravan9.myconciergeandroid.databinding.ActivityBookAmenityBinding;
import com.tsaravan9.myconciergeandroid.databinding.ActivityQractivityBinding;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRActivity extends AppCompatActivity {

    private ImageView qrCodeIV;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    private ActivityQractivityBinding binding;
    private String info;
    private String amenity;
    private String date;
    private int time;
    private String timeslot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityQractivityBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        info = getIntent().getExtras().getString("INFO");

        amenity = getIntent().getExtras().getString("AMENITY_NAME");
        date = getIntent().getExtras().getString("AMENITY_DATE");
        time = getIntent().getExtras().getInt("AMENITY_TIME");

        qrCodeIV = this.binding.idIVQrcode;

        if (info.isEmpty()) {

            // if the edittext inputs are empty then execute
            // this method showing a toast message.
            Toast.makeText(QRActivity.this, "No Info", Toast.LENGTH_SHORT).show();
        } else {
            // below line is for getting
            // the windowmanager service.
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

            // initializing a variable for default display.
            Display display = manager.getDefaultDisplay();

            // creating a variable for point which
            // is to be displayed in QR Code.
            Point point = new Point();
            display.getSize(point);

            // getting width and
            // height of a point
            int width = point.x;
            int height = point.y;

            // generating dimension from width and height.
            int dimen = width < height ? width : height;
            dimen = dimen * 3 / 4;

            // setting this dimensions inside our qr code
            // encoder to generate our qr code.
            Log.d("qr", info);
            qrgEncoder = new QRGEncoder(info, null, QRGContents.Type.TEXT, dimen);
            try {
                // getting our qrcode in the form of bitmap.
                bitmap = qrgEncoder.encodeAsBitmap();
                // the bitmap is set inside our image
                // view using .setimagebitmap method.
                qrCodeIV.setImageBitmap(bitmap);
            } catch (WriterException e) {
                // this method is called for
                // exception handling.
                Log.e("Tag", e.toString());
            }
        }

        prepareTimeslot(time);
        this.binding.congratsText.setText("Congratulations! Your booking has been done for "+amenity+" on " +date+ " from "+timeslot);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void prepareTimeslot(int pos) {
        switch (pos) {
            case 0: {
                timeslot = "6:00 AM - 8:00 AM";
                break;
            }
            case 1: {
                timeslot = "8:00 AM - 10:00 AM";
                break;
            }
            case 2: {
                timeslot = "10:00 AM - 12:00 PM";
                break;
            }
            case 3: {
                timeslot = "12:00 PM - 2:00 PM";
                break;
            }
            case 4: {
                timeslot = "2:00 PM - 4:00 PM";
                break;
            }
            case 5: {
                timeslot = "4:00 PM - 6:00 PM";
                break;
            }
            case 6: {
                timeslot = "6:00 PM - 8:00 PM";
                break;
            }
            case 7: {
                timeslot = "8:00 PM - 10:00 PM";
                break;
            }
        }
    }
}