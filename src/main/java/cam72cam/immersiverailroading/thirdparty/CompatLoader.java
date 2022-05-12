package cam72cam.immersiverailroading.thirdparty;

import cam72cam.immersiverailroading.ImmersiveRailroading;
import cpw.mods.fml.common.Loader;
import cam72cam.mod.ModEvent;
import cam72cam.mod.event.CommonEvents;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CompatLoader {
	private static Object invokeStatic(String modID, String cname, String method, Object ...objects) {
		if (Loader.isModLoaded(modID)) {
			try {
				Class<?> cls = Class.forName(cname);
				return cls.getMethod(method).invoke(null, objects);
			} catch (Exception ex) {
				ImmersiveRailroading.catching(ex);
			}
		}
		return null;
	}

    public static boolean openWiki() {
        return false;
    }

	public static void common(ModEvent event) {
		switch (event) {
			case CONSTRUCT:
				CommonEvents.Block.REGISTER.subscribe(Legacy::registerBlocks);
				break;
			case INITIALIZE:
				invokeStatic("igwmod", "cam72cam.immersiverailroading.thirdparty.IGWMod", "init");
				break;
			case SETUP:
				invokeStatic("OpenComputers", "cam72cam.immersiverailroading.thirdparty.opencomputers.Compat", "init");
				invokeStatic("ComputerCraft", "cam72cam.immersiverailroading.thirdparty.ComputerCraft", "init");
				invokeStatic("ImmersiveEngineering", "cam72cam.immersiverailroading.thirdparty.ImmersiveEngineering", "init");
				invokeStatic("Railcraft", "cam72cam.immersiverailroading.thirdparty.Railcraft", "init");
				break;
			case FINALIZE:
				break;
			case START:
				break;
			case RELOAD:
				break;
		}
	}
}
