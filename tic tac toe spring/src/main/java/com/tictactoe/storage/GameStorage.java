package com.tictactoe.storage;

import com.tictactoe.model.Game;

import java.util.HashMap;
import java.util.Map;

public class GameStorage {

   private static Map<String, Game> games;
   private static GameStorage instance;

   private GameStorage() {
      games = new HashMap<>();
   }


   public static synchronized GameStorage getInstance() {
      if (instance == null) {
         instance = new GameStorage();
      }
      return instance;
   }

   public Map<String, Game> getGames() {
      return games;
   }

   public void setGame(Game game) {
      // the put method below is from the Map interface
      // and it is used to add a new entry to the map
      games.put(game.getGameId(), game);
   }



}
