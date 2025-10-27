///[Error:return|8]
//Author:Franco
class A {
}

class B extends A {
    B m1(){
        return new A();
    }
}
class Init{
    static void main()
    { }
}


