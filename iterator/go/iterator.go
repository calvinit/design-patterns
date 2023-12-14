package iterator

import (
	"errors"
	"fmt"
	"slices"
)

type MyList[E any] interface {
	Add(e E)
	Remove(index int) error
	Get(index int) (E, error)
	Size() int
	Iterator() MyIterator[E]
}

func zero[E any]() E {
	var e E
	return e
}

type MyArrayList[E any] struct {
	elements []E
	modCount int // 修改次数，每 add 或 remove 一次就加 1
}

func NewMyArrayList[E any]() *MyArrayList[E] {
	var elements []E
	return &MyArrayList[E]{
		elements: elements,
		modCount: 0,
	}
}

func (al *MyArrayList[E]) Add(e E) {
	al.elements = append(al.elements, e)
	al.modCount++
}

func (al *MyArrayList[E]) Remove(index int) error {
	if index < 0 || index >= al.Size() {
		return fmt.Errorf("index out of range: %d, size: %d", index, al.Size())
	}
	al.elements = slices.Delete(al.elements, index, index+1)
	al.modCount++
	return nil
}

func (al *MyArrayList[E]) Get(index int) (E, error) {
	if index < 0 || index >= al.Size() {
		return zero[E](), fmt.Errorf("index out of range: %d, size: %d", index, al.Size())
	}
	return al.elements[index], nil
}

func (al *MyArrayList[E]) Size() int {
	return len(al.elements)
}

func (al *MyArrayList[E]) Iterator() MyIterator[E] {
	return &MyArrayItr[E]{
		Itr:              NewItr[E](al),
		expectedModCount: al.modCount,
	}
}

func (al *MyArrayList[E]) String() string {
	return fmt.Sprint(al.elements)
}

type MyLinkedList[E any] struct {
	first    *node[E]
	last     *node[E]
	size     int
	modCount int // 修改次数，每 add 或 remove 一次就加 1
}

func NewMyLinkedList[E any]() *MyLinkedList[E] {
	return &MyLinkedList[E]{}
}

type node[E any] struct {
	element E
	next    *node[E]
	prev    *node[E]
}

func newNode[E any](prev *node[E], element E, next *node[E]) *node[E] {
	return &node[E]{
		element: element,
		next:    next,
		prev:    prev,
	}
}

func (ll *MyLinkedList[E]) Add(e E) {
	l := ll.last
	n := newNode(l, e, nil)
	ll.last = n
	if l == nil {
		ll.first = n
	} else {
		l.next = n
	}
	ll.size++
	ll.modCount++
}

func (ll *MyLinkedList[E]) Remove(index int) error {
	if index < 0 || index >= ll.size {
		return fmt.Errorf("index out of range: %d, size: %d", index, ll.size)
	}
	x := ll.node(index)
	prev, next := x.prev, x.next

	if prev == nil {
		ll.first = next
	} else {
		prev.next = next
		x.prev = nil
	}

	if next == nil {
		ll.last = prev
	} else {
		next.prev = prev
		x.next = nil
	}

	x.element = zero[E]()
	ll.size--
	ll.modCount++
	return nil
}

func (ll *MyLinkedList[E]) Get(index int) (E, error) {
	if index < 0 || index >= ll.size {
		return zero[E](), fmt.Errorf("index out of range: %d, size: %d", index, ll.size)
	}
	return ll.node(index).element, nil
}

func (ll *MyLinkedList[E]) node(index int) *node[E] {
	var n *node[E]
	if index < (ll.size >> 1) {
		// 从前往后搜索
		i := 0
		n = ll.first
		for i < index {
			n = n.next
			i++
		}
	} else {
		// 从后往前搜索
		i := ll.size - 1
		n = ll.last
		for i > index {
			n = n.prev
			i--
		}
	}
	return n
}

func (ll *MyLinkedList[E]) Size() int {
	return ll.size
}

func (ll *MyLinkedList[E]) Iterator() MyIterator[E] {
	return &MyLinkedItr[E]{
		Itr:              NewItr[E](ll),
		expectedModCount: ll.modCount,
	}
}

func (ll *MyLinkedList[E]) String() string {
	n := ll.first
	if n == nil {
		return "[]"
	}
	var elements []E
	elements = append(elements, n.element)
	i := 1
	for i < ll.size {
		elements = append(elements, ll.node(i).element)
	}
	return fmt.Sprint(elements)
}

// ============================================================

type MyIterator[E any] interface {
	HasNext() bool
	Next() (E, error)
	Remove() error
}

type Itr[E any] struct {
	cursor  int // 当前遍历到的元素游标
	lastRet int // 指向当前游标的前一个元素，-1 表示没有
	list    MyList[E]
}

func NewItr[E any](list MyList[E]) *Itr[E] {
	return &Itr[E]{
		cursor:  0,
		lastRet: -1,
		list:    list,
	}
}

func (i *Itr[E]) HasNext() bool {
	return i.cursor != i.list.Size() // 注意这里：cursor 在指向最后一个元素的时候，HasNext() 仍旧返回 true
}

func (i *Itr[E]) Next() (E, error) {
	if i.cursor >= i.list.Size() {
		return zero[E](), fmt.Errorf("NoSuchElementException: Overflow cursor: %d", i.cursor)
	}
	i.lastRet = i.cursor
	i.cursor++
	return i.list.Get(i.lastRet)
}

func (i *Itr[E]) Remove() error {
	if i.lastRet < 0 {
		return errors.New("IllegalStateException: Next() is neccessary before Remove()")
	}
	if err := i.list.Remove(i.lastRet); err != nil {
		return err
	}
	i.cursor, i.lastRet = i.lastRet, -1
	return nil
}

type MyArrayItr[E any] struct {
	*Itr[E]
	expectedModCount int // 期望的修改次数，当和 MyList 的 modCount 不一致时，说明在遍历过程中 MyList 已经发生了改变
}

func (ai *MyArrayItr[E]) Next() (E, error) {
	if err := ai.checkForComodification(); err != nil {
		return zero[E](), err
	}
	return ai.Itr.Next()
}

func (ai *MyArrayItr[E]) Remove() error {
	if err := ai.checkForComodification(); err != nil {
		return err
	}
	if err := ai.Itr.Remove(); err != nil {
		return err
	}
	ai.expectedModCount = ai.list.(*MyArrayList[E]).modCount
	return nil
}

func (ai *MyArrayItr[E]) checkForComodification() error {
	if ai.list.(*MyArrayList[E]).modCount != ai.expectedModCount {
		return errors.New("ConcurrentModificationException")
	}
	return nil
}

type MyLinkedItr[E any] MyArrayItr[E]

func (li *MyLinkedItr[E]) Next() (E, error) {
	if err := li.checkForComodification(); err != nil {
		return zero[E](), err
	}
	return li.Itr.Next()
}

func (li *MyLinkedItr[E]) Remove() error {
	if err := li.checkForComodification(); err != nil {
		return err
	}
	if err := li.Itr.Remove(); err != nil {
		return err
	}
	li.expectedModCount = li.list.(*MyLinkedList[E]).modCount
	return nil
}

func (li *MyLinkedItr[E]) checkForComodification() error {
	if li.list.(*MyLinkedList[E]).modCount != li.expectedModCount {
		return errors.New("ConcurrentModificationException")
	}
	return nil
}
