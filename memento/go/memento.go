package memento

import "bytes"

type Snapshot struct {
	Bakpoint int
}

type InputText struct {
	text bytes.Buffer
}

func (t *InputText) GetText() string {
	return t.text.String()
}

func (t *InputText) Append(input string) {
	t.text.WriteString(input)
}

func (t *InputText) CreateSnapshot() *Snapshot {
	return &Snapshot{t.text.Len()}
}

func (t *InputText) RestoreSnapshot(snapshot *Snapshot) {
	t.text.Truncate(snapshot.Bakpoint)
}

type SnapshotHolder struct {
	snapshots []*Snapshot
}

func (h *SnapshotHolder) PopSnapshot() *Snapshot {
	if len(h.snapshots) == 0 {
		return nil
	}
	v := h.snapshots[len(h.snapshots)-1]
	h.snapshots = h.snapshots[:len(h.snapshots)-1]
	return v
}

func (h *SnapshotHolder) PushSnapshot(snapshot *Snapshot) {
	if len(h.snapshots) >= 3 {
		h.snapshots = h.snapshots[1:]
	}
	h.snapshots = append(h.snapshots, snapshot)
}
