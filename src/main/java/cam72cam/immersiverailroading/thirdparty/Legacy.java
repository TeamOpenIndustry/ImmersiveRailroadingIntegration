package cam72cam.immersiverailroading.thirdparty;

import cam72cam.immersiverailroading.IRBlocks;
import cam72cam.immersiverailroading.ImmersiveRailroading;
import cam72cam.immersiverailroading.thirdparty.trackapi.TileEntityTickableTrack;
import cam72cam.mod.ModCore;
import cam72cam.mod.block.tile.TileEntity;
import cam72cam.mod.block.tile.TileEntityTickable;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagCompound;
import net.minecraft.block.Block;

import net.minecraft.nbt.NBTTagCompound;

public class Legacy {
    public static class LegacyRailGagTile extends TileEntityTickableTrack {
        @Override
        public void readFromNBT(NBTTagCompound data) {
            data.setString("instanceId", new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_RAIL_GAG.id.getPath()).toString());
            super.readFromNBT(data);
        }

        @Override
        public void writeToNBT(NBTTagCompound data) {
            super.writeToNBT(data);
            data.setString("instanceId", new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_RAIL_GAG.id.getPath()).toString());
            data.setString("id", new Identifier(ModCore.MODID, "tile_track").toString());
        }
    }

    public static class LegacyRailTile extends TileEntityTickableTrack {
        @Override
        public void readFromNBT(NBTTagCompound data) {
            data.setString("instanceId", new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_RAIL.id.getPath()).toString());
            super.readFromNBT(data);
        }

        @Override
        public void writeToNBT(NBTTagCompound data) {
            super.writeToNBT(data);
            data.setString("instanceId", new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_RAIL.id.getPath()).toString());
            data.setString("id", new Identifier(ModCore.MODID, "tile_track").toString());
        }
    }

    public static class LegacyRailPreview extends TileEntityTickable {
        @Override
        public void readFromNBT(NBTTagCompound data) {
            data.setString("instanceId", new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_RAIL_PREVIEW.id.getPath()).toString());
            super.readFromNBT(data);
        }

        @Override
        public void writeToNBT(NBTTagCompound data) {
            super.writeToNBT(data);
            data.setString("instanceId", new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_RAIL_PREVIEW.id.getPath()).toString());
            data.setString("id", new Identifier(ModCore.MODID, "tile_track").toString());
        }
    }

    public static class LegacyMultiblockTile extends TileEntityTickable {
        @Override
        public void readFromNBT(NBTTagCompound data) {
            data.setString("instanceId", new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_MULTIBLOCK.id.getPath()).toString());
            super.readFromNBT(data);
        }

        @Override
        public void writeToNBT(NBTTagCompound data) {
            super.writeToNBT(data);
            data.setString("instanceId", new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_MULTIBLOCK.id.getPath()).toString());
            data.setString("id", new Identifier(ModCore.MODID, "tile_track").toString());
        }
    }

    public static void registerBlocks() {
        // register legacy TE's
        // Forge can go suck a NPE

        TileEntity.registerTileEntity(LegacyRailGagTile.class, new Identifier("minecraft", IRBlocks.BLOCK_RAIL_GAG.id.getPath()));
        TileEntity.registerTileEntity(LegacyRailTile.class, new Identifier("minecraft", IRBlocks.BLOCK_RAIL.id.getPath()));
        TileEntity.registerTileEntity(LegacyRailPreview.class, new Identifier("minecraft", IRBlocks.BLOCK_RAIL_PREVIEW.id.getPath()));
        TileEntity.registerTileEntity(LegacyMultiblockTile.class, new Identifier("minecraft", IRBlocks.BLOCK_MULTIBLOCK.id.getPath()));
    }
}
