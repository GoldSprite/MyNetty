package com.goldSprite.nioDevFramework.codec.codecInterfaces;

import java.util.HashMap;

/**
 * 四种收发状态分别以1234万开头
 * 每100作为不同指令的信息码id
 */
public interface IStatus {
    int SEND_SUCCESS = 10001;
    int SEND_DEFEAT = 20001;
    int RETURN_SUCCESS = 30001;
    int RETURN_DEFEAT = 40001;

//    int RETURN_SUCCESS_LOGIN = 30101;
//    String MSG_RETURN_SUCCESS_LOGIN = "登录成功";
    int RETURN_SUCCESS_LOGIN_PUSHTOP = 30101;
    String MSG_RETURN_SUCCESS_LOGIN_PUSHTOP = "挤号登录成功";
    int RETURN_DEFEAT_LOGIN_REPEAT = 40101;
    String MSG_RETURN_DEFEAT_LOGIN_REPEAT = "重复登陆";
    int RETURN_DEFEAT_LOGIN_REPEAT_SAMEGUID = 40102;
    String MSG_RETURN_DEFEAT_LOGIN_REPEAT_SAMEGUID = "重复的Guid登陆";
    int RETURN_DEFEAT_LOGIN_REPEAT_SAMEADDRESS = 40103;
    String MSG_RETURN_DEFEAT_LOGIN_REPEAT_SAMEADDRESS = "重复的IP地址登陆";
    int RETURN_DEFEAT_LOGIN_NOTLOGIN = 40111;
    String MSG_RETURN_DEFEAT_LOGIN_NOTLOGIN = "未登录, 请先登录";

    HashMap<Integer, String> msgMap = new HashMap<Integer, String>() {{
//        put(RETURN_SUCCESS_LOGIN, MSG_RETURN_SUCCESS_LOGIN);
        put(RETURN_SUCCESS_LOGIN_PUSHTOP, MSG_RETURN_SUCCESS_LOGIN_PUSHTOP);
        put(RETURN_DEFEAT_LOGIN_REPEAT, MSG_RETURN_DEFEAT_LOGIN_REPEAT);
        put(RETURN_DEFEAT_LOGIN_REPEAT_SAMEGUID, MSG_RETURN_DEFEAT_LOGIN_REPEAT_SAMEGUID);
        put(RETURN_DEFEAT_LOGIN_REPEAT_SAMEADDRESS, MSG_RETURN_DEFEAT_LOGIN_REPEAT_SAMEADDRESS);
        put(RETURN_DEFEAT_LOGIN_NOTLOGIN, MSG_RETURN_DEFEAT_LOGIN_NOTLOGIN);
    }};

    static String getStatusMsg(Packet pk, int status) {
        var msg = pk.getClass().getSimpleName() +" "
                + (isReturnStatus(status) ? "响应" : "发送")
                + (isSuccessStatus(status) ? "成功" : "失败")
                + (msgMap.get(status) == null ? "" : ", reason: "+msgMap.get(status));
        return msg;
    }
    static String getStatusMsg(Packet pk) {
        return getStatusMsg(pk, pk.getCode());
    }

    static boolean isReturnStatus(Packet pk) {
        return isReturnStatus(pk.getCode());
    }
    static boolean isReturnStatus(int status) {
        return status / 10000 == 3 || status / 10000 == 4;
    }

    static boolean isSuccessStatus(Packet pk) {
        return isSuccessStatus(pk.getCode());
    }
    static boolean isSuccessStatus(int status) {
        return status / 10000 == 1 || status / 10000 == 3;
    }
}
