package com.lexivo.util;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lexivo.schema.Dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class Memory {
    private final static String FILENAME = "dictionaries.json";
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

    private static boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private static boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }
}
