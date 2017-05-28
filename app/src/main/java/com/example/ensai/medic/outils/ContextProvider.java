package com.example.ensai.medic.outils;

/**
 * Created by ensai on 18/05/17.
 */
import android.app.Application;
import android.content.Context;
public class ContextProvider extends Application {

        public static Context sContext;


        public void onCreate() {
            super.onCreate();

            sContext = getApplicationContext();

        }

        /**
         * Returns the application context
         *
         * @return application context
         */
        public static Context getContext() {
            return sContext;
        }

}





