package com.interaxon.test.libmuse; /**
 * Created by Jbrooks on 3/19/2016.
 */

import java.util.*;
import java.lang.System;
import java.lang.String;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.locks.Lock;
import android.graphics.Rect;

import java.lang.Math;


class Gun_t{
    public int iAimPointX, iAimPointY;
    public int iTop, iLeft;
    static int iPointOffsetX = 0;
    static int iPointOffsetY = 0;

    Gun_t(Rect rScreenIn)
    {
        int x = 0;
        int y = 0;
        iTop = y;
        iLeft = x;
        iAimPointX = 125;
        iAimPointY = 125;
    }
    void Move(long x, long y)
    {
        iLeft += x;
        iTop += y;
    }
    int getiAimPointX()
    {
        return iLeft + 125;
    }
    int getiAimPointY()
    {
        return iTop + 125;
    }

};

class Muse_t{
    public double dSTSTilt;
    public double dFBTilt;
    public boolean bJawClench;
    public double dConcentration;

    public Muse_t ()
    {
        dSTSTilt = 0;
        dFBTilt = 0;
        bJawClench = false;
        dConcentration = .5;
    }

};

class Target{
    int height;
    int width;
    int top;
    int left;
    float centerpointx;
    float centerpointy;
    boolean destroyed;
    Rect rWindow;

    public Target(Rect rTargetIn, Rect rWindowIn){
        width = rTargetIn.width();
        height = rTargetIn.height();
        top = rTargetIn.top;
        left = rTargetIn.left;
        centerpointx = rTargetIn.centerX();
        centerpointy = rTargetIn.centerY();
        rWindow = new Rect(rWindowIn);
        randomlySpawnInRect();
    }

    public double getHitDistance(int x, int y)
    {
        double distance = ((y-centerpointy) * (y-centerpointy)) + ((x-centerpointx)*(x-centerpointx));
        distance = Math.sqrt(distance);

        if(distance >width/2)  //*radius*)
            return -1;
        else
            return distance;

    }

    public void randomlySpawnInRect()
    {
        Random rand = new Random();
        top = rand.nextInt( rWindow.bottom - height );
        left = rand.nextInt( rWindow.right - width);
        centerpointx = left + 125;
        centerpointy = top + 125;
    }



}

public class core_t {
    //Constants
    final static double FBTILTCONST = .00001;
    final static double STSTILTCONST = .00001;
    final static int FIGURE8PERIOD = 4000;
    final static int iGameTimeSeconds = 60;

    //objects
    public Muse_t muse;
    private Gun_t gun;

    //private members
    private long lLastUpdateTime =0;
    private long lGameStartTime = 0;
    private boolean bCurrentlyClenched = false;
    private double dLastHitDistance = 0;
    private long lLastClenchTime = 0;
    private Target target;
    Rect rScreen;

    //public members
    public boolean bShotResultWaiting = false;
    public long lLastHitScore = -1;
    public boolean bGameOver = false;
    public long lGameTimeRemaining = 60000;
    public String sLastScore;
    public long lastX = -1;
    public long lastY = -1;
    public long lTotalScore = 0;

    public core_t(Rect rScreenIn, Rect rTarget)
    {
        lGameStartTime = System.currentTimeMillis();  //getsystemtime
        lLastUpdateTime = lGameStartTime;  //getsystemtime
        rScreen = rScreenIn;
        target = new Target(rTarget, rScreenIn);
        muse = new Muse_t();
        gun = new Gun_t(rScreenIn);
    }

    public void run()
    {
        long lCurrentSystemTime = System.currentTimeMillis();
        lGameTimeRemaining = (iGameTimeSeconds * 1000) - (lCurrentSystemTime - lGameStartTime);
        if(lGameTimeRemaining < 0)
            bGameOver = true;

        if(bGameOver)


        if(bCurrentlyClenched && (lCurrentSystemTime - lLastClenchTime) > 300) {
            muse.bJawClench = false;
            bCurrentlyClenched = false;
        }
        checkForGunShot();
        updateGunPosition(lLastUpdateTime, lCurrentSystemTime);
    }

