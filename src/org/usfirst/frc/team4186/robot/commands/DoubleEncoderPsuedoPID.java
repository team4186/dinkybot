package org.usfirst.frc.team4186.robot.commands;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.usfirst.frc.team4186.robot.AuxiliaryFunctions;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PIDSource;


public class DoubleEncoderPsuedoPID extends Command {
	
	private final DifferentialDrive drive;
	private final double distance;
	private Encoder encoderLeft;
	private Encoder encoderRight;
	private double absTol;
	private double powerLeft;
	private double powerRight;
	private double errorLeft;
	private double errorRight;
	
	public DoubleEncoderPsuedoPID(DifferentialDrive drive, Encoder encoderLeft, Encoder encoderRight, double distance) {
		
		this.drive = drive;
		this.distance = distance;
		this.encoderLeft = encoderLeft;
		this.encoderRight = encoderRight;
		
	}
	
	@Override
	protected void initialize() {
		
		errorLeft = distance;
		errorRight = distance;
		powerLeft = 1/(1 + 1.85714285714*Math.exp(-distance/100));
		powerRight = powerLeft;
		absTol = 10.0;
		
	}
	
	@Override
	protected void execute() {
		
		drive.tankDrive(-powerLeft, -powerRight);
		
		errorLeft = distance - encoderLeft.get();
		errorRight = distance - encoderRight.get();
		
		powerLeft = Math.signum(errorLeft)*Math.abs(newPower(absTol, distance, encoderLeft.get()));
		powerRight = Math.signum(errorRight)*Math.abs(newPower(absTol, distance, encoderRight.get()));
		
	}
	

	@Override
	protected boolean isFinished() {
		
		return Math.abs(errorLeft) <= absTol && Math.abs(errorRight) <= absTol;
		
	}
	
	
	@Override
	protected void end() {
		
		drive.stopMotor();
		
	}
	
	
	private static double newPower(double absTol, double distance, double current_dist){
		
		return 0.25/(absTol - distance)*current_dist + 1;
		
	}
		

}
