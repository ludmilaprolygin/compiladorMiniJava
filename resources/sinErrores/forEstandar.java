///[SinErrores]

class Estandar {
    int<K> checkForEstandar() {

        for(int i = 0; i < 10; i++) {}
        for(int i = 0; i < 10; ++i) {}
        for(    i = 0; i < 10; ++i) {}
        for(; i < 10; --i) {}
    }
}