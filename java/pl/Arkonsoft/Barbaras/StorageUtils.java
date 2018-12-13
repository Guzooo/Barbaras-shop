package pl.Arkonsoft.Barbaras;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

public class StorageUtils {

    private final String SHARED_PREFERENCE_NAME = "barbaraspreference";

    private final String FOLDER_WITH_DOWNLOAD_IMAGE_NAME = "Images";
    private String FOLDER_WITH_DOWNLOAD_IMAGE_PATH;

    private SharedPreferences sharedPreferences;
    private static  StorageUtils storage;

    public static StorageUtils getStorage(Context context){
        if(storage == null) {
            storage = new StorageUtils();
            storage.sharedPreferences = context.getSharedPreferences(storage.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
            //catalogs = new ArrayList<>();
            //pictures = new ArrayList<>();
            //jakies jeszcze 3 z Storage/initialize

            try {
                File file = new File(context.getFilesDir().getAbsolutePath() + "/" + storage.FOLDER_WITH_DOWNLOAD_IMAGE_NAME);
                if(!file.exists()){
                    file.mkdir();
                }
                storage.FOLDER_WITH_DOWNLOAD_IMAGE_PATH = file.getAbsolutePath() + "/";
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return storage;
    }

    public void setString(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
