package org.kustom.api.preset;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class PresetInfo {
    private final static String TAG = PresetInfo.class.getSimpleName();

    private final static Gson sGson = new Gson();

    @SerializedName("version")
    private final int mVersion;
    @SerializedName("title")
    private final String mTitle;
    @SerializedName("description")
    private final String mDescription;
    @SerializedName("author")
    private final String mAuthor;
    @SerializedName("email")
    private final String mEmail;
    @SerializedName("archive")
    private final String mArchive;
    @SerializedName("width")
    private final int mWidth;
    @SerializedName("height")
    private final int mHeight;
    @SerializedName("xscreens")
    private final int mXScreens;
    @SerializedName("yscreens")
    private final int mYScreens;
    @SerializedName("features")
    private final String mFeatures;
    @SerializedName("release")
    private final int mRelease;
    @SerializedName("locked")
    private final boolean mLocked;
    @SerializedName("pflags")
    private final int mFlags;

    private PresetInfo(Builder builder) {
        mVersion = builder.mVersion;
        mTitle = TextUtils.isEmpty(builder.mTitle) ? builder.mFallbackTitle : builder.mTitle;
        mDescription = builder.mDescription;
        mAuthor = builder.mAuthor;
        mEmail = builder.mEmail;
        mArchive = builder.mArchive;
        mWidth = builder.mWidth;
        mHeight = builder.mHeight;
        mXScreens = builder.mXScreens;
        mYScreens = builder.mYScreens;
        mFeatures = builder.mFeatures;
        mRelease = builder.mRelease;
        mLocked = builder.mLocked;
        mFlags = builder.mFlags;
    }

    @NonNull
    @Override
    public String toString() {
        String result = mTitle;
        if (!TextUtils.isEmpty(mDescription)) result += "\n" + mDescription;
        if (!TextUtils.isEmpty(mAuthor)) result += "\nAuthor: " + mAuthor;
        return result;
    }

    /**
     * @return preset version, like 11
     */
    public int getVersion() {
        return mVersion;
    }

    /**
     * @return title of the preset
     */
    @NonNull
    public String getTitle() {
        return mTitle != null ? mTitle : "";
    }

    /**
     * @return description of the preset
     */
    @NonNull
    public String getDescription() {
        return mDescription != null ? mDescription : "";
    }

    /**
     * @return author of the preset
     */
    @NonNull
    public String getAuthor() {
        return mAuthor != null ? mAuthor : "";
    }

    /**
     * @return email of the author
     */
    @NonNull
    public String getEmail() {
        return mEmail != null ? mEmail : "";
    }

    /**
     * @return Kustom internal URI representing file authority
     */
    @NonNull
    public String getArchive() {
        return mArchive != null ? mArchive : "";
    }

    /**
     * @return width of the preset at the time of saving
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * @return height of the preset at the time of saving
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * @return number of the horizontal screens in the preset when saving (if applicable)
     */
    public int getXScreens() {
        return mXScreens;
    }

    /**
     * @return number of the vertical screens in the preset when saving (if applicable)
     */
    public int getYScreens() {
        return mYScreens;
    }

    /**
     * @return preset features
     */
    @NonNull
    public PresetFeatures getFeatures() {
        if (mFeatures == null) return new PresetFeatures();
        else return new PresetFeatures(mFeatures);
    }

    /**
     * @return kustom release in which this preset was saved
     */
    public int getRelease() {
        return mRelease;
    }

    /**
     * @return weather or not this preset has been locked by the author
     */
    public boolean isLocked() {
        return mLocked;
    }

    /**
     * @return internal used preset update flags
     */
    public int getFlags() {
        return mFlags;
    }

    /**
     * @return a copy method with a builder function
     */
    public PresetInfo copy(@Nullable Consumer<Builder> builder) {
        Builder b = new Builder(this);
        if (builder != null) builder.accept(b);
        return b.build();
    }

    /**
     * @return a copy method with a builder function
     */
    public PresetInfo copy() {
        return copy(null);
    }

    @Nullable
    private static PresetInfo fromStream(@NonNull InputStream is) {
        PresetInfo info = null;
        try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             JsonReader reader = new JsonReader(new BufferedReader(isr))) {
            reader.beginObject();
            String name = reader.nextName();
            if (name.equals("preset_info"))
                return sGson.fromJson(reader, PresetInfo.class);
        } catch (IOException e) {
            Log.w(TAG, "Unable to read preset from input stream", e);
        }
        return null;
    }

    @Nullable
    private static PresetInfo fromJson(@NonNull String json) {
        if (!TextUtils.isEmpty(json)) try {
            return sGson.fromJson(json, PresetInfo.class);
        } catch (Exception ignored) {
        }
        return null;
    }

    @SuppressWarnings("WeakerAccess")
    public static final class Builder {
        private String mFallbackTitle = "";
        private String mTitle;
        private int mVersion;
        private String mDescription;
        private String mAuthor;
        private String mEmail;
        private String mArchive;
        private int mWidth;
        private int mHeight;
        private int mXScreens;
        private int mYScreens;
        private String mFeatures;
        private int mRelease;
        private boolean mLocked;
        private int mFlags;

        public Builder() {
        }

        public Builder(@Nullable PresetInfo info) {
            if (info != null) {
                mTitle = info.mTitle;
                mVersion = info.mVersion;
                mDescription = info.mDescription;
                mAuthor = info.mAuthor;
                mEmail = info.mEmail;
                mArchive = info.mArchive;
                mWidth = info.mWidth;
                mHeight = info.mHeight;
                mXScreens = info.mXScreens;
                mYScreens = info.mYScreens;
                mFeatures = info.mFeatures;
                mRelease = info.mRelease;
                mLocked = info.mLocked;
                mFlags = info.mFlags;
            }
        }

        public Builder(@NonNull InputStream is) {
            this(fromStream(is));
        }

        public Builder(@NonNull JsonObject json) {
            this(json.toString());
        }

        public Builder(@NonNull String json) {
            this(fromJson(json));
        }

        public Builder withVersion(int version) {
            mVersion = version;
            return this;
        }

        public Builder withTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder withFallbackTitle(String title) {
            mFallbackTitle = TextUtils.isEmpty(title) ? "" : title;
            return this;
        }

        public Builder withDescription(String description) {
            mDescription = description;
            return this;
        }

        public Builder withAuthor(String author) {
            mAuthor = author;
            return this;
        }

        public Builder withEmail(String email) {
            mEmail = email;
            return this;
        }

        public Builder withArchive(String archive) {
            mArchive = archive;
            return this;
        }

        public Builder withWidth(int width) {
            mWidth = width;
            return this;
        }

        public Builder withHeight(int height) {
            mHeight = height;
            return this;
        }

        public Builder withXScreens(int xScreens) {
            mXScreens = xScreens;
            return this;
        }

        public Builder withYScreens(int yScreens) {
            mYScreens = yScreens;
            return this;
        }

        public Builder withFeatures(String features) {
            mFeatures = features;
            return this;
        }

        public Builder withRelease(int release) {
            mRelease = release;
            return this;
        }

        public Builder withLocked(boolean locked) {
            mLocked = locked;
            return this;
        }

        public Builder withFlags(int flags) {
            mFlags = flags;
            return this;
        }

        public PresetInfo build() {
            return new PresetInfo(this);
        }
    }
}