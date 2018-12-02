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

public class Star extends Body{
    static Star SUN;
    static Star ALPHA_CENTAURI_A;
    static Star ALPHA_CENTAURI_B;
    static Star PROXIMA_CENTAURI;
    static Star WOLF_359;
    static Star SIRIUS_A;
    static Star SIRIUS_B;
    static Star RIGEL;
    static Star VY_CANIS_MAJORIS;
    static Star ETA_CARINAE_A;
    static Star CRAB_PULSAR;


    static Bitmap O_TYPE_BODY;
    static Bitmap B_TYPE_BODY;
    static Bitmap A_TYPE_BODY;
    static Bitmap F_TYPE_BODY;
    static Bitmap G_TYPE_BODY;
    static Bitmap K_TYPE_BODY;
    static Bitmap M_TYPE_BODY;
    static Bitmap O_TYPE_LIGHT;
    static Bitmap B_TYPE_LIGHT;
    static Bitmap A_TYPE_LIGHT;
    static Bitmap F_TYPE_LIGHT;
    static Bitmap G_TYPE_LIGHT;
    static Bitmap K_TYPE_LIGHT;
    static Bitmap M_TYPE_LIGHT;
    private double temperature;
    private double luminosity;
    private String type;
    private static Resources resource;
    public Star(){
        super();
    }

    @Override
    public void setMass(double m) {
        if(type.equals("main sequence star")){
            mass = m;
            setRadius(Universe.R * Math.pow(mass / Universe.M, 0.5));
        }else{
            double d = Calculate.density(this);
            mass = m;
            radius = Math.max(Math.pow((3 * mass) / (4 * Math.PI * d),1.0 / 3.0), 0);
        }
    }

    @Override
    public void setRadius(double r) {
        radius = r;
        if(getMass() < 0.43*Universe.M){
            setTemperature(Math.pow(0.23 * Math.pow(mass / Universe.M,2.3) * Math.pow(Universe.R,2) * Math.pow(5778,4) / Math.pow(radius,2),0.25));
        }else if(getMass() < 2*Universe.M){
            setTemperature(Math.pow(Math.pow(mass / Universe.M,4) * Math.pow(Universe.R,2) * Math.pow(5778,4) / Math.pow(radius,2),0.25));
        }else if(getMass() < 20*Universe.M){
            setTemperature(Math.pow(1.4 * Math.pow(mass / Universe.M,3.5) * Math.pow(Universe.R,2) * Math.pow(5778,4) / Math.pow(radius,2),0.25));
        }else{
            setTemperature(Math.pow(32000 * Math.pow(mass / Universe.M,1) * Math.pow(Universe.R,2) * Math.pow(5778,4) / Math.pow(radius,2),0.25));
        }
        luminosity = Calculate.luminosity(this);
    }

    @Override
    public int getFlag() {
        if(radius < Calculate.schwarzschildRadius(mass)){
            return EVENT_TO_BLACK_HOLE;
        }else {
            switch (type) {
                case "super giant star" :
                    if(mass < Universe.M*0.08){
                        return EVENT_TO_PLANET;
                    }else if(mass < Universe.M*30){
                        return EVENT_NOTHING;
                    }else{
                        return EVENT_STAR_COLLAPSE;
                    }
                case "giant star" :
                    if(mass < Universe.M*0.08){
                        return EVENT_TO_PLANET;
                    }else if(mass < Universe.M*30){
                        return EVENT_NOTHING;
                    }else{
                        return EVENT_STAR_COLLAPSE;
                    }
                case "main sequence star" :
                    if(mass < Universe.M*0.08){
                        return EVENT_TO_PLANET;
                    }else if(mass < Universe.M*150){
                        return EVENT_NOTHING;
                    }else{
                        return EVENT_STAR_COLLAPSE;
                    }
                case "white dwarf" :
                    if(mass < Universe.M * 2.5){
                        return EVENT_NOTHING;
                    }else{
                        return EVENT_HYPER_NOVA;
                    }
                case "neutron star" :
                    if(mass < Universe.M * 2.5){
                        return EVENT_NOTHING;
                    }else{
                        return EVENT_HYPER_NOVA;
                    }
                    default:
                        return EVENT_NOTHING;
            }
        }
    }

    @Override
    public void printInfo(Canvas canvas, Paint paint) {
        NumberFormat format = new DecimalFormat(("0.####"));
        canvas.drawText("type :" + type,0,paint.getTextSize(),paint);
        canvas.drawText("mass :" + format.format(getMass()/Universe.M) + " M",0,2*paint.getTextSize(),paint);
        canvas.drawText("radius :" + format.format(getRadius()/Universe.R) + " R",0,3*paint.getTextSize(),paint);
        canvas.drawText("location :" + format.format(location.getX()/Universe.AU) + ", " + format.format(location.getY()/Universe.AU) + " AU",0,4*paint.getTextSize(),paint);
        canvas.drawText("velocity :" + format.format(velocity.getX()) + ", " + format.format(velocity.getY()),0,5*paint.getTextSize(),paint);
        canvas.drawText("temperature : " + (int)(getTemperature()) + " K",0,6*paint.getTextSize(),paint);
        canvas.drawText("luminosity : " + format.format(getLuminosity() / Star.SUN.getLuminosity()) + " of Sun",0,7*paint.getTextSize(),paint);
    }

