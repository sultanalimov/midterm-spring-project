import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private static final int HEARTS = 0;
    private static final int DIAMONDS = 1;
    private static final int SPADES = 2;
    private static final int CLUBS = 3;

    private static final int JACK = 11;
    private static final int QUEEN = 12;
    private static final int KING = 13;
    private static final int ACE = 14;
    private static final int STARTING_BANKROLL_TEAM_1 = 100;
    private static final int STARTING_BANKROLL_TEAM_2 = 100;

    public static String getPlayerMove(String playerName) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(playerName + ", hit or stand: ");
            String move = scanner.nextLine().toLowerCase();
            if (move.equals("hit") || move.equals("stand")) {
                return move;
            }
            System.out.println("Invalid input. Please enter 'hit' or 'stand'.");
        }
    }

    public static void dealerTurn(Hand dealer, Deck deck) {
        System.out.println("\nDealer's turn...");
        while (dealer.getValue() < 17) {
            Card c = deck.deal();
            dealer.addCard(c);
            System.out.println("Dealer drew: " + c);
            if (dealer.busted()) {
                System.out.println("Dealer busted!");
                return;
            }
        }
        System.out.println("Dealer stands.");
    }

    public static boolean playerTurn(Hand player, Deck deck, String playerName) {
        while (true) {
            String move = getPlayerMove(playerName);
            if (move.equals("hit")) {
                Card c = deck.deal();
                player.addCard(c);
                System.out.println(playerName + "'s hand: " + player);
                if (player.busted()) {
                    System.out.println(playerName + " busted!");
                    return true;
                }
            } else {
                System.out.println(playerName + " stands.");
                return false;
            }
        }
    }

    public static int calculateTeamScore(List<Hand> team) {
        int totalScore = 0;
        for (Hand hand : team) {
            if (!hand.busted()) {
                totalScore += hand.getValue();
            }
        }
        return totalScore;
    }


    public static double findWinner(Hand dealer, List<Hand> team1, List<Hand> team2, int firstTeamBet, int secondTeamBet) {
        int team1Score = calculateTeamScore(team1);
        int team2Score = calculateTeamScore(team2);

        System.out.println("\nDealer's final hand: " + dealer);
        System.out.println("Team 1 score: " + team1Score);
        System.out.println("Team 2 score: " + team2Score);

        if (team1Score > team2Score) {
            System.out.println("Team 1 wins!");
            return firstTeamBet;
        } else if (team2Score > team1Score) {
            System.out.println("Team 2 wins!");
            return -secondTeamBet;
        } else {
            System.out.println("It's a tie!");
            return 0;
        }
    }

    public static double playRound(double teamBankroll1, double teamBankroll2, int playerAmount) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Team 1, enter your bet: ");
        int firstTeamBet = Math.min(scanner.nextInt(), (int) teamBankroll1);
        System.out.println("Team 2, enter your bet: ");
        int secondTeamBet = Math.min(scanner.nextInt(), (int) teamBankroll2);

        Deck deck = new Deck();
        deck.shuffle();

        List<Hand> team1 = new ArrayList<>();
        List<Hand> team2 = new ArrayList<>();
        Hand dealer = new Hand();

        int teamSize = playerAmount / 2;
        for (int i = 0; i < teamSize; i++) {
            team1.add(new Hand());
            team2.add(new Hand());
        }

        dealer.addCard(deck.deal());
        dealer.addCard(deck.deal());


        for (Hand player : team1) {
            player.addCard(deck.deal());
            player.addCard(deck.deal());
        }
        for (Hand player : team2) {
            player.addCard(deck.deal());
            player.addCard(deck.deal());
        }

        System.out.println("\nDealer's hand: ");
        dealer.printDealerHand();

        System.out.println("Team 1's hands:");
        for (int i = 0; i < team1.size(); i++) {
            System.out.println("Player " + (i + 1) + ": " + team1.get(i));
        }

        for (int i = 0; i < team1.size(); i++) {
            System.out.println("It's Team 1 Player " + (i + 1) + "'s turn.");
            playerTurn(team1.get(i), deck, "Team 1 Player " + (i + 1));
            for (int j = 0; j < team1.size(); j++) {
                System.out.println("Player " + (j + 1) + ": " + team1.get(j));
            }
        }

        System.out.println("\nNow, Team 2 will play.");

        System.out.println("\nDealer's hand: ");
        dealer.printDealerHand();

        System.out.println("Team 2's hands:");
        for (int i = 0; i < team2.size(); i++) {
            System.out.println("Player " + (i + 1) + ": " + team2.get(i));
        }

        for (int i = 0; i < team2.size(); i++) {
            System.out.println("It's Team 2 Player " + (i + 1) + "'s turn.");
            playerTurn(team2.get(i), deck, "Team 2 Player " + (i + 1));
            for (int j = 0; j < team2.size(); j++) {
                System.out.println("Player " + (j + 1) + ": " + team2.get(j));
            }
        }


        dealerTurn(dealer, deck);

        double bankrollChange = findWinner(dealer, team1, team2, firstTeamBet, secondTeamBet);

        if (bankrollChange > 0) {
            teamBankroll1 += bankrollChange;
            teamBankroll2 -= bankrollChange;
        } else {
            teamBankroll1 += bankrollChange;
            teamBankroll2 -= bankrollChange;
        }

        System.out.println("New bankrolls:");
        System.out.println("Team 1: " + teamBankroll1);
        System.out.println("Team 2: " + teamBankroll2);

        return bankrollChange;
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int playerAmount;

        while (true) {
            System.out.print("Enter number of players (4 for 2v2 or 6 for 3v3): ");
            playerAmount = scanner.nextInt();
            scanner.nextLine();

            if (playerAmount == 4 || playerAmount == 6) {
                break;
            } else {
                System.out.println("Invalid number of players. Please enter 4 for 2v2 or 6 for 3v3.");
            }
        }


        double bankrollTeam1 = STARTING_BANKROLL_TEAM_1;
        double bankrollTeam2 = STARTING_BANKROLL_TEAM_2;

        while (true) {
            playRound(bankrollTeam1, bankrollTeam2, playerAmount);

            System.out.println("Would you like to play another round? (Y/N): ");
            String playAgain = scanner.nextLine();
            if (playAgain.equalsIgnoreCase("N")) {
                break;
            }
        }

        System.out.println("\nFinal bankrolls:");
        System.out.println("Team 1: " + bankrollTeam1);
        System.out.println("Team 2: " + bankrollTeam2);

        if (bankrollTeam1 > bankrollTeam2) {
            System.out.println("Team 1 wins!");
        } else if (bankrollTeam2 > bankrollTeam1) {
            System.out.println("Team 2 wins!");
        } else {
            System.out.println("It's a tie!");
        }

        System.out.println("Thanks for playing!");
    }
}
