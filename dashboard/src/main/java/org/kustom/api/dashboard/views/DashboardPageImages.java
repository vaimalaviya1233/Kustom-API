package org.kustom.api.dashboard.views;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kustom.api.dashboard.ImagePreviewActivity;
import org.kustom.api.dashboard.model.DashboardImageItem;
import org.kustom.api.dashboard.utils.JsonUtils;
import org.kustom.api.dashboard.utils.OkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DashboardPageImages extends DashboardPage<DashboardImageItem> {
    private final OkHttpClient mHttpClient;

    public DashboardPageImages(@NonNull Context context) {
        super(context);
        mHttpClient = new OkHttpClient.Builder()
                .cache(OkHttpUtils.getCacheDirectory(context))
                .build();
    }

    public void setUrl(@NonNull String url) {
        // If url is a file, load it directly
        if (!url.contains("://")) {
            try {
                String jsonData = JsonUtils.loadAssetFileAsString(getContext(), url);
                ArrayList<DashboardImageItem> list = parseJson(jsonData);
                setEntries(list);
            } catch (IOException e) {
                e.printStackTrace();
                setText(e.getMessage());
            }
        }
        // Otherwise load it from the web
        else {
            Request request = new Request.Builder().url(url).build();
            mHttpClient.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                    setText(e.getMessage());
                }

                @Override
                public void onResponse(
                        @NonNull Call call,
                        @NonNull Response response
                ) throws IOException {
                    ArrayList<DashboardImageItem> list;
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);
                        String body = responseBody != null ? responseBody.string() : "";
                        list = parseJson(body);
                    } catch (Exception e) {
                        throw new IOException(e);
                    }
                    setEntries(list);
                }
            });
        }
    }

    @Override
    public boolean onClick(DashboardImageItem item) {
        Intent intent = new Intent(getContext(), ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.EXTRA_IMAGE_DATA, item.getImageData().getJsonData());
        getContext().startActivity(intent);
        return false;
    }

    private ArrayList<DashboardImageItem> parseJson(String jsonData) throws IOException {
        if (TextUtils.isEmpty(jsonData)) throw new IOException("Empty data");
        ArrayList<DashboardImageItem> list = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(jsonData);
            JSONArray walls = data.getJSONArray("wallpapers");
            for (int i = 0; i < walls.length(); i++) {
                list.add(new DashboardImageItem(walls.getJSONObject(i), getScreenRatio()));
            }
            return list;
        } catch (JSONException e) {
            throw new IOException("Invalid JSON " + e.getMessage());
        }
    }
}
