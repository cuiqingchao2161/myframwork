package com.cui.vrlibrary.network;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Information class of media file
 */
public class ImageInfo implements Parcelable {
    public static String FILE_FORMAT_CODE_EXIF_JPEG = "JPEG";
    public static String FILE_FORMAT_CODE_EXIF_MPEG = "MPEG";

    private String mFileName;
    private String mFileId;
    private long mFileSize;
    private String mCaptureDate;
    private String mFileFormat;
    private int mWidth;
    private int mHeight;

    /**
     * Acquire file name
     * @return File name
     */
    public String getFileName() {
        return mFileName;
    }

    /**
     * Set file name
     * @param fileName File name
     */
    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    /**
     * Acquire File ID
     * @return File ID
     */
    public String getFileId() {
        return mFileId;
    }

    /**
     * Set File ID
     * @param fileId File ID
     */
    public void setFileId(String fileId) {
        mFileId = fileId;
    }

    /**
     * Acquire file size
     * @return File size (unit: bytes)
     */
    public long getFileSize() {
        return mFileSize;
    }

    /**
     * Set file size
     * @param fileSize File size (unit: bytes)
     */
    public void setFileSize(long fileSize) {
        mFileSize = fileSize;
    }

    /**
     * Acquire shooting time
     * @return Shooting time
     */
    public String getCaptureDate() {
        return mCaptureDate;
    }

    /**
     * Set shooting time
     * @param captureDate Shooting time
     */
    public void setCaptureDate(String captureDate) {
        mCaptureDate = captureDate;
    }

    /**
     * Acquire media format
     * @return Media format
     */
    public String getFileFormat() {
        return mFileFormat;
    }

    /**
     * Set media format<p>
     * Set {@link ImageInfo#FILE_FORMAT_CODE_EXIF_JPEG} or {@link ImageInfo#FILE_FORMAT_CODE_EXIF_MPEG}.
     * @param fileFormat Media format
     */
    public void setFileFormat(String fileFormat) {
        mFileFormat = fileFormat;
    }

    /**
     * Acquire image width
     * @return Image width
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * Set image width
     * @param width Image width
     */
    public void setWidth(int width) {
        mWidth = width;
    }

    /**
     * Acquire image height
     * @return Image height
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * Set image height
     * @param height Image height
     */
    public void setHeight(int height) {
        mHeight = height;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mFileName);
        dest.writeString(this.mFileId);
        dest.writeLong(this.mFileSize);
        dest.writeString(this.mCaptureDate);
        dest.writeString(this.mFileFormat);
        dest.writeInt(this.mWidth);
        dest.writeInt(this.mHeight);
    }

    public ImageInfo() {
    }

    protected ImageInfo(Parcel in) {
        this.mFileName = in.readString();
        this.mFileId = in.readString();
        this.mFileSize = in.readLong();
        this.mCaptureDate = in.readString();
        this.mFileFormat = in.readString();
        this.mWidth = in.readInt();
        this.mHeight = in.readInt();
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel source) {
            return new ImageInfo(source);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };
}
