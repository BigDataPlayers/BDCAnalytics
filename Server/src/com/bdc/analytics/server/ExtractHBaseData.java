package com.bdc.analytics.server; /**
 * Created with IntelliJ IDEA.
 * User: mahesh
 * Date: 2/16/13
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ExtractHBaseData {

    String[] columnNames;
    //DateParser mydt = new DateParser();
    String dirName = "/var/bdc/csv/";

    // ProcessingType.User,"32760565", mailingList ,
    // 201109171702l,201309171703l,columnNames

    public ExtractHBaseData(String[] columnNames) {

        this.columnNames = columnNames;

    }

    private String getTableName() {
        return "snap";
    }

    BufferedWriter writer;

    //hadoop-1.0.4
    //hbase-0.94.4

    private void writeFileHeader() {
        try {
            writer = new BufferedWriter(new FileWriter(dirName
                    + "clicks_hbase15.txt"));
            StringBuilder header = new StringBuilder();
            for (int i = 0; i < columnNames.length; i++) {
                header.append(columnNames[i] + ",");
            }
            writer.write(header.substring(0, header.length() - 1));
            // writer.write(columnNameHeader);
            writer.newLine();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void writeFileLine(String s) {
        try {

            writer.write(s);
            writer.newLine();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void generateFile() {
        ResultScanner scanner = null;
        try {
            Configuration config = HBaseConfiguration.create();
            String name = getTableName();

            HTable table = new HTable(config, name);

            Scan s = new Scan();

            scanner = table.getScanner(s);

            writeFileHeader();

            for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
                String ColName;

                StringBuilder key = new StringBuilder();

                for (int i = 0; i < columnNames.length; i++) {
                    ColName = columnNames[i];
                    byte[] returnValue = rr.getValue(Bytes.toBytes("cf1"),
                            Bytes.toBytes(ColName));
                    if (returnValue != null) {
                        String str = new String(returnValue);
                        // System.out.println(str);
                        key.append(str + ",");
                    } else {
                        System.out.println("Null value , ignoring");
                        key.append(",");
                    }

                }
                writeFileLine(key.toString().substring(0, key.length() - 1));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {

            scanner.close();

            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace(); // To change body of catch statement use
                // File | Settings | File Templates.
            }
        }
    }

}

