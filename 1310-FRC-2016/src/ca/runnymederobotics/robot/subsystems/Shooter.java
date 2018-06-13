package ca.runnymederobotics.robot.subsystems;

import static ca.runnymederobotics.robot.Constants.HOOD_PIN;
import static ca.runnymederobotics.robot.Constants.SHOOTER_PIN;
import static ca.runnymederobotics.robot.Constants.SHOOTER_SPEED;
import static ca.runnymederobotics.robot.Constants.SPIKE_PIN;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.VictorSP;

public class Shooter {

	private VictorSP hood;
	private VictorSP flyWheel;
	private Relay flashLight;

	public Shooter() {
		flyWheel = new VictorSP(SHOOTER_PIN);
		hood = new VictorSP(HOOD_PIN);
		flashLight = new Relay(SPIKE_PIN);

		// hood.changeControlMode(TalonControlMode.Position);
		// hood.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		// hood.setPosition(0);
	}


	public void setLight(boolean b){
		if(b)
			if(!flashLight.get().equals(Relay.Value.kForward))
				flashLight.set(Relay.Value.kForward);
		else
			if(!flashLight.get().equals(Relay.Value.kOff))
				flashLight.set(Relay.Value.kOff);
	}
	
	public double getSpeed() {
		return flyWheel.getSpeed();
	}

	public void periodic() {
		// wew
	}

	public void set(double speed) {
		flyWheel.set(0.9);
	}

	public void setHood(double power) {
		hood.set(power);
	}


	public void stop() {
		flyWheel.set(0);
	}


	public void spinUp(double shooterSpeed) {
		flyWheel.set(SHOOTER_SPEED);		
	}
}
