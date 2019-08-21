package com.cui.vrlibrary.network;

/**
 * HTTP communication download listener class
 */
public interface HttpDownloadListener {
    /**
     * Total byte count
     */
    void onTotalSize(long totalSize);

    /**
     * Received byte count
     */
    void onDataReceived(int size);

    /**
     * 下载结束
     */
    void onDownloadFinish(ImageData imageData);

    boolean isCanceled();
}
