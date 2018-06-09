package com.tingwa.utils;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 *
 */
public class FileUtils {

    //获取最近三十天的总时间，单位为秒
    public static final double ONEMOUNTHMILLTIMES = 2592000;
    //默认获取最多500条最近添加记录
    public static final int LASTFILESCOUNT = 500;

    /**
     * Check if the primary "external" storage device is available.
     * 判断sd卡是否存在
     *
     * @return
     */
    public static boolean hasSDCardMounted() {
        String state = Environment.getExternalStorageState();
        if (state != null && state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 当前路径可用空间大小 bytes
     *
     * @param path
     * @return
     */
    public static long getUsableSpace(File path) {
        if (path == null) {
            return -1;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        } else {
            if (!path.exists()) {
                return 0;
            } else {
                final StatFs stats = new StatFs(path.getPath());
                return (long) stats.getBlockSizeLong() * (long) stats.getAvailableBlocksLong();
            }
        }
    }

    /**
     * 当前路径可用空间大小 bytes
     *
     * @param filePath
     * @return
     */
    public static long getAvailableSize(String filePath) {
        if (filePath == null) {
            return -1;
        }

        File path = new File(filePath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        } else {
            if (!path.exists()) {
                return 0;
            } else {
                final StatFs stats = new StatFs(path.getPath());
                return (long) stats.getBlockSizeLong() * (long) stats.getAvailableBlocksLong();
            }
        }
    }

    /**
     * 当前路径空间大小 bytes
     *
     * @param path
     * @return
     */
    public static long getTotalSpace(File path) {
        if (path == null) {
            return -1;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getTotalSpace();
        } else {
            if (!path.exists()) {
                return 0;
            } else {
                final StatFs stats = new StatFs(path.getPath());
                return (long) stats.getBlockSizeLong() * (long) stats.getBlockCountLong();
            }
        }
    }

    /**
     * @param realPath
     * @return
     */
    public static long getTotalSize(String realPath) {
        try {
            StatFs statFs = new StatFs(realPath);
            long blockSize = statFs.getBlockSizeLong();
            long totalBlocks = statFs.getBlockCountLong();
            long totalSize = blockSize * totalBlocks;
            return totalSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取文件所在目录
     *
     * @param filePath
     * @return
     */
    public static String getFileParent(String filePath) {
        String path = filePath;
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        if (path.endsWith("/")) {
            int index = path.lastIndexOf("/");
            if (index >= 0 || index < path.length()) {
                path = path.substring(0, index);
            }
        }

        int start = path.lastIndexOf("/");
        if (start == -1) {
            return null;
        }
        String dir = path.substring(0, start);
        return dir;
    }

    /**
     * 获取文件名称
     *
     * @return
     */
    public static String getFileName(String filepath) {
        String path = filepath;
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        if (path.endsWith("/")) {
            int index = path.lastIndexOf("/");
            if (index >= 0 || index < path.length()) {
                path = path.substring(0, index);
            }
        }
        int start = path.lastIndexOf("/");
        if (start == -1) {
            return path;
        }
        String fileName = path.substring(start + 1);
        return fileName;
    }

    /**
     * 获取文件后缀
     *
     * @param filepath
     */
    public static String getExtension(String filepath) {
        if (TextUtils.isEmpty(filepath)) {
            return "";
        }
        String fileName = getFileName(filepath);
        int dotPosition = fileName.lastIndexOf(".");
        if ((dotPosition > 0) && (dotPosition != (fileName.length() - 1))) {
            return fileName.substring(dotPosition + 1);
        } else {
            // No extension.
            return "";
        }
    }

    /**
     * 获取文件名称 不包含extension
     *
     * @param fileName
     * @return
     */
    public static String getFileNameWithoutExtension(String fileName) {
        if (fileName == null) {
            return "";

        }
        String name = getFileName(fileName);
        int dotPosition = name.lastIndexOf(".");
        if (dotPosition > 0) {
            return name.substring(0, dotPosition);
        } else {
            return name;
        }
    }

    private static DecimalFormat mFormater = new DecimalFormat("#.##");

    /**
     * @param length 文件长度
     * @return 带有合适单位名称的文件大小
     */
    public static String getSizeFormatText(long length) {
        if (length <= 0) {
            return "0KB";
        }

        String str = "B";
        double result = (double) length;
        if (length < 1024) {
            return "1KB";
        }
        // 以1024为界，找到合适的文件大小单位
        if (result >= 1024) {
            str = "KB";
            result /= 1024;
            if (result >= 1024) {
                str = "MB";
                result /= 1024;
            }
            if (result >= 1024) {
                str = "GB";
                result /= 1024;
            }
        }
        String sizeString = null;

        // 按照需求设定文件的精度
        // MB 和 GB 保留两位小数
        if (str.equals("MB") || str.equals("GB")) {
            sizeString = mFormater.format(result);
        }
        // B 和 KB 保留到各位
        else {
            sizeString = Integer.toString((int) result);
        }
        return sizeString + str;
    }

    public static boolean createFile(String desFileDir, String filename) {
        boolean success = false;
        try {
            File file = new File(desFileDir, filename);
            for (int i = 0; i < 3; i++) {
                if (!file.exists()) {
                    success = file.createNewFile();
                } else {
                    success = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean mkFileDir(String desFileDir, String filename) {
        boolean success = false;
        File destDir = new File(desFileDir + "/" + filename);
        if (!destDir.exists()) {
            success = destDir.mkdirs();
        }
        return success;
    }

    // 检查是否还有足够的空间
    public static boolean checkAvailableSpace(long size, String filePath) {
        if (false == isInSdCard(filePath)) {
            //判断外界盘的存储空间
            long availableSize = getAvailableSize(filePath);
            if (availableSize >= size && availableSize != 0) {
                return true;
            } else {
                return false;
            }
        }

        if (!(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
            // can not be saved
            return false;
        }

        File path = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(path.getPath());
        long blockSize = statFs.getBlockSize();
        long availableBlocks = statFs.getAvailableBlocks();
        long availabSize = blockSize * availableBlocks;

        if (availabSize >= size && availabSize != 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isInSdCard(String filePath) {
        String sdCardPath = Environment.getExternalStorageDirectory().getPath();
        if (!TextUtils.isEmpty(filePath) && filePath.startsWith(sdCardPath)) {
            return true;
        }
        return false;
    }

    public static boolean renameFile(String filePath, String newPath) {
        boolean success = false;
        File file = new File(filePath);
        if (file.exists()) {
            success = file.renameTo(new File(newPath));
        }
        return success;
    }

    public static long getFileSize(File file) {
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
                Log.e("获取文件大小", "文件不存在!");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    public static boolean isContainsHiddenFolderPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        if (path.contains("/.")) {
            return true;
        }
        return false;
    }

    public static boolean isDirectoryByFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return !file.isFile();
    }

    /**
     * Copy data from a source stream to destFile. Return true if succeed,
     * return false if failed.
     */
    public static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            if (destFile.exists()) {
                destFile.delete();
            }
            FileOutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.flush();
                try {
                    out.getFD().sync();
                } catch (IOException e) {
                }
                out.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 判断本地是否存在该文件
     *
     * @param localPath
     * @return
     */
    public static boolean checkLocalExist(String localPath) {
        boolean isExist = false;
        if (!TextUtils.isEmpty(localPath)) {
            File file = new File(localPath);
            if (file.exists()) {
                isExist = true;
            }
        }
        return isExist;
    }


    /**
     * 获取sdcard文件夹路径
     *
     * @return
     */
    public static String getSdCardPath() {
        boolean exist = hasSDCardMounted();
        String sdpath = "";
        if (exist) {
            sdpath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return sdpath;
    }

    /**
     * 获取根文件夹路径
     *
     * @return
     */
    public static String getRootPath() {
        boolean exist = hasSDCardMounted();
        String rootPath = "";
        if (exist) {
            rootPath = Environment.getRootDirectory().getAbsolutePath();
        }
        return rootPath;
    }

    /**
     * 获取文件路径下的所有子文件数组
     *
     * @param path
     * @return
     */
    public static File[] getAllFiles(String path) {
        File[] files = null;
        if (TextUtils.isEmpty(path)) {
            return files;
        }

        try {
            File file = new File(path);
            if (file.exists()) {
                files = file.listFiles();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }


    /**
     * 获取当前目录及子目录下包含的所有文件数量
     *
     * @param path
     * @return
     */
    public static int getFilesCount(String path, boolean hide) {
        int count = 0;
        try {
            if (TextUtils.isEmpty(path)) {
                return 0;
            }
            File file = new File(path);
            if (file.exists() && (!file.isHidden() || hide)) {
                File[] fileList = file.listFiles();
                if (fileList != null) {
                    for (File f : fileList) {
                        if (f.isFile() && (!file.isHidden() || hide)) {
                            count++;
                        } else {
                            count += getFilesCount(f.getPath(), hide);
                        }
                    }
                } else {
                    return 1;//当前路径是文件
                }
            }
        } catch (Exception e) {

        }
        return count;
    }


    private final static String EMMC_SIZE_NODE = "/sys/block/mmcblk0/size";

    private static String readFileInfo(String path) {
        String s = "";
        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                s = s + tempString;
            }
        } catch (FileNotFoundException e) {
            //Log.w("readFileInfo FileNotFoundException = " + e);
        } catch (IOException e) {
            //Log.w("readFileInfo IOException = " + e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //Log.w("close reader IOException = " + e);
                }
            }
        }
        return s;
    }

    private static long getAllBlockCountLong(long blockSize) {
        String allBolockCountStr = readFileInfo(EMMC_SIZE_NODE);

        long allBlockCountLong = 0;
        try {
            allBlockCountLong = Long.parseLong(allBolockCountStr);
        } catch (NumberFormatException e) {
            //Log.w("getAllBlockCountLong error = " + e);
            return 0;
        }
        return allBlockCountLong / blockSize * 512;
    }

    /**
     * 计算磁盘总容量
     *
     * @return
     */
    public static long getSDCardTotalSize() {
        try {
            File dataPath = Environment.getDataDirectory();
            StatFs dataStat = new StatFs(dataPath.getPath());

            long blockSize = dataStat.getBlockSizeLong();
            long allBlocks = getAllBlockCountLong(blockSize);

            long mDiskTotalCapacity = allBlocks * blockSize;

            return mDiskTotalCapacity;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 计算磁盘剩余空间
     */
    private void computeDiskAvailableCapacity() {
        try {
            File dataPath = Environment.getDataDirectory();
            StatFs dataStat = new StatFs(dataPath.getPath());

            long blockSize = dataStat.getBlockSizeLong();
            long availableBlocks = dataStat.getAvailableBlocksLong();

            long mDiskAvailableCapacity = availableBlocks * blockSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将一个inputStream里面的数据写到SD卡中
     *
     * @param path
     * @param fileName
     * @param inputStream
     * @return
     */
    public static File writeToSDfromInput(String path, String fileName, InputStream inputStream) {
        //createSDDir(path);
        File file = null;
        if (createFile(path, fileName)) {
            file = new File(path + fileName);
            file.mkdir();
        }
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            while (inputStream.read(buffer) != -1) {
                outStream.write(buffer);
            }
            outStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
