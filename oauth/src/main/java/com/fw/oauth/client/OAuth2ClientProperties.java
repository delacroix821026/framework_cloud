package com.fw.oauth.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;

/**
 * OAuth 2.0 client properties.
 *
 * @author Madhura Bhave
 * @author Phillip Webb
 */
@RefreshScope
@ConfigurationProperties(prefix = "fw.security.oauth2.client")
public class OAuth2ClientProperties {

    /**
     * OAuth granter details.
     */
    private final Map<String, Provider> provider = new HashMap<>();

    /**
     * OAuth client registrations.
     */
    private final Map<String, Registration> registration = new HashMap<>();

    public Map<String, Provider> getProvider() {
        return this.provider;
    }

    public Map<String, Registration> getRegistration() {
        return this.registration;
    }

    @PostConstruct
    public void validate() {
        this.getRegistration().values().forEach(this::validateRegistration);
    }

    private void validateRegistration(Registration registration) {
        if (!StringUtils.hasText(registration.getClientId())) {
            throw new IllegalStateException("Client id must not be empty.");
        }
        if (!StringUtils.hasText(registration.getClientSecret())) {
            throw new IllegalStateException("Client secret must not be empty.");
        }
        if(!StringUtils.hasText(registration.getRedirectUriTemplate())) {
            throw new IllegalStateException("Redirect Uri Template must not be empty.");
        }
    }

    /**
     * A single client registration.
     */
    public static class Registration {

        /**
         * Reference to the OAuth 2.0 granter to use. May reference an element from the
         * 'granter' property or used one of the commonly used providers (google, github,
         * facebook, okta).
         */
        private String provider;

        /**
         * Client ID for the registration.
         */
        private String clientId;

        /**
         * Client secret of the registration.
         */
        private String clientSecret;

        /**
         * Client authentication method. May be left blank then using a pre-defined
         * granter.
         */
        private String clientAuthenticationMethod;

        /**
         * Authorization grant type. May be left blank then using a pre-defined granter.
         */
        private String authorizationGrantType;

        /**
         * Redirect URI. May be left blank then using a pre-defined granter.
         */
        private String redirectUriTemplate;

        /**
         * Authorization scopes. May be left blank then using a pre-defined granter.
         */
        private Set<String> scope;

        private Set<String> resourceId;

        private Set<String> authoritie;

        /**
         * Client name. May be left blank then using a pre-defined granter.
         */
        private String clientName;

        private Integer accessTokenValiditySeconds = 300;

        private Integer refreshTokenValiditySeconds = 300;

        public String getProvider() {
            return this.provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getClientId() {
            return this.clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return this.clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getClientAuthenticationMethod() {
            return this.clientAuthenticationMethod;
        }

        public void setClientAuthenticationMethod(String clientAuthenticationMethod) {
            this.clientAuthenticationMethod = clientAuthenticationMethod;
        }

        public String getAuthorizationGrantType() {
            return this.authorizationGrantType;
        }

        public void setAuthorizationGrantType(String authorizationGrantType) {
            this.authorizationGrantType = authorizationGrantType;
        }

        public String getRedirectUriTemplate() {
            return this.redirectUriTemplate;
        }

        public void setRedirectUriTemplate(String redirectUriTemplate) {
            this.redirectUriTemplate = redirectUriTemplate;
        }

        public Set<String> getScope() {
            return this.scope;
        }

        public void setScope(Set<String> scope) {
            this.scope = scope;
        }

        public Set<String> getResourceId() {
            return this.resourceId;
        }

        public void setAuthoritie(Set<String> authoritie) {
            this.authoritie = authoritie;
        }

        public Set<String> getAuthoritie() {
            return this.authoritie;
        }

        public void setResourceId(Set<String> resourceId) {
            this.resourceId = resourceId;
        }

        public String getClientName() {
            return this.clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public Integer getAccessTokenValiditySeconds() {
            return accessTokenValiditySeconds;
        }

        public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
            this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        }

        public Integer getRefreshTokenValiditySeconds() {
            return refreshTokenValiditySeconds;
        }

        public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
            this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        }
    }

    public static class Provider {

        /**
         * Authorization URI for the granter.
         */
        private String authorizationUri;

        /**
         * Token URI for the granter.
         */
        private String tokenUri;

        /**
         * User info URI for the granter.
         */
        private String userInfoUri;

        /**
         * Name of the attribute that will be used to extract the username from the call
         * to 'userInfoUri'.
         */
        private String userNameAttribute;

        /**
         * JWK set URI for the granter.
         */
        private String jwkSetUri;

        public String getAuthorizationUri() {
            return this.authorizationUri;
        }

        public void setAuthorizationUri(String authorizationUri) {
            this.authorizationUri = authorizationUri;
        }

        public String getTokenUri() {
            return this.tokenUri;
        }

        public void setTokenUri(String tokenUri) {
            this.tokenUri = tokenUri;
        }

        public String getUserInfoUri() {
            return this.userInfoUri;
        }

        public void setUserInfoUri(String userInfoUri) {
            this.userInfoUri = userInfoUri;
        }

        public String getUserNameAttribute() {
            return this.userNameAttribute;
        }

        public void setUserNameAttribute(String userNameAttribute) {
            this.userNameAttribute = userNameAttribute;
        }

        public String getJwkSetUri() {
            return this.jwkSetUri;
        }

        public void setJwkSetUri(String jwkSetUri) {
            this.jwkSetUri = jwkSetUri;
        }

    }

}



