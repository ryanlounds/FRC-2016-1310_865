package ca.runnymederobotics.robot.hardware.controllerSettings;


import static ca.runnymederobotics.robot.RunnymedeRobot.drive;
import static ca.runnymederobotics.robot.RunnymedeRobot.intake;
import static ca.runnymederobotics.robot.RunnymedeRobot.shooter;

import ca.runnymederobotics.robot.Constants;
import ca.runnymederobotics.robot.hardware.XboxController;
import ca.runnymederobotics.robot.hardware.XboxController.RumbleType;
import edu.wpi.first.wpilibj.Compressor;

public class DefaultControls extends ControllerSettings {
	private static boolean changedA;
	private static boolean changedRS;
	private static boolean O_ChangedLB;
	private static boolean O_ChangedBack;
	private static boolean O_ChangedStart;
	private static boolean O_ChangedX;
	private static boolean O_ChangedB;

	private static boolean O_DPadLeft;
	private static boolean O_DPadRight;
	
	private static double hoodSpeed;
	private static boolean firing;
	private XboxController driver;
	private XboxController operator;
	private Compressor compressor;
	boolean disableHood;

	public DefaultControls(XboxController driver, XboxController operator, Compressor compressor) {
		this.driver = driver;
		this.operator = operator;
		this.compressor = compressor;
	}
	
	public void reset() {
		changedA = false;
		changedRS = false;
		O_ChangedLB = false;
		O_ChangedBack = false;
		O_ChangedStart = false;
		O_ChangedB = false;
		O_ChangedX = false;
		O_DPadLeft = false;
		O_DPadRight = false;
		
		hoodSpeed = 0.0;
		firing = false;
		disableHood = false;
		drive.setDrivetrainReversed(false);
	}

	@Override
	public void periodic() {

		// In take
        if (driver.getLeftTrigger() >= 0.5) {
            if (!firing) {
                intake.intake();
            } else {
                intake.fireBall();
            }
        // Out take
        } else if (driver.getRightTrigger() >= 0.5) {
            intake.outake();
        } else {
            intake.stopIntake();
        }

		// Toggles the hood
		if (driver.getAbutton()) {
			if (!changedA) {
				if(!operator.getBbutton()){
					intake.toggleAdjustingArm();
				}
				intake.initialBackAdjustingForward(false);
				changedA = true;
			}
		} else {
			if (changedA)
				changedA = false;
		}
		
		if(operator.getBbutton()){
			if(!O_ChangedB){
				intake.initialBackAdjustingForward(true);
				O_ChangedB = true;
			}
		}else {
            if (O_ChangedB){
                O_ChangedB = false;
            intake.initialBackAdjustingForward(false);
        }
		}

		// High Gear, on a hold basis, not toggle basis
		drive.setGear(driver.getRightBumperbutton());

		// Toggle Front (Default is intake forwards, press to make flat side forward)
		if (driver.getRightStickButton()) {
			if (!changedRS) {
				drive.setDrivetrainReversed(!drive.isDrivetrainReversed());
				changedRS = true;
			}
		} else {
			if (changedRS)
				changedRS = false;
		}
		hoodSpeed = 0.2; // drive up by default

		firing = false;
		// Shooting on a hold basis
		if (driver.getBbutton()) {
			shooter.spinUp(Constants.SHOOTER_SPEED);
			hoodSpeed = 0.3;
			if (driver.getBbutton()) {
				firing = true;
			} else {
				driver.setRumble(RumbleType.kRightRumble, 0.0f);
				driver.setRumble(RumbleType.kLeftRumble, 0.0f);
			}
		}else if(driver.getBbutton()){ 
			shooter.spinUp(Constants.SHOOTER_SPEED);
			hoodSpeed = 0.3;
			if (driver.getBbutton()) {
				firing = true;
			} else {
				driver.setRumble(RumbleType.kRightRumble, 0.0f);
				driver.setRumble(RumbleType.kLeftRumble, 0.0f);
			}
		}else {
		
			shooter.stop();
			driver.setRumble(RumbleType.kRightRumble, 0.0f);
			driver.setRumble(RumbleType.kLeftRumble, 0.0f);
		}
		if(disableHood) {
			hoodSpeed = 0;
		}
		 shooter.setHood(hoodSpeed);

		if (driver.getBackButton()) {
			if (!O_ChangedBack) {
				compressor.setClosedLoopControl(!compressor.getClosedLoopControl());
				O_ChangedBack = true;
			}
		} else {
			if (O_ChangedBack)
				O_ChangedBack = false;
		}

		// If ya feel like CheesyDrive :)
		drive.cheesyDrive(driver.getRightX(), driver.getLeftY(), driver.getLeftBumperbutton());
		// If Cheesy isn't your style and you're more into Tank Drive
		//drive.tankDrive(driver.getLeftY(), driver.getRightY(), driver.getLeftBumperbutton());
	}
    @Override
    public void disabled() {
        driver.setRumble(RumbleType.kLeftRumble, 0);
        driver.setRumble(RumbleType.kRightRumble, 0);
    }
}
