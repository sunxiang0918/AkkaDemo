package cn.sunxiang0918.akka.demo5.actor;

import akka.actor.ActorSystem;
import akka.routing.CustomRouterConfig;
import akka.routing.Router;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/27 10:30
 */
public class VoteCountRouter extends CustomRouterConfig{

    @Override
    public Router createRouter(ActorSystem system) {
        return null;
    }
}
