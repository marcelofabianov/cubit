package io.github.zenndg.cubit.domain.desk;

/**
 * What the desk found on this tick, read by the caller: the order, the redstone signal, the
 * materials of the recipe, whether the recipe accepts them, the energy of one craft and the room in
 * the output.
 */
public record DeskCheck(
    boolean hasOrder,
    boolean redstone,
    boolean materials,
    boolean accepted,
    boolean energy,
    boolean outputFits) {}
