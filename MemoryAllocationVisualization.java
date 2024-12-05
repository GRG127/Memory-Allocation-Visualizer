import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class MemoryAllocationVisualization extends JFrame {
    private ArrayList<Integer> memoryBlocks = new ArrayList<>();
    private JPanel memoryPanel;
    private JTextField requestInput;
    private JComboBox<String> algorithmSelector;
    public MemoryAllocationVisualization() {
        setTitle("Memory Allocation Visualization");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        memoryBlocks.add(50);
        memoryBlocks.add(100);
        memoryBlocks.add(200);
        memoryBlocks.add(300);
        memoryBlocks.add(150);
        memoryPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                visualizeMemory(g);
            }
        };
        memoryPanel.setPreferredSize(new Dimension(500, 400));
        memoryPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLUE, 2),
                "Memory Blocks",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                Color.BLUE
        ));
        add(memoryPanel, BorderLayout.CENTER);
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(new Color(230, 230, 250));
        JLabel requestLabel = new JLabel("Memory Request (MB):");
        requestLabel.setFont(new Font("Arial", Font.BOLD, 12));
        controlPanel.add(requestLabel);
        requestInput = new JTextField(10);
        requestInput.setFont(new Font("Arial", Font.PLAIN, 12));
        controlPanel.add(requestInput);
        algorithmSelector = new JComboBox<>(new String[]{"First-Fit", "Best-Fit", "Worst-Fit"});
        algorithmSelector.setFont(new Font("Arial", Font.PLAIN, 12));
        controlPanel.add(algorithmSelector);
        JButton allocateButton = new JButton("Allocate Memory");
        allocateButton.setFont(new Font("Arial", Font.BOLD, 12));
        allocateButton.setBackground(new Color(72, 209, 204));
        allocateButton.setForeground(Color.WHITE);
        allocateButton.setFocusPainted(false);
        allocateButton.addActionListener(new AllocationListener());
        controlPanel.add(allocateButton);
        add(controlPanel, BorderLayout.SOUTH);
    }
    private void visualizeMemory(Graphics g) {
        int x = 100, y = 30, blockWidth = 400, blockHeight = 50;
        for (int i = 0; i < memoryBlocks.size(); i++) {
            int blockSize = memoryBlocks.get(i);
            g.setColor(blockSize > 0 ? new Color(135, 206, 250) : new Color(50, 205, 50)); // Blue for free, Green for allocated
            g.fillRect(x, y, blockWidth, blockHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, blockWidth, blockHeight);
            String blockLabel = "Block " + (i + 1) + ": " + (blockSize > 0 ? blockSize + " MB Free" : "Allocated");
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString(blockLabel, x + 10, y + 30);
            y += 60;
        }
    }
    private void allocateMemory(int request, String algorithm) {
        int index = -1;
        switch (algorithm) {
            case "First-Fit":
                for (int i = 0; i < memoryBlocks.size(); i++) {
                    if (memoryBlocks.get(i) >= request) {
                        index = i;
                        break;
                    }
                }
                break;
            case "Best-Fit":
                int smallestFit = Integer.MAX_VALUE;
                for (int i = 0; i < memoryBlocks.size(); i++) {
                    if (memoryBlocks.get(i) >= request && memoryBlocks.get(i) < smallestFit) {
                        smallestFit = memoryBlocks.get(i);
                        index = i;
                    }
                }
                break;

            case "Worst-Fit":
                int largestFit = -1;
                for (int i = 0; i < memoryBlocks.size(); i++) {
                    if (memoryBlocks.get(i) >= request && memoryBlocks.get(i) > largestFit) {
                        largestFit = memoryBlocks.get(i);
                        index = i;
                    }
                }
                break;
        }
        if (index != -1) {
            memoryBlocks.set(index, memoryBlocks.get(index) - request);
            JOptionPane.showMessageDialog(this, "Memory allocated in Block " + (index + 1));
        } else {
            JOptionPane.showMessageDialog(this, "Memory request cannot be allocated!");
        }
        memoryPanel.repaint();
    }
    private class AllocationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int request = Integer.parseInt(requestInput.getText());
                String algorithm = (String) algorithmSelector.getSelectedItem();
                allocateMemory(request, algorithm);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(MemoryAllocationVisualization.this, "Invalid input. Please enter a valid number.");
            }
        }
    }
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MemoryAllocationVisualization app = new MemoryAllocationVisualization();
            app.setVisible(true);
        });
    }
}
