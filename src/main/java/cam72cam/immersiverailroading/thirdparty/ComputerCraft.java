package cam72cam.immersiverailroading.thirdparty;

import cam72cam.immersiverailroading.ImmersiveRailroading;
import cam72cam.immersiverailroading.entity.EntityRollingStock;
import cam72cam.immersiverailroading.entity.Locomotive;
import cam72cam.immersiverailroading.library.Augment;
import cam72cam.immersiverailroading.tile.TileRailBase;
import cam72cam.mod.event.CommonEvents;
import cam72cam.mod.math.Vec3i;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ComputerCraft {
    public static void init() {
        ComputerCraftAPI.registerPeripheralProvider(new IPeripheralProvider() {
            @Nullable
            @Override
            public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull EnumFacing enumFacing) {
                TileRailBase rail = cam72cam.mod.world.World.get(world).getBlockEntity(new Vec3i(blockPos), TileRailBase.class);
                if (rail != null) {
                    if (rail.getAugment() == Augment.DETECTOR) {
                        return new DetectorPeripheral(world, blockPos);
                    }
                    if (rail.getAugment() == Augment.LOCO_CONTROL) {
                        return new LocoControlPeripheral(world, blockPos);
                    }
                }
                return null;
            }
        });

        CommonEvents.World.TICK.subscribe(TickHandler::onWorldTick);
    }

    @FunctionalInterface
    private interface APICall {
        Object[] apply(CommonAPI api, Object[] params) throws LuaException;
    }

    public static class TickHandler {
        private static final Map<BasePeripheral, Set<IComputerAccess>> tickable = new HashMap<>();

        public static void onWorldTick(World world) {
            tickable.forEach((peripheral, computers) -> {
                if (!world.isRemote && peripheral.world == world) {
                    peripheral.update(computers);
                }
            });
        }

        public static void attach(BasePeripheral p, IComputerAccess c) {
            if (!tickable.containsKey(p)) {
                tickable.put(p, new HashSet<>());
            }
            tickable.get(p).add(c);
        }

        public static void detach(BasePeripheral p, IComputerAccess c) {
            if (tickable.containsKey(p)) {
                tickable.get(p).remove(c);
                if (tickable.get(p).isEmpty()) {
                    tickable.remove(p);
                }
            }
        }
    }

    private static abstract class BasePeripheral implements IPeripheral {
        private final World world;
        private final BlockPos pos;
        private final String[] fnNames;
        private final APICall[] fnImpls;
        private UUID wasOverhead;
        protected Class<? extends EntityRollingStock> typeFilter = EntityRollingStock.class;

        public BasePeripheral(World world, BlockPos blockPos, LinkedHashMap<String, APICall> methods) {
            this.world = world;
            this.pos = blockPos;
            this.fnNames = methods.keySet().toArray(new String[0]);
            this.fnImpls = methods.values().toArray(new APICall[0]);
            this.wasOverhead = null;
        }

        public void update(Set<IComputerAccess> computers) {
            if (computers.size() > 0) {
                TileRailBase te = cam72cam.mod.world.World.get(world).getBlockEntity(new Vec3i(pos), TileRailBase.class);
                EntityRollingStock nearby = te.getStockNearBy(typeFilter);
                UUID isOverhead = nearby != null ? nearby.getUUID() : null;
                if (isOverhead != wasOverhead) {
                    for (IComputerAccess computer : computers) {
                        computer.queueEvent("ir_train_overhead", new String[]{te.getAugment().toString(), isOverhead == null ? null : isOverhead.toString()});
                    }
                }

                wasOverhead = isOverhead;
            }
        }

        @Override
        public void attach(@Nonnull IComputerAccess computer) {
            TickHandler.attach(this, computer);
        }

        @Override
        public void detach(@Nonnull IComputerAccess computer) {
            TickHandler.detach(this, computer);
        }

        @Nonnull
        @Override
        public String[] getMethodNames() {
            return fnNames;
        }

        @Nullable
        @Override
        public Object[] callMethod(@Nonnull IComputerAccess iComputerAccess, @Nonnull ILuaContext iLuaContext, int i, @Nonnull Object[] objects) throws LuaException, InterruptedException {
            try {
                CommonAPI api = CommonAPI.create(world, pos);
                if (api != null && i < fnImpls.length) {
                    return fnImpls[i].apply(api, objects);
                }
            } catch (Exception ex) {
                ImmersiveRailroading.catching(ex);
            }
            return null;
        }

        @Override
        public boolean equals(@Nullable IPeripheral iPeripheral) {
            return iPeripheral == this;
        }
    }

    private static Object getObjParam(Object[] params, int id, String name) throws LuaException {
        if (params.length > id) {
            return params[id];
        }
        throw new LuaException("Required parameter \"" + name +"\"");
    }

    private static double getDoubleParam(Object[] params, int id, String name) throws LuaException {
        Object obj = getObjParam(params, id, name);
        try {
            return Double.parseDouble(obj.toString());
        } catch (NumberFormatException ex) {
            throw new LuaException("Required parameter \"" + name +"\" is not a number");
        }
    }

    private static boolean getBooleanParam(Object[] params, int id, String name) throws LuaException {
        Object obj = getObjParam(params, id, name);
        try {
            return Boolean.parseBoolean(obj.toString());
        } catch (NumberFormatException ex) {
            throw new LuaException("Required parameter \"" + name +"\" is not a number");
        }
    }

    private static class DetectorPeripheral extends BasePeripheral {
        private static LinkedHashMap<String, APICall> methods = new LinkedHashMap<>();
        static {
            methods.put("info", (CommonAPI api, Object[] params) -> new Object[]{api.info()});
            methods.put("consist", (CommonAPI api, Object[] params) -> new Object[]{api.consist(false)});
            methods.put("getTag", (CommonAPI api, Object[] params) -> new Object[]{api.getTag()});
            methods.put("setTag", (CommonAPI api, Object[] params) -> {
                api.setTag(getObjParam(params, 0, "tag").toString());
                return null;
            });
        }

        public DetectorPeripheral(World world, BlockPos blockPos) {
            super(world, blockPos, methods);
        }

        @Nonnull
        @Override
        public String getType() {
            return "ir_augment_detector";
        }
    }

    private static class LocoControlPeripheral extends BasePeripheral {
        private static LinkedHashMap<String, APICall> methods = new LinkedHashMap<>();
        static {
            methods.putAll(DetectorPeripheral.methods);
            methods.put("setThrottle", (CommonAPI api, Object[] params) -> {
                api.setThrottle(getDoubleParam(params, 0, "throttle"));
                return null;
            });
            methods.put("setReverser", (CommonAPI api, Object[] params) -> {
                api.setReverser(getDoubleParam(params, 0, "reverser"));
                return null;
            });
            methods.put("setBrake", (CommonAPI api, Object[] params) -> {
                api.setTrainBrake(getDoubleParam(params, 0, "brake"));
                return null;
            });
            methods.put("setTrainBrake", (CommonAPI api, Object[] params) -> {
                api.setTrainBrake(getDoubleParam(params, 0, "brake"));
                return null;
            });
            methods.put("setIndependentBrake", (CommonAPI api, Object[] params) -> {
                api.setIndependentBrake(getDoubleParam(params, 0, "brake"));
                return null;
            });
            methods.put("setHorn", (CommonAPI api, Object[] params) -> {
                api.setHorn((int) getDoubleParam(params, 0, "horn"));
                return null;
            });
            methods.put("setBell", (CommonAPI api, Object[] params) -> {
                api.setBell((int) getDoubleParam(params, 0, "bell"));
                return null;
            });
            methods.put("getIgnition", (CommonAPI api, Object[] params) -> new Object[] { api.getIgnition() });
            methods.put("setIgnition", (CommonAPI api, Object[] params) -> {
                api.setIgnition(getBooleanParam(params, 0, "ignition"));
                return null;
            });
        }

        public LocoControlPeripheral(World world, BlockPos blockPos) {
            super(world, blockPos, methods);
            typeFilter = Locomotive.class;
        }

        @Nonnull
        @Override
        public String getType() {
            return "ir_augment_control";
        }
    }
}
