/*
 * jPOS Project [http://jpos.org]
 * Copyright (C) 2000-2021 jPOS Software SRL
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jpos.flyway;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.logging.Log;
import org.flywaydb.core.api.logging.LogCreator;
import org.flywaydb.core.api.logging.LogFactory;
import org.flywaydb.core.internal.configuration.ConfigUtils;
import org.jpos.core.Environment;
import org.jpos.ee.DB;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FlywaySupport implements LogCreator, Log {

    protected Flyway getFlyway(String configModifier, String args[]) {
        LogFactory.setFallbackLogCreator(this);
        Properties p = new DB(configModifier).getProperties();
        FluentConfiguration config = Flyway.configure()
                .configuration(getConfigurationProperties())
                .dataSource(
                        p.getProperty("hibernate.connection.url"),
                        p.getProperty("hibernate.connection.username"),
                        p.getProperty("hibernate.connection.password"))
                .outOfOrder(has(args, "--out-of-order"));
        return config.load();
    }

    public Log createLogger(Class<?> clazz) {
        return this;
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public void debug(String message) {
        System.out.println("DEBUG: " + message);
    }

    @Override
    public void info(String message) {
        System.out.println(message);
    }

    @Override
    public void warn(String message) {
        System.err.println("WARNING: " + message);
    }

    @Override
    public void error(String message) {
        System.err.println("ERROR: " + message);
    }

    @Override
    public void error(String message, Exception e) {
        System.err.println("ERROR: " + message);
        e.printStackTrace(System.err);
    }

    @Override
    public void notice(String message) {
        System.out.println("NOTICE: " + message);
    }

    private boolean has(String[] args, String arg) {
        return Arrays.asList(args).contains(arg);
    }

    private Map<String, String> getConfigurationProperties() {
        String[] names = new String[]{
          ConfigUtils.CONFIG_FILE_NAME,
          ConfigUtils.CONFIG_FILES,
          ConfigUtils.CONFIG_FILE_ENCODING,
          ConfigUtils.BASELINE_DESCRIPTION,
          ConfigUtils.BASELINE_ON_MIGRATE,
          ConfigUtils.BASELINE_VERSION,
          ConfigUtils.BATCH,
          ConfigUtils.CALLBACKS,
          ConfigUtils.CLEAN_DISABLED,
          ConfigUtils.CLEAN_ON_VALIDATION_ERROR,
          ConfigUtils.CONNECT_RETRIES,
          ConfigUtils.CONNECT_RETRIES_INTERVAL,
          ConfigUtils.DEFAULT_SCHEMA,
          ConfigUtils.DRIVER,
          ConfigUtils.DRYRUN_OUTPUT,
          ConfigUtils.ENCODING,
          ConfigUtils.DETECT_ENCODING,
          ConfigUtils.ERROR_OVERRIDES,
          ConfigUtils.GROUP,
          ConfigUtils.IGNORE_MIGRATION_PATTERNS,
          ConfigUtils.INIT_SQL,
          ConfigUtils.INSTALLED_BY,
          ConfigUtils.LICENSE_KEY,
          ConfigUtils.LOCATIONS,
          ConfigUtils.MIXED,
          ConfigUtils.OUT_OF_ORDER,
          ConfigUtils.SKIP_EXECUTING_MIGRATIONS,
          ConfigUtils.OUTPUT_QUERY_RESULTS,
          ConfigUtils.PASSWORD,
          ConfigUtils.PLACEHOLDER_PREFIX,
          ConfigUtils.PLACEHOLDER_REPLACEMENT,
          ConfigUtils.PLACEHOLDER_SUFFIX,
          ConfigUtils.PLACEHOLDER_SEPARATOR,
          ConfigUtils.SCRIPT_PLACEHOLDER_PREFIX,
          ConfigUtils.SCRIPT_PLACEHOLDER_SUFFIX,
          ConfigUtils.PLACEHOLDERS_PROPERTY_PREFIX,
          ConfigUtils.LOCK_RETRY_COUNT,
          ConfigUtils.JDBC_PROPERTIES_PREFIX,
          ConfigUtils.REPEATABLE_SQL_MIGRATION_PREFIX,
          ConfigUtils.RESOLVERS,
          ConfigUtils.SCHEMAS,
          ConfigUtils.SKIP_DEFAULT_CALLBACKS,
          ConfigUtils.SKIP_DEFAULT_RESOLVERS,
          ConfigUtils.SQL_MIGRATION_PREFIX,
          ConfigUtils.SQL_MIGRATION_SEPARATOR,
          ConfigUtils.SQL_MIGRATION_SUFFIXES,
          ConfigUtils.STREAM,
          ConfigUtils.TABLE,
          ConfigUtils.TABLESPACE,
          ConfigUtils.TARGET,
          ConfigUtils.CHERRY_PICK,
          ConfigUtils.UNDO_SQL_MIGRATION_PREFIX,
          ConfigUtils.URL,
          ConfigUtils.USER,
          ConfigUtils.VALIDATE_ON_MIGRATE,
          ConfigUtils.VALIDATE_MIGRATION_NAMING,
          ConfigUtils.CREATE_SCHEMAS,
          ConfigUtils.FAIL_ON_MISSING_LOCATIONS,
          ConfigUtils.LOGGERS,
          ConfigUtils.KERBEROS_CONFIG_FILE,
          ConfigUtils.ORACLE_SQLPLUS,
          ConfigUtils.ORACLE_SQLPLUS_WARN,
          ConfigUtils.ORACLE_KERBEROS_CACHE_FILE,
          ConfigUtils.ORACLE_WALLET_LOCATION,
          ConfigUtils.JAR_DIRS,
          ConfigUtils.CONFIGURATIONS,
          ConfigUtils.FLYWAY_PLUGINS_PREFIX
        };
        Map<String, String> props = new HashMap<>();
        props.put(ConfigUtils.CLEAN_DISABLED, "true");
        for (String name : names) {
            String v = Environment.get(String.format("${%s}", name), null);
            if (v != null) {
                props.put(name, v);
            }
        }
        return props;
    }
}
