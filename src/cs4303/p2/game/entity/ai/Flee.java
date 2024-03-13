package cs4303.p2.game.entity.ai;

import cs4303.p2.game.entity.Entity;

/**
 * Flee from another entity
 * @param entity entity to flee from
 */
public record Flee(Entity entity) implements Goal {
}
