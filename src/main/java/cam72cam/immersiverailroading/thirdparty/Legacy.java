package cam72cam.immersiverailroading.thirdparty;

import cam72cam.immersiverailroading.IRBlocks;
import cam72cam.immersiverailroading.ImmersiveRailroading;
import cam72cam.immersiverailroading.thirdparty.trackapi.TileEntityTickableTrack;
import cam72cam.mod.block.tile.TileEntity;
import cam72cam.mod.block.tile.TileEntityTickable;
import cam72cam.mod.event.CommonEvents;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.world.World;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public class Legacy {
    static List<Runnable> onNextTick = new ArrayList<>();

    static {
        World.onTick((world) -> {
            List<Runnable> copy = new ArrayList<>(onNextTick);
            onNextTick.clear();
            copy.forEach(Runnable::run);
        });
    }


    public static class LegacyRailGagTile extends TileEntityTickableTrack {
        private int setTimes = 1;

        public LegacyRailGagTile() {
            super(new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_RAIL_GAG.id.getPath()));
        }

        @Override
        public CompoundNBT write(CompoundNBT data) {
            data = super.write(data);
            data.putString("id", new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_RAIL_GAG.id.getPath()).toString());
            return data;
        }

        @Override
        public void tick() {
            if (setTimes > 0 && !world.isRemote && !isRemoved()) {
                if (world.getServer().getExecutionThread() != Thread.currentThread()) {
                    return;
                }
                this.world.setBlockState(pos, IRBlocks.BLOCK_RAIL_GAG.internal.getDefaultState(), 0);
                this.world.setTileEntity(pos, this);
                setTimes -= 1;
            }
        }

        @Override
        public void setWorld(net.minecraft.world.World worldIn) {
            super.setWorld(worldIn);
            onNextTick.add(this::tick);
        }
    }

    public static class LegacyRailTile extends TileEntityTickableTrack {
        private int setTimes = 1;
        public LegacyRailTile() {
            super(new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_RAIL.id.getPath()));
        }

        @Override
         public CompoundNBT write(CompoundNBT data) {
            data = super.write(data);
            data.putString("id", new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_RAIL.id.getPath()).toString());
            return data;
        }

        @Override
        public void tick() {
            if (setTimes > 0 && !world.isRemote && !isRemoved()) {
                if (world.getServer().getExecutionThread() != Thread.currentThread()) {
                    return;
                }
                this.world.setBlockState(pos, IRBlocks.BLOCK_RAIL.internal.getDefaultState(), 0);
                this.world.setTileEntity(pos, this);
                setTimes -= 1;
            }
        }

        @Override
        public void setWorld(net.minecraft.world.World worldIn) {
            super.setWorld(worldIn);
            onNextTick.add(this::tick);
        }
    }

    public static class LegacyRailPreview extends TileEntityTickable {
        private int setTimes = 1;
        public LegacyRailPreview() {
            super(new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_RAIL_PREVIEW.id.getPath()));
        }

        @Override
        public CompoundNBT write(CompoundNBT data) {
            data = super.write(data);
            data.putString("id", new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_RAIL_PREVIEW.id.getPath()).toString());
            return data;
        }

        @Override
        public void tick() {
            if (setTimes > 0 && !world.isRemote && !isRemoved()) {
                if (world.getServer().getExecutionThread() != Thread.currentThread()) {
                    return;
                }
                this.world.setBlockState(pos, IRBlocks.BLOCK_RAIL_PREVIEW.internal.getDefaultState(), 0);
                this.world.setTileEntity(pos, this);
                setTimes -= 1;
            }
        }

        @Override
        public void setWorld(net.minecraft.world.World worldIn) {
            super.setWorld(worldIn);
            onNextTick.add(this::tick);
        }
    }

    public static class LegacyMultiblockTile extends TileEntityTickable {
        private int setTimes = 1;
        public LegacyMultiblockTile() {
            super(new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_MULTIBLOCK.id.getPath()));
        }
        @Override
        public CompoundNBT write(CompoundNBT data) {
            data = super.write(data);
            data.putString("id", new Identifier(ImmersiveRailroading.MODID, IRBlocks.BLOCK_MULTIBLOCK.id.getPath()).toString());
            return data;
        }

        @Override
        public void tick() {
            if (setTimes > 0 && !world.isRemote && !isRemoved()) {
                if (world.getServer().getExecutionThread() != Thread.currentThread()) {
                    return;
                }
                this.world.setBlockState(pos, IRBlocks.BLOCK_MULTIBLOCK.internal.getDefaultState(), 0);
                this.world.setTileEntity(pos, this);
                setTimes -= 1;
            }
        }

        @Override
        public void setWorld(net.minecraft.world.World worldIn) {
            super.setWorld(worldIn);
            onNextTick.add(this::tick);
        }
    }

    private static void legacy(Supplier<? extends TileEntity> ctr, Identifier id) {
        TileEntityType<TileEntity> type = new TileEntityType<>(ctr, new HashSet<Block>() {
            public boolean contains(Object var1) {
                return true;
            }
        }, null);
        type.setRegistryName(id.internal);
        ForgeRegistries.TILE_ENTITIES.register(type);
    }

    public static void registerBlocks() {
        // register legacy TE's
        // Forge can go suck a NPE
        CommonEvents.Tile.REGISTER.subscribe(() -> {
            legacy(LegacyRailGagTile::new, new Identifier("minecraft", IRBlocks.BLOCK_RAIL_GAG.id.getPath()));
            legacy(LegacyRailTile::new, new Identifier("minecraft", IRBlocks.BLOCK_RAIL.id.getPath()));
            legacy(LegacyRailPreview::new, new Identifier("minecraft", IRBlocks.BLOCK_RAIL_PREVIEW.id.getPath()));
            legacy(LegacyMultiblockTile::new, new Identifier("minecraft", IRBlocks.BLOCK_MULTIBLOCK.id.getPath()));
        });
    }

}
