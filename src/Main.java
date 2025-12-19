import functions.*;
import functions.basic.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== Задание 1: Итератор ===");
            System.out.println("Тест итератора ArrayTabulatedFunction:");
            Function sin1 = new Sin();
            TabulatedFunction arrayFunc = TabulatedFunctions.tabulate(sin1, 0, Math.PI, 5);

            System.out.println("Итерация по точкам (улучшенный цикл for):");
            for (FunctionPoint p : arrayFunc) {
                System.out.println(p);
            }

            // Тест итератора для LinkedListTabulatedFunction
            System.out.println("\nТест итератора LinkedListTabulatedFunction:");
            Function sin2 = new Sin();
            TabulatedFunction linkedFunc = TabulatedFunctions.tabulate(sin2, 0, Math.PI, 5);
            for (FunctionPoint point : linkedFunc) {
                System.out.println(point);
            }

            System.out.println("\n=== Задание 2: Фабрики ===");
            Function cos = new Cos();
            TabulatedFunction tf;

            tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
            System.out.println("Тип объекта (по умолчанию): " + tf.getClass().getSimpleName());

            TabulatedFunctions.setTabulatedFunctionFactory(
                    new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory()
            );
            tf = TabulatedFunctions.tabulate(cos, 0, Math.PI, 11);
            System.out.println("Тип объекта (после смены фабрики): " + tf.getClass().getSimpleName());

            StringReader reader = new StringReader("3 0 0 5 25 10 100");
            TabulatedFunction readFunc = TabulatedFunctions.readTabulatedFunction(reader);
            System.out.println("Тип объекта, прочитанного из Reader: " + readFunc.getClass().getSimpleName());

            System.out.println("\n=== Задание 3: Рефлексивные методы ===");
            TabulatedFunction fRef;

            fRef = TabulatedFunctions.createTabulatedFunction(
                    ArrayTabulatedFunction.class, 0, 10, 3
            );
            System.out.println("Рефлексия (границы): " + fRef.getClass().getSimpleName());
            System.out.println(fRef);

            FunctionPoint[] points = {
                    new FunctionPoint(0, 0),
                    new FunctionPoint(10, 10)
            };
            fRef = TabulatedFunctions.createTabulatedFunction(
                    LinkedListTabulatedFunction.class, points
            );
            System.out.println("Рефлексия (массив точек): " + fRef.getClass().getSimpleName());
            System.out.println(fRef);

            fRef = TabulatedFunctions.tabulate(
                    LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11
            );
            System.out.println("Рефлексия (tabulate Sin): " + fRef.getClass().getSimpleName());
            System.out.println(fRef);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
