import java.util.ArrayList;
import java.util.List;

class Content {
    private String content;

    public Content(String content) {
        this.content = content;
    }

    public String get() {
        return content;
    }

    public void set(String content) {
        this.content = content;
    }
}

// ============================================================

interface SensitiveWordFilter1 {
    boolean doFilter(Content content);
}

class SexyWordFilter1 implements SensitiveWordFilter1 {
    @Override
    public boolean doFilter(Content content) {
        System.out.println("1. 「情色内容」过滤中。。。");
        boolean legal = !content.get().contains("涉黄");
        legal = legal && !content.get().contains("儿童不宜");
        // 返回内容是否合法，true 则是合法的，会继续执行链中的下一个过滤器（如果有的话）
        return legal;
    }
}

class PoliticalWordFilter1 implements SensitiveWordFilter1 {
    @Override
    public boolean doFilter(Content content) {
        System.out.println("1. 「政治敏感内容」过滤中。。。");
        boolean legal = !content.get().contains("反动");
        legal = legal && !content.get().contains("不符合社会价值观");
        return legal;
    }
}

class AdsWordFilter1 implements SensitiveWordFilter1 {
    @Override
    public boolean doFilter(Content content) {
        System.out.println("1. 「广告内容」过滤中。。。");
        return !content.get().contains("广告");
    }
}

class SensitiveWordFilterChain1 {
    // 使用集合存储所有过滤器
    private final List<SensitiveWordFilter1> filters = new ArrayList<>();

    public void addFilter(SensitiveWordFilter1 filter) {
        filters.add(filter);
    }

    /**
     * 过滤内容
     *
     * @return true 如果内容不包含敏感词
     */
    public boolean filter(Content content) {
        for (SensitiveWordFilter1 filter : filters) {
            if (!filter.doFilter(content)) {
                return false;
            }
        }
        return true;
    }
}

// ============================================================

interface SensitiveWordFilter2 {
    void doFilter(Content content);
}

abstract class Successor implements SensitiveWordFilter2 {
    protected Successor successor;  // next 后缀过滤器

    public void setSuccessor(Successor successor) {
        this.successor = successor;
    }

    public final void filter(Content content) {
        doFilter(content);
        if (successor != null) {
            // 另一种变体，所有请求都会被所有过滤器过滤一遍，不存在中途终止的情况
            successor.filter(content);
        }
    }
}

class SexyWordFilter2 extends Successor {
    @Override
    public void doFilter(Content content) {
        System.out.println("2. 「情色内容」过滤中。。。");
        String c = content.get().replace("涉黄", "**").replace("儿童不宜", "****");
        content.set(c);
    }
}

class PoliticalWordFilter2 extends Successor {
    @Override
    public void doFilter(Content content) {
        System.out.println("2. 「政治敏感内容」过滤中。。。");
        String c = content.get().replace("反动", "##").replace("不符合社会价值观", "########");
        content.set(c);
    }
}

class AdsWordFilter2 extends Successor {
    @Override
    public void doFilter(Content content) {
        System.out.println("2. 「广告内容」过滤中。。。");
        String c = content.get().replace("广告", "$$");
        content.set(c);
    }
}

class SensitiveWordFilterChain2 {
    // 使用链表存储过滤器，head 表示当前执行到的过滤器，tail 表示过滤器链的末尾过滤器（方便在链表末尾添加新的过滤器）
    private Successor head;

    private Successor tail;

    public void addFilter(Successor filter) {
        filter.setSuccessor(null);  // 保证它不会自带后继过滤器（否则 tail 就需要递归获取到最后的过滤器来重新指定）
        // 初次添加首个过滤器的情况
        if (head == null) {
            head = filter;
            tail = filter;
            return;
        }
        tail.setSuccessor(filter);
        tail = filter;
    }

    public void filter(Content content) {
        if (head != null) {
            head.filter(content);
        }
    }
}

// ============================================================

interface SensitiveWordFilter3 {
    void doFilter(Content content, SensitiveWordFilterChain3 chain);
}

class SexyWordFilter3 implements SensitiveWordFilter3 {
    @Override
    public void doFilter(Content content, SensitiveWordFilterChain3 chain) {
        System.out.println("3.1 「情色内容」过滤中。。。");
        String c = content.get().replace("涉黄", "**").replace("儿童不宜", "****");
        content.set(c);
        System.out.println("3.1 拦截请求，请求前的帖子内容为：" + content.get());
        chain.filter(content);
        System.out.println("3.1 拦截响应，响应前的帖子内容为：" + content.get());
    }
}

class PoliticalWordFilter3 implements SensitiveWordFilter3 {
    @Override
    public void doFilter(Content content, SensitiveWordFilterChain3 chain) {
        System.out.println("3.2 「政治敏感内容」过滤中。。。");
        String c = content.get().replace("反动", "##").replace("不符合社会价值观", "########");
        content.set(c);
        System.out.println("3.2 拦截请求，请求前的帖子内容为：" + content.get());
        chain.filter(content);
        System.out.println("3.2 拦截响应，响应前的帖子内容为：" + content.get());
    }
}

class AdsWordFilter3 implements SensitiveWordFilter3 {
    @Override
    public void doFilter(Content content, SensitiveWordFilterChain3 chain) {
        System.out.println("3.3 「广告内容」过滤中。。。");
        String c = content.get().replace("广告", "$$");
        content.set(c);
        System.out.println("3.3 拦截请求，请求前的帖子内容为：" + content.get());
        chain.filter(content);
        System.out.println("3.3 拦截响应，响应前的帖子内容为：" + content.get());
    }
}

interface Something {
    void justDoIt(Content content);
}

class SensitiveWordFilterChain3 {
    // 使用数组存储所有过滤器
    private SensitiveWordFilter3[] filters = new SensitiveWordFilter3[]{};

    private int pos = 0; // 当前执行到了哪个 filter
    private int n;       // filter 的个数

    private final Something something;

    private static final int INCREMENT = 16;

    public SensitiveWordFilterChain3(Something something) {
        this.something = something;
    }

    public void addFilter(SensitiveWordFilter3 filter) {
        for (SensitiveWordFilter3 f : filters) {
            if (filter == f) return;
        }

        // 扩容
        if (n == filters.length) {
            SensitiveWordFilter3[] newFilters = new SensitiveWordFilter3[n + INCREMENT];
            System.arraycopy(filters, 0, newFilters, 0, n);
            filters = newFilters;
        }

        filters[n++] = filter;
    }

    public void filter(Content content) {
        if (pos < n) {
            SensitiveWordFilter3 filter = filters[pos++];
            // 这里其实是递归调用 chain.filter，参考 Tomcat 的过滤器链的实现和使用去理解
            filter.doFilter(content, this);
        } else {
            // filter 都处理完毕后，执行业务
            something.justDoIt(content);
        }
    }
}