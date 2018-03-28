package org.usfirst.frc.team4186.robot;

import org.usfirst.frc.team4186.robot.commands.ChangeLiftState;
import org.usfirst.frc.team4186.robot.commands.DistanceFromTarget;
import org.usfirst.frc.team4186.robot.commands.DriveEncoder;
import org.usfirst.frc.team4186.robot.commands.DriveForTime;
import org.usfirst.frc.team4186.robot.commands.ManualMotorControl;
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
import edu.wpi.first.wpilibj.command.ConditionalCommand;
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
		JoystickButton liftStateUp = new JoystickButton(joystick, 9);
		JoystickButton liftStateDown = new JoystickButton(joystick, 10);
		//JoystickButton gantryStateUp = new JoystickButton(joystick, 11);
		//JoystickButton gantryStateDown = new JoystickButton(joystick, 12);
		JoystickButton parallelUp = new JoystickButton(joystick, 13);
		JoystickButton parallelDown = new JoystickButton(joystick, 14);


	Encoder liftEncoder = new Encoder(8, 9, false, Encoder.EncodingType.k2X); //6,7 on dinky
	
	Encoder leftDriveEncoder = new Encoder(4, 5, false, Encoder.EncodingType.k2X); //switch left and right ports on Dinky
	Encoder rightDriveEncoder = new Encoder(6, 7, false, Encoder.EncodingType.k2X); //2, 3 on dinky
	
	//Encoder armEncoder = new Encoder(8,9);
	
	DigitalInput limitSwitchLower = new DigitalInput(10);
	DigitalInput limitSwitchUpper = new DigitalInput(11);
	DigitalInput limitSwitchGantryBottom = new DigitalInput(12);
	DigitalInput limitSwitchGantryTop = new DigitalInput(13);
	
	Ultrasonic sonar = new Ultrasonic(0, 1, Ultrasonic.Unit.kMillimeters); //8,9 on dinky
	Ultrasonic sonar2 = new Ultrasonic(2, 3, Ultrasonic.Unit.kMillimeters); //delete on dinky
	AHRS navx = new AHRS(SPI.Port.kMXP);
	
	DifferentialDrive drive; 
	SpeedController liftDrive;
	DifferentialDrive intakeDrive;
	SpeedController armMotor;
	
	String gameData;
	
	Command liftCommand;
		Command liftDefaultCommand;
		Command liftExchangeCommand;
		Command liftSwitchCommand;
		Command liftScaleCommand;
		
	Command gantryCommand;
		/*Command gantryDefaultCommand;
		Command gantryCarryCommand;
		Command gantrySwitchCommand; 
		Command gantryScaleCommand;*/
		
	Command moveGantryUp;
	Command moveGantryDown;
	Command moveLiftUp;
	Command moveLiftDown;
	
	CommandGroup gantryLiftUp;
	CommandGroup gantryLiftDown;
	
	CommandGroup resetLift;
		
	
	public RobotBase(MotorFactory motorFactory) {
		
		drive = motorFactory.createDrive();
		liftDrive = motorFactory.createLiftDrive();
		intakeDrive = motorFactory.createIntakeDrive();
		armMotor = motorFactory.createArmMotor();
		
		liftDefaultCommand = new MoveLift(liftDrive, liftEncoder, 0);
		liftExchangeCommand = new MoveLift(liftDrive, liftEncoder, 55);
		liftSwitchCommand = new MoveLift(liftDrive, liftEncoder, 200);
		liftScaleCommand = new MoveLift(liftDrive, liftEncoder, 335);
		
		/*gantryDefaultCommand = new MoveLift(armMotor, armEncoder, 300);
		gantryCarryCommand = new MoveLift(armMotor, armEncoder, 200);
		gantrySwitchCommand = new MoveLift(armMotor, armEncoder, 100);
		gantryScaleCommand = new MoveLift(armMotor, armEncoder, 0);*/
		
		moveGantryUp = new ConditionalCommand(new ManualMotorControl(armMotor, null, -1.0)) {
			
			@Override
			protected boolean condition() {
				
				return true;
				
			}
			
		};
		
		moveGantryDown = new ConditionalCommand(new ManualMotorControl(armMotor, null, 0.4)) {
			
			@Override
			protected boolean condition() {
				
				return true;
				
			}
			
		};
		
		moveLiftUp = new ConditionalCommand(new ManualMotorControl(liftDrive, null /*limitSwitchUpper*/, -1.0)) {
			
			@Override
			protected boolean condition() {
				
				return liftCommand == null || !liftCommand.isRunning();
				
			}
			
		};
		
		moveLiftDown = new ConditionalCommand(new ManualMotorControl(liftDrive, null /*limitSwitchLower*/ , 0.5)) {
			
			@Override
			protected boolean condition() {
				
				return liftCommand == null || !liftCommand.isRunning();
				
			}
			
		};
		
		gantryLiftUp = new CommandGroup();
		gantryLiftUp.addParallel(new ManualMotorControl(liftDrive, null, -1.0));
		gantryLiftUp.addParallel(new ManualMotorControl(armMotor, limitSwitchGantryTop, -1.0));
		
		gantryLiftDown = new CommandGroup();
		gantryLiftDown.addParallel(new ManualMotorControl(liftDrive, null, 1.0));
		gantryLiftDown.addParallel(new ManualMotorControl(armMotor, limitSwitchGantryBottom, 0.4));
		
		resetLift = new CommandGroup();
		resetLift.addSequential(new ManualMotorControl(liftDrive, limitSwitchLower, 0.15));
		resetLift.addSequential(new InstantCommand() {
			
			@Override
			protected void initialize() {
				
				liftEncoder.reset();
				
			}
			
		});
		
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
		
		//CameraServer.getInstance().startAutomaticCapture(0);
		//CameraServer.getInstance().startAutomaticCapture(1); //uncomment
		
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
		
		if(autonomous != null) {
			
			autonomous.cancel();
			autonomous = null;
			
		}
		
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
	
	
	private enum GantryState {
		
		GANTRY_DEFAULT,
		GANTRY_CARRY,
		GANTRY_SWITCH,
		GANTRY_SCALE;
		
		GantryState nextState() {
			
			switch(this) {
			case GANTRY_DEFAULT:
				
				return GANTRY_CARRY;
			
			case GANTRY_CARRY:
				
				return GANTRY_SWITCH;
				
			case GANTRY_SWITCH:
				
				return GANTRY_SCALE;
				
			case GANTRY_SCALE:
				
				return GANTRY_SCALE;
			
			}
			
			return GANTRY_DEFAULT;
			
		}
		
		GantryState previousState() {
			
			switch(this) {
			case GANTRY_DEFAULT:
				
				return GANTRY_DEFAULT;
			
			case GANTRY_CARRY:
				
				return GANTRY_DEFAULT;
				
			case GANTRY_SWITCH:
				
				return GANTRY_CARRY;
				
			case GANTRY_SCALE:
				
				return GANTRY_SWITCH;
			
			}
			
			return GANTRY_DEFAULT;
			
		}
		
	}
	
	private LiftState liftState;
	private GantryState gantryState;
	
	private LiftState changeLiftState(LiftState liftState) {
		
		System.out.println(liftState);
				
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
	
	/*private GantryState changeGantryState(GantryState gantryState) {
		
		if(gantryCommand != null) {
			
			gantryCommand.cancel();
			
		}
		
		switch(gantryState) {
		case GANTRY_DEFAULT:
			
			gantryCommand = gantryDefaultCommand;
			break;
			
		case GANTRY_CARRY:
			
			gantryCommand = gantryCarryCommand;
			break;
			
		case GANTRY_SWITCH:
			
			gantryCommand = gantrySwitchCommand;
			break;
			
		case GANTRY_SCALE:
			
			gantryCommand = gantryScaleCommand;
			break;
			
		default:
			
			gantryCommand = null;
		
		}
		
		return gantryState;
		
	}*/
	

	@Override
	public void teleopInit() {
		
		if(autonomous != null) {
			
			autonomous.cancel();
			autonomous = null;
			
		}
		
		liftState = LiftState.LIFT_DEFAULT;
		gantryState = GantryState.GANTRY_DEFAULT;
		
		leftDriveEncoder.reset();
		
		drive.setSafetyEnabled(false);
		intakeDrive.setSafetyEnabled(false);
		
		liftDrive.stopMotor();
		
		liftStateUp.whenPressed(new InstantCommand() {
			
			@Override
			protected void initialize() {
				
				System.out.println(liftState);
				liftState = changeLiftState(liftState.nextState());
				
			}
			
		});
		
		liftStateDown.whenPressed(new InstantCommand() {
			
			@Override
			protected void initialize() {
				
				System.out.println(liftState);
				liftState = changeLiftState(liftState.previousState());
					
			}
			
		});
		
		/*gantryStateUp.whenPressed(new InstantCommand(){
			
			@Override
			protected void initialize() {
				
				gantryState = changeGantryState(gantryState.nextState());
				
			}
			
		});
		
		gantryStateDown.whenPressed(new InstantCommand(){
			
			@Override
			protected void initialize() {
				
				gantryState = changeGantryState(gantryState.previousState());
				
			}
			
		});*/
		
			
		dpadUp.whenPressed(moveGantryUp);
		dpadUp.whenReleased(new InstantCommand() {
					
			@Override
			protected void initialize() {
						
				moveGantryUp.cancel();
						
			}
					
		});
				
		dpadDown.whenPressed(moveGantryDown);
		dpadDown.whenReleased(new InstantCommand() {
					
			@Override
			protected void initialize() {
						
				moveGantryDown.cancel();
					
			}
					
		});
			
		liftUp.whenPressed(moveLiftUp);
		liftUp.whenReleased(new InstantCommand() {
				
			@Override
			protected void initialize() {
					
				moveLiftUp.cancel();
					
			}
				
		});
			
		liftDown.whenPressed(moveLiftDown);
		liftDown.whenReleased(new InstantCommand() {
				
			@Override
			protected void initialize() {
					
				moveLiftDown.cancel();
					
			}
				
		});
			
		
		/*if(liftCommand == null || !liftCommand.isRunning()) {
			
			parallelUp.whenPressed(gantryLiftUp);
			parallelDown.whenReleased(new InstantCommand() {
				
				@Override
				protected void initialize() {
					
					gantryLiftUp.cancel();
					
				}
				
			});
			
			parallelDown.whenPressed(gantryLiftDown);
			parallelDown.whenReleased(new InstantCommand() {
				
				@Override
				protected void initialize() {
					
					gantryLiftDown.cancel();
					
				}
				
			});
			
		}*/
		
		//resetLift.start(); //Uncomment 

	}
	
	
	@Override
	public void teleopPeriodic() {
		
		Scheduler.getInstance().run();
			
		//drive.arcadeDrive(-joystick.getY(), 0.75*(-joystick.getTwist() - joystick.getY()*0.35  );
		drive.arcadeDrive(joystick.getY(), 0.75*(-joystick.getTwist()));
		
		//System.out.println(limitSwitchUpper.get());
		
		/*if(joystick.getPOV() == 0) {
			
			intakeDrive.tankDrive(-1.0, 1.0);
			
		}
		else if(joystick.getPOV() == 180) {
			
			intakeDrive.tankDrive(1.0, -1.0);
			
		}
		else if(joystick.getPOV() == 90) {
			
			intakeDrive.tankDrive(-1.0, -1.0);
			
		}
		else if(joystick.getPOV() == 270) {
			
			intakeDrive.tankDrive(1.0, 1.0);
			
		}
		else {
			
			intakeDrive.tankDrive(0.0, 0.0);
			
		}*/
		
		if(topTrigger.get()) {
			
			intakeDrive.tankDrive(-1.0, 1.0);
			
		}
		else if(bottomTrigger.get()) {
			
			intakeDrive.tankDrive(1.0, -1.0);
			
		}
		else {
			
			intakeDrive.stopMotor();
			
		}
		/*if((liftCommand == null || moveLiftUp == null || moveLiftDown == null) || (!liftCommand.isRunning() && !moveLiftUp.isRunning() && !moveLiftDown.isRunning())) {
			
			liftDrive.;
			
		}*/
		
		/*if(gantryCommand == null) {
			
			if(dpadUp.get()) {
			
				armMotor.set(-0.5);
			
			}
			else if(dpadDown.get()) {
			
				armMotor.set(0.5);
			
			}
			else {
			
				armMotor.stopMotor();
			
			}
			
		}*/
		
		/*if(liftCommand == null) {
			
			if(liftUp.get()) {
			
				liftDrive.set(-0.3);
			
			}
			else if(liftDown.get()) {
				
				liftDrive.set(0.1);
				
			}
			else {
					
				liftDrive.stopMotor();
				
			}
		}*/
		
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
		
		System.out.println(sonar.getRangeMM());
		
	}
	
}
