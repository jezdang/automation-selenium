package org.openweathermap.report;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.log4j.Logger;
import org.openweathermap.helper.CommonUtils;

public class ReportManager {
    private static final Logger LOGGER = Logger.getLogger(ReportManager.class);

    public static void main(String[] args) throws Throwable {
        File reportDirectory = new File(args[0]);
        ReportManager reportManager = new ReportManager();
        reportManager.createTestReportDirectory();

        if (reportDirectory.exists()) {
            reportManager.generateMasterThoughtReport();
            reportManager.copyNewestReport();
            reportManager.copyJSONresult();
        } else {
            LOGGER.error("Reports folder is not exists [" + System.getProperty("user.dir") + reportDirectory.getPath() + "]");
        }
    }

    public void copyNewestReport() throws Throwable {
        String strNewestTestOutput = System.getProperty("user.dir")  + "/reports/newest_report/";

        File destFolder = new File(strNewestTestOutput);
        File srcFolder = new File(System.getProperty("strReportDirectory"));
        CommonUtils.copyDirectory(srcFolder, destFolder);
    }

    private void copyJSONresult() throws Throwable{
        String strNewestTestOutput = CommonUtils.getClassRootPath() + "/reports/newest_report/cucumber-html-reports/json-results/";
        String strJsonTestResult = CommonUtils.getClassRootPath() + "/target/cucumber-parallel/";
        File destFolder = new File(strNewestTestOutput);
        File srcFolder = new File(strJsonTestResult);
        if (!destFolder.exists()) {
            if (destFolder.mkdirs()) {
                CommonUtils.copyDirectory(srcFolder, destFolder);
                LOGGER.info("Copy JSON files of execution result successfully");
            } else {
                LOGGER.info("Cannot copy JSON files of execution result");
            }
        }
    }

    public void createTestReportDirectory() throws Throwable {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy_MM_dd");
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss");
        String strDate = sdfDate.format(cal.getTime());
        String strTime = sdfDateTime.format(cal.getTime());
        String reportDir = "/reports/";

        String strReportDirectoryPath = CommonUtils.getClassRootPath() + reportDir + strDate + "/" + strTime + "/";
        System.setProperty("strReportDirectory", strReportDirectoryPath);

        File files = new File(strReportDirectoryPath);
        if (!files.exists()) {
            if (files.mkdirs()) {
                LOGGER.info("Multiple directories are created: " + strReportDirectoryPath);
            } else {
                LOGGER.info("Failed to create multiple directories!");
            }
        }
    }

    private static void removeSteps() {
        try {
            ArrayList<String> jsonFiles = getJsonFiles();
            JsonFormatter.parseJsonFiles(jsonFiles);
        } catch (IOException ex) {
            LOGGER.error(ex);
        }
    }

    public void generateMasterThoughtReport() {
        List<String> jsonFiles;
        removeSteps();
        try {
            File reportOutputDirectory = new File(System.getProperty("strReportDirectory"));
            String buildProject = "Open Weather Map - Automation Testing";
            boolean parallelTesting = true;
            boolean runWithJenkins = false;

            Configuration configuration = new Configuration(reportOutputDirectory, buildProject);
            configuration.setTagsToExcludeFromChart("@Before", "@After");
            configuration.setParallelTesting(parallelTesting);
            configuration.setRunWithJenkins(runWithJenkins);

            jsonFiles = getJsonFiles();
            ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);

            reportBuilder.generateReports();
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
    }

    private static ArrayList<String> getJsonFiles() {
        ArrayList<String> jsonFiles = new ArrayList<>();
        String strConfigDirectory = System.getProperty("user.dir") + "/target/cucumber-reports";
        File directory = new File(strConfigDirectory);

        //get all the configuration files from config directory
        File[] files = directory.listFiles(pathname -> {
            String name = pathname.getName().toLowerCase();
            return name.endsWith(".json") && pathname.isFile();
        });
        if(files != null) {
            for (File file : files) {
                String strFileName = file.getAbsolutePath();
                jsonFiles.add(strFileName);
            }
        } else
            LOGGER.error("No report Found!");
        return jsonFiles;
    }
}
