package com.goldSprite.nioDevFramework.tools;

import java.util.HashMap;

public interface ILogLevel {
    int FORCE = -2;// 强制输出
    int ERROR = 1;// 错误
    int WARNING = 2;// 警告
    int DEBUG = 3;// 调试: 用于判断调试
    int INFO = 4;// 信息: 程序运行提示信息
    int MSG = 5;// 消息: 玩家看到的消息

    HashMap<Integer, String> msgMap = new HashMap<Integer, String>() {{
        put(ILogLevel.MSG, "[ MSG ] ");
        put(ILogLevel.INFO, "[INFO ] ");
        put(ILogLevel.DEBUG, "[DEBUG] ");
        put(ILogLevel.WARNING, "[WARN ] ");
        put(ILogLevel.ERROR, "[ ERR ] ");
        put(ILogLevel.FORCE, "[FORCE] ");
    }};

    static String getLogMsg(int logLevel) {
        if(!msgMap.containsKey(logLevel)) return "[UNKNOWN] ";
        return msgMap.get(logLevel);
    }
}
