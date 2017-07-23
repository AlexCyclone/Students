package com.gmail.babanin.aleksey.group;

public interface CompareNull {
    public int NO_NULL = 3;

    static int compare(Object a, Object b) {
        if (a == null && b == null) {
            return 0;
        }
        if (a != null && b == null) {
            return 1;
        }
        if (a == null && b != null) {
            return -1;
        }

        return NO_NULL;
    }
}
