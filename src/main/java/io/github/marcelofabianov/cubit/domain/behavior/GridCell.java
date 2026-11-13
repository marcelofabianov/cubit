package io.github.marcelofabianov.cubit.domain.behavior;

/** A block of a {@link TorchGrid}, counted from the corner of the grid. */
public record GridCell(int x, int y, int z) {}
