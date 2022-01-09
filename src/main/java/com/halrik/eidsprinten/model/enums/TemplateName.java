package com.halrik.eidsprinten.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TemplateName {
    START_LIST_HEATS("startlist-heats.html");

    private final String templateName;

}
