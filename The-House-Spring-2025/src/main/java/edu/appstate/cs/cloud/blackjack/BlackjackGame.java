package src.main.java.edu.appstate.cs.cloud.blackjack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Card {
    String value;
    String suit;

    public Card(String value, String suit) {
        this.value = value;
        this.suit = suit;
    }

    public String toString() {
        return value + " of " + suit;
    }

    public int getNumericValue() {
        switch (value) {
            case "ACE":
                return 11;
            case "KING":
            case "QUEEN":
            case "JACK":
                return 10;
            default:
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    return 0;
                }
        }
    }
}

public class BlackjackGame {
    private static final PersonService personService = new PersonService();

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");
            String playerName = scanner.nextLine().trim();
            if (playerName.isEmpty()) {
                System.err.println("Name cannot be empty. Exiting.");
                return;
            }

            double balance = getPlayerBalance(playerName);
            if (balance == 0) {
                balance = 100.0;
                createPlayer(playerName, balance);
            }
            System.out.println("Current balance: $" + balance);

            double bet = promptForBet(scanner, balance);
            if (bet == 0) {
                System.out.println("Game cancelled.");
                scanner.close();
                return;
            }

            String deckId = getDeckId();
            if (deckId == null) {
                System.err.println("Could not retrieve deck ID. Exiting.");
                return;
            }

            List<Card> cards = drawCards(deckId, 4);
            if (cards.size() < 4) {
                System.err.println("Not enough cards drawn.");
                return;
            }

            List<Card> playerHand = new ArrayList<>();
            List<Card> dealerHand = new ArrayList<>();
            playerHand.add(cards.get(0));
            playerHand.add(cards.get(1));
            dealerHand.add(cards.get(2));
            dealerHand.add(cards.get(3));

            System.out.println("\nBlackjack");
            boolean playerTurn = true;
            boolean playerBust = false;

            while (playerTurn) {
                System.out.println("\nPlayer's Hand: " + playerHand + " (Total: " + calculateHandValue(playerHand) + ")");
                System.out.println("Dealer's Hand: [X of X, " + dealerHand.get(1) + "]");
                if (calculateHandValue(playerHand) == 21 && playerHand.size() == 2) {
                    System.out.println("Blackjack! Player wins!");
                    balance += bet * 1.5;
                    updatePlayerBalance(playerName, balance);
                    System.out.println("New balance: $" + balance);
                    scanner.close();
                    return;
                }

                String prompt = playerHand.size() == 2 ?
                    "Would you like to (H)it, (S)tand, or (D)ouble Down? " :
                    "Would you like to (H)it or (S)tand? ";
                System.out.print(prompt);
                String choice = scanner.nextLine().trim().toUpperCase();

                if (choice.equals("H")) {
                    List<Card> newCard = drawCards(deckId, 1);
                    if (!newCard.isEmpty()) {
                        playerHand.add(newCard.get(0));
                        int playerValue = calculateHandValue(playerHand);
                        if (playerValue > 21) {
                            System.out.println("\nPlayer's Hand: " + playerHand + " (Total: " + playerValue + ")");
                            System.out.println("Player busts! Dealer wins!");
                            balance -= bet;
                            updatePlayerBalance(playerName, balance);
                            System.out.println("New balance: $" + balance);
                            playerBust = true;
                            playerTurn = false;
                        }
                    }
                } else if (choice.equals("S")) {
                    playerTurn = false;
                } else if (choice.equals("D") && playerHand.size() == 2) {
                    if (balance < bet * 2) {
                        System.out.println("Insufficient balance to double down.");
                    } else {
                        bet *= 2;
                        System.out.println("Bet doubled to $" + bet);
                        List<Card> newCard = drawCards(deckId, 1);
                        if (!newCard.isEmpty()) {
                            playerHand.add(newCard.get(0));
                            int playerValue = calculateHandValue(playerHand);
                            System.out.println("\nPlayer's Hand: " + playerHand + " (Total: " + playerValue + ")");
                            if (playerValue > 21) {
                                System.out.println("Player busts! Dealer wins!");
                                balance -= bet;
                                updatePlayerBalance(playerName, balance);
                                System.out.println("New balance: $" + balance);
                                playerBust = true;
                            }
                            playerTurn = false;
                        }
                    }
                }
            }

