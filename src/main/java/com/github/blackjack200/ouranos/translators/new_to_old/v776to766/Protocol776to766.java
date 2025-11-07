package com.github.blackjack200.ouranos.translators.new_to_old.v776to766;

import com.github.blackjack200.ouranos.base.ProtocolToProtocol;
import com.github.blackjack200.ouranos.converter.ItemTypeDictionary;
import org.cloudburstmc.protocol.bedrock.packet.ItemComponentPacket;

public class Protocol776to766 extends ProtocolToProtocol {
    @Override
    protected void registerProtocol() {
        this.registerClientbound(ItemComponentPacket.class, wrapped -> {
            final ItemComponentPacket packet = (ItemComponentPacket) wrapped.getPacket();

            // The older client only need the non-vanilla item definition send over, they will handle the vanilla one.
            // Well send it if you like to, but just know that the client WON'T LIKE it very much...
            packet.getItems().removeIf(definition -> ItemTypeDictionary.getInstance(wrapped.getOutput()).fromStringId(definition.getIdentifier()) == null);
        });
    }
}
