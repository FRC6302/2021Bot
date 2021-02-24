/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class DriveTrain extends SubsystemBase {
  /*
  NOTE TO NEW CODERS: YOU SHOULD PROBABLY DO THE AP CSA CODEHS COURSE BEFORE TRYING TO CODE 
  FOR ROBOTICS. IT WAS THE MOST HELPFUL COURSE I'VE DONE, AND THE BEGINNING IS FUN WITH KAREL.
  MAKE SURE YOU COMPLETELY UNDERSTAND ALL THE STUFF ABOUT CLASSES, OBJECTS, AND METHODS. YOU CAN
  PROBABLY SKIP SOME OF THE STRING STUFF, BUT IT'S STILL GOOD TO KNOW. GOOD LUCK :) -SAMUEL
  */

  //REFRAIN FROM USING WAIT() FUNCTIONS ANYWHERE IF POSSIBLE BECAUSE IT MESSES WITH THE HOW THE FRC
  //STUFF WORKS AND UPDATES. IT MIGHT CAUSE WEIRD ERRORS

  TalonSRX motorL1;
  TalonSRX motorL2;
  TalonSRX motorR1;
  TalonSRX motorR2;
  
  /**
   * Creates a new DriveTrain.
   */
  public DriveTrain() {
    motorL1 = new TalonSRX(Constants.motorL1Value);
    motorL2 = new TalonSRX(Constants.motorL2Value);
    motorR1 = new TalonSRX(Constants.motorR1Value);
    motorR2 = new TalonSRX(Constants.motorR2Value);
    //motorL1.setInverted(false);
    //motorL2.setInverted(false);
    //motorR1.setInverted(false);
    //motorR2.setInverted(false);

    motorL1.setNeutralMode(NeutralMode.Brake);
    motorL2.setNeutralMode(NeutralMode.Brake);
    motorR1.setNeutralMode(NeutralMode.Brake);
    motorR2.setNeutralMode(NeutralMode.Brake);    
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setLeftMotors(double speed){
    //motorL1.setNeutralMode(NeutralMode.Coast);
    //motorL2.setNeutralMode(NeutralMode.Coast);
  
    motorL1.set(ControlMode.PercentOutput, speed);
    motorL2.set(ControlMode.PercentOutput, speed);
  }

  //right motors have inverted speed bc of how the motors are oriented on robot
  public void setRightMotors(double speed){
    //motorR1.setNeutralMode(NeutralMode.Coast);
    //motorR2.setNeutralMode(NeutralMode.Coast);

    motorR1.set(ControlMode.PercentOutput, -speed);
    motorR2.set(ControlMode.PercentOutput, -speed);
  }

  /*
  //dont know if it would save time to do this instead of Move() and MoveStraight() commands
  public void Move(double leftCommand, double rightCommand, double time) {
  }

  public void MoveStraight(double leftCommand, double rightCommand, double time) {  
  }
  */

  double PIDDivisor = 1;
  public void usePIDOutput(double output) {
    SmartDashboard.putNumber("PIDOutput", output);
    setLeftMotors(output / PIDDivisor);
    setRightMotors(-output / PIDDivisor); //right is neg so it turns right
  }

  public void stopDrive(){
    //motorL1.setNeutralMode(NeutralMode.Brake);
    //motorL2.setNeutralMode(NeutralMode.Brake);
    //motorR1.setNeutralMode(NeutralMode.Brake);
    //motorR2.setNeutralMode(NeutralMode.Brake);    
    
    motorL1.set(ControlMode.PercentOutput, 0);
    motorL2.set(ControlMode.PercentOutput, 0);
    motorR1.set(ControlMode.PercentOutput, 0);
    motorR2.set(ControlMode.PercentOutput, 0);
  }
}
