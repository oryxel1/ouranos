package com.github.blackjack200.ouranos.old_translators;

import com.github.blackjack200.ouranos.ProtocolInfo;
import com.github.blackjack200.ouranos.converter.ItemTypeDictionary;
import com.github.blackjack200.ouranos.converter.biome.BiomeDefinitionRegistry;
import com.github.blackjack200.ouranos.session.OuranosSession;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.codec.v419.Bedrock_v419;
import org.cloudburstmc.protocol.bedrock.codec.v527.Bedrock_v527;
import org.cloudburstmc.protocol.bedrock.codec.v544.Bedrock_v544;
import org.cloudburstmc.protocol.bedrock.codec.v589.Bedrock_v589;
import org.cloudburstmc.protocol.bedrock.codec.v649.Bedrock_v649;
import org.cloudburstmc.protocol.bedrock.codec.v671.Bedrock_v671;
import org.cloudburstmc.protocol.bedrock.codec.v685.Bedrock_v685;
import org.cloudburstmc.protocol.bedrock.codec.v712.Bedrock_v712;
import org.cloudburstmc.protocol.bedrock.codec.v729.Bedrock_v729;
import org.cloudburstmc.protocol.bedrock.codec.v800.Bedrock_v800;
import org.cloudburstmc.protocol.bedrock.data.InputInteractionModel;
import org.cloudburstmc.protocol.bedrock.data.biome.BiomeDefinitions;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.*;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponse;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseStatus;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.ItemUseTransaction;
import org.cloudburstmc.protocol.bedrock.packet.*;

import java.awt.*;
import java.util.*;
import java.util.List;

@SuppressWarnings("ALL")
public class ProtocolRewriterTranslator implements BaseTranslator {
    @Override
    public BedrockPacket translateClientbound(OuranosSession session, BedrockPacket bedrockPacket) {
        final int input = session.getTargetVersion(), output = session.getProtocolId();

        if (bedrockPacket instanceof PlayerListPacket packet) {
            for (final PlayerListPacket.Entry entry : packet.getEntries()) {
                entry.setColor(Objects.requireNonNullElse(entry.getColor(), Color.WHITE));
                if (entry.getSkin() != null) {
                    entry.setSkin(entry.getSkin().toBuilder().geometryDataEngineVersion(ProtocolInfo.getPacketCodec(output).getMinecraftVersion()).build());
                }
                if (input < Bedrock_v649.CODEC.getProtocolVersion()) {
                    entry.setSubClient(false);
                }
            }
        } if (input < Bedrock_v544.CODEC.getProtocolVersion() && bedrockPacket instanceof ModalFormResponsePacket packet) {
            if (session.prevFormId == packet.getFormId()) {
                return null;
            }

            session.prevFormId = packet.getFormId();
        }

        if (input < Bedrock_v729.CODEC.getProtocolVersion() && bedrockPacket instanceof TransferPacket packet) {
            packet.setReloadWorld(true);
        }

        if (bedrockPacket instanceof ItemStackResponsePacket packet && input >= Bedrock_v419.CODEC.getProtocolVersion() && output < Bedrock_v419.CODEC.getProtocolVersion()) {
            final List<ItemStackResponse> translated = packet.getEntries().stream().map((entry) -> {
                return new ItemStackResponse(entry.getResult().equals(ItemStackResponseStatus.OK), entry.getRequestId(), entry.getContainers());
            }).toList();
            packet.getEntries().clear();
            packet.getEntries().addAll(translated);
        }

        return translateBothWay(session, bedrockPacket, input, output);
    }

    @Override
    public BedrockPacket translateServerbound(OuranosSession session, BedrockPacket bedrockPacket) {
        final int input = session.getProtocolId(), output = session.getTargetVersion();

        if (input < Bedrock_v712.CODEC.getProtocolVersion() && bedrockPacket instanceof InventoryTransactionPacket packet) {

        }

        return translateBothWay(session, bedrockPacket, input, output);
    }

    private BedrockPacket translateBothWay(OuranosSession session, final BedrockPacket bedrockPacket, int input, int output) {
        if (input < Bedrock_v685.CODEC.getProtocolVersion() && bedrockPacket instanceof ContainerClosePacket packet) {
            packet.setType(ContainerType.NONE);
        } else if (input < Bedrock_v671.CODEC.getProtocolVersion() && bedrockPacket instanceof ResourcePackStackPacket packet) {
            packet.setHasEditorPacks(false);
        } else if (input < Bedrock_v589.CODEC.getProtocolVersion() && bedrockPacket instanceof EmotePacket packet) {
            packet.setXuid("");
            packet.setPlatformId("");
            packet.setEmoteDuration(20);
        }

        return bedrockPacket;
    }

}
