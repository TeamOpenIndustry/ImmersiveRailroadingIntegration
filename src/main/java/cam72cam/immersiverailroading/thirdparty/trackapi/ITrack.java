package cam72cam.immersiverailroading.thirdparty.trackapi;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;
import net.minecraft.world.phys.Vec3;
import trackapi.lib.Util;

public interface ITrack {
    static boolean isRail(World world, Vec3i pos) {
        return get(world, new Vec3d(pos), true) != null;
    }

    static ITrack from(trackapi.lib.ITrack track) {
        if (track == null) {
            return null;
        }
        return new ITrack() {
            @Override
            public double getTrackGauge() {
                return track.getTrackGauge();
            }

            @Override
            public Vec3d getNextPosition(Vec3d pos, Vec3d vel) {
                Vec3 next = track.getNextPosition(pos.internal(), vel.internal());
                return next != null ? new Vec3d(next) : null;
            }
        };
    }

    static ITrack get(World world, Vec3d pos, boolean allowMCRail) {
        trackapi.lib.ITrack track = Util.getTileEntity(world.internal, pos.internal(), allowMCRail);
        if (track instanceof TileEntityTickableTrack) {
            // shortcut Vec3d wrapping
            return ((ITrack)((TileEntityTickableTrack) track).instance());
        }
        return from(track);
    }

    double getTrackGauge();

    Vec3d getNextPosition(Vec3d vec3d, Vec3d vec3d1);

    default trackapi.lib.ITrack to() {
        return new trackapi.lib.ITrack() {
            @Override
            public double getTrackGauge() {
                return ITrack.this.getTrackGauge();
            }

            @Override
            public Vec3 getNextPosition(Vec3 pos, Vec3 vel) {
                Vec3d next = ITrack.this.getNextPosition(new Vec3d(pos), new Vec3d(vel));
                return next != null ? next.internal() : null;
            }
        };
    }
}
