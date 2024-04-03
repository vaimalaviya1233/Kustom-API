package org.kustom.api.dashboard.config;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class KustomEnv {
    private final static String TAG = KustomEnv.class.getSimpleName();

    private final String mExtension;
    private final String mFolder;
    private final String mPkg;
    private final String mEditorActivity;
    private String[] mFiles;

    KustomEnv(
            @NonNull String extension,
            @NonNull String folder,
            String pkg,
            String editorActivity
    ) {
        mExtension = extension;
        mFolder = folder;
        mPkg = pkg;
        mEditorActivity = editorActivity;
    }

    @NonNull
    public String getExtension() {
        return mExtension;
    }

    @NonNull
    public String getFolder() {
        return mFolder;
    }

    @Nullable
    public String getPkg() {
        return mPkg;
    }

    @Nullable
    public String getEditorActivity() {
        return mEditorActivity;
    }

    public synchronized String[] getFiles(@NonNull Context context) {
        if (mFiles == null) {
            ArrayList<String> result = new ArrayList<>();
            try {
                AssetManager assets = context.getAssets();
                String[] files = assets.list(getFolder());
                if (files != null) {
                    for (String file : files) {
                        if (file.toLowerCase().endsWith(getExtension())
                                || file.toLowerCase().endsWith(getExtension() + ".zip"))
                            result.add(String.format("%s/%s", getFolder(), file));
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Unable to list folder: " + getFolder());
            }
            mFiles = result.toArray(new String[0]);
        }
        return mFiles;
    }
}
