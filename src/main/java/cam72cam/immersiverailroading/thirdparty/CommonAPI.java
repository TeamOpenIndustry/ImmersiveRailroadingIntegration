package cam72cam.immersiverailroading.thirdparty;

import cam72cam.immersiverailroading.entity.*;
import cam72cam.immersiverailroading.physics.PhysicsAccummulator;
import cam72cam.immersiverailroading.registry.EntityRollingStockDefinition;
import cam72cam.immersiverailroading.registry.LocomotiveDefinition;
import cam72cam.immersiverailroading.tile.TileRailBase;
import cam72cam.mod.math.Vec3i;
import cam72cam.immersiverailroading.thirdparty.event.TagEvent;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;

public class CommonAPI {
    private final EntityRollingStock stock;

    public static CommonAPI create(World world, BlockPos pos) {
        return create(world, pos, EntityRollingStock.class);
    }

    public static CommonAPI create(World world, BlockPos pos, Class<? extends EntityRollingStock> stockClass) {
        TileRailBase te = cam72cam.mod.world.World.get(world).getBlockEntity(new Vec3i(pos), TileRailBase.class);
        if (te != null) {
            EntityRollingStock stock = te.getStockNearBy(stockClass);
            if (stock != null) {
                return new CommonAPI(stock);
            }
        }
        return null;
    }

    public CommonAPI(EntityRollingStock stock) {
        this.stock = stock;
    }
    
    public enum StockProperty {
        ID("id"),
        NAME("name"),
        TAG("tag"),
        WEIGHT("weight"),
        SPEED("speed"),
        DIRECTION("direction"),
        SPEED_DIRECTION("speed_direction"),
        SPEED3D("speed3d"),
        PASSENGERS("passengers"),
        POSITION("position"),
        UUID("uuid"),
        HORSEPOWER("horsepower"),
        TRACTION("traction"),
        MAX_SPEED("max_speed"),
        BRAKE("brake"),
        THROTTLE("throttle"),
        PRESSURE("pressure"),
        TEMPERATURE("temperature"),
        PRESSURE_TEMPERATURE("pressure_temperature"),
        FLUID("fluid"),
        FLUID_MAX("fluid_max"),
        CARGO_PERCENT("cargo_percent"),
        CARGO_SIZE("cargo_size"),
        ;
        
        private static Map<String, StockProperty> mMap = initializeMapping();
        
        private final String name;
        
        StockProperty(String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
        private static Map<String, StockProperty> initializeMapping() {
            mMap = new HashMap<String, StockProperty>();
            for (StockProperty s : StockProperty.values()) {
                mMap.put(s.name, s);
            }
            return mMap;
        }
        
        public static StockProperty fromName(String name) {
            try {
                return mMap.get(name);
            } catch(Exception e) {
                return null;
            }
        }
    }
    
    public Object[] getProperty(String PropertyName) {
        try {
            return getProperty(StockProperty.fromName(PropertyName));
        } catch(Exception e) {
            return null;
        }
    }

    public Object[] getProperty(StockProperty PropertyName) {
        if (stock != null) {
            switch(PropertyName) {
                case ID:
                    return new Object[] { stock.getDefinitionID() };
                case NAME:
                    return new Object[] { stock.getDefinition().name() };
                case TAG:
                    return new Object[] { stock.tag };
                case WEIGHT:
                    return new Object[] { stock.getWeight() };
                case DIRECTION:
                    EnumFacing dir = EnumFacing.fromAngle(stock.getRotationYaw());
                    return new Object[] { dir.toString() };
                case PASSENGERS:
                    if (stock instanceof EntityRidableRollingStock) {
                        return new Object[] { stock.getPassengerCount() };
                    }
                    return null;
                case POSITION:
                    return getPositionArray();
                case UUID:
                    return new Object[] { getUniqueID() };   
                case FLUID:
                    FluidStack fluid = getFluid();
                    if (fluid != null) {
                        return new Object[] { fluid.getFluid().getName(), fluid.amount };
                    } else {
                        return null;
                    }
                case FLUID_MAX:
                    if (stock instanceof FreightTank) {
                        return new Object[] { ((FreightTank) stock).getTankCapacity().MilliBuckets() };
                    }
                    return null;
            default:
                break;
            }
            if (stock instanceof EntityMoveableRollingStock) {
                switch(PropertyName) {
                    case SPEED:
                        return new Object[] { ((EntityMoveableRollingStock)stock).getCurrentSpeed().metric() };
                    case SPEED_DIRECTION:
                        return getSpeedPolar2D();
                    case SPEED3D:
                        cam72cam.mod.math.Vec3d current3DVelocity = ((EntityMoveableRollingStock)stock).getVelocity(); 
                        //TODO: Waiting for Entity.java change to forge version Vec3d
                        return new Object[] { current3DVelocity.x, current3DVelocity.y, current3DVelocity.z };
                default:
                    break;
                }
            }
            if (stock instanceof Locomotive) {
                Locomotive loco = (Locomotive) stock;
                LocomotiveDefinition locoDef = loco.getDefinition();
                switch(PropertyName) {
                    case HORSEPOWER:
                        return new Object[] { locoDef.getHorsePower(loco.gauge) };
                    case TRACTION:
                        return new Object[] { locoDef.getStartingTractionNewtons(loco.gauge) };
                    case MAX_SPEED:
                        return new Object[] { locoDef.getMaxSpeed(loco.gauge).metric() };
                    case BRAKE:
                        return new Object[] { loco.getAirBrake() };
                    case THROTTLE:
                        return new Object[] { loco.getThrottle() };
                default:
                    break;
                }
                if (loco instanceof LocomotiveSteam) {
                    LocomotiveSteam steam = (LocomotiveSteam) loco;
                    switch(PropertyName) {
                        case PRESSURE:
                            return new Object[] { steam.getBoilerPressure() };
                        case TEMPERATURE:
                            return new Object[] { steam.getBoilerTemperature() };
                        case PRESSURE_TEMPERATURE:
                            return new Object[] { steam.getBoilerPressure(),steam.getBoilerTemperature() };
                    default:
                        break;
                    }
                }
                if (loco instanceof LocomotiveDiesel) {
                    switch(PropertyName) {
                        case TEMPERATURE:
                            return new Object[] { ((LocomotiveDiesel) loco).getEngineTemperature() };
                    default:
                        break;
                    }
                }
                if (stock instanceof Freight) {
                    Freight freight = ((Freight) stock);
                    switch(PropertyName) { 
                        case CARGO_PERCENT:
                            return new Object[] { freight.getPercentCargoFull() };
                        case CARGO_SIZE:
                            return new Object[] { freight.getInventorySize() };
                    default:
                        break;
                    }
                }                
            }
        }
        return null;
    }
    
