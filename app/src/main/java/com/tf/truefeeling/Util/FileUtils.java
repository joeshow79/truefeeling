package com.tf.truefeeling.util;

import com.tf.truefeeling.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FileUtils {

    private static final String FOLDER_APP = "/Sioeye";
    private static final String FOLDER_AVATAR = "/avatar/";
    private static Random random = new Random(SystemClock.uptimeMillis());

    private static String sdCardPath = Environment.getExternalStorageDirectory().getPath();
    // private static String AVATAR_PATH = sdCardPath + "/Sioeye/images/";

    public static String getAvatarDirectory() {
        if (getStorageDirectory() != null) {
            String directory = getStorageDirectory() + FOLDER_AVATAR;
            File folder = new File(directory);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            return directory;
        }
        return null;
    }

    private static String getStorageDirectory() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? sdCardPath + FOLDER_APP : null;
    }

    public static boolean saveImage(byte[] b, String filePath) {
        if (b == null || b.length == 0 || TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        BufferedOutputStream bos = null;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(b);
            return true;
        } catch (Exception e) {

        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return false;
    }

    public static Drawable bitmapToDrawable(Bitmap originalBitmap, Context context) {
        if (null == originalBitmap) {
            return null;
        }
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();
        int newWidth = context.getResources().getDimensionPixelOffset(R.dimen.main_activity_user_icon_width);
        int newHeight = context.getResources().getDimensionPixelOffset(R.dimen.main_activity_user_icon_height);

        float scaleX = ((float) newHeight) / originalHeight;
        float scaleY =    ((float) newWidth) / originalWidth;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);

        Bitmap changedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0,
                originalWidth, originalHeight, matrix, true);
        return new BitmapDrawable(context.getResources(),changedBitmap);
    }

    public static String getUniqueFileName(String filename, String extension) {
        String fullFilename = filename + extension;
        if (!new File(fullFilename).exists()) {
            return fullFilename;
        }

        filename += "_";
        int sequence = 1;
        for (int magnitude = 1; magnitude < 1000000000; magnitude *= 10) {
            for (int iteration = 0; iteration < 9; ++iteration) {
                fullFilename = filename + sequence + extension;
                if (!new File(fullFilename).exists()) {
                    return fullFilename;
                }
                sequence += random.nextInt(magnitude) + 1;
            }
        }

        return fullFilename;
    }

    public static String genPhotoName() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String photoName = myFormat.format(new Date());
        //photoName = photoName + ".jpg";
        return photoName;
    }

    public static void saveBitmapToLocal(Bitmap mBitmap, String absolutePath) {
        File file = new File(absolutePath);
        FileOutputStream fOut = null;
        try {
            file.createNewFile();
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteLocalFile(String absolutePath) {
        if (TextUtils.isEmpty(absolutePath))
            return;
        File file = new File(absolutePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public static boolean moveFileTo(String original, String dest){
        boolean result  = false;
        if(TextUtils.isEmpty(original) || TextUtils.isEmpty(dest)){
            return result;
        }

        File orgFile  = new File(original);
        if(!orgFile.exists()){
            return  false;
        }
        return  orgFile.renameTo(new File(dest));
    }

}
