package com.cargame.cargame.service;

import com.cargame.cargame.dto.CarEntity;
import com.cargame.cargame.dto.MapEntity;
import com.cargame.cargame.dto.Position;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Log4j2
public class CarService {

    private static final String CLIFF_WARN = "You can't go here , there is a CLIFF ==>";
    public String carGame() {
        int lives = 5;
        int steps=0;
        Random random = new Random();


        CarEntity car = new CarEntity();
        Position carStartingPoint = new Position();
        List<Position> cliffs = new ArrayList<>();
        List<Position> stones = new ArrayList<>();

        MapEntity mapEntity = new MapEntity(10, 10, null, null, null);
        generateCliffs(random, cliffs);
        mapEntity.setCliffs(cliffs);

       log.info("POSITION CLIFFS -->" + cliffs);

        generateStones(random, stones,cliffs);
        mapEntity.setStones(stones);
        log.info("POSTIION STONES -->" + stones);


        do {
            carStartingPoint.setX(random.nextInt(10));
            carStartingPoint.setY(random.nextInt(10));
        } while (cliffs.contains(carStartingPoint) || stones.contains(carStartingPoint));

        car.setPosition(carStartingPoint);
        log.info("START POINT -->" + carStartingPoint);
        mapEntity.setCarPosition(carStartingPoint);

       return carMoves(lives, stones, mapEntity,steps);
    }

    private static String carMoves(int lives, List<Position> stones, MapEntity mapEntity,int steps) {
        do {
            boolean correctChoose;
            do {
                correctChoose = chooseDirection( mapEntity);
            } while (!correctChoose);

            if (stones.contains(mapEntity.getCarPosition())) {
                log.info("CRASHED ON " + mapEntity.getCarPosition());
                lives--;
                log.info("Current lives : " + lives);
            }else{
                steps++;
            }
            log.info(steps);
        if(steps==100){
            log.info("VICTORY!");
            return "VICTORY!";
        }
        } while (lives != 0);

        log.info("GAME OVER !");
        return "GAME OVER!";
    }

    private static Boolean chooseDirection( MapEntity mapEntity) {
        int direction = new Random().nextInt(4);
        return seeIfThereAreCliffs(mapEntity, direction);
    }

    private static Boolean seeIfThereAreCliffs(MapEntity mapEntity, int direction) {
        Position newPosition;
        switch (direction) {
            case 0:
                newPosition = forward(mapEntity);
                if (!mapEntity.getCliffs().contains(newPosition)) {
                    mapEntity.setCarPosition(newPosition);
                    log.info("Moving FORWARD--> " + mapEntity.getCarPosition());
                    return true;
                } else {
                    log.info(CLIFF_WARN  + newPosition);
                }
                break;
            case 1:
                newPosition = backward(mapEntity);
                if (!mapEntity.getCliffs().contains(newPosition)) {
                    mapEntity.setCarPosition(newPosition);
                    log.info("Moving BACKWARD--> " + mapEntity.getCarPosition());
                    return true;
                } else {
                    log.info(CLIFF_WARN + newPosition);
                }
                break;
            case 2:
                newPosition = right(mapEntity);
                if (!mapEntity.getCliffs().contains(newPosition)) {
                    mapEntity.setCarPosition(newPosition);
                    log.info("Moving RIGHT --> " + mapEntity.getCarPosition());
                    return true;
                } else {
                    log.info(CLIFF_WARN + newPosition);
                }
                break;
            case 3:
                newPosition = left(mapEntity);
                if (!mapEntity.getCliffs().contains(newPosition)) {
                    mapEntity.setCarPosition(newPosition);
                    log.info("Moving LEFT-->" + mapEntity.getCarPosition());
                    return true;
                } else {
                    log.info(CLIFF_WARN + newPosition);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
        return false;
    }

    private static Position forward(MapEntity mapEntity) {
        Position newPosition = new Position();
        newPosition.setX(mapEntity.getCarPosition().getX());
        if(mapEntity.getCarPosition().getY() + 1 > 9){
            newPosition.setY(0);
        }else{
        newPosition.setY(mapEntity.getCarPosition().getY() + 1);

        }
        return newPosition;
    }

    private static Position backward(MapEntity mapEntity) {
        Position newPosition = new Position();
        newPosition.setX(mapEntity.getCarPosition().getX());
        if(mapEntity.getCarPosition().getY() - 1 <0){
            newPosition.setY(9);
        }else{
            newPosition.setY(mapEntity.getCarPosition().getY() - 1);

        }
        return newPosition;
    }

    private static Position right(MapEntity mapEntity) {
        Position newPosition = new Position();
        if(mapEntity.getCarPosition().getX() + 1 >9){
            newPosition.setX(0);
        }else{
            newPosition.setX(mapEntity.getCarPosition().getX() + 1);
        }
        newPosition.setY(mapEntity.getCarPosition().getY());
        return newPosition;
    }

    private static Position left(MapEntity mapEntity) {
        Position newPosition = new Position();
        if(mapEntity.getCarPosition().getX() -1  <0){
            newPosition.setX(9);
        }else{
            newPosition.setX(mapEntity.getCarPosition().getX() -1);
        }
        newPosition.setY(mapEntity.getCarPosition().getY());
        return newPosition;
    }


    private static void generateCliffs(Random random, List<Position> cliffs) {
        for (int i = 0; i < 3; i++) {
            Position newObstacle = new Position();
            newObstacle.setX(random.nextInt(10));
            newObstacle.setY(random.nextInt(10));

            if (!cliffs.contains(newObstacle)) {
                cliffs.add(newObstacle);
            }else {
                i--;
            }
        }
    }
    private static void generateStones(Random random, List<Position> stones, List<Position>cliffs) {
        for (int i = 0; i < 3; i++) {
            Position newObstacle = new Position();
            newObstacle.setX(random.nextInt(10));
            newObstacle.setY(random.nextInt(10));

            if (!cliffs.contains(newObstacle) && !stones.contains(newObstacle)) {
                stones.add(newObstacle);
            }else {
                i--;
            }
        }
    }
}
