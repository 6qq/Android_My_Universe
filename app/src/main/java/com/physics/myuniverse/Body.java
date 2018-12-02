package com.physics.myuniverse;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcelable;

public abstract class Body implements Parcelable{
    protected double mass, radius;

    static final int EVENT_NOTHING = -1;
    static final int EVENT_TO_BLACK_HOLE = 0;
    static final int EVENT_STAR_COLLAPSE = 1;
    static final int EVENT_HYPER_NOVA = 2;
    static final int EVENT_TO_STAR = 3;
    static final int EVENT_TO_PLANET = 4;
    static final int EVENT_TO_DISCARD = 5;
    protected Vector location, velocity, acceleration;

    public Body(){
        mass = 1;
        radius = 10;
        location = new Vector(0,0);
        velocity = new Vector(0,0);
        acceleration = new Vector(0,0);
    }

    public Body(double m, double r){
        mass = m;
        radius = r;
        location = new Vector(0,0);
        velocity = new Vector(0,0);
        acceleration = new Vector(0,0);
    }

    public Body(double m, double r, Vector l){
        mass = m;
        radius = r;
        location = new Vector(l);
        velocity = new Vector(0,0);
        acceleration = new Vector(0,0);
    }

    public Body(double m,double r, Vector l, Vector v){
        mass = m;
        radius = r;
        location = new Vector(l);
        velocity = new Vector(v);
        acceleration = new Vector(0,0);
    }

    public Body(Body ob){
        mass = ob.getMass();
        radius = ob.getRadius();
        location = new Vector(ob.location);
        velocity = new Vector(ob.velocity);
        acceleration = new Vector(ob.acceleration);
    }

    Vector getLocation(){
        return location;
    }

    void setLocation(Vector l){
        location = new Vector(l);
    }

    Vector getVelocity(){
        return velocity;
    }

    void setVelocity(Vector v){
        velocity = new Vector(v);
    }

    public Vector getAcceleration(){
        return acceleration;
    }

    public void setAcceleration(Vector a){
        acceleration = new Vector(a);
    }

    Vector getMomentum(){
        return velocity.get(mass);
    }

    double getKineticEnergy(){
        return 0.5 * mass * velocity.getSize() * velocity.getSize();
    }

    public abstract void setMass(double m);

    public double getMass(){
        return mass;
    }

    public abstract void setRadius(double r);

    public double getRadius(){
        return radius;
    }

    public abstract int getFlag();

    public abstract void printInfo(Canvas canvas, Paint paint);

    public abstract void printBody(Canvas canvas, Vector observer, double sight, int width, int height, Paint paint);

    void addForce(Vector f) {
        Vector delAccelaration = f.get(1 / mass);
        acceleration = Vector.sum(acceleration, delAccelaration);
    }

    void move(double expandRate, double timeSpeed) {
        velocity = Vector.sum(velocity, acceleration.get(Universe.TIME_SPEED*0.025));
        location = Vector.sum(location, velocity.get(Universe.TIME_SPEED*0.025));
        acceleration = new Vector(0,0);

        Vector expansion = Vector.ex(new Vector(location).get(expandRate),location).get(0.025*timeSpeed);
        Vector.sum(location,expansion);
    }

    Vector distance(Body ob) {
        return Vector.ex(ob.location, location);
    }

    Vector distance(Vector v1){
        return Vector.ex(v1, location);
    }
}

