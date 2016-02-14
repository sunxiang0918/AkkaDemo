package cn.sunxiang0918.akka.demo5.model;

import java.io.Serializable;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/26 14:31
 */
public class StartCommand implements Serializable {
    
    private int actorCount =0;

    public StartCommand() {
    }

    public StartCommand(int actorCount) {
        this.actorCount = actorCount;
    }

    public int getActorCount() {
        return actorCount;
    }

    public void setActorCount(int actorCount) {
        this.actorCount = actorCount;
    }
}
