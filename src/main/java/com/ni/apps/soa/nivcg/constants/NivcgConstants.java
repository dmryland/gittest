package com.ni.apps.soa.nivcg.constants;

/**
 * Created with IntelliJ IDEA.
 * User: NINJIUNN
 * Date: 5/9/12
 * Time: 7:57 AM
 * To change this template use File | Settings | File Templates.
 */
public final class NivcgConstants {
    private NivcgConstants() {
    }

    // patterns
    public static final String TIER_PATTERN = "\\^(dev[2|3]?)\\$|\\^(test[2|3]?)\\$|\\^(prod)\\$";

    // resources
    public static final String DEFAULT_RESOURCE_BUNDLE = "defaultOptions";
    public static final String DEFAULT_DATA_TIER_DATABASE = "DEFAULT_DATA_TIER_DATABASE";
    public static final String DEFAULT_APPLICATION_TIER = "DEFAULT_APPLICATION_TIER";
    public static final String DEFAULT_VARNISH_CONFIG_FILENAME = "DEFAULT_VARNISH_CONFIG_FILENAME";

    // domains
    public static final String IMMIX = "immix";
    public static final String FLUX = "flux";
    public static final String SINE = "sine";
    public static final String VENUS = "venus";

    // sql
    public static final String DEV = "dev";
    public static final String DEV2 = "dev2";
    public static final String TEST = "test";
    public static final String TEST2 = "test2";
    public static final String PROD = "prod";
    public static final String DOMAIN = "domain";
    public static final String TIER = "tier";
    public static final String CONTEXT_ROOT = "context_root";
    public static final String VER = "ver";
    public static final String API_KEY = "api_key";
    public static final String NAME = "name";
    public static final String NISOAR_DOMAIN_MASTER = "select ni.nisoar_domain.nisoar_domain_id, ni.nisoar_domain.name from ni.nisoar_domain";
//    public static final String NISOAR_VCG_MASTER = "select domain, tier, context_root, ver, api_key from (select ws_id, tier, api_key from (select ni.nisoar_client_to_ws.client_id, ni.nisoar_client_to_ws.ws_id from ni.nisoar_client_to_ws where ni.nisoar_client_to_ws.enabled = 'y'), (select nisoar_client_id, api_key, tier from (select ni.nisoar_client.nisoar_client_id, ni.nisoar_client.api_key from ni.nisoar_client), (select client_id, tier_id, name as tier from (select ni.nisoar_tier.nisoar_tier_id, ni.nisoar_tier.name from ni.nisoar_tier), (select ni.nisoar_client_to_tier.client_id, ni.nisoar_client_to_tier.tier_id from ni.nisoar_client_to_tier) where nisoar_tier_id = tier_id) where nisoar_client_id = client_id) where nisoar_client_id = client_id), (select nisoar_ws_id, context_root, ver, domain from (select ni.nisoar_ws.nisoar_ws_id, ni.nisoar_ws.context_root, ni.nisoar_ws.ver from ni.nisoar_ws where ni.nisoar_ws.enabled = 'y'), (select ws_id, domain_id, name as domain from (select ni.nisoar_domain.nisoar_domain_id, ni.nisoar_domain.name from ni.nisoar_domain), (select ni.nisoar_ws_to_domain.ws_id, ni.nisoar_ws_to_domain.domain_id from ni.nisoar_ws_to_domain) where nisoar_domain_id = domain_id) where nisoar_ws_id = ws_id) where nisoar_ws_id = ws_id and domain = ? and tier = ? order by context_root, ver";
    public static final String NISOAR_VCG_MASTER = "select domain, tier, context_root, ver, key_enforced, api_key from (select ws_id, tier, api_key from (select ni.nisoar_client_to_ws.client_id, ni.nisoar_client_to_ws.ws_id from ni.nisoar_client_to_ws where ni.nisoar_client_to_ws.enabled = 'y'), (select nisoar_client_id, api_key, tier from (select ni.nisoar_client.nisoar_client_id, ni.nisoar_client.api_key from ni.nisoar_client), (select client_id, tier_id, name as tier from (select ni.nisoar_tier.nisoar_tier_id, ni.nisoar_tier.name from ni.nisoar_tier), (select ni.nisoar_client_to_tier.client_id, ni.nisoar_client_to_tier.tier_id from ni.nisoar_client_to_tier) where nisoar_tier_id = tier_id) where nisoar_client_id = client_id) where nisoar_client_id = client_id), (select nisoar_ws_id, context_root, ver, key_enforced, domain from (select ni.nisoar_ws.nisoar_ws_id, ni.nisoar_ws.context_root, ni.nisoar_ws.ver, ni.nisoar_ws.key_enforced from ni.nisoar_ws where ni.nisoar_ws.enabled = 'y'), (select ws_id, domain_id, name as domain from (select ni.nisoar_domain.nisoar_domain_id, ni.nisoar_domain.name from ni.nisoar_domain), (select ni.nisoar_ws_to_domain.ws_id, ni.nisoar_ws_to_domain.domain_id from ni.nisoar_ws_to_domain) where nisoar_domain_id = domain_id) where nisoar_ws_id = ws_id) where nisoar_ws_id = ws_id and domain = ? and tier = ? order by context_root, ver";

    // replacements
    public static final String REPLACE_DOMAIN = "<domain>";
    public static final String REPLACE_TIER = "<tier>";

    // errors
    public static final String IMMIX_ERROR = "http://immix/cgi-bin/errorpage.cgi?errorcode=401";
    public static final String FLUX_ERROR = "http://flux/cgi-bin/errorpage.cgi?errorcode=401";
    public static final String SINE_ERROR = "http://sine/cgi-bin/errorpage.cgi?errorcode=401";
    public static final String VENUS_ERROR = "http://venus/cgi-bin/errorpage.cgi?errorcode=401";
}