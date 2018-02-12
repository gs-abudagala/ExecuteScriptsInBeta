package utils;

import com.sforce.async.AsyncApiException;
import com.sforce.async.BulkConnection;
import com.sforce.soap.apex.SoapConnection;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.SessionRenewer;
import models.NSConfig;
import models.SFDCConfig;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Salesforce Connector which will fetch all the possible connections except for Salesforce REST Connection.
 * It includes partner connection, Apex Connection, Metadata Connection, Bulk Connection
 *
 * @author Sunand
 */
public class SalesforceConnector extends BaseSalesforceConnector {
    private PartnerConnection partnerConnection;
    private SoapConnection apexConnection;
    private BulkConnection bulkConnection;
    private MetadataConnection metadataConnection;

    private String username;
    private String password;
    //	private String token;
    private String partnerUrl;
    private LoginResult loginResult;

    //private SFDCInfo sfdcInfo;
    private String API_VERSION = "29.0";


    public SalesforceConnector(String username, String password, String partnerUrl, String API_VERSION) {
        this.username = username;
        this.password = password;
//		this.token = token;
        this.partnerUrl = partnerUrl;
        //sample partner url format for reference --> https://login.salesforce.com/services/Soap/u/29.0
        this.API_VERSION = API_VERSION;
    }

    public static SalesforceConnector get(SFDCConfig sfdcConfig) {
        return new SalesforceConnector(sfdcConfig.getSfdcUsername(),
                sfdcConfig.getSfdcPassword() + sfdcConfig.getSfdcSToken(), sfdcConfig.getSfdcPartnerUrl(), sfdcConfig.getSfdcAPIVersion());
    }

    public static SalesforceConnector get(NSConfig nsConfig) {
        return new SalesforceConnector(nsConfig.getNsSFDCUsername(),
                nsConfig.getNsSFDCPassword() + nsConfig.getNsSFDCSToken(), nsConfig.getNsSFDCPartnerUrl(), nsConfig.getNsSFDCAPIVersion());
    }

    /**
     * Connecting to Salesforce and creating multiple connections all at once
     *
     * @return
     */
    public boolean connect() {
        // Write the connection creation code
        if (isConnected())
            return true;
        else {
            connectToSfdc();
        }
        return isConnected();
    }

    private boolean connectToSfdc() {
        boolean condition = false;
        try {
            //We can keep flag to control what all connections to initialize.
            loginResult = loginToSalesforce(username, password, partnerUrl);
            Log.info("Login Result : " + loginResult.toString());
            metadataConnection = createMetadataConnection(loginResult);
            apexConnection = createApexConnection(loginResult);
            bulkConnection = createBulkConnection(partnerConnection.getConfig());
            fillSfdcInfo(loginResult);
            Log.info("SFDC Connection established");
            condition = true;
        } catch (Exception e) {
            Log.error(e.getLocalizedMessage(), e);
        }
        return condition;
    }

    public boolean isConnected() {
        if (partnerConnection != null && bulkConnection != null && apexConnection != null && metadataConnection != null)
            return true;
        else
            return false;

    }

    public LoginResult getLoginResult() {
        return loginResult;
    }

    @Override
    public PartnerConnection getPartnerConnection() {
        return partnerConnection;
    }

    @Override
    public BulkConnection getBulkConnection() {
        return bulkConnection;
    }

    @Override
    public SoapConnection getApexConnection() {
        return apexConnection;
    }

    @Override
    public MetadataConnection getMetadataConnection() {
        return metadataConnection;
    }

    /**
     * Reconnection to Salesforce if the session has expired.
     * Also establishing all other connections.
     */
    public void reconnect() {
        try {
            Log.info("Creating a Salesforce session using " + partnerConnection.getConfig().getUsername());
            loginResult = partnerConnection.login(partnerConnection.getConfig().getUsername(), partnerConnection.getConfig().getPassword());

            if (loginResult.isPasswordExpired()) {
                try {
                    partnerConnection.logout();
                } catch (ConnectionException e) {
                    Log.error(e.getMessage(), e);
                }
                String username = partnerConnection.getConfig().getUsername();
                partnerConnection = null;
                throw new RuntimeException("The password for the user " + username + " has expired");
            }

            Log.info("Session established successfully with ID " + loginResult.getSessionId() + " at instance " + loginResult.getServerUrl());
            partnerConnection.getSessionHeader().setSessionId(loginResult.getSessionId());
            partnerConnection.getConfig().setServiceEndpoint(loginResult.getServerUrl());
            partnerConnection.getConfig().setSessionId(loginResult.getSessionId());

            //creating other connections
            metadataConnection = createMetadataConnection(loginResult);
            apexConnection = createApexConnection(loginResult);
            bulkConnection = createBulkConnection(partnerConnection.getConfig());
            fillSfdcInfo(loginResult);
        } catch (ConnectionException e) {
            Log.error(e.getMessage(), e);
        }
    }

