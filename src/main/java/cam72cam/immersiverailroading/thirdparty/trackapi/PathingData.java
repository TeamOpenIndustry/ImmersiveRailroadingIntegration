package cam72cam.immersiverailroading.thirdparty.trackapi;

import cam72cam.mod.math.Vec3d;

public class PathingData extends trackapi.lib.PathingData{
    public PathingData(Vec3d position, double roll) {
        super(position.internal(), roll);
    }

    public Vec3d getPos() {
        return new Vec3d(position.toVanilla());
    }

    public PathingData fromPrev(PathingData inputData) {
        return (PathingData) super.fromPrev(inputData);
    }
}
