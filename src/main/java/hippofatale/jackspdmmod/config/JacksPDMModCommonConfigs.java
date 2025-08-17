package hippofatale.jackspdmmod.config;

import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class JacksPDMModCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

//    public static final ForgeConfigSpec.ConfigValue<List<Integer>> TEST_GACHA_WEIGHT_LIST;

    static {
        BUILDER.push("Jack's PDMonster Config");

//        TEST_GACHA_WEIGHT_LIST = BUILDER.comment("가챠에서 나오는 아이템의 확률 비율(정수) 목록")
//                        .defineInList("Test Gacha Weight List", ;int, );

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
