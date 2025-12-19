package threads;

import functions.*;
import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private Task task;
    public SimpleGenerator(Task task){
        this.task = task;
    }

    @Override
    public void run() {
        Random random = new Random();
        int tasksCount = task.getTasksCount();

        for (int i = 0; i < tasksCount; i++) {
            double base = 1 + random.nextDouble() * 9;
            double left = random.nextDouble() * 100;
            double right = 100 + random.nextDouble() * 100;
            double step = random.nextDouble();
            Log logFunction = new Log(base);

            synchronized (task){
                task.setFunction(logFunction);
                task.setLeftBorder(left);
                task.setRightBorder(right);
                task.setDiscretizationStep(step);
            }

            System.out.printf("Generator: Source %.4f %.4f %.4f %d %n", left, right, step, i);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Generator finished.");
    }
}
