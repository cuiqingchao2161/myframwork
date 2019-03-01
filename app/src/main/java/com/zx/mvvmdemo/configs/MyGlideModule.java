package com.zx.mvvmdemo.configs;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

/**
 * Created by Administrator on 2019/2/28.
 */

public class MyGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(
                new InternalCacheDiskCacheFactory(context, "glide_cache", 100 * 1024 * 1024));
        //builder.setDiskCache(
        //        new ExternalCacheDiskCacheFactory(context, "glide_cache", 100 * 1024 * 1024));
        builder.setDiskCache(new DiskLruCacheFactory("myframwork","glide_cache",100*1024*1024));

        //Note: getMyCacheLocationBlockingIO方法返回的文件不能为空，而且必须是一个已经创建好的文件目录，不可以是文件。
        builder.setDiskCache(new DiskLruCacheFactory(new DiskLruCacheFactory.CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                return new File("ded");
            }
        },  100 * 1024 * 1024));
    }


    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
