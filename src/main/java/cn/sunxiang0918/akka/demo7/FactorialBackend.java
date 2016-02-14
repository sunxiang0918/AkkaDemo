package cn.sunxiang0918.akka.demo7;

import java.math.BigInteger;

import akka.actor.UntypedActor;
import akka.dispatch.Mapper;
import scala.concurrent.Future;

import static akka.dispatch.Futures.future;
import static akka.pattern.Patterns.pipe;

//#backend
public class FactorialBackend extends UntypedActor {

    @Override
    public void onReceive(Object message) {
        //如果是数字
        if (message instanceof Integer) {
            final Integer n = (Integer) message;
            /*使用akka的future功能,异步的计算阶乘*/
            Future<BigInteger> f = future(() -> factorial(n), getContext().dispatcher());

            /*合并计算的结果*/
            Future<FactorialResult> result = f.map(
                    new Mapper<BigInteger, FactorialResult>() {
                        public FactorialResult apply(BigInteger factorial) {
                            return new FactorialResult(n, factorial);
                        }
                    }, getContext().dispatcher());
            
            /*把结果返回Sender*/
            pipe(result, getContext().dispatcher()).to(getSender());
        } else {
            unhandled(message);
        }
    }

    /**
     * 进行阶乘计算
     * @param n
     * @return
     */
    BigInteger factorial(int n) {
        BigInteger acc = BigInteger.ONE;
        for (int i = 1; i <= n; ++i) {
            acc = acc.multiply(BigInteger.valueOf(i));
        }
        return acc;
    }
}
//#backend

