package com.andreea.bakingapp.baking_app.activity;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestUtils {

    public static String readStringFromFile(Context context, String filePath) throws Exception {
        final InputStream is = context.getResources().getAssets().open(filePath);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }

        reader.close();
        is.close();

        return stringBuilder.toString();
    }
}
