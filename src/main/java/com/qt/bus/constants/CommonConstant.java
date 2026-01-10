package com.qt.bus.constants;

import com.google.common.collect.Lists;
import java.util.List;

public class CommonConstant {

    // type
    public static final String REQ = "req";
    public static final String ACK = "ack";
    public static final String RESP = "resp";
    public static final String PULL = "pull";
    public static final String PUSH = "push";

    // action
    public static final String LOGIN = "login";
    public static final String SEND_MSG = "sendMsg";
    public static final String PULL_MSG = "pullMsg";
    public static final String MSG_STATUS = "msgStatus";
    public static final String MSG_STATUS_DETAIL = "msgStatusDetail";
    public static final String MSG_HISTORY = "msgHistory";
    public static final String HEART_BEAT = "heartbeat";

    // symbol
    public static final String BAR = "-";
    public static final String COMMA = ",";
    public static final String SLASH = "/";
    public static final String UNDER_LINE = "_";

    public static final String GMT_MODIFIED = "gmt_modified";
    public static final String GMT_CREATE = "gmt_create";
    public static final String DELETED = "deleted";
    public static final String ENABLED = "enabled";
    public static final String USER_ID = "user_id";
    public static final String USER_TYPE = "user_type";
    public static final String ENV = "env";
    public static final String NICK_NAME = "nick_name";
    public static final String PLATFORM = "platform";
    public static final String IF_CANCEL = "if_cancel";
    public static final String LQ = "lq";
    public static final String ROLE = "role";


    // trace-id
    public static final String TRACE_ID = "TRACE_ID";

    // 系统用户
    public static final String SYSTEM = "SYSTEM";

    // 省市区
    public static final String PROVINCE_LIST = "provinceList";
    public static final String CITY_LIST = "cityList";
    public static final String REGION_LIST = "regionList";

    // 招投标布隆过滤器
    public final static String BIDDING_DATA_BLOOM_FILTER = "BIDDING_DATA_BLOOM_FILTER";

    // es 高亮前缀
    public final static String ES_HIGH_LIGHT_PRE_TAGS = "<a style='color:#0260F9'>";

    // AI调用次数dimKey
    public static final String AI_USAGE_COUNT = "AI_USAGE_COUNT";

    // 企业logo前缀
    public static final String COMPANY_LOGO_HTTPS_PREFIX = "https://oss.lianqiai.cn/logo/";

    // 首购优惠商品code列表
    public static final List<String> FIRST_PURCHASE_GOODS_LIST = List.of("VIP_month_0");

    // 订单锁key前缀
    public static final String ORDER_LOCK_KEY_PRE = "lock_order_";

    // 流结束标识
    public static final String STREAM_DONE = "[DONE]";


    public static final String TOKEN_SUFFIX = "_WEB_TOKEN";

    // 邀请码redis前缀
    public static final String INVITE_CODE_PRE = "INVITE_";
    // 标书页数字数比例 1:620 (20250725修改 620->660)
    public static final Integer WORD_CNT_PRE_PAGE = 660;
    // 标书页数字数比例 1:800（无图文并茂） (20250725修改 800->660)
    public static final Integer WORD_CNT_NOT_WITH_IMAGE_PRE_PAGE = 660;
    // 标书小节默认页数比例 1:5
    public static final Integer PAGE_CNT_PRE_LEVEL_3_CHAPTER = 5;
    // 标书小节页数最小比例 1:0.25
    public static final float MIN_PAGE_CNT_PRE_LEVEL_3_CHAPTER = 0.5f;
    // 标书小节页数最大比例 1:15
    public static final Integer MAX_PAGE_CNT_PRE_LEVEL_3_CHAPTER = 15;
    // 标书小节页数最大比例 1:12（无图文并茂）
    public static final Integer MAX_PAGE_CNT_NOT_WITH_IMAGE_PRE_LEVEL_3_CHAPTER = 13;
    // 标书旗舰版每日使用上限次数
    public static final Integer MAX_COUNT_PRO_BID_DOC_PER_DAY = 3;
    // 创建大页数标书每日上限次数
    public static final Integer MAX_COUNT_LARGE_PAGE_BID_DOC_PER_DAY = 2;
    // 标书工程图纸每日上限次数
    public static final Integer MAX_COUNT_BID_DOC_ENGINEERING_DRAWING_PER_DAY = 1;
    // 正文新版灰度名单
    public static final List<Long> CONTENT_DART_LAUNCH = Lists.newArrayList();

    public static final String TEAM_TOKEN_SUFFIX = "_WEB_TEAM_TOKEN";

    public static final Integer AUTO_LIST_KEY = 10000;

    public static final String ADVANCED_FLOW_CHART_SVG_CODE_S_JSON = "lianqiai/mqtt/ai_compose_data/advanced_flow_chart_svg_code/%s.json";

}
