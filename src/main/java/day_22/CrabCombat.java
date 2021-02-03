package day_22;

import util.InputReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CrabCombat {

    private static final String INPUT_FILE = "input_22.txt";

    public static void main(String[] args) {
        new CrabCombat().playCombat();
    }

    private void playCombat() {
        List<List<Long>> cardDecks = InputReader.readInputFileLineGroups(INPUT_FILE).stream()
                .map(group -> group.stream().skip(1).map(Long::parseLong).collect(Collectors.toList()))
                .collect(Collectors.toList());

        List<Long> player1 = cardDecks.get(0);
        List<Long> player2 = cardDecks.get(1);

        List<Long> playerDeck1 = new ArrayList<>(player1);
        List<Long> playerDeck2 = new ArrayList<>(player2);

        playGame(playerDeck1, playerDeck2);
        playRecursiveGame(player1, player2);

        long winningScore = getWinningScore(playerDeck1, playerDeck2);
        long winningScoreRecursive = getWinningScore(player1, player2);

        System.out.println("Winning score:" + winningScore);
        System.out.println("Winning score for recursive game:" + winningScoreRecursive);
    }

    /**
     * The game consists of a series of rounds: both players draw their top card, and the player with the higher-valued card wins the round.
     * The winner keeps both cards, placing them on the bottom of their own deck so that the winner's card is above the other card.
     * If this causes a player to have all of the cards, they win, and the game ends.
     *
     * @param player1 starting deck for player 1
     * @param player2 starting deck for player 2
     */
    private void playGame(List<Long> player1, List<Long> player2) {
        while (player1.size() > 0 && player2.size() > 0) {
            long draw1 = player1.remove(0);
            long draw2 = player2.remove(0);
            if (draw1 > draw2) {
                player1.addAll(Arrays.asList(draw1, draw2));
            } else {
                player2.addAll(Arrays.asList(draw2, draw1));
            }
        }
    }

    /**
     * The game consists of a series of rounds with a few changes:
     * - Before either player deals a card, if there was a previous round in this game that had exactly the same cards in
     * the same order in the same players' decks, the game instantly ends in a win for player 1.
     * - Otherwise, this round's cards must be in a new configuration; the players begin the round by each drawing the top card of their deck as normal.
     * - If both players have at least as many cards remaining in their deck as the value of the card they just drew,
     * the winner of the round is determined by playing a new game of Recursive Combat (see below).
     * Otherwise, at least one player must not have enough cards left in their deck to recurse;
     * the winner of the round is the player with the higher-value card.
     *
     * @param player1 starting deck for player 1
     * @param player2 starting deck for player 2
     */
    private void playRecursiveGame(List<Long> player1, List<Long> player2) {
        List<GameSnapshot> previousStates = new ArrayList<>();

        while (player1.size() > 0 && player2.size() > 0) {
            // check if deck configuration already appeared in this game to prevent infinite recursion
            GameSnapshot thisRoundState = new GameSnapshot(player1, player2);
            if (previousStates.contains(thisRoundState)) {
                player2.clear();
                return;
            }
            previousStates.add(thisRoundState);

            long draw1 = player1.remove(0);
            long draw2 = player2.remove(0);
            if (player1.size() >= draw1 && player2.size() >= draw2) {
                List<Long> subDeck1 = new ArrayList<>(player1.subList(0, (int) draw1));
                List<Long> subDeck2 = new ArrayList<>(player2.subList(0, (int) draw2));
                playRecursiveGame(subDeck1, subDeck2);
                if (subDeck2.isEmpty()) {
                    player1.addAll(Arrays.asList(draw1, draw2));
                } else {
                    player2.addAll(Arrays.asList(draw2, draw1));
                }
            } else {
                if (draw1 > draw2) {
                    player1.addAll(Arrays.asList(draw1, draw2));
                } else {
                    player2.addAll(Arrays.asList(draw2, draw1));
                }
            }
        }
    }

    /**
     * Calculate score of a winning card deck. The bottom card in the deck is worth the value of the card multiplied by 1,
     * the second-from-the-bottom card is worth the value of the card multiplied by 2, and so on.
     *
     * @param playerDeck1
     * @param playerDeck2
     * @return total score calculated as sum of all card values multiplied by their worth
     */
    private long getWinningScore(List<Long> playerDeck1, List<Long> playerDeck2) {
        List<Long> winningDeck = playerDeck1.isEmpty() ? playerDeck2 : playerDeck1;
        return winningDeck.stream()
                .mapToLong(card -> card * (winningDeck.size() - winningDeck.indexOf(card)))
                .sum();
    }
}
