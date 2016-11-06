/**
 * Created by Josh on 2016-11-05.
 */
public class CompanyTest {
    public static void main(String[] args){
        String campanyName = "Josh's Company";
        Company a = new Company(campanyName);
        assert a.getName().equals(campanyName) : "CompanyTest - Company name not present";
        assert a.getId() > 0 : "CompanyTest - Company id not present";
        System.out.println("Company tests passed");
    }
}