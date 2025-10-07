///[SinErrores]

// 1. Basic generic class declaration
class Box<T> {
    private T content;
}

// 2. Inheriting with explicit type parameter
class StringBox<String> extends Box<String> {
    // Type parameter specified
}

// 3. Inheriting with own type parameter
class Container<E> extends Box<E> {
    // Type parameter passed through
}

// 4. Multiple type parameters
class Pair<K> {
    private K key;
}

// 5. Inheriting from multiple-parameter generic
class StringIntPair<String> extends Pair<String> {
    // Both type parameters specified
}

// 6. Generic class with bounded type
class NumberBox<T> {
    private T value;
}

// 7. Inheriting with bounded type specified
class IntegerBox<Integer> extends NumberBox<Integer> {
    // Concrete type within bounds
}