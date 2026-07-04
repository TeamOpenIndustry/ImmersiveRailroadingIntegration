package cam72cam.immersiverailroading.thirdparty.trackapi;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;
import trackapi.lib.ITrackV2;
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
            @Override
            public double[] getTrackGauges() {
                return track.getTrackGauges();
            }

            @Override
            public void getNextPosition(IRPathingData pos, Vec3d vel, double gauge) {
                track.getNextPosition(pos, vel.internal(), gauge);
            }
        };
    }

    static ITrack get(World world, Vec3d pos, boolean allowMCRail) {
        trackapi.lib.ITrackV2 track = Util.findTrackBlocks(world.internal, pos.internal(), allowMCRail, ITrackV2.class);
        if (track instanceof TileEntityTickableTrack) {
            // shortcut Vec3d wrapping
            return ((ITrack)((TileEntityTickableTrack) track).instance());
        }
        return from(track);
    }

    double[] getTrackGauges();

    void getNextPosition(IRPathingData data, Vec3d motion, double gauge);

    default trackapi.lib.ITrackV2 to() {
        return new trackapi.lib.ITrackV2() {
            @Override
            public double[] getTrackGauges() {
                return ITrack.this.getTrackGauges();
            }

            @Override
            public <D extends trackapi.lib.PathingData> void getNextPosition(D pos, net.minecraft.util.math.Vec3d vel, double gauge) {
                ITrack.this.getNextPosition(IRPathingData.wrap(pos), new Vec3d(vel), gauge);
            }
        };
    }
}
