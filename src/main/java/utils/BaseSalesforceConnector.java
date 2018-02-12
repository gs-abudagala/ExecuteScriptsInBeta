
package utils;

import beans.SFDCInfo;
import com.sforce.async.BulkConnection;
import com.sforce.soap.apex.ExecuteAnonymousResult;
import com.sforce.soap.apex.SoapConnection;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.*;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import org.apache.commons.io.FileUtils;
import utils.wait.CommonWait;
import utils.wait.ExpectedCommonWaitCondition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseSalesforceConnector {

    protected SFDCInfo sfdcInfo;

    public abstract PartnerConnection getPartnerConnection();

    public abstract BulkConnection getBulkConnection();

    public abstract SoapConnection getApexConnection();

    public abstract MetadataConnection getMetadataConnection();


    // Start of Apex Utilities
    public void runApexCode(final String apexCode) {
        // logic to run the apex code and return the result
        ExecuteAnonymousResult result;

        if (getApexConnection() == null)
            throw new RuntimeException("Apex Connection Failed.");
        Log.info("Running Apex Code : " + apexCode);
        result = CommonWait.waitForCondition(CommonWait.DEFAULT_WAIT * 2, CommonWait.DEFAULT_INTERVAL * 5,
                new ExpectedCommonWaitCondition<ExecuteAnonymousResult>() {
                    @Override
                    public ExecuteAnonymousResult apply() {
                        try {
                            return getApexConnection().executeAnonymous(apexCode);
                        } catch (ConnectionException ce) {
                            Log.error(ce.getLocalizedMessage(), ce);
                        }
                        return null;
                    }
                });

        if (result.isCompiled()) {
            if (result.isSuccess()) {
                Log.info("Apex code executed sucessfully");
            } else {
                Log.error("Apex code execution failed : " + result.getExceptionMessage());
                throw new RuntimeException("Apex code execution failed : " + result.getExceptionMessage());
            }
        } else {
            Log.error("Apex code execution failed : " + result.getCompileProblem());
            throw new RuntimeException("Apex code compilation failed :" + result.getCompileProblem());
        }
    }

    public void runApexCodeFromFile(File apexFile) throws IOException {
        if (apexFile.exists())
            runApexCode(FileUtils.readFileToString(apexFile));
    }

    // End of Apex Utilities

    // Start of Partner Connection Methods
    public DescribeGlobalResult describeGlobal() {
        try {
            return getPartnerConnection().describeGlobal();
        } catch (Exception e) {
            Log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public DescribeSObjectResult describeSObject(String type) {
        try {
            return getPartnerConnection().describeSObject(type);
        } catch (Exception e) {
            Log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Method to return the number of records that the query has returned.
     *
     * @param query - SOQL query
     * @return count - Returns the number of records that the query as queried.
     */
    public int getRecordCount(final String query) {
        int count = 0;

        QueryResult result;
        result = CommonWait.waitForCondition(CommonWait.DEFAULT_WAIT * 2, CommonWait.DEFAULT_INTERVAL * 5,
                new ExpectedCommonWaitCondition<QueryResult>() {
                    @Override
                    public QueryResult apply() {
                        try {
                            return getPartnerConnection().queryAll(query);
                        } catch (ConnectionException ce) {
                            Log.error(ce.getLocalizedMessage(), ce);
                        }
                        return null;
                    }
                });
        if (result == null) {
            throw new RuntimeException("Failed to get the count of records.");
        }

        count = result.getSize();
        Log.info("No of records : " + count);
        return count;
    }

    public SObject[] getRecords(final String query) {
        SObject[] records = null;
        QueryResult result;
        result = CommonWait.waitForCondition(CommonWait.DEFAULT_WAIT * 2, CommonWait.DEFAULT_INTERVAL * 5,
                new ExpectedCommonWaitCondition<QueryResult>() {
                    @Override
                    public QueryResult apply() {
                        try {
                            return getPartnerConnection().query(query);
                        } catch (ConnectionException ce) {
                            Log.error(ce.getLocalizedMessage(), ce);
                        }
                        return null;
                    }
                });
        if (result == null) {
            throw new RuntimeException("Failed to get the records.");
        }
        records = result.getRecords();
        return records;
    }

    public void updateRecords(SObject[] sObjects) {
        try {
            com.sforce.soap.partner.SaveResult[] results = getPartnerConnection().update(sObjects);
            for (com.sforce.soap.partner.SaveResult r : results) {
                if (r.isSuccess()) {
                    Log.info("Created component: " + r.getId());
                } else {
                    Log.info("Errors were encountered while updating " + r.getId());
                    for (com.sforce.soap.partner.Error e : r.getErrors()) {
                        Log.error("Error message: " + e.getMessage());
                        Log.error("Status code: " + e.getStatusCode());
                    }
                    throw new RuntimeException("Failed to update records");
                }
            }
        } catch (ConnectionException ce) {
            throw new RuntimeException("Error while updating records", ce);
        }
    }

    //Need to revisit this code
    public void deleteQuery(List<String> queryList) {
        //
        for (String query : queryList) {
            //Write Logic Here
        }
    }

    // End of Partner Connection Methods

    // Start of Bulk Connection Methods

    // End of Bulk Connection Methods

    // End of Metadata Connection Methods

    /**
     * @param partnerConnection
     * @return - returns all gainsight package objects
     */
    public static List<String> getAllGainSightObjects(PartnerConnection partnerConnection) {
        List<String> packageObjects = new ArrayList<String>();
        try {
            DescribeGlobalResult describeGlobalResult = partnerConnection.describeGlobal();
            DescribeGlobalSObjectResult[] sobjectResults = describeGlobalResult.getSobjects();
            if (describeGlobalResult != null) {
                if (sobjectResults != null)
                    for (int i = 0; i < sobjectResults.length; i++) {
                        if (sobjectResults[i].getName().startsWith("JBCXM__")) {
                            Log.info(("GS Object: " + sobjectResults[i].getName() + ""));
                            packageObjects.add(sobjectResults[i].getName());
                        }
                    }
            }
        } catch (ConnectionException ce) {
            throw new RuntimeException(ce);
        }
        return packageObjects;
    }

    protected void fillSfdcInfo(LoginResult loginResult) {
        sfdcInfo = new SFDCInfo();
        sfdcInfo.setLoginResult(loginResult);
        sfdcInfo.setOrg(loginResult.getUserInfo().getOrganizationId());
        sfdcInfo.setUserId(loginResult.getUserId());
        sfdcInfo.setSessionId(loginResult.getSessionId());
        String sept = loginResult.getServerUrl();
        sept = sept.substring(0, sept.indexOf(".com") + 4);
        sfdcInfo.setEndpoint(sept);
        sfdcInfo.setUserName(loginResult.getUserInfo().getUserName());
        sfdcInfo.setUserFullName(loginResult.getUserInfo().getUserFullName());
        sfdcInfo.setUserEmail(loginResult.getUserInfo().getUserEmail());
        sfdcInfo.setUserCurrencySymbol(loginResult.getUserInfo().getCurrencySymbol());
        sfdcInfo.setUserLocale(loginResult.getUserInfo().getUserLocale());
        sfdcInfo.setUserTimeZone(loginResult.getUserInfo().getUserTimeZone());
    }
}
