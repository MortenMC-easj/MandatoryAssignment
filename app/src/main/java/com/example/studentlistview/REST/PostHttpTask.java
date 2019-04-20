package com.example.studentlistview.REST;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

// Morten: Check https://stackoverflow.com/a/46329100
public class PostHttpTask extends AsyncTask<String, Void, CharSequence> {
    @Override
    protected CharSequence doInBackground(String... params) {
        String urlString = params[0];
        String jsonPayload = params[1];
        try {
            CharSequence result = HttpHelper2.PostHttpResponse(urlString, jsonPayload);
            return result;
        } catch (IOException ex) {
            cancel(true);
            String errorMessage = ex.getMessage() + "\n" + urlString;
            Log.e("DATA", errorMessage);
            return errorMessage;
        }
    }
}
