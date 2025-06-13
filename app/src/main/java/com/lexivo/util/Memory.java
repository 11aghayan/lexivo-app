package com.lexivo.util;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lexivo.schema.Dictionary;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class Memory {
    private final static String FILENAME = "dictionaries.json";
    private static final int CREATE_FILE = 1;
    private static final int OPEN_FILE = 2;

    private static final Gson gson = new Gson();

    public static boolean saveData(Context context) {
        if (!isExternalStorageWritable())
            return false;

        try {
            FileOutputStream outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

            String dataJson = gson.toJson(Dictionary.getDictionaries());

            outputStream.write(dataJson.getBytes());
            outputStream.flush();
            outputStream.close();

            return true;
        }
        catch (IOException ioe) {
            System.out.println("Exception in saving: " + ioe.getMessage());
            return false;
        }
    }

    public static List<Dictionary> retrieveData(Context context) {
        if (!isExternalStorageReadable())
            return new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput(FILENAME), StandardCharsets.UTF_8))) {
            String dataJson = reader.lines().reduce("", (acc, val) -> acc + val);
            Type type = new TypeToken<ArrayList<Dictionary>>(){}.getType();
            return gson.fromJson(dataJson, type);
        }
        catch (IOException fnfe) {
            fnfe.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void exportDictionary(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, "dictionary.json");

        startActivityForResult(activity, intent, CREATE_FILE, null);
    }

    public static void importDictionary(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");

        startActivityForResult(activity, intent, OPEN_FILE, null);
    }

    private static boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private static boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }
}
