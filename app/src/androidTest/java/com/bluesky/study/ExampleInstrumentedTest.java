package com.bluesky.study;

import static android.content.res.AssetManager.ACCESS_RANDOM;
import static android.content.res.AssetManager.ACCESS_UNKNOWN;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    public String json;
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.bluesky.study", appContext.getPackageName());
    }

    @Test
    public void getJsonString() throws IOException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        AssetManager assetManager = appContext.getAssets();
        InputStream inputStream = assetManager.open("person.json");
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        json=stringBuilder.toString();
        Log.e("Json", stringBuilder.toString());
        Gson gson = new Gson();
        List<Person> list = gson.fromJson(json, new TypeToken<List<Person>>(){}.getType());

        for (Person person : list
        ) {
            Log.e("json", "Person=" + person.toString());
        }
    }

    @Test
    public void translateStringToPerson() {
        Gson gson = new Gson();
        List<Person> list = gson.fromJson(json, new TypeToken<List<Person>>(){}.getType());

        for (Person person : list
        ) {
            Log.e("json", "Person=" + person.toString());
        }
    }
}