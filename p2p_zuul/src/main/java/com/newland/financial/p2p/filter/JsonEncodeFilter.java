package com.newland.financial.p2p.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.log4j.Log4j;
import org.ohuyo.libra.client.session.SlaveLibraSessionImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
@Log4j
public class JsonEncodeFilter extends ZuulFilter {
    @Override
    public int filterOrder() {
        return 1; // run before PreDecoration
    }

    @Override
    public String filterType() {
        return "pre";
    }

    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        if(ctx.getRequest().getQueryString() != null && ctx.getRequest().getQueryString().indexOf("jsonStr") > -1) {
            log.debug("JsonEncodeFilter filter shoud be run");
            return true;
        }
        return false;
    }

    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();

        try {
            Map<String, List<String>> map = new HashMap<String, List<String>>();
            for(Map.Entry<String, List<String>> entry : ctx.getRequestQueryParams().entrySet()) {
                List<String> list = entry.getValue();
                List<String> newList = new ArrayList<String>();
                for(String value : list) {
                    newList.add(URLEncoder.encode(value, "UTF-8"));
                }
                map.put(entry.getKey(), newList);
            }
            ctx.setRequestQueryParams(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

