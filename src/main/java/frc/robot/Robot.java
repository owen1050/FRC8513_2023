// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.RobotController;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    //auto strings
    public final String kDefaultAuto = "Default";
    public final String kDriveStraight = "DriveStraightAuto";
    public final String kDriveStraightAndMoveArm = "DriveStraightAndMoveArmAuto";
    public final String kScoreConeAndStation = "ScoreConeAndStationAuto";
    public final String kScoreCubeAndStation = "ScoreCubeAndStationAuto";
    public final String kScoreConeReloadScore = "ScoreConeReloadScoreAuto";
    public final String kScoreCubeReloadScore = "ScoreCubeReloadScoreAuto";
    public final String kStation = "StationAuto";
    public final String kScoreConeAndBackUp = "ScoreConeAndBackUpAuto";
    public final String kScoreCubeMidAndBackUp = "ScoreCubeMidAndBackUpAuto";
    public final String kScoreCubeHighAndBackUp = "ScoreCubeHighAndBackUpAuto";

    public String m_autoSelected;
    private final SendableChooser<String> m_chooser = new SendableChooser<>();

    // Angle settings
    double autoStartingAngle;
    double currentAngle;
    double goalAngle;
    float pitch;
    float previousPitch;
    float roll;
    public AHRS ahrs;

    // Position settings
    double goalPosition;
    double leftPosition;
    double rightPosition;

    //auto settings
    double autoStartTime;
    double autoWaitTime;
    int autoStep = 0;
    int autoTholdCount = 0;
    public long autoStepTime;
    public Timer m_timer = new Timer();
    int balanceCount = 0;

    //drivebase settings
    public PIDController turnPID = new PIDController(Setting.turnPID_p, Setting.turnPID_i, Setting.turnPID_d);
    public PIDController drivePID = new PIDController(Setting.drivePID_p, Setting.drivePID_i, Setting.drivePID_d);
    public boolean drivebaseAutomaticControl = false;
    double leftSpeed = 0;
    double rightSpeed = 0;
    double driveSpeed = 0;
    double turnSpeed = 0;

    // Arm settings
    public Arm arm;
    public double armPosition;
    public double prevArmPosition;
    public double armGoal;
    public PIDController armPID = new PIDController(Setting.armPID_p, Setting.armPID_i, Setting.armPID_d);
    public boolean armAutomaticControl = false;
    public boolean armInConeMode = false;
    double armSpeed;

    // Wrist settings
    public double wristPosition;
    public double wristGoal;
    public double calculatedWristGoal;
    public PIDController wristPID = new PIDController(Setting.wristPID_p, Setting.wristPID_i, Setting.wristPID_d);
    public boolean wristAutomaticControl = false;

    // Claw settings
    public double clawPosition;
    public double clawGoal;
    public PIDController clawPID = new PIDController(Setting.clawPID_p, Setting.clawPID_i, Setting.clawPID_d);
    public boolean clawAutomaticControl = true;
    public boolean isClawClosed = false;

    //Robot calsses
    public Auto auto;
    public Drivebase drivebase;

    // CAN Spark Max settings
    CANSparkMax armMotor = new CANSparkMax(Setting.armMotorCANID, Setting.armMotorType);
    CANSparkMax wristMotor = new CANSparkMax(Setting.wristMotorCANID, Setting.wristMotorType);
    CANSparkMax clawMotor = new CANSparkMax(Setting.clawMotorCANID, Setting.clawMotorType);

    CANSparkMax leftDriveMotor1 = new CANSparkMax(Setting.leftDriveMotor1CANID, Setting.drivebMotorType);
    CANSparkMax leftDriveMotor2 = new CANSparkMax(Setting.leftDriveMotor2CANID, Setting.drivebMotorType);
    CANSparkMax leftDriveMotor3 = new CANSparkMax(Setting.leftDriveMotor3CANID, Setting.drivebMotorType);

    CANSparkMax rightDriveMotor1 = new CANSparkMax(Setting.rightDriveMotor1CANID, Setting.drivebMotorType);
    CANSparkMax rightDriveMotor2 = new CANSparkMax(Setting.rightDriveMotor2CANID, Setting.drivebMotorType);
    CANSparkMax rightDriveMotor3 = new CANSparkMax(Setting.rightDriveMotor3CANID, Setting.drivebMotorType);

    PowerDistribution PDP = new PowerDistribution(Setting.PDPCANID, Setting.PDPType);
    DifferentialDrive differentialDrivebase = new DifferentialDrive(leftDriveMotor1, rightDriveMotor1);

    //joysticks
    Joystick driverJoystick = new Joystick(Setting.driverJoystickPort);
    Joystick opperatorJoystick = new Joystick(Setting.opperatorJotstickPort);
    Joystick manualJoystick = new Joystick(Setting.manualJoystickPort);

    // Limelight values
    double LL_X = 0;
    double LL_Y = 0;
    double LL_Area = 0;
    NetworkTable table;

    /**
     * This function is run when the robot is first started up and should be used
     * for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        CameraServer.startAutomaticCapture();

        m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
        m_chooser.addOption("Drive Straight", kDriveStraight);
        m_chooser.addOption("Drive Straight and move arm", kDriveStraightAndMoveArm);
        m_chooser.addOption("Score Cone and Station", kScoreConeAndStation);
        m_chooser.addOption("Score Cube and Station", kScoreCubeAndStation);
        m_chooser.addOption("Score Cone Reload Score", kScoreConeReloadScore);
        m_chooser.addOption("Score Cube Reload Score", kScoreCubeReloadScore);
        m_chooser.addOption("Station", kStation);
        m_chooser.addOption("Score Cone and BackUp", kScoreConeAndBackUp);
        m_chooser.addOption("Score Cube Mid and BackUp", kScoreCubeMidAndBackUp);
        m_chooser.addOption("Score Cube High and BackUp", kScoreCubeHighAndBackUp);
        SmartDashboard.putData("Auto choices", m_chooser);

        leftDriveMotor1.setIdleMode(Setting.drivebaseIdleMode);
        leftDriveMotor1.setInverted(Setting.leftSideInverted);
        leftDriveMotor1.setSmartCurrentLimit(Setting.drivebaseCurrentLimit);

        leftDriveMotor2.setIdleMode(Setting.drivebaseIdleMode);
        leftDriveMotor2.setInverted(Setting.leftSideInverted);
        leftDriveMotor2.setSmartCurrentLimit(Setting.drivebaseCurrentLimit);

        leftDriveMotor3.setIdleMode(Setting.drivebaseIdleMode);
        leftDriveMotor3.setInverted(Setting.leftSideInverted);
        leftDriveMotor3.setSmartCurrentLimit(Setting.drivebaseCurrentLimit);

        rightDriveMotor1.setIdleMode(Setting.drivebaseIdleMode);
        rightDriveMotor1.setInverted(Setting.rightSideInverted);
        rightDriveMotor1.setSmartCurrentLimit(Setting.drivebaseCurrentLimit);

        rightDriveMotor2.setIdleMode(Setting.drivebaseIdleMode);
        rightDriveMotor2.setInverted(Setting.rightSideInverted);
        rightDriveMotor2.setSmartCurrentLimit(Setting.drivebaseCurrentLimit);

        rightDriveMotor3.setIdleMode(Setting.drivebaseIdleMode);
        rightDriveMotor3.setInverted(Setting.rightSideInverted);
        rightDriveMotor3.setSmartCurrentLimit(Setting.drivebaseCurrentLimit);

        wristMotor.setIdleMode(Setting.wristMotorIdleMode);
        wristMotor.setInverted(Setting.wristMotorInverted);
        wristMotor.setSmartCurrentLimit(Setting.wristMotorCurrentLimit);

        armMotor.setIdleMode(Setting.armMotorIdleMode);
        armMotor.setInverted(Setting.armMotorInverted);
        armMotor.setSmartCurrentLimit(Setting.armMotorCurrentLimit);

        clawMotor.setIdleMode(Setting.clawMotorIdleMode);
        clawMotor.setInverted(Setting.clawMotorInverted);
        clawMotor.setSmartCurrentLimit(Setting.clawMotorCurrentLimit);

        rightDriveMotor2.follow(rightDriveMotor1);
        rightDriveMotor3.follow(rightDriveMotor1);

        leftDriveMotor2.follow(leftDriveMotor1);
        leftDriveMotor3.follow(leftDriveMotor1);

        ahrs = new AHRS(Port.kMXP);

        auto = new Auto(this);
        arm = new Arm(this);
        drivebase = new Drivebase(this);
        table = NetworkTableInstance.getDefault().getTable("limelight");

        resetSensors();

    }

    /**
     * This function is called every 20 ms, no matter the mode. Use this for items
     * like diagnostics
     * that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>
     * This runs after the mode specific periodic functions, but before LiveWindow
     * and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        // update vars
        leftPosition = leftDriveMotor1.getEncoder().getPosition();
        rightPosition = rightDriveMotor1.getEncoder().getPosition();
        prevArmPosition = armPosition;
        armPosition = armMotor.getEncoder().getPosition() * 18 / 22;
        clawPosition = clawMotor.getEncoder().getPosition();
        wristPosition = wristMotor.getEncoder().getPosition();
        currentAngle = ahrs.getAngle();
        previousPitch = pitch;
        pitch = ahrs.getPitch() - 2.5f;
        roll = ahrs.getRoll();

        //coast mode when user button pressed
        if (RobotController.getUserButton()) {
            armMotor.setIdleMode(IdleMode.kCoast);
            wristMotor.setIdleMode(IdleMode.kCoast);
            clawMotor.setIdleMode(IdleMode.kCoast);
            rightDriveMotor1.setIdleMode(IdleMode.kCoast);
            rightDriveMotor2.setIdleMode(IdleMode.kCoast);
            rightDriveMotor3.setIdleMode(IdleMode.kCoast);
            leftDriveMotor1.setIdleMode(IdleMode.kCoast);
            leftDriveMotor2.setIdleMode(IdleMode.kCoast);
            leftDriveMotor3.setIdleMode(IdleMode.kCoast);
        } else {
            armMotor.setIdleMode(Setting.armMotorIdleMode);
            wristMotor.setIdleMode(Setting.wristMotorIdleMode);
            clawMotor.setIdleMode(Setting.clawMotorIdleMode);
            rightDriveMotor1.setIdleMode(Setting.drivebaseIdleMode);
            rightDriveMotor2.setIdleMode(Setting.drivebaseIdleMode);
            rightDriveMotor3.setIdleMode(Setting.drivebaseIdleMode);
            leftDriveMotor1.setIdleMode(Setting.drivebaseIdleMode);
            leftDriveMotor2.setIdleMode(Setting.drivebaseIdleMode);
            leftDriveMotor3.setIdleMode(Setting.drivebaseIdleMode);
        }

        // LL Get from NT
        NetworkTableEntry tx = table.getEntry("tx");
        NetworkTableEntry ty = table.getEntry("ty");
        NetworkTableEntry ta = table.getEntry("ta");
        NetworkTableEntry ledMode = table.getEntry("ledMode");

        // read values periodically
        LL_X = tx.getDouble(0.0);
        LL_Y = ty.getDouble(0.0);
        ledMode.setDouble(3);
        LL_Area = ta.getDouble(0.0);

        putToSmartDashboard();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different
     * autonomous modes using the dashboard. The sendable chooser code works with
     * the Java
     * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the
     * chooser code and
     * uncomment the getString line to get the auto name from the text box below the
     * Gyro
     *
     * <p>
     * You can add additional auto modes by adding additional comparisons to the
     * switch structure
     * below with additional strings. If using the SendableChooser make sure to add
     * them to the
     * chooser code above as well.
     */
    @Override
    public void autonomousInit() {
        m_autoSelected = m_chooser.getSelected();
        auto.autoInit();
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        auto.autoPeriodic();
    }

    /**
     * This function is called once when teleop is enabled.
     */
    @Override
    public void teleopInit() {
        armAutomaticControl = true;
        clawAutomaticControl = true;
        wristAutomaticControl = true;
        arm.teleopInit();
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        arm.teleopPeriodic();
        drivebase.teleopPeriodic();
    }

    /**
     * This function is called once when the robot is disabled.
     */
    @Override
    public void disabledInit() {
    }

    /**
     * This function is called periodically when disabled.
     */
    @Override
    public void disabledPeriodic() {
    }

    /**
     * This function is called once when test mode is enabled.
     */
    @Override
    public void testInit() {
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }

    /**
     * This function is called once when the robot is first started up.
     */
    @Override
    public void simulationInit() {
    }

    /**
     * This function is called periodically whilst in simulation.
     */
    @Override
    public void simulationPeriodic() {
    }

    public void resetSensors() {
        leftDriveMotor1.getEncoder().setPosition(0);
        rightDriveMotor1.getEncoder().setPosition(0);
        ahrs.reset();
        goalAngle = 0;
        goalPosition = 0;

    }

    public void putToSmartDashboard() {

        SmartDashboard.putNumber("LeftDriveMotor1Ouput", leftDriveMotor1.getAppliedOutput());
        SmartDashboard.putNumber("LeftDriveMotor1Current", PDP.getCurrent(Setting.leftDriveMotor1PDPPort));

        SmartDashboard.putNumber("LeftDriveMotor1Ouput", leftDriveMotor1.getAppliedOutput());
        SmartDashboard.putNumber("LeftDriveMotor1Current", PDP.getCurrent(Setting.leftDriveMotor1PDPPort));

        SmartDashboard.putNumber("LeftDriveMotor2Ouput", leftDriveMotor2.getAppliedOutput());
        SmartDashboard.putNumber("LeftDriveMotor2Current", PDP.getCurrent(Setting.leftDriveMotor2PDPPort));

        SmartDashboard.putNumber("LeftDriveMotor3Ouput", leftDriveMotor3.getAppliedOutput());
        SmartDashboard.putNumber("LeftDriveMotor3Current", PDP.getCurrent(Setting.leftDriveMotor3PDPPort));

        SmartDashboard.putNumber("RightDriveMotor1Ouput", rightDriveMotor1.getAppliedOutput());
        SmartDashboard.putNumber("RightDriveMotor1Current", PDP.getCurrent(Setting.rightDriveMotor1PDPPort));

        SmartDashboard.putNumber("RightDriveMotor2Ouput", rightDriveMotor2.getAppliedOutput());
        SmartDashboard.putNumber("RightDriveMotor2Current", PDP.getCurrent(Setting.rightDriveMotor2PDPPort));

        SmartDashboard.putNumber("RightDriveMotor3Ouput", rightDriveMotor3.getAppliedOutput());
        SmartDashboard.putNumber("RightDriveMotor3Current", PDP.getCurrent(Setting.rightDriveMotor3PDPPort));

        SmartDashboard.putNumber("leftDriveSpeed", leftSpeed);
        SmartDashboard.putNumber("rightDriveSpeed", rightSpeed);

        SmartDashboard.putNumber("leftPosition", leftPosition);
        SmartDashboard.putNumber("rightPosition", rightPosition);

        SmartDashboard.putNumber("armOutput", armMotor.getAppliedOutput());
        SmartDashboard.putNumber("clawOutput", clawMotor.getAppliedOutput());
        SmartDashboard.putNumber("wristOutput", wristMotor.getAppliedOutput());

        SmartDashboard.putNumber("armCurrent", PDP.getCurrent(Setting.armMotorPDPPort));
        SmartDashboard.putNumber("clawCurrent", PDP.getCurrent(Setting.clawMotorPDPPort));
        SmartDashboard.putNumber("wristCurrent", PDP.getCurrent(Setting.wristMotorPDPPort));

        SmartDashboard.putNumber("armPosition", armPosition);
        SmartDashboard.putNumber("arm speed", armSpeed);
        SmartDashboard.putNumber("clawPosition", clawPosition);
        SmartDashboard.putNumber("wristPosition", wristPosition);

        SmartDashboard.putNumber("armGoal", armGoal);
        SmartDashboard.putNumber("clawGoal", clawGoal);
        SmartDashboard.putNumber("wristGoal", wristGoal);
        SmartDashboard.putNumber("CalculatedwristGoal", calculatedWristGoal);

        SmartDashboard.putNumber("gyroscope angle", currentAngle);
        SmartDashboard.putNumber("goal angle", goalAngle);

        SmartDashboard.putBoolean("isClawClosed", isClawClosed);
        SmartDashboard.putBoolean("coneMode", armInConeMode);

        SmartDashboard.putBoolean("Drivebase Automatic", drivebaseAutomaticControl);
        SmartDashboard.putBoolean("ArmAutomatic", armAutomaticControl);
        SmartDashboard.putBoolean("Wrist automatic control", wristAutomaticControl);
        SmartDashboard.putBoolean("Claw automatic control", clawAutomaticControl);

        SmartDashboard.putNumber("LimelightX", LL_X);
        SmartDashboard.putNumber("LimelightY", LL_Y);
        SmartDashboard.putNumber("LimelightArea", LL_Area);
        SmartDashboard.putNumber("LL sum", drivebase.sum);

        SmartDashboard.putNumber("pitch", pitch);
        SmartDashboard.putNumber("pitch rate", drivebase.pitchRate);
        SmartDashboard.putNumber("balance count", balanceCount);
        SmartDashboard.putNumber("roll", roll);

        SmartDashboard.putNumber("left drivebase position", leftPosition);
        SmartDashboard.putNumber("right drivebase position", rightPosition);
        SmartDashboard.putNumber("goal position", goalPosition);

        SmartDashboard.putNumber("Auto THold Count", autoTholdCount);
        SmartDashboard.putNumber("Auto Step Timer", autoStepTime);
        SmartDashboard.putNumber("Auto Step", autoStep);

    }
}
