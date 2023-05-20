package ru.myitschool.satspaceshooter;

public class Shot extends Object {
    public Shot(float x, float y) {
        super(x, y, 100, 100);
        vy = 25;
    }

    boolean overlap(Enemy enemy){
        return Math.abs(x-enemy.x) < width/2 + enemy.width/2 & Math.abs(y-enemy.y) < height/2 + enemy.height/2;
    }
    boolean overlap(Horse horse){
        return Math.abs(x-horse.x) < width/2 + horse.width/2 & Math.abs(y-horse.y) < height/2 + horse.height/2;
    }
    boolean overlap(EnemyFat fat){
        return Math.abs(x-fat.x) < width/2 + fat.width/2 & Math.abs(y-fat.y) < height/2 + fat.height/2;
    }
}