    public void updateMuseSTSTilt(double dTiltSideToSide)
    {
        muse.dSTSTilt = dTiltSideToSide;
    }
    public void updateMuseFBTilt(double dTiltForwardBackward)
    {
        muse.dFBTilt = dTiltForwardBackward;
    }
    public void updateMuseJawClench(boolean bJawClench)
    {
        muse.bJawClench = bJawClench;
    }
    public void updateMuseConcentration(double dConcentration)
    {
        muse.dConcentration = dConcentration;
    }

    public double getMuseSTSTilt()
    {
        return muse.dSTSTilt;
    }

    public long getGunLeft()
    {
        return gun.iLeft;
    }
    public long getGunTop()
    {
        return gun.iTop;
    }

    public long getTargetTop()
    {
        return target.top;
    }
    public long getTargetLeft()
    {
        return target.left;
    }

    private boolean checkForGunShot()
    {
        if(!bCurrentlyClenched && muse.bJawClench)
        {
            bShotResultWaiting = true;
            double dDistanceFromCenter = target.getHitDistance(gun.getiAimPointX(),gun.getiAimPointY());

            if(dDistanceFromCenter > 0)
            {
                dLastHitDistance = dDistanceFromCenter;
                lLastHitScore = Math.round(((target.width / 2) - dLastHitDistance) * 10);
                lTotalScore += lLastHitScore;
                sLastScore = String.valueOf(lLastHitScore);
                lastX = gun.getiAimPointX();
                lastY = gun.getiAimPointY();
                target.randomlySpawnInRect();
            }
            else
            {
                dLastHitDistance = -1;
                lLastHitScore = -1;
            }

            bCurrentlyClenched = true;
            return true;
        }
        return false;
    }
    private void updateGunPosition(long lLastTime, long lTimeNow)
    {
        //***************Figure 8 Logic (Sway)*********************

        double dRadius = 5.0;

        double dCurrentPositionInPeriod = (lLastTime % FIGURE8PERIOD) / FIGURE8PERIOD;
        double dLastPositionInPeriod = (lTimeNow % FIGURE8PERIOD) / FIGURE8PERIOD;

        double dThisTimePosAsRadian = getRadianFromPercentage(dCurrentPositionInPeriod);
        double dLastTimePosAsRadian = getRadianFromPercentage(dLastPositionInPeriod);

        double dOldX, dNewX, dOldY, dNewY;

        dNewX = Math.cos(dThisTimePosAsRadian) * (dRadius * muse.dConcentration);
        dOldX = Math.cos(dLastTimePosAsRadian) * (dRadius * muse.dConcentration);

        dNewY = Math.sin(dThisTimePosAsRadian) * (dRadius * muse.dConcentration);
        dOldY = Math.sin(dLastTimePosAsRadian) * (dRadius * muse.dConcentration);

        double swayXTranslation = dNewX - dOldX;
        double swayYTranslation = dNewY - dOldY;

        //***************Accelerometer Logic**********************
        long lTimeDifference = lTimeNow - lLastTime;
        double accelXTranslation = STSTILTCONST * muse.dSTSTilt * lTimeDifference;
        double accelYTranslation = FBTILTCONST * muse.dFBTilt * lTimeDifference;

        //***************Apply the change**********************

        gun.Move(Math.round(swayXTranslation + accelXTranslation), Math.round(swayYTranslation + accelYTranslation));

        if(gun.iLeft < 0)
            gun.iLeft = 0;
        if(gun.iLeft + 250 > rScreen.right)
            gun.iLeft = rScreen.right - 250;
        if(gun.iTop < 0)
            gun.iTop = 0;
        if(gun.iTop + 250 > rScreen.bottom)
            gun.iTop = rScreen.bottom - 250;

    }
    private double getRadianFromPercentage(double dPercentage)
    {
        //There are different equations necessary in order to generate the figure 8 effect
        if(dPercentage > 0.75)//Last quarter
        {
            double dNewPercentage = (dPercentage -  0.75) * 4;
            return Math.PI + (Math.PI * dNewPercentage);
        }
        else if(dPercentage > 0.25) //middle half
        {
            double dNewPercentage = (dPercentage -  0.25) * 2;
            return (2*Math.PI) - ((2*Math.PI) * dNewPercentage);
        }
        else //First Quarter
        {
            double dNewPercentage = dPercentage * 4;
            return Math.PI * dNewPercentage;
        }
    }

}
