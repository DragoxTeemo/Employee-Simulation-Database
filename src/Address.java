public class Address {
    private String street;
    private String cityName; // From 'cities' tables join
    private String stateAbbr;
    private String zip;

    public Address(String street, String cityName, String stateAbbr, String zip) {
        this.street = street;
        this.cityName = cityName;
        this.stateAbbr = stateAbbr;
        this.zip = zip;
    }

    //Getters
    public String getStreet() {
        return street;
    }

    public String getCityName() {
        return cityName;
    }

    public String getStateAbbr() {
        return stateAbbr;
    }

    public String getZip() {
        return zip;
    }

    public String getFullAddress() { 
        return street + ", " + cityName + ", " + stateAbbr + " " + zip; 
    }

    //Setters
    public void setStreet(String street) {
        this.street = street;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setStateAbbr(String stateAbbr) {
        this.stateAbbr = stateAbbr;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}

