package com.tictactoe.controller.dto;

import com.tictactoe.model.Player;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ConnectRequest {

   private Player player;
   private String gameId;
}
