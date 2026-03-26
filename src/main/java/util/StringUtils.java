package util;

public final class StringUtils {

    private StringUtils() {}

    public static boolean allNotBlank(String... strings) {
        return !anyBlank(strings);
    }

    public static boolean anyBlank(String... strings) {
        if (strings == null) {
            return true;
        }

        for (String str : strings) {
            if (isBlank(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        return str.isBlank();
    }

}
