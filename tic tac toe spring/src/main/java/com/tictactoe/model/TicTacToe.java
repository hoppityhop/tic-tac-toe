package com.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TicTacToe {
   /**
    * If the player puts X, the array index will be 1.
    * If the player puts O, the array index will be 2.
    */
   X(1),O(2);

   private Integer value;
}
