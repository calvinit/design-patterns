package iterator

import (
	"testing"
)

func TestIterator(t *testing.T) {
	var list MyList[string] = NewMyArrayList[string]()
	list.Add("San Zhang")
	list.Add("Si Li")
	list.Add("Wu Wang")

	itr := list.Iterator()
	for itr.HasNext() {
		element, err := itr.Next()
		if err != nil {
			t.Fatal(err)
		}
		t.Logf(">>> 当前元素：%s", element)
		err = itr.Remove()
		// err = list.Remove(1)
		if err != nil {
			t.Fatal(err)
		}
	}
	t.Logf("[Array List] --> %s", list)

	t.Log("==========================================")

	list = NewMyLinkedList[string]()
	list.Add("San Zhang")
	list.Add("Si Li")
	list.Add("Wu Wang")

	itr = list.Iterator()
	for itr.HasNext() {
		element, err := itr.Next()
		if err != nil {
			t.Fatal(err)
		}
		t.Logf(">>> 当前元素：%s", element)
		err = itr.Remove()
		// err = list.Remove(2)
		if err != nil {
			t.Fatal(err)
		}
	}
	t.Logf("[Linked List] --> %s", list)
}
