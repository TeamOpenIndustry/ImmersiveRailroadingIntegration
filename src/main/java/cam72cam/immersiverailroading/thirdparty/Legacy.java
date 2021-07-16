package cam72cam.immersiverailroading.thirdparty;

import cam72cam.immersiverailroading.IRBlocks;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.block.tile.TileEntity;
import cam72cam.mod.resource.Identifier;

public class Legacy {
    private static void legacy(BlockTypeEntity type) {
        TileEntity.registerLegacyTE(new Identifier("minecraft", type.id.getPath()), type.internal.defaultBlockState());
    }

    public static void registerBlocks() {
        // register legacy TE's
        // Forge can go suck a NPE

        legacy(IRBlocks.BLOCK_RAIL_GAG);
        legacy(IRBlocks.BLOCK_RAIL);
        legacy(IRBlocks.BLOCK_RAIL_PREVIEW);
        legacy(IRBlocks.BLOCK_MULTIBLOCK);
    }

}
