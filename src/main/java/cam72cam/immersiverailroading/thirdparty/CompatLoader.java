package cam72cam.immersiverailroading.thirdparty;

import cam72cam.immersiverailroading.ImmersiveRailroading;
import net.minecraftforge.fml.ModList;
import cam72cam.mod.ModEvent;

public class CompatLoader {
	public static Object invokeStatic(String modID, String cname, String method, Object ...objects) {
		if (ModList.get().isLoaded(modID)) {
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
				invokeStatic("computercraft", "cam72cam.immersiverailroading.thirdparty.ComputerCraft", "init");
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
