package main

// Tree is a binary tree.
type Tree[T comparable] struct {
	cmp  func(T, T) int
	root *node[T]
}

// A node in a Tree.
type node[T any] struct {
	left, right *node[T]
	val         T
}

// find returns a pointer to the node containing val,
// or, if val is not present, a pointer to where it
// would be placed if added.
func (bt *Tree[T]) find(val T) **node[T] {
	pl := &bt.root
	for *pl != nil {
		switch cmp := bt.cmp(val, (*pl).val); {
		case cmp < 0:
			pl = &(*pl).left
		case cmp > 0:
			pl = &(*pl).right
		default:
			return pl
		}
	}
	return pl
}

// Insert inserts val into bt if not already there,
// and reports whether it was inserted.
func (bt *Tree[T]) Insert(val T) bool {
	pl := bt.find(val)
	if *pl != nil {
		return false
	}
	*pl = &node[T]{val: val}
	return true
}
