package com.ni.apps.soa.nivcg.utils;

import com.ni.apps.soa.nivcg.model.NivcgDto;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static com.ni.apps.soa.nivcg.constants.NivcgConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: NINJIUNN
 * Date: 5/14/12
 * Time: 9:15 AM
 * To change this template use File | Settings | File Templates.
 */
public final class NivcgFileUtils {
    private static Logger logger = Logger.getLogger(NivcgFileUtils.class);

    private NivcgFileUtils() {
    }

    /**
     * Write vcl files
     *
     * @param vcgDtoList - dto list
     */
    @SuppressWarnings("all")
    public static void writeVcgConfig(String domain, String tier, List<NivcgDto> vcgDtoList) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NiVcgFileUtils.writeVcgConfig()");
        }

        final String charset = "UTF-8";
        String filename = NivcgResourceUtils.getFromResourceBundle(DEFAULT_VARNISH_CONFIG_FILENAME).replaceAll(REPLACE_DOMAIN, domain).replaceAll(REPLACE_TIER, tier);
        String info = null;
        String svc = null;
        String apikeys = null;
        String vcgClause = null;
        int i = 1;
        File vcgConfigFile = new File(filename);
        FileOutputStream fos = null;

        if(vcgDtoList != null && !vcgDtoList.isEmpty()) {
            if(new File(filename).delete()) { logger.info("File: " + filename + "deleted"); }

            try {
                info = String.format("# %s%n# %s%n# %s%n%n", vcgConfigFile.getName(), NivcgResourceUtils.getFromResourceBundle(DEFAULT_APPLICATION_TIER).replaceAll(REPLACE_TIER, tier.toUpperCase()), rightNow());
                fos = new FileOutputStream(vcgConfigFile);
                fos.write(info.getBytes(charset));
                fos.write(getSchnell(domain).getBytes(charset));
                String theString = null;

                for(NivcgDto dtoItem : vcgDtoList) {
                    svc = String.format("# no. %s%n# context root: /%s%n# version: %s%n", String.valueOf(i), dtoItem.getContextRoot(), dtoItem.getVersion());
                    apikeys = getApiKeyComments(dtoItem.getApiKeys());
                    
                    if (dtoItem.getKeyEnforced()) {
                    	theString = "if(req.url ~ \"^/%s/%s\")%n{%n\tif(%s)%n\t{%n\t\terror 403 \"NIVCG-Forbidden\";%n\t}%n}%n%n";
                    } else {
                        theString = "if(req.url ~ \"^/%s/%s\")%n{%n\tif(%s)%n\t{%n\t\t# error 403 \"NIVCG-Forbidden\";%n\t}%n}%n%n";
                    }
                    if (i > 1) theString = "els" + theString;
                    vcgClause = String.format(theString, dtoItem.getContextRoot(), dtoItem.getVersion(), getApiKeyHeader(dtoItem.getApiKeys()));
 
                    fos.write(svc.getBytes(charset));
                    fos.write(apikeys.getBytes(charset));
                    fos.write(vcgClause.getBytes(charset));
                    fos.flush();
                    ++i;
                }
            } catch(FileNotFoundException xFNF) {
                logger.error("File create failure: " + vcgConfigFile.getName(), xFNF);
            } catch(IOException xIO) {
                logger.error("File write failure: " + vcgConfigFile.getName(), xIO);
            } finally {
                streamCloser(fos);
            }
        }
    }

    /**
     * Write Schnell configs for nisoars
     *
     * @param domain - domain
     * @return String
     */
    @SuppressWarnings("all")
    private static String getSchnell(String domain) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NiVcgFileUtils.getSchnell()");
        }

        final String noDomain = "<no domain found>";
        final String warn = "# *************************************\r\n# ADMIN SECTION: DO NOT EDIT OR DELETE!\r\n# *************************************\r\n\r\n";
        final String end = "# *************************************************************************************************************************************************************\r\n\r\n";

        String service = null;
        String client = null;
        String nicm = null;

        if(Pattern.compile(IMMIX).matcher(isNullorEmpty(domain) ? noDomain : domain).matches()) {
            service = "if(req.url ~ \"^/nisoars/1/service\")\r\n{\r\n\tif(req.http.X-NI-API-KEY != \"a09dfe47-0311-404c-8365-6f254438ff34\")\r\n\t{\r\n\t\tset req.url = \"" + IMMIX_ERROR + "\";\r\n\t}\r\n}\r\n\r\n";
            client = "if(req.url ~ \"^/nisoars/1/client\")\r\n{\r\n\tif(req.http.X-NI-API-KEY != \"a09dfe47-0311-404c-8365-6f254438ff34\")\r\n\t{\r\n\t\tset req.url = \"" + IMMIX_ERROR + "\";\r\n\t}\r\n}\r\n\r\n";
            nicm = "if(req.url ~ \"^/nisoars/1/nicm\")\r\n{\r\n\tif(req.http.X-NI-API-KEY != \"a09dfe47-0311-404c-8365-6f254438ff34\")\r\n\t{\r\n\t\tset req.url = \"" + IMMIX_ERROR + "\";\r\n\t}\r\n}\r\n\r\n";
            return(warn + service + client + nicm + end);
        } else if(Pattern.compile(FLUX).matcher(isNullorEmpty(domain) ? noDomain : domain).matches()) {
            service = "if(req.url ~ \"^/nisoars/1/service\")\r\n{\r\n\tif(req.http.X-NI-API-KEY != \"a09dfe47-0311-404c-8365-6f254438ff34\")\r\n\t{\r\n\t\tset req.url = \"" + FLUX_ERROR + "\";\r\n\t}\r\n}\r\n\r\n";
            client = "if(req.url ~ \"^/nisoars/1/client\")\r\n{\r\n\tif(req.http.X-NI-API-KEY != \"a09dfe47-0311-404c-8365-6f254438ff34\")\r\n\t{\r\n\t\tset req.url = \"" + FLUX_ERROR + "\";\r\n\t}\r\n}\r\n\r\n";
            nicm = "if(req.url ~ \"^/nisoars/1/nicm\")\r\n{\r\n\tif(req.http.X-NI-API-KEY != \"a09dfe47-0311-404c-8365-6f254438ff34\")\r\n\t{\r\n\t\tset req.url = \"" + FLUX_ERROR + "\";\r\n\t}\r\n}\r\n\r\n";
            return(warn + service + client + nicm + end);
        } else {
            return(warn + end);
        }
    }

    /**
     * Return # api key: <KEY>
     *
     * @param set - set
     * @return String
     */
    @SuppressWarnings("all")
    private static String getApiKeyComments(Set<String> set) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NiVcgFileUtils.getApiKeys()");
        }

        StringBuilder buffer = new StringBuilder("");
        for(String item : set) {
            buffer = buffer.append("# api key: ").append(item).append("\r\n");
        }
        return(buffer.toString());
    }

    /**
     * Return req.http.X-NI-API-KEY != "<KEY>" && ...
     *
     * @param set - set
     * @return String
     */
    @SuppressWarnings("all")
    private static String getApiKeyHeader(Set<String> set) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NiVcgFileUtils.getApiKeyHeaders()");
        }

        StringBuilder buffer = new StringBuilder("");
        for(String item : set) {
            buffer = buffer.append("req.http.X-NI-API-KEY != ").append('\"').append(item).append('\"').append(" && ");
        }
        buffer = new StringBuilder(buffer.substring(0, buffer.length() - " && ".length()));

        /*
        if(set.size() > 1) {
            buffer = new StringBuilder(buffer).insert(0, "(").append(")");
        }
        */
        return(buffer.toString());
    }

    /**
     * Get domain specific error page
     *
     * @param domain - domain
     * @return String
     */
    private static String getErrorPage(String domain) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NiVcgFileUtils.getErrorPage()");
        }

        if(domain.equals(IMMIX)) {
            return(IMMIX_ERROR);
        } else if(domain.equals(FLUX)) {
            return(FLUX_ERROR);
        } else if(domain.equals(SINE)) {
            return(SINE_ERROR);
        } else {
            return(VENUS_ERROR);
        }
    }

    /**
     * Get date & time
     *
     * @return String
     */
    private static String rightNow() {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NiVcgFileUtils.rightNow()");
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return(dateFormat.format(date));
    }

    /**
     * Stream object closer that can be closed
     *
     * @param streamArgument - Stream object to close
     */
    private static void streamCloser(Closeable streamArgument) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NiVcgFileUtils.streamCloser()");
        }

        if(streamArgument != null) {
            try {
                streamArgument.close();
            } catch(IOException xIO) {
                logger.error("NiVcgFileUtils.streamCloser() Stream object close failure: ", xIO);
            }
        }
    }

    /**
     * Test if string is null or empty
     *
     * @param string - string to test
     * @return boolean
     */
    private static boolean isNullorEmpty(String string) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgFileUtils.isNullorEmpty()");
        }

        if(string == null || "".equals(string)) {
            return(true);
        }
        return(false);
    }
}