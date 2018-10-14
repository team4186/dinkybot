package org.usfirst.frc.team4186.robot.commands;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.usfirst.frc.team4186.robot.AuxiliaryFunctions;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PIDSource;


public class DoubleEncoderPID extends Command {
	
	private final DifferentialDrive drive;
	private final double distance;
	private final PIDController pidLeft;
	private final PIDController pidRight;
	private double powerLeft;
	private double powerRight;
	private final AHRS navx;
	private int ticks;
	
	public DoubleEncoderPID(DifferentialDrive drive, AHRS navx, Encoder encoderLeft, Encoder encoderRight, double distance) {
		
		this.drive = drive;
		this.distance = distance;
		this.navx = navx;
		
		pidLeft = pidSet(encoderLeft, new PIDOutput(){
			@Override
			public void pidWrite(double input){	
				powerLeft = input;
			}
		});
		
		pidRight = pidSet(encoderRight, new PIDOutput(){
			@Override
			public void pidWrite(double input){	
				powerRight = input;
			}
		});
		
	}
	
	@Override
	protected void initialize() {
		
		pidLeft.enable();
		pidLeft.setSetpoint(distance); //setpoints
		
		pidRight.enable();
		pidRight.setSetpoint(distance); //Setpoints
		
		ticks = 0;
		
	}
	
	@Override
	protected void execute() {
		
		double newPowerLeft = AuxiliaryFunctions.linearMap(powerLeft);
		double newPowerRight = AuxiliaryFunctions.linearMap(powerRight);
		
		System.out.println(powerLeft);
		
//		drive.tankDrive(newPower, newPower);
		drive.tankDrive(-newPowerLeft, -newPowerRight);
		
		ticks += 1;
		
		//SmartDashboard.putNumber("Power", -AuxiliaryFunctions.linearMap(power));
		//SmartDashboard.putNumber("Distance", pidUltra.getError());
		
	}
	

	@Override
	protected boolean isFinished() {
		
		return (pidLeft.onTarget() && pidRight.onTarget()) || (!navx.isMoving() && ticks >= 10);
		
	}
	
	
	@Override
	protected void end() {
		
		drive.stopMotor();
		pidLeft.disable();
		pidRight.disable();
		
	}
	
	
	private PIDController pidSet(Encoder encoder, PIDOutput power){
		
		PIDController encoderPID = new PIDController(0.1, 0.0, 0.0, encoder, power);
		
		encoderPID.setAbsoluteTolerance(10.0);
		encoderPID.setOutputRange(-0.1, 0.1); //Jerks at high speeds
		encoderPID.setContinuous(false);
		encoderPID.setSetpoint(0.0);
		encoderPID.disable();
			
		return encoderPID;
	}
		

}
