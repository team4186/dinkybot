package org.usfirst.frc.team4186.robot;

import org.usfirst.frc.team4186.robot.commands.CorrectEncoderAngle;
import org.usfirst.frc.team4186.robot.commands.CorrectSonarAngle;
import org.usfirst.frc.team4186.robot.commands.DistanceFromTarget;
import org.usfirst.frc.team4186.robot.commands.DriveEncoder;
import org.usfirst.frc.team4186.robot.commands.DriveForTime;
import org.usfirst.frc.team4186.robot.commands.MoveLift;
import org.usfirst.frc.team4186.robot.factory.ClinkyMotorFactory;
import org.usfirst.frc.team4186.robot.factory.DinkyMotorFactory;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends RobotBase {
	
	public Robot() {
		
		super(new ClinkyMotorFactory());
		
	}
	
	CommandGroup autonomous;	
	
	@Override
	public void autonomousInit() {
		
		if(autonomous != null) {
			
			autonomous.cancel();
			autonomous = null;
			
		}
		
		autonomous = new CommandGroup();

		drive.setSafetyEnabled(false);
		intakeDrive.setSafetyEnabled(false);
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		/*if(gameData.charAt(0) == 'R') {
			
			autonomous.addSequential(new MoveLift(liftDrive, liftEncoder, 55));
			autonomous.addSequential(new DistanceFromTarget(drive, sonar, 0.05));
			autonomous.addSequential(new DriveForTime(intakeDrive, 4000, -1.0));
			
		}
		else {
			
			autonomous.addSequential(new DistanceFromTarget(drive, sonar, 0.025));
			
		}*/
		
		//autonomous.addSequential(new CorrectEncoderAngle(drive, leftDriveEncoder, rightDriveEncoder, 100));
		autonomous.addSequential(new CorrectSonarAngle(drive, sonar, sonar2, 0.0));
		
		//autonomous.addSequential(new DriveEncoder(drive, rightDriveEncoder, 360, -0.2));
		//autonomous.addSequential(new DistanceFromTarget(drive, sonar, 0.025));
		//autonomous.addSequential(new DriveForTime(drive, 4000, -0.3));
		//autonomous.addSequential(new DriveForTime(intakeDrive, 2000, -1.0));
		//leftDriveEncoder.reset();
		
		autonomous.start();
		
	}
		
	@Override
	public void autonomousPeriodic() {
		
		super.autonomousPeriodic();
		//System.out.println("Encoder1 " + Math.abs(leftDriveEncoder.get()) + " Encoder2 " + Math.abs(rightDriveEncoder.get()));
		//System.out.println(Math.abs(leftDriveEncoder.get()) - Math.abs(rightDriveEncoder.get()));
		
	} 

}
