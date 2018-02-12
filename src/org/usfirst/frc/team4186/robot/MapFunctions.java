package org.usfirst.frc.team4186.robot;

public class MapFunctions {
	
	public static double linearMap(double input){
		
		final double m = (1-0.3)/0.95;
		final double b = 1-m;
		
		return Math.signum(input)*(m*Math.abs(input) + b);
	}

}
