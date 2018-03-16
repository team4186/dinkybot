package org.usfirst.frc.team4186.robot.factory;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DinkyMotorFactory implements MotorFactory {
	
	private final WPI_TalonSRX talon1 = new WPI_TalonSRX(1); // left primary
	private final WPI_TalonSRX talon8 = new WPI_TalonSRX(8); // right primary

		private final WPI_TalonSRX talon2 = new WPI_TalonSRX(2); // left auxiliary
		private final WPI_TalonSRX talon4 = new WPI_TalonSRX(4); // left auxiliary

		private final WPI_TalonSRX talon6 = new WPI_TalonSRX(6); // right auxiliary
		private final WPI_TalonSRX talon7 = new WPI_TalonSRX(7); // right auxiliary

	private final WPI_VictorSPX victor1 = new WPI_VictorSPX(10); // lift motor
	private final WPI_VictorSPX victor2 = new WPI_VictorSPX(11); //gantry motor

	private final WPI_TalonSRX intakeTalon1 = new WPI_TalonSRX(3);
	private final WPI_TalonSRX intakeTalon2 = new WPI_TalonSRX(9);

	
	@Override
	public DifferentialDrive createDrive() {
		
		talon2.follow(talon1);
		talon4.follow(talon1);
		talon6.follow(talon8);
		talon7.follow(talon8);

		talon1.setInverted(false);
			talon2.setInverted(false);
			talon4.setInverted(false);
		talon8.setInverted(false);
			talon6.setInverted(false);
			talon7.setInverted(false);

		return new DifferentialDrive(talon1, talon8);
		
	}

	
	@Override
	public SpeedController createLiftDrive() {
		
		return victor1;
		
	}
	

	@Override
	public DifferentialDrive createIntakeDrive() {
		
		intakeTalon1.setInverted(false);
		intakeTalon2.setInverted(true);
		
		return new DifferentialDrive(intakeTalon1, intakeTalon2);
		
	}
	
	
	@Override
	public SpeedController createArmMotor() {
		
		return victor2;
		
	}

}
