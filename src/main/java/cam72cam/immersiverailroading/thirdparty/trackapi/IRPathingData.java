package cam72cam.immersiverailroading.thirdparty.trackapi;

import cam72cam.mod.math.Vec3d;
import net.minecraft.world.phys.Vec3;

public class IRPathingData extends trackapi.lib.PathingData {
    private Vec3d posCache;
    private double deltaMovement;

    public IRPathingData(Vec3d position, double roll) {
        this(position, roll, 0);
    }

    protected IRPathingData(Vec3d position, double roll, double deltaMovement) {
        super(position.internal(), roll);
        this.posCache = position;
        this.deltaMovement = deltaMovement;
    }

    public Vec3d getUMCPos() {
        return posCache;
    }

    public IRPathingData setUMCPos(Vec3d pos) {
        return setUMCPos(pos, false);
    }

    public IRPathingData setUMCPos(Vec3d pos, boolean preserveDeltaMovement) {
        this.posCache = pos;
        super.setPos(pos.internal());
        if (!preserveDeltaMovement) {
            this.deltaMovement = 0;
        }
        return this;
    }

    @Override
    public IRPathingData setRoll(double roll) {
        return wrap(super.setRoll(roll));
    }

    public double getDeltaMovement() {
        return deltaMovement;
    }

    public void advanceTo(IRPathingData next) {
        advanceTo(next.getUMCPos(), next.getRoll());
    }

    public void advanceTo(Vec3d nextPos) {
        advanceTo(nextPos, 0);
    }

    public void advanceTo(Vec3d nextPos, double nextRoll) {
        this.deltaMovement += nextPos.distanceTo(this.posCache);
        this.setPos(nextPos.internal());
        this.setRoll(nextRoll);
    }

    public void copyFrom(IRPathingData other) {
        this.setPos(other.getPos()).setRoll(other.getRoll());
        this.deltaMovement = other.deltaMovement;
    }

    @Override
    public IRPathingData clone() {
        return new IRPathingData(this.posCache, this.getRoll(), this.deltaMovement);
    }

    public static IRPathingData wrap(trackapi.lib.PathingData inputData) {
        if (inputData instanceof IRPathingData) {
            return (IRPathingData) inputData;
        } else {
            return new IRPathingData(new Vec3d(inputData.getPos()), inputData.getRoll());
        }
    }

    /**
     * @deprecated Use the UMC variant
     */
    @Deprecated
    @Override
    public trackapi.lib.PathingData setPos(Vec3 pos) {
        super.setPos(pos);
        if (this.posCache == null || !this.posCache.internal().equals(pos)) {
            Vec3d newPos = new Vec3d(pos);
            this.deltaMovement += newPos.distanceTo(this.posCache);
            this.posCache = newPos;
        }
        return this;
    }
}