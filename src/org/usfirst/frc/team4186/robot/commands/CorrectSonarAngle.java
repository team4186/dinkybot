package org.usfirst.frc.team4186.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.usfirst.frc.team4186.robot.AuxiliaryFunctions;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PIDSource;


public class CorrectSonarAngle extends Command {
	
	private final DifferentialDrive drive;
	private double power;
	private double rotation;
	private double distance;
	private final PIDController pidUltraCorrection;
	private final PIDController pidUltraDistance;
	
	public CorrectSonarAngle(DifferentialDrive drive, Ultrasonic sonar1, Ultrasonic sonar2, double distance) {
		
		this.drive = drive;
		this.distance = distance;
		
		pidUltraCorrection = new PIDController(0.1, 0.0, 0.0, new PIDSource() {
			
			
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
				
			}
			
			
			@Override
			public PIDSourceType getPIDSourceType() {
				
				return PIDSourceType.kDisplacement;
				
			}
			
			
			@Override
			public double pidGet() {
				
				return (sonar1.getRangeMM() - sonar2.getRangeMM())*0.001;
				
			}
			
		}, new PIDOutput(){
			@Override
			public void pidWrite(double input){	
				rotation = input;
			}
		});
		
		pidUltraDistance = new PIDController(0.1, 0.0, 0.0, new PIDSource() {
			
			
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
				
			}
			
			
			@Override
			public PIDSourceType getPIDSourceType() {
				
				return PIDSourceType.kDisplacement;
				
			}
			
			
			@Override
			public double pidGet() {
				
				return sonar1.getRangeMM()/1000;
				
			}
			
		}, new PIDOutput(){
			@Override
			public void pidWrite(double input){	
				power = input;
			}
		});
				
		pidUltraCorrection.setInputRange(0, 5);
        pidUltraCorrection.setAbsoluteTolerance(0.01);
        pidUltraCorrection.setOutputRange(-0.1, 0.1); //Jerks at high speeds
        pidUltraCorrection.setContinuous(false);
        pidUltraCorrection.setSetpoint(0.0);
        pidUltraCorrection.disable();
        
        pidUltraDistance.setInputRange(0, 5);
        pidUltraDistance.setAbsoluteTolerance(0.01);
        pidUltraDistance.setOutputRange(-0.1, 0.1); //Jerks at high speeds
        pidUltraDistance.setContinuous(false);
        pidUltraDistance.setSetpoint(distance);
        pidUltraDistance.disable();
        
	}
	
	@Override
	protected void initialize() {
		
		pidUltraCorrection.enable();
		pidUltraDistance.enable();
		pidUltraCorrection.setSetpoint(0.0);
        pidUltraDistance.setSetpoint(distance);
		
	}
	
	@Override
	protected void execute() {
		
		drive.arcadeDrive(-AuxiliaryFunctions.linearMap(power), AuxiliaryFunctions.linearMap(rotation));
		
	}
	

	@Override
	protected boolean isFinished() {
		
		return pidUltraDistance.onTarget() && pidUltraCorrection.onTarget();
		
	}
	
	
	@Override
	protected void end() {
		
		drive.stopMotor();
		pidUltraCorrection.disable();
		pidUltraDistance.disable();
		
	}

}
