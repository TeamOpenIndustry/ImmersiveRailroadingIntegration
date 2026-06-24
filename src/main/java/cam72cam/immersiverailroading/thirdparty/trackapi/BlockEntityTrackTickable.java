package cam72cam.immersiverailroading.thirdparty.trackapi;

import cam72cam.umc.api.block.BlockEntityTickable;
import cam72cam.umc.api.block.tile.TileEntity;
import cam72cam.umc.api.block.tile.TileEntityTickable;
import cam72cam.umc.api.resource.Identifier;

public abstract class BlockEntityTrackTickable extends BlockEntityTickable implements ITrack {
    protected TileEntity supplier(Identifier id) {
        return new TileEntityTickableTrack(id);
    }
}
