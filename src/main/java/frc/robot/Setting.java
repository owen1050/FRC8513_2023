package frc.robot;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;

public class Setting {

    // drive motor settings
    public static int leftDriveMotor1CANID = 5;
    public static int leftDriveMotor1PDPPort = 0;

    public static int leftDriveMotor2CANID = 6;
    public static int leftDriveMotor2PDPPort = 1;

    public static int leftDriveMotor3CANID = 7;
    public static int leftDriveMotor3PDPPort = 2;

    public static int rightDriveMotor1CANID = 2;
    public static int rightDriveMotor1PDPPort = 15;

    public static int rightDriveMotor2CANID = 3;
    public static int rightDriveMotor2PDPPort = 14;

    public static int rightDriveMotor3CANID = 4;
    public static int rightDriveMotor3PDPPort = 13;

    // wrist settings
    public static int wristMotorCANID = 8;
    public static int wristMotorPDPPort = 8;
    public static MotorType wristMotorType = MotorType.kBrushless;
    public static IdleMode wristMotorIdleMode = IdleMode.kBrake;
    public static int wristMotorCurrentLimit = 25;
    public static Boolean wristMotorInverted = false;
    public static double wristTHold = 3;
    public static double wristForwardPower = 0.5;
    public static double wristReversePower = -0.5;
    public static double armToWristRatio = -1; // this is made up and needs to be determined experimentally

    // arm settings
    public static int armMotorCANID = 1;
    public static int armMotorPDPPort = 9;
    public static MotorType armMotorType = MotorType.kBrushless;
    public static IdleMode armMotorIdleMode = IdleMode.kBrake;
    public static int armMotorCurrentLimit = 30;
    public static Boolean armMotorInverted = false;
    public static double armTHold = 5;
    public static double armForwardPower = 0.5;
    public static double armReversePower = -0.5;
    public static double armMaxSpeed = .5;
    public static double armSlowdownConst = 0.55;

    // claw settings
    public static int clawMotorCANID = 9;
    public static int clawMotorPDPPort = 10;
    public static MotorType clawMotorType = MotorType.kBrushless;
    public static IdleMode clawMotorIdleMode = IdleMode.kBrake;
    public static int clawMotorCurrentLimit = 20;
    public static Boolean clawMotorInverted = false;
    public static double clawTHold = 3;
    public static double clawForwardPower = 0.5;
    public static double clawReversePower = -0.5;

    //drivebase settings
    public static MotorType drivebMotorType = MotorType.kBrushless;
    public static IdleMode drivebaseIdleMode = IdleMode.kBrake;
    public static int drivebaseCurrentLimit = 40;
    public static Boolean leftSideInverted = true;
    public static Boolean rightSideInverted = false;
    public static double drivebaseDistanceTHold = 2;
    public static double drivebaseAngTHold = 3;

    //PDP settings
    public static int PDPCANID = 0;
    public static ModuleType PDPType = ModuleType.kCTRE;

    //joystick settings
    public static int driverJoystickPort = 0;
    public static int opperatorJotstickPort = 1;
    public static int manualJoystickPort = 2;
    public static int driverJoystickDriveAxis = 1;
    public static int driverJoystickTurnAxis = 4;

    // turn PID
    public static double turnPID_p = 0.06;
    public static double turnPID_i = 0.003;
    public static double turnPID_d = 0.005;

    // drive PID
    public static double drivePID_p = 0.06;
    public static double drivePID_i = 0.003;
    public static double drivePID_d = 0.0;

    // arm PID
    public static double armPID_p = 0.125; //0.125
    public static double armPID_i = 0.0;
    public static double armPID_d = 0.003; //0.003

    // claw PID
    public static double clawPID_p = 0.1;
    public static double clawPID_i = 0.001;
    public static double clawPID_d = 0.01;

    // wrist PID
    public static double wristPID_p = 0.13;
    public static double wristPID_i = 0.002;
    public static double wristPID_d = 0.005; //0.001

