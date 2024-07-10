package de.tiiita.util;

//Untested
public class OsUtils {
    public static OsType identifyOsType() {
        String osName = System.getProperty("os.name");

        if (osName.contains("win")) {
            return OsType.WINDOWS;
        } else if (osName.contains("mac")) {
            return OsType.MAC_OS;
        } else if (osName.contains("nix") || osName.contains("nux")) {
            return OsType.LINUX;
        } else if (osName.contains("cros")) {
            return OsType.CHROME_OS;
        } else {
            return OsType.UNKNOWN;
        }
    }

    public enum OsType {
        MAC_OS("MacOS"),
        LINUX("Linux based os"),
        CHROME_OS("ChromeOS"),
        WINDOWS("Windows"),
        UNKNOWN("Unknown OS");

        private final String displayName;

        OsType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}

