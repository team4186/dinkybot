package org.usfirst.frc.team4186.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class TeleopActions {

	public static void moveLift(boolean isUp, DifferentialDrive liftDrive) {
		
		if (isUp) {
			
			liftDrive.tankDrive(0.8, 0.8);
			
		} else {
			
			liftDrive.tankDrive(-0.8, -0.8);
			
		}

	}

	public static void intake(boolean isIn, DifferentialDrive intakeDrive) {

		if (isIn) {

			intakeDrive.tankDrive(1, 1);

		} else {

			intakeDrive.tankDrive(-1, -1);

		}

	}

	public static final int LIFT_LEVEL_DEFAULT = 0;
	public static final int LIFT_LEVEL_EXCHANGE = 1;
	public static final int LIFT_LEVEL_SWITCH = 2;
	public static final int LIFT_LEVEL_SCALE = 3;
	
	public static boolean changeLiftState(boolean isActive, int liftState, DifferentialDrive liftDrive, Encoder liftEncoder, double previousDistance) {
		
		if (isActive) {

			switch (liftState) {

			case LIFT_LEVEL_DEFAULT:

				if (liftEncoder.getDistance() >= 0) {

					liftDrive.tankDrive(-0.5, -0.5);
					System.out.println("0");

					return true;

				}
				
				break;

			case LIFT_LEVEL_EXCHANGE:

				if (liftEncoder.getDistance() > 275 && previousDistance > 275) {

					liftDrive.tankDrive(-0.5, -0.5);
					System.out.println("1, 1");

					return true;
				} else if (liftEncoder.getDistance() <= 275  && previousDistance < 275) {

					liftDrive.tankDrive(0.5, 0.5);
					System.out.println("1, 2");

					return true;

				}
				break;
			
			case LIFT_LEVEL_SWITCH:

				if (liftEncoder.getDistance() > 425 && previousDistance > 425) {

					liftDrive.tankDrive(-0.5, -0.5);
					System.out.println("1, 1");

					return true;
				} else if (liftEncoder.getDistance() <= 425  && -previousDistance < 425) {

					liftDrive.tankDrive(0.5, 0.5);
					System.out.println("1, 2");

					return true;

				}
				break;

			case LIFT_LEVEL_SCALE:

				if (Math.abs(liftEncoder.getDistance()) <= 575) {

					liftDrive.tankDrive(0.5, 0.5);
					System.out.println("2");

					return true;
				}
				break;

			}

		}
		else {
			liftDrive.stopMotor();
		}

		return false;
	}

}
