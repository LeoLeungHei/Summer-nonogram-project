import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.awt.*;
import java.io.IOException;
import java.awt.BorderLayout;
public class Nonogram extends JFrame
{   
    private JLabel[][] labels; // Label array for 15x15 
    private static final int GRID_SIZE = 15;
    private static final int LABEL_SIZE = 50;

    public Nonogram()
    {
        setTitle("Oh no no no nonogram"); // title
        labels = new JLabel[GRID_SIZE][GRID_SIZE]; // declare array 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Close GUI when X button is pressed
        
        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE + 1, GRID_SIZE + 1, 0, 0));// Grid layout of 15x15 with 0,0 spacing, with +1 blank spaces

        int frameSize = (GRID_SIZE + 1) * LABEL_SIZE; // Set the Size of the grid
        setSize(frameSize, frameSize); // Set the size of the frame
        setResizable(false); // Disable resizing

        
        // Add 16 empty white labels to the top row
        for (int i = 0; i < GRID_SIZE + 1; i++) 
        {
            JLabel emptyLabel = new JLabel();
            emptyLabel.setPreferredSize(new Dimension(LABEL_SIZE, LABEL_SIZE));
            emptyLabel.setBackground(Color.WHITE);
            emptyLabel.setOpaque(true);
            gridPanel.add(emptyLabel);
        }
        for (int i = 0; i < 15; i++) 
        {
            // Add empty white labels to the left column
            JLabel emptyLabel = new JLabel();
            emptyLabel.setPreferredSize(new Dimension(LABEL_SIZE, LABEL_SIZE));
            emptyLabel.setBackground(Color.WHITE);
            emptyLabel.setOpaque(true);
            gridPanel.add(emptyLabel);

            for (int j = 0; j < 15; j++) 
            {
                //Label creation to make a 15x15 grid
                JLabel label = new JLabel();
                label.setPreferredSize(new Dimension(50, 50));// 50 pixel square
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));// Black border
                label.setBackground(Color.white);// White background
                label.setOpaque(true); // Ensure the label is opaque
                label.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e) // Listener for click on labels
                    {
                        JLabel clickedLabel = (JLabel) e.getSource();
                        System.out.printf("a click\n");
                        Color labelColor = label.getBackground();
                        if (labelColor.equals(Color.red)) 
                        {
                            clickedLabel.setBackground(Color.black); // Change color to black when clicked
                            System.out.printf("Set to Black\n");
                        }
                        else if (labelColor.equals(Color.black))
                        {
                            clickedLabel.setBackground(Color.white); // Change color to white when clicked
                            System.out.printf("Set to white\n");
                        } 
                        else 
                        {
                            clickedLabel.setBackground(Color.red); // Change color to red when clicked
                            System.out.printf("Set to Red\n");
                        }
                        clickedLabel.repaint(); // update the label visually
                    }
                });
                labels[i][j] = label;// assigning each array unit a label
                gridPanel.add(label);
            }
        }

        //Actionlistener for Import Nonogram        
        JButton importButton = new JButton("Import Nonogram");
        importButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                System.out.println("Import Button clicked!");
                JFileChooser fileChooser = new JFileChooser();// Initilize File chooser to importbutton
                FileNameExtensionFilter filter = new FileNameExtensionFilter("BMP images", "bmp");
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            // User has chosen a file
            System.out.println("Selected file: " + fileChooser.getSelectedFile().getName());
            File selectedFile = fileChooser.getSelectedFile();

            try {
                // Load the selected BMP file
                BufferedImage image = ImageIO.read(selectedFile);

                // Check if the image is loaded successfully
                if (image != null) {
                    // Get the width and height of the image
                    int width = image.getWidth();
                    int height = image.getHeight();

                    // Create a 2D array to store pixel data
                    int[][] pixelData = new int[height][width];

                    // Loop through each pixel in the image
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            // Get the pixel value at the current position
                            int pixelValue = image.getRGB(x, y);

                            // Store the pixel data in the 2D array
                            pixelData[y][x] = pixelValue;
                        }
                    }
                    for (int i = 0; i < 15; i++)
                    {
                        for (int j = 0; j < 15; j++)
                        {
                            System.out.println(pixelData[i][j]);
                        }
                    }

                    // Now you have the pixel data stored in the 2D array
                    // You can perform further processing or manipulation as needed
                } else {
                    System.out.println("Failed to load image.");
                }
            } catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
            }
        });

        //Actionlistener for Check Answer
        JButton ansButton = new JButton("Check Answer");
        ansButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                System.out.println("Answer Button clicked!");
                // Add answer checking logic here
            }
        });

        JPanel buttonPanel = new JPanel(); // Construct a panel for buttons
        buttonPanel.setBackground(Color.white); 
        buttonPanel.add(importButton); // Add the 2 buttons to the panels
        buttonPanel.add(ansButton);

        add(gridPanel, BorderLayout.CENTER); // Set the grids to the center
        add(buttonPanel, BorderLayout.SOUTH); // Set the buttonpanel to the bottom

        setVisible(true);// make frame visible  
    }  
  
    public static void main(String[] args) 
    {  
        new Nonogram(); // launch the nonogram
    }  
}  