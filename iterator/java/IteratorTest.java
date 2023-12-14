public class IteratorTest {
    public static void main(String[] args) {
        MyList<String> list = new MyArrayList<>();
        list.add("San Zhang");
        list.add("Si Li");
        list.add("Wu Wang");

        MyIterator<String> itr = list.iterator();
        while (itr.hasNext()) {
            String element = itr.next();
            System.out.println(">>> 当前元素：" + element);
            itr.remove();
            // list.remove(1);
        }
        System.out.println("[Array List] --> " + list);

        System.out.println("==========================================");

        list = new MyLinkedList<>();
        list.add("San Zhang");
        list.add("Si Li");
        list.add("Wu Wang");

        itr = list.iterator();
        while (itr.hasNext()) {
            String element = itr.next();
            System.out.println(">>> 当前元素：" + element);
            itr.remove();
            // list.remove(2);
        }
        System.out.println("[Linked List] --> " + list);
    }
}
