package org.usfirst.frc.team4186.robot;

import org.usfirst.frc.team4186.robot.factory.ClinkyMotorFactory;
import org.usfirst.frc.team4186.robot.factory.DinkyMotorFactory;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends RobotBase {
	
	public Robot() {
		
		super(new DinkyMotorFactory());
		
	}
	
	CommandGroup autonomous = new CommandGroup();	
	
	@Override
	public void autonomousInit() {

		//autonomous.addSequential(new );
		
	}
	
	@Override
	public void autonomousPeriodic() {
		
		Scheduler.getInstance();
		
	}

}
