package org.dynamis.core.entity;

/**
 * Stable identity for a component type.
 *
 * @param id component identifier
 */
public record ComponentId(long id) implements Comparable<ComponentId> {
  /**
   * Creates a {@link ComponentId} from a numeric identifier.
   *
   * @param id numeric identifier
   * @return component id instance
   */
  public static ComponentId of(long id) {
    if (id <= 0L) {
      throw new IllegalArgumentException("ComponentId must be positive, got: " + id);
    }
    return new ComponentId(id);
  }

  @Override
  public int compareTo(ComponentId other) {
    return Long.compare(this.id, other.id);
  }
}
