package com.spike.giantdataanalysis.task.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/task" })
public class TaskAction {

  @RequestMapping(value = { "/workers" }, method = RequestMethod.GET)
  @ResponseBody
  public List<String> workers() {
    List<String> result = new ArrayList<String>();
    result.add("WORKER-01");
    result.add("WORKER-02");
    return result;
  }
}
