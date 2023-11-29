package com.poluectov.criticine.webapp.service.impl;

import com.poluectov.criticine.webapp.dao.CinemaDAO;
import com.poluectov.criticine.webapp.dao.connectionpool.BlockedQueueConnectionPool;
import com.poluectov.criticine.webapp.dao.mysqldao.MySQLCinemaDao;
import com.poluectov.criticine.webapp.service.CinemaListService;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.poluectov.criticine.webapp.dao.mysqldao.MySQLCinemaDao.*;
import static org.junit.jupiter.api.Assertions.*;

class MySqlCinemaListServiceTest {

    @Test
    void buildCinemaListSql_noneFilter_withoutWhereAndOrderBy() {

        MySqlCinemaListService service = new MySqlCinemaListService(null, 20);

        String actual = service.buildCinemaListSql(null, 0);
        String expected = "SELECT * FROM cinema LIMIT ?";

        actual = actual.toLowerCase();
        expected = expected.toLowerCase();


        assertEquals(expected, actual);
    }

    @Test
    void buildCinemaListSql_filterCreationYear_sqlWithWhereStatement() {

        MySqlCinemaListService service = new MySqlCinemaListService(null, 20);

        Map<String, Object> filterMap = Map.of(MySQLCinemaDao.CINEMA_CREATING_YEAR, 2020);

        CinemaListService.Filter filter = new CinemaListService.Filter(filterMap, null);
        String actual = service.buildCinemaListSql(filter, 0);
        String expected = "SELECT * FROM cinema WHERE "+MySQLCinemaDao.CINEMA_CREATING_YEAR+"=? LIMIT ?";

        actual = actual.toLowerCase();
        expected = expected.toLowerCase();

        assertEquals(expected, actual);
    }
    @Test
    void buildCinemaListSql_orderName_sqlWithOrderByStatement(){
        MySqlCinemaListService service = new MySqlCinemaListService(null, 20);

        CinemaListService.Filter filter = new CinemaListService.Filter(null, CINEMA_NAME);
        String actual = service.buildCinemaListSql(filter, 0);
        String expected = "SELECT * FROM cinema ORDER BY "+ CINEMA_NAME+" LIMIT ?";

        actual = actual.toLowerCase();
        expected = expected.toLowerCase();

        assertEquals(expected, actual);
    }

    @Test
    void buildCinemaListSql_filterAndOrder_sqlWithWhereAndOrderByStatement(){
        MySqlCinemaListService service = new MySqlCinemaListService(null, 20);

        Map<String, Object> filterMap = Map.of(MySQLCinemaDao.CINEMA_CREATING_YEAR, 2020);

        CinemaListService.Filter filter = new CinemaListService.Filter(filterMap, CINEMA_NAME);
        String actual = service.buildCinemaListSql(filter, 0);
        String expected = "SELECT * FROM cinema WHERE "
                +CINEMA_CREATING_YEAR+"=? ORDER BY "+ CINEMA_NAME+" LIMIT ?";

        actual = actual.toLowerCase();
        expected = expected.toLowerCase();


        assertEquals(expected, actual);
    }

    @Test
    void buildCinemaListSql_secondPage_sqlWithWhereAndOrderByStatement(){
        MySqlCinemaListService service = new MySqlCinemaListService(null, 20);

        String actual = service.buildCinemaListSql(null, 1);
        String expected = "SELECT * FROM cinema LIMIT ? OFFSET ?";

        actual = actual.toLowerCase();
        expected = expected.toLowerCase();


        assertEquals(expected, actual);
    }

    @Test
    void buildCinemaListSql_multipleFilters_sqlWithMultipleStatementsInWhere(){
        MySqlCinemaListService service = new MySqlCinemaListService(null, 20);

        //to save order
        Map<String, Object> filterMap = new LinkedHashMap<>();
        filterMap.put(MySQLCinemaDao.CINEMA_CREATING_YEAR, 2020);
        filterMap.put(MySQLCinemaDao.CINEMA_FK_CINEMA_TYPE, 1);

        CinemaListService.Filter filter = new CinemaListService.Filter(filterMap, CINEMA_NAME);
        String actual = service.buildCinemaListSql(filter, 0);
        String expected = "SELECT * FROM cinema WHERE "
                +CINEMA_CREATING_YEAR+"=? AND "+CINEMA_FK_CINEMA_TYPE+"=? ORDER BY "+ CINEMA_NAME+" LIMIT ?";

        actual = actual.toLowerCase();
        expected = expected.toLowerCase();


        assertEquals(expected, actual);
    }

    @Test
    void buildCinemaListCountSql() {
    }
}