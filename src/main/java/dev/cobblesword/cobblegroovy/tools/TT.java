package dev.cobblesword.cobblegroovy.tools;

import dev.cobblesword.cobblegroovy.CobbleGroovy;

// Text Theme
public class TT
{
    public static String error(String message)
    {
        return CC.error(CobbleGroovy.PREFIX, message);
    }

    public static String info( String message)
    {
        return CC.info(CobbleGroovy.PREFIX, message);
    }

    public static String success(String message)
    {
        return CC.success(CobbleGroovy.PREFIX, message);
    }

    public static String announcement(String message)
    {
        return CC.announcement(CobbleGroovy.PREFIX, message);
    }
}
