package com.github.blackjack200.ouranos.converter.block.impl;

import com.github.blackjack200.ouranos.converter.BlockStateDictionary;
import lombok.experimental.UtilityClass;
import org.cloudburstmc.nbt.NbtMap;

@UtilityClass
public class BlockHashDowngrader786to776 {
    public BlockStateDictionary.Dictionary.BlockEntry downgradeState(BlockStateDictionary.Dictionary.BlockEntry entry) {
        return switch (entry.name()) {
            case "minecraft:leaf_litter", "minecraft:wildflowers" ->
                    new BlockStateDictionary.Dictionary.BlockEntry("minecraft:pink_petals", entry.rawState(), -1, -1);
            case "minecraft:firefly_bush" ->
                    new BlockStateDictionary.Dictionary.BlockEntry("minecraft:tall_grass", NbtMap.builder().putBoolean("upper_block_bit", true).build(), -1, -1);
            case "minecraft:bush" ->
                    new BlockStateDictionary.Dictionary.BlockEntry("minecraft:sweet_berry_bush", NbtMap.builder().putInt("growth", 0).build(), -1, -1);
            case "minecraft:tall_dry_grass", "minecraft:short_dry_grass" ->
                    new BlockStateDictionary.Dictionary.BlockEntry("minecraft:fire_tube_coral_fan", NbtMap.builder().putInt("coral_fan_direction", 0).build(), -1, -1);
            case "minecraft:cactus_flower" ->
                    new BlockStateDictionary.Dictionary.BlockEntry("minecraft:brain_coral_fan", NbtMap.builder().putInt("coral_fan_direction", 0).build(), -1, -1);
            default -> entry;
        };

    }
}
