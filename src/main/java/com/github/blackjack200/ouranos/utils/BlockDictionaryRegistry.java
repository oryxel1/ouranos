package com.github.blackjack200.ouranos.utils;

import com.github.blackjack200.ouranos.converter.BlockStateDictionary;
import lombok.val;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleBlockDefinition;
import org.cloudburstmc.protocol.common.DefinitionRegistry;

public class BlockDictionaryRegistry implements DefinitionRegistry<BlockDefinition> {
    public final int protocol;

    public BlockDictionaryRegistry(int protocol) {
        this.protocol = protocol;
    }

    @Override
    public BlockDefinition getDefinition(int runtimeId) {
        final BlockStateDictionary.Dictionary entry = BlockStateDictionary.getInstance(this.protocol);
        final Integer hash = entry.toLatestStateHash(runtimeId);
        final BlockStateDictionary.Dictionary.BlockEntry states = entry.lookupStateFromStateHash(hash);
        if (states == null) {
            return () -> runtimeId; // This is fine.
        }
        return new SimpleBlockDefinition(states.name(), runtimeId, states.rawState());
    }

    @Override
    public boolean isRegistered(BlockDefinition blockDefinition) {
        return true;
    }
}
