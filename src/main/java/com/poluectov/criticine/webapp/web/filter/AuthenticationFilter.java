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

import java.io.IOException;
import java.sql.SQLException;

public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String userName = request.getParameter("user_name");
        String passwordHash = request.getParameter("user_password_hash");

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

        //logged
        if (userName != null && passwordHash != null && session.getAttribute("user_name") != null){
            return;
        }else {
            try {
                if (dbAccessible){
                    User user = userDAO.userByPassword(userName, passwordHash);
                    //Not logged
                    if (user == null){
                        logout(session);

                    }else {
                        //just logged
                        int id = user.getRoleId();

                        session.setAttribute("user_name", userName);
                        session.setAttribute("user_password_hash", passwordHash);
                        session.setAttribute("user_role", id);
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
                logout(session);
                dbAccessible = false;
            }
        }
    }


    private void logout(HttpSession session){
        session.removeAttribute("user_name");
        session.removeAttribute("user_password_hash");
        session.removeAttribute("user_role");
    }
}
