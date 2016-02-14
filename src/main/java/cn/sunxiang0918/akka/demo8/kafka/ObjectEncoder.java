package cn.sunxiang0918.akka.demo8.kafka;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

public class ObjectEncoder implements Encoder<Serializable> {

    public ObjectEncoder(){}
    
    public ObjectEncoder(VerifiableProperties props) {
//        this.encoding = props == null?"UTF8":props.getString("serializer.encoding", "UTF8");
    }
    
    public byte[] toBytes(Serializable object) {
        if (object == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), ex);
        }
        return baos.toByteArray();
    }
}
