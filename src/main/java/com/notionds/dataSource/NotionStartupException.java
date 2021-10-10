package com.notionds.dataSource;

public class NotionStartupException extends RuntimeException {

    public enum Type {
        ReflectiveOperationFailed("A reflective operation failed to instantiate class"),
        NullPointerOnGeneric("A null pointer exception happened when trying to instantiate or retrieve generic"),
        MissingDefaultValue("Missing a default value"),
        WAITED_TOO_LONG_FOR_CONNECTION("Waited too long for a connection"),
        TEST_CONNECTION_FAILURE("A fatal error in when running the startup connection test")
        ;
        private final String description;
        Type(String description) {
            this.description = description;
        }
        public String getDescription() {
            return this.description;
        }
    }
    public final Type type;
    public final Class where;
    public NotionStartupException(Type type, Class where) {
        super("Type: " + type + ", Where=" + where.getCanonicalName());
        this.type = type;
        this.where = where;
    }
}
