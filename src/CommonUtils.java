public class CommonUtils {

    public static String fetchClassName(String name) {
        if (!name.contains("."))
            return name;

        return name.substring(name.lastIndexOf('.') + 1);
    }

}
