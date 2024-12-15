package org.joyapi.model.enums;

import lombok.Getter;

@Getter
public enum BotOption {
    start("/start"),
    new_author("/new_author"),
    sync_posts("/sync_posts"),
    invalid_option("/invalid_option"),;

    private final String value;


    BotOption(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        for (BotOption tag : BotOption.values()) {
            if (tag.value.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
