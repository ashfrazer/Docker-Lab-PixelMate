package edu.uca.swe.Game.Connection;

import edu.uca.swe.GUI.Controllers.Controller;
import edu.uca.swe.GUI.Panels.GamePanel;
import edu.uca.swe.GUI.Panels.HostPanel;
import edu.uca.swe.GUI.Panels.JoinPanel;
import edu.uca.swe.Game.Board;  // Import the Board class

import ocsf.client.AbstractClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Client extends AbstractClient {

    private String username;
    private HostPanel hostPanel;
    private JoinPanel joinPanel;
    private GamePanel gamePanel;
    private JPanel container;
    private CardLayout cardLayout;
    private Controller controller;
    private Board board;

    public Client(String host, int port, String username, JPanel container, CardLayout cardLayout) {
        super(host, port);
        this.username = username;
        this.container = container;
        this.cardLayout = cardLayout;
        this.board = new Board(null);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public void handleMessageFromServer(Object msg) {
        System.out.println("Message from server: " + msg.toString());

        String message = msg.toString();

        if (message.contains("white")) {
            System.out.println("Client is white!");
        } else if (message.contains("black")) {
            System.out.println("Client is black!");
        } else if (message.startsWith("USERNAMES:")) {
            String[] usernames = message.substring("USERNAMES:".length()).split(",");
            String host = usernames.length > 0 ? usernames[0] : "Unknown";
            String clientUser = usernames.length > 1 ? usernames[1] : "Unknown";

            // Update labels on hostPanel and joinPanel to display opponent username
            SwingUtilities.invokeLater(() -> {
                if (hostPanel != null) {
                    hostPanel.updateWaitingLabel(clientUser);
                }
                if (joinPanel != null) {
                    joinPanel.updateWaitingLabel(host);
                }
            });
        } else if (message.equals("quit")) {
            // Handle quit message by going to main menu
            SwingUtilities.invokeLater(() -> {
                if (controller != null) {
                    cardLayout.show(container, "mainmenu");
                }
            });
        } else if (message.equals("start")) {
            SwingUtilities.invokeLater(() -> {
                // Create a new board
                board = new Board(controller);
                if (gamePanel == null) {
                    gamePanel = new GamePanel(board, "client", this);
                    this.setGamePanel(gamePanel);
                    container.add(gamePanel, "game");
                } else {
                    // Update existing game panel with new board
                    gamePanel = new GamePanel(board, gamePanel.getPlayerRole(), this);
                    this.setGamePanel(gamePanel);
                    // Remove old game panel
                    container.remove(7);
                    // Add new game panel
                    container.add(gamePanel, "game");
                }
                cardLayout.show(container, "game");
            });
        } else if (message.startsWith("Game Over!")) {
            // Handle game over message
            String[] parts = message.split("Winner: ");
            String winner = parts[1]; // The color of the winner
            SwingUtilities.invokeLater(() -> {
                if (controller != null) {
                    controller.showGameOver(winner);
                }
            });
        } else if (message.contains("[")) {
            String role = gamePanel != null ? gamePanel.getPlayerRole() : "";
            if (!message.startsWith(role + " moved")) {
                SwingUtilities.invokeLater(() -> {
                    if (gamePanel != null) {
                        try {
                            gamePanel.getGameController().handleOther(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    public void setHostPanel(HostPanel hostPanel) {
        this.hostPanel = hostPanel;
    }

    public void setJoinPanel(JoinPanel joinPanel) {
        this.joinPanel = joinPanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void sendMove(Object move) {
        try {
            sendToServer(move);
        } catch (Exception e) {
            System.err.println("Error sending move: " + e.getMessage());
        }
    }
}