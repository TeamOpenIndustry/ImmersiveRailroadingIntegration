package cam72cam.immersiverailroading.thirdparty;

import blusunrize.immersiveengineering.common.blocks.metal.MetalScaffoldingType;
import blusunrize.immersiveengineering.common.blocks.wooden.TreatedWoodStyles;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import cam72cam.immersiverailroading.util.IRFuzzy;
import cam72cam.mod.item.ItemStack;

public class ImmersiveEngineering {

	public static void init() {
		ItemStack casing = new ItemStack(new net.minecraft.world.item.ItemStack(IEBlocks.StoneDecoration.BLASTBRICK));
		ItemStack light_eng = new ItemStack(new net.minecraft.world.item.ItemStack(IEBlocks.MetalDecoration.ENGINEERING_LIGHT));
		ItemStack heavy_eng = new ItemStack(new net.minecraft.world.item.ItemStack(IEBlocks.MetalDecoration.ENGINEERING_HEAVY));
		ItemStack scaffold = new ItemStack(new net.minecraft.world.item.ItemStack(IEBlocks.MetalDecoration.STEEL_SCAFFOLDING.get(MetalScaffoldingType.STANDARD)));
		IRFuzzy.IR_CASTING_CASING.add(casing);
		IRFuzzy.IR_LIGHT_ENG.add(light_eng);
		IRFuzzy.IR_HEAVY_ENG.add(heavy_eng);
        IRFuzzy.IR_SCAFFOLDING.add(scaffold);
		IRFuzzy.IR_TIE.add(new ItemStack(new net.minecraft.world.item.ItemStack(IEBlocks.WoodenDecoration.TREATED_WOOD.get(TreatedWoodStyles.HORIZONTAL))));
	}
}
