package org;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Timestamp;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.wink.common.model.multipart.InMultiPart;
import org.apache.wink.common.model.multipart.InPart;

/**
 * Created by Josh on 2017-01-14.
 */
public class CSVHandler {
    public static boolean importShifts(InMultiPart csv_file, int company_id){
        try{
            if(!csv_file.hasNext()){
                return false;
            }
            InPart part = csv_file.next();

            InputStream inStream = part.getInputStream();

            CSVParser parser = new CSVParser(new InputStreamReader(inStream), CSVFormat.DEFAULT.withHeader());
            for(CSVRecord line : parser){

                String tm = line.get("REAL_START_TIME");
                System.out.println(tm);

                String scheduled_start = line.get("SCHEDULED_START_TIME");
                String scheduled_end = line.get("SCHEDULED_END_TIME");
                String real_start = line.get("REAL_START_TIME");
                String real_end = line.get("REAL_END_TIME");
                String approved_start = line.get("APPROVED_START_TIME");
                String approved_end = line.get("APPROVED_END_TIME");

                Shift.importFromCSV(
                        Integer.parseInt(line.get("EMPLOYEES_ID")),
                        Integer.parseInt(line.get("LOCATION_ID")),
                        company_id,
                        scheduled_start.equals("")? null : Timestamp.valueOf(scheduled_start),
                        scheduled_end.equals("")? null : Timestamp.valueOf(scheduled_end),
                        real_start.equals("")? null : Timestamp.valueOf(real_start),
                        real_end.equals("")? null : Timestamp.valueOf(real_end),
                        approved_start.equals("")? null : Timestamp.valueOf(approved_start),
                        approved_end.equals("")? null : Timestamp.valueOf(approved_end),
                        (line.get("AVAILABLE").toLowerCase()=="true")? 1 : 0,
                        line.get("WORKED_NOTES")
                        );
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }


        return true;
    }

    public static boolean importEmployees(InMultiPart csv_file, int company_id){
        try{
            if(!csv_file.hasNext()){
                return false;
            }
            InPart part = csv_file.next();

            InputStream inStream = part.getInputStream();

            CSVParser parser = new CSVParser(new InputStreamReader(inStream), CSVFormat.DEFAULT.withHeader());
            for(CSVRecord line : parser){

                String name = line.get("NAME");
                String employee_company_id = line.get("USERNAME");
                String manager = line.get("MANAGER").toLowerCase();
                String password = line.get("WEB_PASSWORD");

                Employee.importFromCSV(
                        name,
                        employee_company_id,
                        company_id,
                        manager.equals("true")? true : false,
                        0,
                        password
                );
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
}