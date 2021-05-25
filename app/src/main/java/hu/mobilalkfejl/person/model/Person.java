package hu.mobilalkfejl.person.model;

public class Person {

    private String id;
    private HumanName name;
    private String birthDate;
    private String gender;
    private boolean active;
    private String link;


    public Person(HumanName name, String birthDate, String gender, boolean active, String link) {
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.active = active;
        this.link = link;
    }

    public Person() {
    }

    public static class HumanName {
        private String family;
        private String given;

        public HumanName(String family, String given) {
            this.family = family;
            this.given = given;
        }

        public HumanName() {
        }

        public String getFamily() {
            return family;
        }

        public void setFamily(String family) {
            this.family = family;
        }

        public String getGiven() {
            return given;
        }

        public void setGiven(String given) {
            this.given = given;
        }
    }

    public String _getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HumanName getName() {
        return name;
    }

    public void setName(HumanName name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


}



