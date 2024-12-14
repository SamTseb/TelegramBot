package org.joyapi.model.enums;

import lombok.Getter;

@Getter
public enum AuthorTag {
    kawa_v("kawa-v"),
    afrobull("afrobull"),
    nayaase_beleguii("nayaase_beleguii"),
    ;

    private final String value;


    AuthorTag(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        for (AuthorTag tag : AuthorTag.values()) {
            if (tag.value.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
