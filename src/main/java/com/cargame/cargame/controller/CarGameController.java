package com.cargame.cargame.controller;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cargame.cargame.dto.GameInfo;
import com.cargame.cargame.dto.MoveDirection;
import com.cargame.cargame.dto.Position;
import com.cargame.cargame.service.CarService;

@RestController
public class CarGameController {
    @Autowired
    private CarService carService;

    @GetMapping("/playSoloGame")
    public String carGame(){
        return carService.carGame();
    }
    
    @GetMapping("/startNewGame")
    public ResponseEntity<GameInfo> startGame(){
    	GameInfo gameInfo = carService.startNewGame();
    	return new ResponseEntity<>(gameInfo,HttpStatus.OK);
    }
    
    @PostMapping(value = "/playTurn",consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<GameInfo> playTurn(@RequestBody MoveDirection move){
    	
    	int moveDir = move.dir;
    	if(moveDir < 0 || moveDir > 4) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	
    	GameInfo gameInfo = carService.playMove(moveDir);
    	return new ResponseEntity<>(gameInfo,HttpStatus.OK);
    }
    
    @PostMapping(value="/setLives",consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> setLives(@RequestBody MoveDirection lives){
    	carService.setLives(lives.dir);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
