package org.usfirst.frc.team540.robot;         //huguenot bot code

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	Talon left1, left2, right1, right2;
	Joystick leftStick, rightStick;
	ADXRS450_Gyro gyro;
	AnalogInput ultrasonic;
	double distance, cameraAlign;
	NetworkTable table;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		left1 = new Talon(0);
		left2 = new Talon(1);
		right1 = new Talon(2);
		left2 = new Talon(3);
		leftStick = new Joystick(0);
		rightStick = new Joystick(1);
		gyro = new ADXRS450_Gyro();
		ultrasonic = new AnalogInput(0);
		distance = ultrasonic.getVoltage()/0.009765625;
		table = NetworkTable.getTable("table");
	    NetworkTable.setServerMode();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		autoSelected = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);

	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		cameraAlign = table.getNumber("align", 1);
		switch (autoSelected) {
		case customAuto:
			if (cameraAlign < 0.1){ // do nothing, this is where the correction would be put in when we know
				left1.set(1);
				left2.set(1);
				right1.set(-1);
				right2.set(-1);
			}
			else{
				left1.set(0);
				left2.set(0);
				right1.set(0);
				right2.set(0);
			}
			break;
		case defaultAuto:
		default:
			left1.set(0);
			left2.set(0);
			right1.set(0);
			right2.set(0);
			break;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		double yLeft = leftStick.getY();
		double yRight = -rightStick.getY();

		if(Math.abs(yLeft) > 0.2) {
			left1.set(yLeft);
			left2.set(yLeft);
		}
		else {
			left1.set(0);
			left2.set(0);
		}

		if(Math.abs(yRight) > 0.2) {
			right1.set(yRight);
			right2.set(yRight);
		}
		else {
			right1.set(0);
			right2.set(0);
		}

		System.out.println(gyro.getAngle()); //prints angle up to 360, where 0 is the initial "forward" position until reset
		System.out.println(distance); // prints distance in inches
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
}

