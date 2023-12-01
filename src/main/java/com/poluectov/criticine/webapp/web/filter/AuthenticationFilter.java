package com.poluectov.criticine.webapp.web.filter;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.dao.UserDAO;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.model.data.User;
import com.poluectov.criticine.webapp.model.data.UserRole;
import com.poluectov.criticine.webapp.model.security.PasswordVerifier;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class AuthenticationFilter implements Filter {
    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class);
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //setting locale
        String locale = request.getLocale().getLanguage();
        request.getSession().setAttribute("locale", locale);

        String userName = request.getParameter("user_name");
        String password = request.getParameter("user_password");


        HttpSession session = request.getSession();
        boolean dbAccessible = true;
        UserDAO userDAO = null;
        try {
             userDAO = ApplicationContext.INSTANCE.getUserDAO();
        } catch (DataBaseNotAvailableException e) {
            e.printStackTrace();
            logout(session);
            dbAccessible = false;
        }

        try {
            //logged
            if (userName == null && password == null && session.getAttribute("user_name") != null) {
                // User is already logged in
            } else {
                try {
                    if (dbAccessible) {
                        User user = userDAO.userByName(userName);
                        //Not logged
                        if (user == null) {
                            logger.info("User " + userName + " not found");
                            logout(session);
                        } else {
                            //just logged
                            boolean isVerified = PasswordVerifier.verifyPassword(password, user.getPasswordHash());

                            if (isVerified){
                                int id = user.getRoleId();
                                session.setAttribute("user_id", user.getId());
                                session.setAttribute("user_name", user.getName());
                                session.setAttribute("user_password_hash", user.getPasswordHash());
                                session.setAttribute("user_role", id);
                                logger.info("User " + user.getName() + " logged in");
                            }else {
                                logger.error("User " + userName + " not found");
                                logout(session);
                            }
                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    logout(session);
                    dbAccessible = false;
                }
            }
        }finally {
            filterChain.doFilter(request, response);
        }
    }


    private void logout(HttpSession session){
        session.removeAttribute("user_name");
        session.removeAttribute("user_password_hash");
        session.removeAttribute("user_role");
    }
}
