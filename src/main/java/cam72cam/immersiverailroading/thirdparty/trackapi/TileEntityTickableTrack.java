package cam72cam.immersiverailroading.thirdparty.trackapi;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.tile.TileEntityTickable;
import cam72cam.mod.resource.Identifier;

import net.minecraft.world.phys.Vec3;

public class TileEntityTickableTrack extends TileEntityTickable implements trackapi.lib.ITrackV2 {
    static {
        registerLegacyTE(new Identifier(ModCore.MODID, "tile_track"));
    }

    public TileEntityTickableTrack(Identifier id) {
        super(id);
    }

    private trackapi.lib.ITrackV2 track() {
        return instance() instanceof ITrack ? ((ITrack) instance()).to() : null;
    }

    @Override
    public double[] getTrackGauges() {
        if(track() != null) {
            return track().getTrackGauges();
        } else {
            double[] fallback = new double[1];
            fallback[0] = 0;
            return fallback;
        }
    }

    @Override
    public <D extends trackapi.lib.PathingData> void getNextPosition(D pos, Vec3 mot, double gauge) {
        track().getNextPosition(pos, mot, gauge);
    }
}
