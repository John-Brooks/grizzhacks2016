/**
 * Created by Rohit on 3/19/2016.
 */
import java.lang.Math;

public class Target{
    int height;
    int width;
    int top;
    int left;
    float centerpointx;
    float centerpointy;
    boolean destoryed;

    public Target( int x, int y, int w, int h){
        width = w;
        height = h;
        top = y;
        left = x;
        centerpointx = x + w/2;
        centerpointy = y + h/2;
    }


    double getHitDistance(int x, int y)
    {
        double distance = ((y-centerpointy) * (y-centerpointy)) + ((x-centerpointx)*(x-centerpointx));
        distance = Math.sqrt(distance);

        if(distance >width/2)  //*radius*)
            return -1;
        else
            return distance;





    }



}

