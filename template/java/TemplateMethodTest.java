public class TemplateMethodTest {
    public static void main(String[] args) {
        Recipient recipient = new Recipient("张三", "13800138000", "zhangsan@email.com", "地球村");
        String message = "这是一条有用的消息，请查阅！";

        ISender sender = new SMSSender();
        sender.send(recipient, message);

        System.out.println("==========================================");

        sender = new EmailSender();
        sender.send(recipient, message);

        System.out.println("==========================================");

        sender = new LetterSender();
        sender.send(recipient, message);
    }
}