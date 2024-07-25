import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// key listenner used to listen to key presses as when typing
public class MorseCodeTranslatorGUI  extends JFrame implements KeyListener {
    private MorseCodeController morseCodeController;
    // textInputArea - user input, morseCodeArea - translated text to morse code
    private JTextArea textInputArea, morseCodeArea;

    public MorseCodeTranslatorGUI() {
        // add text to the title bar
        super("Morse Code Translator");

        // size of the frame
        setSize(new Dimension(540, 760));

        // prevents GUI from being able to be resized
        setResizable(false);

        // allows us to manually position and set the size of the component in our GUI
        setLayout(null);

        // exits program when closing the GUI
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // color of the background
        getContentPane().setBackground(Color.decode("#264653"));

        // places the GUI in the center of the screen
        setLocationRelativeTo(null);

        morseCodeController = new MorseCodeController();
        addGuiComponents();
    }

    private void addGuiComponents() {
        // title label
        JLabel titleLabel = new JLabel("Morse Code Translator");

        // font size and weight
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 32));

        // font color of the text
        titleLabel.setForeground(Color.WHITE);

        // center text relative to its container's width
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // set x,y position and width, height dimensions to make the title align to the center of the GUI
        titleLabel.setBounds(0, 0, 540, 100);

        // text input
        JLabel textInputLabel = new JLabel("Text:");
        textInputLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        textInputLabel.setForeground(Color.WHITE);
        textInputLabel.setBounds(20, 100, 200, 30);

        textInputArea = new JTextArea();
        textInputArea.setFont(new Font("Dialog", Font.PLAIN, 18));

        // makes it so that we are listening to key presses when typing
        textInputArea.addKeyListener(this);

        // simulates padding of 10px in the text area
        textInputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // make it so that words wrap to the next line after reaching the end of the text area
        textInputArea.setLineWrap(true);

        // make it so that words do get wrap, the word doesn't split off
        textInputArea.setWrapStyleWord(true);

        // adds scrolling ability to the input text area
        JScrollPane textInputScroll = new JScrollPane(textInputArea);
        textInputScroll.setBounds(20, 132, 484, 236);

        // morse code input
        JLabel morseCodeInputLabel = new JLabel("Morse Code:");
        morseCodeInputLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        morseCodeInputLabel.setForeground(Color.WHITE);
        morseCodeInputLabel.setBounds(20, 390, 200, 30);

        morseCodeArea = new JTextArea();
        morseCodeArea.setFont(new Font("Dialog", Font.PLAIN, 18));
        morseCodeArea.setEditable(false);
        morseCodeArea.setLineWrap(true);
        morseCodeArea.setWrapStyleWord(true);
        morseCodeArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // adds scrolling ability to the morse code text area
        JScrollPane morseCodeScroll = new JScrollPane(morseCodeArea);
        morseCodeScroll.setBounds(20, 430, 484, 236);

        // play sound button
        JButton playSoundButton = new JButton("Play Sound");
        playSoundButton.setBounds(210, 680, 100, 30);
        playSoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // disable the play button as it prevents the sound from getting interrupted
                playSoundButton.setEnabled(false);

                Thread playMorseCodeThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // attempt to play the morse code sound
                        try{
                            String[] morseCodeMessage = morseCodeArea.getText().split(" ");
                            morseCodeController.playSound(morseCodeMessage);
                        }catch(LineUnavailableException lineUnavailableException){
                            lineUnavailableException.printStackTrace();
                        }catch(InterruptedException interruptedException){
                            interruptedException.printStackTrace();
                        }finally{
                            // enable play sound button
                            playSoundButton.setEnabled(true);
                        }
                    }
                });
                playMorseCodeThread.start();
            }
        });

        // add to GUI
        add(titleLabel);
        add(textInputLabel);
        add(textInputScroll);
        add(morseCodeInputLabel);
        add(morseCodeScroll);
        add(playSoundButton);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // ignore shift key press
        if(e.getKeyCode() != KeyEvent.VK_SHIFT) {
            // retrieve text input
            String inputText = textInputArea.getText();

            // update the GUI with the translated text
            morseCodeArea.setText(morseCodeController.translateToMorse(inputText));
        }
    }
}
