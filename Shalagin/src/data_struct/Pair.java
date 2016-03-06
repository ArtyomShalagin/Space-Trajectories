package data_struct;

public class Pair<T1, T2> {
    private T1 val1;
    private T2 val2;

    public Pair(T1 val1, T2 val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    public T1 get1() {
        return val1;
    }

    public T2 get2() {
        return val2;
    }

    public void set1(T1 val1) {
        this.val1 = val1;
    }

    public void set2(T2 val2) {
        this.val2 = val2;
    }

    public boolean equals(Object o) {
        if (o instanceof Pair)
            return equals((Pair) o);
        return false;
    }

    public boolean equals(Pair p) {
        return val1.equals(p.val1) && val2.equals(p.val2);
    }

    public int hashCode() {
        return val1.hashCode() * 94329 + val2.hashCode() * 5782;
    }

    public String toString() {
        return "[" + val1.toString()
                 + "; " + val2.toString() + "]";
    }
}
