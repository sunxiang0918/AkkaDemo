package cn.sunxiang0918.akka.demo3.actor;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/6 22:35
 */
public class WorkerActor extends UntypedActor {

    private ActorRef jobController;//jobController是接受者

    @Override //接受到generateLoad的发送消息
    public void onReceive(Object message) throws Exception {
        // using scheduler to send the reply after 1000 milliseconds
//        getContext()
//                .system()
//                .scheduler()
//                .scheduleOnce(Duration.create(1000, TimeUnit.MILLISECONDS),jobController,"DONE",getContext()
//                        .system().dispatcher(),ActorRef.noSender());

        //http://172.16.131.36:8081/api/media/task/media/info

//        System.out.println(Thread.currentThread().getName());
        
        DefaultHttpClient httpclient = new DefaultHttpClient();

        // 目标地址  
        HttpPost httppost = new HttpPost(
                "http://172.16.131.36:8081/api/media/task/media/info");

        HttpResponse response = httpclient.execute(httppost);

//        System.out.println(response.getStatusLine().getStatusCode());
        
        httpclient.close();
        
        jobController.tell("DONE",ActorRef.noSender());
        
    }

    public WorkerActor(ActorRef inJobController) {
        jobController = inJobController;
    }
}
