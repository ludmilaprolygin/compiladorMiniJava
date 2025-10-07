///[SinErrores]

class Estandar {
    int<K> checkForEstandar() {

        for(int i = 0; i < 10; i++) {}
        for(int i = 0; i < 10; ++i) {}
        for(    i = 0; i < 10; ++i) {}
        for(; i < 10; --i) {}
        for(;;) {}
        for(var i = 10;;) {}
        for(int i;;) {}
        for(i;;) {}
        for(i;; i=i+1) {}
        for(var f: x.get());
        for(var a=10; a >10 ; a+b) {}
    }
}