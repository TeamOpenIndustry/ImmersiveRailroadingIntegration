//package cam72cam.immersiverailroading.thirdparty;
//
//import cam72cam.immersiverailroading.IRBlocks;
//import cam72cam.immersiverailroading.ImmersiveRailroading;
//import dan200.computercraft.api.peripheral.PeripheralCapability;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
//import net.neoforged.fml.ModList;
//
//@EventBusSubscriber(modid = ImmersiveRailroading.MODID)
//public class RegistrySubscriber {
//    @SubscribeEvent
//    public static void onCapabilityRegister(RegisterCapabilitiesEvent event) {
//        if (ModList.get().isLoaded("computercraft")) {
//            event.registerBlock(PeripheralCapability.get(), ComputerCraft.run.get(),
//                                IRBlocks.BLOCK_RAIL.internal, IRBlocks.BLOCK_RAIL_GAG.internal);
//        }
//    }
//}
