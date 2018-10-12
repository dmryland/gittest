package com.ni.apps.soa.nivcg.model;

import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: NINJIUNN
 * Date: 5/15/12
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class NivcgDto {
    private static Logger logger = Logger.getLogger(NivcgDto.class);

    private static final String EMPTY = "";
    private String domain = null;
    private String tier = null;
    private String contextRoot = null;
    private String version = null;
    private Boolean keyEnforced = false;
    private Set<String> apiKeys = null;

    // --

    /**
     * NivcgDto default constructor
     */
    public NivcgDto() {
        this.domain = EMPTY;
        this.tier = EMPTY;
        this.contextRoot = EMPTY;
        this.version = EMPTY;
        this.apiKeys = Collections.emptySet();
    }

    // --

    /**
     * NivcgDto constructor
     */
    public NivcgDto(String domain, String tier, String contextRoot, String version, boolean keyEnforced,  
    	Set<String> apiKeys) {
        this.domain = domain;
        this.tier = tier;
        this.contextRoot = contextRoot;
        this.version = version;
        this.keyEnforced = keyEnforced;
        this.apiKeys = apiKeys;
    }

    // --

    /**
     * Domain getter
     *
     * @return String
     */
    public String getDomain() {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDto.getDomain()");
        }

        return(this.domain.trim());
    }

    /**
     * Domain setter
     *
     * @param domain - domain
     */
    public void setDomain(String domain) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDto.setDomain()");
        }

        if(!isNullorEmpty(domain)) {
            this.domain = domain.trim();
        } else {
            this.domain = EMPTY;
        }
    }

    // --

    /**
     * Tier getter
     *
     * @return String
     */
    public String getTier() {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDto.getTier()");
        }

        return(this.tier.trim());
    }

    /**
     * Tier setter
     *
     * @param tier - tier
     */
    public void setTier(String tier) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDto.setTier()");
        }

        if(!isNullorEmpty(tier)) {
            this.tier = tier.trim();
        } else {
            this.tier = EMPTY;
        }
    }

    // --

    /**
     * Context root getter
     *
     * @return String
     */
    public String getContextRoot() {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDto.getContextRoot()");
        }

        return(this.contextRoot.trim());
    }

    /**
     * Context root setter
     *
     * @param contextRoot - context root
     */
    public void setContextRoot(String contextRoot) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDto.setContextRoot()");
        }

        if(!isNullorEmpty(contextRoot)) {
            this.contextRoot = contextRoot.trim();
        } else {
            this.contextRoot = EMPTY;
        }
    }

    // --

    /**
     * Version getter
     *
     * @return String
     */
    public String getVersion() {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDto.getVersion()");
        }

        return(this.version.trim());
    }

    /**
     * Version setter
     *
     * @param version - version
     */
    public void setVersion(String version) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDto.setVersion()");
        }

        if(!isNullorEmpty(version)) {
            this.version = version.trim();
        } else {
            this.version = EMPTY;
        }
    }

    /**
     * keyEnforced getter
     * 
     * @return keyEnforced
     */
    public Boolean getKeyEnforced() {
		return keyEnforced;
	}
    /**
     * keyEnforced setter
     * 
     * @param keyEnforced - The keyenforced to set
     */
	public void setKeyEnforced(Boolean keyEnforced) {
		this.keyEnforced = keyEnforced;
	}

	/**
     * Api key getter
     *
     * @return String
     */
    public Set<String> getApiKeys() {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDto.getApiKeys()");
        }

        return(this.apiKeys);
    }

    /**
     * Api key setter
     *
     * @param apiKeys - api keys
     */
    public void setApiKeys(Set<String> apiKeys) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDto.setApiKeys()");
        }

        if(!apiKeys.isEmpty()) {
            this.apiKeys = apiKeys;
        } else {
            this.apiKeys = Collections.emptySet();
        }
    }

    // --

    /**
     * Set all
     *
     * @param domain - domain
     * @param tier - tier
     * @param contextRoot - context root
     * @param version - verison
     * @param keyEnforced - API key enforced
     * @param apiKeys - api key
     */
    public void setAll(String domain, String tier, String contextRoot, String version, Boolean keyEnforced, 
    		Set<String> apiKeys) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDto.setAll()");
        }

        if(!isNullorEmpty(domain.trim())) {
            setDomain(domain.trim());
        }
        if(!isNullorEmpty(tier.trim())) {
            setTier(tier.trim());
        }
        if(!isNullorEmpty(contextRoot.trim())) {
            setContextRoot(contextRoot.trim());
        }
        if(!isNullorEmpty(version.trim())) {
            setVersion(version.trim());
        }
        if(!apiKeys.isEmpty()) {
            setApiKeys(apiKeys);
        }
        this.keyEnforced = keyEnforced;
    }

    // --

    /**
     * Test if string is null or empty
     *
     * @param string - string
     * @return boolean
     */
    private static boolean isNullorEmpty(String string) {
        if(logger.isDebugEnabled()) {
            logger.debug("Executing NivcgDbUtils.isNullorEmpty()");
        }

        if(string == null || "".equals(string)) {
            return(true);
        }
        return(false);
    }
}