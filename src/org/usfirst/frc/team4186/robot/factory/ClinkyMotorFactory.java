package org.usfirst.frc.team4186.robot.factory;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class ClinkyMotorFactory implements MotorFactory {
	
	private final WPI_VictorSPX victorLeftMain = new WPI_VictorSPX(5);
	private final WPI_VictorSPX victorRightMain = new WPI_VictorSPX(2);
	private final WPI_VictorSPX victorLeft1 = new WPI_VictorSPX(6);
	private final WPI_VictorSPX victorLeft2 = new WPI_VictorSPX(7);
	private final WPI_VictorSPX victorRight1 = new WPI_VictorSPX(3);
	private final WPI_VictorSPX victorRight2 = new WPI_VictorSPX(4);

	private final WPI_VictorSPX liftMotor = new WPI_VictorSPX(9);
	
	private final WPI_VictorSPX leftIntake = new WPI_VictorSPX(8);
	private final WPI_VictorSPX rightIntake = new WPI_VictorSPX(0);

	@Override
	public DifferentialDrive createDrive() {
		
		victorLeft1.follow(victorLeftMain);
		victorLeft2.follow(victorLeftMain);
		victorRight1.follow(victorRightMain);
		victorRight2.follow(victorRightMain);
		
		victorLeftMain.setInverted(false);
		victorLeft1.setInverted(false);
		victorLeft2.setInverted(false);
		victorRightMain.setInverted(false);
		victorRight1.setInverted(false);
		victorRight2.setInverted(false);
		
		return new DifferentialDrive(victorLeftMain, victorRightMain);
		
	}

	@Override
	public SpeedController createLiftDrive() {
		return liftMotor;
		
	}

	@Override
	public DifferentialDrive createIntakeDrive() {
		
		return new DifferentialDrive(leftIntake, rightIntake);
		
	}

}
