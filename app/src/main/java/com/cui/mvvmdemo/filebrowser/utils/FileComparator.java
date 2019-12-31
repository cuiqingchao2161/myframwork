package com.cui.mvvmdemo.filebrowser.utils;

import java.io.File;
import java.util.Comparator;

/**
 *
 */
public class FileComparator implements Comparator<File> {
    @Override
    public int compare(File f1, File f2) {
        if(f1 == f2) {
            return 0;
        }
        if(f1.isDirectory() && f2.isFile()) {
            // Show directories above files
            return -1;
        }
        if(f1.isFile() && f2.isDirectory()) {
            // Show files below directories
            return 1;
        }
        // Sort the directories alphabetically
        if(f1.lastModified() < f2.lastModified())
        {
            return 1;
        }else
        {
            return -1;
        }
    }
}