    // Operator button settings
    public static final int pickUpHPButtonNum = 1;
    public static final int pickUpFloorButtonNum = 2;
    public static final int scoreMidButtonNum = 3;
    public static final int scoreHighButtonNum = 4;
    public static final int toggleConeCubeModeButton = 5;
    public static final int toggleClawPositionButton = 6;
    public static final int toggleAutomaticArmControlButtonNum = 7;

    // Manual Button Settings
    public static final int armForwardButtonNum = 2;
    public static final int armBackwardButtonNum = 3;

    public static final int wristForwardButtonNum = 4;
    public static final int wristBackwardButtonNum = 1;

    public static final int clawForwardButtonNum = 6;
    public static final int clawBackwardButtonNum = 5;

    public static final int armJoystickControlAxis = 1;
    public static final int wristJoystickControlAxis = 5;

    // Driver button settings
    public static final int startingConfigButtonNum = 5;
    public static final int autoBalanceButton = 6;

    //POSITIONS (arm/claw/wrist)
    //pickup cube from floor
    public static double cubePickupFlrArmPosition = 188.0; // 190
    public static double cubePickupFlrWristPosition = cubePickupFlrArmPosition + -103.9; //-101.2 

    //pickup cone from floor
    public static double conePickupFlrArmPosition = 189.0; // 190.4
    public static double conePickupFlrWristPosition = conePickupFlrArmPosition + -102.8; //-101.2

    // pickup cube from human player station
    public static double cubePickupHPSArmPosition = 136;
    public static double cubePickupHPSWristPosition = 41.6; // -94.8

    // pickup cone from human player station
    public static double conePickupHPSArmPosition = 136;
    public static double conePickupHPSWristPosition = conePickupHPSArmPosition + -85; // -94.1

    // place cube on high
    public static double cubePlaceHighArmPosition = 130.0; // 145.0
    public static double cubePlaceHighWristPosition = cubePlaceHighArmPosition + -83.2; // -77.5

    // place cube on med
    public static double cubePlaceMedArmPosition = 145.0; // 158
    public static double cubePlaceMedWristPosition = cubePlaceMedArmPosition + -92.8; // -95.9

    //place cube mid backwards
    public static double cubePlaceBackwardArmPosition = 52; // 47.65
    public static double cubePlaceBackwardWristPosition = cubePlaceBackwardArmPosition + 38.78;
    public static double cubePlaceBackwardClawPosition = -30;

    // place cone on high
    public static double conePlaceHighArmPosition = 133; // 155
    public static double conePlaceHighWristPosition = conePlaceHighArmPosition + -66.7; // -73.4

    // place cone on med
    public static double conePlaceMedArmPosition = 133; // 146
    public static double conePlaceMedWristPosition = conePlaceMedArmPosition + -90; // -95.1

    // claw positions for cone
    public static double clawClosedConePos = -0.05;
    public static double clawOpenConePos = -50;

    // claw positions for cube
    public static double clawClosedCubePos = -7;
    public static double clawOpenCubePos = -45;

    // claw folded range
    public static double armFoldedMin = 10;
    public static double armFoldedMax = 130;
    public static double armTooLowToBringClawIn = 165;
    public static double wristStartingConfigPosition = 0;
    public static double wristFoldedInPosition = 10; // this is the position of the wrist when its folded over the top
    public static double startingConfigPos = 7.5; //arm starting config position

    // CHARGING STATION THRESHOLDS & SPEEDS
    public static double pitchTHold = 10.0;
    public static double PRTHold = 0.15;
    public static double autoBalanceSpeed = 0.3;
    public static double autoBalanceTHold = 1;
    public static double balanceCountTHold = 50;
    public static double inTholdCount = 10;
    public static double maxDrivebasePow = 0.4;
    public static double maxTurnPow = 0.5;

    //LL Settings
    public static double lowerGoalX = -11.2;
    public static double upperGoalX = -11.2;
    public static double midLLY = 0;
    public static int LLButtonNum = 1;

}
