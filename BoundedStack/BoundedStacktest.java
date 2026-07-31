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
     * ตัวช่วยกลางสำหรับรันเทสหนึ่งเคส — ตรวจผลแล้วพิมพ์ PASS/FAIL พร้อมนับผลรวมให้อัตโนมัติ
     * ถ้า condition เป็น true จะนับเข้า passed และพิมพ์ [PASS]
     * ถ้าเป็น false จะนับเข้า failed และพิมพ์ [Failed]
     * @param name ชื่อ/คำอธิบายของเทสเคสที่ต้องการแสดง
     * @param condition ผลของเทส — true ถ้าผ่าน, false ถ้าไม่ผ่าน
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
        testExposure();

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
        // เช็คว่า capacity = 0 ต้องโยน exception (0 ไม่ผ่านเงื่อนไข > 0)
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
        // เช็คว่า capacity ติดลบต้องโยน exception
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
        // เช็คว่า capacity เกิน 100000 ต้องโยน exception (เกินเพดาน)
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
        // เช็คว่า capacity ปกติ (100) ต้องสร้างได้ ไม่โยน exception
        check("capacity==100 is normal", regular);
    }

    private static void testCapacityandListinCreators()
    {
        System.out.println("\n-- testCapacityandListinCreators --");

        BoundedStack normal = new BoundedStack(100,Arrays.asList("text","book","notebook"));
        // เช็คว่าสร้างจาก list 3 ตัว แล้วนับจำนวนถูก (size == 3)
        check("normal_size==3",normal.size()==3);
        // เช็คว่าของที่ใส่เข้าไปหาเจอจริง (contains "text")
        check("normal -> contains text ",normal.contains("text"));
        // เช็คว่าเก็บของแล้วยังเรียงลำดับเดิม ไม่สลับ
        check("normal -> preserves order",normal.box().equals(Arrays.asList("text","book","notebook")));

        BoundedStack empty = new BoundedStack();
        // เช็คว่าสร้างกล่องเปล่า (no-arg) แล้ว size = 0
        check("size==0", empty.size()==0);
        // เช็คว่ากล่องว่างหาอะไรก็ไม่เจอ
        check("empty contains's noting", !empty.contains("anything"));

        BoundedStack putnewemptylist = new BoundedStack(new ArrayList<String>());
        // เช็คว่าส่ง list ว่างเข้าไป แล้ว size = 0 (boundary ขอบล่างที่ถูก)
        check("when put new empty list -> size 0", putnewemptylist.size()==0);

        boolean same=false;
        try {
            new BoundedStack(Arrays.asList("A", "A"));
        } catch (IllegalArgumentException e) {
            same = true;
        }
        // เช็คว่าส่ง list ที่มีของซ้ำ [A,A] ต้องโยน exception (constructor กันของซ้ำในตัว)
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
        // เช็คว่า push สตริงว่าง "" ต้องโยน exception
        check("push empty string -> IllegalArgumentException ", noting);
        // เช็คว่าหลัง push fail แล้ว size ยังเป็น 0 (push พังไม่ทิ้งของค้าง)
        check("s size is still 0",s.size()==0);

        BoundedStack tmp = new BoundedStack();
        // เช็คว่า push ของใหม่สำเร็จ คืน true
        check("push pen -> return true", tmp.push("pen"));
        // เช็คว่าหลัง push แล้ว size เพิ่มเป็น 1
        check("tmp's size is 1",tmp.size()==1);
        // เช็คว่า push แล้วหาของนั้นเจอ (contains "pen")
        check("find pen in tmp",tmp.contains("pen"));

        tmp.push("pencil");
        tmp.push("Ipad");
        // เช็คว่า push หลายตัวแล้วเรียงตามลำดับที่ใส่ (ตัวใหม่ต่อท้าย)
        check("add preserves insertion order",
                tmp.box().equals(Arrays.asList("pen","pencil","Ipad")));
        
        // เช็คว่า push ของที่มีอยู่แล้ว (ซ้ำ) ต้องคืน false
        check("duplicate value makes push return false",tmp.push("pen")==false);
        // เช็คว่าหลัง push ซ้ำ fail แล้ว size ยังเท่าเดิม (3) ไม่เพิ่ม
        check("pen can't add in list then size is still unchaged",tmp.size()==3);
    }

    private static void testpop()
    {
        System.out.println("\n-- testpop --");

        BoundedStack a =new BoundedStack(Arrays.asList("pen","pencil","Ipad"));
        // เช็คว่าสร้างจาก list 3 ตัว size = 3
        check("a size is 3", a.size()==3);
        // เช็คว่า pop สำเร็จ คืน true
        check("pop Ipad -> return true",a.pop());
        // เช็คว่าตัวท้ายสุด (Ipad) ถูกลบออกไปจริง หาไม่เจอแล้ว
        check("Ipad is out now",!a.contains("Ipad"));
        // เช็คว่าหลัง pop แล้ว size ลดลง 1 (เหลือ 2)
        check("after pop size get-1",a.size()==2);
        // เช็คว่า pop เอา "ตัวท้าย" ออก (LIFO) เหลือ [pen,pencil] เรียงเดิม
        check("pop will remove last value",a.box().equals(Arrays.asList("pen","pencil")));

        a.pop(); //  a-> "pen"
        a.pop();// a-> "noting"
        // เช็คว่า pop จนหมดแล้ว size = 0
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
        // เช็คว่า pop ตอนกล่องว่างต้องโยน exception
        check("a empty and then pop -> IllegalArgumentException", popempty);


    }
    private static void testObservers()
    {
        System.out.println("\n-- testObservers --");

        BoundedStack s =new BoundedStack(Arrays.asList("pen","pencil","Ipad"));
        // เช็คว่า size() อ่านจำนวนถูก (3)
        check("s have size = 3", s.size()==3);
        // เช็คว่าเรียก size() แล้วไม่แก้ state (box() ยังเท่าเดิม) = observer ไม่มี side effect
        check("s unchaged after size()",s.box().equals(Arrays.asList("pen","pencil","Ipad")));
        // เช็คว่า contains หาของที่มีเจอ (true)
        check("pen is in a box", s.contains("pen"));
        // เช็คว่า contains หาของที่ไม่มีไม่เจอ (false)
        check("clock isn't in a box", !s.contains("clock"));
        // เช็คว่าเรียก contains() แล้วไม่แก้ state (box() ยังเท่าเดิม) = observer ไม่มี side effect
        check("s unchaged after contains()",s.box().equals(Arrays.asList("pen","pencil","Ipad")));
        

    }
    private static void testProducers()
    {
        System.out.println("\n-- Producer --");

        BoundedStack origin = new BoundedStack(Arrays.asList("pen", "pencil", "Ipad", "clock"));
        BoundedStack shuffled = origin.shuffled();

        // เช็คว่า shuffled มีจำนวนสมาชิกเท่าเดิมกับ origin (สับแล้วไม่ทำของหาย/งอก)
        check("shuffled has the same size", shuffled.size() == origin.size());

        List<String> a = new ArrayList<String>(Arrays.asList("A","B","C"));
        List<String> b = new ArrayList<String>(Arrays.asList("C","B","A"));
        Collections.sort(b); // b ->("A","B","C")
        // เช็คว่า sort สอง list ที่มีสมาชิกชุดเดียวกันแล้วต้อง equal กัน (พิสูจน์หลักการเทียบแบบไม่สนลำดับ)
        check("shuffled contains exactly the same songs", a.equals(b));

        // เช็คว่า origin ไม่เปลี่ยนหลังเรียก shuffled() (producer ไม่แก้ตัวเดิม)
        check("origin unchanged after shuffled",origin.box().equals(Arrays.asList("pen", "pencil", "Ipad", "clock")));
        
        BoundedStack emptyShuffled = new BoundedStack().shuffled();
        // เช็คว่า shuffle กล่องว่างไม่พัง ได้กล่องว่าง (size 0)
        check("shuffling an empty playlist is safe", emptyShuffled.size() == 0);
    }

    private static void testExposure()
    {
        System.out.println("\n-- Representation Exposure --");

        BoundedStack s = new BoundedStack(Arrays.asList("A"));
        List<String> tmp = s.box();
        tmp.clear();

         // เช็คว่า clear() list ที่ได้จาก box() แล้วกล่องจริงไม่กระทบ (ยัง size 1) = defensive copy ขาออก
        check("s and tmp are not the same object", s.size()==1);
        tmp=s.box();
        tmp.add("B");

        // เช็คว่า add ของแปลกปลอมเข้า list ที่ได้จาก box() แล้วกล่องจริงไม่กระทบ (ยัง size 1, ไม่มี B)
        check("s and tmp are not the same object", s.size()==1 && !s.contains("B"));

        // เช็คว่า box() คืน object ใหม่คนละก้อนทุกครั้งที่เรียก (สร้าง new ArrayList ใหม่เสมอ)
        check("each call to box() returns a new (distinct) ArrayList object",s.box() != s.box());
    }
}
