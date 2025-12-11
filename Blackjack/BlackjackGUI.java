package client;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class BlackjackGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private JButton hitButton;
    private JButton standButton;
    private JButton finishButton; //added
    private JButton changeBetButton; // added
    private JButton newHandButton; // added
    private JLabel statusLabel;

    

    private String BASE_URL = "http://euclid.knox.edu:8080/api/blackjack";
    private String USERNAME = "myname123";
    private String PASSWORD = "mypassword123";
    private ClientConnecter clientConnecter;

    private CardPanel cardPanel;
    private Map<Card, ImageIcon> cardImages;
    private Random random = new Random();
    private UUID sessionId;
    private GameState currentState = new GameState(); // prevent null pointer error on launch

    public BlackjackGUI() {
        setTitle("Blackjack Game");
        setSize(1000, 800);


        loadCards(); //load card images for display
        // create and pass the buttons to the card panel
        // it will resize them and add them to the panel

        // initialize buttons
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        finishButton = new JButton("Finish Game"); //added
        changeBetButton = new JButton("Change Bet"); // added
        newHandButton = new JButton("New Hand"); // added



        // create card panel with buttons and images
        cardPanel = new CardPanel(hitButton, standButton, cardImages);
        setContentPane(cardPanel);

        // Initialize connection to the Blackjack server
        clientConnecter = new ClientConnecter(BASE_URL, USERNAME, PASSWORD);

        // now set the action listeners for the hit/stand buttons
        //added: // Setup button actions to call server and update UI
        // TODO: Replace random card logic with actual server logic.
        // The following placeholder code uses random cards and was likely added for testing UI layout:
        /* 
        hitButton.addActionListener(e -> {
            System.out.println("Hit button clicked");
            List<Card> cards = List.of(Card.values());
            cardPanel.addPlayerCard(cards.get(random.nextInt(cards.size())));
            repaint(); 
        });
        standButton.addActionListener(e -> {
            System.out.println("Stand button clicked");
            List<Card> cards = List.of(Card.values());
            cardPanel.addDealerCard(cards.get(random.nextInt(cards.size())));
            repaint(); 
        });

        */

        // Setup button actions to call server and update UI
        // added: Setup button actions to call server and update UI
        hitButton.addActionListener(e -> handleHit());
        standButton.addActionListener(e -> handleStand());

        //added
        finishButton.setBounds(500, 600, 150, 60);
        cardPanel.add(finishButton);
        finishButton.addActionListener(e -> {
            try {
                if (sessionId != null) {
                    clientConnecter.finishGame(sessionId);
                    statusLabel.setText("Game session finished.");
                    hitButton.setEnabled(false);
                    standButton.setEnabled(false);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error finishing game: " + ex.getMessage());
            }
        });

        //added

        changeBetButton.setBounds(670, 600, 150, 60); // added
        cardPanel.add(changeBetButton);
        changeBetButton.addActionListener(e -> {
            if (sessionId != null) {
                String betStr = JOptionPane.showInputDialog(this, "Enter new bet amount:");
                try {
                    int betAmount = Integer.parseInt(betStr);
                    if (betAmount > 0 && betAmount % 10 == 0) {
                        currentState = clientConnecter.placeBet(sessionId, betAmount);
                        updateUIFromState(currentState);
                        statusLabel.setText("Bet placed: " + betAmount + " units");
                    } else {
                        JOptionPane.showMessageDialog(this, "Bet must be a positive number and a multiple of 10.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error placing bet: " + ex.getMessage());
                }
            }
        });

        newHandButton.setBounds(830, 600, 150, 60); // added
        cardPanel.add(newHandButton);
        newHandButton.addActionListener(e -> {
            try {
                if (sessionId != null) {
                    currentState = clientConnecter.newGame(sessionId);
                    updateUIFromState(currentState);
                    statusLabel.setText("New hand started. Place a bet.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error starting new hand: " + ex.getMessage());
            }
        });

        //till here


        statusLabel = new JLabel("Welcome to Blackjack");
        statusLabel.setBounds(350, 20, 300, 20);
        cardPanel.add(statusLabel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);



        

        // client connecter to make API calls on the server
        clientConnecter = new ClientConnecter(BASE_URL, USERNAME, PASSWORD);

        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setLocationRelativeTo(null);
        
        // Create and attach menu bar for starting or reconnecting
        addMenuBar();
        //TODO: keyboard shortcuts
        //TODO: mouse events
    }

    private void addMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        
        menuBar.add(fileMenu);

        // Menu option to reconnect to existing sessions
        addMenuItem(fileMenu, "Reconnect", () -> {
            System.out.println("Load clicked");
            try {
                List<SessionSummary> sessionSummaryList = clientConnecter.listSessions();
                List<String> options = new ArrayList<>(); //added
                for (SessionSummary session : sessionSummaryList) {
                    System.out.println("Session ID: " + session.sessionId + ", Balance: " + session.balance);
                    options.add(session.sessionId.toString() + " - Balance: " + session.balance); //added
                }

                // Show session options in popup, bottom code added
                String selected = (String) JOptionPane.showInputDialog(this, "Choose a session:", "Reconnect",
                        JOptionPane.PLAIN_MESSAGE, null, options.toArray(), options.get(0));
                if (selected != null) {
                    String selectedId = selected.split(" ")[0];
                    sessionId = UUID.fromString(selectedId);
                    currentState = clientConnecter.resumeSession(sessionId);
                    updateUIFromState(currentState); 
                    statusLabel.setText("Reconnected. Balance: " + currentState.balance);
                } //till here
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        // Menu option to start a new game   
        addMenuItem(fileMenu, "New Game", () -> {
            System.out.println("New Game clicked");
            try {
                // commented GameState state = clientConnecter.startGame();
                // commented System.out.println(state);
                // commented sessionId = state.sessionId;

                currentState = clientConnecter.startGame();
                sessionId = currentState.sessionId;
                updateUIFromState(currentState);

                //added: added this to place a bet of 10 immediately
                currentState = clientConnecter.placeBet(sessionId, 10);
                updateUIFromState(currentState);

                // added: label to show bet amount
                statusLabel.setText("Bet placed: 10 units");
                hitButton.setEnabled(true);
                standButton.setEnabled(true);




                // TODO: use the game state to update the UI
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error starting new game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    //added: code below updateUIFromState, This method syncs your screen with the server after each move (bet, hit, or stand). Without it, the GUI wouldn’t know what cards to show or when the game ends.

    private void updateUIFromState(GameState state) {
        System.out.println(state);
        cardPanel.clearCards();
        if (state.playerCards != null) { //added
        for (String cardName : state.playerCards) {
            cardPanel.addPlayerCard(getCard(cardName));
        }}

        if (state.playerCards != null) {
        for (String cardName : state.dealerCards) {
            if (cardName.equals("???")){
                continue;
            }

          
            cardPanel.addDealerCard(getCard(cardName));
        }

    }
        cardPanel.repaint();
    
        if (state.gameOver) {
            JOptionPane.showMessageDialog(this, "Game Over! Outcome: " + state.outcome +
                    "\\nBalance: " + state.balance);
        }
    }
    

    // convert "THREE OF HEARTS" from server to Card.THREE_OF_HEARTS
    // basically converts a string from server like "TWO OF HEARTS" to enum constant
    private Card getCard(String cardName) {
        return Card.valueOf(cardName.toUpperCase().replace(' ', '_'));
    }

    private void addMenuItem(JMenu menu, String name, Runnable action) {
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.addActionListener(e -> action.run());
        menu.add(menuItem);
    }

    private void loadCards() {
        // Load card images and add them to the main panel
        // This is where you would implement the logic to load and display cards
        // Load images for each card once at startup
        cardImages = new HashMap<>();
        for (Card card : Card.values()) {
            ImageIcon cardImage = new ImageIcon(getClass().getResource("/assets/" + card.getFilename()));
            cardImages.put(card, cardImage);
        }
    }

    // added: Handles the "Hit" button click, calls server and updates UI
    private void handleHit() {
        try {
            if (currentState.canHit) {
                currentState = clientConnecter.hit(sessionId);
                updateUIFromState(currentState);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error hitting: " + e.getMessage());
        }
    }

    // added: Handles the "Stand" button click, calls server and updates UI
    private void handleStand() {
        try {
            if (currentState.canStand) {
                currentState = clientConnecter.stand(sessionId);
                updateUIFromState(currentState);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error standing: " + e.getMessage());
        }
    }

    

    public void showListPopup(String title, java.util.List<String> items) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JList<String> list = new JList<>(new DefaultListModel<>());
        DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
        for (String item : items) {
            model.addElement(item);
        }

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // double click to select
                    String selected = list.getSelectedValue();
                    System.out.println("Selected: " + selected);
                    dialog.dispose();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(list);
        dialog.add(scrollPane);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        BlackjackGUI gui = new BlackjackGUI();
        gui.setVisible(true);
    });
    
}
}
