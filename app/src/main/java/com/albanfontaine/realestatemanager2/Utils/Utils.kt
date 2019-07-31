package com.albanfontaine.realestatemanager2.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import java.text.SimpleDateFormat
import java.util.*

class Utils{
    companion object{
        /**
         * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
         * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
         * @param dollars
         * @return
         */
        fun convertDollarToEuro(dollars: Int): Int { return Math.round(dollars * 0.812).toInt() }

        /**
         * Conversion de la date d'aujourd'hui en un format plus approprié
         * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
         * @return
         */
        fun getTodayDate(): String {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
            return dateFormat.format(Date())
        }



        /**
         * Vérification de la connexion réseau
         * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
         * @param context
         * @return
         */
        fun isInternetAvailable(context: Context): Boolean {
            val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = cm.activeNetworkInfo
            return wifi.isWifiEnabled && network != null && network!!.isConnectedOrConnecting()
        }

        /**
         * Added by Alban Fontaine - 27/07/2019
         */

        /**
         * Conversion of the price of a housing (Euros to Dollars)
         * @param euros
         * @return
         */
        fun convertEuroToDollar(euros: Int): Int { return Math.round(euros / 0.812).toInt() }
    }

}