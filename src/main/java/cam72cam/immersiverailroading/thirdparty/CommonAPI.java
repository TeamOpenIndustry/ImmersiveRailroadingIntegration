package cam72cam.immersiverailroading.thirdparty;

import cam72cam.immersiverailroading.entity.*;
import cam72cam.immersiverailroading.physics.PhysicsAccummulator;
import cam72cam.immersiverailroading.registry.EntityRollingStockDefinition;
import cam72cam.immersiverailroading.registry.LocomotiveDefinition;
import cam72cam.immersiverailroading.tile.TileRailBase;
import cam72cam.mod.math.Vec3i;
import cam72cam.immersiverailroading.thirdparty.event.TagEvent;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;
import java.util.function.Supplier;

public class CommonAPI {
    private final Supplier<EntityRollingStock> stockSupplier;

    public static CommonAPI create(World world, BlockPos pos) {
        return create(world, pos, EntityRollingStock.class);
    }

    public static CommonAPI create(World world, BlockPos pos, Class<? extends EntityRollingStock> stockClass) {
        TileRailBase te = cam72cam.mod.world.World.get(world).getBlockEntity(new Vec3i(pos), TileRailBase.class);
        if (te != null) {
            return new CommonAPI(te, stockClass);
        }
        return null;
    }

    public CommonAPI(TileRailBase te, Class<? extends EntityRollingStock> stockClass) {
        stockSupplier = () -> te.getStockNearBy(stockClass);
    }

    public CommonAPI(EntityRollingStock stock) {
        stockSupplier = () -> stock;
    }

    public EntityRollingStock stock() {
        return stockSupplier.get();
    }

    public FluidStack getFluid() {
        EntityRollingStock stock = this.stock();
        if (stock instanceof FreightTank) {
            FreightTank tank = (FreightTank) stock;
            return tank.getLiquid() != null ? new FluidStack(tank.getLiquid().internal.get(0), tank.getLiquidAmount()) : null;
        }
        return null;
    }

    public Map<String, Object> info() {
        EntityRollingStock stock = this.stock();
        if (stock != null) {
            Map<String, Object> info = new HashMap<>();
            EntityRollingStockDefinition def = stock.getDefinition();

            info.put("id", def.defID);
            info.put("name", def.name());
            info.put("tag", stock.tag);
            info.put("weight", stock.getWeight());

            Direction dir = Direction.fromAngle(stock.getRotationYaw());
            if (stock instanceof EntityMoveableRollingStock) {
                EntityMoveableRollingStock movable = (EntityMoveableRollingStock) stock;
                info.put("speed", movable.getCurrentSpeed().metric());

                if (movable.getCurrentSpeed().metric() < 0) {
                    dir = dir.getOpposite();
                }
            }
            info.put("direction", dir.toString());

            if (stock instanceof EntityRidableRollingStock) {
                info.put("passengers", stock.getPassengerCount());
            }

            if (stock instanceof EntityMoveableRollingStock) {
                info.put("independent_brake", ((EntityMoveableRollingStock) stock).getIndependentBrake());
            }

            if (stock instanceof Locomotive) {
                Locomotive loco = (Locomotive) stock;
                LocomotiveDefinition locoDef = loco.getDefinition();
                info.put("horsepower", locoDef.getHorsePower(loco.gauge));
                info.put("traction", locoDef.getStartingTractionNewtons(loco.gauge));
                info.put("max_speed", locoDef.getMaxSpeed(loco.gauge).metric());
                info.put("brake", loco.getTrainBrake());
                info.put("train_brake", loco.getTrainBrake());
                info.put("throttle", loco.getThrottle());
                info.put("reverser", loco.getReverser());

                if (loco instanceof LocomotiveSteam) {
                    LocomotiveSteam steam = (LocomotiveSteam) loco;
                    info.put("pressure", steam.getBoilerPressure());
                    info.put("temperature", steam.getBoilerTemperature());
                }
                if (loco instanceof LocomotiveDiesel) {
                    info.put("ignition", ((LocomotiveDiesel) loco).isTurnedOn());
                    info.put("temperature", ((LocomotiveDiesel) loco).getEngineTemperature());
                }
            }

            FluidStack fluid = getFluid();
            if (fluid != null) {
                info.put("fluid_type", fluid.getFluid().getAttributes().getDisplayName(null));
                info.put("fluid_amount", fluid.getAmount());
            } else {
                info.put("fluid_type", null);
                info.put("fluid_amount", 0);
            }
            if (stock instanceof FreightTank) {
                info.put("fluid_max", ((FreightTank) stock).getTankCapacity().MilliBuckets());
            }

            if (stock instanceof Freight) {
                Freight freight = ((Freight) stock);
                info.put("cargo_percent", freight.getPercentCargoFull());
                info.put("cargo_size", freight.getInventorySize());
            }
            return info;
        }
        return null;
    }

