package cam72cam.immersiverailroading.thirdparty.trackapi;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;
import trackapi.lib.Util;

public interface ITrack {
    static boolean isRail(World world, Vec3i pos) {
        return get(world, new Vec3d(pos), true) != null;
    }

    static ITrack from(trackapi.lib.ITrackV2 track) {
        if (track == null) {
            return null;
        }
        return new ITrack() {
            @Deprecated
            @Override
            public double getTrackGauge() {
                return track.getTrackGauge();
            }

            @Override
            public double[] getTrackGauges() {
                return track.getTrackGauges();
            }

            @Override
            public PathingData getNextPosition(PathingData pos, Vec3d vel, double gauge) {
                trackapi.lib.PathingData next = track.getNextPosition(pos, new trackapi.lib.Vec3(vel.internal()), gauge);
                return next != null ? PathingData.safeCast(next): null;
            }
        };
    }

    static ITrack get(World world, Vec3d pos, boolean allowMCRail) {//TODO:这里强转了，等待trackApi动作
        trackapi.lib.ITrackV2 track = (trackapi.lib.ITrackV2) Util.getTileEntity(world.internal, pos.internal(), allowMCRail);
        if (track instanceof TileEntityTickableTrack) {
            // shortcut Vec3d wrapping
            return ((ITrack)((TileEntityTickableTrack) track).instance());
        }
        return from(track);
    }

    double getTrackGauge();
    double[] getTrackGauges();

    PathingData getNextPosition(PathingData vec3d, Vec3d vec3d1, double gauge);

    default trackapi.lib.ITrackV2 to() {
        return new trackapi.lib.ITrackV2() {
            @Deprecated
            @Override
            public double getTrackGauge() {
                return ITrack.this.getTrackGauge();
            }

            @Override
            public double[] getTrackGauges() {
                return ITrack.this.getTrackGauges();
            }

            @Override
            public trackapi.lib.PathingData getNextPosition(trackapi.lib.PathingData pos, trackapi.lib.Vec3 vel, double gauge) {
                PathingData next = ITrack.this.getNextPosition(PathingData.fastCast(pos), new Vec3d(vel.toVanilla()), gauge);
                return next != null ? next : null;
            }
        };
    }
}
