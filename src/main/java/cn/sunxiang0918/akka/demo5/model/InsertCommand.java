package cn.sunxiang0918.akka.demo5.model;

import java.io.Serializable;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/26 14:34
 */
public class InsertCommand implements Serializable {
    
    private int recordCount;

    public InsertCommand() {
    }

    public InsertCommand(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }
}
