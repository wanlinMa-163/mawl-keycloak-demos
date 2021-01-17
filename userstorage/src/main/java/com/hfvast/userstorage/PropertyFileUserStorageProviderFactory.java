package com.hfvast.userstorage;

import org.keycloak.Config;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author mawl
 */
public class PropertyFileUserStorageProviderFactory implements UserStorageProviderFactory<PropertyFileUserStorageProvider> {
    public static final String PROVIDER_NAME = "readonly-property-file";
    private static final Logger logger = Logger.getLogger(String.valueOf(PropertyFileUserStorageProviderFactory.class));
    protected Properties properties = new Properties();

    @Override
    public void init(Config.Scope config) {
        InputStream is = getClass().getClassLoader().getResourceAsStream("/users.properties");
        if (is == null) {
            logger.warning("未找到users.properties");
        } else {
            try {
                logger.warning("加载users.properties");
                properties.load(is);
                logger.warning("加载users.properties完成");
            } catch (IOException ex) {
                logger.severe("Failed to load users.properties file"+ ex);
            }
        }
    }

    public void init() {

    }

    @Override
    public PropertyFileUserStorageProvider create(KeycloakSession session, ComponentModel model) {
       return new PropertyFileUserStorageProvider(session, model, properties);
    }

    @Override
    public String getId() {
        return PROVIDER_NAME;
    }
}
