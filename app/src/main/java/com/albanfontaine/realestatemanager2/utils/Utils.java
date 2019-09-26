package com.albanfontaine.realestatemanager2.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Utils {
    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        return formatDate(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Added by Alban Fontaine - 27/07/2019
     */

    /**
     * Conversion of the price of a housing (Euros to Dollars)
     * @param euros
     * @return
     */
    public static int convertEuroToDollar(int euros) { return (int) Math.round(euros / 0.812);}

    /**
     * Formats a date to "dd/MM/yyyy" format
     * @param date
     * @return
     */
    public static String formatDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        return dateFormat.format(date);
    }

    /**
     * Checks if the app has permission to write to device storage
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, Constants.Companion.getPERMISSIONS_STORAGE(), Constants.REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**
     * Takes a String and returns a Google Maps Static API url
     * like https://maps.googleapis.com/maps/api/staticmap?markers=108+boulevard+saint+germain+75006+paris&zoom=15&size=300x300&scale=3&key=AIzaSyADBmQ_3NHCQjEFjqwO4R8YSe3WUc5AwcI
     * @param location
     * @return
     */
    public static String getMapUrl(String location){
        String[] addressParts = location.split(" ");
        StringBuilder sb = new StringBuilder(Constants.MAP_URL_BEGINNING);
        sb.append(addressParts[0]);
        for (int i = 1; i < addressParts.length; i++){
            sb.append("+").append(addressParts[i]);
        }
        sb.append(Constants.MAP_URL_END);
        return sb.toString();
    }

    /**
     * Takes a String of a price in dollars and returns a String of a price in euros with the proper format
     * @param priceDollars
     * @return
     */
    public static String formatPriceToEuros(String priceDollars){
        // 1 - Remove commas
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < priceDollars.length(); i++){
            if(priceDollars.charAt(i) != ',')
                sb.append(priceDollars.charAt(i));
        }
        // 2 - Convert to euros
        String priceEuros = String.valueOf(Utils.convertDollarToEuro(Integer.parseInt(sb.toString())));
        // 3 - Add spaces every 3 digits
        ArrayList<Character> resultArray = new ArrayList<>();
        int x = 0;
        for(int i = priceEuros.length()-1; i >= 0; i--){
            if(x == 3){
                resultArray.add(0, ' ');
                x = 0;
            }
            resultArray.add(0, priceEuros.charAt(i));
            x++;
        }
        // 4 Convert arraylist to string
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < resultArray.size(); i++){
            result.append(resultArray.get(i));
        }
        return result.toString();
    }
}
