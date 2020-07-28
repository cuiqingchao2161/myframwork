package com.cui.lib.utils.glide;

import android.content.Context;


import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import androidx.annotation.NonNull;

/**
 * @author xujiangang
 */
@GlideModule(glideName = "HiGlideApp")
public class HiGlideModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        // 设置磁盘缓存为100M，缓存在内部缓存目录
        int cacheSize100MegaBytes = 100 * 1024 * 1024;
        //设置到内部路径
        builder.setDiskCache(
                new InternalCacheDiskCacheFactory(context, "glide_cache",cacheSize100MegaBytes)
        );
        //设置到外部路径
        //builder.setDiskCache(
        //new ExternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));

        //缓存到指定位置
//        String downloadDirectoryPath = Environment.getDownloadCacheDirectory().getPath();
//        builder.setDiskCache(
//                new DiskLruCacheFactory( downloadDirectoryPath, cacheSize100MegaBytes )
//        );

        //设置ram缓存
        MemorySizeCalculator.Builder memoryBuilder = new MemorySizeCalculator.Builder(context);
        MemorySizeCalculator calculator = memoryBuilder.build();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        // app比默认多20%的缓存
        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache( new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool( new LruBitmapPool(customBitmapPoolSize));

    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpGlideUrlLoader.Factory());
    }
}
