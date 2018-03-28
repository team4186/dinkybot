package org.usfirst.frc.team4186.robot.commands;

import org.usfirst.frc.team4186.robot.AuxiliaryFunctions;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


public class CorrectEncoderAngle extends Command {

	private final DifferentialDrive drive;
	private final Encoder encoder1;
	private final Encoder encoder2;
	private final PIDController pidEncoderCorrection;
	private final PIDController pidEncoderDrive;
	private final double amount;
	private double power;
	private double rotation;
	
	public CorrectEncoderAngle(DifferentialDrive drive, Encoder encoder1, Encoder encoder2, double distance) {
		
		this.drive = drive;
		this.encoder1 = encoder1;
		this.encoder2 = encoder2;
		this.amount = 21.13105731 * distance - 531.1489974;
		
		pidEncoderCorrection = new PIDController(1.0, 0.0, 0.0, new PIDSource() {
			
			
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
				
			}
			
			
			@Override
			public PIDSourceType getPIDSourceType() {
				
				return PIDSourceType.kDisplacement;
				
			}
			
			
			@Override
			public double pidGet() {
				
				return Math.abs(Math.abs(encoder1.get()) - Math.abs(encoder2.get()));
				
			}
			
		}, new PIDOutput(){
			@Override
			public void pidWrite(double input){
				
				rotation = input;
				
			}
		});
		
		pidEncoderDrive = new PIDController(1.0, 0.0, 0.0, new PIDSource() {
			
			
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
				
			}
			
			
			@Override
			public PIDSourceType getPIDSourceType() {
				
				return PIDSourceType.kDisplacement;
				
			}
			
			
			@Override
			public double pidGet() {
				
				return Math.abs(encoder1.get());
				
			}
			
		}, new PIDOutput(){
			@Override
			public void pidWrite(double input){
				
				power = input;
				
			}
		});
		
		pidEncoderCorrection.setAbsoluteTolerance(2.5*distance);
		pidEncoderCorrection.setOutputRange(-0.05, 0.05); //Jerks at high speeds
		pidEncoderCorrection.setContinuous(false);
		pidEncoderCorrection.disable();
		
		pidEncoderDrive.setAbsoluteTolerance(2.5*distance);
		pidEncoderDrive.setOutputRange(-0.1, 0.1); //Jerks at high speeds
		pidEncoderDrive.setContinuous(false);
		pidEncoderDrive.disable();
	}
	
	
	@Override
	protected void initialize() {
	
		encoder1.reset();
		encoder2.reset();
		pidEncoderCorrection.enable();
		pidEncoderCorrection.setSetpoint(0.0);
		pidEncoderDrive.enable();
		pidEncoderDrive.setSetpoint(amount);
		
	}
	
	
	@Override
	protected void execute() {
		
		//drive.tankDrive(-AuxiliaryFunctions.linearMap(power + Math.signum(power)*0.055), -AuxiliaryFunctions.linearMap(power));
		drive.arcadeDrive(AuxiliaryFunctions.linearMap(power), -AuxiliaryFunctions.linearMap(rotation));
		//System.out.println(rotation);
		
	}
	
	
	@Override
	protected boolean isFinished() {
		
		return pidEncoderDrive.onTarget() && pidEncoderCorrection.onTarget();
		
	}
	
	
	@Override
	protected void end() {
		
		drive.stopMotor();
		pidEncoderCorrection.disable();
		
	}

}