            if (!playerBust) {
                System.out.println("\nDealer's Hand: " + dealerHand + " (Total: " + calculateHandValue(dealerHand) + ")");
                while (calculateHandValue(dealerHand) < 17) {
                    List<Card> newCard = drawCards(deckId, 1);
                    if (!newCard.isEmpty()) {
                        dealerHand.add(newCard.get(0));
                        System.out.println("Dealer draws: " + newCard.get(0));
                        System.out.println("Dealer's Hand: " + dealerHand + " (Total: " + calculateHandValue(dealerHand) + ")");
                    }
                }

                int playerValue = calculateHandValue(playerHand);
                int dealerValue = calculateHandValue(dealerHand);

                if (dealerValue > 21) {
                    System.out.println("Dealer busts! Player wins!");
                    balance += bet;
                } else if (playerValue > dealerValue) {
                    System.out.println("Player wins! " + playerValue + " vs " + dealerValue);
                    balance += bet;
                } else if (dealerValue > playerValue) {
                    System.out.println("Dealer wins! " + dealerValue + " vs " + playerValue);
                    balance -= bet;
                } else {
                    System.out.println("Push! Both have " + playerValue);
                }
                updatePlayerBalance(playerName, balance);
                System.out.println("New balance: $" + balance);
            }

            scanner.close();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static double promptForBet(Scanner scanner, double balance) {
        while (true) {
            System.out.print("Enter your bet (max $" + balance + "): $");
            try {
                double bet = Double.parseDouble(scanner.nextLine().trim());
                if (bet > 0 && bet <= balance) {
                    return bet;
                } else {
                    System.out.println("Invalid bet. Must be between $0.01 and $" + balance);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void createPlayer(String name, double balance) {
        Person person = new Person.Builder()
                .withName(name)
                .withBalance(balance)
                .build();
        try {
            personService.createPerson(person);
        } catch (Exception e) {
            System.err.println("Error creating player: " + e.getMessage());
        }
    }

    private static double getPlayerBalance(String name) {
        try {
            Person person = personService.getPersonByName(name);
            return person != null ? person.getBalance() : 0;
        } catch (Exception e) {
            System.err.println("Error retrieving balance: " + e.getMessage());
            return 0;
        }
    }

    private static void updatePlayerBalance(String name, double balance) {
        try {
            Person person = new Person.Builder()
                    .withName(name)
                    .withBalance(balance)
                    .build();
            personService.updatePerson(person);
        } catch (Exception e) {
            System.err.println("Error updating balance: " + e.getMessage());
        }
    }

    public static int calculateHandValue(List<Card> hand) {
        int value = 0;
        int aces = 0;

        for (Card card : hand) {
            if (card.value.equals("ACE")) {
                aces++;
            } else {
                value += card.getNumericValue();
            }
        }

        for (int i = 0; i < aces; i++) {
            if (value + 11 <= 21) {
                value += 11;
            } else {
                value += 1;
            }
        }

        return value;
    }

    public static String getDeckId() throws Exception {
        String url = "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1";
        String json = fetch(url);
        json = json.trim().replaceAll("\\s+", "");
        String key = "\"deck_id\":\"";
        int start = json.indexOf(key);
        if (start == -1) {
            System.err.println("deck_id not found in response.");
            return null;
        }
        start += key.length();
        int end = json.indexOf("\"", start);
        if (end == -1) {
            System.err.println("Invalid deck_id format in response.");
            return null;
        }
        return json.substring(start, end);
    }

    public static List<Card> drawCards(String deckId, int count) throws Exception {
        String url = "https://deckofcardsapi.com/api/deck/" + deckId + "/draw/?count=" + count;
        String json = fetch(url);
        json = json.trim().replaceAll("\\s+", "");

        List<Card> cards = new ArrayList<>();
        int index = 0;
        while ((index = json.indexOf("\"value\":\"", index)) != -1) {
            index += 9;
            int endValue = json.indexOf("\"", index);
            if (endValue == -1) break;
            String value = json.substring(index, endValue);

            int suitStart = json.indexOf("\"suit\":\"", endValue);
            if (suitStart == -1) break;
            suitStart += 8;
            int suitEnd = json.indexOf("\"", suitStart);
            if (suitEnd == -1) break;
            String suit = json.substring(suitStart, suitEnd);

            cards.add(new Card(value, suit));
            index = suitEnd;
        }

        return cards;
    }

    public static String fetch(String urlString) throws Exception {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                String errorResponse = readErrorStream(conn);
                throw new RuntimeException("HTTP error: " + responseCode + ", Message: " + errorResponse);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            return response.toString();
        } catch (Exception e) {
            throw new Exception("Failed to fetch URL: " + urlString + ", Error: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static String readErrorStream(HttpURLConnection conn) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
            StringBuilder error = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                error.append(line);
            }
            return error.toString();
        } catch (Exception e) {
            return "Could not read error stream: " + e.getMessage();
        }
    }
}