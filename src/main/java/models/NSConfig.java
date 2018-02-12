package models;

import config.IConfig;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@EqualsAndHashCode
public class NSConfig implements IConfig {

    @Field("datascience_host")
    private String dataScienceHost;
    @Field("elasticSearch_host")
    private String elasticSearchHost;
    @Field("externalSharing_host")
    private String externalSharingHost;
    @Field("gatewayUrl")
    private String gatewayUrl;
    @Field("hapostgres_dbName")
    private String haPostgresDBName;
    @Field("hapostgres_host")
    private String haPostgresHost;
    @Field("hapostgres_password")
    private String haPostgresPassword;
    @Field("hapostgres_sslEnabled")
    private String haPostgresSSLEnabled;
    @Field("hapostgres_username")
    private String haPostgresUsername;
    @Field("mandrill_webhook_secret")
    private String mandrillWebhookSecret;
    @Field("mongo_auth_db")
    private String mongoAuthDB;
    @Field("mongo_auth_host")
    private String mongoAuthHost;
    @Field("mongo_auth_password")
    private String mongoAuthPassword;
    @Field("mongo_auth_port")
    private String mongoAuthPort;
    @Field("mongo_auth_ssl_enable")
    private String mongoAuthSSLEnable;
    @Field("mongo_auth_username")
    private String mongoAuthUsername;
    @Field("mongo_global_db")
    private String mongoGlobalDB;
    @Field("mongo_global_host")
    private String mongoGlobalHost;
    @Field("mongo_global_password")
    private String mongoGlobalPassword;
    @Field("mongo_global_port")
    private String mongoGlobalPort;
    @Field("mongo_global_ssl_enable")
    private String mongoGlobalSSLEnable;
    @Field("mongo_global_username")
    private String mongoGlobalUsername;
    @Field("mongo_sally_db")
    private String mongoSallyDB;
    @Field("mongo_sally_host")
    private String mongoSallyHost;
    @Field("mongo_sally_password")
    private String mongoSallyPassword;
    @Field("mongo_sally_port")
    private String mongoSallyPort;
    @Field("mongo_sally_ssl_enable")
    private String mongoSallySSLEnable;
    @Field("mongo_sally_username")
    private String mongoSallyUsername;
    @Field("mongo_scheduler_db")
    private String mongoSchedulerDB;
    @Field("mongo_scheduler_host")
    private String mongoSchedulerHost;
    @Field("mongo_scheduler_password")
    private String mongoSchedulerPassword;
    @Field("mongo_scheduler_port")
    private String mongoSchedulerPort;
    @Field("mongo_scheduler_ssl_enable")
    private String mongoSchedulerSSLEnable;
    @Field("mongo_scheduler_username")
    private String mongoSchedulerUsername;
    @Field("nlu_host")
    private String nluHost;
    @Field("ns_adminUrl")
    private String nsAdminUrl;
    @Field("ns_appUrl")
    private String nsAppUrl;
    @Field("ns_sfdc_apiVersion")
    private String nsSFDCAPIVersion;
    @Field("ns_sfdc_partnerUrl")
    private String nsSFDCPartnerUrl;
    @Field("ns_sfdc_password")
    private String nsSFDCPassword;
    @Field("ns_sfdc_stoken")
    private String nsSFDCSToken;
    @Field("ns_sfdc_username")
    private String nsSFDCUsername;
    @Field("ns_version")
    private String nsVersion;
    @Field("orchestrationEngineUrl")
    private String orchestrationEngineUrl;
    @Field("query_api_host")
    private String queryAPIHost;
    @Field("redshift_dbName")
    private String redshiftDBName;
    @Field("redshift_host")
    private String redshiftHost;
    @Field("redshift_password")
    private String redshiftPassword;
    @Field("redshift_sslEnabled")
    private String redshiftSSLEnabled;
    @Field("redshift_username")
    private String redshiftUsername;
    @Field("scribble_host")
    private String scribbleHost;

    @Tolerate
    public NSConfig() {
    }
}