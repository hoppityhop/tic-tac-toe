package com.tictactoe.storage;

import java.util.Map;

public class GameStorage {

   private static Map<String, Game> games;
   private static GameStorage instance;

   private GameStorage() {
   }


   public static synchronized GameStorage getInstance() {
      if (instance == null) {
         instance = new GameStorage();
      }
      return instance;
   }


}
