package cam72cam.immersiverailroading.thirdparty.trackapi;

import cam72cam.mod.math.Vec3d;

public class PathingData extends trackapi.lib.PathingData{
    private final Vec3d posCache;//is this safe enough?

    public PathingData(Vec3d position, double roll) {
        super(position.internal(), roll);
        posCache = position;
    }

    public Vec3d getPos() {
        return posCache;
    }

    public PathingData fromPrev(PathingData inputData) {
        return (PathingData) super.fromPrev(inputData);
    }

    public static PathingData safeCast(trackapi.lib.PathingData inputData) {
        if (inputData instanceof PathingData) return fastCast(inputData);

        PathingData result = new PathingData(new Vec3d(inputData.position.toVanilla()), inputData.roll);
        if (inputData.containsKey(DELTA_MOVEMENT)) {
            result.with(DELTA_MOVEMENT, inputData.get(DELTA_MOVEMENT));
        }
        if (inputData.containsKey(DELTA_ROLL)) {
            result.with(DELTA_ROLL, inputData.get(DELTA_ROLL));
        }

        return result;
    }

    public static PathingData fastCast(trackapi.lib.PathingData inputData) {
        return (PathingData) inputData;
    }
}
