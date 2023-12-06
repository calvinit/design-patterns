public class ChainOfResponsibilityTest {
    public static void main(String[] args) throws InterruptedException {
        String content = "这是一条帖子内容，包含了一些需要过滤的关键词，例如：广告、涉黄、反动 等等。。。";

        SensitiveWordFilterChain1 filterChain1 = new SensitiveWordFilterChain1();
        filterChain1.addFilter(new SexyWordFilter1());
        filterChain1.addFilter(new PoliticalWordFilter1());
        filterChain1.addFilter(new AdsWordFilter1());
        boolean legal = filterChain1.filter(new Content(content));
        if (legal) {
            System.out.println("1. 恭喜！帖子内容经审查没有问题，可以正常发布！");
        } else {
            System.out.println("1. WARNING! 帖子内容经审查存在问题，无法正常发布！");
        }

        System.out.println("==========================================");

        SensitiveWordFilterChain2 filterChain2 = new SensitiveWordFilterChain2();
        filterChain2.addFilter(new SexyWordFilter2());
        filterChain2.addFilter(new PoliticalWordFilter2());
        filterChain2.addFilter(new AdsWordFilter2());
        Content c = new Content(content);
        System.out.println("2. 帖子内容原文内容为：【" + c.get() + "】！");
        filterChain2.filter(c);
        System.out.println("2. 经审查过滤后的内容为：【" + c.get() + "】，请按此内容发布！");

        System.out.println("==========================================");

        SensitiveWordFilterChain3 filterChain3 = new SensitiveWordFilterChain3(new ConcreateThing());
        filterChain3.addFilter(new SexyWordFilter3());
        filterChain3.addFilter(new PoliticalWordFilter3());
        filterChain3.addFilter(new AdsWordFilter3());
        filterChain3.filter(new Content(content));
    }
}

class ConcreateThing implements Something {
    @Override
    public void justDoIt(Content content) {
        content.set("业务处理中，当前帖子内容被加工，这是修改了后的内容！");
        System.out.println(">>> 3. 正在做具体的事情，帖子内容为：" + content.get());
    }
}