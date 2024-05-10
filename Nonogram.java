import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.io.File;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
public class Nonogram extends JFrame
{   
    private JLabel[][] labels; // Label array for 15x15 
    private JLabel[] topLabels;
    private JLabel[] leftLabels;
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
        labels = new JLabel[height][width]; // declare array 
        topLabels = new JLabel[width];
        leftLabels = new JLabel[height];

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Close GUI when X button is pressed
        
        JPanel gridPanel = new JPanel(new GridLayout(height, width, 0, 0));// Grid layout of 15x15 with 0,0 spacing
        JPanel topPanel = new JPanel(new GridLayout(1, width + 3, 0, 0));// Top horizontal panel for nonogram logic, +3 to add a blank square
        JPanel leftPanel = new JPanel(new GridLayout(height, 1, 0, 0));// Left vertical panel for nonogram logic

        // Populate top left with 3 labels to make a square for the empty space
        for (int i = 0; i < 3; i++)
        {
            JLabel topLeftLabel = new JLabel();
            topLeftLabel.setPreferredSize(new Dimension(VERTICAL_WHITE_LABEL_LENGTH, LABEL_SIZE));
            topLeftLabel.setBackground(Color.WHITE); // Set background color
            topLeftLabel.setOpaque(true);
            topPanel.add(topLeftLabel);
        }


        

        int frameSize = (width) * LABEL_SIZE; // Set the Size of the grid
        setSize(frameSize + HORIZONTAL_WHITE_LABEL_WIDTH, frameSize + VERTICAL_WHITE_LABEL_LENGTH); // Set the size of the frame
        setResizable(false); // Disable resizing
        
        
        // Add 15 empty white labels to the top row
        for (int i = 0; i < width; i++) 
        {
            JLabel emptyLabel = new JLabel();
            emptyLabel.setPreferredSize(new Dimension(LABEL_SIZE, VERTICAL_WHITE_LABEL_LENGTH));
            emptyLabel.setBackground(Color.WHITE);
            emptyLabel.setOpaque(true);
            topPanel.add(emptyLabel);
            topLabels[i] = emptyLabel; // Add empty label to the topLabels array
        }

