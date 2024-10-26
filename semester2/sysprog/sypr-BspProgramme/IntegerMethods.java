//IntegerMethods.java

public final class IntegerMethods {
    private IntegerMethods() { }

    public static int max(final int m, final int n) {
        return m > n ? m : n;
    }

    public static int min(final int m, final int n) {
        return m < n ? m : n;
    }
}