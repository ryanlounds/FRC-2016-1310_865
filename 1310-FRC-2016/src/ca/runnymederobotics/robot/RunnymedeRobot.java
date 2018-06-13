package ca.runnymederobotics.robot;

import static ca.runnymederobotics.robot.Constants.COMPRESSOR_PIN;

import ca.runnymederobotics.robot.hardware.XboxController;
import ca.runnymederobotics.robot.hardware.controllerSettings.ControllerSettings;
import ca.runnymederobotics.robot.hardware.controllerSettings.DefaultControls;
import ca.runnymederobotics.robot.networking.DataPool;
import ca.runnymederobotics.robot.subsystems.Drive;
import ca.runnymederobotics.robot.subsystems.Intake;
import ca.runnymederobotics.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class RunnymedeRobot extends SampleRobot {

    private ControllerSettings controls;
    private Compressor compressor;
    private PowerDistributionPanel pdp;
    public static Shooter shooter;
    public static Intake intake;
    public static Drive drive;
    public static DriverStation driverStation;
    private int counter;
    private DataPool _pool;
    public DigitalInput switchA; // square
    public DigitalInput switchB; // round
 
    
    public void robotInit() {
        System.out.println("hello i am robit");
        _pool = new DataPool("Robot");
        NetworkTable.setUpdateRate(0.050);
        driverStation = DriverStation.getInstance();

        compressor = new Compressor(COMPRESSOR_PIN);
        
        shooter = new Shooter();
        drive = new Drive();
        intake = new Intake();

        pdp = new PowerDistributionPanel();

        XboxController driver = new XboxController(0);
        XboxController operator = new XboxController(1);
        controls = new DefaultControls(driver, operator, compressor);
        //auto = new SwagDrive();

    }

    public void operatorControl() {
        controls.reset();
        intake.reset();
        if(driverStation.isFMSAttached()) {
            compressor.setClosedLoopControl(false);
        } else {
            compressor.setClosedLoopControl(true);
        }
        while (isOperatorControl() && isEnabled()) {
            controls.periodic();
            allEnabledLoop();
            slowLoop();
            Timer.delay(0.005);
        }
    }

    public void autonomous() {
    	
    }

    public void disabled() {
        while (!isEnabled()) {
            shooter.stop();
            drive.stop();
            intake.stop(); // TODO Investigate these, seems kinda pointless
            slowLoop();
            controls.disabled();
            Timer.delay(0.005);
        }
    }

    private void allEnabledLoop() {
        shooter.periodic();
        intake.periodic();
    }

    private void slowLoop() {
        if (counter++ >= 10) {
            counter = 0;
            intake.slowPeriodic();
            drive.slowPeriodic();
            shooter.periodic();
        }
    }
}