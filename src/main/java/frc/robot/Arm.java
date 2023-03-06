package frc.robot;

public class Arm {
    public Robot thisRobot;

    public Arm(Robot thisRobotParameter) {
        thisRobot = thisRobotParameter;
    }

    public void teleopPeriodic() {
        boolean toggleAutomaticControlButtonPressed = thisRobot.opperatorJoystick.getRawButtonPressed(Setting.toggleAutomaticArmControlButtonNum);
        if(toggleAutomaticControlButtonPressed) {
            thisRobot.armAutomaticControl = !thisRobot.armAutomaticControl;
            thisRobot.clawAutomaticControl = !thisRobot.clawAutomaticControl;
            thisRobot.wristAutomaticControl = !thisRobot.wristAutomaticControl;
            thisRobot.armGoal = thisRobot.armPosition;
            thisRobot.wristGoal = thisRobot.wristPosition;
            thisRobot.clawGoal = thisRobot.clawPosition;
        }

        //If toggle button pressed on the operator jostick, toggle cube cone mode
        boolean toggleCubeConeButtonPressed = thisRobot.opperatorJoystick.getRawButtonPressed(Setting.toggleConeCubeModeButton);
        if(toggleCubeConeButtonPressed){
            thisRobot.armInConeMode = !thisRobot.armInConeMode;
        }

        //toggel claw open close
        boolean toggleClawButtonPressed = thisRobot.opperatorJoystick.getRawButtonPressed(Setting.toggleClawPositionButton);
        if(toggleClawButtonPressed){
            thisRobot.isClawClosed = !thisRobot.isClawClosed;
        }

        //if in cone mode, use cone setpoints. If in cube mode use cube setpoints
        if(thisRobot.armInConeMode){
            
            if(thisRobot.isClawClosed){
                thisRobot.clawGoal = Setting.clawClosedConePos;
            } else {
                thisRobot.clawGoal = Setting.clawOpenConePos;
            }

            boolean pickupFloorButtonPressed = thisRobot.opperatorJoystick.getRawButtonPressed(Setting.pickUpFloorButtonNum);
            if(pickupFloorButtonPressed){
                thisRobot.armGoal = Setting.conePickupFlrArmPosition;
                thisRobot.wristGoal = Setting.conePickupFlrWristPosition;
            }

            boolean pickupHPButtonPressed = thisRobot.opperatorJoystick.getRawButtonPressed(Setting.pickUpHPButtonNum);
            if(pickupHPButtonPressed){
                thisRobot.armGoal = Setting.conePickupHPSArmPosition;
                thisRobot.wristGoal = Setting.conePickupHPSWristPosition;
            }

            boolean scoreHighButtonPressed = thisRobot.opperatorJoystick.getRawButtonPressed(Setting.scoreHighButtonNum);
            if(scoreHighButtonPressed){
                thisRobot.armGoal = Setting.conePlaceHighArmPosition;
                thisRobot.wristGoal = Setting.conePlaceHighWristPosition;
            }

            boolean scoreMidButtonPressed = thisRobot.opperatorJoystick.getRawButtonPressed(Setting.scoreMidButtonNum);
            if(scoreMidButtonPressed){
                thisRobot.armGoal = Setting.conePlaceMedArmPosition;
                thisRobot.wristGoal = Setting.conePlaceMedWristPosition;
            }

        } else{
            //all cube positions
            if(thisRobot.isClawClosed){
                thisRobot.clawGoal = Setting.clawClosedCubePos;
            } else {
                thisRobot.clawGoal = Setting.clawOpenCubePos;
            }

            boolean pickupFloorButtonPressed = thisRobot.opperatorJoystick.getRawButtonPressed(Setting.pickUpFloorButtonNum);
            if(pickupFloorButtonPressed){
                thisRobot.armGoal = Setting.cubePickupFlrArmPosition;
                thisRobot.wristGoal = Setting.cubePickupFlrWristPosition;
            }

            boolean pickupHPButtonPressed = thisRobot.opperatorJoystick.getRawButtonPressed(Setting.pickUpHPButtonNum);
            if(pickupHPButtonPressed){
                thisRobot.armGoal = Setting.cubePickupHPSArmPosition;
                thisRobot.wristGoal = Setting.cubePickupHPSWristPosition;
            }

            boolean scoreHighButtonPressed = thisRobot.opperatorJoystick.getRawButtonPressed(Setting.scoreHighButtonNum);
            if(scoreHighButtonPressed){
                thisRobot.armGoal = Setting.cubePlaceHighArmPosition;
                thisRobot.wristGoal = Setting.cubePlaceHighWristPosition;
            }

            boolean scoreMidButtonPressed = thisRobot.opperatorJoystick.getRawButtonPressed(Setting.scoreMidButtonNum);
            if(scoreMidButtonPressed){
                thisRobot.armGoal = Setting.cubePlaceMedArmPosition;
                thisRobot.wristGoal = Setting.cubePlaceMedWristPosition;
            }
        }

        moveArm();
        
    }
    
    public void moveArm(){
        if(thisRobot.armAutomaticControl) {
            double armPower = thisRobot.armPID.calculate(thisRobot.armPosition, thisRobot.armGoal);
            thisRobot.armMotor.set(armPower);
        }
        else {
            boolean armForward = thisRobot.manualJoystick.getRawButton(Setting.armForwardButtonNum);
            boolean armBackward = thisRobot.manualJoystick.getRawButton(Setting.armBackwardButtonNum);
            if(armForward) {
                thisRobot.armMotor.set(Setting.armForwardPower);
            }
            if(armBackward) {
                thisRobot.armMotor.set(Setting.armReversePower);
            }
            if(!(armForward||armBackward)) {
                thisRobot.armMotor.set(0);
            }
        }

        
        if(thisRobot.wristAutomaticControl) {
            double wristPower = thisRobot.wristPID.calculate(thisRobot.wristPosition, thisRobot.wristGoal);
            thisRobot.wristMotor.set(wristPower);
        }
        else {
            boolean wristForward = thisRobot.manualJoystick.getRawButton(Setting.wristForwardButtonNum);
            boolean wristBackward = thisRobot.manualJoystick.getRawButton(Setting.wristBackwardButtonNum);
            if(wristForward) {
                thisRobot.wristMotor.set(Setting.wristForwardPower);
            }
            if(wristBackward) {
                thisRobot.wristMotor.set(Setting.wristReversePower);
            }
            if(!(wristForward||wristBackward)) {
                thisRobot.wristMotor.set(0);
            }
        }

        if(thisRobot.clawAutomaticControl) {
            double clawPower = thisRobot.clawPID.calculate(thisRobot.clawPosition, thisRobot.clawGoal);
            thisRobot.clawMotor.set(clawPower);
        }
        else {
            boolean clawForward = thisRobot.manualJoystick.getRawButton(Setting.clawForwardButtonNum);
            boolean clawBackward = thisRobot.manualJoystick.getRawButton(Setting.clawBackwardButtonNum);
            if(clawForward) {
                thisRobot.clawMotor.set(Setting.clawForwardPower);
            }
            if(clawBackward) {
                thisRobot.clawMotor.set(Setting.clawReversePower);
            }
            if(!(clawForward||clawBackward)) {
                thisRobot.clawMotor.set(0);
            }
        }
    }
}
