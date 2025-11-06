package com.github.blackjack200.ouranos.translators.new_to_old.v419to408;

import com.github.blackjack200.ouranos.base.ProtocolToProtocol;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponse;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseStatus;
import org.cloudburstmc.protocol.bedrock.packet.ItemStackResponsePacket;

public class Protocol419to408 extends ProtocolToProtocol {
    @Override
    protected void registerProtocol() {
        this.registerClientbound(ItemStackResponsePacket.class, wrapped -> {
            final ItemStackResponsePacket packet = (ItemStackResponsePacket) wrapped.getPacket();

            packet.getEntries().replaceAll(entry -> new ItemStackResponse(entry.getResult().equals(ItemStackResponseStatus.OK), entry.getRequestId(), entry.getContainers()));
        });
    }
}
