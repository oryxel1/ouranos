package com.github.blackjack200.ouranos.converter.block;

import com.github.blackjack200.ouranos.converter.BlockStateDictionary;
import com.github.blackjack200.ouranos.converter.block.impl.BlockHashDowngrader786to776;
import com.github.blackjack200.ouranos.utils.HashUtils;
import org.cloudburstmc.protocol.bedrock.codec.v786.Bedrock_v786;

import java.util.Map;
import java.util.TreeMap;

public class BlockHashDowngrader {
    private static final TreeMap<Integer, Processor> CONVERTERS = new TreeMap<>();
    private interface Processor {
        BlockStateDictionary.Dictionary.BlockEntry process(BlockStateDictionary.Dictionary.BlockEntry entry);
    }

    static {
        CONVERTERS.put(Bedrock_v786.CODEC.getProtocolVersion(), BlockHashDowngrader786to776::downgradeState);
    }

    public static Integer downgradeHash(BlockStateDictionary.Dictionary.BlockEntry entry, int input, int output) {
        if (input <= output) {
            return entry.latestStateHash();
        }

        for (Map.Entry<Integer, Processor> mapper : CONVERTERS.descendingMap().entrySet()) {
            final int protocolVersion = mapper.getKey();
            if (protocolVersion < output) {
                break;
            }
            if (protocolVersion > input) {
                continue;
            }

            entry = mapper.getValue().process(entry);
        }

        return HashUtils.computeBlockStateHash(entry.name(), entry.rawState());
    }
}
