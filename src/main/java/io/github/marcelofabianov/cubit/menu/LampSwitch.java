package io.github.marcelofabianov.cubit.menu;

/** The lamp switch as the inventory screen sees it. */
public interface LampSwitch {
  /** On the client, where the real values come from the server. */
  LampSwitch NONE =
      new LampSwitch() {
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
