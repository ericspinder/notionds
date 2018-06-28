package com.notionds.dataSource.connection;

public enum State {
    Open("Open, normal operations allowed, even encouraged one may say"),
    Closed("Closed, has already been closed"),
    Empty("Empty, allow current opertions to finish, but do not return to pool (if pooled)"),
    KillWithFire("Kill with fire, should have been destoryed")
    ;
    final String description;
    State(String description) {
        this.description = description;
    }
}
