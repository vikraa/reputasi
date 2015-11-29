package com.reputasi.callblocker.view.utilities;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by vikraa on 4/18/2015.
 */
public class Font {
    public static final class FontType{
        private static final String [] AssetLocation = new String[]{
                "fonts/Roboto-Light.ttf",
                "fonts/Roboto-Black.ttf",
                "fonts/Roboto-Bold.ttf",
                "fonts/Roboto-Italic.ttf",
                "fonts/Roboto-Medium.ttf",
                "fonts/Roboto-Regular.ttf",
        };

        public static final int RobotoLight = 0;
        public static final int RobotoBlack = 1;
        public static final int RobotoBold = 2;
        public static final int RobotoItalic = 3;
        public static final int RobotoMedium = 4;
        public static final int RobotoRegular = 5;
        public static final int BebasNeue = 6;
    }

    public static Typeface getFont(Context context, int typeFont){

        if (typeFont > 0 && typeFont < FontType.AssetLocation.length ){
            return Typeface.createFromAsset(context.getAssets(), FontType.AssetLocation[typeFont] );
        }
        else{
            return Typeface.createFromAsset(context.getAssets(), FontType.AssetLocation[0] );
        }
    }
}
