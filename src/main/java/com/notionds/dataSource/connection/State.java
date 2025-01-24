package com.notionds.dataSource.connection;

public enum State {
    Loaned("Open, held by database client, normal operations allowed"),
    Pooled("Held in Pool"),
    Empty("Empty, allow current operations to finish, but do not return to pool"),
    ;
    final String description;
    State(String description) {
        this.description = description;
    }
}
