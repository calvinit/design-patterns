record Recipient(
        String name,
        String phone,
        String email,
        String address
) {}

interface ISender {
    void send(Recipient recipient, String message);
}

abstract class Sender implements ISender {
    protected abstract boolean recipientIsValid(Recipient recipient);

    protected abstract boolean messageIsValid(String message);

    protected abstract boolean doSend(Recipient recipient, String message);

    @Override
    public final void send(Recipient recipient, String message) {
        if (!recipientIsValid(recipient)) {
            System.err.println("ERROR! 收件人信息不合法，无法发送消息！");
            return;
        }
        if (!messageIsValid(message)) {
            System.err.println("ERROR! 消息内容不合法，无法发送消息！");
            return;
        }
        if (doSend(recipient, message)) {
            System.out.println("恭喜！消息发送成功！");
        } else {
            System.err.println("WARNING! 消息发送失败！");
        }
    }
}

class SMSSender extends Sender {
    @Override
    protected boolean recipientIsValid(Recipient recipient) {
        String phone = recipient.phone();
        return phone != null && phone.matches("^1[3456789]\\d{9}$");
    }

    @Override
    protected boolean messageIsValid(String message) {
        return message != null && message.length() <= 500;
    }

    @Override
    protected boolean doSend(Recipient recipient, String message) {
        System.out.printf("向「%s」的手机号码 [%s] 发送短信完成，短信内容为：【%s】！\n", recipient.name(), recipient.phone(), message);
        return true;
    }
}

class EmailSender extends Sender {
    @Override
    protected boolean recipientIsValid(Recipient recipient) {
        String email = recipient.email();
        return email != null && email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    @Override
    protected boolean messageIsValid(String message) {
        return message != null && message.getBytes().length <= 4 * 1024 * 1024;
    }

    @Override
    protected boolean doSend(Recipient recipient, String message) {
        System.out.printf("向「%s」的电子邮箱 [%s] 发送邮件完成，邮件内容为：【%s】！\n", recipient.name(), recipient.email(), message);
        return true;
    }
}

class LetterSender extends Sender {
    @Override
    protected boolean recipientIsValid(Recipient recipient) {
        String address = recipient.address();
        return address != null && !address.isBlank();
    }

    @Override
    protected boolean messageIsValid(String message) {
        return message != null && !message.isBlank();
    }

    @Override
    protected boolean doSend(Recipient recipient, String message) {
        System.out.printf("向「%s」的收件地址 [%s] 邮递信件完成，信件内容为：【%s】！\n", recipient.name(), recipient.address(), message);
        return true;
    }
}