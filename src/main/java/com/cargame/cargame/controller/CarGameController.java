package com.cargame.cargame.controller;

import com.cargame.cargame.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarGameController {
    @Autowired
    private CarService carService;

    @GetMapping("/car")
    public String carGame(){
        return carService.carGame();
    }
}
