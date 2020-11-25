package cam72cam.immersiverailroading.thirdparty;

import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.common.blocks.IEBlocks;
import blusunrize.immersiveengineering.common.blocks.metal.MetalScaffoldingType;
import blusunrize.immersiveengineering.common.blocks.wooden.TreatedWoodStyles;
import cam72cam.immersiverailroading.util.IRFuzzy;
import cam72cam.mod.item.ItemStack;
import net.minecraft.block.Block;

public class ImmersiveEngineering {

	public static void init() {
		ItemStack casing = new ItemStack(new net.minecraft.item.ItemStack(IEBlocks.StoneDecoration.blastbrick));
		ItemStack light_eng = new ItemStack(new net.minecraft.item.ItemStack(IEBlocks.MetalDecoration.engineeringLight));
		ItemStack heavy_eng = new ItemStack(new net.minecraft.item.ItemStack(IEBlocks.MetalDecoration.engineeringHeavy));
		ItemStack scaffold = new ItemStack(new net.minecraft.item.ItemStack(IEBlocks.MetalDecoration.steelScaffolding.get(MetalScaffoldingType.STANDARD)));
		IRFuzzy.IR_CASTING_CASING.add(casing);
		IRFuzzy.IR_LIGHT_ENG.add(light_eng);
		IRFuzzy.IR_HEAVY_ENG.add(heavy_eng);
        IRFuzzy.IR_SCAFFOLDING.add(scaffold);
		IRFuzzy.IR_TIE.add(new ItemStack(new net.minecraft.item.ItemStack(IEBlocks.WoodenDecoration.treatedWood.get(TreatedWoodStyles.HORIZONTAL))));
	}
}
