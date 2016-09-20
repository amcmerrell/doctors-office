//   getPatient save
import java.util.List;
import org.sql2o.*;

public class Doctor {
  private String practice;
  private String name;
  private int id;

  public Doctor(String name, String practice){
    this.practice = practice;
    this.name = name;
  }

  public String getName(){
    return name;
  }

  public String getPractice(){
    return practice;
  }

  public int getId(){
    return id;
  }

  public static List<Doctor> all(){
    String sql = "SELECT * FROM doctors";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).executeAndFetch(Doctor.class);
    }
  }

  public static Doctor find(int id){
    String sql = "SELECT * FROM doctors WHERE id = :id";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).addParameter("id",id).executeAndFetchFirst(Doctor.class);
    }
  }

  @Override
  public boolean equals(Object otherDoctor){
    if(!(otherDoctor instanceof Doctor)){
      return false;
    }else{
      Doctor aDoctor = (Doctor) otherDoctor;
      return this.getName().equals(otherDoctor.getName()) && this.getId()==aDoctor;
    }
  }

  public void save(){
    String sql = "INSERT INTO doctors (name,practice) VALUES (:name,:practice)";
    try(Connection con = DB.sql2o.open()){
      this.id = (int) con.createQuery(sql,true).addParameter("name",this.name).addParameter("practice",this.practice).executeUpdate().getKey();
    }

    public List<Patient> getPatient(){
      try(Connection con = DB.sql2o.open()){
        String sql = "SELECT * FROM patients WHERE doctorId = :id";
        return con.createQuery(sql).addParameter("id",this.id).executeAndFetch(Patient.class);
      }
    }
  }




}
