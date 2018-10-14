package org.usfirst.frc.team4186.robot.commands;

import org.usfirst.frc.team4186.robot.AuxiliaryFunctions;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


public class DriveDoubleEncoder extends Command {

	private final DifferentialDrive drive;
	private final Encoder encoderLeft;
	private final Encoder encoderRight;
	
	private final double amountLeft;
	private final double amountRight;
	private final double powerLeft;
	private final double powerRight;
	
	public DriveDoubleEncoder(DifferentialDrive drive, Encoder encoderLeft, Encoder encoderRight, double distanceLeft, double distanceRight, double powerLeft, double powerRight) {
		
		this.drive = drive;
		this.encoderLeft = encoderLeft;
		this.encoderRight = encoderRight;
		this.powerLeft = powerLeft;
		this.powerRight = powerRight;
		this.amountLeft = 21.13105731 * distanceLeft - 531.1489974;
		this.amountRight = 21.13105731 * distanceRight - 531.1489974;
		
	}
	
	
	@Override
	protected void initialize() {
		
		encoderLeft.reset();
		encoderRight.reset();
		
	}
	
	
	@Override
	protected void execute() {
		
		drive.tankDrive(-AuxiliaryFunctions.linearMap(powerLeft), -AuxiliaryFunctions.linearMap(powerRight));
		
	}
	
	
	@Override
	protected boolean isFinished() {
		
		return amountLeft <= Math.abs(encoderLeft.get()) && amountRight <= Math.abs(encoderRight.get());
		
	}
	
	
	@Override
	protected void end() {
		
		drive.stopMotor();
		
	}

}
