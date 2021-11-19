package com.notionds.dataSupplier.pool;

public enum Situation {
    New_Unattached("New, still needs an open connection"),
    Open("Open, held by client, normal operations allowed, even encouraged one may say"),
    Pooled("Held in Pool"),
    Empty("Notion will not be added to a pool, but will allow current operations to finish, but do not return to pool"),
    Closed("Notion is closed, do not reference object after this"),
    ;
    final String description;
    Situation(String description) {
        this.description = description;
    }
}
