// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  final CANSparkMax motor_left = new CANSparkMax(1, MotorType.kBrushless);
  final CANSparkMax motor_right = new CANSparkMax(2, MotorType.kBrushless);

  final XboxController controller = new XboxController(0);

  double motor_left_speed, motor_right_speed;
  double motor_increase = 10;

  double kP = 0.000125, kI = 2e-8, kD = 0;
  double kF = 0.00017;


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    motor_left.restoreFactoryDefaults();
    motor_right.restoreFactoryDefaults();

    motor_left.setSmartCurrentLimit(35);
    motor_right.setSmartCurrentLimit(35);

    motor_left.setIdleMode(IdleMode.kCoast);
    motor_right.setIdleMode(IdleMode.kCoast);

    motor_left.getPIDController().setP(kP);
    motor_left.getPIDController().setI(kI);
    motor_left.getPIDController().setD(kD);
    motor_right.getPIDController().setP(kP);
    motor_right.getPIDController().setI(kI);
    motor_right.getPIDController().setD(kD);

    motor_left.getPIDController().setSmartMotionMaxVelocity(2000, 0);
    motor_right.getPIDController().setSmartMotionMaxVelocity(2000, 0);

    motor_right.setInverted(true);


  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Left motor velocity (RPM)", motor_left.getEncoder().getVelocity());
    SmartDashboard.putNumber("Right motor target velocity (RPM)", motor_right_speed);
    SmartDashboard.putNumber("Right votor velocity (RPM)", motor_right.getEncoder().getVelocity());
    SmartDashboard.putNumber("Left motor target velocity (RPM)", motor_left_speed);
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    motor_left.set(0);
    motor_right.set(0);

  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if(controller.getRightBumper() && motor_right_speed < 2000) {
      motor_right_speed += motor_increase;
      
    }
    if(controller.getBButton() && motor_right_speed > 0) motor_right_speed -= motor_increase;
    if(controller.getLeftBumper() && motor_left_speed < 2000) motor_left_speed += motor_increase;
    if(controller.getXButton() && motor_left_speed > 0) motor_left_speed -= motor_increase;

    if(controller.getAButton()){
      motor_left_speed = 0;
      motor_right_speed = 0;
    }

    if(motor_left_speed <= 0) motor_left.set(0);
    if(motor_right_speed <= 0) motor_right.set(0);

    motor_left.getPIDController().setReference(motor_left_speed, ControlType.kVelocity, 0, motor_left_speed*kF);
    motor_right.getPIDController().setReference(motor_right_speed, ControlType.kVelocity, 0, motor_right_speed*kF);
    
  }
  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
