package io.github.jkaano.toomanykeybinds.client.compatibility;

import java.util.ArrayList;

public class RobotCompatibilityChecker{

    /*
     *To allow for easy compatibility patches, this ArrayList will be written to by ModCompatibility files.
     * Check EssentialsCompatibility for reference.
     * The main purpose is to combat Robot compatibility issues, if any value is true it will skip the robot.
     */
    private ArrayList<Boolean> isCompatible;

    public RobotCompatibilityChecker(){
        isCompatible = new ArrayList<Boolean>();
    }

    public void addCompatible(boolean RobotCompatibility){
        isCompatible.add(RobotCompatibility);
    }

    public boolean checkRobotCompatibility(){
        return !isCompatible.contains(true);
    }

}
