package com.poluectov.criticine.webapp;

import com.poluectov.criticine.webapp.controller.ControllerCommander;
import com.poluectov.criticine.webapp.controller.HttpControllerCommander;
import com.poluectov.criticine.webapp.controller.HttpStandartControllerCommander;
import com.poluectov.criticine.webapp.controller.NotFoundPageCommand;
import com.poluectov.criticine.webapp.controller.createcinemapage.GetCreateCinemaCommand;
import com.poluectov.criticine.webapp.controller.createcinemapage.HttpCreateCinemaCommander;
import com.poluectov.criticine.webapp.controller.createcinemapage.PostCreateCinemaCommand;
import com.poluectov.criticine.webapp.controller.criticspage.GetCriticsCommand;
import com.poluectov.criticine.webapp.controller.criticspage.HttpCriticsPageControllerCommander;
import com.poluectov.criticine.webapp.controller.mainpage.GetMainPageCommand;
import com.poluectov.criticine.webapp.controller.mainpage.HttpMainPageControllerCommander;
import com.poluectov.criticine.webapp.controller.moviepage.GetMoviePageCommand;
import com.poluectov.criticine.webapp.controller.registration.GetRegistrationPageCommand;
import com.poluectov.criticine.webapp.controller.registration.HttpRegistrationPageControllerCommander;
import com.poluectov.criticine.webapp.controller.registration.PostRegistrationPageCommand;
import com.poluectov.criticine.webapp.controller.reviewpage.GetReviewPageController;
import com.poluectov.criticine.webapp.controller.reviewpage.HttpReviewPageControllerCommander;
import com.poluectov.criticine.webapp.dao.CinemaDAO;
import com.poluectov.criticine.webapp.dao.CinemaTypeDAO;
import com.poluectov.criticine.webapp.dao.UserDAO;
import com.poluectov.criticine.webapp.dao.connectionpool.BlockedQueueConnectionPool;
import com.poluectov.criticine.webapp.dao.connectionpool.ConnectionPool;
import com.poluectov.criticine.webapp.dao.mysqldao.MySQLCinemaDao;
import com.poluectov.criticine.webapp.dao.mysqldao.MySQLCinemaTypeDao;
import com.poluectov.criticine.webapp.dao.mysqldao.MySQLUserDao;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.service.CinemaListService;
import com.poluectov.criticine.webapp.service.impl.MySqlCinemaListService;

/**
 * Global instances through application
 */
public class ApplicationContext {

    public static final String user = "java";
    public static final String password = "password";
    static final String serverName = "localhost";
    static final String portNumber = "3306";
    static final String dbms = "mysql";
    static final String connectionProps = "criticinedb";
    public static final String url =   "jdbc:" + dbms + "://" +
                                serverName + ":" + portNumber + "/" +
                                connectionProps;

    public static final ApplicationContext INSTANCE = new ApplicationContext(
            new HttpControllerCommander(

                    new HttpStandartControllerCommander(
                            new GetMainPageCommand(),
                            null,
                            null,
                            null
                    ),

                    new HttpStandartControllerCommander(
                            new GetRegistrationPageCommand(),
                            new PostRegistrationPageCommand(),
                            null,
                            null
                    ),

                    new HttpStandartControllerCommander(
                            new GetReviewPageController(),
                            null,
                            null,
                            null
                    ),

                    new HttpStandartControllerCommander(
                            new GetCriticsCommand(),
                            null,
                            null,
                            null
                    ),

                    new HttpStandartControllerCommander(
                            new GetCreateCinemaCommand(),
                            new PostCreateCinemaCommand(),
                            null,
                            null
                    ),

                    new HttpStandartControllerCommander(
                            new GetMoviePageCommand(),
                            null,
                            null,
                            null
                    ),

                    new NotFoundPageCommand()
            ),
            new BlockedQueueConnectionPool(url, user, password)
    );
    private final ControllerCommander controllerCommander;
    private final ConnectionPool dbConnectionPool;

    private final CinemaListService cinemaListService;
    private static final int PAGE_LIMIT = 20;
    private CinemaTypeDAO cinemaTypeDAO;
    private CinemaDAO cinemaDAO;
    private UserDAO userDAO;


    private ApplicationContext(ControllerCommander controllerCommander,
                               ConnectionPool connectionPool) {
        this.controllerCommander = controllerCommander;
        this.dbConnectionPool = connectionPool;
        this.cinemaListService = new MySqlCinemaListService(dbConnectionPool, PAGE_LIMIT);
    }

    public CinemaTypeDAO getCinemaTypeDAO() {
        if (cinemaTypeDAO == null){
            cinemaTypeDAO = new MySQLCinemaTypeDao(dbConnectionPool);
        }
        return cinemaTypeDAO;
    }

    public CinemaDAO getCinemaDAO() {
        if (cinemaDAO == null){
            cinemaDAO = new MySQLCinemaDao(dbConnectionPool);
        }
        return cinemaDAO;
    }
    public UserDAO getUserDAO() throws DataBaseNotAvailableException {
        if (userDAO == null){
            userDAO = new MySQLUserDao(dbConnectionPool);
        }
        return userDAO;
    }
    public ControllerCommander getControllerCommander(){
        return this.controllerCommander;
    }
    public ConnectionPool getDataBase(){
        return this.dbConnectionPool;
    }

    public CinemaListService getCinemaListService() {
        return cinemaListService;
    }

    /* ADDRESSES */
    public static final String MAIN_PAGE_ADDRESS = "/app/";
    public static final String REGISTRATION_PAGE_ADDRESS = "/register";
    public static final String LOGIN_PAGE_ADDRESS = "/login";
    public static final String CRITICS_PAGE_ADDRESS = "/critics";
    public static final String REVIEW_PAGE_ADDRESS = "r/review";
    public static final String CREATE_CINEMA_ADDRESS = "/m/create";
    public static final String CINEMA_ADDRESS = "/m/cinema";
    public static final String DOMAIN_ADDRESS = "/CritiCine2";

    /* ADDRESSES */

    /* JSP LOCATIONS */
    public static final String JSP_MAIN_PAGE = "/WEB-INF/index.jsp";
    public static final String JSP_REGISTRATION_PAGE = "/WEB-INF/jsp/register.jsp";
    public static final String JSP_REVIEW_PAGE = "/WEB-INF/jsp/r/review.jsp";
    public static final String JSP_MOVIE_PAGE = "/WEB-INF/jsp/m/movie.jsp";
    public static final String JSP_CRITICS_PAGE = "/WEB-INF/jsp/critics.jsp";
    public static final String JSP_CREATE_MOVIE_PAGE = "/WEB-INF/jsp/m/createMovie.jsp";
    /* JSP LOCATIONS */
    public static final String UPLOAD_DIRECTORY = "/uploads";
    public static final String DEFAULT_CINEMA_PICTURE_FILE_NAME = "default.png";


}
