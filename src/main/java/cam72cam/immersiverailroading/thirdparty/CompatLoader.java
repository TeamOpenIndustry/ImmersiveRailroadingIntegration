package cam72cam.immersiverailroading.thirdparty;

import cam72cam.immersiverailroading.ImmersiveRailroading;
import cpw.mods.fml.common.Loader;

public class CompatLoader {
	public static Object invokeStatic(String modID, String cname, String method, Object ...objects) {
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
	
	public static void setup() {
		invokeStatic("OpenComputers", "cam72cam.immersiverailroading.thirdparty.opencomputers.Compat", "init");
		invokeStatic("ComputerCraft", "cam72cam.immersiverailroading.thirdparty.ComputerCraft", "init");
		invokeStatic("ImmersiveEngineering", "cam72cam.immersiverailroading.thirdparty.ImmersiveEngineering", "init");
	}

	public static void init() {
	}

    public static boolean openWiki() {
        return false;
    }
}
