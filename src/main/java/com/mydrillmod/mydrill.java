package com.example.mydrillmod;

import com.simibubi.create.content.contraptions.components.deployer.BlockBreakingKineticBehaviour;
import com.simibubi.create.api.event.BlockEntityBehaviourEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("mydrillmod")
public class MyDrillMod {
    public MyDrillMod() {}

    @SubscribeEvent
    public void onBehaviourAdd(BlockEntityBehaviourEvent.Add event) {
        BlockEntity be = event.getBlockEntity();

        // 检查是不是钻头的 TileEntity
        if (be.getType().toString().contains("drill")) {
            // 移除原始破坏行为
            event.remove(BlockBreakingKineticBehaviour.TYPE);

            // 添加自定义破坏行为
            event.add(new CustomDrillBehaviour(be));
        }
    }

    static class CustomDrillBehaviour extends BlockBreakingKineticBehaviour {
        public CustomDrillBehaviour(BlockEntity be) {
            super(be);
        }

        @Override
        protected void tickBreaker(BlockPos pos) {
            // 检查目标方块是不是圆石
            if (level.getBlockState(pos).is(Blocks.COBBLESTONE)) {
                BlockPos below = pos.below();
                BlockEntity container = level.getBlockEntity(below);

                if (container != null) {
                    container.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(inv -> {
                        ItemStack cobble = new ItemStack(Blocks.COBBLESTONE);
                        // 遍历所有槽位，尝试插入
                        for (int i = 0; i < inv.getSlots(); i++) {
                            cobble = inv.insertItem(i, cobble, false);
                            if (cobble.isEmpty()) break; // 插入成功
                        }
                    });
                    // 不调用 destroyBlock，直接返回
                    return;
                }
            }
            // 默认逻辑：正常破坏
            super.tickBreaker(pos);
        }
    }
}
