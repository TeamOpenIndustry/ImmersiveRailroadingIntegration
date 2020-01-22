package cam72cam.immersiverailroading.thirdparty;

import cam72cam.immersiverailroading.ImmersiveRailroading;
import cam72cam.mod.ModEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;

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
				break;
			case INITIALIZE:
				break;
			case SETUP:
				invokeStatic("immersiveengineering", "cam72cam.immersiverailroading.thirdparty.ImmersiveEngineering", "init");
				invokeStatic("computercraft", "cam72cam.immersiverailroading.thirdparty.ComputerCraft", "init");
				invokeStatic("opencomputers", "cam72cam.immersiverailroading.thirdparty.opencomputers.Compat", "init");
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
