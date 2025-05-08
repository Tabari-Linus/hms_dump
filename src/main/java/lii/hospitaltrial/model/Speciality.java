package lii.hospitaltrial.model;

public class Speciality {
    private long specialityId;
    private String name;


    public Speciality() {}

    public Speciality(long specialityId, String name) {
        this.specialityId = specialityId;
        this.name = name;
    }

    public long getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(long specialityId) {
        this.specialityId = specialityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}