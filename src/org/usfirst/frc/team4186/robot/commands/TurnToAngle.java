package org.usfirst.frc.team4186.robot.commands;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.usfirst.frc.team4186.robot.MapFunctions;

public class TurnToAngle extends Command {
	
	private final DifferentialDrive drive;
	private final AHRS navx;
	private final PIDController pidNavx;
	private double rotation;
	private final double angle;
	
	public TurnToAngle(DifferentialDrive drive, AHRS navx, double angle) {
		
		this.drive = drive;
		this.navx = navx;
		this.angle = angle;
		
		pidNavx = new PIDController(0.001, 0.0, 0.0, navx, new PIDOutput(){
			@Override
			public void pidWrite(double input){
				rotation = input;
			}
		});
		
		pidNavx.setInputRange(-180, 180);
        pidNavx.setAbsoluteTolerance(1.0);
        pidNavx.setOutputRange(-0.1, 0.1);
        pidNavx.setContinuous(true);
        pidNavx.disable();
	}
	
	@Override
	protected void initialize() {
		
		navx.reset();
		pidNavx.setSetpoint(angle);
		pidNavx.enable();
		
	}
	
	@Override
	protected void execute() {
		
		//drive.arcadeDrive(0.0, MapFunctions.linearMap(rotation));
		drive.tankDrive(-MapFunctions.linearMap(rotation), MapFunctions.linearMap(rotation));
		//drive.tankDrive(rotation, -rotation);
		
		System.out.println("Angle " + navx.getAngle() + " rotation rate " + MapFunctions.linearMap(rotation));
		//System.out.println(pidNavx.getError());
		
	}
	
	

	@Override
	protected boolean isFinished() {
		
		return pidNavx.onTarget();
		
	}
	
	@Override
	protected void end() {
		
		pidNavx.disable();
		drive.stopMotor();
		System.out.println("Done");
		
	}

}
