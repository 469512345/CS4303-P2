package cs4303.p2.game.entity.ai;

import cs4303.p2.game.entity.Entity;

/**
 * Target another entity, with XRay vision, allowing to track even when outside of line of sight
 *
 * @param entity entity to track
 */
public record TargetXRay(Entity entity) implements Goal {
}
