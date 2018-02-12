package models;

import config.IConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by athakur on 2/11/2018.
 */
@Getter
@Setter
public class SFDCConfig implements IConfig {

    @Field("cdnDomain")
    private String cdnDomain;
    @Field("cdnVersion")
    private String cdnVersion;
    @Field("orgid")
    private String orgID;
    @Field("sfdc_apiVersion")
    private String sfdcAPIVersion;
    @Field("sfdc_managedPackage")
    private String sfdcManagedPackage;
    @Field("sfdc_nameSpace")
    private String sfdcNameSpace;
    @Field("sfdc_orgId")
    private String sfdcOrgID;
    @Field("sfdc_partnerUrl")
    private String sfdcPartnerUrl;
    @Field("sfdc_password")
    private String sfdcPassword;
    @Field("sfdc_setupGainsightApp")
    private String sfdcSetupGainsightApp;
    @Field("sfdc_siteCustomURL")
    private String sfdcSiteCustomURL;
    @Field("sfdc_stoken")
    private String sfdcSToken;
    @Field("sfdc_username")
    private String sfdcUsername;
    @Field("tenant_user")
    private String tenantUser;

    @Tolerate
    public SFDCConfig() {

    }
}
