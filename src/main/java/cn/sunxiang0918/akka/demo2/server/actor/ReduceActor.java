package cn.sunxiang0918.akka.demo2.server.actor;

import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import cn.sunxiang0918.akka.demo2.server.model.Result;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/7 10:38
 */
public class ReduceActor extends UntypedActor {

    /*管道Actor*/
    private ActorRef actor = null;

    public ReduceActor(ActorRef inAggregateActor) {
        actor = inAggregateActor;
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("启动ReduceActor:"+Thread.currentThread().getName());
    }
    
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof List) {

            /*强制转换结果*/
            List<Result> work = (List<Result>) message;

            // 第一次汇总单词表结果.
            NavigableMap<String, Integer> reducedList = reduce(work);

            // 把这次汇总的结果发送给最终的结果聚合Actor
            actor.tell(reducedList, null);

        }else if (message instanceof Boolean) {
            //表示已经计算结束了
            // 把这次汇总的结果发送给最终的结果聚合Actor
            actor.tell(message, null);
        } else
            throw new IllegalArgumentException("Unknown message [" + message + "]");
    }

    /**
     * 聚合计算本次结果中各个单词的出现次数
     * @param list
     * @return
     */
    private NavigableMap<String, Integer> reduce(List<Result> list) {

        NavigableMap<String, Integer> reducedMap = new ConcurrentSkipListMap<>();

        for (Result result : list) {
            /*遍历结果,如果在这个小的结果中已经存在相同的单词了,那么数量+1,否则新建*/
            if (reducedMap.containsKey(result.getWord())) {
                Integer value = reducedMap.get(result.getWord());
                value++;
                reducedMap.put(result.getWord(), value);
            } else {
                reducedMap.put(result.getWord(), 1);
            }
        }
        return reducedMap;
    }
}
