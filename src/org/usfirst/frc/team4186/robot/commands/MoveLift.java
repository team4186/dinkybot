package org.usfirst.frc.team4186.robot.commands;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;

public class MoveLift extends Command {

	private final SpeedController motor;
	private final Encoder encoder;

	private PIDController controller;

	public MoveLift(SpeedController motor, Encoder encoder, double target) {
		this.motor = motor;
		this.encoder = encoder;

		controller = new PIDController(0.1, 0.0, 0.0, encoder, new PIDOutput() {
			@Override
			public void pidWrite(double output) {
//				System.out.println("output: " + output + " position: " + encoder.pidGet() + " error: " + controller.getError());
				motor.set(-output);
			}
		});
		
		controller.setContinuous(false);
		controller.setAbsoluteTolerance(1);
		controller.setInputRange(-300, 300);
		controller.setName("Lift", "Lift PID");
		controller.setOutputRange(-0.1, 0.3);
		controller.setSetpoint(target);
	}
	
	@Override
	protected void initialize() {
		controller.enable();
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return controller.onTarget();
	}
	
	@Override
	protected void end() {
		System.out.println(" end!!");
		controller.disable();
		motor.stopMotor();
	}

}
