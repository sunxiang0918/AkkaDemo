package cn.sunxiang0918.akka.demo8;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public abstract class ClusterRoledWorker extends UntypedActor{

    /*记录日志*/
    protected LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    /*集群系统*/
    protected Cluster cluster = Cluster.get(getContext().system());

    // 用来缓存下游注册过来的子系统ActorRef
    protected List<ActorRef> workers = new ArrayList<>();

    @Override
    public void preStart() throws Exception {
        // 订阅集群事件
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberUp.class,ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class);
    }

    @Override
    public void postStop() throws Exception {
        // 取消事件监听
        cluster.unsubscribe(getSelf());
    }

    /**
     * 下游子系统节点发送注册消息
     */

    protected void register(Member member,String actorPath) {
        ActorSelection actorSelection = getContext().actorSelection(actorPath);
        
        /*发送注册消息*/
        actorSelection.tell(new EventMessages.Registration(),getSelf());
    }
    
}
