package ca.runnymederobotics.robot.subsystems;

import static ca.runnymederobotics.robot.Constants.*;

import ca.runnymederobotics.robot.networking.DataPool;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Intake {
	private Solenoid initialArm;
	private Solenoid adjustingArm;
	private Solenoid climberT1;
	private Solenoid climberT2;
	private VictorSP innerMotor;
	private VictorSP outerMotor;
	int goCounter = 0;
	private DigitalInput photosensorClose;
	private DigitalInput photosensorFar;
	private DataPool _pool;

	public Intake() {
		_pool = new DataPool("Intake");
		photosensorClose = new DigitalInput(INTAKE_PHOTOSENSOR_CLOSE);
		photosensorFar = new DigitalInput(INTAKE_PHOTOSENSOR_FAR);
		innerMotor = new VictorSP(INTAKE_MOTOR_PIN);
		outerMotor = new VictorSP(OUTER_INTAKE_PIN);
		initialArm = new Solenoid(INTAKE_PISTON_A);
		adjustingArm = new Solenoid(INTAKE_PISTON_B);
		initialArm.set(false);
		adjustingArm.set(false);
	}

	public void reset() {
		initialArm.set(true);
	}

	public void toggleInitialArm() {
		initialArm.set(!initialArm.get());
	}

	public void toggleAdjustingArm() {
		adjustingArm.set(!adjustingArm.get());
	}

	public void initialBackAdjustingForward(Boolean b){
		if(b){
		initialArm.set(false);
		adjustingArm.set(true);
		}else{
			initialArm.set(true);
		}
	}
	
	public void intake() {
		//               ||
		// Nathan wiring VV
		boolean close = !photosensorClose.get();
		boolean far = photosensorFar.get();
		
		if(close && far){
			outerMotor.set(SUPER_SLOW_INTAKE_SPEED);
			innerMotor.set(-SUPER_SLOW_INTAKE_SPEED);
		}else if(far){
			outerMotor.set(SLOW_INTAKE_SPEED);
			innerMotor.set(-SLOW_INTAKE_SPEED);
		}else if(!close && !far){
			outerMotor.set(INTAKE_SPEED);
			innerMotor.set(-INTAKE_SPEED);
		// close is true
		}else if (goCounter <= 0) {
			outerMotor.set(0);
			innerMotor.set(0);
		}
	}
	
	public void shoot() {
		innerMotor.set(-INTAKE_SPEED);
		outerMotor.set(INTAKE_SPEED);
	}

	public void outake() {
		innerMotor.set(INTAKE_SPEED);
		outerMotor.set(-INTAKE_SPEED);
	}

	public void stop() {
		innerMotor.set(0);
		outerMotor.set(0);
		initialArm.set(false);
		adjustingArm.set(false);
	}

	public void set(double speed) {
		innerMotor.set(speed);
		outerMotor.set(speed);
	}

	public boolean adjustedArmRetracted() {
		return adjustingArm.get() == false;
	}

	public void stopIntake() {
		innerMotor.set(0);
		outerMotor.set(0);
	}

	public void raisePortculus(boolean b) {
		adjustingArm.set(!b);
		initialArm.set(!b);
	}

	public void periodic() {
		if (goCounter > 0) {
			innerMotor.set(-INTAKE_SPEED);
			outerMotor.set(-INTAKE_SPEED);
			goCounter--;
		}
	}

	public void fireBall() {
		goCounter = 150;
	}

	public void slowPeriodic() {
		//				//					 ||
		//					Nathan wiring	 VV
		_pool.logBoolean("photosensorClose", !photosensorClose.get());
		_pool.logBoolean("photosensorFar", photosensorFar.get());
		_pool.logBoolean("photosensor", photosensorClose.get() || !photosensorFar.get());//for gui
	}
	
	public int getCounter(){
		return goCounter;
	}
	
	public boolean hasBall(){
		return !photosensorClose.get();
	}

	public void setAdjusting(boolean b) {
		if(adjustingArm.get() != b)
			adjustingArm.set(b);
	}
	
	public void toggleT1(){
		climberT1.set(!climberT1.get());
	}
	
	public void toggleT2(){
		climberT2.set(!climberT2.get());
	}
}
