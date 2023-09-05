package singleton

import "testing"

func TestHungrySingleton(t *testing.T) {
	hungry1 := NewHungrySingleton()
	hungry2 := NewHungrySingleton()
	t.Logf("1. 饿汉式: (1)=%v, (2)=%v, (1==2?)=%t, (1.id)=%d, (2.id)=%d\n",
		hungry1, hungry2, hungry1 == hungry2, hungry1.GetID(), hungry2.GetID())
}

func TestLazySingleton(t *testing.T) {
	lazy1 := NewLazySingleton()
	lazy2 := NewLazySingleton()
	t.Logf("2. 懒汉式: (1)=%v, (2)=%v, (1==2?)=%t, (1.id)=%d, (2.id)=%d\n",
		lazy1, lazy2, lazy1 == lazy2, lazy1.GetID(), lazy2.GetID())
}
