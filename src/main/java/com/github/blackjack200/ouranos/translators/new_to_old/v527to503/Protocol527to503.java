package com.github.blackjack200.ouranos.translators.new_to_old.v527to503;

import com.github.blackjack200.ouranos.base.ProtocolToProtocol;
import org.cloudburstmc.protocol.bedrock.data.Ability;
import org.cloudburstmc.protocol.bedrock.data.AdventureSetting;
import org.cloudburstmc.protocol.bedrock.data.InputInteractionModel;
import org.cloudburstmc.protocol.bedrock.packet.AdventureSettingsPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerActionPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.cloudburstmc.protocol.bedrock.packet.RequestAbilityPacket;

import java.util.Optional;

public class Protocol527to503 extends ProtocolToProtocol {
    @Override
    protected void registerProtocol() {
        this.registerServerbound(RequestAbilityPacket.class, wrapped -> {
            final RequestAbilityPacket packet = (RequestAbilityPacket) wrapped.getPacket();

            final AdventureSettingsPacket newPacket = new AdventureSettingsPacket();
            newPacket.setUniqueEntityId(wrapped.session().getUniqueId());
            if (packet.getAbility() == Ability.FLYING) {
                newPacket.getSettings().add(AdventureSetting.FLYING);
            }
            wrapped.setPacket(newPacket);
        });

        this.registerServerbound(PlayerAuthInputPacket.class, wrapped -> {
            final PlayerAuthInputPacket packet = (PlayerAuthInputPacket) wrapped.getPacket();
            packet.setInputInteractionModel(Optional.ofNullable(packet.getInputInteractionModel()).orElse(InputInteractionModel.CLASSIC));
        });

        this.registerServerbound(PlayerActionPacket.class, wrapped -> {
            final PlayerActionPacket packet = (PlayerActionPacket) wrapped.getPacket();
            packet.setResultPosition(packet.getBlockPosition());
        });
    }
}
