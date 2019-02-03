package com.okro;

import android.content.Context;
import android.graphics.Typeface;

public class FontManager {

    private static final String PATH_NEXA_BOLD = "fonts/NexaDemo-Bold.otf";
    private static final String PATH_NEXA_LIGHT = "fonts/NexaDemo-Light.otf";


    private static final int NEXA_BOLD = 0;
    private static final int NEXA_LIGHT = 1;
    private static final int NUM_OF_CUSTOM_FONTS = 2;

    private static boolean fontsLoaded = false;

    private static Typeface[] fonts = new Typeface[NUM_OF_CUSTOM_FONTS];

    private static String[] fontPath = {PATH_NEXA_BOLD,PATH_NEXA_LIGHT};

    private static Typeface getTypeface(Context context, int fontIdentifier) {
        if (!fontsLoaded) {
            loadAllFonts(context);
        }
        return fonts[fontIdentifier];
    }

    private static void loadAllFonts(Context context) {
        for (int i = 0; i < NUM_OF_CUSTOM_FONTS; i++) {
            fonts[i] = Typeface.createFromAsset(context.getAssets(),
                    fontPath[i]);
        }
        fontsLoaded = true;
    }


    public static Typeface getNexaBold(Context context) {
        return getTypeface(context, NEXA_BOLD);
    }

    public static Typeface getNexaLight(Context context) {
        return getTypeface(context, NEXA_LIGHT);
    }

}