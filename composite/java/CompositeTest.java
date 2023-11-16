public class CompositeTest {
    public static void main(String[] args) {
        Composite composite = new Composite();
        FileSystemNode fileSystemTree = composite.buildFileSystemTree(System.getProperty("user.home") + "/Downloads");
        System.out.println(fileSystemTree);
    }
}
