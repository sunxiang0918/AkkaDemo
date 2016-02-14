package cn.sunxiang0918.akka.demo1_5.actor;

import akka.japi.Option;
import scala.concurrent.Future;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/24 13:53
 */
public interface Squarer {

    Future<Integer> square(int i); //non-blocking send-request-reply

    Option<Integer> squareNowPlease(int i);//blocking send-request-reply

    int squareNow(int i); //blocking send-request-reply
}
