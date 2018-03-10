package org.usfirst.frc.team4186.robot;

import org.usfirst.frc.team4186.robot.factory.ClinkyMotorFactory;

public class Robot extends RobotBase {
	
	public Robot() {
		
		super(new ClinkyMotorFactory());
		
	}
	
	/*private Command driveAuton() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new DriveEncoder(drive, leftDriveEncoder, 300, 0.1));
		commandGroup.addSequential(new TurnToAngle(drive, navx, -87.5));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.1));
//		commandGroup.addSequential(new DriveForTime(drive, (long)MapFunctions.distanceToTime(150), 0.1));
//		commandGroup.addSequential(new TurnToAngle(drive, navx, -87.5));
//		commandGroup.addSequential(new DriveForTime(drive, (long)MapFunctions.distanceToTime(150), 0.1));
//		commandGroup.addSequential(new TurnToAngle(drive, navx, -87.5));
//		commandGroup.addSequential(new DriveForTime(drive, (long)MapFunctions.distanceToTime(150), 0.1));
//		commandGroup.addSequential(new TurnToAngle(drive, navx, -87.5));
//		commandGroup.addSequential(new DriveForTime(drive, (long)MapFunctions.distanceToTime(150), 0.1));
//		commandGroup.addSequential(new TurnToAngle(drive, navx, -87.5));
		
		
		return commandGroup;
	}
	
	private Command autonomous;

	
	@Override //Remove for Dinky Auton
	public void autonomousInit() {
		
		drive.setSafetyEnabled(false);
		
		autonomous = driveAuton();
				
		if(autonomous != null) {
					
			autonomous.start();
					
		}
		
		drive.setSafetyEnabled(false);
		
	}*/

}
