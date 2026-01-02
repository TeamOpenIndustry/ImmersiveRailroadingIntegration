package cam72cam.immersiverailroading.thirdparty;

import cam72cam.immersiverailroading.IRBlocks;
import cam72cam.immersiverailroading.ImmersiveRailroading;
import dan200.computercraft.api.peripheral.PeripheralCapability;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@Mod.EventBusSubscriber(modid = ImmersiveRailroading.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistrySubscriber {
    @SubscribeEvent
    public static void onCapabilityRegister(RegisterCapabilitiesEvent event) {
        event.registerBlock(PeripheralCapability.get(), ComputerCraft.run.get(),
                            IRBlocks.BLOCK_RAIL.internal, IRBlocks.BLOCK_RAIL_GAG.internal);
    }
}
