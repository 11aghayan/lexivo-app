package com.lexivo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;

public abstract class BitmapUtil {
    public static Bitmap getBitmapFromVectorDrawable(Context context, int flagId){
        Drawable drawable = ContextCompat.getDrawable(context, flagId);
        assert drawable != null;
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static String getBitmapStringFromDrawableId(Context context, int drawableId) {
        return getStringFromBitmap(getBitmapFromVectorDrawable(context, drawableId));
    }

    public static String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static Drawable getDrawableFromBitmapString(Context context, String bitmapString) {
        Bitmap bitmap = getBitmapFromString(bitmapString);
        return new BitmapDrawable(context.getResources(), bitmap);
    }
}
