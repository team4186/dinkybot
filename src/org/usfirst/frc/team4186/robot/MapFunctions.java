package org.usfirst.frc.team4186.robot;

import com.kauailabs.navx.frc.AHRS;

public class MapFunctions {
	
	public static double linearMap(double input){
		
		final double m = (1-0.35)/0.95;
		final double b = 1-m;
		
		return Math.signum(input)*(m*Math.abs(input) + b);
	}
	
	public static double listCorrection(double input, AHRS navx) {
		
		return input + 0.3*navx.getVelocityY()/1.3696442;
	}

}
