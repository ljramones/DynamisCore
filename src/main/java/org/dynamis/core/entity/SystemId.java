package org.dynamis.core.entity;

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

  /**
   * Creates a deterministic {@link SystemId} from a system name.
   *
   * <p>Hash collisions are possible, so this is stable but not globally unique.
   *
   * @param name system name
   * @return system id instance
   */
  public static SystemId of(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("System name must be non-null and non-empty");
    }
    return of(Integer.toUnsignedLong(name.hashCode()));
  }

  @Override
  public int compareTo(SystemId other) {
    return Long.compare(this.id, other.id);
  }
}
