package com.ashrafcoder.astrofacts.utils;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Environment;

import java.io.File;

/**
 * Created by Ashraf-XCODER on 2/25/2017.
 */

public class DrawableUtils {


    public void CreateDrawable(){
        //A Drawable with a color gradient for buttons, backgrounds, etc.
        //It can be defined in an XML file with the <shape> element.

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {});

        float[] values = { 0.2f, 0.2f, 0.2f, 0.2f };
        gradientDrawable.setCornerRadii(values);

        RectShape shape = new RectShape();


        ShapeDrawable shapeDrawable = new ShapeDrawable();


        // Environment.getRootDirectory() is a fancy way of saying ANDROID_ROOT or "/system".
        File rootDirectory = Environment.getRootDirectory();
        final File favFile = new File(rootDirectory, "etc/favorites.xml");

    }


    //View.setBackgroundResource(R.drawable.backgradient)

    /*
        Drawable
    */


}
