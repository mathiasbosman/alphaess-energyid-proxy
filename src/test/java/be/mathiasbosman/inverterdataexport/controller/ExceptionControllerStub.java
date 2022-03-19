package be.mathiasbosman.inverterdataexport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionControllerStub {

  @Autowired
  ExceptionServiceStub exceptionServiceStub;

  @GetMapping("/exceptionStub/throw")
  public Throwable throwException() {
    exceptionServiceStub.throwException();
    return null;
  }

}
