package cn.sunxiang0918.akka.demo5.model;

import java.io.Serializable;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/26 14:32
 */
public class ExecutionResult implements Serializable {
    
    private long costTime;

    public ExecutionResult() {
    }

    public ExecutionResult(long costTime) {
        this.costTime = costTime;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }
}
