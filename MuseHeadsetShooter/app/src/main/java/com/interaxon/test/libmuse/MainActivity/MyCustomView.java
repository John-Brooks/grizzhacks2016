package com.interaxon.test.libmuse.MainActivity;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import android.graphics.*;

import com.interaxon.test.libmuse.R;
import com.interaxon.test.libmuse.core_t;


/**
 * Created by Jbrooks on 3/20/2016.
 */
public class MyCustomView extends View {
    public core_t core;
    boolean bFirstDraw = true;
    private Canvas offscreen;
    Drawable dCrosshair;
    Drawable dTarget;
    Bitmap bCrosshair;


    public MyCustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCustomView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //We want the superclass to draw directly to the offscreen canvas so that we don't get an infinitely deep recursive call
        if (canvas == offscreen) {
            super.onDraw(offscreen);
        } else {
            if(bFirstDraw)
            {
                Rect rView = new Rect(0, 0, getWidth(),getHeight() );
                Rect rTarget = new Rect(0, 0, 250,250);
                core = new core_t(rView, rTarget);

                Resources res = getResources();
                dCrosshair = res.getDrawable(R.drawable.crosshair);
                dTarget = res.getDrawable(R.drawable.target);

                bFirstDraw = false;

            }

            core.run(); //tell the core to process the latest information
            float fTilt = (float)core.getMuseSTSTilt();
            Paint paint = new Paint();
            dTarget.setBounds((int)core.getTargetLeft(), (int)core.getTargetTop(), (int)core.getTargetLeft() + 250,(int)core.getTargetTop() + 250 );
            dTarget.draw(canvas);
            dCrosshair.setBounds((int)core.getGunLeft(), (int)core.getGunTop(), (int)core.getGunLeft()+250, (int)core.getGunTop()+250);
            dCrosshair.draw(canvas);
        }
    }
    @Override
    public void onAttachedToWindow ()
    {
        super.onAttachedToWindow ();

    }
}
