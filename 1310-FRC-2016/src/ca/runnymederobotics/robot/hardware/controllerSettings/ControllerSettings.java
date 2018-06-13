package ca.runnymederobotics.robot.hardware.controllerSettings;

public abstract class ControllerSettings {

	abstract public void reset();

	abstract public void periodic();

    public abstract void disabled();
}
