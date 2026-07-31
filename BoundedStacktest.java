import java.util.*;

/**
 * Test runner
 */
public class BoundedStacktest 
{
    private static int passed =0;
    private static int failed =0;
    
    /**
     * 
     */
    public static void check(String name, boolean condition)
    {
        if(condition)
        {
            passed++;
            System.out.println("[PASS]" + name);
        }
        else
        {
            failed++;
            System.out.println("[Failed]" + name);
        }
    }

    public static void main(String[] args) 
    {
        
    }
}
