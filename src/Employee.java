import java.time.LocalDate;


public class Employee {
    private int empID;
    private String firstName;
    private String lastName;
    private String ssn;
    private LocalDate dob;
    private Address address; 

    public Employee(int empID, String firstName, String lastName, String ssn, LocalDate dob, Address address) {
        this.empID = empID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.dob = dob;
        this.address = address;
    }

    public int getEmpID() {
        return empID;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSsn() {
        return ssn;
    }

    public LocalDate getDob() {
        return dob;
    }

    public Address getAddress() {
        return address;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}