    public FluidStack getFluid() {
        /*
        Capability<ITank> energyCapability = CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
        ITank fh = stock.getCapability(energyCapability, null);
        if (fh != null) {
            return fh.drain(Integer.MAX_VALUE, false);
        }
        */
        return null;
    }

    public Map<String, Object> info() {
        if (stock != null) {
            Map<String, Object> info = new HashMap<>();
            EntityRollingStockDefinition def = stock.getDefinition();

            info.put("id", def.defID);
            info.put("name", def.name());
            info.put("tag", stock.tag);
            info.put("weight", stock.getWeight());

            EnumFacing dir = EnumFacing.fromAngle(stock.getRotationYaw());
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

            if (stock instanceof Locomotive) {
                Locomotive loco = (Locomotive) stock;
                LocomotiveDefinition locoDef = loco.getDefinition();
                info.put("horsepower", locoDef.getHorsePower(loco.gauge));
                info.put("traction", locoDef.getStartingTractionNewtons(loco.gauge));
                info.put("max_speed", locoDef.getMaxSpeed(loco.gauge).metric());
                info.put("brake", loco.getAirBrake());
                info.put("throttle", loco.getThrottle());

                if (loco instanceof LocomotiveSteam) {
                    LocomotiveSteam steam = (LocomotiveSteam) loco;
                    info.put("pressure", steam.getBoilerPressure());
                    info.put("temperature", steam.getBoilerTemperature());
                }
                if (loco instanceof LocomotiveDiesel) {
                    info.put("temperature", ((LocomotiveDiesel) loco).getEngineTemperature());
                }
            }

            FluidStack fluid = getFluid();
            if (fluid != null) {
                info.put("fluid_type", fluid.getFluid().getName());
                info.put("fluid_amount", fluid.amount);
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
        if (!(stock instanceof EntityCoupleableRollingStock)) {
            return null;
        }
        EntityCoupleableRollingStock stock = (EntityCoupleableRollingStock) this.stock;

        int traction = 0;
        PhysicsAccummulator acc = new PhysicsAccummulator(stock.getCurrentTickPosAndPrune());
        stock.mapTrain(stock, true, true, acc::accumulate);
        Map<String, Object> info = new HashMap<>();
        List<Object> locos = new ArrayList<>();

        info.put("cars", acc.count);
        info.put("tractive_effort_N", acc.tractiveEffortNewtons);
        info.put("weight_kg", acc.massToMoveKg);
        info.put("speed_km", stock.getCurrentSpeed().metric());
        EnumFacing dir = EnumFacing.fromAngle(stock.getRotationYaw());
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
        TagEvent.GetTagEvent tagEvent = new TagEvent.GetTagEvent(stock.getUUID());
        MinecraftForge.EVENT_BUS.post(tagEvent);
        
        if (tagEvent.tag != null)
        {
            return tagEvent.tag;
        }
        
        return stock.tag;
    }

    public void setTag(String tag) {
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
        if (stock instanceof Locomotive) {
            ((Locomotive)stock).setThrottle(normalize(throttle));
        }
    }
    public void setAirBrake(double brake) {
        if (stock instanceof Locomotive) {
            ((Locomotive)stock).setAirBrake(normalize(brake));
        }
    }

    public void setHorn(int horn) {
        if (stock instanceof Locomotive) {
            ((Locomotive)stock).setHorn(horn, null);
        }
    }

    public void setBell(int bell) {
        if (stock instanceof Locomotive) {
            ((Locomotive)stock).setBell(bell);
        }
    }

    public Vec3d getPosition() {
        return stock.getPosition().internal;
    }

    public Object[] getPositionArray() {
        Vec3d pos = getPosition();
        return new Object[] { pos.x, pos.y, pos.z };
    }
    
    public Object[] getSpeedPolar2D() {
        if (stock instanceof EntityMoveableRollingStock) {
            double CurrentSpeed = ((EntityMoveableRollingStock)stock).getCurrentSpeed().metric();
            double CurrentAngle = stock.getRotationYaw();
            return new Object[] {CurrentSpeed,CurrentAngle};
        }
        return null;
    }   
    
    public UUID getUniqueID() {
        return stock.getUUID();
    }
}
