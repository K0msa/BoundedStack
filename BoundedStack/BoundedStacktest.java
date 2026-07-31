package BoundedStack;
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

        testCapacityinCreators();
        testCapacityandListinCreators();
        testpush();
        testpop();
        testObservers();
        testProducers();

        System.out.println("\n=== Summary ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total : " + (passed + failed));
        System.out.println(failed == 0 ? "ALL TESTS PASSED" : "SOME TESTS FAILED");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testCapacityinCreators()
    {
        System.out.println("\n-- testCapacityinCreators --");

        //input ที่ผิดเงื่อนไขที่ก่อให้เกิด Exception
        boolean zerocheck=false;

        try
        {
            new BoundedStack(0);
        }
        catch(IllegalArgumentException e)
        {
            zerocheck=true;
        }
        check("capacity == 0 throw IllegalArgumentException", zerocheck);

        boolean negative=false;

        try
        {
            new BoundedStack(-1);
        }
        catch(IllegalArgumentException e)
        {
            negative=true;
        }
        check("capacity<0 throw IllegalArgumentException", negative);

        boolean overload=false;

        try
        {
            new BoundedStack(100001);
        }
        catch(IllegalArgumentException e)
        {
            overload=true;
        }
        check("capcaity>100000 throw IllegalArgumentException",overload);

        boolean regular=true;
        try
        {
            new BoundedStack(100);
        }
        catch(IllegalArgumentException e)
        {
            regular=false;
        }
        check("capacity==100 is normal", regular);
    }

    private static void testCapacityandListinCreators()
    {
        System.out.println("\n-- testCapacityandListinCreators --");

        BoundedStack normal = new BoundedStack(100,Arrays.asList("text","book","notebook"));
        check("normal_size==3",normal.size()==3);
        check("normal -> contains text ",normal.contains("text"));
        check("normal -> preserves order",normal.box().equals(Arrays.asList("text","book","notebook")));

        BoundedStack empty = new BoundedStack();
        check("size==0", empty.size()==0);
        check("empty contains's noting", !empty.contains("anything"));

        BoundedStack putnewemptylist = new BoundedStack(new ArrayList<String>());
        check("when put new empty list -> size 0", putnewemptylist.size()==0);

        boolean same=false;
        try {
            new BoundedStack(Arrays.asList("A", "A"));
        } catch (IllegalArgumentException e) {
            same = true;
        }
        check("same -> throw IllegalArgumentException", same);
    }

    private static void testpush()
    {
        System.out.println("\n-- testpush --");

        BoundedStack s = new BoundedStack();
        boolean noting = false;
        try 
        {
            s.push("");   
        } 
        catch (IllegalArgumentException e) 
        {
            noting= true;
        }
        check("push empty string -> IllegalArgumentException ", noting);
        check("s size is still 0",s.size()==0);

        BoundedStack tmp = new BoundedStack();
        check("push pen -> return true", tmp.push("pen"));
        check("tmp's size is 1",tmp.size()==1);
        check("find pen in tmp",tmp.contains("pen"));

        tmp.push("pencil");
        tmp.push("Ipad");
        check("add preserves insertion order",
                tmp.box().equals(Arrays.asList("pen","pencil","Ipad")));
        
        check("duplicate value makes push return false",tmp.push("pen")==false);
        check("pen can't add in list then size is still unchaged",tmp.size()==3);
    }

    private static void testpop()
    {
        System.out.println("\n-- testpop --");

        BoundedStack a =new BoundedStack(Arrays.asList("pen","pencil","Ipad"));
        check("a size is 3", a.size()==3);
        check("pop Ipad -> return true",a.pop());
        check("Ipad is out now",!a.contains("Ipad"));
        check("after pop size get-1",a.size()==2);
        check("pop will remove last value",a.box().equals(Arrays.asList("pen","pencil")));

        a.pop(); //  a-> "pen"
        a.pop();// a-> "noting"
        check("pop already have size = 0", a.size()==0);
        boolean popempty=false;
        try 
        {
            a.pop();
        } 
        catch (IllegalArgumentException e) 
        {
            popempty=true;
        }
        check("a empty and then pop -> IllegalArgumentException", popempty);


    }
    private static void testObservers()
    {
        System.out.println("\n-- testObservers --");

        BoundedStack s =new BoundedStack(Arrays.asList("pen","pencil","Ipad"));
        check("s have size = 3", s.size()==3);
        check("s unchaged after size()",s.box().equals(Arrays.asList("pen","pencil","Ipad")));
        check("pen is in a box", s.contains("pen"));
        check("clock isn't in a box", !s.contains("clock"));
        check("s unchaged after contains()",s.box().equals(Arrays.asList("pen","pencil","Ipad")));

    }
    private static void testProducers()
    {
        System.out.println("\n-- Producer --");

        BoundedStack origin = new BoundedStack(Arrays.asList("pen", "pencil", "Ipad", "clock"));
        BoundedStack shuffled = origin.shuffled();

        check("shuffled has the same size", shuffled.size() == origin.size());

        List<String> a = new ArrayList<String>(Arrays.asList("A","B","C"));
        List<String> b = new ArrayList<String>(Arrays.asList("C","B","A"));
        Collections.sort(b); // b ->("A","B","C")
        check("shuffled contains exactly the same songs", a.equals(b));

        check("origin unchanged after shuffled",origin.box().equals(Arrays.asList("pen", "pencil", "Ipad", "clock")));
        
        BoundedStack emptyShuffled = new BoundedStack().shuffled();
        check("shuffling an empty playlist is safe", emptyShuffled.size() == 0);
    }
}
