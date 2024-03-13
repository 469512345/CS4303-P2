package cs4303.p2.game.entity.ai;

import cs4303.p2.game.entity.Entity;

/**
 * Target an entity while it is in line of sight
 *
 * @param entity entity to target
 */
public record TargetSight(Entity entity) implements Goal {
}
