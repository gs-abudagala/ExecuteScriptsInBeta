package mainTest;

import models.SFDCConfigDocument;
import repository.TenantPoolRepository;
import repository.impl.TenantInfoRepositoryImpl;
import utils.SalesforceConnector;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by athakur on 2/11/2018.
 */
public class MainExecutor {

    static Properties properties = null;

    private static void initializeProperties() {
        properties = new Properties();
        try {
            properties.load(new FileReader("C:\\Users\\athakur\\Desktop\\Automation\\executeScriptsInBeta\\resources\\app-config.properties"));
        } catch (IOException ioException) {
            System.out.println("Properties file is not available or, is having corrupt - " + ioException.getMessage());
        }
    }

    public static void executeScript(SFDCConfigDocument sfdcConfigDocument) {
        SalesforceConnector connector = SalesforceConnector.get(sfdcConfigDocument.getSfdcConfig());
        connector.connect();
        try {
            connector.runApexCodeFromFile(new File("C:\\Users\\athakur\\Desktop\\Automation\\executeScriptsInBeta\\resources\\ApexScript.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initializeProperties();
        TenantPoolRepository tenantPoolRepository = new TenantInfoRepositoryImpl();
        List<SFDCConfigDocument> sfdcConfigDocuments = tenantPoolRepository.getAllSFDCConfigDocumentsForATeam(properties.getProperty("TEAM_NAME"));
        sfdcConfigDocuments.forEach(sfdcConfigDocument -> executeScript(sfdcConfigDocument));
    }
}
