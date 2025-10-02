///[Error:x|9]
// Atributo con el mismo nombre que uno en superclase

class A {
    int x;
}

class B extends A {
    char x;
}

class Init {
    static void main() {}
}
