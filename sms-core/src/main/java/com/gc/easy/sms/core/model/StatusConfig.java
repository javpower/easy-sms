package com.gc.easy.sms.core.model;

public enum StatusConfig {
    UPDATE("UPDATE"),
    NONE("NONE"),
    CREATE("CREATE");

    private final String statusConfig;

    StatusConfig(String statusConfig) {
        this.statusConfig = statusConfig;
    }

    public String getStatusConfig() {
        return statusConfig;
    }
}