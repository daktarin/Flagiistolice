package eu.huras.marcin.flagiistolice;

import android.content.Context;
import android.graphics.Typeface;
import android.icu.text.DateFormat;

import java.lang.reflect.Field;

/**
 * Created by Marcin on 2017-01-19.
 */

public class ReplaceFont {

    public static void replaceDefaultFont(Context context, String nameOfFontBeingReplace, String nameOfFontInAsset) {

        Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), nameOfFontInAsset);
        replaceFont(nameOfFontBeingReplace, customFontTypeface);

    }

    private static void replaceFont(String nameOfFontBeingReplace, Typeface customFontTypeface) {
        Field myfield;
        try {
            myfield =Typeface.class.getDeclaredField(nameOfFontBeingReplace);
            myfield.setAccessible(true);
            myfield.set(null, customFontTypeface);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
