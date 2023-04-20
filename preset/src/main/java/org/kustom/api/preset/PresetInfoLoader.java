package org.kustom.api.preset;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class PresetInfoLoader {
    private final static HashMap<String, PresetInfo> sPresetInfoCache = new HashMap<>();
    private final PresetFile mFile;
    private final Executor executor = Executors.newFixedThreadPool(4);
    private final Handler handler = new Handler(Looper.getMainLooper());

    private PresetInfoLoader(@NonNull PresetFile file) {
        mFile = file;
    }

    public static PresetInfoLoader create(@NonNull PresetFile file) {
        return new PresetInfoLoader(file);
    }

    public void load(@NonNull Context context, @NonNull Callback callback) {
        synchronized (sPresetInfoCache) {
            if (sPresetInfoCache.containsKey(mFile.getPath()))
                callback.onInfoLoaded(sPresetInfoCache.get(mFile.getPath()));
            else executor.execute(() -> {
                PresetInfo info = null;
                String file = mFile.isKomponent() ? "komponent.json" : "preset.json";
                // Try to load preset info
                try (InputStream stream = mFile.getStream(context, file)) {
                    info = new PresetInfo
                            .Builder(stream)
                            .withFallbackTitle(mFile.getName())
                            .build();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // If failed just use file name as a title
                if (info == null) info = new PresetInfo
                        .Builder()
                        .withTitle(mFile.getName())
                        .build();
                // Done, call callback on main thread
                final PresetInfo result = info;
                handler.post(() -> {
                    synchronized (sPresetInfoCache) {
                        sPresetInfoCache.put(mFile.getPath(), result);
                        callback.onInfoLoaded(result);
                    }
                });
            });
        }
    }

    public interface Callback {
        void onInfoLoaded(PresetInfo info);
    }
}