package cs4303.p2.game.entity.ai;

/**
 * Base interface for goals
 */
public sealed interface Goal permits Flee, Patrol, TargetSight, TargetXRay {
}
