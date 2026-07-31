# BoundedStack

ADT (Abstract Data Type) แทนกล่องเก็บชุดข้อความที่บรรจุได้อย่างจำกัด สามารถดึงข้อความเข้าและออกแบบ LIFO (Last In, First Out) โดยมีกฎควบคุมข้อมูลภายในให้สะอาดเสมอ

## คุณสมบัติ

- เก็บข้อความแบบ **stack** — เข้าหลังออกก่อน (LIFO)
- **จำกัดความจุ** ด้วย `capacity` (1 ถึง 100,000)
- **ห้ามข้อความซ้ำ** ในกล่อง
- **ห้ามข้อความว่างหรือ null**
- ป้องกัน **representation exposure** ด้วย defensive copying

## กฎภายใน (Representation Invariant)

ข้อมูลในกล่องต้องเป็นจริงตามนี้เสมอ:

- กล่องเก็บข้อความต้องไม่เป็น `null`
- ข้อความแต่ละตัวต้องไม่เป็น `null`
- ข้อความต้องไม่เป็นสตริงว่าง
- ห้ามมีข้อความซ้ำกัน
- จำนวนข้อความต้องไม่เกิน `capacity`

## วิธีใช้งาน

### สร้างกล่อง (Creators)

```java
// กล่องว่าง ความจุ 100 (ค่าเริ่มต้น)
BoundedStack a = new BoundedStack();

// กล่องว่าง กำหนดความจุเอง
BoundedStack b = new BoundedStack(50);

// สร้างจาก list ที่มีอยู่ ความจุ 100
BoundedStack c = new BoundedStack(Arrays.asList("pen", "book"));

// สร้างจาก list กำหนดความจุเอง
BoundedStack d = new BoundedStack(10, Arrays.asList("pen", "book"));
```

### เพิ่ม/ลบข้อความ (Mutators)

```java
BoundedStack s = new BoundedStack();

s.push("pen");      // เพิ่มข้อความ (ต่อท้าย) — คืน true ถ้าสำเร็จ
s.push("pen");      // ซ้ำ — คืน false
s.pop();            // ลบตัวท้ายสุดออก (LIFO) — คืน true ถ้าสำเร็จ
```

### อ่านค่า (Observers)

```java
s.size();               // จำนวนข้อความในกล่อง
s.contains("pen");      // มีข้อความนี้ไหม (true/false)
s.box();                // คืนสำเนา list ของข้อความทั้งหมด
```

### สร้างตัวใหม่ (Producers)

```java
BoundedStack shuffled = s.shuffled();   // คืนกล่องใหม่ที่สลับลำดับแล้ว (ไม่แก้ตัวเดิม)
```

## API

| Method | ประเภท | คำอธิบาย |
|---|---|---|
| `BoundedStack()` | Creator | สร้างกล่องว่าง ความจุ 100 |
| `BoundedStack(int)` | Creator | สร้างกล่องว่าง กำหนดความจุ |
| `BoundedStack(List)` | Creator | สร้างจาก list ความจุ 100 |
| `BoundedStack(int, List)` | Creator | สร้างจาก list กำหนดความจุ |
| `push(String)` | Mutator | เพิ่มข้อความต่อท้าย |
| `pop()` | Mutator | ลบข้อความตัวท้ายสุด |
| `size()` | Observer | คืนจำนวนข้อความ |
| `contains(String)` | Observer | เช็คว่ามีข้อความนี้ไหม |
| `box()` | Observer | คืนสำเนารายการข้อความ |
| `shuffled()` | Producer | คืนกล่องใหม่ที่สลับลำดับ |

## พฤติกรรมเมื่อเจอ input ผิด

โค้ดแยกแยะระหว่าง "input ผิดกฎ" กับ "ทำไม่ได้ตอนนี้":

| สถานการณ์ | ผลลัพธ์ |
|---|---|
| `push` หรือ constructor เจอข้อความ `null` / ว่าง | โยน `IllegalArgumentException` |
| constructor เจอข้อความซ้ำใน list | โยน `IllegalArgumentException` |
| `capacity` ≤ 0 หรือ > 100,000 | โยน `IllegalArgumentException` |
| `push` ข้อความที่มีอยู่แล้ว (ซ้ำ) | คืน `false` |
| `push` ตอนกล่องเต็ม | คืน `false` |
| `pop` ตอนกล่องว่าง | โยน `IllegalArgumentException` |

หลักการ: ข้อความผิดกฎ (null/ว่าง/ซ้ำใน list) คือ bug ของคนเรียก จึงโยน exception เพื่อให้รู้ตัวทันที ส่วนกรณีที่ input ถูกต้องแต่ทำไม่ได้ตอนนั้น (ซ้ำ/เต็ม) เป็นเหตุการณ์ปกติ จึงคืน `false`

## การทดสอบ

ไฟล์ `BoundedStacktest.java` มีชุดเทสครอบคลุม creators, mutators, observers, producers และ representation exposure

### รันเทส

```bash
javac BoundedStack.java BoundedStacktest.java
java -ea BoundedStack.BoundedStacktest
```

> **หมายเหตุ:** ต้องใส่ `-ea` เพื่อเปิด assertion ให้ `checkRep()` ทำงาน มิฉะนั้นการตรวจกฎภายในจะถูกข้าม

ผลลัพธ์จะแสดง `[PASS]` / `[Failed]` ของแต่ละเคส พร้อมสรุปจำนวนท้ายสุด

## โครงสร้างไฟล์

```
BoundedStack/
├── BoundedStack.java        # คลาสหลัก (ADT)
├── BoundedStacktest.java    # ชุดเทส
└── README.md
```

## แนวคิดที่ใช้

- **Abstract Data Type (ADT)** — ซ่อนรายละเอียดการเก็บข้อมูล เปิดเฉพาะ operation ที่กำหนด
- **Representation Invariant (RI)** — กฎที่ข้อมูลภายในต้องเป็นจริงเสมอ ตรวจด้วย `checkRep()`
- **Defensive Copying** — คืนสำเนาแทนตัวจริงใน `box()` และ copy input ใน constructor เพื่อกัน rep exposure
- **Fail Fast** — โยน exception ทันทีเมื่อเจอ input ผิดกฎ แทนที่จะปล่อยให้ error ไปโผล่ที่อื่น
