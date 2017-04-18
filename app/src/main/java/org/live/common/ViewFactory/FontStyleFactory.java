package org.live.common.ViewFactory;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

/**
 * @author Paul Okeke
 * Created by paulex10 on 22/01/2015.
 */
public class FontStyleFactory {

    public static String DEFAULT_FONT = StyleConstants.GENERAL_APP_FONT;

    public static void setFontForAllViews(Context contexts, View[] views, String styleConstant)
    {
        if(checkFontStyleConstant(styleConstant)) {
            for (View view : views) {
                if (view instanceof TextView) {
                    ((TextView )view).setTypeface(Typeface.createFromAsset(contexts.getAssets(), styleConstant));
                }
                else
                {
                    System.err.println("Not An Instance sorry");
                }
            }
        }
        else
        {
            System.err.println("There was an error while setting StyleConstant:: Invalid font Specified");
        }
    }

    public static void setFontForView(Context context, View view, String styleConstant)
    {
        if(checkFontStyleConstant(styleConstant))
        {
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(Typeface.createFromAsset(context.getAssets(), styleConstant));
            }
        }
    }

    public static boolean checkFontStyleConstant(String styleConstant)
    {
        //first lets check that the style constants isn't empty or invalid
        if(styleConstant.isEmpty()&&styleConstant.equals(""))
        {
            styleConstant = DEFAULT_FONT;
            System.out.println(styleConstant);
        }
        else if(!styleConstant.equalsIgnoreCase(StyleConstants.GENERAL_APP_FONT))
        {

            return false;
        }
        return true;
    }

}
