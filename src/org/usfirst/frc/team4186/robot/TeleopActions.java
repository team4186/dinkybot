package org.usfirst.frc.team4186.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class TeleopActions {

	public static void moveLift(boolean isUp, DifferentialDrive liftDrive) {

		if (isUp) {

			liftDrive.tankDrive(1, 1);
		} else {
			liftDrive.tankDrive(-1, -1);
		}

	}

	public static void intake(boolean isIn, DifferentialDrive intakeDrive) {

		if (isIn) {

			intakeDrive.tankDrive(1, 1);

		} else {

			intakeDrive.tankDrive(-1, -1);

		}

	}

	public static final int LIFT_LEVEL_LOW = 0;
	public static final int LIFT_LEVEL_MEDIUM = 1;
	public static final int LIFT_LEVEL_HIGHT = 2;
	
	public static boolean changeLiftState(boolean isActive, int liftState, DifferentialDrive liftDrive, Encoder liftEncoder, double previousDistance) {
		
		if (isActive) {

			switch (liftState) {

			case LIFT_LEVEL_LOW:

				if (-liftEncoder.getDistance() >= 0) {

					liftDrive.tankDrive(-0.5, -0.5);
					System.out.println("0");

					return true;

				}
				break;

			case LIFT_LEVEL_MEDIUM:

				if (-liftEncoder.getDistance() > 275 && -previousDistance > 275) {

					liftDrive.tankDrive(-0.5, -0.5);
					System.out.println("1, 1");

					return true;
				} else if (-liftEncoder.getDistance() <= 275  && -previousDistance < 275) {

					liftDrive.tankDrive(0.5, 0.5);
					System.out.println("1, 2");

					return true;

				}
				break;

			case LIFT_LEVEL_HIGHT:

				if (-liftEncoder.getDistance() <= 575) {

					liftDrive.tankDrive(0.5, 0.5);
					System.out.println("2");

					return true;
				}
				break;

			}

		}

		return false;
	}

}
