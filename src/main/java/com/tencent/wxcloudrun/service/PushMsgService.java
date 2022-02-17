package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.WxAccount;

public interface PushMsgService {

    void pushMsgToUser(WxAccount to, String title,  String msg);

}
