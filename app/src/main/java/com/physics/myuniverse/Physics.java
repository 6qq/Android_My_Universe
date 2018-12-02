package com.physics.myuniverse;

import java.util.ArrayList;

public class Physics {
    static void doGravity(Body ob1, Body ob2, double g){
        Vector force;
        double forceSize;
        forceSize = g * ob1.getMass() * ob2.getMass() / (Math.pow(ob1.distance(ob2).getSize(),2));
        force = ob1.distance(ob2).unit().get(forceSize);
        if(forceSize != 0){
            ob1.addForce(force);
            ob2.addForce(force.negative());
        }
    }

    static void doCollision(Body ob1, Body ob2) {
        Vector res1, res2, new1, new2;
        double sumMass, exMass;
        sumMass = ob1.getMass() + ob2.getMass();
        exMass = ob1.getMass() - ob2.getMass();
        res1 = Vector.ex(ob1.velocity, ob1.velocity.per(ob1.distance(ob2)));
        res2 = Vector.ex(ob2.velocity, ob2.velocity.per(ob2.distance(ob1)));
        new1 = Vector.sum(res1.get(exMass / sumMass),res2.get(2*ob2.getMass() / sumMass));
        new2 = Vector.ex(res1.get(2*ob1.getMass() / sumMass),res2.get(exMass / sumMass));
		/*
		double a, b, x, y, m, n, l, d, t;
		a = ob1.location.x - ob2.location.x;
		b = ob1.location.y - ob2.location.y;
		x = ob1.velocity.x - ob2.velocity.x;
		y = ob1.velocity.y - ob2.velocity.y;
		d = Math.pow(ob1.radius + ob2.radius, 2);
		m = x*x + y*y;
		n = 2*(a*x + b*y);
		l = (a*a + b*b - d);
		t = (-n + Math.sqrt(n*n - 4*m*l)) / (2*m);
		if(t > 0) {
			t = (-n - Math.sqrt(n*n - 4*m*l)) / (2*m);
		}*/

        ob1.setVelocity(Vector.ex(ob1.getVelocity(), res1));
        ob2.setVelocity(Vector.ex(ob2.getVelocity(), res2));
        ob1.setVelocity(Vector.sum(ob1.getVelocity(), new1));
        ob2.setVelocity(Vector.sum(ob2.getVelocity(), new2));
    }

    static void doThermoDynamics(ArrayList<Star>stars, ArrayList<Burst>bursts, ArrayList<Planet>planets) {
        for(Planet dst : planets){
            double input = 0;
            for(Star src : stars){
                input += ((Calculate.luminosity(src)*Math.PI*dst.getRadius()*dst.getRadius()) / (4 * Math.PI * Math.pow(dst.distance(src).getSize(), 2))) * (1 - dst.albedo);
            }
            for(Burst src : bursts){
                if(src.distance(dst).getSize() < src.getRadius()){
                    input += ((src.luminosity*Math.PI*dst.getRadius()*dst.getRadius()) / (4 * Math.PI * Math.pow(dst.distance(src).getSize(), 2))) * (1 - dst.albedo);
                }
            }
            dst.setTemperature(input);
        }
    }
}
