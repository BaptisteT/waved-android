package com.waved.streetshout.waved.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.waved.streetshout.waved.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import utils.Constants;
import utils.ImageUtils;

public class ProfilePictureActivity extends Activity {

    private ImageView profilePictureImage = null;
    private Button profilePictureButton = null;
    private Bitmap profilePicture = null;
    private String imageName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_picture);

        profilePictureImage = (ImageView) findViewById(R.id.profile_picture_image);
        profilePictureButton = (Button) findViewById(R.id.profile_picture_profile_button);

        profilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {getString(R.string.profile_picture_camera_option),
                                                getString(R.string.profile_picture_library_option)};

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfilePictureActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals(getString(R.string.profile_picture_camera_option))) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                                                                                .format(new Date());
                            imageName = "waved_" + timeStamp + ".jpg";
                            File f = new File(android.os.Environment.getExternalStorageDirectory(),
                                                                                         imageName);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, Constants.PROFILE_PIC_CAMERA_REQ);
                        } else if (items[item].equals(getString(R.string.profile_picture_library_option))) {
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                     android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent,
                                           getString(R.string.profile_picture_library_select_from)),
                                                                    Constants.PROFILE_PIC_FILE_REQ);
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.PROFILE_PIC_CAMERA_REQ) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals(imageName)) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);

                    bm = ImageUtils.makeThumb(bm);

                    profilePicture = ImageUtils.makeThumb(bm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == Constants.PROFILE_PIC_FILE_REQ) {
                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, ProfilePictureActivity.this);
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);

                profilePicture = ImageUtils.makeThumb(bm);
            }
        }
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
