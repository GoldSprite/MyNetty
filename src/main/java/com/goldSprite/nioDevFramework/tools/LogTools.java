package com.goldSprite.nioDevFramework.tools;

import java.util.HashMap;

public class LogTools {
    public static HashMap<Integer, Boolean> logLevels = new HashMap<>() {{
        put(ILogLevel.ERROR, true);
        put(ILogLevel.WARNING, true);
        put(ILogLevel.DEBUG, true);
        put(ILogLevel.INFO, true);
        put(ILogLevel.MSG, true);
    }};

    //    public static void NLog(InetSocketAddress sender, String msg){
//        boolean isServer = sender.getHostString().equals(UdpClient.remoteAddress2.getHostString()) && sender.getPort()==UdpClient.remoteAddress2.getPort();
//        LogTools.NLog(
//                DateTools.currentDateTime()+
//                        "["+(isServer?"SERVER":"CLIENT")+"] "+
//                        "["+sender.getHostString()+":"+sender.getPort()+"] "+
//                        msg
//        );
//    }
    public static void NLogMsg(Object msg) {
        NLog(ILogLevel.MSG, msg);
    }

    public static void NLogInfo(Object msg) {
        NLog(ILogLevel.INFO, msg);
    }

    public static void NLogDebug(Object msg) {
        NLog(ILogLevel.DEBUG, msg);
    }

    public static void NLogWarn(Object msg) {
        NLog(ILogLevel.WARNING, msg);
    }

    public static void NLogErr(Object msg) {
        NLog(ILogLevel.ERROR, msg);
    }

    public static void NLog(int logLevel, Object msg) {
        if (logLevel != ILogLevel.FORCE)
            if (!logLevels.containsKey(logLevel) || !logLevels.get(logLevel)) return;
        System.out.println(
                "[" + TimeUtils.formatTime() + "]"
                        + ILogLevel.msgMap.get(logLevel)
                        + msg.toString()
        );
    }
}
