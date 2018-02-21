package org.usfirst.frc.team4186.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.usfirst.frc.team4186.robot.MapFunctions;
import edu.wpi.first.wpilibj.PIDSource;


public class DriveForTime extends Command {
	
	private final DifferentialDrive drive;
	private long startTime;
	private final long time;
	private double power;
	
	
	public DriveForTime(DifferentialDrive drive, long time, double power) {
		
		this.drive = drive;
		this.time = time;
		this.power = power;
		
	}
	
	@Override
	protected void initialize() {
		
		startTime = System.currentTimeMillis();
		
	}
	
	@Override
	protected void execute() {
		
		drive.tankDrive(-MapFunctions.linearMap(power + Math.signum(power)*0.055), -MapFunctions.linearMap(power));
				
	}
	
	

	@Override
	protected boolean isFinished() {
		
		return System.currentTimeMillis() - startTime >= time;
		
	}
	
	@Override
	protected void end() {
		
		System.out.println("Done");
		drive.stopMotor();
		
	}

}
