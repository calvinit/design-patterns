// Visitor 接口，多个【重载】方法：执行「对象的哪个方法」，由参数对象的声明类型决定
@SuppressWarnings("ClassEscapesDefinedScope")
public interface Visitor {
    void visit(PPTFile pptFile);

    void visit(PdfFile pdfFile);

    void visit(WordFile wordFile);
}

// Visitable 接口或抽象类，由子类【继承】或【实现】：执行「哪个对象的方法」，由对象的实际类型决定
abstract class ResourceFile {
    protected String filePath;

    public ResourceFile(String filePath) {
        this.filePath = filePath;
    }

    protected abstract void accept(Visitor visitor);
}

class PPTFile extends ResourceFile {
    public PPTFile(String filePath) {
        super(filePath);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class PdfFile extends ResourceFile {
    public PdfFile(String filePath) {
        super(filePath);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class WordFile extends ResourceFile {
    public WordFile(String filePath) {
        super(filePath);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class Extractor implements Visitor {
    @Override
    public void visit(PPTFile pptFile) {
        System.out.println("Extract PPT.");
    }

    @Override
    public void visit(PdfFile pdfFile) {
        System.out.println("Extract PDF.");
    }

    @Override
    public void visit(WordFile wordFile) {
        System.out.println("Extract WORD.");
    }
}

class Compressor implements Visitor {
    @Override
    public void visit(PPTFile pptFile) {
        System.out.println("Compress PPT.");
    }

    @Override
    public void visit(PdfFile pdfFile) {
        System.out.println("Compress PDF.");
    }

    @Override
    public void visit(WordFile wordFile) {
        System.out.println("Compress WORD.");
    }
}