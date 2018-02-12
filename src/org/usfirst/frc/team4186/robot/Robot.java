/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4186.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

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
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;

import com.kauailabs.navx.frc.AHRS;

import org.usfirst.frc.team4186.robot.commands.TurnToAngle;
import org.usfirst.frc.team4186.robot.commands.DistanceFromTarget;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCenterAuto = "Center";
	private static final String kLeftAuto = "Left";
	private static final String kRightAuto = "Right";

	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	private Joystick joystick = new Joystick(0);
		
	WPI_TalonSRX talon1 = new WPI_TalonSRX(1);
	WPI_TalonSRX talon5 = new WPI_TalonSRX(8);
	
	WPI_TalonSRX talon2 = new WPI_TalonSRX(2);
	WPI_TalonSRX talon3 = new WPI_TalonSRX(4);
	WPI_TalonSRX talon6 = new WPI_TalonSRX(6);
	WPI_TalonSRX talon7 = new WPI_TalonSRX(7);
	
	DifferentialDrive drive = new DifferentialDrive(talon1, talon5);
	
	Ultrasonic sonar = new Ultrasonic(1, 2, Ultrasonic.Unit.kMillimeters);
	AHRS navx = new AHRS(SPI.Port.kMXP);
	
	char gameData;
	
	private Command leftStartLeftSwitch() {
		
		CommandGroup commandGroup = new CommandGroup();
		
		commandGroup.addSequential(new DistanceFromTarget(drive, sonar, 1.0));
		//commandGroup.addSequential(new TurnToAngle(drive, navx, 90.0));
		
		return commandGroup;
	}
	
	private Command autonomous;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
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
		talon3.follow(talon1);
		talon6.follow(talon5);
		talon7.follow(talon5);
		
		talon1.setInverted(false);
		talon2.setInverted(false);
		talon3.setInverted(false);
		talon5.setInverted(false);
		talon6.setInverted(false);
		talon7.setInverted(false);
		
		//CameraServer.getInstance().startAutomaticCapture(0);
		//CameraServer.getInstance().startAutomaticCapture(1);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// m_autoSelected = SmartDashboard.getString("Auto Selector",
		// 		kDefaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
		
		gameData = DriverStation.getInstance().getGameSpecificMessage().charAt(0);
		
		drive.setSafetyEnabled(false);
		
		switch(gameData) {
		case 'L':
			switch(m_autoSelected) {
			case kCenterAuto:
				
				break;
			case kLeftAuto:
				
				autonomous = leftStartLeftSwitch();
				
				break;
			case kRightAuto:
				
				break;
			}
		case 'R':
			switch(m_autoSelected) {
			case kCenterAuto:
				
				break;
			case kLeftAuto:
				
				break;
			case kRightAuto:
				
				break;
			}
		}
		
		if(autonomous != null) {
			
			autonomous.start();
			
		}
 
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		
		Scheduler.getInstance().run();
		
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		
		drive.arcadeDrive(-joystick.getY(), -joystick.getTwist() - joystick.getY()*0.35);
		
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
	
}
