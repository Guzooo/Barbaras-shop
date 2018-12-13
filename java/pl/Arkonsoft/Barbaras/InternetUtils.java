package pl.Arkonsoft.Barbaras;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class InternetUtils {

    public static boolean isOnline(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static String makeServiceCallGetAuth(String string, String string2){
        String convertedResponse = "";
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(string).openConnection();
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.addRequestProperty("Authorization", string2);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            int code = Integer.valueOf(httpURLConnection.getResponseCode());
            if(code == 200){
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String readerLine = "";
                while ((readerLine = reader.readLine()) != null){
                    convertedResponse += readerLine;
                }
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return convertedResponse;
    }

    //TODO: delete when no used
    private String convertResponseToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String string = "";
        while (true) {
            try {
                String readLine = reader.readLine();
                if(readLine == null)
                    break;
                string = readLine + "\n";
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return string;
    }
}
