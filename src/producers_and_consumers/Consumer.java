package producers_and_consumers;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Consumer<ProductType> implements Runnable {

    private final Table<ProductType> table;
    private final int timeToConsume;

    public Consumer(Table<ProductType> table, int timeToConsume) {
        this.table = table;
        this.timeToConsume = timeToConsume;
    }

    @Override
    public void run() {
        try {
            while (true) {
                MILLISECONDS.sleep(timeToConsume);
                ProductType product = table.consume();
            }

        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }
}
