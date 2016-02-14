package cn.sunxiang0918.akka.demo8.kafka;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import kafka.serializer.Decoder;
import kafka.utils.VerifiableProperties;

public class ObjectDecoder implements Decoder<Serializable> {

    public ObjectDecoder(){}
    
    public ObjectDecoder(VerifiableProperties props) {
//        this.encoding = props == null?"UTF8":props.getString("serializer.encoding", "UTF8");
    }
    
    public Serializable fromBytes(byte[] bytes) {

//        return (Serializable) SerializationUtils.deserialize(bytes);

        if (bytes == null) {
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return (Serializable) ois.readObject();
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Failed to deserialize object", ex);
        }
        catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to deserialize object type", ex);
        }
    }
}
