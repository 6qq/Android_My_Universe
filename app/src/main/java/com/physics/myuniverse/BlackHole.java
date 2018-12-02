package com.physics.myuniverse;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BlackHole extends Body{
    static BlackHole SAGITARRUS_A;
    static BlackHole NGC_1277;
    static BlackHole S5_0014_81;
    static BlackHole GRO_1655_40;
    public static Bitmap PICTURE;

    public BlackHole(double m){
        super();
        setMass(m);
    }

    public BlackHole(double m, Vector l){
        super();
        setMass(m);
        location = l;
    }

    public BlackHole(double m, Vector l, Vector v){
        super();
        setMass(m);
        location = l;
        velocity = v;
    }

    public BlackHole(Body ob){
        super();
        setMass(ob.getMass());
        location = new Vector(ob.location.getX(),ob.location.getY());
        velocity = new Vector(ob.velocity.getX(),ob.velocity.getY());
    }

    public BlackHole(Parcel parcel){
        mass = parcel.readDouble();
        radius = parcel.readDouble();
        location = new Vector(parcel.readDouble(),parcel.readDouble());
        velocity = new Vector(parcel.readDouble(),parcel.readDouble());
        acceleration = new Vector(0,0);
    }

    public static void init(Resources res){
        PICTURE = BitmapFactory.decodeResource(res,R.drawable.black_hole);
        SAGITARRUS_A = new BlackHole(4.1e6*Universe.M);
        NGC_1277 = new BlackHole(121.4e9*Universe.M);
        S5_0014_81 = new BlackHole(40e9*Universe.M);
        GRO_1655_40 = new BlackHole(6.3*Universe.M);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeDouble(mass);
        dest.writeDouble(radius);
        dest.writeDouble(location.getX());
        dest.writeDouble(location.getY());
        dest.writeDouble(velocity.getX());
        dest.writeDouble(velocity.getY());
    }

    public static final Parcelable.Creator<BlackHole>CREATOR = new Parcelable.Creator<BlackHole>(){
        @Override
        public BlackHole createFromParcel(Parcel parcel) {
            return new BlackHole(parcel);
        }

        @Override
        public BlackHole[] newArray(int i) {
            return new BlackHole[i];
        }
    };

    @Override
    public void setMass(double m) {
        mass = m;
        radius = Calculate.schwarzschildRadius(mass);
    }

    @Override
    public void setRadius(double r) {
        radius = r;
        mass = Calculate.schwarzschildMass(radius);
    }

    @Override
    public int getFlag() {
        return EVENT_NOTHING;
    }

    @Override
    public void printInfo(Canvas canvas, Paint paint) {
        NumberFormat format = new DecimalFormat(("0.####"));
        canvas.drawText("mass :" + format.format(getMass()/Universe.M) + " M",0,paint.getTextSize(),paint);
        canvas.drawText("radius :" + format.format(getRadius()/Universe.R) + " R",0,2*paint.getTextSize(),paint);
        canvas.drawText("location :" + format.format(location.getX()/Universe.AU) + ", " + format.format(location.getY()/Universe.AU) + " AU",0,3*paint.getTextSize(),paint);
        canvas.drawText("velocity :" + format.format(velocity.getX()) + ", " + format.format(velocity.getY()),0,4*paint.getTextSize(),paint);
    }

    @Override
    public void printBody(Canvas canvas, Vector observer, double sight, int width, int height, Paint paint) {
        int radius = (int)(1.5*getRadius()/sight + 2);
        int coordX = (int)((location.getX() - observer.getX())/sight) + width/2;
        int coordY = (int)((location.getY() - observer.getY())/sight) + height/2;
        canvas.drawBitmap(BlackHole.PICTURE,new Rect(0,0,BlackHole.PICTURE.getWidth(),BlackHole.PICTURE.getHeight()),new Rect(coordX - radius,coordY - radius,coordX + radius,coordY + radius),paint);
    }


}
