package hippofatale.jackspdmmod.block.custom;

import hippofatale.jackspdmmod.networking.ModMessages;
import hippofatale.jackspdmmod.networking.packet.SetSlotMachineScreenS2CPacket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class SlotMachineBlock extends Block {
    public SlotMachineBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!world.isClientSide()) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            ModMessages.sendToPlayer(new SetSlotMachineScreenS2CPacket(0, 0, 0, true), serverPlayer);
        }
        return ActionResultType.CONSUME;
    }
}
