package functions;

import java.io.*;
import java.lang.reflect.Constructor;

public final class TabulatedFunctions {
    public static final double EPSILON = 1e-10;

    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions() {
        throw new AssertionError("Экземпляры утилитного класса не создаются");
    }

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }


    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }


    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass, double leftX, double rightX, int pointsCount) {
        try {
            Constructor<? extends TabulatedFunction> c = functionClass.getConstructor(double.class, double.class, int.class);
            return c.newInstance(leftX, rightX, pointsCount);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass, double leftX, double rightX, double[] values) {
        try {
            Constructor<? extends TabulatedFunction> c = functionClass.getConstructor(double.class, double.class, double[].class);
            return c.newInstance(leftX, rightX, values);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(
            Class<? extends TabulatedFunction> functionClass, FunctionPoint[] points) {
        try {
            Constructor<? extends TabulatedFunction> c = functionClass.getConstructor(FunctionPoint[].class);
            return c.newInstance((Object) points);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


    public static TabulatedFunction tabulate(Function f, double leftX, double rightX, int pointsCount) {
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            values[i] = f.getFunctionValue(leftX + i * step);
        }
        return createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> functionClass,
                                             Function f, double leftX, double rightX, int pointsCount) {
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            values[i] = f.getFunctionValue(leftX + i * step);
        }
        return createTabulatedFunction(functionClass, leftX, rightX, values);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeInt(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        dataOut.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        int pointsCount = dataIn.readInt();
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            points[i] = new FunctionPoint(dataIn.readDouble(), dataIn.readDouble());
        }
        return createTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        PrintWriter dataOut = new PrintWriter(out);
        dataOut.print(function.getPointsCount() + " ");
        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOut.print(function.getPointX(i) + " " + function.getPointY(i) + (i < function.getPointsCount() - 1 ? " " : ""));
        }
        dataOut.flush();
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.parseNumbers();
        if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Ошибка формата");
        int pointCount = (int) tokenizer.nval;
        FunctionPoint[] points = new FunctionPoint[pointCount];
        for (int i = 0; i < pointCount; i++) {
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Ошибка X");
            double x = tokenizer.nval;
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Ошибка Y");
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }
        return createTabulatedFunction(points);
    }
}
