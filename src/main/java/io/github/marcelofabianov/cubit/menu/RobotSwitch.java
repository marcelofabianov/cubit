package io.github.marcelofabianov.cubit.menu;

/** A switch of the robot as the inventory screen sees it, such as the lamp or the scanners. */
public interface RobotSwitch {
  /** On the client, where the real values come from the server. */
  RobotSwitch NONE =
      new RobotSwitch() {
        @Override
        public boolean isOn() {
          return false;
        }

        @Override
        public boolean isAvailable() {
          return false;
        }

        @Override
        public void toggle() {}
      };

  boolean isOn();

  boolean isAvailable();

  void toggle();
}
