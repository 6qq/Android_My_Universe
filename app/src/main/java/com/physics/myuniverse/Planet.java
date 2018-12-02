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

public class Planet extends Body{
    private double temperature = 0;
    public float albedo = 0.4f;
    public int pictureId;
    private static Resources resource;

    static Planet MERCURY;
    static Planet VENUS;
    static Planet EARTH;
    static Planet MARS;
    static Planet JUPITER;
    static Planet SATURN;
    static Planet URANUS;
    static Planet NEPTUNE;

    static Planet MOON;
    static Planet PHOBOS;
    static Planet DEIMOS;
    static Planet GANYMEDE;
    static Planet CALLISTO;
    static Planet EUROPA;
    static Planet IO;
    static Planet TITAN;
    static Planet ENCELADUS;
    static Planet RHEA;
    static Planet DIONE;
    static Planet MIMAS;
    static Planet TETHYS;
    static Planet IAPETUS;
    static Planet OBERON;
    static Planet TITANIA;
    static Planet UMBRIEL;
    static Planet TRITON;
    static Planet CHARON;

    static Planet PLUTO;
    static Planet CERES;

    static Planet THE_DEATH_STAR;

    private static Bitmap BROWN_DWARF_IMAGE;
    private static Bitmap MERCURY_IMAGE;
    private static Bitmap VENUS_IMAGE;
    private static Bitmap EARTH_IMAGE;
    private static Bitmap MARS_IMAGE;
    private static Bitmap JUPITER_IMAGE;
    private static Bitmap SATURN_IMAGE;
    private static Bitmap URANUS_IMAGE;
    private static Bitmap NEPTUNE_IMAGE;
    private static Bitmap MOON_IMAGE;
    private static Bitmap PHOBOS_IMAGE;
    private static Bitmap DEIMOS_IMAGE;
    private static Bitmap GANYMEDE_IMAGE;
    private static Bitmap CALLISTO_IMAGE;
    private static Bitmap EUROPA_IMAGE;
    private static Bitmap IO_IMAGE;
    private static Bitmap TITAN_IMAGE;
    private static Bitmap ENCELADUS_IMAGE;
    private static Bitmap RHEA_IMAGE;
    private static Bitmap DIONE_IMAGE;
    private static Bitmap MIMAS_IMAGE;
    private static Bitmap TETHYS_IMAGE;
    private static Bitmap IAPETUS_IMAGE;
    private static Bitmap OBERON_IMAGE;
    private static Bitmap TITANIA_IMAGE;
    private static Bitmap UMBRIEL_IMAGE;
    private static Bitmap TRITON_IMAGE;
    private static Bitmap PLUTO_IMAGE;
    private static Bitmap CHARON_IMAGE;
    private static Bitmap CERES_IMAGE;
    private static Bitmap THE_DEATH_STAR_IMAGE;

    private static final int BROWN_DWARF_ID = 0;
    private static final int MERCURY_ID = 1;
    private static final int VENUS_ID = 2;
    private static final int EARTH_ID = 3;
    private static final int MARS_ID = 4;
    private static final int JUPITER_ID = 5;
    private static final int SATURN_ID = 6;
    private static final int URANUS_ID = 7;
    private static final int NEPTUNE_ID = 8;
    private static final int MOON_ID = 9;
    private static final int PHOBOS_ID = 10;
    private static final int DEIMOS_ID = 11;
    private static final int GANYMEDE_ID = 12;
    private static final int CALLISTO_ID = 13;
    private static final int EUROPA_ID = 14;
    private static final int IO_ID = 15;
    private static final int TITAN_ID = 16;
    private static final int ENCELADUS_ID = 17;
    private static final int RHEA_ID = 18;
    private static final int DIONE_ID = 19;
    private static final int MIMAS_ID = 20;
    private static final int TETHYS_ID = 21;
    private static final int IAPETUS_ID = 22;
    private static final int OBERON_ID = 23;
    private static final int TITANIA_ID = 24;
    private static final int UMBRIEL_ID = 25;
    private static final int TRITON_ID = 26;
    private static final int PLUTO_ID = 27;
    private static final int CHARON_ID = 28;
    private static final int CERES_ID = 29;
    private static final int THE_DEATH_STAR_ID = 30;

    public Planet(){
        super();
    }

