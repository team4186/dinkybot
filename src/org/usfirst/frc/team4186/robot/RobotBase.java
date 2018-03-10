package org.usfirst.frc.team4186.robot;

import org.usfirst.frc.team4186.robot.commands.ChangeLiftState;
import org.usfirst.frc.team4186.robot.commands.DistanceFromTarget;
import org.usfirst.frc.team4186.robot.commands.DriveEncoder;
import org.usfirst.frc.team4186.robot.commands.DriveForTime;
import org.usfirst.frc.team4186.robot.commands.TurnToAngle;
import org.usfirst.frc.team4186.robot.factory.MotorFactory;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.PrintCommand;
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


	Encoder liftEncoder = new Encoder(6, 7, false, Encoder.EncodingType.k2X);
	boolean isLiftActive = false;
	int liftState = 0;
	double previousDistance;
	
	Encoder leftDriveEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k2X);
	Encoder rightDriveEncoder = new Encoder(4, 5, false, Encoder.EncodingType.k2X);
	
	Ultrasonic sonar = new Ultrasonic(0, 1, Ultrasonic.Unit.kMillimeters);
	AHRS navx = new AHRS(SPI.Port.kMXP);
	
	DifferentialDrive drive; 
	SpeedController liftDrive;
	DifferentialDrive intakeDrive;
	
	AnalogInput longSonar = new AnalogInput(0);
	
	String gameData;
	
	public RobotBase(MotorFactory motorFactory) {
		
		drive = motorFactory.createDrive();
		liftDrive = motorFactory.createLiftDrive();
		intakeDrive = motorFactory.createIntakeDrive();
		
	}
	
	private Command leftInnerStartLeftSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new PrintCommand("0"));
		//commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 2, liftEncoder));
		commandGroup.addSequential(new PrintCommand("1"));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.1));
		commandGroup.addSequential(new PrintCommand("2"));
		commandGroup.addSequential(new DriveForTime(intakeDrive, 3000, 1.0));
		commandGroup.addSequential(new PrintCommand("Finished"));
		
		return commandGroup;
	}
	
	private Command leftInnerStartRightSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new DriveEncoder(drive, leftDriveEncoder, 100, -0.1));
		commandGroup.addSequential(new TurnToAngle(drive, navx, 87.5));
		commandGroup.addSequential(new DriveEncoder(drive, leftDriveEncoder, 228.45, -0.1));
		commandGroup.addSequential(new TurnToAngle(drive, navx, -87.5));
		//commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 2, liftEncoder));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.1));
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
		
		commandGroup.addSequential(new DriveEncoder(drive, leftDriveEncoder, 100, -0.1));
		commandGroup.addSequential(new TurnToAngle(drive, navx, -87.5));
		commandGroup.addSequential(new DriveEncoder(drive, leftDriveEncoder, 228.45, -0.1));
		commandGroup.addSequential(new TurnToAngle(drive, navx, 87.5));
		//commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 2, liftEncoder));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.1));
		commandGroup.addSequential(new DriveForTime(intakeDrive, 3000, 1.0));
		
		return commandGroup;
	}
	
	private Command leftOuterStartLeftSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new DriveEncoder(drive, leftDriveEncoder, 355.6, -0.1));
		commandGroup.addSequential(new TurnToAngle(drive, navx, 87.5));
		//commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 2, liftEncoder));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.1));
		commandGroup.addSequential(new DriveForTime(intakeDrive, 3000, 1.0));
		
		return commandGroup;
	}
	
	private Command leftOuterStartLeftScale() {
		
		CommandGroup commandGroup = new CommandGroup();

		//commandGroup.addSequential(new DriveEncoder(drive, leftDriveEncoder, ?, -0.1));
		commandGroup.addSequential(new TurnToAngle(drive, navx, 87.5));
		//commandGroup.addSequential(new DriveEncoder(drive, leftDriveEncoder, ?, -0.1));
		
		return commandGroup;
	}
	
	private Command rightOuterStartRightScale() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		//commandGroup.addSequential(new DriveEncoder(drive, leftDriveEncoder, ?, -0.1));
		commandGroup.addSequential(new TurnToAngle(drive, navx, -87.5));
		//commandGroup.addSequential(new DriveEncoder(drive, leftDriveEncoder, ?, -0.1));
		
		return commandGroup;
	}
	
	private Command rightOuterStartRightSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new DriveEncoder(drive, leftDriveEncoder, 355.6, -0.1));
		commandGroup.addSequential(new TurnToAngle(drive, navx, -87.5));
		//commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 2, liftEncoder));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.1));
		commandGroup.addSequential(new DriveForTime(intakeDrive, 3000, 1.0));
		
		return commandGroup;
	}
	
	private Command baseline() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new DriveEncoder(drive, leftDriveEncoder, 355.6, -0.1));
		
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
		
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		SmartDashboard.putString("Game Data", DriverStation.getInstance().getGameSpecificMessage());
		
		drive.setSafetyEnabled(false);
		intakeDrive.setSafetyEnabled(false);
		
		liftEncoder.reset();
		
		switch(gameData.charAt(0)) {
		case 'L':
			switch(m_autoSelected) {
			case kLeftOuterAuto:
				
				autonomous = leftOuterStartLeftSwitch();
				SmartDashboard.putString("Auto State", "Left outer; Left switch");
				
				break;
			case kLeftInnerAuto:
				
				autonomous = leftInnerStartLeftSwitch();
				SmartDashboard.putString("Auto State", "Left inner; Left switch");
				
				break;
			case kRightOuterAuto:
				
				SmartDashboard.putString("Auto State", "Right outer; Left switch");
				
				switch(gameData.charAt(1)) {
				case 'R':
					
					autonomous = rightOuterStartRightScale();
					
					break;
				case 'L':
					
					autonomous = baseline();
					
					break;
				}
				
				break;
			case kRightInnerAuto:
				
				autonomous = rightInnerStartLeftSwitch();
				SmartDashboard.putString("Auto State", "Right inner; Left switch");
				
				break;
			}
			break;
		case 'R':
			switch(m_autoSelected) {
			case kLeftOuterAuto:
				
				SmartDashboard.putString("Auto State", "Left outer; Right switch");
				
				switch(gameData.charAt(1)) {
				case 'L':
					
					autonomous = leftOuterStartLeftScale();
					
					break;
				case 'R':
					
					autonomous = baseline();
					
					break;
				}
				
				break;
			case kLeftInnerAuto:
				
				autonomous = leftInnerStartRightSwitch();
				SmartDashboard.putString("Auto State", "Left inner; Right switch");
				
				break;
			case kRightOuterAuto:
				
				autonomous = rightOuterStartRightSwitch();
				SmartDashboard.putString("Auto State", "Right outer; Right switch");
				
				break;
			case kRightInnerAuto:
				
				autonomous = rightInnerStartRightSwitch();
				SmartDashboard.putString("Auto State", "Right inner; Right switch");
				
				break;
			}
			break;
			
			default:
				
				autonomous = baseline();
				
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
			
		drive.arcadeDrive(joystick.getY(), -joystick.getTwist() - joystick.getY()*0.35);
				
		/*if(dpadUp.get()){
			
			TeleopActions.moveLift(true, liftDrive);
			System.out.println(liftEncoder.getDistance());
			
		}
		else if(dpadDown.get()){
			
			TeleopActions.moveLift(false, liftDrive);
			System.out.println(liftEncoder.getDistance());
			
		}
		else {
			
			liftDrive.set(0.0);
		}*/
		
		if(topTrigger.get()){
			
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
		previousDistance = liftEncoder.getDistance();
		
		/*if(!isLiftActive) {
			
			liftDrive.stopMotor();
			
		}*/
		
	}


	@Override
	public void testPeriodic() {
		
	}
	
}
