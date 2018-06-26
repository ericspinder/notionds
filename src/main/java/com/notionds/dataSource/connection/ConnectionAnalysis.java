package com.notionds.dataSource.connection;

public abstract class ConnectionAnalysis {

    public enum Recommendation {
        CloseConnectionInstance("Close Connection"),
        FailoverDatabase("Failover Database"),
        NoAction("No additional action")
        ;
        private String description;
        Recommendation(String description) {
            this.description = description;
        }
        public String getDescription() {
            return this.description;
        }
    }

    public ConnectionAnalysis()
}
