package cn.sunxiang0918.akka.demo9;

import java.util.Random;

import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import akka.dispatch.OnComplete;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import scala.concurrent.Future;

public class FutureTest {

    public static void main(String[] args) throws Exception {

        final ActorSystem system = ActorSystem.create("helloakka");
        
        /*通过Futures的静态方法future创建一个Future类.入参就是一个异步的计算*/
        Future<String> f = Futures.future(() -> {
            Thread.sleep(10);
            if (new Random(System.currentTimeMillis()).nextBoolean()){
                return "Hello"+"World!";
            }else {
                throw new IllegalArgumentException("参数错误");       
            }
        },system.dispatcher());

        f.andThen(new OnComplete<String>() {
            @Override
            public void onComplete(Throwable failure, String success) throws Throwable {
                System.out.println("这里是andThen");
            }
        },system.dispatcher()).andThen(new OnComplete<String>() {
            @Override
            public void onComplete(Throwable failure, String success) throws Throwable {
                System.out.println("这里是andThen2");
            }
        },system.dispatcher());
        
        f.onSuccess(new PrintResult<String>(),system.dispatcher());
        f.onFailure(new FailureResult(),system.dispatcher());
        
        System.out.println("这个地方是外面");
    }

    public final static class PrintResult<T> extends OnSuccess<T> {
        @Override public final void onSuccess(T t) {
            System.out.println(t);
        }
    }
    
    public final static class FailureResult extends OnFailure {
        @Override
        public void onFailure(Throwable failure) throws Throwable {
            System.out.println("进入错误的处理");
            failure.printStackTrace();
        }
    }
}
