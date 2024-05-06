import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.awt.*;
import java.io.IOException;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
public class Nonogram extends JFrame
{   
    private JLabel[][] labels; // Label array for 15x15 
    private int GRID_SIZE_X = 15;
    private int GRID_SIZE_Y = 15;
    private static final int LABEL_SIZE = 50;
    private static final int VERTICAL_WHITE_LABEL_LENGTH = 150;
    private static final int HORIZONTAL_WHITE_LABEL_WIDTH = 150;
    private int[][] pixelData;
    private int height = 15;
    private int width = 15;
    private Color[][] answerPixelColor;
    public Nonogram()
    {
        setTitle("Oh no no nonogram"); // title
        labels = new JLabel[height + 1][width + 1]; // declare array 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Close GUI when X button is pressed
        
        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE_Y + 1, GRID_SIZE_X + 1, 0, 0));// Grid layout of 15x15 with 0,0 spacing, with +1 blank spaces

        int frameSize = (width) * LABEL_SIZE; // Set the Size of the grid
        setSize(frameSize + HORIZONTAL_WHITE_LABEL_WIDTH, frameSize + VERTICAL_WHITE_LABEL_LENGTH); // Set the size of the frame
        setResizable(false); // Disable resizing

        
        // Add 16 empty white labels to the top row
        for (int i = 0; i < width + 1; i++) 
        {
            JLabel emptyLabel = new JLabel();
            if (i == 0)
            {
                emptyLabel.setSize(new Dimension(HORIZONTAL_WHITE_LABEL_WIDTH, VERTICAL_WHITE_LABEL_LENGTH));
            }
            else
            {
                emptyLabel.setSize(new Dimension(LABEL_SIZE, VERTICAL_WHITE_LABEL_LENGTH));
            }
            emptyLabel.setBackground(Color.WHITE);
            emptyLabel.setOpaque(true);
            gridPanel.add(emptyLabel);
            labels[0][i] = emptyLabel; // Add empty label to the first row in the labels array
        }
        for (int i = 0; i < height; i++) 
        {
            // Add 15 empty white labels to the left column
            JLabel emptyLabel = new JLabel();
            emptyLabel.setSize(new Dimension(VERTICAL_WHITE_LABEL_LENGTH, LABEL_SIZE));
            emptyLabel.setBackground(Color.WHITE);
            emptyLabel.setOpaque(true);
            gridPanel.add(emptyLabel);
            labels[i + 1][0] = emptyLabel; // Add empty label to the left column in the labels array
        
            for (int j = 0; j < width; j++) 
            {
                //Label creation to make a 15x15 grid
                JLabel label = new JLabel();
                label.setSize(new Dimension(LABEL_SIZE, LABEL_SIZE));// 50 pixel square
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));// Black border
                label.setBackground(Color.white);// White background
                label.setOpaque(true); // Ensure the label is opaque
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JLabel clickedLabel = (JLabel) e.getSource();
                        Color labelColor = label.getBackground();
                        if (labelColor.equals(Color.red)) 
                        {
                            clickedLabel.setBackground(Color.black); // Change color to black when clicked
                        } 
                        else if (labelColor.equals(Color.black)) 
                        {
                            clickedLabel.setBackground(Color.white); // Change color to white when clicked
                        } 
                        else 
                        {
                            clickedLabel.setBackground(Color.red); // Change color to red when clicked
                        }
                        clickedLabel.repaint(); // update the label visually
                    }
                });
                labels[i + 1][j + 1] = label; // Assign the label to the corresponding position in the labels array
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
                fileChooser.setCurrentDirectory(new File("C:\\Users\\user\\Downloads\\bmp-files\\summer-project"));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("BMP images", "bmp");
                fileChooser.setFileFilter(filter); // Filter images to BMP
                int returnValue = fileChooser.showOpenDialog(null);// Open JFile Chooser
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            // User has chosen a file
            System.out.println("Selected file: " + fileChooser.getSelectedFile().getName());
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Load the selected BMP file
                BufferedImage image = ImageIO.read(selectedFile);

                // Check if the image is loaded successfully
                if (image != null) {

                    answerPixelColor = new Color[height][width];

                    // Get the width and height of the BMP image
                    width = image.getWidth();
                    height = image.getHeight();

                    // Create a 2D array to store each pixel's sRGB data
                    pixelData = new int[height][width];


                    // Loop through each pixel in the image
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            // Get the pixel value at the current position
                            int pixelValue = image.getRGB(x, y);

                            // Store the pixel data in the 2D array
                            pixelData[y][x] = pixelValue;
                        }
                    }

                    // Extract BMP file's sRGB components and store them in a 2D array
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            int pixel = pixelData[i][j];

                            // Extracting alpha, red, green, and blue components
                            int red = (pixel >> 16) & 0xFF;   // Shift right by 16 bits, add 0xFF to get sRGB values
                            int green = (pixel >> 8) & 0xFF;  // Shift right by 8 bits, add 0xFF to get sRGB values
                            int blue = pixel & 0xFF;          // add 0xFF to get sRGB values
                            if (red > blue && red > green)
                            {
                                answerPixelColor[i][j] = Color.red;
                            }
                            else if (blue > red && blue > green)
                            {
                                answerPixelColor[i][j] = Color.blue;
                            }
                            else if (green > red && green > blue)
                            {
                                answerPixelColor[i][j] = Color.green;
                            }
                            else if (red == 0 && blue == 0 && green == 0)
                            {
                                answerPixelColor[i][j] = Color.black;
                            }
                            else if (red == 255 && blue == 255 && green == 255)
                            {
                                answerPixelColor[i][j] = Color.white;
                            }
                            else
                            {
                                System.out.println("error");
                            }
                            // Pixel data is stored in the 2D array
                        }
                    } 


                    
                    ArrayList<ArrayList<Integer>> y_counterArrayList = new ArrayList<>(); // ArrayList to store counters for each column
                    for (int y = 0; y < height; y++) 
                    {

                        ArrayList<Integer> counterArrayList = new ArrayList<>(); // ArrayList to store counters for a single column
                        int counter = 0; // Initialize counter for current column
                    
                        for (int i = 0; i < height; i++) {
                            if (i > 0 && answerPixelColor[y][i] == answerPixelColor[y][i - 1]) 
                            {
                                // Increment counter if the current color is the same as the previous one
                                counter++;
                            } 
                            else 
                            {
                                // Add the current counter value to the list and reset counter
                                if (counter > 0) {
                                    counterArrayList.add(counter);
                                }
                                counter = 1; // Start a new counter for the current color
                            }
                        }
                    
                        // Add the last counter value to the list
                        if (counter > 0) 
                        {
                            counterArrayList.add(counter);
                        }
                    
                        // Add the list of counters for the current column to the main list
                        y_counterArrayList.add(counterArrayList);
                        
                    }
                    for (int i = 0; i < y_counterArrayList.size(); i++) {
                        System.out.println(y_counterArrayList.get(i));
                    }
                    System.out.println(y_counterArrayList.size());

                    for (int y = 0; y < y_counterArrayList.size(); y++) 
                    {
                        Integer[] counts = y_counterArrayList.get(y).toArray(new Integer[0]); // Get counts for current column
                        System.out.println(counts);
                        JLabel label = labels[y + 1][0];
                        label.setText(Arrays.toString(counts));
                        label.repaint();
                    }

                    /*  Update frame size
                    int frameSize_X = (width + 1) * LABEL_SIZE;
                    int frameSize_Y = (height + 1) * LABEL_SIZE;
                    setSize(frameSize_X, frameSize_Y); // Update the size of the frame
                    revalidate(); // Revalidate the layout to reflect the changes
                    repaint(); // Repaint the frame to update the changes
                    */
                } else {
                    System.out.println("Failed to load image.");
                }
            } 
            catch (IOException e) 
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

        JButton showButton = new JButton("Show/Hide Answer");
        showButton.addActionListener(new ActionListener()
        {
            boolean showing = false;
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                System.out.println("Show Answer Button clicked!");
                if (showing == false)
                {
                    if (answerPixelColor != null)
                    {
                        for (int y = 0; y < height; y++)
                        {
                            for (int x = 0; x < width; x++)
                            {
                                Color pixelColor = answerPixelColor[y][x];
                                JLabel label = labels[y + 1][x + 1];
                                label.setBackground(pixelColor);
                                labels[y + 1][x + 1] = label;
                                label.repaint(); // update the label visually
                            }
                        }
                        showing = true;
                    }
                    else
                    {
                        System.out.println("Image hasn't been imported");
                    }
                }
                else
                {
                    for (int y = 0; y < height; y++)
                    {
                        for (int x = 0; x < width; x++)
                        {
                            JLabel label = labels[y + 1][x + 1];
                            label.setBackground(Color.white);
                            labels[y + 1][x + 1] = label;
                            label.repaint(); // update the label visually
                        }
                    }
                    showing = false;
                }
            }
        });

        JPanel buttonPanel = new JPanel(); // Construct a panel for buttons
        buttonPanel.setBackground(Color.white); 
        buttonPanel.add(importButton); // Add the 3 buttons to the panels
        buttonPanel.add(ansButton);
        buttonPanel.add(showButton);

        add(gridPanel, BorderLayout.CENTER); // Set the grids to the center
        add(buttonPanel, BorderLayout.SOUTH); // Set the buttonpanel to the bottom

        setVisible(true);// make frame visible  
    }  
  
    public static void main(String[] args) 
    {  
        new Nonogram(); // launch the nonogram
    }  
}  