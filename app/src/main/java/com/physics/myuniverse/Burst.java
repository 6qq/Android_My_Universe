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

public class Burst extends Body{
    static Burst HYPER_NOVA;
    static Burst SUPER_NOVA;
    static Burst GAMMA_RAY_BURST;
    static Burst TSAR_BOMB;

    static Bitmap SUPER_NOVA_IMAGE;
    public double luminosityStart;
    public double luminosity;
    private static Resources resource;
    public Burst(){
        super();
    }

    @Override
    public void setMass(double m) {
        mass = m;
    }

    @Override
    public void setRadius(double r) {
        radius = r;
    }

    @Override
    public int getFlag() {
        if(radius < Calculate.schwarzschildRadius(mass)){
            return EVENT_TO_BLACK_HOLE;
        }else{
            return EVENT_NOTHING;
        }
    }

    @Override
    public void printInfo(Canvas canvas, Paint paint) {
        NumberFormat format = new DecimalFormat(("0.####"));
        canvas.drawText("mass :" + format.format(getMass()/Universe.M) + " M",0,paint.getTextSize(),paint);
        canvas.drawText("radius :" + format.format(getRadius()/Universe.R) + " R",0,2*paint.getTextSize(),paint);
        canvas.drawText("location :" + format.format(location.getX()/Universe.AU) + ", " + format.format(location.getY()/Universe.AU) + " AU",0,3*paint.getTextSize(),paint);
        canvas.drawText("velocity :" + format.format(velocity.getX()) + ", " + format.format(velocity.getY()),0,4*paint.getTextSize(),paint);
        canvas.drawText("luminosity : " + format.format(luminosity / Star.SUN.getLuminosity()) + " of Sun",0,5*paint.getTextSize(),paint);
    }

    @Override
    public void printBody(Canvas canvas, Vector observer, double sight, int width, int height, Paint paint) {
        int radius1 = (int)(getRadius()/sight + 1);
        int coordX = (int)((location.getX() - observer.getX())/sight) + width/2;
        int coordY = (int)((location.getY() - observer.getY())/sight) + height/2;
        paint.setAlpha((int)(Math.min(255,Math.pow(luminosityStart,0.25)*(luminosity / luminosityStart))));
        if(luminosityStart > 1e40){
            canvas.drawBitmap(Burst.SUPER_NOVA_IMAGE,new Rect(0,0,Burst.SUPER_NOVA_IMAGE.getWidth(),Burst.SUPER_NOVA_IMAGE.getHeight()),new Rect(coordX - radius1,coordY - radius1,coordX + radius1,coordY + radius1),paint);
        }else{
            if(luminosity < 1e15){
                canvas.drawBitmap(Star.K_TYPE_LIGHT,new Rect(0,0,Star.K_TYPE_LIGHT.getWidth(),Star.K_TYPE_LIGHT.getHeight()),new Rect(coordX - radius1,coordY - radius1,coordX + radius1,coordY + radius1),paint);
            }else if(luminosity < 1e20){
                canvas.drawBitmap(Star.G_TYPE_LIGHT,new Rect(0,0,Star.G_TYPE_LIGHT.getWidth(),Star.G_TYPE_LIGHT.getHeight()),new Rect(coordX - radius1,coordY - radius1,coordX + radius1,coordY + radius1),paint);
            }else if(luminosity < 1e25){
                canvas.drawBitmap(Star.F_TYPE_LIGHT,new Rect(0,0,Star.F_TYPE_LIGHT.getWidth(),Star.F_TYPE_LIGHT.getHeight()),new Rect(coordX - radius1,coordY - radius1,coordX + radius1,coordY + radius1),paint);
            }else if(luminosity < 1e30){
                canvas.drawBitmap(Star.A_TYPE_LIGHT,new Rect(0,0,Star.A_TYPE_LIGHT.getWidth(),Star.A_TYPE_LIGHT.getHeight()),new Rect(coordX - radius1,coordY - radius1,coordX + radius1,coordY + radius1),paint);
            }else if(luminosity < 1e35){
                canvas.drawBitmap(Star.B_TYPE_LIGHT,new Rect(0,0,Star.B_TYPE_LIGHT.getWidth(),Star.B_TYPE_LIGHT.getHeight()),new Rect(coordX - radius1,coordY - radius1,coordX + radius1,coordY + radius1),paint);
            }else{
                canvas.drawBitmap(Star.O_TYPE_LIGHT,new Rect(0,0,Star.O_TYPE_LIGHT.getWidth(),Star.O_TYPE_LIGHT.getHeight()),new Rect(coordX - radius1,coordY - radius1,coordX + radius1,coordY + radius1),paint);
            }
        }
        paint.setAlpha(255);
    }


    @Override
    void move(double expandRate, double timeSpeed) {
        //super.move(expandRate);
        double startRadius = radius;
        radius += Math.min(Universe.c, radius*25)*timeSpeed*0.025*0.01;
        luminosity *= ((startRadius*startRadius) / (radius*radius));

        double newLocX = location.getX()*expandRate;
        double newLocY = location.getY()*expandRate;
        location.setX((newLocX - location.getX())*0.025*timeSpeed + location.getX());
        location.setY((newLocY - location.getY())*0.025*timeSpeed + location.getY());
    }

    public Burst(double lum){
        super(Calculate.convertToMass(lum),Math.pow(lum,0.20));
        luminosity = lum;
        luminosityStart = luminosity;
    }

    public Burst(double lum, Vector l){
        super(Calculate.convertToMass(lum),Math.pow(lum,0.20),l);
        luminosity = lum;
        luminosityStart = luminosity;
    }

    public Burst(double lum, Vector l, Vector v){
        super(Calculate.convertToMass(lum),Math.pow(lum,0.20),l,v);
        luminosity = lum;
        luminosityStart = luminosity;
    }

    public Burst(Burst ob){
        super(ob);
        luminosity = ob.luminosity;
        luminosityStart = luminosity;
    }

    public static void init(Resources res){
        resource = res;
        SUPER_NOVA_IMAGE = BitmapFactory.decodeResource(res,R.drawable.supernova);
        HYPER_NOVA = new Burst(1e46);
        SUPER_NOVA = new Burst(2e44);
        GAMMA_RAY_BURST = new Burst(5e43);
        TSAR_BOMB = new Burst(2.1e17);
    }

    public Burst(Parcel parcel){
        mass = parcel.readDouble();
        radius = parcel.readDouble();
        location = new Vector(parcel.readDouble(),parcel.readDouble());
        velocity = new Vector(parcel.readDouble(),parcel.readDouble());
        acceleration = new Vector(0,0);
        luminosity = parcel.readDouble();
        luminosityStart = parcel.readDouble();
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
        dest.writeDouble(luminosity);
        dest.writeDouble(luminosityStart);
    }

    public static final Parcelable.Creator<Burst>CREATOR = new Parcelable.Creator<Burst>(){
        @Override
        public Burst createFromParcel(Parcel parcel) {
            return new Burst(parcel);
        }

        @Override
        public Burst[] newArray(int i) {
            return new Burst[i];
        }
    };
}
