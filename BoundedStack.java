import java.util.*;

/**
 * BoundedStack - ADT แทนกล่องเก็บชุดข้อความที่สามารถบรรจุข้อความได้อย่างจำกัด สามารถดึงข้อความเข้าและออกได้
 * 
 * ค่านามธรรม (A): กองของชุดข้อมูล เช่น [ข้อความ 1 , ข้อความ 2 , ข้อความ 3] 
 *                
 */

public class BoundedStack 
{
    private final List<String> box;
    private final int capacity;

    //AF -> AF(box) = กล่องที่บรรจุชุดข้อความไว้ ซึ่งภายในก็จะมีข้อความ


    //RI 
    // กล่องเก็บชุดข้อความ != null
    // ข้อความ !=null
    // ข้อความไม่เป็นสตริงว่าง
    // ไม่เก็บข้อความซ้ำ
    // ข้อความมีได้ไม่เกิน capacity
    //-

    //Rep Exposure 
    // box เป็น private final  
    // ใช้วิธี Defensive Copying 
    // box เป็น private final

    private void checkRep()
    {
        assert box!=null : "song is null";
        assert box.size()<=capacity : "overload";
        
        Set<String> seen = new HashSet<>();
        for(String s : box)
        {
            assert s!=null : "ข้อความห้ามเป็น null";
            assert !s.isEmpty() : "ข้อความห้ามเป็นสตริงว่าง";
            assert seen.add(s) : "เพลงซ้ำ" + s;
        }
    }

    //===Creators===

    /**
     * สร้างกล่องชุดข้อความว่าง พร้อมกำหนดขนาดที่บรรจุได้
     * @param capacity ขนาดความจุข้อความที่รับเข้ามาต้องไม่เป็นค่าลบและ 0 รวมถึงจะต้องเป็นค่าที่น้อยกว่าหรือเท่ากับ 100,000
     * @throws IllegalArgumentException() เมื่อ capacity ผิดเงื่อนไขที่ดังกล่าวไป
     *   
     */

    public BoundedStack(int capacity)
    {
        if(capacity <=0) throw new IllegalArgumentException();
        if(capacity >100000) throw new IllegalArgumentException();

        this.box = new ArrayList<>();
        this.capacity = capacity;
        checkRep(); 
    }

    /**
     * สร้างกล่องชุดข้อความจากกล่องที่รับเข้ามา พร้อมกำหนดขนาดที่บรรจุได้
     * @param capacity ขนาดความจุข้อความที่รับเข้ามาต้องไม่เป็นค่าลบและ 0 รวมถึงจะต้องเป็นค่าที่น้อยกว่าหรือเท่ากับ 100,000
     * @param box กล่องชุดข้อความที่รับเข้ามา โดยข้อความในกล่องห้ามซ้ำกัน, ห้ามเป็น Str ว่าง, ห้ามเป็น null 
     * ข้อความด้านใน box ห้ามเป็น null
     * @throws IllegalArgumentException() เมื่อ capacity, box ผิดเงื่อนไขที่ดังกล่าวไป
     *   
     */
    public BoundedStack(int capacity,List<String> box)
    {
        if(capacity<=0) throw new IllegalArgumentException();
        if(capacity>100000) throw new IllegalArgumentException();

        if(box==null) throw new IllegalArgumentException();
        Set<String> seen=new HashSet<>();
        for(String s : box)
        {
            if(s==null) throw new IllegalArgumentException();
            if(s.isEmpty()) throw new IllegalArgumentException();
            if(!seen.add(s)) throw new IllegalArgumentException();
        }

        this.capacity=capacity;
        this.box = new ArrayList<>(box);
        checkRep();
    }
    

    //===Mutators===


    /**
     * 
     * เพิ่มข้อความในกล่องโดยข้อความที่เพิ่มเข้ามาจะอยู่หลังสุด
     * @param mess ข้อความที่ต้องการเพิ่มเข้าไป ต้องไม่เป็น null ไม่เป็น สตริงว่าง
     * @return true เมื่อเพิ่มสำเร็จ , false เมื่อมีเพลงนี้อยู่แล้วหรือเพลงเต็ม 
     * @throws IllegalArgumentException() เมื่อ mess เป็น null หรือ สตริงว่าง
     */
    public boolean push(String mess)
    {
        if(mess == null || mess.isEmpty()) throw new IllegalArgumentException();
        if(!box.contains(mess)) return false;
        if(box.size()==capacity) return false;


        box.add(mess);
        return true;
    }

    /**
     * 
     * ลบข้อความในกล่องทิ้ง 
     * @param mess ข้อความที่ต้องการลบ 
     * @return true ถ้าลบสำเร็จ , false ถ้าไม่พบข้อความที่ต้องการลบ
     * @throws IllegalArgumentException() เมื่อ box เป็น null , ข้อความภายใน box เป็น สตริงว่าง 
     */

    public boolean pop()
    {
        if(box == null) throw new IllegalArgumentException();
        for(String s : box)
        {
            if(s==null) throw new IllegalArgumentException();
            if(s.isEmpty()) throw new IllegalArgumentException();
        }

        box.remove(box.get(box.size()-1));
        return true; //แก้
    }



    //===Observers===



    /**
     * คืนจำนวน ข้อความ ภายในกล่อง
     * @param mess
     * @return จำนวนข้อความ ภายในกล่อง
     */
    public int size()
    {
        return box.size();
    }

    /**
     * เช็กว่าข้อความมีอยู่ในกล่องชุดข้อความจริงหรือเปล่า
     * @param mess
     * @return เป็น true เมื่อมีข้อความในกล่องข้อความ , เป็น false เมื่อไม่มี
     */
    public boolean contains(String mess)
    {
        return box.contains(mess);
    }

    /**
     * ส่งกล่องชุดข้อความออกไป
     * @return กล่องชุดข้อความ
     */
    public ArrayList<String> box()
    {
        return new ArrayList<>(box);
    }

    public BoundedStack shuffled()
    {
        List<String> copy = new ArrayList<>(box);
        Collections.shuffle(copy);

        return new BoundedStack(copy.size(),copy);
    }
}
