package com.spike.giantdataanalysis.commons.agent;

import java.io.IOException;
import java.util.List;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

/**
 * 示例Agent加载器.
 */
public class ExampleAgentLoader {

  public static void main(String[] args) throws InterruptedException {

    if (args[0].equals("ExampleApplication")) {
      new ExampleApplication().hello();
    } else if (args[0].equals("LoadAgent")) {
      runAgent();
    }
  }

  private static void runAgent() {
    final String agentJarPath =
        "/Users/zhoujiagen/workspace/giant-data-analysis/data-management-infrastructure/infrastructure-commons-agent/target/infrastructure-commons-agent-0.0.1-SNAPSHOT-agent.jar";

    List<VirtualMachineDescriptor> vms = VirtualMachine.list();
    String vmPid = null;
    for (VirtualMachineDescriptor vm : vms) {
      System.out.println(vm.displayName());
      // 定位启动应用的JVM
      if (vm.displayName().contains("ExampleApplication")) {
        vmPid = vm.id();
        break;
      }
    }

    try {
      VirtualMachine vm = VirtualMachine.attach(vmPid);
      // 加载Agent
      vm.loadAgent(agentJarPath);
      vm.detach();
    } catch (AttachNotSupportedException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (AgentLoadException e) {
      e.printStackTrace();
    } catch (AgentInitializationException e) {
      e.printStackTrace();
    }
  }
}
