package com.pets.app.utilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.pets.app.R;
import com.pets.app.initialsetup.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by BAJIRAO on 10 November 2017.
 */
@SuppressLint("Registered")
public class ImagePicker extends BaseActivity {

    public static final int RC_CROP_ACTIVITY = com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = ImagePicker.class.getName();
    private static final String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_ALL = 500;
    protected File updatedImageFile;
    protected Uri mImageUri;
    protected int imageFlag = 0;

    // My Generic Check Permission Method
    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void showTakeImagePopup() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            final CharSequence[] items = {getString(R.string.select_image_from_gallery), getString(R.string.open_camera), getString(R.string.cancel)};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.select_photo));
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {

                    switch (item) {
                        case 0:
                            openGallery();
                            break;
                        case 1:
                            openPhoneCamera();
                            break;
                        case 3:
                            break;
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openPhoneCamera() {
        String storageState = Environment.getExternalStorageState();
        try {
            File mediaDir;
            if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                mediaDir = new File(String.valueOf(Environment.getExternalStorageDirectory()));
            } else {
                mediaDir = new File(getFilesDir(), "SalamZindagi");
            }
            if (!mediaDir.exists()) {
                mediaDir.mkdirs();
            }
            updatedImageFile = new File(mediaDir, new Date().getTime() + ".jpg");
            if (!updatedImageFile.exists()) {
                updatedImageFile.createNewFile();
            }
        } catch (IOException e) {
            Log.d(TAG, "Could not create file: " + e);
        }

        if (updatedImageFile != null)
            mImageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".utils.provider", updatedImageFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        //GRANT URI PERMISSIONS TO ALL APPS , ELSE CAMERA CRASHES WHILE SAVING PHOTO.
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, mImageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isPermissionGranted = true;
        switch (requestCode) {
            case PERMISSION_ALL:
                for (int i : grantResults) {
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        isPermissionGranted = false;
                        break;
                    }
                }
                break;
        }
        if (isPermissionGranted) {
            showTakeImagePopup();
        } else {
            Toast.makeText(this, getString(R.string.camera_permission), Toast.LENGTH_SHORT).show();
        }
    }

    private void startCroppingActivity(Uri mImageUri) {
        // start picker to get image for cropping and then use the image in cropping activity
        com.theartofdev.edmodo.cropper.CropImage.activity(mImageUri)
                .setMultiTouchEnabled(true)
                .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    startCroppingActivity(mImageUri);
                    break;
                case PICK_IMAGE_REQUEST:
                    if (data != null) {
                        Uri uri = data.getData();
                        startCroppingActivity(uri);
                    }
                    break;
               /* case RC_CROP_ACTIVITY:
                    if (data != null) {
                        CropImage.ActivityResult result = CropImage.getActivityResult(data);
                        String imgPath = result.getUri().getPath();
                        updatedImageFile = new File(imgPath);
                        if (mImageProfile != null && updatedImageFile.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                            mImageProfile.setImageBitmap(bitmap);
                        }
                    }
                    break;*/
            }
        }
    }
}