package org.usfirst.frc.team4186.robot;

import org.usfirst.frc.team4186.robot.commands.ChangeLiftState;
import org.usfirst.frc.team4186.robot.commands.DistanceFromTarget;
import org.usfirst.frc.team4186.robot.commands.DriveEncoder;
import org.usfirst.frc.team4186.robot.commands.DriveForTime;
import org.usfirst.frc.team4186.robot.commands.MoveLift;
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
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;
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
	
	Encoder leftDriveEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k2X);
	
	Encoder armEncoder = new Encoder(8,9);
	
	Ultrasonic sonar = new Ultrasonic(0, 1, Ultrasonic.Unit.kMillimeters);
	AHRS navx = new AHRS(SPI.Port.kMXP);
	
	DifferentialDrive drive; 
	SpeedController liftDrive;
	DifferentialDrive intakeDrive;
	SpeedController armMotor;
	
	String gameData;
	
	Command liftDefaultCommand;
	Command liftExchangeCommand;
	Command liftSwitchCommand;
	Command liftScaleCommand;
	
	Command liftCommand;
	
	public RobotBase(MotorFactory motorFactory) {
		
		drive = motorFactory.createDrive();
		liftDrive = motorFactory.createLiftDrive();
		intakeDrive = motorFactory.createIntakeDrive();
		armMotor = motorFactory.createArmMotor();
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
		liftEncoder.setReverseDirection(true);
		liftEncoder.setSamplesToAverage(1);
		liftEncoder.setPIDSourceType(PIDSourceType.kDisplacement);
		
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
	
	private enum LiftState {
		
		LIFT_DEFAULT,
		LIFT_EXCHANGE,
		LIFT_SWITCH,
		LIFT_SCALE;
		
		LiftState nextState() {
			
			switch(this) {
			case LIFT_DEFAULT:
				
				return LIFT_EXCHANGE;
				
			case LIFT_EXCHANGE:
				
				return LIFT_SWITCH;
				
			case LIFT_SWITCH:
			
				return LIFT_SCALE;
				
			case LIFT_SCALE:
				
				return LIFT_SCALE;
				
			}
			
			return LIFT_DEFAULT;
			
		}
		
		LiftState previousState() {
			
			switch(this) {
			case LIFT_DEFAULT:
				
				return LIFT_DEFAULT;
				
			case LIFT_EXCHANGE:
				
				return LIFT_DEFAULT;
				
			case LIFT_SWITCH:
			
				return LIFT_EXCHANGE;
				
			case LIFT_SCALE:
				
				return LIFT_SWITCH;
				
			}
			
			return LIFT_DEFAULT;
			
		}
		
	}
	
	private LiftState liftState = LiftState.LIFT_DEFAULT;
	
	private LiftState changeLiftState(LiftState liftState) {
				
		if(liftCommand != null) {
			
			liftCommand.cancel();
			
		}
		
		switch(liftState) {
		case LIFT_DEFAULT:
			
			liftCommand = liftDefaultCommand;
			break;
			
		case LIFT_EXCHANGE:
			
			liftCommand = liftExchangeCommand;
			break;
		
		case LIFT_SWITCH:
			
			liftCommand = liftSwitchCommand;
			break;
			
		case LIFT_SCALE:
			
			liftCommand = liftScaleCommand;
			break;
			
		default:
			
			liftCommand = null;
			
		}
		
		if(liftCommand != null) {
			
			liftCommand.start();
			
		}
		
		return liftState;
		
	}

	@Override
	public void teleopInit() {
		
		//liftEncoder.reset();
		leftDriveEncoder.reset();
		
		drive.setSafetyEnabled(false);
		intakeDrive.setSafetyEnabled(false);
		
		liftDrive.stopMotor();
		
		liftDefaultCommand = new MoveLift(liftDrive, liftEncoder, 0);
		liftExchangeCommand = new MoveLift(liftDrive, liftEncoder, 50);
		liftSwitchCommand = new MoveLift(liftDrive, liftEncoder, 200);
		liftScaleCommand = new MoveLift(liftDrive, liftEncoder, 335);
		
		liftUp.whenPressed(new InstantCommand() {
			
			@Override
			protected void initialize() {
				
				liftState = changeLiftState(liftState.nextState());
				
			}
			
		});
		
		liftDown.whenPressed(new InstantCommand() {
			
			@Override
			protected void initialize() {
				
				liftState = changeLiftState(liftState.previousState());
					
			}
			
		});

	}
	
	
	@Override
	public void teleopPeriodic() {
		
		Scheduler.getInstance().run();
			
		drive.arcadeDrive(joystick.getY(), -joystick.getTwist() - joystick.getY()*0.35);
		
		if(joystick.getPOV() == 0) {
			
			intakeDrive.tankDrive(1.0, -1.0);
			
		}
		else if(joystick.getPOV() == 180) {
			
			intakeDrive.tankDrive(-1.0, 1.0);
			
		}
		else if(joystick.getPOV() == 90) {
			
			intakeDrive.tankDrive(1.0, 1.0);
			
		}
		else if(joystick.getPOV() == 270) {
			
			intakeDrive.tankDrive(-1.0, -1.0);
			
		}
		else {
			
			intakeDrive.tankDrive(0.0, 0.0);
			
		}
		
		
		if(dpadUp.get()) {
			
			armMotor.set(-0.5);
			
		}
		else if(dpadDown.get()) {
			
			armMotor.set(0.5);
			
		}
		else {
			
			armMotor.stopMotor();
			
		}
		
	}

	@Override
	public void disabledPeriodic() {
		if(liftDefaultCommand != null) liftDefaultCommand.cancel();
		if(liftExchangeCommand != null) liftExchangeCommand.cancel();
		if(liftSwitchCommand != null) liftSwitchCommand.cancel();
		if(liftScaleCommand != null) liftScaleCommand.cancel(); 
	}
	
	@Override
	public void testPeriodic() {
		
	}
	
}
