import java.util.Scanner;

public class MementoTest {
    public static void main(String[] args) {
        InputText inputText = new InputText();
        SnapshotHolder snapshotHolder = new SnapshotHolder();
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        while (scanner.hasNext()) {
            System.out.print("> ");
            String input = scanner.next();
            // 这里不会出现 null、""、换行回车 等输入的情况
            switch (input) {
                case ":list" -> System.out.printf("%s \n> ", inputText.getText());
                case ":undo" -> {
                    Snapshot snapshot = snapshotHolder.popSnapshot();
                    if (snapshot != null) {
                        inputText.restoreSnapshot(snapshot);
                    }
                }
                case ":exit" -> {
                    scanner.close();
                    System.exit(0);
                }
                default -> {
                    snapshotHolder.pushSnapshot(inputText.createSnapshot());
                    inputText.append(input);
                }
            }
        }
    }
}
