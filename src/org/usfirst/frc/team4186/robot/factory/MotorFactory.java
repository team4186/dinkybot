package org.usfirst.frc.team4186.robot.factory;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public interface MotorFactory {
	DifferentialDrive createDrive();
	DifferentialDrive createLiftDrive();
	DifferentialDrive createIntakeDrive();
}
