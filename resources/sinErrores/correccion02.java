///[SinErrores]

class A<T>{ T f() {} }
class B extends A<Base>{ Base f() {}}
class Base{}

class Tester {
    static void main() {}
}