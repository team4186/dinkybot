package org.usfirst.frc.team4186.robot.commands;

import org.usfirst.frc.team4186.robot.AuxiliaryFunctions;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveEncoder extends Command {

	private final DifferentialDrive drive;
	private final Encoder encoder;
	
	private final double amount;
	private final double power;
	
	public DriveEncoder(DifferentialDrive drive, Encoder encoder, double distance, double power) {
		
		this.drive = drive;
		this.encoder = encoder;
		this.power = power;
		this.amount = 21.13105731 * distance - 531.1489974;
		
	}
	
	@Override
	protected void initialize() {
		
		encoder.reset();
		
	}
	
	@Override
	protected void execute() {
		
		drive.tankDrive(AuxiliaryFunctions.linearMap(power), AuxiliaryFunctions.linearMap(power));
		
	}
	
	@Override
	protected boolean isFinished() {
		
		return amount < -encoder.get();
		
	}
	
	@Override
	protected void end() {
		
		drive.stopMotor();
		
	}

}
