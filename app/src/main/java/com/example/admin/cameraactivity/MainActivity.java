package com.example.admin.cameraactivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        iv = ((ImageView) findViewById(R.id.imageView));
    }

    public void takePicture(View view) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.admin.cameraactivity",
                        photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(i, 1);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            iv.setImageBitmap(imageBitmap);
        }

    }

    String path;

    private File createImageFile() throws IOException{
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.d("storageDir", "-------->" + storageDir);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        path = "file:" + image.getAbsolutePath();
        return image;
    }

    private void storeImage(Bitmap image){

        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", ContextWrapper.MODE_PRIVATE);
        File myPath = new File(directory, "MI_"+ timeStamp +".jpg");
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(myPath);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        }

    }


}
