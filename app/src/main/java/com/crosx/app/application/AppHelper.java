package com.crosx.app.application;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.crosx.app.ui.im.ChatActivity;
import com.crosx.app.utils.ListenerManager;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.ChatManager;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.easeui.Notifier;
import com.hyphenate.helpdesk.easeui.UIProvider;
import com.hyphenate.helpdesk.easeui.util.CommonUtils;
import com.hyphenate.helpdesk.easeui.util.IntentBuilder;
import com.hyphenate.helpdesk.util.Log;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by CrosX on 2016/11/21.
 *
 * @BlinRoom
 */

public class AppHelper {

    private static String TAG = AppHelper.class.getSimpleName();

    public static AppHelper instance = new AppHelper();

    /**
     * kefuChat.MessageListener
     */
    protected ChatManager.MessageListener messageListener = null;

    private UIProvider _uiProvider;


    private AppHelper() {
    }

    public synchronized static AppHelper getInstance() {
        return instance;
    }

    public void init(final Context context) {
        ChatClient.Options options = new ChatClient.Options();
        options.setAppkey("1132161121178186#crosx");
        options.setTenantId("31884");

        // 环信客服 SDK 初始化, 初始化成功后再调用环信下面的内容
        if (ChatClient.getInstance().init(context, options)) {

            //设为调试模式，打成正式包时，最好设为false，以免消耗额外的资源
            ChatClient.getInstance().setDebugMode(true);

            _uiProvider = UIProvider.getInstance();
            //初始化EaseUI
            _uiProvider.init(context);
            //调用easeui的api设置providers
            //设置全局监听
            setEaseUIProvider(context);
            setGlobalListeners();
        }
    }


    private void setEaseUIProvider(final Context context) {
        //设置头像和昵称
//        UIProvider.getInstance().setUserProfileProvider(new UIProvider.UserProfileProvider() {
//            @Override
//            public void setNickAndAvatar(Context context, Message message, ImageView userAvatarView, TextView usernickView) {
//                if (message.getDirect() == Message.Direct.RECEIVE) {
//                    //设置接收方的昵称和头像
//                    UserUtil.setAgentNickAndAvatar(context, message, userAvatarView, usernickView);
//                } else {
//                    //此处设置当前登录用户的昵称信息(发送方)
////                    userAvatarView.setImageResource();
////                    usernickView.setText("");
//                }
//            }
//        });


        //设置通知栏样式
        _uiProvider.getNotifier().setNotificationInfoProvider(new Notifier.NotificationInfoProvider() {
            @Override
            public String getTitle(Message message) {
                //修改标题,这里使用默认
                return null;
            }

            @Override
            public int getSmallIcon(Message message) {
                //设置小图标，这里为默认
                return 0;
            }

            @Override
            public String getDisplayedText(Message message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
                String ticker = CommonUtils.getMessageDigest(message, context);
                if (message.getType() == Message.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                return message.getFrom() + ": " + ticker;
            }

            @Override
            public String getLatestText(Message message, int fromUsersNum, int messageNum) {
                return null;
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
            }

            @Override
            public Intent getLaunchIntent(Message message) {
                //设置点击通知栏跳转事件
                Intent intent = new IntentBuilder(context)
                        .setTargetClass(ChatActivity.class)
                        .setServiceIMNumber(message.getFrom())
                        .setShowUserNick(true)
                        .build();
                return intent;
            }
        });

        //不设置,则使用默认, 声音和震动设置
//        _uiProvider.setSettingsProvider(new UIProvider.SettingsProvider() {
//            @Override
//            public boolean isMsgNotifyAllowed(Message message) {
//                return false;
//            }
//
//            @Override
//            public boolean isMsgSoundAllowed(Message message) {
//                return false;
//            }
//
//            @Override
//            public boolean isMsgVibrateAllowed(Message message) {
//                return false;
//            }
//
//            @Override
//            public boolean isSpeakerOpened() {
//                return false;
//            }
//        });
//        ChatClient.getInstance().getChat().addMessageListener(new MessageListener() {
//            @Override
//            public void onMessage(List<Message> msgs) {
//
//            }
//
//            @Override
//            public void onCmdMessage(List<Message> msgs) {
//
//            }
//
//            @Override
//            public void onMessageSent() {
//
//            }
//
//            @Override
//            public void onMessageStatusUpdate() {
//
//            }
//        });
    }

    private void setGlobalListeners() {
        registerEventListener();

    }

    protected void registerEventListener() {
        messageListener = new ChatManager.MessageListener() {

            @Override
            public void onMessage(List<Message> msgs) {
                for (Message message : msgs) {
                    Log.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    Log.d(TAG, "message:" + message);
                    //应用在后台,不需要刷新UI,通知栏提示新消息
                    if (_uiProvider.hasForegroundActivies()) {
                        getNotifier().viberateAndPlayTone(message);
                    }

                    //这里全局监听通知类消息,通知类消息是通过普通消息的扩展实现
                    if (message.isNotificationMessage()) {
                        // 检测是否为留言的通知消息
                        String eventName = getEventNameByNotification(message);
                        if (!TextUtils.isEmpty(eventName)) {
                            if (eventName.equals("TicketStatusChangedEvent") || eventName.equals("CommentCreatedEvent")) {
                                // 检测为留言部分的通知类消息,刷新留言列表
                                JSONObject jsonTicket = null;
                                try {
                                    jsonTicket = message.getJSONObjectAttribute("weichat").getJSONObject("event").getJSONObject("ticket");
                                } catch (Exception e) {
                                }
                                ListenerManager.getInstance().sendBroadCast(eventName, jsonTicket);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCmdMessage(List<Message> msgs) {
                for (Message message : msgs) {
                    Log.d(TAG, "收到透传消息");
                    //获取消息body
                    EMCmdMessageBody cmdMessageBody = (EMCmdMessageBody) message.getBody();
                    String action = cmdMessageBody.action(); //获取自定义action
                    Log.d(TAG, String.format("透传消息: action:%s,message:%s", action, message.toString()));
                }
            }

            @Override
            public void onMessageStatusUpdate() {

            }

            @Override
            public void onMessageSent() {

            }
        };

        ChatClient.getInstance().getChat().addMessageListener(messageListener);
    }

    /**
     * 获取EventName
     *
     * @param message
     * @return
     */
    public String getEventNameByNotification(Message message) {

        try {
            JSONObject weichatJson = message.getJSONObjectAttribute("weichat");
            if (weichatJson != null && weichatJson.has("event")) {
                JSONObject eventJson = weichatJson.getJSONObject("event");
                if (eventJson != null && eventJson.has("eventName")) {
                    return eventJson.getString("eventName");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Notifier getNotifier() {
        return _uiProvider.getNotifier();
    }

}
