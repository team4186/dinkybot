package org.usfirst.frc.team4186.robot.commands;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4186.robot.TeleopActions;


public class ChangeLiftState extends Command {
	
	private final SpeedController liftDrive;
	private final int liftState;
	private final Encoder liftEncoder;
	private double previousDistance;
	private boolean isLiftActive;
	
	public ChangeLiftState(boolean isLiftActive, SpeedController liftDrive, int liftState, Encoder liftEncoder) {
		
		this.liftDrive = liftDrive;
		this.liftState = liftState;
		this.liftEncoder = liftEncoder;
		this.isLiftActive = isLiftActive;
		
	}
	
	
	@Override
	protected void initialize() {
		
		previousDistance = liftEncoder.getDistance();
		
	}
	
	
	@Override
	protected void execute() {
		
		isLiftActive = TeleopActions.changeLiftState(isLiftActive, liftState, liftDrive, liftEncoder, previousDistance);
		previousDistance = liftEncoder.getDistance();
		System.out.println(liftEncoder.getDistance());
		
	}
	

	@Override
	protected boolean isFinished() {
		
		return !TeleopActions.changeLiftState(isLiftActive, liftState, liftDrive, liftEncoder, previousDistance);
		
	}
	
	
	@Override
	protected void end() {
		
		liftDrive.stopMotor();
		System.out.println("Done");
		
	}

}
