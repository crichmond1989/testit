package testit

class ReflectionUtils {
    static void invokeMethod(Object source, String method) {
        source."$method"()
    }

    static String invokeStringMethod(Object source, String method) {
        return source."$method"()
    }
}