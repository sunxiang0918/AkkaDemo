package cn.sunxiang0918.akka.demo8;

public class Demo8App {

    public static void main(String[] args) throws Exception {

        // 启动一个Client
        EventClient.main(new String[0]);
        
        // 启动两个Interceptor
        EventInterceptorMain.main(new String[] { "2851" });
        EventInterceptorMain.main(new String[] { "2852" });
        
        // 启动两个Processor
        EventProcessorMain.main(new String[]{"2951"});
        EventProcessorMain.main(new String[]{"2952"});
        EventProcessorMain.main(new String[]{"2953"});
        EventProcessorMain.main(new String[]{"2954"});
        EventProcessorMain.main(new String[]{"2955"});
        
    }
}
