package com.unlimitec.porschetower.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.unlimitec.porschetower.R;

import java.util.Random;

/**
 * Created by buddy on 9/8/2016.
 */
public class ScenarioView extends ImageView {
    Bitmap city1;
    Bitmap city2;
    Bitmap city3;

    Bitmap mCloud1;
    Bitmap mCloud2;
    Bitmap mCloud3;

    private class CloudPosition{
        int BitmapId;
        int mCloudX;
        int mCloudY;
    }

    CloudPosition[] clouds;

    private static final int MAX_CLOUDS = 6;

    private int FRAME_RATE = 30;
    private Handler mHandler;
    private int mSpeed1 = 10;
    private int mSpeed2 = mSpeed1 / 2;
    private int mBGFarMoveX = 10;
    private int mBGNearMoveX = 10;

    public ScenarioView(Context context, AttributeSet attrs){
        super(context, attrs);
        setBackgroundResource(R.mipmap.setting_background);
        mHandler = new Handler();
        clouds = new CloudPosition[MAX_CLOUDS];
    }

    private Runnable r = new Runnable() {

        @Override
        public void run() {
            invalidate();
        }
    };

    public void setCity1Bitmap(Bitmap city){
        city1 = city;
    }

    public void setCity2Bitmap(Bitmap city){
        city2 = city;
    }

    public void setCity3Bitmap(Bitmap city){
        city3 = city;
    }

    private void generateCloudPositions() {
        int rowSeparator = getHeight() / 2;
        Random r = new Random(System.currentTimeMillis());
        int minY = 0;
        int maxY = rowSeparator;
        int minX = 0;
        int maxX = getWidth();

        // Generate 1st row
        int y = r.nextInt(maxY - minY + 1) + minY;
        int x = r.nextInt(maxX - minX + 1) + minX;
        int cloudId = r.nextInt(3);
        setupCloud(0, x, y, cloudId);

        y = r.nextInt(maxY - minY + 1) + minY;
        x = r.nextInt(maxX - minX + 1) + minX;
        cloudId = r.nextInt(3);
        setupCloud(1, x, y, cloudId);

        y = r.nextInt(maxY - minY + 1) + minY;
        x = r.nextInt(maxX - minX + 1) + minX;
        cloudId = r.nextInt(3);
        setupCloud(2, x, y, cloudId);

        minY = rowSeparator;
        maxY = getHeight();
        // Generate 2nd row
        y = r.nextInt(maxY - minY + 1) + minY;
        x = r.nextInt(maxX - minX + 1) + minX;
        cloudId = r.nextInt(3);
        setupCloud(3, x, y, cloudId);

        y = r.nextInt(maxY - minY + 1) + minY;
        x = r.nextInt(maxX - minX + 1) + minX;
        cloudId = r.nextInt(3);
        setupCloud(4, x, y, cloudId);

        y = r.nextInt(maxY - minY + 1) + minY;
        x = r.nextInt(maxX - minX + 1) + minX;
        cloudId = r.nextInt(3);
        setupCloud(5, x, y, cloudId);
    }

    public void setCloudsBitmaps(Bitmap cloud1, Bitmap cloud2, Bitmap cloud3){
        mCloud1 = cloud1;
        mCloud2 = cloud2;
        mCloud3 = cloud3;
        generateCloudPositions();
    }

    private void setupCloud(int cloudNum, int x, int y, int cloudId){
        CloudPosition cp = new CloudPosition();
        cp.mCloudX = x;
        cp.mCloudY = y;
        cp.BitmapId = cloudId;
        clouds[cloudNum] = cp;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mSpeed1 = mCallbacks.getSpeed();
        mSpeed2 = mSpeed1 / 2;
        mBGFarMoveX = mBGFarMoveX - mSpeed1;
        // decrement the near background
        mBGNearMoveX = mBGNearMoveX - mSpeed1;

        if(city1 != null && city2 != null && city3 != null){
            if(mCloud1 != null && mCloud2 != null && mCloud3 != null){
                drawClouds(canvas);
            }
            drawLandscape(canvas);
        }

//        if(mCar != null){
//            canvas.drawBitmap(mCar, mCarX, mCarY, null);
//        }
//
//        if(mWheel != null){
//            drawWheels(canvas);
//        }
//
//        if(mBridge != null){
//            drawBridge(canvas);
//        }

        mHandler.postDelayed(r, FRAME_RATE);
    }

