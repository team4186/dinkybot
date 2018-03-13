package org.usfirst.frc.team4186.robot;

import com.kauailabs.navx.frc.AHRS;


public class AuxiliaryFunctions {
	
	public static double linearMap(double input){
		
		final double m = (1-0.35)/0.95;
		final double b = 1-m;
		
		return Math.signum(input)*(m*Math.abs(input) + b);
	}
	
	public static double listCorrection(double input, AHRS navx) {
		
		return input + 0.3*navx.getVelocityY()/1.3696442;
	} 
	
	public static double distanceToTime(double distance, String robot) {
		
		switch(robot) {
		
		case "Clinky":
			
			return 13.11413441*distance - 355.6572878;
			
		/*case "Dinky":
			
			return ???*/
			
		}
			
			return 13.11413441*distance - 355.6572878;
		
	}
	
	public static enum LiftState {
		
		LIFT_DEFAULT,
		LIFT_EXCHANGE,
		LIFT_SWITCH,
		LIFT_SCALE;
		
		LiftState nextState() {
			
			switch(this) {
			case LIFT_DEFAULT:
				
				return LIFT_EXCHANGE;
				
			case LIFT_EXCHANGE:
				
				return LIFT_SWITCH;
				
			case LIFT_SWITCH:
			
				return LIFT_SCALE;
				
			case LIFT_SCALE:
				
				return LIFT_SCALE;
				
			}
			
			return LIFT_DEFAULT;
			
		}
		
		LiftState previousState() {
			
			switch(this) {
			case LIFT_DEFAULT:
				
				return LIFT_DEFAULT;
				
			case LIFT_EXCHANGE:
				
				return LIFT_DEFAULT;
				
			case LIFT_SWITCH:
			
				return LIFT_EXCHANGE;
				
			case LIFT_SCALE:
				
				return LIFT_SWITCH;
				
			}
			
			return LIFT_DEFAULT;
			
		}
		
	}
	
	public static enum GantryState {
		
		GANTRY_DEFAULT,
		GANTRY_CARRY,
		GANTRY_SWITCH,
		GANTRY_SCALE;
		
		GantryState nextState() {
			
			switch(this) {
			case GANTRY_DEFAULT:
				
				return GANTRY_CARRY;
			
			case GANTRY_CARRY:
				
				return GANTRY_SWITCH;
				
			case GANTRY_SWITCH:
				
				return GANTRY_SCALE;
				
			case GANTRY_SCALE:
				
				return GANTRY_SCALE;
			
			}
			
			return GANTRY_DEFAULT;
			
		}
		
		GantryState previousState() {
			
			switch(this) {
			case GANTRY_DEFAULT:
				
				return GANTRY_DEFAULT;
			
			case GANTRY_CARRY:
				
				return GANTRY_DEFAULT;
				
			case GANTRY_SWITCH:
				
				return GANTRY_CARRY;
				
			case GANTRY_SCALE:
				
				return GANTRY_SWITCH;
			
			}
			
			return GANTRY_DEFAULT;
			
		}
		
	}

}
