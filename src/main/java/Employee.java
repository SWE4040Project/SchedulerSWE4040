/**
 * Created by Josh on 2016-11-06.
 */
public class Employee {
    private int id;
    private String name;
    private String company_employee_id;
    private int company_id;
    private boolean manager;
    private boolean super_admin;

    public Employee(int id, String name, String company_employee_id,int company_id,boolean manager,boolean super_admin){
        this.id = id;
        this.name = name;
    }

}
