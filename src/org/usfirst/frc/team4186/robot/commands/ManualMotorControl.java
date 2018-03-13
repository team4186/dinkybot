package org.usfirst.frc.team4186.robot.commands;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

public class ManualMotorControl extends Command {

	private final SpeedController motor;
	DigitalInput limitSwitch;
	double power;

	public ManualMotorControl(SpeedController motor, DigitalInput limitSwitch, double power) {
		
		this.motor = motor;
		this.limitSwitch = limitSwitch;
		this.power = power;

	}
	
	
	@Override
	protected void initialize() {
		
	}
	
	
	@Override
	protected void execute() {
		
		motor.set(power);
		
	}
	

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return limitSwitch != null && limitSwitch.get();
		//return false;
	}
	
	
	@Override
	protected void end() {
		
		System.out.println(" end!!");
		motor.stopMotor();
		
	}

}
