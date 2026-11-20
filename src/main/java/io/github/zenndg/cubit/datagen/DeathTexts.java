package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.menu.DeathKeys;
import io.github.zenndg.cubit.menu.ScreenKeys;
import java.util.function.BiConsumer;

/** The death point, in English, the base language (answers 1111, 1118 and 1127). */
final class DeathTexts {
  private static final String SWITCH = "Death point";
  private static final String SWITCH_DETAIL = "Shows where you fell and guides you there";
  private static final String FELL = "You fell at %s, %s, %s · %s";
  private static final String ARRIVED = "Here is where you fell";
  private static final String STUCK = "I can't find a way there";
  private static final String TAB_TITLE = "Deaths";
  private static final String EMPTY = "No deaths yet";
  private static final String WHEN = "When";
  private static final String WHERE = "Where and cause";
  private static final String DAY_TIME = "Day %s, %s";
  private static final String COORDINATES = "%s, %s, %s";
  private static final String GUIDE = "Guide";
  private static final String CANCEL = "Cancel";
  private static final String DELETE = "Delete";
  private static final String GUIDE_DETAIL = "The Cubit walks ahead and takes you there";
  private static final String CANCEL_DETAIL = "The Cubit stops guiding and follows you";
  private static final String DELETE_DETAIL = "Removes this death from the list";
  private static final String SWITCHED_OFF = "The Death point switch is off";
  private static final String DEATH_ELSEWHERE = "The Cubit only guides in the Overworld";
  private static final String CUBIT_ELSEWHERE = "The Cubit must be in the Overworld";
  private static final String NOT_FOLLOWING = "Only a following Cubit guides · use Follow first";
  private static final String OVERWORLD = "Overworld";
  private static final String NETHER = "Nether";
  private static final String END = "The End";

  private DeathTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(ScreenKeys.DEATH_POINT, SWITCH);
    add.accept(ScreenKeys.DEATH_POINT_DETAIL, SWITCH_DETAIL);
    add.accept(DeathKeys.NOTICE_FELL, FELL);
    add.accept(DeathKeys.NOTICE_ARRIVED, ARRIVED);
    add.accept(DeathKeys.NOTICE_STUCK, STUCK);
    add.accept(DeathKeys.TAB_TITLE, TAB_TITLE);
    add.accept(DeathKeys.EMPTY, EMPTY);
    add.accept(DeathKeys.WHEN, WHEN);
    add.accept(DeathKeys.WHERE, WHERE);
    add.accept(DeathKeys.DAY_TIME, DAY_TIME);
    add.accept(DeathKeys.COORDINATES, COORDINATES);
    add.accept(DeathKeys.GUIDE, GUIDE);
    add.accept(DeathKeys.CANCEL, CANCEL);
    add.accept(DeathKeys.DELETE, DELETE);
    add.accept(DeathKeys.GUIDE_DETAIL, GUIDE_DETAIL);
    add.accept(DeathKeys.CANCEL_DETAIL, CANCEL_DETAIL);
    add.accept(DeathKeys.DELETE_DETAIL, DELETE_DETAIL);
    add.accept(DeathKeys.SWITCHED_OFF, SWITCHED_OFF);
    add.accept(DeathKeys.DEATH_ELSEWHERE, DEATH_ELSEWHERE);
    add.accept(DeathKeys.CUBIT_ELSEWHERE, CUBIT_ELSEWHERE);
    add.accept(DeathKeys.NOT_FOLLOWING, NOT_FOLLOWING);
    add.accept(DeathKeys.OVERWORLD, OVERWORLD);
    add.accept(DeathKeys.NETHER, NETHER);
    add.accept(DeathKeys.END, END);
  }
}
