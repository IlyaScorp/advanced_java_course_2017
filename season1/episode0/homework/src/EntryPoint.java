public interface EntryPoint {
    static void main(String... args) {
        FibonacciAlgorithm algorithm = new Fibonaccis();
        System.out.println(algorithm.evaluate(Integer.parseInt(args[0])));
    }
}