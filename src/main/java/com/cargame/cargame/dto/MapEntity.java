package com.cargame.cargame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MapEntity {
    private int row;
    private int col;
    private List<Position> cliffs;
    private List<Position> stones;
    private Position carPosition;


}
