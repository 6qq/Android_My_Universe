package com.physics.myuniverse;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class Universe extends RelativeLayout implements Runnable{
    static double G = 6.674e-11;
    static final double M = 1.988e30;
    static final double R = 6.957e8;
    static final double M_EARTH = 5.9722e24;
    static final double R_EARTH = 6.371e6;
    static final double c = 2.99792458e8;
    static final double AU = 149597871e3;
    static final double TNT = 4.184e9;
    static final double STEFAN_BOLTZMANN = 5.67e-8;
    static final double CHANDRASEKHAR_MASS = 1.4*M;
    static double EXPAND_RATE = 1;
    static double TIME_SPEED = 1;
    Button create, pause, settings;

    ArrayList<Body> bodies = new ArrayList<Body>();
    ArrayList<Planet>planets = new ArrayList<>();
    ArrayList<Star>stars = new ArrayList<>();
    ArrayList<BlackHole>blackholes = new ArrayList<>();
    ArrayList<Burst>bursts = new ArrayList<>();
    Vector god = new Vector(0,0);
    Body selectedBody = null;
    Paint paint = new Paint();
    Paint fontPaint = new Paint();
    Handler handler = new Handler();
    double sight = 1;
    boolean isPaused = false;
    boolean manageMod = false;


    Universe(Context c){
        super(c);
        paint.setColor(Color.RED);
        fontPaint.setColor(Color.WHITE);
        fontPaint.setTextSize(30);
    }

    Universe(Context c, AttributeSet attr){
        super(c,attr);
        paint.setColor(Color.RED);
        fontPaint.setColor(Color.WHITE);
        fontPaint.setTextSize(30);
    }

    void addBody(Body ob) {
        bodies.add(ob);
        if(ob instanceof Planet && !planets.contains(ob)){
            planets.add((Planet)ob);
        }else if(ob instanceof Star && !stars.contains(ob)){
            stars.add((Star)ob);
        }else if(ob instanceof BlackHole && !blackholes.contains(ob)){
            blackholes.add((BlackHole)ob);
        }else if(ob instanceof Burst && !bursts.contains(ob)){
            bursts.add((Burst)ob);
        }
    }

    void addStruct(AstronomicalStruct struct){
        for(Body ob : struct.getBodies()){
            addBody(ob);
        }
        for(AstronomicalStruct st : struct.getStructs()){
            addStruct(st);
        }
    }

    void removeBody(Body ob) {
        if(ob == selectedBody){
            selectedBody = null;
            create.setText("CREATE");
            manageMod = false;
        }
        bodies.remove(ob);
        if(ob instanceof Planet){
            planets.remove(ob);
        }else if(ob instanceof Star){
            stars.remove(ob);
        }else if(ob instanceof BlackHole){
            blackholes.remove(ob);
        }else if(ob instanceof Burst){
            bursts.remove(ob);
        }
    }

    void doPhysics() {
        Body ob1,ob2;
        for(int i = 0;i < bodies.size();i++) {
            for(int j = i + 1;j < bodies.size();j++) {
                ob1 = bodies.get(i);
                ob2 = bodies.get(j);
                if(ob1.distance(ob2).getSize() < ob1.getRadius() + ob2.getRadius()) {
                    if(ob1 instanceof Burst || ob2 instanceof Burst) {

                    }else if(ob1 instanceof BlackHole){
                        ob1.setMass(ob1.getMass() + ob2.getMass());
                        removeBody(ob2);
                    }else if(ob2 instanceof BlackHole){
                        ob2.setMass(ob1.getMass() + ob2.getMass());
                        removeBody(ob1);
                    }else {
                        Physics.doCollision(ob1,ob2);
                        double sumEnergy, diff;
                        Burst explode;
                        Vector explodeLocation = Vector.sum(new Vector(ob1.getLocation()),ob1.distance(ob2).unit().get(ob1.getRadius()));
                        sumEnergy = ob1.getKineticEnergy() + ob2.getKineticEnergy();
                        if(ob1.getMass() > ob2.getMass()){
                            ob1.setVelocity(Vector.sum(ob1.getMomentum(), ob2.getMomentum()).get(1 / (ob1.getMass() + ob2.getMass())));
                            double d = Calculate.density(ob1);
                            ob1.setMass(ob1.getMass() + ob2.getMass());
                            if(ob1 instanceof Planet){
                                ob1.setRadius(Math.max(Math.pow((3 * ob1.getMass()) / (4 * Math.PI * d),1.0 / 3.0), 0));
                            }
                            removeBody(ob2);
                            diff = sumEnergy - ob1.getKineticEnergy();
                            explode = new Burst(diff,explodeLocation);
                        }else{
                            ob2.setVelocity(Vector.sum(ob1.getMomentum(), ob2.getMomentum()).get(1 / (ob1.getMass() + ob2.getMass())));
                            double d = Calculate.density(ob2);
                            ob2.setMass(ob2.getMass() + ob1.getMass());
                            if(ob2 instanceof Planet){
                                ob2.setRadius(Math.max(Math.pow((3 * ob2.getMass()) / (4 * Math.PI * d),1.0 / 3.0), 0));
                            }
                            removeBody(ob1);
                            diff = sumEnergy - ob2.getKineticEnergy();
                            explode = new Burst(diff,explodeLocation);
                        }
                        addBody(explode);
                    }
                }else {
                    Physics.doGravity(ob1,ob2,G);
                }
            }
            Physics.doThermoDynamics(stars,bursts,planets);
        }
    }

    void doMovements() {
        for(Body ob : bodies){
            ob.move(Universe.EXPAND_RATE, TIME_SPEED);
        }

        Vector expansion = Vector.ex(new Vector(god).get(EXPAND_RATE),god).get(0.025*TIME_SPEED);
        Vector.sum(god,expansion);
    }

    Body isContainBody(Vector v1){
        for(Body ob : bodies){
            if(ob.distance(v1).getSize() < ob.getRadius()){
                return ob;
            }
        }
        return null;
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        for(Planet ob : planets){
            ob.printBody(canvas,god,sight,getWidth(),getHeight(),paint);
        }
        for(BlackHole ob : blackholes){
            ob.printBody(canvas,god,sight,getWidth(),getHeight(),paint);
        }
        for(Star ob : stars){
            ob.printBody(canvas,god,sight,getWidth(),getHeight(),paint);
        }
        for(Burst ob : bursts){
            ob.printBody(canvas,god,sight,getWidth(),getHeight(),paint);
        }
        if(selectedBody != null){
            selectedBody.printInfo(canvas,fontPaint);
        }
    }

    @Override
    public void run() {
        if(!isPaused){
            doPhysics();
            doMovements();
            for(int i = bodies.size() - 1;i >= 0;i--){
                if(bodies.get(i).getMass() < 1e6){
                    removeBody(bodies.get(i));
                    continue;
                }
                int flag = bodies.get(i).getFlag();
                switch (flag){
                    case Body.EVENT_TO_BLACK_HOLE :
                        addBody(new BlackHole(bodies.get(i)));
                        removeBody(bodies.get(i));
                        break;
                    case Body.EVENT_TO_STAR :
                        addBody(new Star((Planet)bodies.get(i)));
                        removeBody(bodies.get(i));
                        break;
                    case Body.EVENT_TO_PLANET :
                        addBody(new Planet((Star)bodies.get(i)));
                        removeBody(bodies.get(i));
                        break;
                    case Body.EVENT_STAR_COLLAPSE :
                        addBody(new BlackHole(bodies.get(i).getMass()*0.1));
                        addBody(new Burst(Burst.HYPER_NOVA.luminosity, new Vector(bodies.get(i).getLocation())));
                        removeBody(bodies.get(i));
                        break;
                    case Body.EVENT_HYPER_NOVA :
                        addBody(new BlackHole(bodies.get(i).getMass()));
                        addBody(new Burst(Burst.HYPER_NOVA.luminosity,new Vector(bodies.get(i).getLocation())));
                        removeBody(bodies.get(i));
                        break;
                }
            }
            for(int i = bursts.size() - 1;i >= 0;i--){
                if(bursts.get(i).luminosity <= 0 || (Math.pow(bursts.get(i).luminosityStart,0.25)*(bursts.get(i).luminosity / bursts.get(i).luminosityStart)) < 10){
                    removeBody(bursts.get(i));
                }
            }
            for(int i = planets.size() - 1;i >= 0;i--){
                if(planets.get(i).mass < 1e6){
                    removeBody(planets.get(i));
                }
            }
            if(manageMod){
                god.setX(selectedBody.getLocation().getX());
                god.setY(selectedBody.getLocation().getY());
            }
        }
        invalidate();
        handler.postDelayed(this,25);
    }

    static AstronomicalStruct generateSolarSystemAndMoons(){
        AstronomicalStruct solarSystem = new AstronomicalStruct(new Star(Star.SUN));
        solarSystem.addMoon(new Planet(Planet.MERCURY),57.91e9,47360);
        solarSystem.addMoon(new Planet(Planet.VENUS),108.2e9,35020);
        solarSystem.addMoon(Universe.generateEarthSystem(),149.6e9,30000);
        solarSystem.addMoon(Universe.generateMarsSystem(),227.9e9,24000);
        solarSystem.addMoon(Universe.generateJupiterSystem(),778.5e9,13070);
        solarSystem.addMoon(Universe.generateSaturnSystem(),1433e9,9680);
        solarSystem.addMoon(Universe.generateUranusSystem(),2871e9,6800);
        solarSystem.addMoon(Universe.generateNeptuneSystem(),4495e9,5430);
        return solarSystem;
    }

    static AstronomicalStruct generateSolarSystem(){
        AstronomicalStruct solarSystem = new AstronomicalStruct(new Star(Star.SUN));
        solarSystem.addMoon(new Planet(Planet.MERCURY),57.91e9,47360);
        solarSystem.addMoon(new Planet(Planet.VENUS),108.2e9,35020);
        solarSystem.addMoon(new Planet(Planet.EARTH),149.6e9,30000);
        solarSystem.addMoon(new Planet(Planet.MARS),227.9e9,24000);
        solarSystem.addMoon(new Planet(Planet.JUPITER),778.5e9,13070);
        solarSystem.addMoon(new Planet(Planet.SATURN),1433e9,9680);
        solarSystem.addMoon(new Planet(Planet.URANUS),2871e9,6800);
        solarSystem.addMoon(new Planet(Planet.NEPTUNE),4495e9,5430);
        return solarSystem;
    }

    static AstronomicalStruct generateEarthSystem(){
        AstronomicalStruct earthSystem = new AstronomicalStruct(new Planet(Planet.EARTH));
        earthSystem.addMoon(new Planet(Planet.MOON),0.384e9,1022);
        return earthSystem;
    }

    static AstronomicalStruct generateMarsSystem(){
        AstronomicalStruct marsSystem = new AstronomicalStruct(new Planet(Planet.MARS));
        marsSystem.addMoon(new Planet(Planet.PHOBOS),9376e3,2138);
        marsSystem.addMoon(new Planet(Planet.DEIMOS),23460e3,1351);
        return marsSystem;
    }

    static AstronomicalStruct generateJupiterSystem(){
        AstronomicalStruct jupiterSystem = new AstronomicalStruct(new Planet(Planet.JUPITER));
        jupiterSystem.addMoon(new Planet(Planet.GANYMEDE),1070000e3,10880);
        jupiterSystem.addMoon(new Planet(Planet.CALLISTO),1882700e3,8204);
        jupiterSystem.addMoon(new Planet(Planet.EUROPA),670900e3,13740);
        jupiterSystem.addMoon(new Planet(Planet.IO),421700e3,17330);
        return jupiterSystem;
    }

    static AstronomicalStruct generateSaturnSystem(){
        AstronomicalStruct saturnSystem = new AstronomicalStruct(new Planet(Planet.SATURN));
        saturnSystem.addMoon(new Planet(Planet.TITAN),1221850e3,5570);
        saturnSystem.addMoon(new Planet(Planet.ENCELADUS),239156e3,12640);
        saturnSystem.addMoon(new Planet(Planet.RHEA),527000e3,8480);
        saturnSystem.addMoon(new Planet(Planet.DIONE),377400e3,10030);
        saturnSystem.addMoon(new Planet(Planet.MIMAS),185520e3,14280);
        saturnSystem.addMoon(new Planet(Planet.TETHYS),295000e3,11350);
        saturnSystem.addMoon(new Planet(Planet.IAPETUS),3560851e3,3260);
        return saturnSystem;
    }

    static AstronomicalStruct generateUranusSystem(){
        AstronomicalStruct uranusSystem = new AstronomicalStruct(new Planet(Planet.URANUS));
        uranusSystem.addMoon(new Planet(Planet.OBERON),584000e3,3150);
        uranusSystem.addMoon(new Planet(Planet.TITANIA),436000e3,3640);
        uranusSystem.addMoon(new Planet(Planet.UMBRIEL),266000e3,4670);
        return uranusSystem;
    }

    static AstronomicalStruct generateNeptuneSystem(){
        AstronomicalStruct neptuneSystem = new AstronomicalStruct(new Planet(Planet.NEPTUNE));
        neptuneSystem.addMoon(new Planet(Planet.TRITON),354800e3,4390);
        return neptuneSystem;
    }
}
