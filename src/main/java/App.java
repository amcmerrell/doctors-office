import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {

  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("doctors", Doctor.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/", (request, response)->{
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("doctor-name");
      String practice = request.queryParams("doctor-practice");
      Doctor theDoctor = new Doctor(name,practice);
      theDoctor.save();
      model.put("doctors",Doctor.all());
      model.put("template","templates/index.vtl");
      return new ModelAndView(model,layout);
    }, new VelocityTemplateEngine());

    get("/doctors/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Doctor theDoctor = Doctor.find(Integer.parseInt(request.params(":id")));

      model.put("doctor", theDoctor);
      model.put("patients", theDoctor.getPatient());
      model.put("template", "templates/doctor.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/doctors/doctorId", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Doctor theDoctor = Doctor.find(Integer.parseInt(request.queryParams("doctorId")));
      String patientName = request.queryParams("patient-name");
      String patientBirthdate = request.queryParams("patient-birthdate");
      Patient thePatient = new Patient(patientName, patientBirthdate, theDoctor.getId());
      thePatient.formatDate();
      thePatient.save();
      String url = String.format("/doctors/%d", thePatient.getDoctorId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/doctors/:doctor_id/patients/:id", (request,response)->{
      Map<String, Object> model = new HashMap<String, Object>();
      Doctor theDoctor = Doctor.find(Integer.parseInt(request.params("doctor_id")));
      Patient thePatient = Patient.find(Integer.parseInt(request.params("id")));
      model.put("doctor",theDoctor);
      model.put("patient",thePatient);
      model.put("template","templates/patient.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
