package org.usfirst.frc.team4186.robot;

import org.usfirst.frc.team4186.robot.commands.ChangeLiftState;
import org.usfirst.frc.team4186.robot.commands.DistanceFromTarget;
import org.usfirst.frc.team4186.robot.commands.DriveForTime;
import org.usfirst.frc.team4186.robot.commands.TurnToAngle;
import org.usfirst.frc.team4186.robot.factory.MotorFactory;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public abstract class RobotBase extends TimedRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kLeftOuterAuto = "Left Outer";
	private static final String kRightOuterAuto = "Right Outer";
	private static final String kLeftInnerAuto = "Left Inner";
	private static final String kRightInnerAuto = "Right Inner";
	
	DigitalInput limitSwitch = new DigitalInput(8);

	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	private Joystick joystick = new Joystick(0);
	
	JoystickButton dpadUp = new JoystickButton(joystick, 20);
	JoystickButton dpadDown = new JoystickButton(joystick, 22);
	JoystickButton topTrigger = new JoystickButton(joystick, 1);
	JoystickButton bottomTrigger = new JoystickButton(joystick, 6);
	JoystickButton liftUp = new JoystickButton(joystick, 3);
	JoystickButton liftDown = new JoystickButton(joystick, 4);
	JoystickButton resetEncoders = new JoystickButton(joystick, 7);


	Encoder liftEncoder = new Encoder(4, 5, false, Encoder.EncodingType.k2X);
	boolean isLiftActive = false;
	int liftState = 0;
	double previousDistance;
	
	Encoder leftDriveEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k2X);
	
	Ultrasonic sonar = new Ultrasonic(0, 1, Ultrasonic.Unit.kMillimeters);
	AHRS navx = new AHRS(SPI.Port.kMXP);
	
	DifferentialDrive drive; 
	DifferentialDrive liftDrive;
	DifferentialDrive intakeDrive;
	
	AnalogInput longSonar = new AnalogInput(0);
	
	char gameData;
	
	public RobotBase(MotorFactory motorFactory) {
		drive = motorFactory.createDrive();
		liftDrive = motorFactory.createLiftDrive();
		intakeDrive = motorFactory.createIntakeDrive();
	}
	
	private Command leftInnerStartLeftSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 2, liftEncoder));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.025));
		commandGroup.addSequential(new DriveForTime(intakeDrive, 3000, 1.0));
		
		return commandGroup;
	}
	
	private Command leftInnerStartRightSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.825));
		commandGroup.addSequential(new TurnToAngle(drive, navx, 90.0));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 2.64755));
		commandGroup.addSequential(new TurnToAngle(drive, navx, -90.0));
		commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 2, liftEncoder));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.025));
		commandGroup.addSequential(new DriveForTime(intakeDrive, 3000, 1.0));
		
		return commandGroup;
	}
	
	private Command rightInnerStartRightSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 2, liftEncoder));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.025));
		commandGroup.addSequential(new DriveForTime(intakeDrive, 3000, 1.0));
		
		return commandGroup;
	}
	
	private Command rightInnerStartLeftSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.525));
		commandGroup.addSequential(new TurnToAngle(drive, navx, -90.0));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 2.64755));
		commandGroup.addSequential(new TurnToAngle(drive, navx, 90.0));
		commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 2, liftEncoder));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.025));
		commandGroup.addSequential(new DriveForTime(intakeDrive, 3000, 1.0));
		
		return commandGroup;
	}
	
	private Command leftOuterStartLeftSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 4.2922));
		commandGroup.addSequential(new TurnToAngle(drive, navx, 90.0));
		commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 2, liftEncoder));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.025));
		commandGroup.addSequential(new DriveForTime(intakeDrive, 3000, 1.0));
		
		return commandGroup;
	}
	
	private Command leftOuterStartRightSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 5.8234));
		commandGroup.addSequential(new TurnToAngle(drive, navx, -90.0));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 4.597));
		commandGroup.addSequential(new TurnToAngle(drive, navx, 180.0));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 2.525));
		commandGroup.addSequential(new TurnToAngle(drive, navx, -90.0));
		commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 2, liftEncoder));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.025));
		commandGroup.addSequential(new DriveForTime(intakeDrive, 3000, 1.0));
		
		return commandGroup;
	}
	
	private Command rightOuterStartLeftSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 5.8234));
		commandGroup.addSequential(new TurnToAngle(drive, navx, 90.0));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 4.597));
		commandGroup.addSequential(new TurnToAngle(drive, navx, 180.0));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 2.525));
		commandGroup.addSequential(new TurnToAngle(drive, navx, 90.0));
		commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 2, liftEncoder));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.025));
		commandGroup.addSequential(new DriveForTime(intakeDrive, 3000, 1.0));
		
		return commandGroup;
	}
	
	private Command rightOuterStartRightSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 4.2922));
		commandGroup.addSequential(new TurnToAngle(drive, navx, -90.0));
		commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 2, liftEncoder));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.025));
		commandGroup.addSequential(new DriveForTime(intakeDrive, 3000, 1.0));
		
		return commandGroup;
	}
	
	private Command autonomous;
	
	
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("Left Outer Auto | Ultrasonics facing drive station", kLeftOuterAuto);
		m_chooser.addObject("Right Outer Auto | Ultrasonics facing drive station", kRightOuterAuto);
		m_chooser.addObject("Left Inner Auto | Ultrasonics facing switch", kLeftInnerAuto);
		m_chooser.addObject("Right Inner Auto | Ultrasonics facing switch", kRightInnerAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		joystick.setThrottleChannel(2);
        joystick.setTwistChannel(5);
        
		sonar.setAutomaticMode(true);
		
		CameraServer.getInstance().startAutomaticCapture(0);
		CameraServer.getInstance().startAutomaticCapture(1);
		
		liftEncoder.setMaxPeriod(0.01);
		liftEncoder.setMinRate(20);
		liftEncoder.setDistancePerPulse(1); 
		liftEncoder.setReverseDirection(false);
		liftEncoder.setSamplesToAverage(1);
		
		leftDriveEncoder.setMaxPeriod(0.01);
		leftDriveEncoder.setMinRate(20);
		leftDriveEncoder.setDistancePerPulse(1); 
		leftDriveEncoder.setReverseDirection(false);
		leftDriveEncoder.setSamplesToAverage(1);
	}
	

	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		System.out.println("Auto selected: " + m_autoSelected);
		
		gameData = DriverStation.getInstance().getGameSpecificMessage().charAt(0);
		
		drive.setSafetyEnabled(false);
		liftDrive.setSafetyEnabled(false);
		
		liftEncoder.reset();
		
		switch(gameData) {
		case 'L':
			switch(m_autoSelected) {
			case kLeftOuterAuto:
				
				autonomous = leftOuterStartLeftSwitch();
				
				break;
			case kLeftInnerAuto:
				
				autonomous = leftInnerStartLeftSwitch();
				
				break;
			case kRightOuterAuto:
				
				autonomous = rightOuterStartLeftSwitch();
				
				break;
			case kRightInnerAuto:
				
				autonomous = rightInnerStartLeftSwitch();
				
				break;
			}
		case 'R':
			switch(m_autoSelected) {
			case kLeftOuterAuto:
				
				autonomous = leftOuterStartRightSwitch();
				
				break;
			case kLeftInnerAuto:
				
				autonomous = leftInnerStartRightSwitch();
				
				break;
			case kRightOuterAuto:
				
				autonomous = rightOuterStartRightSwitch();
				
				break;
			case kRightInnerAuto:
				
				autonomous = rightInnerStartRightSwitch();
				
				break;
			}
		}
		
		if(autonomous != null) {
			
			autonomous.start();
			
		}
 
	}
	

	@Override
	public void autonomousPeriodic() {
		
		Scheduler.getInstance().run();
		
	}
	
	
	@Override
	public void teleopInit() {
		
		liftEncoder.reset();
		leftDriveEncoder.reset();
		
		liftState = 0;
		liftDrive.stopMotor();
		previousDistance = liftEncoder.getDistance();
		
	}
	
	
	@Override
	public void teleopPeriodic() {
			
		drive.arcadeDrive(-joystick.getY(), -joystick.getTwist() - joystick.getY()*0.35);
				
		if(dpadUp.get()){
			
			TeleopActions.moveLift(true, liftDrive);
			
		}
		else if(dpadDown.get()){
			
			TeleopActions.moveLift(false, liftDrive);
			
		}
		
		if (resetEncoders.get()) {
			System.out.println("REset");
			leftDriveEncoder.reset();
		}
		
		/*if(topTrigger.get()){
			
			TeleopActions.intake(true, intakeDrive);
			
		}
		else if(bottomTrigger.get()){
			
			TeleopActions.intake(false, intakeDrive);
			
		}
		else {
			
			intakeDrive.stopMotor();
			
		}
		
		if(liftUp.get() && !isLiftActive && liftState <= 2){
			
			liftState += 1;
			isLiftActive = true;
			
		}
		else if(liftDown.get() && !isLiftActive && liftState >= 1){
			
			liftState -= 1;
			isLiftActive = true;
			
		}
		
		
		isLiftActive = TeleopActions.changeLiftState(isLiftActive, liftState, liftDrive, liftEncoder, previousDistance);	
		previousDistance = liftEncoder.getDistance();*/
		
		/*if(!isLiftActive) {
			
			liftDrive.stopMotor();
			
		}*/
		
	}


	@Override
	public void testPeriodic() {
		
		/*if(limitSwitch.get()) {
			
			System.out.println(".____.");
		}else {
			
			System.out.println("HAJsgjkashdjkhasd");
		}*/
		
	}
	
}
