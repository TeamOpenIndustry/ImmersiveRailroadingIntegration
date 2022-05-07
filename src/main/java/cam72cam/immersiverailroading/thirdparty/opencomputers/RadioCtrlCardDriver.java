package cam72cam.immersiverailroading.thirdparty.opencomputers;

import cam72cam.immersiverailroading.Config;
import cam72cam.immersiverailroading.IRItems;
import cam72cam.immersiverailroading.entity.Locomotive;
import cam72cam.immersiverailroading.items.ItemRadioCtrlCard;
import cam72cam.immersiverailroading.library.Augment;
import cam72cam.immersiverailroading.thirdparty.CommonAPI;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import li.cil.oc.api.Network;
import li.cil.oc.api.prefab.DriverItem;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ComponentConnector;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.network.Visibility;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RadioCtrlCardDriver extends DriverItem {

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

	public class RadioCtrlCardManager extends li.cil.oc.api.prefab.ManagedEnvironment {
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

		@Callback(doc = "function():table -- returns an info dump about the current car")
		public Object[] info(Context context, Arguments arguments) {
			if (radioDrain()) {
				return new Object[] {
						api.info()
				};
			}
			return null;
		}

		@Callback(doc = "function():table -- returns an info dump about the current consist")
		public Object[] consist(Context context, Arguments arguments) {
			if (radioDrain()) {
				return new Object[] {
						api.consist(true)
				};
			}
			return null;
		}

		@Callback(doc = "function():table -- gets the stock's tag")
		public Object[] getTag(Context context, Arguments arguments) {
			if (radioDrain()) {
				return new Object[] { api.getTag() };
			}
			return null;
		}

		@Callback(doc = "function():table -- sets the stock's tag")
		public Object[] setTag(Context context, Arguments arguments) {
			if (radioDrain()) {
				api.setTag(arguments.checkString(0));
			}
			return null;
		}

		@Callback(doc = "function(double) -- sets the locomotive throttle")
		public Object[] setThrottle(Context context, Arguments arguments) {
			if (radioDrain()) {
				api.setThrottle(arguments.checkDouble(0));
			}
			return null;
		}
		@Callback(doc = "function(double) -- sets the locomotive reverser")
		public Object[] setReverser(Context context, Arguments arguments) {
			if (radioDrain()) {
				api.setReverser(arguments.checkDouble(0));
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

		@Callback(doc = "function(double) -- sets the train brake")
		public Object[] setBrake(Context context, Arguments arguments) {
			return setTrainBrake(context, arguments);
		}
		@Callback(doc = "function(double) -- sets the train brake")
		public Object[] setTrainBrake(Context context, Arguments arguments) {
			if (radioDrain()) {
				api.setTrainBrake(arguments.checkDouble(0));
			}
			return null;
		}
		@Callback(doc = "function(double) -- sets the independent brake")
		public Object[] setIndependentBrake(Context context, Arguments arguments) {
			if (radioDrain()) {
				api.setIndependentBrake(arguments.checkDouble(0));
			}
			return null;
		}

		@Callback(doc = "function() -- fires the locomotive horn")
		public Object[] horn(Context context, Arguments arguments) {
			if (radioDrain()) {
				api.setHorn(arguments.optInteger(0, 40));
			}
			return null;
		}
		@Callback(doc = "function() -- sets the locomotive bell")
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

		@Callback(doc = "function():araray -- returns the UUID of the bound loco")
		public Object[] getLinkUUID(Context context, Arguments args) {
			if (radioDrain()) {
				return new Object[] { api.getUniqueID() };
			}
			return new Object[] { null };
		}

        @Callback(doc = "function():boolean or nil -- gets the ignition state of bound diesel locomotive")
        public Object[] getIgnition(Context context, Arguments arguments) {
            if (radioDrain()) {
                return new Object[]{api.getIgnition()};
            }
            return null;
        }

		@Callback(doc = "function(boolean: on) -- sets the ignition of the bound diesel locomotive")
		public Object[] setIgnition(Context context, Arguments arguments) {
			if (radioDrain()) {
				api.setIgnition(arguments.checkBoolean(0));
			}
			return null;
		}

	}
}
