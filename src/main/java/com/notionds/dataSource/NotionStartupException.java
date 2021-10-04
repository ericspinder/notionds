package com.notionds.dataSource;

public class NotionStartupException extends RuntimeException {

    public enum Type {
        ReflectiveOperationFailed("A reflective operation failed to instantiate class"),
        NullPointerOnGeneric("A null pointer exception happed when trying to create generic");
        private final String description;
        private Type(String description) {
            this.description = description;
        }
        public String getDescription() {
            return this.description;
        }
    }
    private final Type type;
    private final Class where;
    public NotionStartupException(Type type, Class where) {
        this.type = type;
        this.where = where;
    }

    public final Type getType() {
        return this.type;
    }
    public final Class getWhere() {
        return this.where;
    }
}