        for (int i = 0; i < height; i++) 
        {
            // Add 15 empty white labels to the left column
            JLabel emptyLabel = new JLabel();
            emptyLabel.setPreferredSize(new Dimension(HORIZONTAL_WHITE_LABEL_WIDTH, LABEL_SIZE));
            emptyLabel.setBackground(Color.WHITE);
            emptyLabel.setOpaque(true);
            leftPanel.add(emptyLabel);
            leftLabels[i] = emptyLabel; // Add empty label to the leftLabels array

            for (int j = 0; j < width; j++) 
            {
                //Label creation to make the 15x15 grid
                JLabel label = new JLabel();
                label.setSize(new Dimension(LABEL_SIZE, LABEL_SIZE));// 50 pixel square
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));// Black border
                label.setBackground(Color.yellow);// White background
                label.setOpaque(true); // Ensure the label is opaque
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JLabel clickedLabel = (JLabel) e.getSource();
                        Color labelColor = label.getBackground();
                        if (labelColor.equals(Color.yellow)) 
                        {
                            clickedLabel.setBackground(Color.white); // Change color to black when clicked
                        }
                        else if (labelColor.equals(Color.white))
                        {
                            clickedLabel.setBackground(Color.black);
                        }
                        else if (labelColor.equals(Color.black))
                        {
                            clickedLabel.setBackground(Color.white); // Change color to red when clicked
                        }
                        clickedLabel.repaint(); // update the label visually
                    }
                });
                labels[i][j] = label; // Assign the label to the corresponding position in the labels array
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
        if (returnValue == JFileChooser.APPROVE_OPTION) 
        {
            // User has chosen a file
            System.out.println("Selected file: " + fileChooser.getSelectedFile().getName());
            File selectedFile = fileChooser.getSelectedFile();
            try 
            {
                // Load the selected BMP file
                BufferedImage image = ImageIO.read(selectedFile);

                // Check if the image is loaded successfully
                if (image != null) 
                {

                    answerPixelColor = new Color[height][width];

                    // Get the width and height of the BMP image
                    width = image.getWidth();
                    height = image.getHeight();

                    // Create a 2D array to store each pixel's sRGB data
                    pixelData = new int[height][width];


                    // Loop through each pixel in the image
                    for (int y = 0; y < height; y++)
                    {
                        for (int x = 0; x < width; x++) 
                        {
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


                    // Algorithm to produce numbers on the left of the nonogram
                    ArrayList<ArrayList<Integer>> y_counterArrayList = new ArrayList<>(); // ArrayList to store counters for each row
                    for (int y = 0; y < height; y++) 
                    {

                        ArrayList<Integer> counterArrayList = new ArrayList<>(); // ArrayList to store counters for a single row
                        int counter = 0; // Initialize counter for current row
                    
                        for (int i = 0; i < height; i++) 
                        {
                            if (answerPixelColor[y][i] == Color.black) 
                            {
                                // Increment counter if the current color is black
                                counter++;
                            } 
                            else if (answerPixelColor[y][i] == Color.white)
                            {
                                // Add the current counter value to the list and reset counter
                                if (counter > 0) 
                                {
                                    counterArrayList.add(counter);
                                }
                                counter = 0; // Start a new counter for the current color
                            }
                        }
                    
                        // Add the last counter value to the list
                        if (counter > 0) 
                        {
                            counterArrayList.add(counter);
                        }
                    
                        // Add the list of counters for the current row to the main list
                        y_counterArrayList.add(counterArrayList);
                        
                    }

                    // Code to repaint side labels to contain nonogram numbers
                    for (int y = 0; y < y_counterArrayList.size(); y++) 
                    {
                        Integer[] counts = y_counterArrayList.get(y).toArray(new Integer[0]); // Get counts for current row
                        JLabel label = leftLabels[y];
                        label.setText(Arrays.toString(counts));
                        label.setHorizontalAlignment(SwingConstants.CENTER);
                        label.setVerticalAlignment(SwingConstants.CENTER);
                        label.repaint();
                    }


                    // Algorithm to produce numbers on the top of the nonogram
                    ArrayList<ArrayList<Integer>> x_counterArrayList = new ArrayList<>(); // ArrayList to store counters for each column
                    for (int x = 0; x < width; x++) 
                    {

                        ArrayList<Integer> counterArrayList = new ArrayList<>(); // ArrayList to store counters for a single column
                        int counter = 0; // Initialize counter
                    
                        for (int i = 0; i < width; i++) 
                        {
                            if (answerPixelColor[i][x] == Color.black) 
                            {
                                // Increment counter if the current color is black
                                counter++;
                            } 
                            else if (answerPixelColor[i][x] == Color.white)
                            {
                                // Add the current counter value to the list and reset counter
                                if (counter > 0) 
                                {
                                    counterArrayList.add(counter);
                                }
                                counter = 0; // Start a new counter for the current color
                            }
                        }
                    
                        // Add the last counter value to the list
                        if (counter > 0) 
                        {
                            counterArrayList.add(counter);
                        }
                    
                        // Add the list of counters for the current column to the main list
                        x_counterArrayList.add(counterArrayList);
                        
                    }

                    // Code to repaint side labels to contain nonogram numbers
                    for (int x = 0; x < x_counterArrayList.size(); x++) 
                    {
                        Integer[] counts = x_counterArrayList.get(x).toArray(new Integer[0]); // Get counts for current column
                        JLabel label = topLabels[x];
                        label.setText("<html>" + Arrays.toString(counts) + "<html>");
                        label.setVerticalAlignment(SwingConstants.BOTTOM);
                        label.setHorizontalAlignment(SwingConstants.CENTER);
                        label.repaint();
                    }

                    /*  Update frame size
                    int frameSize_X = (width + 1) * LABEL_SIZE;
                    int frameSize_Y = (height + 1) * LABEL_SIZE;
                    setSize(frameSize_X, frameSize_Y); // Update the size of the frame
                    revalidate(); // Revalidate the layout to reflect the changes
                    repaint(); // Repaint the frame to update the changes
                    */
                } 
                else 
                {
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
                System.out.println("Check Answer Button clicked!");
                if (answerPixelColor != null)
                    {
                        int[][] checkedAnswerResult;
                        checkedAnswerResult = new int[height][width];
                        int wrong = 0;
                        // Iterate over each pixel in the answerPixelColor array
                        for (int y = 0; y < height; y++) 
                        {
                            for (int x = 0; x < width; x++) 
                            {
                                // Get the color of the answerPixelColor array
                                Color answerColor = answerPixelColor[y][x];
                                // Get the color of the corresponding label
                                Color labelColor = labels[y][x].getBackground();
        
                                // Compare the colors
                                // fill corresponding spot in array to show if spots are correct/wrong
                                
                                if (labelColor.equals(Color.yellow)) 
                                {
                                    JOptionPane.showMessageDialog(null, "There are unknown squares still");
                                    return;
                                }
                                // fill corresponding spot in array to show if spots are correct/wrong
                                else if (!answerColor.equals(labelColor))
                                {

                                    checkedAnswerResult[y][x] = 1;
                                    wrong++;
                                }
                                else
                                {
                                    checkedAnswerResult[y][x] = 0;
                                } 
                            }
                        }
                        for (int y = 0; y < height; y++) 
                        {
                            for (int x = 0; x < width; x++) 
                            {
                                // Check if player's answer is incorrect
                                if (checkedAnswerResult[y][x] == 1)
                                {   
                                    JLabel label = labels[y][x];
                                    label.setBackground(Color.RED); //Repaint label to red if label is incorrect
                                    label.repaint();
                                    label.setOpaque(true);
                                    labels[y][x] = label;
                                }
                            }
                        }
                        if (wrong == 0)
                        {
                            // If all colors match, show a message indicating correct answer
                            JOptionPane.showMessageDialog(null, "Correct answer!");
                        } 
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Image has not been imported!");
                    }
           
            }
        });

        // Button to show/hide the imported BMP
        JButton showButton = new JButton("Show/Hide Answer");
        showButton.addActionListener(new ActionListener()
        {
            boolean showing = false;
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                System.out.println("Show Answer Button clicked!");
                if (showing == false) // Paint grid with imported BMP
                {
                    if (answerPixelColor != null)
                    {
                        for (int y = 0; y < height; y++)
                        {
                            for (int x = 0; x < width; x++)
                            {
                                Color pixelColor = answerPixelColor[y][x];
                                JLabel label = labels[y][x];
                                label.setBackground(pixelColor);
                                label.repaint(); // update the label visually
                                labels[y][x] = label;
                            }
                        }
                        showing = true;
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Image has not been imported!");
                    }
                }
                else // Repaint grid to hide BMP
                {
                    for (int y = 0; y < height; y++)
                    {
                        for (int x = 0; x < width; x++)
                        {
                            JLabel label = labels[y][x];
                            label.setBackground(Color.white);
                            labels[y][x] = label;
                            label.repaint(); // update the label visually
                        }
                    }
                    showing = false;
                }
            }
        });

        JPanel buttonPanel = new JPanel(); // Construct a panel for buttons
        buttonPanel.setBackground(Color.white); 
        buttonPanel.add(importButton); // Add the buttons to the panels
        buttonPanel.add(ansButton);
        buttonPanel.add(showButton);

        add(gridPanel, BorderLayout.CENTER); // Set the grids to the center
        add(topPanel, BorderLayout.NORTH); // Adds topPanel to the top
        add(leftPanel, BorderLayout.WEST);// Adds leftPanel to the left
        add(buttonPanel, BorderLayout.SOUTH); // Adds the buttonpanel to the bottom

        setVisible(true);// make frame visible  
    }  
  
    public static void main(String[] args) 
    {  
        new Nonogram(); // launch the nonogram
    }  
}  