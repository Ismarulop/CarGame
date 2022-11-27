package com.cargame.cargame.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.cargame.cargame.dto.CarEntity;
import com.cargame.cargame.dto.GameInfo;
import com.cargame.cargame.dto.MapEntity;
import com.cargame.cargame.dto.Position;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CarService {

	private static final String CLIFF_WARN = "You can't go here , there is a CLIFF ==>";

	public String carGame() {
		int lives = 5;
		int steps = 0;
		Random random = new Random();

		CarEntity car = new CarEntity();
		Position carStartingPoint = new Position();
		List<Position> cliffs = new ArrayList<>();
		List<Position> stones = new ArrayList<>();

		MapEntity mapEntity = new MapEntity(10, 10, null, null, null);
		generateCliffs(random, cliffs);
		mapEntity.setCliffs(cliffs);

		log.info("POSITION CLIFFS -->" + cliffs);

		generateStones(random, stones, cliffs);
		mapEntity.setStones(stones);
		log.info("POSTIION STONES -->" + stones);

		do {
			carStartingPoint.setX(random.nextInt(10));
			carStartingPoint.setY(random.nextInt(10));
		} while (cliffs.contains(carStartingPoint) || stones.contains(carStartingPoint));

		car.setPosition(carStartingPoint);
		log.info("START POINT -->" + carStartingPoint);
		mapEntity.setCarPosition(carStartingPoint);

		return carMoves(lives, stones, mapEntity, steps);
	}

	private static String carMoves(int lives, List<Position> stones, MapEntity mapEntity, int steps) {
		do {
			boolean correctChoose;
			do {
				correctChoose = chooseDirection(mapEntity);
			} while (!correctChoose);

			if (stones.contains(mapEntity.getCarPosition())) {
				log.info("CRASHED ON " + mapEntity.getCarPosition());
				lives--;
				log.info("Current lives : " + lives);
			} else {
				steps++;
			}
			log.info(steps);
			if (steps == 100) {
				log.info("VICTORY!");
				return "VICTORY!";
			}
		} while (lives != 0);

		log.info("GAME OVER !");
		return "GAME OVER!";
	}

	private static Boolean chooseDirection(MapEntity mapEntity) {
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
				log.info(CLIFF_WARN + newPosition);
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
		if (mapEntity.getCarPosition().getY() + 1 > 9) {
			newPosition.setY(0);
		} else {
			newPosition.setY(mapEntity.getCarPosition().getY() + 1);

		}
		return newPosition;
	}

	private static Position backward(MapEntity mapEntity) {
		Position newPosition = new Position();
		newPosition.setX(mapEntity.getCarPosition().getX());
		if (mapEntity.getCarPosition().getY() - 1 < 0) {
			newPosition.setY(9);
		} else {
			newPosition.setY(mapEntity.getCarPosition().getY() - 1);

		}
		return newPosition;
	}

	private static Position right(MapEntity mapEntity) {
		Position newPosition = new Position();
		if (mapEntity.getCarPosition().getX() + 1 > 9) {
			newPosition.setX(0);
		} else {
			newPosition.setX(mapEntity.getCarPosition().getX() + 1);
		}
		newPosition.setY(mapEntity.getCarPosition().getY());
		return newPosition;
	}

	private static Position left(MapEntity mapEntity) {
		Position newPosition = new Position();
		if (mapEntity.getCarPosition().getX() - 1 < 0) {
			newPosition.setX(9);
		} else {
			newPosition.setX(mapEntity.getCarPosition().getX() - 1);
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
			} else {
				i--;
			}
		}
	}

	private static void generateStones(Random random, List<Position> stones, List<Position> cliffs) {
		for (int i = 0; i < 3; i++) {
			Position newObstacle = new Position();
			newObstacle.setX(random.nextInt(10));
			newObstacle.setY(random.nextInt(10));

			if (!cliffs.contains(newObstacle) && !stones.contains(newObstacle)) {
				stones.add(newObstacle);
			} else {
				i--;
			}
		}
	}

	private GameInfo gameInfo;
	Random random = new Random();

	public GameInfo startNewGame() {
		GameInfo info = new GameInfo();


		CarEntity car = new CarEntity();
		Position carStartingPoint = new Position();
		List<Position> cliffs = new ArrayList<>();
		List<Position> stones = new ArrayList<>();

		MapEntity mapEntity = new MapEntity(10, 10, null, null, null);
		generateCliffs(random, cliffs);
		mapEntity.setCliffs(cliffs);
		generateStones(random, stones, cliffs);
		mapEntity.setStones(stones);
		do {
			carStartingPoint.setX(random.nextInt(10));
			carStartingPoint.setY(random.nextInt(10));
		} while (cliffs.contains(carStartingPoint) || stones.contains(carStartingPoint));

		car.setPosition(carStartingPoint);
		log.info("START POINT -->" + carStartingPoint);
		mapEntity.setCarPosition(carStartingPoint);

		info.map = mapEntity;
		info.lives = 5;
		info.turnsPlayed = 0;
		info.gameState = 0;
		
		this.gameInfo = info;
		return info;
	}

	private Position playTurn(MapEntity mapEntity, int direction) {
		Position newPosition;
		switch (direction) {
		case 0:
			newPosition = forward(mapEntity);
			mapEntity.setCarPosition(newPosition);
			log.info("Moving FORWARD--> " + mapEntity.getCarPosition());
			return newPosition;

		case 1:
			newPosition = backward(mapEntity);

			mapEntity.setCarPosition(newPosition);
			log.info("Moving BACKWARD--> " + mapEntity.getCarPosition());
			return newPosition;

		case 2:
			newPosition = right(mapEntity);
			mapEntity.setCarPosition(newPosition);
			log.info("Moving RIGHT --> " + mapEntity.getCarPosition());
			return newPosition;

		case 3:
			newPosition = left(mapEntity);
			mapEntity.setCarPosition(newPosition);
			log.info("Moving LEFT-->" + mapEntity.getCarPosition());
			return newPosition;

		default:
			throw new IllegalStateException("Unexpected value: " + direction);
		}
	}

	public GameInfo playMove(int moveDir) {

		if(this.gameInfo == null) {
			return null;
		}
		
		this.gameInfo.turnMessage = "";
		MapEntity map = this.gameInfo.map;
		List<Position> cliffs = map.getCliffs();
		List<Position> stones = map.getStones();
		int lives = this.gameInfo.lives;
		int turns = this.gameInfo.turnsPlayed;
		
		if(this.gameInfo.gameState == 1) {
			this.gameInfo.turnMessage += "GAME IS OVER. \n Start a new game.";
			return this.gameInfo;
		}
		
		playTurn(map, moveDir);
		

		if (stones.contains(map.getCarPosition())) {
			log.info("CRASHED ON " + map.getCarPosition());
			this.gameInfo.turnMessage += "CRASHED ON " + map.getCarPosition() + "\n";
			lives--;
			log.info("Current lives : " + lives);
			this.gameInfo.turnMessage += "Current lives : " + lives + "\n";
		} else if(cliffs.contains(map.getCarPosition())){
			this.gameInfo.turnMessage += "FELL FROM CLIFF ON " + map.getCarPosition() + "\n";
			log.info("FELL FROM CLIFF ON" + map.getCarPosition());
			this.gameInfo.turnMessage += "FELL FROM CLIFF ON " + map.getCarPosition() + "\n";
			lives--;
			log.info("Current lives : " + lives);
			this.gameInfo.turnMessage += "Current lives : " + lives + "\n";
		}
		turns++;
		log.info(turns);

		if (lives == 0) {
			this.gameInfo.turnMessage += "GAME OVER !";
			log.info("GAME OVER !");
		} else if (turns == 100) {
			log.info("VICTORY!");
			this.gameInfo.turnMessage += "VICTORY!\n";
			this.gameInfo.gameState = 1;
		}

		this.gameInfo.lives = lives;
		this.gameInfo.turnsPlayed = turns;

		return this.gameInfo;
	}

}
