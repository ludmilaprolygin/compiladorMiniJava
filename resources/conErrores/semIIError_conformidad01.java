///[Error:m1|10]
//Author: Ludmi
class A {
    void m1(){}
}

class B extends A {
    void m2(){
        var i = 10;
        i = m1();
    }
}

class Ex {
    static void main () {}
}