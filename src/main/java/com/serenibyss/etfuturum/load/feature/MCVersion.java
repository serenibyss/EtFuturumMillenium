package com.serenibyss.etfuturum.load.feature;

public enum MCVersion {

    MC1_12("1.12", "1.12.2"),
    MC1_13("1.13", "1.13.2"),
    MC1_14("1.14", "1.14.4"),
    MC1_15("1.15", "1.15.2"),
    MC1_16("1.16", "1.16.5"),
    MC1_17("1.17", "1.17.1"),
    MC1_18("1.18", "1.18.2"),
    MC1_19("1.19", "1.19.4"),
    MC1_20("1.20", "1.20.4");

    private final String version, latestPatch;

    MCVersion(String version, String latestPatch) {
        this.version = version;
        this.latestPatch = latestPatch;
    }

    public String getVersion() {
        return version;
    }

    public String getLatestVersion() {
        return latestPatch;
    }
}
