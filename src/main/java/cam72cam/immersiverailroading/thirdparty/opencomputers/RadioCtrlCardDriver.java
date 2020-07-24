package cam72cam.immersiverailroading.thirdparty.opencomputers;

import cam72cam.immersiverailroading.Config;
import cam72cam.immersiverailroading.IRItems;
import cam72cam.immersiverailroading.entity.Locomotive;
import cam72cam.immersiverailroading.items.ItemRadioCtrlCard;
import cam72cam.immersiverailroading.thirdparty.CommonAPI;
import li.cil.oc.api.Network;
import li.cil.oc.api.driver.DriverItem;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ComponentConnector;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RadioCtrlCardDriver implements DriverItem {

	@Override
	public boolean worksWith(ItemStack stack) {
		if (stack != null && stack.getItem() == IRItems.ITEM_RADIO_CONTROL_CARD.internal) {
			return true;
		}
		return false;
	}

	@Override
	public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
		World hostWorld = host.world();
		if (stack == null || stack.getItem() != IRItems.ITEM_RADIO_CONTROL_CARD.internal) {
			return null;
		}

		ItemRadioCtrlCard.Data data = new ItemRadioCtrlCard.Data(new cam72cam.mod.item.ItemStack(stack));
		if (data.linked == null) {
			return null;
		}

		cam72cam.mod.world.World world = cam72cam.mod.world.World.get(hostWorld);
		Locomotive found = world.getEntity(data.linked, Locomotive.class);
		if (found == null) {
			return null;
		}

		return new RadioCtrlCardManager(found, host.xPosition(), host.yPosition(), host.zPosition());
	}

	@Override
	public String slot(ItemStack stack) {
		return Slot.Card;
	}

	@Override
	public int tier(ItemStack stack) {
		// Should we bump the tier up? This component is quite powerful
		return 0;
	}

	@Override
	public NBTTagCompound dataTag(ItemStack stack) {
		return null;
	}

	public class RadioCtrlCardManager extends AbstractManagedEnvironment {
		protected Vec3d cardPosition;
		protected CommonAPI api;
		protected ComponentConnector node;

		public RadioCtrlCardManager(Locomotive loco, double x, double y, double z) {
			cardPosition = new Vec3d(x, y, z);
			api = new CommonAPI(loco);
			node = Network.newNode(this, Visibility.Network).withComponent("ir_remote_control", Visibility.Network).withConnector().create();
			setNode(node);
		}

		@Override
		public boolean canUpdate() {
			return false;
		}

		@Override
		public void update() {
			// Node node = this.node();
		}

        @Callback(doc = "function():number -- gets the locomotive throttle")
        public Object[] getThrottle(Context context, Arguments arguments) {
            if (radioDrain()) {
                return new Object[]{api.getThrottle()};
            }
            return new Object[]{null};
        }

		@Callback(doc = "function(level: number) -- sets the locomotive throttle")
		public Object[] setThrottle(Context context, Arguments arguments) {
			if (radioDrain()) {
				api.setThrottle(arguments.checkDouble(0));
			}
			return null;
		}

		private boolean radioDrain()
		{
			if(api == null) {
				return false;
			}
			double distance = api.getPosition().distanceTo(cardPosition);
			if( distance > Config.ConfigBalance.RadioRange) {
				return false;
			}
			if(node.tryChangeBuffer(-Config.ConfigBalance.RadioCostPerMetre * distance)) {
				return true;
			}
			return false;
		}

        @Callback(doc = "function():number -- gets the locomotive brake")
        public Object[] getBrake(Context context, Arguments arguments) {
            if (radioDrain()) {
                return new Object[]{api.getAirBrake()};
            }
            return new Object[]{null};
        }

		@Callback(doc = "function(level: number) -- sets the locomotive brake")
		public Object[] setBrake(Context context, Arguments arguments) {
			if (radioDrain()) {
				api.setAirBrake(arguments.checkDouble(0));
			}
			return null;
		}

		@Callback(doc = "function([time: number]) -- fires the locomotive horn")
		public Object[] horn(Context context, Arguments arguments) {
			if (radioDrain()) {
				api.setHorn(arguments.optInteger(0, 40));
			}
			return null;
		}
		@Callback(doc = "function([time: number]) -- sets the locomotive bell")
		public Object[] bell(Context context, Arguments arguments) {
			if (radioDrain()) {
				api.setBell(arguments.optInteger(0, 40));
			}
			return null;
		}

		@Callback(doc = "function():array -- returns the XYZ position of the locomotive")
		public Object[] getPos(Context context, Arguments args) {
			if (radioDrain()) {
				Vec3d pos = api.getPosition();
				return new Object[] { pos.x, pos.y, pos.z };
			}
			return null;
		}

		@Callback(doc = "function():string -- returns the UUID of the bound locomotive")
		public Object[] getLinkUUID(Context context, Arguments args) {
			if (radioDrain()) {
				return new Object[] { api.getUniqueID() };
			}
			return new Object[] { null };
		}

        @Callback(doc = "function():table -- returns an info dump about bound locomotive")
        public Object[] info(Context context, Arguments arguments) {
            if (radioDrain()) {
                return new Object[]{
                        api.info()
                };
            }
            return null;
        }

        @Callback(doc = "function():table -- returns an info dump about the current bound consist")
        public Object[] consist(Context context, Arguments arguments) {
            if (radioDrain()) {
                return new Object[]{
                        api.consist(true)
                };
            }
            return null;
        }

        @Callback(doc = "function():table -- gets the stock's tag")
        public Object[] getTag(Context context, Arguments arguments) {
            if (radioDrain()) {
                return new Object[]{api.getTag()};
            }
            return null;
        }

	}
}
