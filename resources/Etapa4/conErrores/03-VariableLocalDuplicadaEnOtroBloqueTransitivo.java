///[Error:x|16]
// Variable local duplicada en el metodo m2
//Author:Franco
class A {
    void m1() {
        var x = 10;
    }

    void m2() {
        var x = 10;
        {
            {
                {
                    {
                        {
                            var x = 20;
                        }
                    }
                }
            }
        }
    }

    static void main() {}
}