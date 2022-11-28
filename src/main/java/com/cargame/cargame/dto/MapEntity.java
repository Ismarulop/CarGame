package com.cargame.cargame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class MapEntity {
	public int row;
    public int col;
    public List<Position> cliffs;
    public List<Position> stones;
    public Position carPosition;


    public MapEntity() {
    	
    }
}
