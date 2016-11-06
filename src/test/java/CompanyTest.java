/**
 * Created by Josh on 2016-11-05.
 */
public class CompanyTest {
    public static void main(String[] args){
        String campanyName = "Josh's Company";
        Company c = new Company(campanyName);
        assert c.getName().equals(campanyName) : "CompanyTest - Company name not present";
        assert c.getId() > 0 : "CompanyTest - Company id not present";
        System.out.println("Company tests passed");
    }
}
