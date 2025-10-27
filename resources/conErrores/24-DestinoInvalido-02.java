///[Error:=|5]

class B {
    void m1() {
        this.m2() = 10;
    }

    void m2() {}
    static void main() {
        this.main();
    }
}