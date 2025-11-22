///B&C&exitosamente

class A {

}

class B extends A {

}

class C extends A {

}

class Init{
    static void main(){
        var i = true;

        var x = new B();
        var y = new C();

        var a = i ? x : y;
        var b = !i ? x : y;

        a.toString();
        b.toString();
    }
}