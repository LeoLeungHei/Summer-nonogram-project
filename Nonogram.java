import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.File;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
public class Nonogram extends JFrame
{   
    private JLabel[][] labels; // Label array for 15x15 
    private JLabel[] topLabels;
    private JLabel[] leftLabels;
    private static final int LABEL_SIZE = 50;
    private static final int VERTICAL_WHITE_LABEL_LENGTH = 100;
    private static final int HORIZONTAL_WHITE_LABEL_WIDTH = 100;
    private int height = 15;
    private int width = 15;
    private Color[][] answerPixelColor;
    private Color[][] currentuserColors;

    // Function to convert a byte array to an integer
    private static int byteArrayToInt(byte[] bytes, int start, int end) 
    {
        int value = 0;
        for (int i = start; i < end; i++) 
        {
            value |= (bytes[i] & 0xFF) << (8 * (i - start));
        }
        return value;
    }

    private void updateLabels(Color color, Color[][] answerPixelColor, int height, int width) {
        // Algorithm to produce numbers on the left of the nonogram
        ArrayList<ArrayList<Integer>> y_counterArrayList = new ArrayList<>(); // ArrayList to store counters for each row
        for (int y = 0; y < height; y++) 
        {
            ArrayList<Integer> counterArrayList = new ArrayList<>(); // ArrayList to store counters for a single row
            int counter = 0; // Initialize counter for current row
                    
            for (int i = 0; i < width; i++) 
            {
                if (answerPixelColor[y][i] == color) 
                {
                // Increment counter if the current color is black
                    counter++;
                } 
                else if (answerPixelColor[y][i] != color)
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
            String currentText = label.getText();
            String newText = "";
            if (color == Color.black)
            {
                newText = currentText + "<html><font color='black'>" + Arrays.toString(counts) + "</font><html>";
            }
            else if (color == Color.red)
            {
                newText = currentText + "<html><font color='red'>" + Arrays.toString(counts) + "</font><html>";
            }
            else if (color == Color.green)
            {
                newText = currentText + "<html><font color='green'>" + Arrays.toString(counts) + "</font><html>";
            }
            else if (color == Color.blue)
            {
                newText = currentText + "<html><font color='blue'>" + Arrays.toString(counts) + "</font><html>";
            }
            label.setText(newText);
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
                    
            for (int i = 0; i < height; i++) 
            {
                if (answerPixelColor[i][x] == color) 
                {
                    // Increment counter if the current color is the one being counted
                    counter++;
                } 
                else if (answerPixelColor[i][x] != color)
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
            String currentText = label.getText();
            String newText = "";
            if (color == Color.black)
            {
                newText = currentText + "<html><font color='black'>" + Arrays.toString(counts) + "</font><html>";
            }
            else if (color == Color.red)
            {
                newText = currentText + "<html><font color='red'>" + Arrays.toString(counts) + "</font><html>";
            }
            else if (color == Color.green)
            {
                newText = currentText + "<html><font color='green'>" + Arrays.toString(counts) + "</font><html>";
            }
            else if (color == Color.blue)
            {
                newText = currentText + "<html><font color='blue'>" + Arrays.toString(counts) + "</font><html>";
            }
            label.setText(newText);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.repaint();
        }
    }

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

        // Populate top left with 2 labels to make a square for the empty space
        for (int i = 0; i < 2; i++)
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

        // Construct label grid
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
                        // Change colors
                        if (labelColor.equals(Color.yellow)) 
                        {
                            clickedLabel.setBackground(Color.black);
                        }
                        else if (labelColor.equals(Color.black))
                        {
                            clickedLabel.setBackground(Color.white);
                        }
                        else if (labelColor.equals(Color.white))
                        {
                            clickedLabel.setBackground(Color.black); 
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
                FileInputStream fis = new FileInputStream(selectedFile);

                // Read file header (14 bytes)
                byte[] fileHeader = new byte[14];
                fis.read(fileHeader);
            
                // Read bitmap information header (40 bytes)
                byte[] bitmapInfoHeader = new byte[40];
                fis.read(bitmapInfoHeader);

                // Get where pixel data starts from file header
                int pixelDatapointer = byteArrayToInt(fileHeader, 10, 13);
                System.out.println("Data pointer: " + pixelDatapointer);

                // Get width and height from bitmap information header
                width = byteArrayToInt(bitmapInfoHeader, 4, 8);
                height = byteArrayToInt(bitmapInfoHeader, 8, 12);
                System.out.println("width: " +width);
                System.out.println("height: " + height);

                // Update frame size
                int frameSize_X = width * LABEL_SIZE;
                int frameSize_Y = height * LABEL_SIZE;
                setSize(frameSize_X + HORIZONTAL_WHITE_LABEL_WIDTH, frameSize_Y + VERTICAL_WHITE_LABEL_LENGTH);
                revalidate();
                repaint();

                // Remove existing label grids
                gridPanel.removeAll();
                topPanel.removeAll();
                leftPanel.removeAll();

                // Update label grids
                gridPanel.setLayout(new GridLayout(height, width, 0, 0));
                topPanel.setLayout(new GridLayout(1, width + 3, 0, 0));
                leftPanel.setLayout(new GridLayout(height, 1, 0, 0));
                labels = new JLabel[height][width];
                topLabels = new JLabel[width];
                leftLabels = new JLabel[height];

                // Recreate top panel with updated labels
                for (int i = 0; i < 2; i++) 
                {
                    JLabel topLeftLabel = new JLabel();
                    topLeftLabel.setPreferredSize(new Dimension(VERTICAL_WHITE_LABEL_LENGTH, LABEL_SIZE));
                    topLeftLabel.setBackground(Color.WHITE);
                    topLeftLabel.setOpaque(true);
                    topPanel.add(topLeftLabel);
                }
                for (int i = 0; i < width; i++) 
                {
                    JLabel emptyLabel = new JLabel();
                    emptyLabel.setPreferredSize(new Dimension(LABEL_SIZE, VERTICAL_WHITE_LABEL_LENGTH));
                    emptyLabel.setBackground(Color.WHITE);
                    emptyLabel.setOpaque(true);
                    topPanel.add(emptyLabel);
                    topLabels[i] = emptyLabel;
                }

                // Recreate left panel with updated labels
                for (int i = 0; i < height; i++) 
                {
                    JLabel emptyLabel = new JLabel();
                    emptyLabel.setPreferredSize(new Dimension(HORIZONTAL_WHITE_LABEL_WIDTH, LABEL_SIZE));
                    emptyLabel.setBackground(Color.WHITE);
                    emptyLabel.setOpaque(true);
                    leftPanel.add(emptyLabel);
                    leftLabels[i] = emptyLabel;
                }

                // Recreate label grid
                for (int i = 0; i < height; i++) 
                {
                    for (int j = 0; j < width; j++) 
                    {
                        JLabel label = new JLabel();
                        label.setSize(new Dimension(LABEL_SIZE, LABEL_SIZE));
                        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        label.setBackground(Color.YELLOW);
                        label.setOpaque(true);
                        label.addMouseListener(new MouseAdapter() 
                        {
                            @Override
                            public void mouseClicked(MouseEvent e) 
                            {
                                JLabel clickedLabel = (JLabel) e.getSource();
                                Color labelColor = clickedLabel.getBackground();
                                // Change colors
                                if (labelColor.equals(Color.YELLOW)) 
                                {
                                    clickedLabel.setBackground(Color.BLACK);
                                } 
                                else if (labelColor.equals(Color.BLACK)) 
                                {
                                    clickedLabel.setBackground(Color.WHITE);
                                } 
                                else if (labelColor.equals(Color.WHITE)) 
                                {
                                   clickedLabel.setBackground(Color.BLACK);
                                }
                                clickedLabel.repaint();
                            }
                        });
                        labels[i][j] = label;
                        gridPanel.add(label);
                    }
                }
                // Repaint the panels
                topPanel.revalidate();
                topPanel.repaint();
                leftPanel.revalidate();
                leftPanel.repaint();
                gridPanel.revalidate();
                gridPanel.repaint();



                // Get color depth of pixels
                int bitDepth = byteArrayToInt(bitmapInfoHeader, 14, 15);
                System.out.println("bit depth: " + bitDepth);

                // Jump to pixel data bytes - file header and information header (54 bytes)
                fis.skip(pixelDatapointer - 54);

                currentuserColors = new Color[height][width];
                answerPixelColor = new Color[height][width];
                if (bitDepth == 1) // Read bit depth to determine what algorithm to use
                {
                    // Calculate the size of the pixel data
                    int dataSize = (int)Math.ceil(width / 8.0);
                    if (dataSize < 4)
                    {
                        dataSize = 4; // Pad null bytes
                    }
                    System.out.println("data size: " + dataSize);

                    // Read rows to import image to array

                    for (int i = height - 1; i >= 0; i--) // Loop from height - 1 to 0
                    { 
                        byte[] pixels = new byte[dataSize];
                        fis.read(pixels); // Read pixel data into the array
                        
                        for (int j = 0; j < width; j++) // Loop over each bit in the row
                        { 
                            int byteIndex = j / 8; // Index of the byte containing the bit
    
                            int bitIndex = 7 - (j % 8); // Index of the bit within the byte (7 to 0)
    
                            int mask = 1 << bitIndex; // Mask to extract the bit
    
                            // Check if the bit is 1 or 0 in the current byte
                            boolean isBitSet = (pixels[byteIndex] & mask) != 0;
    
                            // Set the color in the array depending on bit
                            if (isBitSet == true)
                            {
                                answerPixelColor[i][j] = Color.white;
                            }
                            else if (isBitSet == false)
                            {
                                answerPixelColor[i][j] = Color.black;
                            }
                        }
                        
                    }   
                    updateLabels(Color.black, answerPixelColor, height, width); // Function to add nonogram logic to the left and top
                }

                else if (bitDepth == 24) // Read bit depth to determine what algorithm to use
                {
                    // Calculate padding for a 24-bit depth BMP file
                    int padding = (4 - (width * 3) % 4) % 4;
                    System.out.println("padding: " + padding);

                    boolean isRed = false, isBlue = false, isGreen = false, isBlack = false, isWhite = false;
                    for (int i = height - 1; i >= 0; i--) // read each row from the bottom up
                    {
                        byte[] pixels = new byte[width * 3 + padding];
                        fis.read(pixels);

                        
                        for (int j = 0; j < width * 3; j += 3) // read each 3 pixels in BGR format due to little endian
                        {

                            // Store BGR values in variables
                            int blue = pixels[j] & 0xFF;
                            int green = pixels[j + 1] & 0xFF;
                            int red = pixels[j + 2] & 0xFF;

                            if (blue > red && blue > green) // Populate answerpixelcolor with the significant colors 
                            {
                                answerPixelColor[i][j / 3] = Color.blue;
                                isBlue = true;
                            }
                            else if (red > blue && red > green)
                            {
                                answerPixelColor[i][j / 3] = Color.red;
                                isRed = true;
                            }
                            else if (green > blue && green > red)
                            {
                                answerPixelColor[i][j / 3] = Color.green;
                                isGreen = true;
                            }
                            else if (red == blue && red == green && red != 255)
                            {
                                answerPixelColor[i][j / 3] = Color.black;
                                isBlack = true;
                            }
                            else if (red == blue && red == green && red == 255)
                            {
                                answerPixelColor[i][j / 3] = Color.white;
                                isWhite = true;
                            }

                        }
                    }

                    // Create arraylist to store colors that are currently in the nonogram
                    ArrayList<Color> colorList = new ArrayList<>();
                    if (isGreen == true)
                    {
                        colorList.add(Color.green);
                    }
                    if (isBlue == true)
                    {
                        colorList.add(Color.blue);
                    }
                    if (isBlack == true)
                    {
                        colorList.add(Color.black);
                    }
                    if (isWhite == true)
                    {
                        colorList.add(Color.white);
                    }
                    if (isRed == true)
                    {
                        colorList.add(Color.red);
                    }                    

                    // Update the mouselistener to contain the colors that are currently in the color arraylist
                    for (int i = 0; i < width; i++)
                    {
                        for (int j = 0; j < height; j++)
                        {
                            final int[] currentIndex = {0};
                            JLabel label = labels[j][i];
                            label.addMouseListener(new MouseAdapter() 
                            {
                                @Override
                                public void mouseClicked(MouseEvent e) 
                                {
                                    JLabel clickedLabel = (JLabel) e.getSource();
                                    Color nextColor = colorList.get(currentIndex[0]); // Get the next color from the list
                                    clickedLabel.setBackground(nextColor);
                                    currentIndex[0] = (currentIndex[0] + 1) % colorList.size(); // Move to the next color index, cycles back to 0 if > list size
                                    clickedLabel.repaint();
                                }
                            });
                            labels[j][i] = label; // Update the label
                        }
                    }

                    //Function to add nonogram logic to top with corresponding colors
                    if (isBlack == true)
                    {
                        updateLabels(Color.black, answerPixelColor, height, width);
                    }
                    if (isBlue == true)
                    {
                        updateLabels(Color.blue, answerPixelColor, height, width);
                    }
                    if (isRed == true)
                    {
                        updateLabels(Color.red, answerPixelColor, height, width);
                    }
                    if (isGreen == true)
                    {
                        updateLabels(Color.green, answerPixelColor, height, width);
                    }


                }
                else
                {
                    JOptionPane.showMessageDialog(null, "BMP file bit depth not supported");
                }


                fis.close();


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
                                    label.setBackground(Color.RED); //Repaint label to red if label is incorrect]
                                    label.setText("X");
                                    label.setVerticalAlignment(SwingConstants.CENTER);
                                    label.setHorizontalAlignment(SwingConstants.CENTER);
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
                if (showing == false) // Paint grid with imported BMP
                {
                    if (answerPixelColor != null)
                    {
                        // Save user progress
                        for (int y = 0; y < height; y++)
                        {
                            for (int x = 0; x < width; x++)
                            {
                                JLabel label = labels[y][x];
                                currentuserColors[y][x] = label.getBackground();
                            }
                        }
                        
                        // Paint grid with answerPixelColor
                        for (int y = 0; y < height; y++)
                        {
                            for (int x = 0; x < width; x++)
                            {
                                Color pixelColor = answerPixelColor[y][x];
                                JLabel label = labels[y][x];
                                label.setBackground(pixelColor);
                                label.setText("");
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
                            label.setBackground(currentuserColors[y][x]);
                            label.setText("");
                            labels[y][x] = label;
                            label.repaint(); // update the label visually
                        }
                    }
                    showing = false;
                }
            }
        });


        // Button to wipe the screen to white labels
        JButton wipeButton = new JButton("Wipe Grid (White)");
        wipeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                for (int y = 0; y < height; y++)
                {
                    for (int x = 0; x < width; x++)
                    {
                        JLabel label = labels[y][x];
                        label.setText("");
                        label.setBackground(Color.white);
                        labels[y][x] = label;
                        label.repaint(); // update the label visually
                    }
                }
            }
        });


        JPanel buttonPanel = new JPanel(); // Construct a panel for buttons
        buttonPanel.setBackground(Color.white); 
        buttonPanel.add(importButton); // Add the buttons to the panels
        buttonPanel.add(ansButton);
        buttonPanel.add(showButton);
        buttonPanel.add(wipeButton);

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