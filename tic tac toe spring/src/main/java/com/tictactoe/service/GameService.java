package com.tictactoe.service;

import com.tictactoe.exceptions.InvalidGameException;
import com.tictactoe.exceptions.InvalidParamException;
import com.tictactoe.exceptions.NotFoundException;
import com.tictactoe.model.Game;
import com.tictactoe.model.GamePlay;
import com.tictactoe.model.Player;
import com.tictactoe.model.TicTacToe;
import com.tictactoe.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.tictactoe.model.GameStatus.*;

@Service
@AllArgsConstructor
public class GameService {

   public Game createGame(Player player) {
      Game game = new Game();
      game.setBoard(new int[3][3]);
      game.setGameId(UUID.randomUUID().toString());
      game.setPlayer1(player);
      game.setStatus(NEW);

      //Store in GameStorage
      /**
       * The method below is from the GameStorage class.
       * It is used to add a new entry to the map.
       * The key is the gameId and the value is the game object.
       * The gameId is generated using the UUID class.
       * The UUID class is used to generate a random string.
       * The random string is used as the gameId.
       * The gameId is used to identify the game.
       */
      GameStorage.getInstance().setGame(game);

      return game;
   }

   public Game connectToGame(Player player2, String gameId) throws InvalidParamException {
      if (GameStorage.getInstance().getGames().containsKey(gameId)) {
         throw new InvalidParamException("Game with provided ID doesn't exist.");
      }

      Game game = GameStorage.getInstance().getGames().get(gameId);

      if (game.getPlayer2() != null) {
         throw new InvalidParamException("Game is already full.");
      }

      game.setPlayer2(player2);

      game.setStatus(IN_PROGRESS);
      GameStorage.getInstance().setGame(game);
      return game;
   }

   public Game connectToRandomGame(Player player2) throws InvalidParamException, NotFoundException {

      /**
       * 1. Gets all games from the GameStorage instance.
       * 2. Filters the games by status. It returns only the games with status NEW.
       * 3. Finds the first game from the list.
       *
       * What does stream do?
       * Stream is a sequence of elements supporting
       * sequential and parallel aggregate operations.
       * One of the most important characteristics of a stream is that it is a
       * sequence of elements that is available for processing.
       */
      Game game = GameStorage.getInstance().getGames().values().stream()
              .filter(it -> it.getStatus().equals(NEW))
              .findFirst().orElseThrow(() ->
                      new NotFoundException("No open games were found."));

      game.setPlayer2(player2);
      game.setStatus(IN_PROGRESS);
      GameStorage.getInstance().setGame(game);
      return game;

   }

   public Game gamePlay(GamePlay gamePlay) throws NotFoundException, InvalidGameException {

      if (!(GameStorage.getInstance().getGames().containsKey((gamePlay.getGameId())))) {
         throw new NotFoundException("Game with provided ID doesn't exist.");
      }

      Game game = GameStorage.getInstance().getGames().get(gamePlay.getGameId());

      if (game.getStatus().equals(FINISHED)) {
         throw new InvalidGameException("Game is already finished.");
      }

      int[][] board = game.getBoard();
      board[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()]
              = gamePlay.getType().getValue();

      checkWinner(game.getBoard(), TicTacToe.X);
      checkWinner(game.getBoard(), TicTacToe.O);

      GameStorage.getInstance().setGame(game);

      return game;
   }

   private Boolean checkWinner(int[][] board, TicTacToe type) {
      //Turn board from 2-d to 1-d array
      int[] boardArray = new int[9];
      int counterIndex = 0;
      for (int i = 0; i < board.length; i++) {
         for (int j = 0; j < board[i].length; j++) {
            boardArray[counterIndex] = board[i][j];
            counterIndex++;
         }
      }

      //Put win combinations in a 2-dimensional array

      int[][] winCombinations = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6},
              {1, 4, 7}, {2, 5, 8}, {3, 6, 9}, {0, 4, 8}, {2, 4, 6}};

      for (int[] winCombination : winCombinations) {
         int counter = 0;
         for (int i : winCombination) {
            if (boardArray[i] == type.getValue()) {
               counter++;
               if (counter == 3) {
                  return true;
               }
            }
         }
      }
      return false;
   }


}