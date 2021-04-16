package fun.nemo.community.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;



public class FileUtils {
    private final static String TAG = "FileUtils";

    public static File initCrashLogFolder(Context context) {
        File file = null;
        file = new File(context.getCacheDir().getAbsolutePath(), "/Crash");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }



    /**
     * 检查文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean isFileExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File f = new File(path);
        return f.exists();
    }

    /**
     * 获取指定路径文件的大小
     *
     * @param path
     * @return
     */
    public static long getFileSize(String path) {
        if (path == null)
            return 0;

        File file = new File(path);
        return file.length();
    }

    public static long lastModified(String path) {
        if (TextUtils.isEmpty(path)) {
            return -1;
        }

        File file = new File(path);

        return file.lastModified();
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                if (!file.delete())
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 目录是否可写
     *
     * @param folder
     * @return
     */
    public static boolean isFolderWritable(String folder) {
        File testFile = new File(folder, ".test.tmp");
        try {
            testFile.createNewFile();
            if (testFile.exists()) {
                testFile.delete();
                return true;
            }
        } catch (Exception e) {
            if (testFile.exists()) {
                testFile.delete();
            }
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 从指定目录删除满足条件的文件
     *
     * @param context
     * @param dirPath
     * @param filter
     * @param removeFromMediaLibrary
     */
    public static void deleteFilesInDir(Context context, String dirPath,
                                        FilenameFilter filter, boolean removeFromMediaLibrary) {

        ContentResolver resolver = context.getContentResolver();

        try {
            File dirFile = new File(dirPath);
            String[] files = filter == null ? dirFile.list() : dirFile
                    .list(filter);
            for (String fileName : files) {
                File file = new File(dirPath + "/" + fileName);
                file.delete();

                if (removeFromMediaLibrary) {
                    resolver.delete(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            MediaStore.Video.Media.DATA + "=?",
                            new String[]{file.getAbsolutePath()});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mkdir(String dirPath) {
        try {
            File dir = new File(dirPath);
            if (dir.exists() && dir.isFile()) {
                dir.delete();
            }

            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSuffix(String path) {
        if (path != null) {
            int index = path.lastIndexOf(".");
            if (index > 0 && index + 1 < path.length() && index + 10 > path.length()) {
                return path.substring(index + 1);
            }
        }
        return "";
    }

    public static void writeByteTOSD(String savePath, String fileName, byte[] data) {
        if (data != null) {
            OutputStream outputStream = null;
            String filePath = savePath + "/" + fileName;
            try {
                File file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                if (data != null) {
                    outputStream = new BufferedOutputStream(new FileOutputStream(file));
                    outputStream.write(data);
                    outputStream.flush();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
//				Log.e("MMMM", "debug:" + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
//				Log.e("MMMM", "debug:" + e.getMessage());
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] readByteToMemory(String savePath, String fileName) {
        String filePath = savePath + "/" + fileName;
        if (filePath != null) {
            InputStream inputStream = null;
            try {
                File file = new File(filePath);
                if (!file.exists()) {
                    return null;
                }
                byte[] data = new byte[(int) file.length()];
                inputStream = new BufferedInputStream(new FileInputStream(file));
                inputStream.read(data, 0, data.length);
                inputStream.close();
                return data;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String getPathRoot(String photoPath) {
        if (photoPath == null) {
            return "";
        }
        if (photoPath.contains("/")) {
            return photoPath.substring(photoPath.lastIndexOf("/") + 1);
        } else {
            return photoPath;
        }
    }




    public static boolean copy(InputStream sourceFile, File destFile) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(destFile);
            byte[] buf = new byte[4096];
            int len;
            while ((len = sourceFile.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {

                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public static boolean copy(File source, File dest) {
        boolean result = false;

        if (source == null || dest == null) {
            return false;
        }

        if (!source.exists()) {
            return false;
        }

        if (!dest.exists()) {
            try {
                dest.createNewFile();
            } catch (Exception e) {
                dest.delete();
                e.printStackTrace();
            }
        }

        InputStream input = null;
        OutputStream output = null;

        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static boolean rename(File file, File newPath) {
        if (newPath == null || file == null || !file.exists()) {
            return false;
        }

        return file.renameTo(newPath);
    }

    public static String read(String path) {
        try {
            String receiveString;
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = new FileInputStream(new File(path));
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }

            inputStream.close();
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }

        return null;
    }



    public static File writeToFile(InputStream is, String path) throws IOException {
        File file = new File(path);
        if (file.exists() && !file.delete()) {
            throw new IOException("failed to delete file:" + file.getPath());
        }

        File tmp = new File(file.getPath() + ".tmp");

        if (tmp.exists() && !tmp.delete()) {
            throw new IOException("failed to delete tmp file:" + tmp.getPath());
        }
        FileOutputStream fos = null;
        try {
            if (!tmp.createNewFile()) {
                throw new IOException("failed to create file:" + tmp.getPath());
            }

            fos = new FileOutputStream(tmp);
            byte[] buffer = new byte[8096];
            int c;
            while ((c = is.read(buffer)) != -1) {
                fos.write(buffer, 0, c);
            }
            if (!tmp.renameTo(file)) {
                throw new IOException("failed to rename file:" + tmp.getPath());
            }
            return file;
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } finally {
            closeQuietly(fos);
        }
    }
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }



    public static boolean delDirAndFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delDirAndFile(f);
            }
        }
        return file.delete();
    }

    /**
     * 从assets目录下拷贝文件
     *
     * @param context            上下文
     * @param assetsFilePath     文件的路径名如：SBClock/0001cuteowl/cuteowl_dot.png
     * @param targetFileFullPath 目标文件路径如：/sdcard/SBClock/0001cuteowl/cuteowl_dot.png
     */
    public static boolean copyFileFromAssets(Context context, String assetsFilePath, String targetFileFullPath) {
        Log.d("FileUtils", "copyFileFromAssets ");
        InputStream assetsInputStream;
        try {
            assetsInputStream = context.getAssets().open(assetsFilePath);
            return copyFileByInputStream(assetsInputStream, targetFileFullPath);
        } catch (IOException e) {
            Log.d("FileUtils", "copyFileFromAssets " + "IOException-" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 拷贝文件流
     *
     * @param in
     * @param targetPath
     */
    private static boolean copyFileByInputStream(InputStream in, String targetPath) {
        try {
            boolean fileExists = isFileExists(targetPath);
            if (fileExists) {
                deleteFile(targetPath);
            }
            FileOutputStream fos = new FileOutputStream(new File(targetPath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = in.read(buffer)) != -1) {// 循环从输入流读取
                // buffer字节
                fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
            }
            fos.flush();// 刷新缓冲区
            in.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