    private void drawLandscape(Canvas canvas) {
        int currCity = 0;
        // calculate the wrap factor for matching image draw
        int newFarX = city1.getWidth() + (int)mBGFarMoveX;
        // if we have scrolled all the way, reset to start
        int bgY = getHeight() - city1.getHeight();

//        if(mSpeed1 > 0 && newFarX <= 0){
//            mBGFarMoveX = 0;
//            increseLandscape();
//        }

//        if(mSpeed1 < 0 && mBGFarMoveX >= getWidth()){
//            mBGFarMoveX = (mBGFarMoveX - city1.getWidth());
//            decreseLandscape();
//        }

        if(newFarX >= 0 && newFarX <= getWidth()){
            switch (currCity) {
                case 0:
                    canvas.drawBitmap(city1, mBGFarMoveX, bgY, null);
                    canvas.drawBitmap(city2, newFarX, bgY, null);
                    break;
                case 1:
                    canvas.drawBitmap(city2, mBGFarMoveX, bgY, null);
                    canvas.drawBitmap(city3, newFarX, bgY, null);
                    break;
                case 2:
                    canvas.drawBitmap(city3, mBGFarMoveX, bgY, null);
                    canvas.drawBitmap(city1, newFarX, bgY, null);
                    break;
            }
        }
        if(mBGFarMoveX >= 0 && mBGFarMoveX <= getWidth()){
            switch (currCity) {
                case 0:
                    canvas.drawBitmap(city3, mBGFarMoveX - city3.getWidth(), bgY, null);
                    canvas.drawBitmap(city1, mBGFarMoveX, bgY, null);
                    break;
                case 1:
                    canvas.drawBitmap(city1, mBGFarMoveX - city1.getWidth(), bgY, null);
                    canvas.drawBitmap(city2, mBGFarMoveX, bgY, null);
                    break;
                case 2:
                    canvas.drawBitmap(city2, mBGFarMoveX - city2.getWidth(), bgY, null);
                    canvas.drawBitmap(city3, mBGFarMoveX, bgY, null);
                    break;
            }
        }
        if(mBGFarMoveX <= 0 && newFarX >= getWidth()){
            switch (currCity) {
                case 0:
                    canvas.drawBitmap(city1, mBGFarMoveX, bgY, null);
                    break;
                case 1:
                    canvas.drawBitmap(city2, mBGFarMoveX, bgY, null);
                    break;
                case 2:
                    canvas.drawBitmap(city3, mBGFarMoveX, bgY, null);
                    break;
            }
        }
    }

    private void drawClouds(Canvas canvas) {
        int width = getWidth();
        for(int i = 0; i < MAX_CLOUDS; i++){
            Bitmap cloud;
            clouds[i].mCloudX = clouds[i].mCloudX - (int)mSpeed2;
            switch (clouds[i].BitmapId) {
                case 0:
                    cloud = mCloud1;
                    break;
                case 1:
                    cloud = mCloud2;
                    break;
                case 2:
                    cloud = mCloud3;
                    break;
                default:
                    cloud = mCloud1;
                    break;
            }
            int cloudX1 = clouds[i].mCloudX;
            int cloudX2 = cloudX1 + cloud.getWidth();

            if (cloudX2 <= 0 && mSpeed2 > 0) {
                clouds[i].mCloudX = clouds[i].mCloudX + (5 * cloud.getWidth());
                cloudX1 = clouds[i].mCloudX;
                cloudX2 = cloudX1 + cloud.getWidth();
            }
            if (cloudX1 >= width && mSpeed2 < 0) {
                clouds[i].mCloudX = clouds[i].mCloudX - (5 * cloud.getWidth());
                cloudX1 = clouds[i].mCloudX;
                cloudX2 = cloudX1 + cloud.getWidth();
            }
            if(cloudX1 < width && cloudX2 > 0){
                canvas.drawBitmap(cloud, clouds[i].mCloudX, clouds[i].mCloudY, null);
            }
        }

    }
}