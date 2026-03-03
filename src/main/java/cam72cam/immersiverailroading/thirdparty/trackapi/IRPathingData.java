package cam72cam.immersiverailroading.thirdparty.trackapi;

import cam72cam.mod.math.Vec3d;

public class IRPathingData extends trackapi.lib.PathingData {
    private Vec3d posCache;
    private double deltaMovement;

    public IRPathingData(Vec3d position, double roll, double deltaMovement) {
        super(position.internal(), roll);
        this.deltaMovement = deltaMovement;
        this.posCache = position;
    }

    @Override
    public trackapi.lib.PathingData setPos(net.minecraft.util.math.Vec3d pos) {
        super.setPos(pos);
        this.deltaMovement += pos.distanceTo(this.getPos());
        this.posCache = new Vec3d(pos);
        return this;
    }

    @Override
    public trackapi.lib.PathingData setRoll(double roll) {
        super.setRoll(roll);
        return this;
    }

    public Vec3d getUMCPos() {
        return posCache;
    }

    public IRPathingData setState(Vec3d pos, double roll) {
        this.setPos(pos.internal()).setRoll(roll);
        return this;
    }

    public IRPathingData setState(IRPathingData prev) {
        this.setState(prev.getUMCPos(), prev.getRoll());
        return this;
    }

    public double getDeltaMovement() {
        return deltaMovement;
    }

    public static IRPathingData fastCast(trackapi.lib.PathingData inputData) {
        return (IRPathingData) inputData;
    }

    public IRPathingData copy() {
        return new IRPathingData(new Vec3d(this.posCache.internal()), this.getRoll(), this.getDeltaMovement());
    }
}
