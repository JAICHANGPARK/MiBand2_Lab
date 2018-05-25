package nodomain.knu2018.bandutils.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;

public class StorageCheck {
    Context mContext;

    public StorageCheck(Context context) {
        this.mContext = context;
    }

    /**
     * 앱 캐시 지우기
     *
     * @param context
     */
    public static void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                //다운로드 파일은 지우지 않도록 설정
                if (s.equals("lib") || s.equals("files") || s.equals("code_cache") || s.equals("shaders"))
                    continue;
                deleteDir(new File(appDir, s));
                Log.e("test", "File /data/data/" + context.getPackageName() + "/" + s + " DELETED");
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static void clearExternalFolder(String path) {

        File dir = new File(Environment.getExternalStorageDirectory()+ path);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
        //        boolean result = false;
//        File external = Environment.getExternalStorageDirectory();
//        File dataPath = new File(external, path);
//        try {
//            if (dataPath.exists()) {
//                result = dataPath.delete();
//                //return result;
//            }
//
//        } catch (IllegalAccessError e) {
//            e.printStackTrace();
//        }
        //return result;

    }

    /**
     * 앱 캐시영역 데이터 사이즈를 가져오는 코드
     *
     * @param context
     * @return
     */

    public String getCachesSize(Context context) {
        long size = 0;
        File cacheDirectory = context.getCacheDir();
        File[] files = cacheDirectory.listFiles();
        for (File f : files) {
            size = size + f.length();
        }
        //Log.e(TAG, "getCachesSize: " + getReadableFileSize(size));
        String total_size = getReadableFileSize(size);
        return total_size;
    }

    public static long checkSize(long result, File targetFolder) {

        try {
            File[] listFile = targetFolder.listFiles();

            for (int i = 0; i < listFile.length; i++) {
                //Log.e(TAG, "checkSize: " + listFile[i].getPath());
                if (listFile[i].isFile()) {
                    result += listFile[i].length();
                    //Log.e(TAG, "checkSize: listFile[i].length() " + listFile[i].length() + "\n");
                } else {
                    checkSize(result, listFile[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public String getExternalFileSize(String path) {
        long size = 0;
        String result = "";
//        long size1 = 0;
//        long size2 = 0;
        //File cacheDirectory = getCacheDir();
        File external = Environment.getExternalStorageDirectory();
        File exTopFoler = new File(external, path);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.size(exTopFoler.toPath());
            }
            if (exTopFoler.exists()) {
                Log.e("storageSize", "getExternalFileSize: getPath " + exTopFoler.getPath().length() );
                Log.e("storageSize", "getExternalFileSize: " + exTopFoler.length() );
                Log.e("storageSize", "getExternalFileSize: " + exTopFoler.length() );
                size = size + exTopFoler.length();
                result = getReadableFileSize(size);
                return result;
            } else {
                result = "Folder Doesn't Exists";
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;


//        Log.e(TAG, "getExternalFileSize: " + external.getPath());
//        String topFolder = "/BandUtil/";
//        String dataFolder = "/BandUtil/data/";
//        String imagesFolder = "/BandUtil/images";


//        File exDataFolder = new File(external, dataFolder);
//        File exImagesFolder = new File(external, imagesFolder);

//        Log.e(TAG, "getExternalFileSize: exTopFoler " + exTopFoler.getPath());
//        Log.e(TAG, "getExternalFileSize: exDataFolder " + exDataFolder.getPath());
//        Log.e(TAG, "getExternalFileSize: exImagesFolder " + exImagesFolder.getPath());

//        size = size + exTopFoler.length();
//        size1 = size1 + exDataFolder.length();
//        size2 = size2 + exImagesFolder.length();

//        Log.e(TAG, "getExternalFileSize: top " + getFileSize(size) );
//        Log.e(TAG, "getCachesSize: data " + getFileSize(size1));
//        Log.e(TAG, "getCachesSize: images " + getFileSize(size2));


//        for (File f : files) {
//            Log.e(TAG, "getCachesSize: " + f.getPath()  + ", " + f.length());
//            size = size + f.length();
//        }
//        //Log.e(TAG, "getCachesSize: " + getReadableFileSize(size));
//        String total_size  = getFileSize(size);
//        return total_size;
    }

    /**
     * long으로 입력된 파라미터를 파일 크기의 사이즈로 변환시켜주는 메소드
     *
     * @param size
     * @return
     */

    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