    /**
     * Salesforce Connector Config which needs to be configured before one can create a connection.
     *
     * @param username
     * @param password
     * @param endpoint
     * @return
     */
    protected ConnectorConfig createConnectorConfig(String username, String password, String endpoint) {
        ConnectorConfig config = new ConnectorConfig();
        config.setUsername(username);
        config.setPassword(password);

        config.setAuthEndpoint(endpoint);
        config.setServiceEndpoint(endpoint);

        // config.setManualLogin(true);

        config.setCompression(false);

        //Need to test this code if this really does Session Renewal or not
        SessionRenewer sessionRenewer = new SessionRenewer() {
            public SessionRenewalHeader renewSession(ConnectorConfig config) throws ConnectionException {
                //Try to reconnect with existing credentials
                reconnect();

                SessionRenewalHeader sessionRenewalHeader = new SessionRenewalHeader();
                sessionRenewalHeader.name = new javax.xml.namespace.QName("urn:partner.soap.sforce.com",
                        "SessionHeader");
                sessionRenewalHeader.headerElement = partnerConnection.getSessionHeader();
                return sessionRenewalHeader;
            }
        };

        config.setSessionRenewer(sessionRenewer);
        return config;
    }

    /**
     * Fetches the Login Result which has most of the details related to a connection and also the user info.
     *
     * @param username
     * @param password
     * @param partnerUrl
     * @return
     * @throws ConnectionException
     */
    private LoginResult loginToSalesforce(String username, String password, String partnerUrl)
            throws ConnectionException {
        ConnectorConfig config = createConnectorConfig(username, password, partnerUrl);
        config.setAuthEndpoint(partnerUrl);
        partnerConnection = Connector.newConnection(config);
        return partnerConnection.login(username, password);
    }

    private MetadataConnection createMetadataConnection(LoginResult loginResult) {
        if (metadataConnection != null)
            return metadataConnection;
        try {
            ConnectorConfig config = new ConnectorConfig();
            config.setServiceEndpoint(loginResult.getMetadataServerUrl());
            config.setSessionId(loginResult.getSessionId());
            config.setConnectionTimeout(2 * 60 * 1000);
            config.setReadTimeout(2 * 60 * 1000);
            return new MetadataConnection(config);
        } catch (ConnectionException ce) {
            Log.error("Metadata Connection Exception " + ce.getLocalizedMessage(), ce);
            return null;
        }
    }

    private SoapConnection createApexConnection(LoginResult loginResult) {
        if (apexConnection != null)
            return apexConnection;
        try {
            ConnectorConfig soapConfig = new ConnectorConfig();
            soapConfig.setAuthEndpoint(partnerUrl);
            //In-order to create apex connection need to modify the Service Endpoint URL
            soapConfig.setServiceEndpoint(loginResult.getServerUrl().replace("/u/", "/s/"));
            soapConfig.setSessionId(loginResult.getSessionId());
            soapConfig.setConnectionTimeout(2 * 60 * 1000);
            soapConfig.setReadTimeout(2 * 60 * 1000);
            return new SoapConnection(soapConfig);

        } catch (ConnectionException ce) {
            Log.error("Apex Connection Exception " + ce.getLocalizedMessage(), ce);
            return null;
        }
    }

    private BulkConnection createBulkConnection(ConnectorConfig config) {
        if (bulkConnection != null)
            return bulkConnection;
        try {
            //Setting aync URL to establish bulk connection.
            String restEndpoint = "https://" + (new URL(config.getServiceEndpoint())).getHost() + "/services/async/" + API_VERSION;
            config.setRestEndpoint(restEndpoint);
            return new BulkConnection(config);
        } catch (MalformedURLException me) {
            Log.error("Malformed URL Exception " + me.getLocalizedMessage(), me);
            return null;
        } catch (AsyncApiException e) {
            Log.error("Bulk Connection Exception " + e.getLocalizedMessage(), e);
            return null;
        }
    }

}
