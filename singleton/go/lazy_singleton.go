package singleton

import (
	"sync"
	"sync/atomic"
)

type LazySingleton struct {
	id *atomic.Int64
}

func (l *LazySingleton) getID() (next int64) {
	return l.id.Add(1)
}

var (
	lazy     *LazySingleton
	lazyOnce sync.Once
)

func NewLazySingleton() *LazySingleton {
	// Once.Do 保证只会被执行一次，且在需要时（NewLazySingleton 被调用时）才会执行
	lazyOnce.Do(func() {
		println("once.do......")
		lazy = &LazySingleton{
			id: new(atomic.Int64),
		}
	})
	return lazy
}
