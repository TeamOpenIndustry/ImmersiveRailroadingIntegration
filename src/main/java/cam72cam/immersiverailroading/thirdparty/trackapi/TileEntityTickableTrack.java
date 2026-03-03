package cam72cam.immersiverailroading.thirdparty.trackapi;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.tile.TileEntityTickable;
import cam72cam.mod.resource.Identifier;

public class TileEntityTickableTrack extends TileEntityTickable implements trackapi.lib.ITrackV2 {
    static {
        registerTileEntity(TileEntityTickableTrack.class, new Identifier(ModCore.MODID, "tile_track"));
    }

    public TileEntityTickableTrack() {
        super();
    }

    public TileEntityTickableTrack(Identifier id) {
        super(id);
    }

    private trackapi.lib.ITrackV2 track() {
        return instance() instanceof ITrack ? ((ITrack) instance()).to() : null;
    }

    @Deprecated
    @Override
    public double getTrackGauge() {
        return track() != null ? track().getTrackGauge() : 0;
    }

    @Override
    public double[] getTrackGauges() {
        double[] fallback = new double[1];
        fallback[0] = 0;
        return track() != null ? track().getTrackGauges() : fallback;
    }

    @Override
    public <D extends trackapi.lib.PathingData> void getNextPosition(D pos, net.minecraft.util.math.Vec3d mot, double gauge) {
        track().getNextPosition(pos, mot, gauge);
    }
}
