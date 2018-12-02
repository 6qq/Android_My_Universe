package com.physics.myuniverse;

import java.util.ArrayList;

class AstronomicalStruct {
    private ArrayList<Body>moonBodies;
    private ArrayList<AstronomicalStruct>moonStructs;
    private Body mainBody;
    private Vector structCenter;
    private Vector structVelocity;
    AstronomicalStruct(Body ob,Vector l,Vector v){
        structCenter = l;
        structVelocity = v;
        moonBodies = new ArrayList<>();
        moonStructs = new ArrayList<>();
        mainBody = ob;
        mainBody.setLocation(structCenter);
        mainBody.setVelocity(structVelocity);
        moonBodies.add(mainBody);
    }

    AstronomicalStruct(Body ob){
        structCenter = new Vector(0,0);
        structVelocity = new Vector(0,0);
        mainBody = ob;
        mainBody.setLocation(structCenter);
        mainBody.setVelocity(structVelocity);
        moonBodies = new ArrayList<>();
        moonStructs = new ArrayList<>();
        moonBodies.add(mainBody);
    }

    void addMoon(Body ob,double distance,double speed){
        ob.setLocation(Vector.sum(structCenter,new Vector(distance,0)));
        ob.setVelocity(Vector.sum(structVelocity,new Vector(0,speed)));
        moonBodies.add(ob);
    }

    void addMoon(AstronomicalStruct struct,double distance,double speed){
        struct.setLocation(Vector.sum(structCenter,new Vector(distance,0)));
        struct.setVelocity(Vector.sum(structVelocity,new Vector(0,speed)));
        moonStructs.add(struct);
    }

    void setLocation(Vector l){
        for(Body ob : moonBodies){
            Vector distance = Vector.ex(ob.getLocation(),structCenter);
            ob.setLocation(Vector.sum(l,distance));
        }
        for(AstronomicalStruct st : moonStructs){
            Vector distance = Vector.ex(st.getLocation(),structCenter);
            st.setLocation(Vector.sum(l,distance));
        }
        structCenter = l;
    }

    Vector getLocation(){
        return structCenter;
    }

    void setVelocity(Vector v){
        for(Body ob : moonBodies){
            Vector orbitalSpeed = Vector.ex(ob.getVelocity(),structVelocity);
            ob.setVelocity(Vector.sum(v,orbitalSpeed));
        }
        for(AstronomicalStruct st : moonStructs){
            Vector orbitalSpeed = Vector.ex(st.getVelocity(),structVelocity);
            st.setVelocity(Vector.sum(v,orbitalSpeed));
        }
        structVelocity = v;
    }

    Vector getVelocity(){
        return structVelocity;
    }

    ArrayList<Body> getBodies(){
        return moonBodies;
    }

    ArrayList<AstronomicalStruct> getStructs(){
        return moonStructs;
    }
}
