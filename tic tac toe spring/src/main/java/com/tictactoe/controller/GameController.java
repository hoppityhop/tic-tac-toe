package com.tictactoe.controller;

import com.tictactoe.controller.dto.ConnectRequest;
import com.tictactoe.exceptions.InvalidGameException;
import com.tictactoe.exceptions.InvalidParamException;
import com.tictactoe.exceptions.NotFoundException;
import com.tictactoe.model.Game;
import com.tictactoe.model.GamePlay;
import com.tictactoe.model.Player;
import com.tictactoe.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.core.AbstractDestinationResolvingMessagingTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

   private final GameService gameService;
   private final SimpMessagingTemplate simpleMessagingTemplate;

   /**
    * This method is used to start a new game.
    * A response entity is a generic class that is used to wrap the response.
    *
    * @param player
    * @return
    */
   @PostMapping("/start")
   public ResponseEntity<Game> start(@RequestBody Player player) {
      log.info("Starting a new game for player {}", player);
      return ResponseEntity.ok(gameService.createGame(player));
   }

   @PostMapping("/connect")
   public ResponseEntity<Game> connect(@RequestBody ConnectRequest connectRequest)
           throws InvalidParamException {

      log.info("Connecting to game {}", connectRequest.getGameId());

      return ResponseEntity.ok(gameService.connectToGame(connectRequest.getPlayer()
              , connectRequest.getGameId()));


   }

   @PostMapping("/connect/random")
   public ResponseEntity<Game> connectRandom(@RequestBody Player player)
           throws InvalidParamException, NotFoundException {

      log.info("Connecting to random game {}", player);

      return ResponseEntity.ok(gameService.connectToRandomGame(player));

   }

   /**
    * Need to send the move information to BOTH players.
    * This will require websockets.
    *
    * @param request - the request body will contain the gameId,
    *                the player making the move, and the move itself.
    * @return Game
    */
   @PostMapping("/gameplay")
   public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request) throws InvalidGameException, NotFoundException {
      log.info("Gameplay {}", request);
      //Use sockets to send the move information to both players
      Game game = gameService.gamePlay(request);
      simpleMessagingTemplate.convertAndSend("/topic/game-progress" + game.getGameId(), game);
      return ResponseEntity.ok(game);
   }

}


