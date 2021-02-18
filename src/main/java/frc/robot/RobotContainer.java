/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.AutonBarrelRoll;
import frc.robot.commands.AutonBouncePath;
import frc.robot.commands.DriveGTA;
import frc.robot.commands.GetInRange;
import frc.robot.commands.Move;
import frc.robot.commands.MoveStraight;
import frc.robot.commands.SeekLeft;
import frc.robot.commands.SeekRight;
import frc.robot.commands.TurnRight90;
import frc.robot.commands.TurnRight;
import frc.robot.commands.TurnToYawZero;
import frc.robot.commands.ZeroYawAndTurnRight;
import frc.robot.subsystems.BatteryVoltage;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.NavX;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  //private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  //private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);
  public static XboxController driverController;
  //public static XboxController operatorController;

  private final DriveTrain driveTrain;
  private final DriveGTA driveGTA;
  //private final MecDriveTrain mecDriveTrain;
  //private final DriveMec driveMec;

  private final SeekLeft seekLeft;
  private final SeekRight seekRight;
  private final GetInRange getInRange;

  private final Move move;

  private final NavX navX;
  private final TurnToYawZero turnToYawZero;
  private final TurnRight90 turnRight90;
  private final TurnRight turnRight;
  private final ZeroYawAndTurnRight zeroYawAndTurnRight;
  private final MoveStraight moveStraight;

  private final BatteryVoltage batteryVoltage;
  
  AutonBarrelRoll autonBarrelRoll;
  AutonBouncePath autonBouncePath;
  SendableChooser<Command> chooser = new SendableChooser<>();
  //Smart Dashboard cannot be set to "Editable" if you want to select an option for auton

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    driverController = new XboxController(Constants.driverControllerPort);
    //operatorController = new XboxController(Constants.operatorControllerPort);
    

    driveTrain = new DriveTrain();
    driveGTA = new DriveGTA(driveTrain);
    driveGTA.addRequirements(driveTrain);
    driveTrain.setDefaultCommand(driveGTA);
    //configure buttons was here

    //mecDriveTrain = new MecDriveTrain();
    //driveMec = new DriveMec(mecDriveTrain);
    //driveMec.addRequirements(mecDriveTrain);
    //mecDriveTrain.setDefaultCommand(driveMec);

    seekLeft = new SeekLeft(driveTrain);
    seekLeft.addRequirements(driveTrain);
    //seekLeft.addRequirements(limelight);
    seekRight = new SeekRight(driveTrain);
    seekRight.addRequirements(driveTrain);
    //seekRight.addRequirements(limelight);
    getInRange = new GetInRange(driveTrain);
    getInRange.addRequirements(driveTrain);

    move = new Move(driveTrain);
    move.addRequirements(driveTrain);

    navX = new NavX(); //NavX class must be instantiated or the code will never run and it wont give values
    turnToYawZero = new TurnToYawZero(driveTrain);
    turnToYawZero.addRequirements(driveTrain, navX); 
    //just added this requirement so i dont get an error saying that navX is not used. Is not needed
    turnRight90 = new TurnRight90(driveTrain);
    turnRight90.addRequirements(driveTrain);
    turnRight = new TurnRight(driveTrain);
    turnRight.addRequirements(driveTrain);
    zeroYawAndTurnRight = new ZeroYawAndTurnRight(driveTrain);
    zeroYawAndTurnRight.addRequirements(driveTrain);
    moveStraight = new MoveStraight(driveTrain);
    moveStraight.addRequirements(driveTrain);

    batteryVoltage = new BatteryVoltage();

    autonBarrelRoll = new AutonBarrelRoll(driveTrain);
    autonBarrelRoll.addRequirements(driveTrain);
    autonBouncePath = new AutonBouncePath(driveTrain);
    autonBouncePath.addRequirements(driveTrain, batteryVoltage); //doesnt actually need battery, I just dont want errors

    chooser.addOption("Auton Barrel Roll", autonBarrelRoll);
    chooser.addOption("Auton Bounce Path", autonBouncePath);
    chooser.setDefaultOption("Move (is default)", move);
    SmartDashboard.putData("Auton Chooser", chooser);

    configureButtonBindings();
  }

  public double getDriverRawAxis(final int axis){
    try {
      return driverController.getRawAxis(axis);
    }
    catch(final RuntimeException exception) {
      DriverStation.reportError("Error getting raw axis because: " + exception.getMessage(), true);
    }
    //this error might have something to do with the squared values in DriveGTA
    return 0;
  }

  public double getDriverDeadzoneAxis(final int axis){
    try {
    final double rawValue = driverController.getRawAxis(axis);
    return (Math.abs(rawValue) <= Constants.deadzone) ? 0.0 : rawValue;
    }
    catch(final RuntimeException exception) {
      DriverStation.reportError("Error getting raw axis or returning deadzone axis because: " + exception.getMessage(), true);
    }
    return 0;
  }
  /*
  public double getOperatorDeadzoneAxis(int axis){
    double rawValue = operatorController.getRawAxis(axis);
    return Math.abs(rawValue) < Constants.deadzone ? 0.0 : rawValue;
  }
  */

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    final JoystickButton limelightTargetButton = new JoystickButton(driverController, Constants.limelightTargetButton);
    //limelightTargetButton.whileHeld(new SeekLeft(driveTrain));
    limelightTargetButton.whileHeld(new SeekRight(driveTrain));

    //final JoystickButton moveButton = new JoystickButton(driverController, Constants.moveButton);
    //moveButton.whenPressed(new Move(driveTrain, 0.3, -0.3, 1));

    final JoystickButton limelightGetInRangeButton = new 
      JoystickButton(driverController, Constants.limelightGetInRangeButton);
    limelightGetInRangeButton.whileHeld(new GetInRange(driveTrain));

    final JoystickButton zeroYawButton = new JoystickButton(driverController, Constants.zeroYawButton);
    zeroYawButton.whenPressed(NavX::zeroGyroYaw); //this is a method reference

    final JoystickButton turnToYawZeroButton = new JoystickButton(driverController, Constants.turnToYawZeroButton);
    turnToYawZeroButton.whileHeld(new TurnToYawZero(driveTrain));

    final JoystickButton turnRightButton = new JoystickButton(driverController, Constants.turnRightButton);
    turnRightButton.whileHeld(new ZeroYawAndTurnRight(driveTrain));

    final JoystickButton moveStraightButton = new JoystickButton(driverController, Constants.moveStraightButton);
    moveStraightButton.whenPressed(new MoveStraight(driveTrain, 0.3, 5));
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return chooser.getSelected();
  }
}
