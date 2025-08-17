package hippofatale.jackspdmmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import hippofatale.jackspdmmod.club.ClubData;
import hippofatale.jackspdmmod.home.Home;
import hippofatale.jackspdmmod.home.HomeData;
import hippofatale.jackspdmmod.home.HomeType;
import hippofatale.jackspdmmod.tileentity.PlacardTile;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static hippofatale.jackspdmmod.JacksPDMMod.*;

public class HomeCommand {
    public HomeCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("땅")
                .then(Commands.literal("철거")
                        .then(Commands.literal("개인").executes((command) -> {
                            return demolishHome(command.getSource(), HomeType.PERSONAL);}))

                        .then(Commands.literal("동아리").executes((command) -> {
                            return demolishHome(command.getSource(), HomeType.CLUB);}))
                )

                .then(Commands.literal("권한")
                        .then(Commands.literal("주기").then(Commands.argument("플레이어", EntityArgument.player()).executes((command) -> {
                            return shareHome(command.getSource(), EntityArgument.getPlayer(command, "플레이어"), true);})))

                        .then(Commands.literal("뺐기").then(Commands.argument("플레이어", EntityArgument.player()).executes((command) -> {
                            return shareHome(command.getSource(), EntityArgument.getPlayer(command, "플레이어"), false);})))

                        .then(Commands.literal("정보").executes((command) -> {
                            return shareInfo(command.getSource());}))
                )
        );
    }

    private int demolishHome(CommandSource source, HomeType homeType) {
        try {
            ServerPlayerEntity player = source.getPlayerOrException();
            switch (homeType) {
                case PERSONAL: {
                    if (personalHomes.getOrDefault(player.getUUID(), null) == null) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.no_personal_home"), false);
                        return 0;
                    } else {
                        BlockPos blockPos = HomeData.getOwningPersonalHome(player.getUUID()).getPlacardPos();
                        if (player.getLevel().getBlockEntity(blockPos) instanceof PlacardTile) {
                            PlacardTile placard = (PlacardTile) player.getLevel().getBlockEntity(blockPos);
                            if (placard != null) {
                                placard.setPurchased(false);
                            }
                        }
                        personalHomes.remove(player.getUUID());
                        HomeData.saveHomeData();
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.demolished_personal_home"), false);
                        return 1;
                    }
                }
                case CLUB: {
                    if (!playerClubs.containsKey(player.getUUID())) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_in_club"), false);
                        return 0;
                    } else if (!ClubData.getBelongingClub(player.getUUID()).isPresident(player.getUUID())) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.not_president"), false);
                        return 0;
                    } else if (clubNameHomes.get(playerClubs.get(player.getUUID())) == null) {
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.no_club_home"), false);
                        return 0;
                    } else {
                        BlockPos blockPos = HomeData.getHomeFromBelongingClubName(player.getUUID()).getPlacardPos();
                        if (player.getLevel().getBlockEntity(blockPos) instanceof PlacardTile) {
                            PlacardTile placard = (PlacardTile) player.getLevel().getBlockEntity(blockPos);
                            if (placard != null) {
                                placard.setPurchased(false);
                            }
                        }
                        clubNameHomes.remove(playerClubs.get(player.getUUID()));
                        HomeData.saveHomeData();
                        player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.demolished_club_home"), false);
                        return 1;
                    }
                }
                default: return 0;
            }
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private int shareHome(CommandSource source, ServerPlayerEntity target, boolean isAddSharing) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        if (personalHomes.getOrDefault(player.getUUID(), null) == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.no_personal_home"), false);
            return 0;
        }
        if (target == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.player_not_found"), false);
            return 0;
        }
        if (target.getUUID().equals(player.getUUID())) {
            player.displayClientMessage(new StringTextComponent("자신에게 권한을 주거나 뺐을 수 없습니다."), false);
            return 0;
        }

        Home sharingHome = personalHomes.get(player.getUUID());
        if (isAddSharing) {
            if (sharingHome.isSharingFull()) {
                player.displayClientMessage(new StringTextComponent("최대 2명까지만 권한을 줄 수 있습니다."), false);
                return 0;
            } else {
                sharingHome.addSharer(target.getUUID(), target.getName().getString());
                target.displayClientMessage(new StringTextComponent(player.getName().getString()).append("님의 개인 땅 권한을 공유받았습니다."), false);
                player.displayClientMessage(new StringTextComponent(target.getName().getString()).append("님에게 개인 땅 권한을 공유했습니다."), false);
                return 1;
            }
        } else {
            if (!sharingHome.getSharedPlayers().containsKey(target.getUUID())) {
                player.displayClientMessage(new StringTextComponent("개인 땅 권한이 없는 플레이어입니다."), false);
                return 0;
            } else {
                sharingHome.removeSharer(target.getUUID());
                target.displayClientMessage(new StringTextComponent(player.getName().getString()).append("님이 개인 땅 권한을 회수했습니다."), false);
                player.displayClientMessage(new StringTextComponent(target.getName().getString()).append("님에게 개인 땅 권한을 회수했습니다."), false);
                return 1;
            }
        }
    }

    private int shareInfo(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrException();
        if (personalHomes.getOrDefault(player.getUUID(), null) == null) {
            player.displayClientMessage(new TranslationTextComponent("message.jackspdmmod.no_personal_home"), false);
            return 0;
        }
        Home sharingHome = personalHomes.get(player.getUUID());
        List<String> sharedMembers = new ArrayList<>();
        for (Map.Entry<UUID, String> entry : sharingHome.getSharedPlayers().entrySet()) {
            sharedMembers.add(entry.getValue());
        }
        player.displayClientMessage(new StringTextComponent("주인: ").append(player.getName().getString()), false);
        player.displayClientMessage(new StringTextComponent("공유: ").append(String.join(", ", sharedMembers)), false);
        return 1;
    }
}
