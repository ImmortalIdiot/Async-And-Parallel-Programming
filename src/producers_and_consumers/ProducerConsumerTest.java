package producers_and_consumers;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProducerConsumerTest {

    private static JPanel[] squares;
    private static JFrame frame;
    private static final int capacity = 10;
    private static final Random random = new Random();

    public static void main(String[] args) {
        testDataWithoutFixedTime();
    }

    private static void  testDataWithFixedTime() {
        Table<String> table = new Table<>(capacity);
        createAndShowGUI();

        int fixedTimeMillis = 100;

        int countProducers = 8;
        int countConsumers = 8;

        List<Producer<String>> producers = new ArrayList<>();
        List<Consumer<String>> consumers = new ArrayList<>();

        for (int i = 0; i < countProducers; i++) {
            producers.add(new Producer<>(table, fixedTimeMillis));
        }

        for (int i = 0; i < countConsumers; i++) {
            consumers.add(new Consumer<>(table, fixedTimeMillis));
        }

        for (int i = 0; i < producers.size(); i++) {
            new Thread(producers.get(i), "Producer №" + i).start();
        }

        for (int i = 0; i < consumers.size(); i++) {
            new Thread(consumers.get(i), "Consumer №" + i).start();
        }
    }

    private static void testDataWithoutFixedTime() {
        Table<String> table = new Table<>(capacity);
        createAndShowGUI();

        int countProducers = 8;
        int countConsumers = 8;

        List<Producer<String>> producers = new ArrayList<>();
        List<Consumer<String>> consumers = new ArrayList<>();

        for (int i = 0; i < countProducers; i++) {
            producers.add(new Producer<>(table, random.nextInt(500, 900)));
        }

        for (int i = 0; i < countConsumers; i++) {
            consumers.add(new Consumer<>(table, random.nextInt(500, 900)));
        }

        for (int i = 0; i < producers.size(); i++) {
            new Thread(producers.get(i), "Producer №" + i).start();
        }

        for (int i = 0; i < consumers.size(); i++) {
            new Thread(consumers.get(i), "Consumer №" + i).start();
        }
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Producer-Consumer Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 200);

        JPanel panel = new JPanel(new GridLayout(1, ProducerConsumerTest.capacity));
        squares = new JPanel[ProducerConsumerTest.capacity];

        for (int i = 0; i < ProducerConsumerTest.capacity; i++) {
            squares[i] = new JPanel();
            squares[i].setBackground(Color.LIGHT_GRAY);
            squares[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.add(squares[i]);
        }

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void updateGUI(Table table) {
        SwingUtilities.invokeLater(() -> {
            int currentMessages = table.currentSize;

            for (int i = 0; i < squares.length; i++) {
                squares[i].setBackground(i < currentMessages ? Color.GREEN : Color.LIGHT_GRAY);
            }

            frame.repaint();
        });
    }
}
