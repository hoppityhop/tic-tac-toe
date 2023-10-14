package com.tictactoe.service;

import com.tictactoe.model.Game;
import com.tictactoe.model.Player;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.tictactoe.model.GameStatus.NEW;

@Service
@AllArgsConstructor
public class GameService {

   public Game createGame(Player player) {
      Game game = new Game();
      game.setBoard(new int[3][3]);
      game.setGameId(UUID.randomUUID().toString());
      game.setPlayer1(player);
      game.setStatus(NEW);
      return game;
   }

}
