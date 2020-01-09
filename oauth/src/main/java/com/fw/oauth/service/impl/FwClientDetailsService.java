package com.fw.oauth.service.impl;

import com.fw.oauth.client.OAuth2ClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@EnableConfigurationProperties(OAuth2ClientProperties.class)
@Service
@Slf4j
public class FwClientDetailsService implements ClientDetailsService {
    @Autowired
    private OAuth2ClientProperties properties;

    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        log.debug("AuthConfigration configure:enter " + properties.getRegistration().size());

        if (properties.getRegistration().containsKey(clientId)) {
            ClientBuilder builder = new ClientBuilder(clientId);
            OAuth2ClientProperties.Registration registration = properties.getRegistration().get(clientId);

            int count = 0;
            String[] scopes = new String[0];
            if (registration.getScope() != null) {
                scopes = new String[registration.getScope().size()];
                for (Iterator<String> ite = registration.getScope().iterator(); ite.hasNext(); count++) {
                    String scope = ite.next();
                    scopes[count] = scope;
                }
            }

            String[] resourceIds = new String[0];
            if (registration.getResourceId() != null) {
                resourceIds = new String[registration.getResourceId().size()];
                count = 0;
                for (Iterator<String> ite = registration.getResourceId().iterator(); ite.hasNext(); count++) {
                    String resourceId = ite.next();
                    resourceIds[count] = resourceId;
                }
            }

            String[] authorities = new String[0];
            if (registration.getAuthoritie() != null) {
                authorities = new String[registration.getAuthoritie().size()];
                count = 0;
                for (Iterator<String> ite = registration.getAuthoritie().iterator(); ite.hasNext(); count++) {
                    String authoritie = ite.next();
                    authorities[count] = authoritie;
                }
            }

            builder
                    .resourceIds(resourceIds)
                    .authorizedGrantTypes(registration.getAuthorizationGrantType().split(","))
                    .autoApprove(true)
                    .scopes(scopes)
                    .authorities(authorities)
                    .secret(registration.getClientSecret())
                    .redirectUris(registration.getRedirectUriTemplate().split(","))
                    .refreshTokenValiditySeconds(registration.getRefreshTokenValiditySeconds())
                    .accessTokenValiditySeconds(registration.getAccessTokenValiditySeconds());
            return builder.build();
        }

        return null;
    }

    public final class ClientBuilder {
        private final String clientId;

        private Collection<String> authorizedGrantTypes = new LinkedHashSet<String>();

        private Collection<String> authorities = new LinkedHashSet<String>();

        private Integer accessTokenValiditySeconds;

        private Integer refreshTokenValiditySeconds;

        private Collection<String> scopes = new LinkedHashSet<String>();

        private Collection<String> autoApproveScopes = new HashSet<String>();

        private String secret;

        private Set<String> registeredRedirectUris = new HashSet<String>();

        private Set<String> resourceIds = new HashSet<String>();

        private boolean autoApprove;

        private Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>();

        private ClientDetails build() {
            BaseClientDetails result = new BaseClientDetails();
            result.setClientId(clientId);
            result.setAuthorizedGrantTypes(authorizedGrantTypes);
            result.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
            result.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
            result.setRegisteredRedirectUri(registeredRedirectUris);
            result.setClientSecret(secret);
            result.setScope(scopes);
            result.setAuthorities(AuthorityUtils.createAuthorityList(authorities.toArray(new String[authorities.size()])));
            result.setResourceIds(resourceIds);
            result.setAdditionalInformation(additionalInformation);
            if (autoApprove) {
                result.setAutoApproveScopes(scopes);
            } else {
                result.setAutoApproveScopes(autoApproveScopes);
            }
            return result;
        }

        public ClientBuilder resourceIds(String... resourceIds) {
            for (String resourceId : resourceIds) {
                this.resourceIds.add(resourceId);
            }
            return this;
        }

        public ClientBuilder redirectUris(String... registeredRedirectUris) {
            for (String redirectUri : registeredRedirectUris) {
                this.registeredRedirectUris.add(redirectUri);
            }
            return this;
        }

        public ClientBuilder authorizedGrantTypes(String... authorizedGrantTypes) {
            for (String grant : authorizedGrantTypes) {
                this.authorizedGrantTypes.add(grant);
            }
            return this;
        }

        public ClientBuilder accessTokenValiditySeconds(int accessTokenValiditySeconds) {
            this.accessTokenValiditySeconds = accessTokenValiditySeconds;
            return this;
        }

        public ClientBuilder refreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
            this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
            return this;
        }

        public ClientBuilder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public ClientBuilder scopes(String... scopes) {
            for (String scope : scopes) {
                this.scopes.add(scope);
            }
            return this;
        }

        public ClientBuilder authorities(String... authorities) {
            for (String authority : authorities) {
                this.authorities.add(authority);
            }
            return this;
        }

        public ClientBuilder autoApprove(boolean autoApprove) {
            this.autoApprove = autoApprove;
            return this;
        }

        public ClientBuilder autoApprove(String... scopes) {
            for (String scope : scopes) {
                this.autoApproveScopes.add(scope);
            }
            return this;
        }

        public ClientBuilder additionalInformation(Map<String, ?> map) {
            this.additionalInformation.putAll(map);
            return this;
        }

        public ClientBuilder additionalInformation(String... pairs) {
            for (String pair : pairs) {
                String separator = ":";
                if (!pair.contains(separator) && pair.contains("=")) {
                    separator = "=";
                }
                int index = pair.indexOf(separator);
                String key = pair.substring(0, index > 0 ? index : pair.length());
                String value = index > 0 ? pair.substring(index + 1) : null;
                this.additionalInformation.put(key, (Object) value);
            }
            return this;
        }

        private ClientBuilder(String clientId) {
            this.clientId = clientId;
        }

    }
}
