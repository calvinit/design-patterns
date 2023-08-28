package singleton

import "sync/atomic"

type HungrySingleton struct {
	id *atomic.Int64
}

func (h *HungrySingleton) getID() (next int64) {
	return h.id.Add(1)
}

var hungry *HungrySingleton

func init() {
	hungry = &HungrySingleton{
		id: new(atomic.Int64),
	}
}

func NewHungrySingleton() *HungrySingleton {
	return hungry
}
