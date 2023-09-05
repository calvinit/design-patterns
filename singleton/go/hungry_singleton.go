package singleton

import "sync/atomic"

type HungrySingleton struct {
	ID *atomic.Int64
}

func (h *HungrySingleton) GetID() (next int64) {
	return h.ID.Add(1)
}

var hungry *HungrySingleton

func init() {
	hungry = &HungrySingleton{
		ID: new(atomic.Int64),
	}
}

func NewHungrySingleton() *HungrySingleton {
	return hungry
}
