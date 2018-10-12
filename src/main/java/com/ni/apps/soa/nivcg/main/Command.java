package com.ni.apps.soa.nivcg.main;

import com.ni.apps.soa.nivcg.model.NivcgDto;
import com.ni.apps.soa.nivcg.utils.NivcgDbUtils;
import com.ni.apps.soa.nivcg.utils.NivcgFileUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static com.ni.apps.soa.nivcg.constants.NivcgConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: NINJIUNN
 * Date: 5/11/12
 * Time: 8:29 AM
 * To change this template use File | Settings | File Templates.
 */
public final class Command {
    private static Logger logger = Logger.getLogger(Command.class); #test

    private List<String> domainList;
    private List<NivcgDto> vcgDtoList;

    // --

    private Command() {
    }

    // --

    /**
     * Get domain list
     *
     * @return List<String>
     */
    private List<String> getDomainList() {
        return(this.domainList);
    }

    /**
     * Set domain list
     *
     * @param list - list
     */
    private void setDomainList(List<String> list) {
        if(list != null && !list.isEmpty()) {
            this.domainList = list;
        } else {
            this.vcgDtoList = Collections.emptyList();
        }
    }

    // --

    /**
     * vcgDtoList getter
     *
     * @return List<NivcgDto>
     */
    private List<NivcgDto> getVcgDtoList() {
        return(this.vcgDtoList);
    }

    /**
     * vcgDtoList setter
     *
     * @param vcgDtoList - dto list
     */
    private void setVcgDtoList(List<NivcgDto> vcgDtoList) {
        if(vcgDtoList != null && !vcgDtoList.isEmpty()) {
            this.vcgDtoList = vcgDtoList;
        } else {
            this.vcgDtoList = Collections.emptyList();
        }
    }

    // --

    /**
     * Generate domain list
     */
    private void genDomainList() {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing Command.genDomainList()");
        }

        if(NivcgDbUtils.isDataTierAlive()) {
            setDomainList(new ArrayList<String>());
            setDomainList(NivcgDbUtils.getDomains());
        } else {
            logger.error("Command.genDomainList() DB error");
        }
    }

    // --

    /**
     * Generate dto list
     *
     * @param domain - domain
     * @param tier - tier
     */
    private void genVcgDtoList(String domain, String tier) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing Command.genVcgDtoList()");
        }

        if(NivcgDbUtils.isDataTierAlive()) {
            setVcgDtoList(new ArrayList<NivcgDto>());
            setVcgDtoList(NivcgDbUtils.getConfigs(domain, tier));
        } else {
            logger.error("Command.genVcgDtoList() DB error");
        }
    }

    // --

    /**
     * Generate vcl files
     */
    private void genVcgFile(String domain, String tier) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing Command.genVcgFile()");
        }

        NivcgFileUtils.writeVcgConfig(domain, tier, getVcgDtoList());
    }

    // --

    /**
     * Print help
     */
    @SuppressWarnings("all")
    private static void printHelp() {
        System.out.println("\nUsage: java -jar nicvg.jar [args]");
        System.out.println("\n\targs:");
        System.out.println("\n\t\tdev - generate current DEV Varnish configs");
        System.out.println("\n\t\tdev2 - generate current DEV2 Varnish configs");
        System.out.println("\n\t\ttest - generate current TEST Varnish configs");
        System.out.println("\n\t\ttest2 - generate current TEST2 Varnish configs");
        System.out.println("\n\t\tprod - generate current PROD Varnish configs");
        System.out.println("\n\t\tall - generate ALL current Varnish configs");
    }

    // --

    /**
     * Valid only for primary, secondary, and tertiary tiers
     *
     * @param param - command line argument
     * @return boolean
     */
    private static boolean isValid(String param) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing Command.isValid()");
        }

        if(Pattern.compile(TIER_PATTERN, Pattern.CASE_INSENSITIVE).matcher(param).matches()) {
            return(true);
        } else {
            return(false);
        }
    }

    // --

    /**
     * Command main() runnable
     */
    public static void main(String[] args) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing Command.main()");
        }

        if(args.length > 0) {
            Command control = new Command();
            control.genDomainList();

            for(String domain : control.getDomainList()) {
                for(String argsItem : args) {
                    if(isValid(argsItem)) {
                        control.genVcgDtoList(domain, argsItem);
                        control.genVcgFile(domain, argsItem);
                    } else if(argsItem.equals("all")) {
                        List<String> tiers = new ArrayList<String>() {{
                            add(DEV);
                            add(DEV2);
                            add(TEST);
                            add(TEST2);
                            add(PROD);
                        }};

                        for(String tier : tiers) {
                            control.genVcgDtoList(domain, tier);
                            control.genVcgFile(domain, tier);
                        }
                    } else {
                        printHelp();
                    }
                }
            }
        } else {
            printHelp();
        }
    }
}