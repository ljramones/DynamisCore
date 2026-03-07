package org.dynamisengine.core.entity;

/**
 * Stable identity for a subsystem.
 *
 * @param id system identifier
 */
public record SystemId(long id) implements Comparable<SystemId> {
  /**
   * Creates a {@link SystemId} from a numeric identifier.
   *
   * @param id numeric identifier
   * @return system id instance
   */
  public static SystemId of(long id) {
    if (id <= 0L) {
      throw new IllegalArgumentException("SystemId must be positive, got: " + id);
    }
    return new SystemId(id);
  }

  @Override
  public int compareTo(SystemId other) {
    return Long.compare(this.id, other.id);
  }
}
