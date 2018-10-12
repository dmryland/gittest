package com.ni.apps.soa.nivcg.utils;

import com.ni.apps.soa.nivcg.model.NivcgDto;
import com.ni.lib.sql.NiStaticConnectionPool;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;

import static com.ni.apps.soa.nivcg.constants.NivcgConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: NINJIUNN
 * Date: 5/9/12
 * Time: 8:37 AM
 * To change this template use File | Settings | File Templates.
 */
public final class NivcgDbUtils {
    private static Logger logger = Logger.getLogger(NivcgDbUtils.class);

    private NivcgDbUtils() {
    }

    /**
     * Get all domains
     *
     * @return List<String>
     */
    @SuppressWarnings("all")
    public static List<String> getDomains() {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDbUtils.getDomains()");
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<String> domains = new ArrayList<String>();

        try {
            connection = getDataTierConnection();

            if(connection != null) {
                preparedStatement = connection.prepareStatement(NISOAR_DOMAIN_MASTER);
                resultSet = preparedStatement.executeQuery();
                domains = resultSetStrListConvert(resultSet);
            } else {
                domains = Collections.emptyList();
                logger.error("NivcgDbUtils.getDomains() DB connection failed.");
            }
        } catch(SQLException xSQL) {
            logger.error("NivcgDbUtils.getDomains() failure: ", xSQL);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return(domains);
    }

    /**
     * Get configs
     *
     * @param domain - domain
     * @return List<NivcgDto>
     */
    @SuppressWarnings("all")
    public static List<NivcgDto> getConfigs(String domain, String tier) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDbUtils.getConfigs()");
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<NivcgDto> resultSetDtoList = new ArrayList<NivcgDto>();

        try {
            connection = getDataTierConnection();

            if(connection != null) {
                preparedStatement = connection.prepareStatement(NISOAR_VCG_MASTER);
                preparedStatement.setString(1, domain);
                preparedStatement.setString(2, tier);
                resultSet = preparedStatement.executeQuery();
                resultSetDtoList = resultSetDtoListConvert(resultSet);
            } else {
                resultSetDtoList = Collections.emptyList();
                logger.error("NivcgDbUtils.getConfigs() DB connection failed.");
             }
        } catch(SQLException xSQL) {
            logger.error("NivcgDbUtils.getConfigs() failure: ", xSQL);
        } finally {
            close(resultSet, preparedStatement, connection);
        }

        if(resultSetDtoList.isEmpty()) {
            resultSetDtoList = Collections.emptyList();
        }
        return(resultSetDtoList);
    }