    Planet(Planet ob){
        setMass(ob.getMass());
        setRadius(ob.getRadius());
        location = new Vector(ob.getLocation());
        velocity = new Vector(ob.getVelocity());
        albedo = ob.albedo;
        pictureId = ob.pictureId;
    }

    Planet(Star ob) {
        super(ob);
    }

    Planet(double m, double r){
        super(m,r);
    }

    public Planet(double m, double r, Vector l){
        super(m,r,l);
    }

    public Planet(double m,double r, Vector l, Vector v){
        super(m,r,l,v);
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
        }else if(mass > Universe.M*0.08){
            return EVENT_TO_STAR;
        }else{
            return EVENT_NOTHING;
        }
    }

    @Override
    public void printInfo(Canvas canvas, Paint paint) {
        NumberFormat format = new DecimalFormat(("0.####"));
        canvas.drawText("mass : " + format.format(getMass()/Universe.M_EARTH) + " M",0,paint.getTextSize(),paint);
        canvas.drawText("radius : " + format.format(getRadius()/Universe.R_EARTH) + " R",0,2*paint.getTextSize(),paint);
        canvas.drawText("location : " + format.format(location.getX()/Universe.AU) + ", " + format.format(location.getY()/Universe.AU) + " AU",0,3*paint.getTextSize(),paint);
        canvas.drawText("velocity(m) : " + format.format(velocity.getX()) + ", " + format.format(velocity.getY()),0,4*paint.getTextSize(),paint);
        canvas.drawText("temperature :" + (int)(getTemperature()) + " K",0,5*paint.getTextSize(),paint);
    }

    @Override
    public void printBody(Canvas canvas, Vector observer, double sight, int width, int height, Paint paint) {
        int radius = (int)(getRadius()/sight + 1);
        int coordX = (int)((location.getX() - observer.getX())/sight) + width/2;
        int coordY = (int)((location.getY() - observer.getY())/sight) + height/2;
        switch (pictureId) {
            case BROWN_DWARF_ID:
                canvas.drawBitmap(Planet.BROWN_DWARF_IMAGE, new Rect(0, 0, Planet.BROWN_DWARF_IMAGE.getWidth(), Planet.BROWN_DWARF_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case MERCURY_ID:
                canvas.drawBitmap(Planet.MERCURY_IMAGE, new Rect(0, 0, Planet.MERCURY_IMAGE.getWidth(), Planet.MERCURY_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case VENUS_ID:
                canvas.drawBitmap(Planet.VENUS_IMAGE, new Rect(0, 0, Planet.VENUS_IMAGE.getWidth(), Planet.VENUS_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case EARTH_ID:
                canvas.drawBitmap(Planet.EARTH_IMAGE, new Rect(0, 0, Planet.EARTH_IMAGE.getWidth(), Planet.EARTH_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case MARS_ID:
                canvas.drawBitmap(Planet.MARS_IMAGE, new Rect(0, 0, Planet.MARS_IMAGE.getWidth(), Planet.MARS_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case JUPITER_ID:
                canvas.drawBitmap(Planet.JUPITER_IMAGE, new Rect(0, 0, Planet.JUPITER_IMAGE.getWidth(), Planet.JUPITER_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case SATURN_ID:
                canvas.drawBitmap(Planet.SATURN_IMAGE, new Rect(0, 0, Planet.SATURN_IMAGE.getWidth(), Planet.SATURN_IMAGE.getHeight()), new Rect(coordX - 2 * radius, coordY - 2 * radius, coordX + 2 * radius, coordY + 2 * radius), paint);
                break;
            case URANUS_ID:
                canvas.drawBitmap(Planet.URANUS_IMAGE, new Rect(0, 0, Planet.URANUS_IMAGE.getWidth(), Planet.URANUS_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case NEPTUNE_ID:
                canvas.drawBitmap(Planet.NEPTUNE_IMAGE, new Rect(0, 0, Planet.NEPTUNE_IMAGE.getWidth(), Planet.NEPTUNE_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case MOON_ID:
                canvas.drawBitmap(Planet.MOON_IMAGE, new Rect(0, 0, Planet.MOON_IMAGE.getWidth(), Planet.MOON_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case PHOBOS_ID:
                canvas.drawBitmap(Planet.PHOBOS_IMAGE, new Rect(0, 0, Planet.PHOBOS_IMAGE.getWidth(), Planet.PHOBOS_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case DEIMOS_ID:
                canvas.drawBitmap(Planet.DEIMOS_IMAGE, new Rect(0, 0, Planet.DEIMOS_IMAGE.getWidth(), Planet.DEIMOS_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case GANYMEDE_ID:
                canvas.drawBitmap(Planet.GANYMEDE_IMAGE, new Rect(0, 0, Planet.GANYMEDE_IMAGE.getWidth(), Planet.GANYMEDE_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case CALLISTO_ID:
                canvas.drawBitmap(Planet.CALLISTO_IMAGE, new Rect(0, 0, Planet.CALLISTO_IMAGE.getWidth(), Planet.CALLISTO_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case EUROPA_ID:
                canvas.drawBitmap(Planet.EUROPA_IMAGE, new Rect(0, 0, Planet.EUROPA_IMAGE.getWidth(), Planet.EUROPA_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case IO_ID:
                canvas.drawBitmap(Planet.IO_IMAGE, new Rect(0, 0, Planet.IO_IMAGE.getWidth(), Planet.IO_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case TITAN_ID:
                canvas.drawBitmap(Planet.TITAN_IMAGE, new Rect(0, 0, Planet.TITAN_IMAGE.getWidth(), Planet.TITAN_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case ENCELADUS_ID:
                canvas.drawBitmap(Planet.ENCELADUS_IMAGE, new Rect(0, 0, Planet.ENCELADUS_IMAGE.getWidth(), Planet.ENCELADUS_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case RHEA_ID:
                canvas.drawBitmap(Planet.RHEA_IMAGE, new Rect(0, 0, Planet.RHEA_IMAGE.getWidth(), Planet.RHEA_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case DIONE_ID:
                canvas.drawBitmap(Planet.DIONE_IMAGE, new Rect(0, 0, Planet.DIONE_IMAGE.getWidth(), Planet.DIONE_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case MIMAS_ID:
                canvas.drawBitmap(Planet.MIMAS_IMAGE, new Rect(0, 0, Planet.MIMAS_IMAGE.getWidth(), Planet.MIMAS_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case TETHYS_ID:
                canvas.drawBitmap(Planet.TETHYS_IMAGE, new Rect(0, 0, Planet.TETHYS_IMAGE.getWidth(), Planet.TETHYS_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case IAPETUS_ID:
                canvas.drawBitmap(Planet.IAPETUS_IMAGE, new Rect(0, 0, Planet.IAPETUS_IMAGE.getWidth(), Planet.IAPETUS_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case OBERON_ID:
                canvas.drawBitmap(Planet.OBERON_IMAGE, new Rect(0, 0, Planet.OBERON_IMAGE.getWidth(), Planet.OBERON_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case TITANIA_ID:
                canvas.drawBitmap(Planet.TITANIA_IMAGE, new Rect(0, 0, Planet.TITANIA_IMAGE.getWidth(), Planet.TITANIA_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case UMBRIEL_ID:
                canvas.drawBitmap(Planet.UMBRIEL_IMAGE, new Rect(0, 0, Planet.UMBRIEL_IMAGE.getWidth(), Planet.UMBRIEL_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case TRITON_ID:
                canvas.drawBitmap(Planet.TRITON_IMAGE, new Rect(0, 0, Planet.TRITON_IMAGE.getWidth(), Planet.TRITON_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case PLUTO_ID:
                canvas.drawBitmap(Planet.PLUTO_IMAGE, new Rect(0, 0, Planet.PLUTO_IMAGE.getWidth(), Planet.PLUTO_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case CHARON_ID:
                canvas.drawBitmap(Planet.CHARON_IMAGE, new Rect(0, 0, Planet.CHARON_IMAGE.getWidth(), Planet.CHARON_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case CERES_ID:
                canvas.drawBitmap(Planet.CERES_IMAGE, new Rect(0, 0, Planet.CERES_IMAGE.getWidth(), Planet.CERES_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
            case THE_DEATH_STAR_ID:
                canvas.drawBitmap(Planet.THE_DEATH_STAR_IMAGE, new Rect(0, 0, Planet.THE_DEATH_STAR_IMAGE.getWidth(), Planet.THE_DEATH_STAR_IMAGE.getHeight()), new Rect(coordX - radius, coordY - radius, coordX + radius, coordY + radius), paint);
                break;
        }
    }


    Planet(Parcel parcel){
        mass = parcel.readDouble();
        radius = parcel.readDouble();
        location = new Vector(parcel.readDouble(),parcel.readDouble());
        velocity = new Vector(parcel.readDouble(),parcel.readDouble());
        acceleration = new Vector(parcel.readDouble(),parcel.readDouble());
        pictureId = parcel.readInt();
    }

    void setTemperature(double energy){
        temperature = Math.pow(energy / (Universe.STEFAN_BOLTZMANN*4*Math.PI*getRadius()*getRadius()), 0.25);
        double d = Calculate.density(this);
        mass -= Math.pow(temperature,4) * 0.025 * Universe.TIME_SPEED * 1e-4;
        if(mass > 1e6){
            radius = Math.max(Math.pow((3 * mass) / (4 * Math.PI * d),1.0 / 3.0), 0);
        }
    }

    double getTemperature(){
        return temperature;
    }

    void setPicture(Picture.Planet id){
        switch (id){
            case BROWN_DWARF :
                pictureId = BROWN_DWARF_ID;
                break;
            case MERCURY :
                pictureId = MERCURY_ID;
                break;
            case VENUS :
                pictureId = VENUS_ID;
                break;
            case EARTH :
                pictureId = EARTH_ID;
                break;
            case MARS :
                pictureId = MARS_ID;
                break;
            case JUPITER :
                pictureId = JUPITER_ID;
                break;
            case SATURN :
                pictureId = SATURN_ID;
                break;
            case URANUS :
                pictureId = URANUS_ID;
                break;
            case NEPTUNE :
                pictureId = NEPTUNE_ID;
                break;
            case MOON :
                pictureId = MOON_ID;
                break;
            case PHOBOS :
                pictureId = PHOBOS_ID;
                break;
            case DEIMOS :
                pictureId = DEIMOS_ID;
                break;
            case GANYMEDE :
                pictureId = GANYMEDE_ID;
                break;
            case CALLISTO :
                pictureId = CALLISTO_ID;
                break;
            case EUROPA :
                pictureId = EUROPA_ID;
                break;
            case IO :
                pictureId = IO_ID;
                break;
            case TITAN :
                pictureId = TITAN_ID;
                break;
            case ENCELADUS :
                pictureId = ENCELADUS_ID;
                break;
            case RHEA :
                pictureId = RHEA_ID;
                break;
            case DIONE :
                pictureId = DIONE_ID;
                break;
            case MIMAS :
                pictureId = MIMAS_ID;
                break;
            case TETHYS :
                pictureId = TETHYS_ID;
                break;
            case IAPETUS :
                pictureId = IAPETUS_ID;
                break;
            case OBERON :
                pictureId = OBERON_ID;
                break;
            case TITANIA :
                pictureId = TITANIA_ID;
                break;
            case UMBRIEL :
                pictureId = UMBRIEL_ID;
                break;
            case TRITON :
                pictureId = TRITON_ID;
                break;
            case PLUTO :
                pictureId = PLUTO_ID;
                break;
            case CHARON :
                pictureId = CHARON_ID;
                break;
            case CERES :
                pictureId = CERES_ID;
                break;
            case THE_DEATH_STAR :
                pictureId = THE_DEATH_STAR_ID;
                break;
        }
    }

    public static void init(Resources res){
        resource = res;
        BROWN_DWARF_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.brown_dwarf);
        MERCURY_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.mercury);
        VENUS_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.venus);
        EARTH_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.earth);
        MARS_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.mars);
        JUPITER_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.jupiter);
        SATURN_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.saturn);
        URANUS_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.uranus);
        NEPTUNE_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.neptune);
        MOON_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.moon);
        PHOBOS_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.phobos);
        DEIMOS_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.deimos);
        GANYMEDE_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.ganymede);
        CALLISTO_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.callisto);
        EUROPA_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.europa);
        IO_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.io);
        TITAN_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.titan);
        ENCELADUS_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.enceladus);
        RHEA_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.rhea);
        DIONE_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.dione);
        MIMAS_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.mimas);
        TETHYS_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.tethys);
        IAPETUS_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.iapetus);
        OBERON_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.oberon);
        TITANIA_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.titania);
        UMBRIEL_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.umbriel);
        TRITON_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.triton);
        PLUTO_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.pluto);
        CHARON_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.charon);
        CERES_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.ceres);
        THE_DEATH_STAR_IMAGE = BitmapFactory.decodeResource(resource,R.drawable.the_death_star);

        MERCURY = new Planet(3.3e23, 2440e3);
        MERCURY.setPicture(Picture.Planet.MERCURY);
        VENUS = new Planet(4.86e24, 6052e3);
        VENUS.setPicture(Picture.Planet.VENUS);
        EARTH = new Planet(Universe.M_EARTH, Universe.R_EARTH);
        EARTH.setPicture(Picture.Planet.EARTH);
        MARS = new Planet(6.41e23, 3390e3);
        MARS.setPicture(Picture.Planet.MARS);
        JUPITER = new Planet(1.89e27, 69911e3);
        JUPITER.setPicture(Picture.Planet.JUPITER);
        SATURN = new Planet(5.68e26, 58232e3);
        SATURN.setPicture(Picture.Planet.SATURN);
        URANUS = new Planet(8.68e25, 25362e3);
        URANUS.setPicture(Picture.Planet.URANUS);
        NEPTUNE = new Planet(1.024e26,24622e3);
        NEPTUNE.setPicture(Picture.Planet.NEPTUNE);
        MOON = new Planet(7.34e22,1737e3);
        MOON.setPicture(Picture.Planet.MOON);
        PHOBOS = new Planet(1.6e16,13e3);
        PHOBOS.setPicture(Picture.Planet.PHOBOS);
        DEIMOS = new Planet(1.47e15,6e3);
        DEIMOS.setPicture(Picture.Planet.DEIMOS);
        GANYMEDE = new Planet(1.48e23,2634e3);
        GANYMEDE.setPicture(Picture.Planet.GANYMEDE);
        CALLISTO = new Planet(1.08E23,2410e3);
        CALLISTO.setPicture(Picture.Planet.CALLISTO);
        EUROPA = new Planet(4.79e22,1560e3);
        EUROPA.setPicture(Picture.Planet.EUROPA);
        IO = new Planet(8.93e22,1821e3);
        IO.setPicture(Picture.Planet.IO);
        TITAN = new Planet(1.34e23,2576e3);
        TITAN.setPicture(Picture.Planet.TITAN);
        ENCELADUS = new Planet(1.08e20,252e3);
        ENCELADUS.setPicture(Picture.Planet.ENCELADUS);
        RHEA = new Planet(2.49e21,764e3);
        RHEA.setPicture(Picture.Planet.RHEA);
        DIONE = new Planet(1.09e21,561e3);
        DIONE.setPicture(Picture.Planet.DIONE);
        MIMAS = new Planet(3.74e19,198e3);
        MIMAS.setPicture(Picture.Planet.MIMAS);
        TETHYS = new Planet(6.17e20,531e3);
        TETHYS.setPicture(Picture.Planet.TETHYS);
        IAPETUS = new Planet(1.80e21,734e3);
        IAPETUS.setPicture(Picture.Planet.IAPETUS);
        OBERON = new Planet(3.03e21,761e3);
        OBERON.setPicture(Picture.Planet.OBERON);
        TITANIA = new Planet(3.52e21,788e3);
        TITANIA.setPicture(Picture.Planet.TITANIA);
        UMBRIEL = new Planet(1.27e21,585e3);
        UMBRIEL.setPicture(Picture.Planet.UMBRIEL);
        TRITON = new Planet(2.14e22,1353e3);
        TRITON.setPicture(Picture.Planet.TRITON);
        PLUTO = new Planet(1.30e22,1188e3);
        PLUTO.setPicture(Picture.Planet.PLUTO);
        CHARON = new Planet(1.58e21,606e3);
        CHARON.setPicture(Picture.Planet.CHARON);
        CERES = new Planet(9.39e20,473e3);
        CERES.setPicture(Picture.Planet.CERES);
        THE_DEATH_STAR = new Planet(5.09e17,120e3);
        THE_DEATH_STAR.setPicture(Picture.Planet.THE_DEATH_STAR);
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
        dest.writeDouble(acceleration.getX());
        dest.writeDouble(acceleration.getY());
        dest.writeInt(pictureId);
    }

    public static final Parcelable.Creator<Planet>CREATOR = new Parcelable.Creator<Planet>(){
        @Override
        public Planet createFromParcel(Parcel parcel) {
            return new Planet(parcel);
        }

        @Override
        public Planet[] newArray(int i) {
            return new Planet[i];
        }
    };
}
