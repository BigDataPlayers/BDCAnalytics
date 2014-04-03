package com.bdc.analytics.server;

import rcaller.RCaller;
import rcaller.RCode;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: a09726a
 * Date: 1/24/13
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChartBuilder {
    private List<String> commandList;
    private String Rlocation;

    public String getRlocation() {
        return Rlocation;
    }

    public void setRlocation(String rlocation) {
        Rlocation = rlocation;
    }

    public void setCommands(List<String> commands) {
        for (String command : commands) {
            commandList.add(command);
        }
    }

    public void createChart() {
        try {
            RCaller caller = new RCaller();
            caller.setRscriptExecutable(Rlocation);
            RCode code = new RCode();
            code.clear();
            buildRCommandList(code);
            caller.setRCode(code);
            caller.runOnly();
            code.clear();
        } catch (Throwable e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }
    }

    private void buildRCommandList(RCode code) {
        String str ;
        while (commandList.iterator().hasNext())
        {
            str = commandList.iterator().next() ;
            code.addRCode(str);
            System.out.println("line Read:" + str);
        }
    }

}
