/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/

// Support Hibernate annotations



// Note: Most of the dataSource configuration resides in resources.groovy and in the
// installation-specific configuration file (see Config.groovy for the include).

dataSource {
   
    dialect = "org.hibernate.dialect.Oracle10gDialect"
    loggingSql = false

}


hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
   	hbm2ddl.auto = null
   	show_sql = false
//   	naming_strategy = "org.hibernate.cfg.ImprovedNamingStrategy"
   	dialect = "org.hibernate.dialect.Oracle10gDialect"
    config.location = [
            "classpath:hibernate-banner-core.cfg.xml"
    ]
}


// environment specific settings
environments {
    development {
        dataSource {
        }
    }
    test {
        dataSource {
        }
    }
    production {
        dataSource {
        }
    }
}