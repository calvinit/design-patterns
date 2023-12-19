import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

record Snapshot(int bakpoint) {}

class InputText {
    private final StringBuilder text = new StringBuilder();

    public String getText() {
        return text.toString();
    }

    public void append(String input) {
        text.append(input);
    }

    public Snapshot createSnapshot() {
        return new Snapshot(text.length());
    }

    public void restoreSnapshot(Snapshot snapshot) {
        text.delete(snapshot.bakpoint(), text.length());
    }
}

class SnapshotHolder {
    // Stack 使用栈结构
    private final Deque<Snapshot> snapshots = new ConcurrentLinkedDeque<>();

    public Snapshot popSnapshot() {
        if (snapshots.isEmpty()) {
            return null;
        }
        return snapshots.pop();
    }

    public void pushSnapshot(Snapshot snapshot) {
        if (snapshots.size() >= 3) {
            snapshots.removeLast();
        }
        snapshots.push(snapshot);
    }
}