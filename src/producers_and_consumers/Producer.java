package producers_and_consumers;

import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Producer<ProductType> implements Runnable {

    private final Table<ProductType> table;
    private final int timeToProduce;
    private final AtomicInteger produceCounter = new AtomicInteger(1);

    public Producer(Table<ProductType> table, int timeToProduce) {
        this.table = table;
        this.timeToProduce = timeToProduce;
    }

    @Override
    public void run() {
        try {
            while (true) {
                MILLISECONDS.sleep(timeToProduce);
                ProductType product = (ProductType) ("Product №" + produceCounter.getAndIncrement());
                table.produce(product);
            }

        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }
}
