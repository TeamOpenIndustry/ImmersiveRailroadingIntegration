package cam72cam.immersiverailroading.thirdparty.trackapi;

import cam72cam.mod.block.BlockEntityTickable;
import cam72cam.mod.block.tile.TileEntity;
import cam72cam.mod.block.tile.TileEntityTickable;
import cam72cam.mod.resource.Identifier;

public abstract class BlockEntityTrackTickable extends BlockEntityTickable implements ITrack {
    protected TileEntity supplier(Identifier id) {
        return new TileEntityTickableTrack(id);
    }
}