    @Override
    public void printBody(Canvas canvas, Vector observer, double sight, int width, int height, Paint paint) {
        int radius1 = (int)(getRadius()/sight + 1);
        int radius2 = (int)(Math.sqrt(getLuminosity()/(16*Math.PI*Math.pow(1000,4)*Universe.STEFAN_BOLTZMANN))/sight);
        int coordX = (int)((location.getX() - observer.getX())/sight) + width/2;
        int coordY = (int)((location.getY() - observer.getY())/sight) + height/2;
        double t = getTemperature();
        if(t < 3500){
            canvas.drawBitmap(Star.M_TYPE_BODY,new Rect(0,0,Star.M_TYPE_BODY.getWidth(),Star.M_TYPE_BODY.getHeight()),new Rect(coordX - radius1,coordY - radius1,coordX + radius1,coordY + radius1),paint);
            canvas.drawBitmap(Star.M_TYPE_LIGHT,new Rect(0,0,Star.M_TYPE_LIGHT.getWidth(),Star.M_TYPE_LIGHT.getHeight()),new Rect(coordX - radius2,coordY - radius2,coordX + radius2,coordY + radius2),paint);
        }else if(t < 4900){
            canvas.drawBitmap(Star.K_TYPE_BODY,new Rect(0,0,Star.K_TYPE_BODY.getWidth(),Star.K_TYPE_BODY.getHeight()),new Rect(coordX - radius1,coordY - radius1,coordX + radius1,coordY + radius1),paint);
            canvas.drawBitmap(Star.K_TYPE_LIGHT,new Rect(0,0,Star.K_TYPE_LIGHT.getWidth(),Star.K_TYPE_LIGHT.getHeight()),new Rect(coordX - radius2,coordY - radius2,coordX + radius2,coordY + radius2),paint);
        }else if(t < 6000){
            canvas.drawBitmap(Star.G_TYPE_BODY,new Rect(0,0,Star.G_TYPE_BODY.getWidth(),Star.G_TYPE_BODY.getHeight()),new Rect(coordX - radius1,coordY - radius1,coordX + radius1,coordY + radius1),paint);
            canvas.drawBitmap(Star.G_TYPE_LIGHT,new Rect(0,0,Star.G_TYPE_LIGHT.getWidth(),Star.G_TYPE_LIGHT.getHeight()),new Rect(coordX - radius2,coordY - radius2,coordX + radius2,coordY + radius2),paint);
        }else if(t < 7400){
            canvas.drawBitmap(Star.F_TYPE_BODY,new Rect(0,0,Star.F_TYPE_BODY.getWidth(),Star.F_TYPE_BODY.getHeight()),new Rect(coordX - radius1,coordY - radius1,coordX + radius1,coordY + radius1),paint);
            canvas.drawBitmap(Star.F_TYPE_LIGHT,new Rect(0,0,Star.F_TYPE_LIGHT.getWidth(),Star.F_TYPE_LIGHT.getHeight()),new Rect(coordX - radius2,coordY - radius2,coordX + radius2,coordY + radius2),paint);
        }else if(t < 10000){
            canvas.drawBitmap(Star.A_TYPE_BODY,new Rect(0,0,Star.A_TYPE_BODY.getWidth(),Star.A_TYPE_BODY.getHeight()),new Rect(coordX - radius1,coordY - radius1,coordX + radius1,coordY + radius1),paint);
            canvas.drawBitmap(Star.A_TYPE_LIGHT,new Rect(0,0,Star.A_TYPE_LIGHT.getWidth(),Star.A_TYPE_LIGHT.getHeight()),new Rect(coordX - radius2,coordY - radius2,coordX + radius2,coordY + radius2),paint);
        }else if(t < 28000){
            canvas.drawBitmap(Star.B_TYPE_BODY,new Rect(0,0,Star.B_TYPE_BODY.getWidth(),Star.B_TYPE_BODY.getHeight()),new Rect(coordX - radius1,coordY - radius1,coordX + radius1,coordY + radius1),paint);
            canvas.drawBitmap(Star.B_TYPE_LIGHT,new Rect(0,0,Star.B_TYPE_LIGHT.getWidth(),Star.B_TYPE_LIGHT.getHeight()),new Rect(coordX - radius2,coordY - radius2,coordX + radius2,coordY + radius2),paint);
        }else{
            canvas.drawBitmap(Star.O_TYPE_BODY,new Rect(0,0,Star.O_TYPE_BODY.getWidth(),Star.O_TYPE_BODY.getHeight()),new Rect(coordX - radius1,coordY - radius1,coordX + radius1,coordY + radius1),paint);
            canvas.drawBitmap(Star.O_TYPE_LIGHT,new Rect(0,0,Star.O_TYPE_LIGHT.getWidth(),Star.O_TYPE_LIGHT.getHeight()),new Rect(coordX - radius2,coordY - radius2,coordX + radius2,coordY + radius2),paint);
        }
    }

