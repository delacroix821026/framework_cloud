package com.newland.financial.p2p.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.log4j.Log4j;
import org.ohuyo.libra.client.session.LibraSession;
import org.ohuyo.libra.client.session.LibraSessionUtils;
import org.ohuyo.libra.client.session.SlaveLibraSessionImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//@Component
@Log4j
public class SessionFilter extends ZuulFilter {
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
        if(ctx.getRequest().getRequestURI().startsWith("/sso2/")) {
            log.info("Session filter shoud be run");
            return true;
        }
        return false;
    }

    public Object run() {
        log.info("Installment filter:");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest httpServletRequest = (HttpServletRequest) ctx.getRequest();
        HttpSession httpSession = httpServletRequest.getSession();//false
        if(httpSession.getAttribute("ORG_OHUYO_LIBRA_CLIENT_LIBRA_SESSION") == null) {
            log.debug("USER_INFO in session is null");
            String userNo = httpServletRequest.getHeader("userNo");
            String mercList = httpServletRequest.getHeader("mercList");
            String tokenId = httpServletRequest.getHeader("tokenId");
            SlaveLibraSessionImpl libraSession = new SlaveLibraSessionImpl();
            libraSession.setAppTicket(tokenId);
            libraSession.setLoginName(userNo);
            libraSession.setOrgList(mercList);
            log.info("Installment filter-userId:" + userNo);
            httpSession.setAttribute("ORG_OHUYO_LIBRA_CLIENT_LIBRA_SESSION", libraSession);
            ctx.addZuulRequestHeader("Cookie", "SESSION=" + httpSession.getId());
        }
        return null;
    }
}

