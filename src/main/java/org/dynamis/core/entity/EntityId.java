package org.dynamis.core.entity;

/**
 * Stable identity for an entity.
 *
 * @param id entity identifier
 */
public record EntityId(long id) implements Comparable<EntityId> {
  /**
   * Creates an {@link EntityId} from a numeric identifier.
   *
   * @param id numeric identifier
   * @return entity id instance
   */
  public static EntityId of(long id) {
    if (id <= 0L) {
      throw new IllegalArgumentException("EntityId must be positive, got: " + id);
    }
    return new EntityId(id);
  }

  @Override
  public int compareTo(EntityId other) {
    return Long.compare(this.id, other.id);
  }
}
