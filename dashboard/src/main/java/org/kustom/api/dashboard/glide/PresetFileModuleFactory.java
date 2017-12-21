package org.kustom.api.dashboard.glide;

import android.content.Context;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import org.kustom.api.dashboard.model.PresetFile;

import java.io.InputStream;

public class PresetFileModuleFactory implements ModelLoaderFactory<PresetFile, InputStream> {
    private final Context mContext;

    PresetFileModuleFactory(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public ModelLoader<PresetFile, InputStream> build(MultiModelLoaderFactory unused) {
        return new PresetFileModelLoader(mContext);
    }

    @Override
    public void teardown() {
    }
}
