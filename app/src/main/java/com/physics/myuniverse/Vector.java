package com.physics.myuniverse;

public class Vector {
    private double x;
    private double y;
    private double size;
    Vector(double x, double y){
        this.x = x;
        this.y = y;
        this.size = Math.sqrt(x*x + y*y);
    }

    Vector(){
        this.x = 0;
        this.y = 0;
        this.size = 0;
    }

    Vector(Vector v1) {
        x = v1.x;
        y = v1.y;
        size = v1.getSize();
    }

    double getX(){
        return x;
    }

    void setX(double n){
        x = n;
    }

    double getY(){
        return y;
    }

    void setY(double n){
        y = n;
    }

    Vector unit() {
        return this.get(1/this.size);
    }

    Vector negative() {
        return this.get(-1);
    }

    Vector get(double n) {
        return new Vector(this.x*n, this.y*n);
    }

    Vector res(Vector v1) {
        return v1.unit().get(Vector.skaler(this,v1.unit()));
    }

   Vector per(Vector v1) {
        return Vector.ex(this, v1.unit().get(Vector.skaler(this,v1.unit())));
    }

    static Vector sum(Vector v1, Vector v2) {
        return new Vector(v1.x + v2.x, v1.y + v2.y);
    }

    static Vector ex(Vector v1, Vector v2) {
        return new Vector(v1.x - v2.x, v1.y - v2.y);
    }

    static double skaler(Vector v1, Vector v2) {
        return v1.x*v2.x + v1.y*v2.y;
    }

    boolean isSameDirection(Vector v1) {
        return Vector.skaler(this, v1) >= 0;
    }

    double getSize() {
        return this.size;
    }
}
