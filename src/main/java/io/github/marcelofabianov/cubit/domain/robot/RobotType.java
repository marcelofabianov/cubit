package io.github.marcelofabianov.cubit.domain.robot;

import java.util.regex.Pattern;

/** A kind of robot. It is data, so a new robot does not change the rules (AGENTS.md, rule 39). */
public record RobotType(String id) {
  private static final String VALID_ID_FORMAT = "[a-z][a-z0-9_]*";
  private static final Pattern VALID_ID = Pattern.compile(VALID_ID_FORMAT);
  private static final String INVALID_ID_MESSAGE = "Invalid robot type id: ";

  public RobotType {
    if (!VALID_ID.matcher(id).matches()) {
      throw new IllegalArgumentException(INVALID_ID_MESSAGE + id);
    }
  }
}
