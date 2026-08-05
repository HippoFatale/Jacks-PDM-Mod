package me.hippofatale.jackspdmmod.block.custom;

import me.hippofatale.jackspdmmod.home.HomeSize;
import me.hippofatale.jackspdmmod.home.HomeType;
import me.hippofatale.jackspdmmod.networking.ModMessages;
import me.hippofatale.jackspdmmod.networking.packet.SetPlacardScreenS2CPacket;
import me.hippofatale.jackspdmmod.tileentity.PlacardTile;
import me.hippofatale.jackspdmmod.tileentity.ModTileEntities;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.math.BigDecimal;

public class PlacardBlock extends StandingSignBlock {
    private HomeType plotType;
    private HomeSize plotSize;

    public PlacardBlock(Properties properties, HomeType homeType, HomeSize homeSize, WoodType woodType) {
        super(properties, woodType);
        this.plotType = homeType;
        this.plotSize = homeSize;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        FluidState fluidstate = p_196258_1_.getLevel().getFluidState(p_196258_1_.getClickedPos());
        return this.defaultBlockState().setValue(ROTATION, Integer.valueOf((MathHelper.floor((double)((180.0F + p_196258_1_.getRotation()) * 16.0F / 360.0F) + 0.5D) & 15) + 1) / 4 * 4 % 16).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
    }

    @Override
    public void setPlacedBy(World world, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity entity, ItemStack itemStack) {
        super.setPlacedBy(world, blockPos, blockState, entity, itemStack);

        if (!world.isClientSide()) {
            TileEntity tileEntity = world.getBlockEntity(blockPos);
            if (tileEntity instanceof PlacardTile) {
                PlacardTile placard = (PlacardTile) tileEntity;
                placard.setPlotType(this.plotType);
                placard.setPlotSize(this.plotSize);
                placard.setRotation(blockState.getValue(ROTATION));
            }
        }
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!world.isClientSide()) {
            TileEntity tileEntity = world.getBlockEntity(blockPos);
            if (tileEntity instanceof PlacardTile) {
                PlacardTile placard = (PlacardTile) tileEntity;
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                ModMessages.sendToPlayer(new SetPlacardScreenS2CPacket(placard), serverPlayer);
            }
        }
        return ActionResultType.CONSUME;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.PLACARD_TILE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}
