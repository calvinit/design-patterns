import java.util.ArrayList;
import java.util.List;

/**
 * 多态是一种「动态绑定」，可以在运行时获取对象的实际类型，来运行实际类型对应的方法。
 * 而函数重载是一种「静态绑定」，在编译时并不能获取对象的实际类型，而是根据声明类型执行声明类型对应的方法。
 */
public class VisitorTest {
    public static void main(String[] args) {
        List<ResourceFile> resourceFiles = listAllResourceFiles("~/");

        Extractor extractor = new Extractor();
        for (ResourceFile resourceFile : resourceFiles) {
            // extractor.visit(resourceFile);
            resourceFile.accept(extractor);
        }

        System.out.println("==========================================");

        Compressor compressor = new Compressor();
        for (ResourceFile resourceFile : resourceFiles) {
            // compressor.visit(resourceFile);
            resourceFile.accept(compressor);
        }
    }

    private static List<ResourceFile> listAllResourceFiles(@SuppressWarnings("SameParameterValue")
                                                           String resourceDirectory) {
        List<ResourceFile> resourceFiles = new ArrayList<>();
        resourceFiles.add(new PdfFile(resourceDirectory + "a.pdf"));
        resourceFiles.add(new WordFile(resourceDirectory + "b.docx"));
        resourceFiles.add(new PPTFile(resourceDirectory + "c.ppt"));
        return resourceFiles;
    }
}
