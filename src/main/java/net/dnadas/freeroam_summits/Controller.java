package net.dnadas.freeroam_summits;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @GetMapping("/protected/hello")
  public String hello() {
    return "Hello from FreeRoamSummitsApplication protected route!";
  }

  @GetMapping("hello")
  public String hello2() {
    return "Hello from FreeRoamSummitsApplication public route!";
  }
}
