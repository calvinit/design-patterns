public class BuilderTest {
    public static void main(String[] args) {
        ResourcePoolConfig config = new ResourcePoolConfig.Builder()
                .name("jdbc-mysql.properties")
                .maxTotal(30)
                .maxIdle(30)
                .minIdle(15)
                .build();
        System.out.printf("建造者模式: %s%n", config);
    }
}
