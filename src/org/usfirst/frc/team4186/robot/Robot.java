package org.usfirst.frc.team4186.robot;

import org.usfirst.frc.team4186.robot.factory.ClinkyMotorFactory;

public class Robot extends RobotBase {
	
	public Robot() {
		
		super(new ClinkyMotorFactory());
		
	}

}
