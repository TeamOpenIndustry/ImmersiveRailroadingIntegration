package cam72cam.immersiverailroading.thirdparty.trackapi;

import cam72cam.mod.math.Vec3d;

/**
 * Packaged variables for pathing rail.
 * Notice that this uses UMC Vec3d.
 * @see trackapi.lib.PathingContext
 */
public class PathingData {
    /**
     * nextPosition or currentPosition.
     */
    public final Vec3d pos;

    /**
     * distance between nextPosition and currentPosition.
     */
    public final double deltaMovement;

    /**
     * nextRoll or currentRoll.
     */
    public final double roll;

    public PathingData(Vec3d pos, double deltaMovement, double roll) {
        this.pos = pos;
        this.roll = roll;
        this.deltaMovement = deltaMovement;
    }

    public PathingData(trackapi.lib.PathingContext pathingContext) {
        this (
                new Vec3d(pathingContext.pos),
                pathingContext.deltaMovement,
                pathingContext.roll
        );
    }

    public trackapi.lib.PathingContext convert() {
        return new trackapi.lib.PathingContext(
                pos.internal(),
                deltaMovement,
                roll
        );
    }

    /**
     * This return a PathingContext with newPos and moved distance
     */
    public PathingData toPosAndRoll(Vec3d newPos, double newRoll) {
        return new PathingData(newPos, newPos.distanceTo(pos), newRoll);
    }
}