    public Star(double m, double r, double t){
        super(m,r);
        setTemperature(t);
        type = Calculate.getStarType(this);
    }

    public Star(double m, double r, double t, Vector l){
        super(m,r,l);
        setTemperature(t);
        type = Calculate.getStarType(this);
    }

    public Star(double m,double r, double t, Vector l, Vector v){
        super(m,r,l,v);
        setTemperature(t);
        type = Calculate.getStarType(this);
    }

    public Star(Planet ob){
        super(ob);
        setTemperature(2000);
        type = Calculate.getStarType(this);
    }

    public Star(Star ob){
        super(ob);
        setTemperature(ob.temperature);
        type = ob.type;
    }

    public void setTemperature(double t){
        temperature = t;
        luminosity = Calculate.luminosity(this);
    }

    public double getTemperature(){
        return temperature;
    }

    public double getLuminosity(){
        return luminosity;
    }

    public static void init(Resources res){
        resource = res;
        O_TYPE_BODY = BitmapFactory.decodeResource(res,R.drawable.o_type_body);
        B_TYPE_BODY = BitmapFactory.decodeResource(res,R.drawable.b_type_body);
        A_TYPE_BODY = BitmapFactory.decodeResource(res,R.drawable.a_type_body);
        F_TYPE_BODY = BitmapFactory.decodeResource(res,R.drawable.f_type_body);
        G_TYPE_BODY = BitmapFactory.decodeResource(res,R.drawable.g_type_body);
        K_TYPE_BODY = BitmapFactory.decodeResource(res,R.drawable.k_type_body);
        M_TYPE_BODY = BitmapFactory.decodeResource(res,R.drawable.m_type_body);

        O_TYPE_LIGHT = BitmapFactory.decodeResource(res,R.drawable.o_type_light);
        B_TYPE_LIGHT = BitmapFactory.decodeResource(res,R.drawable.b_type_light);
        A_TYPE_LIGHT = BitmapFactory.decodeResource(res,R.drawable.a_type_light);
        F_TYPE_LIGHT = BitmapFactory.decodeResource(res,R.drawable.f_type_light);
        G_TYPE_LIGHT = BitmapFactory.decodeResource(res,R.drawable.g_type_light);
        K_TYPE_LIGHT = BitmapFactory.decodeResource(res,R.drawable.k_type_light);
        M_TYPE_LIGHT = BitmapFactory.decodeResource(res,R.drawable.m_type_light);

        SUN = new Star(Universe.M, Universe.R, 5778);
        ALPHA_CENTAURI_A = new Star(1.1*Universe.M, 1.2234*Universe.R, 5790);
        ALPHA_CENTAURI_B = new Star(0.907*Universe.M, 0.8632*Universe.R, 5260);
        PROXIMA_CENTAURI = new Star(0.1221*Universe.M, 0.1542*Universe.R, 3042);
        WOLF_359 = new Star(0.09*Universe.M, 0.16*Universe.R, 2800);
        SIRIUS_A = new Star(2.02*Universe.M, 1.711*Universe.R, 9940);
        SIRIUS_B = new Star(1.01*Universe.M, 0.0084*Universe.R, 25200);
        RIGEL = new Star(18*Universe.M, 62*Universe.R, 12130);
        VY_CANIS_MAJORIS = new Star(17*Universe.M, 1420*Universe.R, 3490);
        ETA_CARINAE_A = new Star(120*Universe.M,200*Universe.R,30000);
        CRAB_PULSAR = new Star(1.4*Universe.M,0.00001438*Universe.R,1.6e6);
    }

    public Star(Parcel parcel){
        mass = parcel.readDouble();
        radius = parcel.readDouble();
        location = new Vector(parcel.readDouble(),parcel.readDouble());
        velocity = new Vector(parcel.readDouble(),parcel.readDouble());
        acceleration = new Vector(0,0);
        setTemperature(parcel.readDouble());
        type = parcel.readString();
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
        dest.writeDouble(temperature);
        dest.writeString(type);
    }

    public static final Parcelable.Creator<Star>CREATOR = new Parcelable.Creator<Star>(){
        @Override
        public Star createFromParcel(Parcel parcel) {
            return new Star(parcel);
        }

        @Override
        public Star[] newArray(int i) {
            return new Star[i];
        }
    };
}
