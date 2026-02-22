package cam72cam.immersiverailroading.thirdparty.trackapi;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;
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
            public PathingData getNextPosition(PathingData pos, Vec3d vel) {
                trackapi.lib.PathingContext next = track.getNextPosition(pos.convert(), vel.internal());
                return next != null ? new PathingData(next) : null;
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

    PathingData getNextPosition(PathingData vec3d, Vec3d vec3d1);

    default trackapi.lib.ITrack to() {
        return new trackapi.lib.ITrack() {
            @Override
            public double getTrackGauge() {
                return ITrack.this.getTrackGauge();
            }

            @Override
            public trackapi.lib.PathingContext getNextPosition(trackapi.lib.PathingContext pos, net.minecraft.util.math.Vec3d vel) {
                PathingData next = ITrack.this.getNextPosition(new PathingData(pos), new Vec3d(vel));
                return next != null ? next.convert() : null;
            }
        };
    }
}
