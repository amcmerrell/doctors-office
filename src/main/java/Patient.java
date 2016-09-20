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
    String sql = "INSERT INTO patients (name, birthdate, doctorId) VALUES (:name, :birthdate, :doctorId)";
    try(Connection con = DB.sql2o.open()) {
      this.id = (int) con.createQuery(sql, true).addParameter("name", this.name).addParameter("birthdate", this.birthdate).addParameter("doctorId", this.doctorId).executeUpdate().getKey();
    }
  }

  @Override
  public boolean equals(Object otherPatient){
    if(!(otherPatient instanceof Patient)){
      return false;
    }else{
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