    public Map<String, Object> consist(boolean supportsList) {
        if (!(stock() instanceof EntityCoupleableRollingStock)) {
            return null;
        }
        EntityCoupleableRollingStock stock = (EntityCoupleableRollingStock) stock();

        int traction = 0;
        PhysicsAccummulator acc = new PhysicsAccummulator(stock.getCurrentTickPosAndPrune());
        stock.mapTrain(stock, true, true, acc::accumulate);
        Map<String, Object> info = new HashMap<>();
        List<Object> locos = new ArrayList<>();

        info.put("cars", acc.count);
        info.put("tractive_effort_N", acc.tractiveEffortNewtons);
        info.put("weight_kg", acc.massToMoveKg);
        info.put("speed_km", stock.getCurrentSpeed().metric());
        Direction dir = Direction.fromAngle(stock.getRotationYaw());
        if (stock.getCurrentSpeed().metric() < 0) {
            dir = dir.getOpposite();
        }
        info.put("direction", dir.toString());

        for (EntityCoupleableRollingStock car : stock.getTrain()) {
            if (car instanceof Locomotive) {
                LocomotiveDefinition locoDef = ((Locomotive) car).getDefinition();
                traction += locoDef.getStartingTractionNewtons(car.gauge);
                locos.add(new CommonAPI(car).info());
            }
        }
        if (supportsList) {
            info.put("locomotives", locos);
        } else {
            Map<String, Object> locomotives = new HashMap<>();
            for (int i = 0; i < locos.size(); i++) {
                locomotives.put("" + i, locos.get(i));
            }
            info.put("locomotives", locomotives);
        }
        info.put("total_traction_N", traction);
        return info;
    }

    public String getTag() {
        EntityRollingStock stock = this.stock();
    	TagEvent.GetTagEvent tagEvent = new TagEvent.GetTagEvent(stock.getUUID());
    	MinecraftForge.EVENT_BUS.post(tagEvent);
    	
    	if (tagEvent.tag != null)
    	{
    		return tagEvent.tag;
    	}
    	
        return stock.tag;
    }

    public void setTag(String tag) {
        EntityRollingStock stock = this.stock();
    	TagEvent.SetTagEvent tagEvent = new TagEvent.SetTagEvent(stock.getUUID(), tag);
    	MinecraftForge.EVENT_BUS.post(tagEvent);
    	
        stock.tag = tag;
    }

    private float normalize(double val) {
        if (Double.isNaN(val)) {
            return 0;
        }
        if (val > 1) {
            return 1;
        }
        if (val < -1) {
            return -1;
        }
        return (float)val;
    }

    public void setThrottle(double throttle) {
        EntityRollingStock stock = this.stock();
        if (stock instanceof Locomotive) {
            ((Locomotive)stock).setThrottle(normalize(throttle));
        }
    }
    public void setReverser(double reverser) {
        EntityRollingStock stock = this.stock();
        if (stock instanceof Locomotive) {
            ((Locomotive)stock).setReverser(normalize(reverser));
        }
    }
    public void setTrainBrake(double brake) {
        EntityRollingStock stock = this.stock();
        if (stock instanceof Locomotive) {
            ((Locomotive)stock).setTrainBrake(normalize(brake));
        }
    }
    public void setIndependentBrake(double brake) {
        EntityRollingStock stock = this.stock();
        if (stock instanceof EntityMoveableRollingStock) {
            ((Locomotive)stock).setIndependentBrake(normalize(brake));
        }
    }

    public void setHorn(int horn) {
        EntityRollingStock stock = this.stock();
        if (stock instanceof Locomotive) {
            ((Locomotive)stock).setHorn(horn, null);
        }
    }

    public void setBell(int bell) {
        EntityRollingStock stock = this.stock();
        if (stock instanceof Locomotive) {
            ((Locomotive)stock).setBell(bell);
        }
    }

    public Vec3d getPosition() {
        EntityRollingStock stock = this.stock();
        return stock.getPosition().internal();
    }

    public UUID getUniqueID() {
        EntityRollingStock stock = this.stock();
        return stock.getUUID();
    }

    public Boolean getIgnition() {
        EntityRollingStock stock = this.stock();
        if (stock instanceof LocomotiveDiesel) {
            return ((LocomotiveDiesel)stock).isTurnedOn();
        }
        return null;
    }

    public void setIgnition(boolean on) {
        EntityRollingStock stock = this.stock();
        if (stock instanceof LocomotiveDiesel) {
            ((LocomotiveDiesel)stock).setTurnedOn(on);
        }
    }

}
