package org.usfirst.frc.team4186.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;

import com.kauailabs.navx.frc.AHRS;

import org.usfirst.frc.team4186.robot.commands.TurnToAngle;
import org.usfirst.frc.team4186.robot.commands.ChangeLiftState;
import org.usfirst.frc.team4186.robot.commands.DistanceFromTarget;
import org.usfirst.frc.team4186.robot.TeleopActions;


public class Robot extends TimedRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCenterAuto = "Center";
	private static final String kLeftAuto = "Left";
	private static final String kRightAuto = "Right";

	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	private Joystick joystick = new Joystick(0);
	
	JoystickButton dpadUp = new JoystickButton(joystick, 20);
	JoystickButton dpadDown = new JoystickButton(joystick, 22);
	JoystickButton topTrigger = new JoystickButton(joystick, 1);
	JoystickButton bottomTrigger = new JoystickButton(joystick, 6);
	JoystickButton liftUp = new JoystickButton(joystick, 3);
	JoystickButton liftDown = new JoystickButton(joystick, 4);
		
	WPI_TalonSRX talon1 = new WPI_TalonSRX(1); //left primary
	WPI_TalonSRX talon8 = new WPI_TalonSRX(8); //right primary
	
	WPI_TalonSRX talon2 = new WPI_TalonSRX(2); //left auxiliary
	WPI_TalonSRX talon4 = new WPI_TalonSRX(4); //left auxiliary
	
	WPI_TalonSRX talon6 = new WPI_TalonSRX(6); //right auxiliary
	WPI_TalonSRX talon7 = new WPI_TalonSRX(7); //right auxiliary
	
	WPI_VictorSPX victor1 = new WPI_VictorSPX(10); //lift left
	WPI_VictorSPX victor2 = new WPI_VictorSPX(11); //lift right
	
	WPI_TalonSRX intakeTalon1 = new WPI_TalonSRX(3);
	WPI_TalonSRX intakeTalon2 = new WPI_TalonSRX(9);


	Encoder liftEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k2X);
	boolean isLiftActive = false;
	int liftState = 0;
	double previousDistance;
	
	Encoder leftDriveEncoder = new Encoder(4, 5, false, Encoder.EncodingType.k2X);
	
	Ultrasonic sonar = new Ultrasonic(0, 1, Ultrasonic.Unit.kMillimeters);
	AHRS navx = new AHRS(SPI.Port.kMXP);
	
	DifferentialDrive drive = new DifferentialDrive(talon1, talon8);
	DifferentialDrive liftDrive = new DifferentialDrive(victor1, victor2);
	DifferentialDrive intakeDrive = new DifferentialDrive(intakeTalon1, intakeTalon2);
	
	char gameData;
	
	private Command leftStartLeftSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 2, liftEncoder));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.025));
		//commandGroup.addSequential(new TurnToAngle(drive, navx, -90.0));
		//commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 1, liftEncoder));
		
		return commandGroup;
	}
	
	private Command leftStartRightSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.025));
		
		return commandGroup;
	}
	
	private Command rightStartRightSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new ChangeLiftState(true, liftDrive, 2, liftEncoder));
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.025));
		
		return commandGroup;
	}
	
	private Command rightStartLeftSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 0.025));
		
		return commandGroup;
	}
	
	private Command autonomous;
	
	
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("Center Auto", kCenterAuto);
		m_chooser.addObject("Left Auto", kLeftAuto);
		m_chooser.addObject("Right Auto", kRightAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		joystick.setThrottleChannel(2);
        joystick.setTwistChannel(5);
        
		sonar.setAutomaticMode(true);
                
        talon2.follow(talon1);
		talon4.follow(talon1);
		talon6.follow(talon8);
		talon7.follow(talon8);
		
		talon1.setInverted(false);
		talon2.setInverted(false);
		talon4.setInverted(false);
		talon8.setInverted(false);
		talon6.setInverted(false);
		talon7.setInverted(false);
		
		CameraServer.getInstance().startAutomaticCapture(0);
		CameraServer.getInstance().startAutomaticCapture(1);
		
		liftEncoder.setMaxPeriod(0.01);
		liftEncoder.setMinRate(20);
		liftEncoder.setDistancePerPulse(1); 
		liftEncoder.setReverseDirection(false);
		liftEncoder.setSamplesToAverage(1);
		
		leftDriveEncoder.setMaxPeriod(0.01);
		leftDriveEncoder.setMinRate(10);
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
			case kCenterAuto:
				
				break;
			case kLeftAuto:
				
				autonomous = leftStartLeftSwitch();
				
				break;
			case kRightAuto:
				
				autonomous = rightStartLeftSwitch();
				
				break;
			}
		case 'R':
			switch(m_autoSelected) {
			case kCenterAuto:
				
				break;
			case kLeftAuto:
				
				autonomous = leftStartRightSwitch();
				
				break;
			case kRightAuto:
				
				autonomous = rightStartRightSwitch();
				
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
		
		if(!isLiftActive) {
			
			liftDrive.stopMotor();
			
		}
		
	}


	@Override
	public void testPeriodic() {
		
	}
	
}
