package com.notionds.dataSource.connection;

public enum State {
    New_Needs_Connection("New, still needs an open connection"),
    Open("Open, normal operations allowed, even encouraged one may say"),
    Pooled("Held in Pool"),
    Closed("Closed, has already been closed"),
    Empty("Empty, allow current opertions to finish, but do not return to pool (if pooled)"),
    ;
    final String description;
    State(String description) {
        this.description = description;
    }
}
