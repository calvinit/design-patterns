import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Stream;

abstract class FileSystemNode {
    protected String path;

    public FileSystemNode(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public abstract int countNumOfFiles();

    public abstract long countSizeOfFiles();

    @Override
    public String toString() {
        return String.format("{path='%s', fileNums=%d, fileSizes=%d}", path, countNumOfFiles(), countSizeOfFiles());
    }
}

// ============================================================

/**
 * 文件
 */
class File extends FileSystemNode {

    public File(String path) {
        super(path);
    }

    @Override
    public int countNumOfFiles() {
        return 1;
    }

    @Override
    public long countSizeOfFiles() {
        return new java.io.File(path).length();
    }

    @Override
    public String toString() {
        return String.format("File%s", super.toString());
    }
}

// ============================================================

/**
 * 目录
 */
class Directory extends FileSystemNode {
    private final Directory parentDir;
    private final SubNodes subNodes = new SubNodes();

    private final LongAdder countNumAdder = new LongAdder();
    private final LongAdder countSizeAdder = new LongAdder();

    public Directory(String path) {
        this(path, null);
    }

    public Directory(String path, Directory parentDir) {
        super(path);
        this.parentDir = parentDir;
    }

    @Override
    public int countNumOfFiles() {
        return countNumAdder.intValue();
    }

    @Override
    public long countSizeOfFiles() {
        return countSizeAdder.longValue();
    }

    private void incrCachedCountNumOfFiles(int delta) {
        countNumAdder.add(delta);
        if (parentDir != null) {
            parentDir.incrCachedCountNumOfFiles(delta);
        }
    }

    private void decrCachedCountNumOfFiles(int delta) {
        countNumAdder.add(-delta);
        if (parentDir != null) {
            parentDir.decrCachedCountNumOfFiles(delta);
        }
    }

    private void incrCachedCountSizeOfFiles(long delta) {
        countSizeAdder.add(delta);
        if (parentDir != null) {
            parentDir.incrCachedCountSizeOfFiles(delta);
        }
    }

    private void decrCachedCountSizeOfFiles(long delta) {
        countSizeAdder.add(-delta);
        if (parentDir != null) {
            parentDir.decrCachedCountSizeOfFiles(delta);
        }
    }

    public void addSubNode(FileSystemNode fsn) {
        if (subNodes.add(fsn)) {
            incrCachedCountNumOfFiles(fsn.countNumOfFiles());
            incrCachedCountSizeOfFiles(fsn.countSizeOfFiles());
        }
    }

    public void removeSubNode(FileSystemNode fsn) {
        if (subNodes.remove(fsn)) {
            decrCachedCountNumOfFiles(fsn.countNumOfFiles());
            decrCachedCountSizeOfFiles(fsn.countSizeOfFiles());
        }
    }

    @Override
    public String toString() {
        return String.format("Directory{path='%s', fileNums=%d, fileSizes=%d, subs=%s}",
                path, countNumOfFiles(), countSizeOfFiles(), subNodes);
    }
}

/**
 * 目录下的子节点
 */
class SubNodes {
    // 注意这里是 FileSystemNode 列表，即 Directory 的抽象父类的列表（这样能组合 File 和 Directory）
    private final List<FileSystemNode> nodes = new ArrayList<>();

    public int indexOf(FileSystemNode fsn) {
        int size = nodes.size();
        boolean found = false;
        int i = 0;
        for (; i < size; i++) {
            if (nodes.get(i).getPath().equalsIgnoreCase(fsn.getPath())) {
                found = true;
                break;
            }
        }
        return found ? i : -1;
    }

    public boolean add(FileSystemNode fsn) {
        if (indexOf(fsn) == -1) {
            nodes.add(fsn);
            return true;
        }
        return false;
    }

    public boolean remove(FileSystemNode fsn) {
        int i;
        if ((i = indexOf(fsn)) != -1) {
            nodes.remove(i);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return nodes.toString();
    }
}

// ============================================================

public class Composite {
    private void walk(Directory parentDir, String path) {
        try (Stream<Path> stream = Files.list(Path.of(path))) {
            stream.forEach(p -> {
                java.io.File file = p.toFile();
                String fullPath = file.getAbsolutePath();
                if (file.isFile()) {
                    File ff = new File(fullPath);
                    parentDir.addSubNode(ff);
                } else {
                    Directory dir = new Directory(fullPath, parentDir);
                    parentDir.addSubNode(dir);
                    walk(dir, fullPath);
                }
            });
        } catch (IOException ignore) {}
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    public FileSystemNode buildFileSystemTree(String basePath) {
        java.io.File root = new java.io.File(basePath);
        if (!root.exists()) {
            return null;
        }
        if (root.isFile()) {
            return new File(basePath);
        }
        Directory directory = new Directory(basePath);
        walk(directory, basePath);
        directory.removeSubNode(new File("/Users/calvinit/Downloads/ll.txt"));
        return directory;
    }
}
