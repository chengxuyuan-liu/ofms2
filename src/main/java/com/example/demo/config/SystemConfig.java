package com.example.demo.config;

public class SystemConfig {
    private static Integer SYSTEM_SPACE;
    private static Integer SYSTEM_USED_SPACE;
    private static Integer SYSTEM_AVAILABLE_SPACE;

    public static Integer getSystemSpace() {
        return SYSTEM_SPACE;
    }

    public static void setSystemSpace(Integer systemSpace) {
        SYSTEM_SPACE = systemSpace;
    }

    public static Integer getSystemUsedSpace() {
        return SYSTEM_USED_SPACE;
    }

    public static void setSystemUsedSpace(Integer systemUsedSpace) {
        SYSTEM_USED_SPACE = systemUsedSpace;
    }

    public static Integer getSystemAvailableSpace() {
        return SYSTEM_AVAILABLE_SPACE;
    }

    public static void setSystemAvailableSpace(Integer systemAvailableSpace) {
        SYSTEM_AVAILABLE_SPACE = systemAvailableSpace;
    }
}
