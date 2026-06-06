package hippofatale.jackspdmmod.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OreSeedTile extends TileEntity implements ITickableTileEntity {
    private int tickCount = 0;
    private final int oreSpawnInterval = 200; //config
    List<Vector3i> oreSpawnPos = Arrays.asList(new Vector3i[]{
            new Vector3i(1, 0, 0),
            new Vector3i(0, 0, 1),
            new Vector3i(-1, 0, 0),
            new Vector3i(0, 0, -1),
            new Vector3i(0, 1, 0),
            new Vector3i(1, 0, 1),
            new Vector3i(1, 0, -1),
            new Vector3i(-1, 0, -1),
            new Vector3i(-1, 0, 1),
            new Vector3i(1, 1, 0),
            new Vector3i(0, 1, 1),
            new Vector3i(-1, 1, 0),
            new Vector3i(0, 1, -1),
            new Vector3i(2, 0, 0),
            new Vector3i(0, 0, 2),
            new Vector3i(-2, 0, 0),
            new Vector3i(0, 0, -2),
            new Vector3i(0, 2, 0)
    });
    List<Block> oreSpawnType = Arrays.asList(new Block[]{
            Blocks.COAL_ORE,
            Blocks.LAPIS_ORE,
            Blocks.REDSTONE_ORE,
            Blocks.IRON_ORE,
            Blocks.GOLD_ORE,
            Blocks.DIAMOND_ORE,
            Blocks.EMERALD_ORE
    });
    List<Integer> oreSpawnWeight = Arrays.asList(new Integer[]{ //config
            70,
            50,
            40,
            30,
            20,
            10,
            1
    });
    int weightTotal = 0;
    List<Block> oreSpawnList = new ArrayList<>();

    public OreSeedTile(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
        for (int i = 0; i < oreSpawnWeight.size(); i++) {
            weightTotal = weightTotal + oreSpawnWeight.get(i);
            for (int j = 0; j < oreSpawnWeight.get(i); j++) {
                oreSpawnList.add(oreSpawnType.get(i));
            }
        }
    }

    public OreSeedTile() {
        this(ModTileEntities.ORE_SEED_TILE.get());
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        return super.save(nbt);
    }


    @Override
    public void tick() {
        if (getLevel() != null && !getLevel().isClientSide()) {
            tickCount++;
            if (tickCount >= oreSpawnInterval) {
                for (int i = 0; i < oreSpawnPos.size(); i++) {
                    if (getLevel().getBlockState(getBlockPos().offset(oreSpawnPos.get(i))).getBlock().is(Blocks.AIR)) {
                        Random random = new Random();
                        getLevel().setBlock(getBlockPos().offset(oreSpawnPos.get(i)), oreSpawnList.get(random.nextInt(weightTotal)).defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                        break;
                    }
                }
                tickCount = 0;
            }
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return super.getUpdateTag();
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
    }
}
