package cn.sunxiang0918.akka.demo8;

import java.io.Serializable;

//#messages
public interface EventMessages {

    public static class EventMessage implements Serializable {

    }

    /**
     * 内存中的Nginx的日志
     */
    public static class RawNginxRecord extends EventMessage {

        private String sourceHost;

        private String line;

        public RawNginxRecord(String sourceHost, String line) {
            this.sourceHost = sourceHost;
            this.line = line;
        }

        public String getLine() {
            return line;
        }

        public String getSourceHost() {
            return sourceHost;
        }
    }

    /**
     * 解析出了事件内容的Nginx记录
     */
    public static class NginxRecord extends EventMessage {

        private String sourceHost;

        private String line;

        private String eventCode;

        public NginxRecord(String sourceHost, String line, String eventCode) {
            this.sourceHost = sourceHost;
            this.line = line;
            this.eventCode = eventCode;
        }

        public String getSourceHost() {
            return sourceHost;
        }

        public String getLine() {
            return line;
        }

        public String getEventCode() {
            return eventCode;
        }
    }

    /**
     * 通过了拦截器的日志记录
     */
    public static class FilteredRecord extends EventMessage {

        private String sourceHost;

        private String line;

        private String eventCode;

        private String logDate;

        private String realIp;

        public FilteredRecord(String sourceHost, String line, String eventCode, String logDate, String realIp) {
            this.sourceHost = sourceHost;
            this.line = line;
            this.eventCode = eventCode;
            this.logDate = logDate;
            this.realIp = realIp;
        }

        public String getSourceHost() {
            return sourceHost;
        }

        public String getLine() {
            return line;
        }

        public String getEventCode() {
            return eventCode;
        }

        public String getLogDate() {
            return logDate;
        }

        public String getRealIp() {
            return realIp;
        }
    }

    /**
     * 子系统注册的消息
     */
    public static final class Registration implements Serializable {
    }

}
//#messages