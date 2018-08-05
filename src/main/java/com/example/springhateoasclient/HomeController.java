package com.example.springhateoasclient;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
public class HomeController {
  private static final String REMOTE_SERVICE_URI = "http://localhost:9000";

  private final RestTemplate restTemplate;

  public HomeController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @GetMapping("/")
  public String index(Model model) throws URISyntaxException {
    Traverson client = new Traverson(new URI(REMOTE_SERVICE_URI), MediaTypes.HAL_JSON_UTF8);
    Resources<Resource<Employee>> employees = client
        .follow("employees")
        .toObject(new TypeReferences.ResourcesType<Resource<Employee>>() {});
    model.addAttribute("employee", new Employee());
    model.addAttribute("employees", employees);
    return "index";
  }

  @PostMapping("/employees")
  public String newEmployee(@ModelAttribute Employee employee) throws URISyntaxException {
    Traverson client = new Traverson(new URI(REMOTE_SERVICE_URI), MediaTypes.HAL_JSON_UTF8);
    Link employeesLink = client
        .follow("employees")
        .asLink();
    restTemplate.postForEntity(employeesLink.expand().getHref(), employee, Employee.class);
    return "redirect:/";
  }
}
