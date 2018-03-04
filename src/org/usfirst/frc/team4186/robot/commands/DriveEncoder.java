package org.usfirst.frc.team4186.robot.commands;

import org.usfirst.frc.team4186.robot.MapFunctions;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveEncoder extends Command {

	private final DifferentialDrive drive;
	private final Encoder encoder;
	
	private final int amount;
	private final double power;
	
	public DriveEncoder(DifferentialDrive drive, Encoder encoder, int amount, double power) {
		this.drive = drive;
		this.encoder = encoder;
		this.amount = amount;
		this.power = power;
	}
	
	@Override
	protected void initialize() {
		encoder.reset();
	}
	
	@Override
	protected void execute() {
		drive.tankDrive(MapFunctions.linearMap(power + Math.signum(power)*0.025), MapFunctions.linearMap(power));
	}
	
	@Override
	protected boolean isFinished() {
		return amount < encoder.get();
	}
	
	@Override
	protected void end() {
		drive.stopMotor();
	}

}
