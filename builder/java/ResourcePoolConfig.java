public class ResourcePoolConfig {
    /**
     * 假设 name 为必填项，其他非必填项
     */
    private final String name;
    private final int maxTotal;
    private final int maxIdle;
    private final int minIdle;

    /**
     * 如果私有化构造函数，那么它只能由内部类 Builder 调用
     */
    private ResourcePoolConfig(Builder builder) {
        this.name = builder.name;
        this.maxTotal = builder.maxTotal;
        this.maxIdle = builder.maxIdle;
        this.minIdle = builder.minIdle;
    }

    /**
     * 如果只提供 Getter 方法，那么对象在 build 出来了之后它就是只读的（不能再通过 Setter 方法修改了，事实上因此属性也被设置为 final 的）
     */
    public String getName() {
        return name;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    /**
     * Builder 可以设计成 ResourcePoolConfig 的内部类，也可以设计成独立的非内部类 ResourcePoolConfigBuilder
     */
    public static class Builder {
        private static final int DEFAULT_MAX_TOTAL = 8;
        private static final int DEFAULT_MAX_IDLE = 8;
        private static final int DEFAULT_MIN_IDLE = 0;

        private String name;
        /**
         * 非必填项，初始赋予默认值
         */
        private int maxTotal = DEFAULT_MAX_TOTAL;
        private int maxIdle = DEFAULT_MAX_IDLE;
        private int minIdle = DEFAULT_MIN_IDLE;

        /**
         * 整体的属性校验逻辑可以放到这里来做，包括必填项校验、依赖关系校验、约束条件校验等，因为它是对象构建中最后一个被调用的方法
         */
        public ResourcePoolConfig build() {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("name cannot be blank");
            }
            if (maxIdle > maxTotal) {
                throw new IllegalArgumentException("maxIdle cannot be greater than maxTotal");
            }
            if (minIdle > maxTotal || minIdle > maxIdle) {
                throw new IllegalArgumentException("minIdle cannot be greater than maxTotal or maxIdle");
            }
            // 校验通过后，返回构建的对象
            return new ResourcePoolConfig(this);
        }

        /**
         * Setter 方法，可使用属性名称作为方法名，或者 setName 作为方法名
         */
        public Builder name(String name) {
            // 这个必填项校验此处可不做，因为 build 中已经处理（build 中必须提供兜底担保，因为本 Setter 方法可能不会被调用）
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("name cannot be blank");
            }
            this.name = name;
            return this;
        }

        public Builder maxTotal(int maxTotal) {
            if (maxTotal < 0) {
                throw new IllegalArgumentException("maxTotal cannot be less than zero");
            }
            this.maxTotal = maxTotal;
            return this;
        }

        public Builder maxIdle(int maxIdle) {
            if (maxIdle < 0) {
                throw new IllegalArgumentException("maxIdle cannot be less than zero");
            }
            this.maxIdle = maxIdle;
            return this;
        }

        public Builder minIdle(int minIdle) {
            if (minIdle < 0) {
                throw new IllegalArgumentException("minIdle cannot be less than zero");
            }
            this.minIdle = minIdle;
            return this;
        }
    }

    @Override
    public String toString() {
        return "ResourcePoolConfig{" +
                "name='" + name + '\'' +
                ", maxTotal=" + maxTotal +
                ", maxIdle=" + maxIdle +
                ", minIdle=" + minIdle +
                '}';
    }
}
