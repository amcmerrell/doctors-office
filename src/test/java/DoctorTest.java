import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DoctorTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doctors_office_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteDoctorsQuery = "DELETE FROM doctors *;";
      String deletePatientQuery = "DELETE FROM patients *;";
      con.createQuery(deleteDoctorsQuery).executeUpdate();
      con.createQuery(deletePatientQuery).executeUpdate();
    }

  }
  @Test
  public void save_savesIntoDatabase_true() {
    Doctor myDoctor = new Doctor("John","Pediatrics");
    myDoctor.save();
    assertTrue(Doctor.all().get(0).equals(myDoctor));
  }
}
