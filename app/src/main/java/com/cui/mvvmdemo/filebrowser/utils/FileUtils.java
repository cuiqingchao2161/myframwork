package com.cui.mvvmdemo.filebrowser.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.cui.mvvmdemo.filebrowser.entity.FileEntity;
import com.cui.mvvmdemo.filebrowser.entity.FileType;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileUtils {
    /**
     * 根据路径获取file的集合
     *
     * @param path
     * @param filter
     * @return
     */
    public static List<FileEntity> getFileListByDirPath(String path, FileFilter filter) {
        File directory = new File(path);
        File[] files = directory.listFiles(filter);

        if (files == null) {
            return new ArrayList<>();
        }

        List<File> result = Arrays.asList(files);
        Collections.sort(result, new FileComparator());

        List<FileEntity> entities = new ArrayList<>();
        for (File f : result) {
            String absolutePath = f.getAbsolutePath();
            FileEntity e;
            if (checkExits(absolutePath)) {
                e = new FileEntity(absolutePath, f, true);
            } else {
                e = new FileEntity(absolutePath, f, false);
            }
            FileType fileType = getFileTypeNoFolder(PickerManager.getInstance().mFileTypes, absolutePath);
            e.setFileType(fileType);
            if (PickerManager.getInstance().files.contains(e)) {
                e.setSelected(true);
            }
            entities.add(e);
        }
        return entities;
    }

    /**
     * 根据路径获取file的集合
     *
     * @return
     */
    public static ArrayList<FileEntity> getFileListByDirPath(String path ) {
        File directory = new File(path);
        File[] files = directory.listFiles();

        if (files == null) {
            return new ArrayList<>();
        }

        List<File> result = Arrays.asList(files);
        Collections.sort(result, new FileComparator());

        ArrayList<FileEntity> entities = new ArrayList<>();
        for (File f : result) {
            String absolutePath = f.getAbsolutePath();
            FileEntity e = new FileEntity(f.getPath(), f, true);
            e.setName(f.getName());
            e.setSize(getReadableFileSize(f.length()));
            FileType fileType = getFileTypeNoFolder(PickerManager.getInstance().mFileTypes, absolutePath);
            e.setFileType(fileType);
            entities.add(e);
        }
        return entities;
    }

    private static boolean checkExits(String path) {
        for (FileEntity entity : PickerManager.getInstance().files) {
            if (entity.getPath().equals(path)) {
                return true;
            }
        }
        return false;
    }

    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static FileType getFileType(ArrayList<FileType> fileTypes, String path) {
        for (String str : PickerManager.getInstance().mFilterFolder) {//按文件夹筛选
            if (path.contains(str)) {
                for (int index = 0; index < fileTypes.size(); index++) {//按照文件类型筛选
                    for (String string : fileTypes.get(index).filterType) {
                        if (path.endsWith(string))
                            return fileTypes.get(index);
                    }
                }
            }
        }
        return null;
    }

    //不包含文件夹
    public static FileType getFileTypeNoFolder(ArrayList<FileType> fileTypes, String path) {
        for (int index = 0; index < fileTypes.size(); index++) {//按照文件类型筛选
            for (String string : fileTypes.get(index).filterType) {
                if (path.endsWith(string))
                    return fileTypes.get(index);
            }
        }
        return null;
    }

    /*
     * 采用了新的办法获取APK图标，之前的失败是因为android中存在的一个BUG,通过
     * appInfo.publicSourceDir = apkPath;来修正这个问题，详情参见:
     * http://code.google.com/p/android/issues/detail?id=9151
     */
    public static Drawable getApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                Log.e("ApkIconLoader", e.toString());
            }
        }
        return null;
    }
}
