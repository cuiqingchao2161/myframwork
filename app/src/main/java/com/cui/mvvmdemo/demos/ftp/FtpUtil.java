package com.cui.mvvmdemo.demos.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;


public class FtpUtil {


    private  static FTPClient ftpClient = new FTPClient();
    private  static String encoding = System.getProperty("file.encoding");

    private static  FtpUtil instance;
    Logger log = Logger.getLogger(FtpUtil.class.getSimpleName());

    /**
     * 实例化FtpUtil
     * @return
     */
    public synchronized static FtpUtil getInstance(){
        if( instance == null ){
            instance = new FtpUtil();
        }
        return instance;
    }



    /**
     * 连接FTP服务器
     */
    public  boolean login(String url, int port, String username,
                          String password){
        try {
            int reply;
            ftpClient.connect(url,port); // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器，否则 ftp.connect(url, port);// 连接FTP服务器
            ftpClient.login(username, password); // 登录
//            ftpClient.setControlEncoding(encoding);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) { // 检验是否连接成功
                log.info("FTP连接失败");
                ftpClient.disconnect();
                return false;
            }
            log.info("FTP连接成功");
        }catch (IOException e) {
            e.printStackTrace();
            closeCon(); //关闭
        }
        return true;
    }


    /**
     * Description: 向FTP服务器上传文件
     *
     * @Version1.0
     * @param filePath
     *            本地文件路径
     * @param ftpPath
     *            FTP服务器保存目录,如果是根目录则为“/”
     * @param fileName
     *            上传到FTP服务器上的文件名
     * @return 成功返回true，否则返回false
     */
    public boolean uploadFile(String ftpPath, String filePath, String fileName) {
        boolean result = false;
        try {
            if( ftpClient!=null ){
                FileInputStream input = new FileInputStream(new File(filePath+fileName));
                boolean change = ftpClient.changeWorkingDirectory(ftpPath); // 转移工作目录至指定目录下
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                if (change) {
                    log.info("成功切换目录到"+ftpPath);
                    result = ftpClient.storeFile(new String(fileName.getBytes("GBK"),"iso-8859-1"), input);
                    if (result) {
                        log.info(fileName+",上传成功!");
                    }else{
                        log.info(fileName+",上传失败");
                    }
                }else {
                    log.info("切换目录失败");
                }
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将本地文件上传到FTP服务器上
     *
     *
     public static void testUpLoadFromDisk(String url, int port, String username,
     String password, String path, String filename,String filepath) {
     try {
     FileInputStream in = new FileInputStream(new File(filepath));
     boolean flag = uploadFile(url, port, username,password, path, filename, in);
     System.out.println(flag);
     } catch (FileNotFoundException e) {
     e.printStackTrace();
     }
     }
     /
     /**
     * 删除FTP上的文件
     */

    public static boolean removeFile(String srcFname){
        boolean flag = false;
        if( ftpClient!=null ){
            try {
                flag = ftpClient.deleteFile(srcFname);
            } catch (IOException e) {
                e.printStackTrace();
                closeCon();
            }
        }
        return flag;
    }

    /**
     * 改名FTP上的文件
     */
    public static boolean renameFile(String ftpPath, String srcFname, String targetFname){
        boolean flag = false;
        if( ftpClient!=null ){
            try {
                flag = ftpClient.rename(ftpPath + srcFname,ftpPath + targetFname);
            } catch (IOException e) {
                e.printStackTrace();
                closeCon();
            }
        }
        return flag;
    }


    /**
     *<p>销毁ftp连接</p>
     */
    public static void closeCon(){
        if(ftpClient !=null){
            if(ftpClient.isConnected()){
                try {
                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载单个文件
     */
    public static boolean downloadFile(String ftpPath, String filePath, String fileName){
        try {
            if( ftpClient!=null ){
                FileInputStream input = new FileInputStream(new File(filePath+fileName));
                boolean change = ftpClient.changeWorkingDirectory(ftpPath); // 转移工作目录至指定目录下
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                if (change) {
                    FTPFile[] fs = ftpClient.listFiles();
                    for(FTPFile ff:fs){
                        if(ff.getName().equals(fileName)){
                            File localFile = new File(filePath+"/"+ff.getName());
                            OutputStream is = new FileOutputStream(localFile);
                            ftpClient.retrieveFile(ff.getName(), is);
                            is.close();
                        }
                    }
                }
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
