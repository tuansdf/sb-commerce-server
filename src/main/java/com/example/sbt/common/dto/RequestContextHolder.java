package com.example.sbt.common.dto;

import com.example.sbt.common.constant.MDCKey;
import com.example.sbt.common.util.ConversionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

public class RequestContextHolder {

    private static final ThreadLocal<RequestContext> context = new ThreadLocal<>();

    public static RequestContext get() {
        RequestContext result = context.get();
        if (result == null) {
            result = new RequestContext();
            context.set(result);
        }
        return result;
    }

    public static void set(RequestContext input) {
        context.set(input);
        syncMDC();
    }

    public static void clear() {
        context.remove();
        MDC.clear();
    }

    public static void syncMDC() {
        RequestContext context = get();
        String userId = ConversionUtils.toString(context.getUserId());
        String requestId = ConversionUtils.toString(context.getRequestId());
        if (StringUtils.isNotEmpty(userId)) {
            MDC.put(MDCKey.USER_ID, userId);
        }
        if (StringUtils.isNotEmpty(requestId)) {
            MDC.put(MDCKey.REQUEST_ID, requestId);
        }
    }

}
