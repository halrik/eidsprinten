package com.halrik.eidsprinten.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Template {
    START_LIST_HEATS("startlist-heats.html"),
    START_LIST_FINALS("startlist-finals.html"),
    ADVANCEMENT_SETUP("advancement-setup.html"),
    RESULT_LIST("resultlist.html"),
    STARTNUMBERS("startnumbers.html");

    private final String templateName;

}
