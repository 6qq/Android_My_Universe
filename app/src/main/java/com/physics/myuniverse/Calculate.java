package com.physics.myuniverse;

public class Calculate {
    static double surfaceArea(Body ob) {
        return 4 * Math.PI * ob.getRadius() * ob.getRadius();
    }

    static double volume(Body ob) {
        return (4.0 / 3.0) * Math.PI * ob.getRadius() * ob.getRadius() * ob.getRadius();
    }

    static double density(Body ob) {
        return ob.getMass() / volume(ob);
    }

    static double brightness(Star s) {
        return Universe.STEFAN_BOLTZMANN * Math.pow(s.getTemperature(), 4);
    }

    static double luminosity(Star s) {
        return surfaceArea(s) * brightness(s);
    }

    static double schwarzschildRadius(double m) {
        return 2 * Universe.G * m / Math.pow(Universe.c, 2);
    }

    static double schwarzschildMass(double r) {
        return r * Math.pow(Universe.c, 2) / (2 * Universe.G);
    }

    static double rocheLimite(Body ob1, Body ob2) {
        return 2.4228 * ob1.getRadius() * Math.pow(density(ob1) / density(ob2), 1.0 / 3.0);
    }

    static double convertToEnergy(double m) {
        return m * Universe.c * Universe.c;
    }

    static double convertToMass(double e) {
        return e / (Universe.c * Universe.c);
    }

    static String getStarType(Star s){
        double d = density(s) * 1e-3;
        if(d < 1e-8){
            return "super giant star";
        }else if(d < 1e-6){
            return "giant star";
        }else if(d < 1e4){
            return "main sequence star";
        }else if(d < 1e14){
            return "white dwarf";
        }else{
            return "neutron star";
        }
    }
}
