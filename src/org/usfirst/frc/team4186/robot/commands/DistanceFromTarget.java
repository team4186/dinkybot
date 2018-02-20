package org.usfirst.frc.team4186.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.usfirst.frc.team4186.robot.MapFunctions;
import edu.wpi.first.wpilibj.PIDSource;


public class DistanceFromTarget extends Command {
	
	private final DifferentialDrive drive;
	private final Ultrasonic sonar;
	private final double distance;
	private double power;
	private final PIDController pidUltra;
	
	public DistanceFromTarget(DifferentialDrive drive, Ultrasonic sonar, double distance) {
		
		this.drive = drive;
		this.sonar = sonar;
		this.distance = distance;
		
		pidUltra = new PIDController(0.1, 0.0, 0.0, new PIDSource() {
			
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
				
			}
			
			@Override
			public PIDSourceType getPIDSourceType() {
				
				return PIDSourceType.kDisplacement;
				
			}
			
			@Override
			public double pidGet() {
				
				return sonar.getRangeMM() / 1000.0;
				
			}
			
		}, new PIDOutput(){
			@Override
			public void pidWrite(double input){
				power = input;
			}
		});
				
		pidUltra.setInputRange(0, 5);
        pidUltra.setAbsoluteTolerance(0.05);
        pidUltra.setOutputRange(-0.2, 0.2);
        pidUltra.setContinuous(false);
        pidUltra.setSetpoint(0.0);
        pidUltra.disable();
	}
	
	@Override
	protected void initialize() {
		
		pidUltra.enable();
		pidUltra.setSetpoint(distance);
		
	}
	
	@Override
	protected void execute() {
		
		drive.tankDrive(-MapFunctions.linearMap(power + Math.signum(power)*0.055), -MapFunctions.linearMap(power));
		
		System.out.println("power " + power + " distance " + sonar.getRangeMM());
				
	}
	
	

	@Override
	protected boolean isFinished() {
		
		return pidUltra.onTarget();
		
	}
	
	@Override
	protected void end() {
		
		System.out.println("Done");
		drive.stopMotor();
		pidUltra.disable();
		
	}

}
