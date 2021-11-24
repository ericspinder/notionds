package com.notionds.dataSupplier;

public class NotionStartupException extends RuntimeException {

    public enum Type {
        ReflectiveOperationFailed("A reflective operation failed to instantiate class"),
        BadCastToGeneric("A bad cast when trying to retrieve a value"),
        ConstructorProblem("Unable to create needed constructor"),
        MissingDefaultValue("Missing a default value"),
        MissMatchedOptionKey("Option key was not set for proper instance value"),
        WAITED_TOO_LONG_FOR_CONNECTION("Waited too long for a connection"),
        SoftReference_Problem("SoftReference problem"),
        TEST_CONNECTION_FAILURE("A fatal error in when running the startup connection test"),
        Timeout_On_Non_Containerized_Notion("A timeout occurred ona non containerized Notion, which has been hard closed by default"),
        No_Failover_Available("No Connection Suppliers in failover stack"),
        UnableToStartJMX("JMX failed to start; there was no NotionDs instance to monitor"),
        UnableToChangeJXM("Unable to change JMX after it been loaded"),
        UnableToStartJMX_className("JMX failed to start; class not found"),
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
        this(type, null, where);
    }
    public NotionStartupException(Type type, Exception causedBy, Class where) {
        super("Type: " + type + ", Where=" + where.getCanonicalName(), causedBy);
        this.type = type;
        this.where = where;
    }
}
