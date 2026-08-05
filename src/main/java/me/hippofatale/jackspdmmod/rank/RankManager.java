package me.hippofatale.jackspdmmod.rank;

import net.minecraft.util.ResourceLocation;

public class RankManager {
    public static class RankTier {
        private final int points;
        private final String name;
        private final ResourceLocation texture;

        public RankTier(int Points, String name, ResourceLocation resourceLocation) {
            this.points = Points;
            this.name = name;
            this.texture = resourceLocation;
        }

        public int getPoints() {
            return points;
        }

        public String getName() {
            return name;
        }

        public ResourceLocation getTexture() {
            return texture;
        }
    }

    private static RankTier[] rankTiers;

    static {
        initializeRanks();
    }

    private static void initializeRanks() {
        rankTiers = new RankTier[] {
            new RankTier(2000, "§4S§r", new ResourceLocation("jackspdmmod:textures/rank/rank_s.png")),
            new RankTier(1500, "§cA§r", new ResourceLocation("jackspdmmod:textures/rank/rank_a.png")),
            new RankTier(1000, "§6B§r", new ResourceLocation("jackspdmmod:textures/rank/rank_b.png")),
            new RankTier(600, "§5C§r", new ResourceLocation("jackspdmmod:textures/rank/rank_c.png")),
            new RankTier(300, "§9D§r", new ResourceLocation("jackspdmmod:textures/rank/rank_d.png")),
            new RankTier(100, "§2E§r", new ResourceLocation("jackspdmmod:textures/rank/rank_e.png")),
            new RankTier(PlayerRankPoint.MIN_POINTS, "§8F§r", new ResourceLocation("jackspdmmod:textures/rank/rank_f.png"))
        };
    }

    public static RankTier getRankTier(int rankPoints) {
        for (RankTier tier : rankTiers) {
            if (rankPoints >= tier.getPoints()) {
                return tier;
            }
        }
        return rankTiers[0];
    }

    public static ResourceLocation getRankTexture(int rankPoints) {
        return getRankTier(rankPoints).getTexture();
    }

    public static String getRankName(int rankPoints) {
        return getRankTier(rankPoints).getName();
    }
}
