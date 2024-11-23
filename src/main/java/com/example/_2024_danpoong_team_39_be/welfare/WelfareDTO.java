package com.example._2024_danpoong_team_39_be.welfare;

import lombok.Getter;

public class WelfareDTO {

    @Getter
    public static class WelfareRequestDTO{
        private String callTp;
        private String srchKeyCode;
        private int lifeArray;
        private int charTrgterArray;
        private int obstKiArray;
        private int obstArr;
        private int trgterIndvdlArray;
        private int desireArray;
        private int servSeCode;
    }

}