    /**
     * Convert DB result set into string list
     *
     * @param resultSet - result set
     * @return List<String>
     */
    private static List<String> resultSetStrListConvert(ResultSet resultSet) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDbUtils.resultSetStrListConvert()");
        }

        List<String> strList = new ArrayList<String>();

        try {
            while(resultSet != null && resultSet.next()) {
                strList.add(resultSet.getString(NAME));
            }
        } catch(SQLException xSQL) {
            logger.error("NivcgDBUtils.resultSetStrListConvert() failure: ", xSQL);
        }

        if(strList.isEmpty()) {
            strList = Collections.emptyList();
        }
        return(strList);
    }

    /**
     * Convert DB result set into dto model
     *
     * @param resultSet - result set
     * @return List<NivcgDto>
     */
    private static List<NivcgDto> resultSetDtoListConvert(ResultSet resultSet) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDbUtils.resultSetDtoListConvert()");
        }

        NivcgDto vcgDto;
        List<NivcgDto> vcgDtoList = new ArrayList<NivcgDto>();
        Set<String> apiKeys = null;

        try {
            while(resultSet != null && resultSet.next()) {
                vcgDto = new NivcgDto();
                apiKeys = new HashSet<String>();
                apiKeys.add(resultSet.getString(API_KEY));
                vcgDto.setAll(resultSet.getString(DOMAIN), resultSet.getString(TIER), 
                		resultSet.getString(CONTEXT_ROOT), resultSet.getString(VER), 
                		(resultSet.getInt("key_enforced") > 0), apiKeys);
                vcgDtoList.add(vcgDto);
            }
        } catch(SQLException xSQL) {
            logger.error("NivcgDBUtils.resultSetDtoListConvert() failure: ", xSQL);
        }

        if(vcgDtoList.isEmpty()) {
            vcgDtoList = Collections.emptyList();
        } else {
            vcgDtoList = collapseApiKeys(vcgDtoList);
        }
        return(vcgDtoList);
    }

    /**
     * Collapse api keys on contest root and version
     *
     * @param dtoList - dto list
     * @return List
     */
    private static List<NivcgDto> collapseApiKeys(List<NivcgDto> dtoList) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDbUtils.collapseApiKeys()");
        }

        String current = null;
        String previous = null;
        List<NivcgDto> collapsed = new ArrayList<NivcgDto>();

        collapsed.add(dtoList.get(0));
        for(int i=1; i<dtoList.size(); ++i) {
            previous = String.format("%s/%s", dtoList.get(i-1).getContextRoot(), dtoList.get(i-1).getVersion());
            current = String.format("%s/%s", dtoList.get(i).getContextRoot(), dtoList.get(i).getVersion());

            if(current.equals(previous)) {
                collapsed.get(collapsed.size()-1).getApiKeys().addAll(dtoList.get(i).getApiKeys());
            } else {
                collapsed.add(dtoList.get(i));
            }
        }
        return(collapsed);
    }

    /**
     * Get connection from connection pool using NiJavaLib
     *
     * @return Connection to data tier
     */
    @SuppressWarnings("all")
    private static Connection getDataTierConnection() {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDbUtils.getDataTierConnection()");
        }

        Connection dataTierConnection = null;

        try {
            dataTierConnection = NiStaticConnectionPool.getConnection(NivcgResourceUtils.getFromResourceBundle(DEFAULT_DATA_TIER_DATABASE).toUpperCase());
        } catch(SQLException xSQL) {
            logger.error("DB connection failure: ", xSQL);
        } catch(ClassNotFoundException xCNF) {
            logger.error("DB failure: ", xCNF);
        }
        return(dataTierConnection);
    }

    /**
     * Test data tier
     */
    public static boolean isDataTierAlive() {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDbUtils.isDataTierAlive()");
        }

        return(isDataTierConnectionValid());
    }

    /**
     * Data tier connection validation
     */
    @SuppressWarnings("all")
    private static boolean isDataTierConnectionValid() {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDbUtils.isDataTierConnectionValid()");
        }

        Connection dataTierConnection;

        try {
            dataTierConnection = NiStaticConnectionPool.getConnection(NivcgResourceUtils.getFromResourceBundle(DEFAULT_DATA_TIER_DATABASE).toUpperCase());
            close(null, null, dataTierConnection);
            return(true);
        } catch(SQLException xSQL) {
            logger.error("DB connection failure: ", xSQL);
            return(false);
        } catch(ClassNotFoundException xCNF) {
            logger.error("DB failure: ", xCNF);
            return(false);
        }
    }

    /**
     * Close
     *
     * @param resultSet - result set
     * @param statement - statement
     * @param connection - connection
     */
    private static void close(ResultSet resultSet, Statement statement, Connection connection) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDbUtils.close()");
        }

        if(resultSet != null) {
            try {
                resultSet.close();
            } catch(SQLException xSQL) {
                logger.error("Resultset close failure: ", xSQL);
            }
        }
        if(statement != null) {
            try {
                statement.close();
            } catch(SQLException xSQL) {
                logger.error("Statement close failure: ", xSQL);
            }
        }
        if(connection != null) {
            NiStaticConnectionPool.returnConnection(connection);
            logger.error("Connection close failure: ");
        }
    }
}