package appengine.parser.utils;

public class OtherUtils {

    public static Byte boolToByte(boolean value) {

        if (value) {
            return (byte) 1;
        } else {
            return (byte) 0;
        }
    }

    public static boolean byteToBool(byte value) {
        if (value == 1) {
            return true;
        } else {
            return false;
        }
    }
}
