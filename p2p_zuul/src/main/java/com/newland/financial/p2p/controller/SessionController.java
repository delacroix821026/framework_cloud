package com.newland.financial.p2p.controller;


import lombok.extern.log4j.Log4j;
import org.ohuyo.libra.client.exception.LibraClientException;
import org.ohuyo.libra.client.session.LibraSession;
import org.ohuyo.libra.client.session.LibraSessionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Log4j
@RestController
public class SessionController {
    @RequestMapping("/getUserSession")
    public String test() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        HttpSession session = req.getSession();
        System.out.println("是否生成："+session.getAttribute("abc"));

        LibraSession libraSession = LibraSessionUtils.getSession(session);
        /*if (req instanceof HttpServletRequest) {

            session = req.getSession(false);
            if (session != null) {
                log.info("--------LibraSession--------isHttpServletRequest---------");
                String userName = (String) session.getAttribute("userName");
                if (userName == null) {
                    libraSession = LibraSessionUtils.getSession(session);
                }
            }
        }*/




        log.info("libraSess================================" + libraSession);
        /*log.info("1--------LibraSession--------permissions---------" + libraSession.getMenuList());
        log.info("1--------LibraSession--------organization---------" + libraSession.getOrgList());
        log.info("1--------LibraSession--------loginName---------" + libraSession.getLoginName());
        log.info("1--------LibraSession--------userName---------" + libraSession.getUserName());*/

        return "PING OK";
    }
}
