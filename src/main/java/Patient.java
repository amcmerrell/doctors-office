import java.util.List;
import org.sql2o.*;

public class Patient {
  private String name;
  private String birthdate;
  private int id;
  private int doctorId;

  public Patient (String name, String birthdate, int doctorId) {
    this.name = name;
    this.birthdate = birthdate;
    this.doctorId = doctorId;
  }

  public String getName() {
    return name;
  }

  public String getBirthdate() {
    return birthdate;
  }

  public int getId() {
    return id;
  }

  public int getDoctorId() {
    return doctorId;
  }

  public void formatDate(){
    String [] dateArray = birthdate.split("-");
    this.birthdate = dateArray[1]+"/"+dateArray[2]+"/"+dateArray[0];
    // return birthdate;
  }

  public static List<Patient> all() {
    String sql = "SELECT * FROM patients";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Patient.class);
    }
  }

  public static Patient find(int id) {
    String sql = "SELECT * FROM patients WHERE id = :id";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).addParameter("id", id).executeAndFetchFirst(Patient.class);
    }
  }

  public void save() {
    String sql = "INSERT INTO patients (name, birthdate, doctorid) VALUES (:name, :birthdate, :doctorid)";
    try(Connection con = DB.sql2o.open()) {
      this.id = (int) con.createQuery(sql, true).addParameter("name", this.name).addParameter("birthdate", this.birthdate).addParameter("doctorid", this.doctorId).executeUpdate().getKey();
    }
  }

  @Override
  public boolean equals(Object otherPatient){
    if(!(otherPatient instanceof Patient)){
      return false;
    } else {
      Patient aPatient = (Patient) otherPatient;
      return this.getName().equals(aPatient.getName()) && this.getId()==aPatient.getId();
    }
  }

  public void update(String name, String birthdate) {
    String sql = "UPDATE patients SET name = :name, birthdate = :birthdate WHERE id = :id";
    try(Connection con = DB.sql2o.open()) {
      con.createQuery(sql).addParameter("name", name).addParameter("birthdate", birthdate).addParameter("id", id).executeUpdate();
    }
  }
}
