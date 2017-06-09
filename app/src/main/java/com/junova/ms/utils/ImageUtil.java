package com.junova.ms.utils;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.junova.ms.app.App;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;

/**
 * Created by junova on 2016/9/30 0030.
 */

public class ImageUtil {
    public static byte[] getSmallBitmap(String filePath) {
        Bitmap photo = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        options.inSampleSize = calculateInSampleSize(options, 300, 300);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap map = BitmapFactory.decodeByteArray(data, 0, data.length,
                options);
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.JPEG, 100, stream1);

        return stream1.toByteArray();
    }

    // 计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    public static String getImagePath(Uri uri){
        String filename="";
        if (uri.getScheme().toString().compareTo("content") == 0) {
          Cursor cursor = App.getContext().getContentResolver().query(uri, new String[] {MediaStore.Audio.Media.DATA}, null, null, null);
            if (cursor.moveToFirst()) {
                filename = cursor.getString(0);
            }
            cursor.close();
        }else if (uri.getScheme().toString().compareTo("file") == 0){
            filename = uri.toString();
            filename = uri.toString().replace("file://", "");
            //替换file://
            if(!filename.startsWith("/mnt")){
                //加上"/mnt"头
             //   filename = "/mnt"+filename;
                Logger.d(filename);
            }
        }
        return filename;
    }
}
