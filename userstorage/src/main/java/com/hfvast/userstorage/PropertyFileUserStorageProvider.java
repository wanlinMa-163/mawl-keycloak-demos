package com.hfvast.userstorage;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.ReadOnlyException;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.user.UserLookupProvider;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author mawl
 */
public class PropertyFileUserStorageProvider implements UserStorageProvider,
        UserLookupProvider,
        CredentialInputValidator,
        CredentialInputUpdater {
    private static final Logger logger = Logger.getLogger(String.valueOf(PropertyFileUserStorageProvider.class));
    protected KeycloakSession session;
    protected Properties properties;
    protected ComponentModel model;
    // map of loaded users in this transaction
    protected Map<String, UserModel> loadedUsers = new HashMap<>();

    public PropertyFileUserStorageProvider(KeycloakSession session, ComponentModel model, Properties properties) {
        this.session = session;
        this.model = model;
        this.properties = properties;
        logger.info("构造函数 PropertyFileUserStorageProvider ");
    }

    @Override
    public void close() {

    }

    // UserLookupProvider
    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        logger.info("UserModel getUserById...");
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();
        logger.info("UserModel getUserById,username" + username);
        return getUserByUsername(username, realm);
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        UserModel adapter = loadedUsers.get(username);
        if (adapter == null) {
            logger.info("UserModel getUserByUsername" + adapter);
            String password = properties.getProperty(username);
            logger.info("UserModel getUserByUsername password : " + password);
            if (password != null) {
                logger.info("UserModel getUserByUsername password!=null : " + password);
                adapter = createAdapter(realm, username);
                loadedUsers.put(username, adapter);
            }
        }
        return adapter;
    }

    protected UserModel createAdapter(RealmModel realm, String username) {
        return new AbstractUserAdapter(session, realm, model) {
            @Override
            public String getUsername() {
                return username;
            }
        };
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        return null;
    }

    //  CredentialInputValidator
    @Override
    public boolean supportsCredentialType(String credentialType) {
        return credentialType.equals(CredentialModel.PASSWORD);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        String password = properties.getProperty(user.getUsername());
        logger.info("boolean isConfiguredFor password: " + password);
        return credentialType.equals(CredentialModel.PASSWORD) && password != null;
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        if (!supportsCredentialType(input.getType())) {
            return false;
        }
        String password = properties.getProperty(user.getUsername());
        logger.info("boolean isValid password: " + password);
        if (password == null) return false;
        return password.equals(input.getChallengeResponse());
    }
    // CredentialInputUpdater

    @Override
    public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
        if (input.getType().equals(CredentialModel.PASSWORD))
            throw new ReadOnlyException("user is read only for this update");

        return false;
    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {

    }

    @Override
    public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel user) {
        return Collections.EMPTY_SET;
    }

}
