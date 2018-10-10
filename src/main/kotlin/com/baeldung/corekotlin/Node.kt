package com.baeldung.corekotlin

class Node(
        var key: Int,
        var left: Node? = null,
        var right: Node? = null) {

    /**
     * Return a node with given value. If no such node exists, return null.
     * @param value
     */
    fun find(value: Int): Node? = when {
        this.key > value -> left?.find(value)
        this.key < value -> right?.find(value)
        else -> this

    }

    /**
     * Insert a given value into the tree.
     * After insertion, the tree should contain a node with the given value.
     * If the tree already contains the given value, nothing is performed.
     * @param value
     */
    fun insert(value: Int) {
        if (value > this.key) {
            if (this.right == null) {
                this.right = Node(value)
            } else {
                this.right?.insert(value)
            }
        } else if (value < this.key) {
            if (this.left == null) {
                this.left = Node(value)
            } else {
                this.left?.insert(value)
            }
        }
    }

    fun delete(value: Int) {
        when {
            value == key -> removeNode(this, null)
            value > key -> scan(value, this.right, this)
            value < key -> scan(value, this.left, this)
            else -> throw RuntimeException("Should never enter here")
        }
    }

    private fun scan(value: Int, node: Node?, parent: Node?) {
        if (node == null) {
            System.out.println("value " + value
                    + " seems not present in the tree.")
            return
        }
        when {
            value > node.key -> scan(value, node.right, node)
            value < node.key -> scan(value, node.left, node)
            value == node.key -> removeNode(node, parent)
        }

    }

    private fun removeNode(node: Node, parent: Node?) {
        node.left?.let { leftChild ->
            run {
                node.right?.let {
                    removeTwoChildNode(node)
                } ?: removeSingleChildNode(node, leftChild)
            }
        } ?: run {
            node.right?.let { rightChild -> removeSingleChildNode(node, rightChild) } ?: removeNoChildNode(node, parent)
        }


    }

    private fun removeNoChildNode(node: Node, parent: Node?) {
        if (parent == null) {
            throw IllegalStateException(
                    "Can not remove the root node without child nodes")
        }
        if (node == parent.left) {
            parent.left = null
        } else if (node == parent.right) {
            parent.right = null
        }
    }

    private fun removeTwoChildNode(node: Node) {
        System.out.println("Removing a node with two children: " + node.key)
        val leftChild = node.left!!
        leftChild.right?.let {
            val maxParent = findParentOfMaxChild(node.left!!)
            println("parent of max node ${node.left!!.key} -> ${maxParent.key}")
            maxParent.right?.let {
                node.key = it.key
                maxParent.right = null
            } ?: throw IllegalStateException("Node with max child must have the right child!")

        } ?: run {
            node.key = leftChild.key
            node.left = leftChild.left
        }

    }

    /**
     * Return a node whose right child contains the biggest value in the given sub-tree.
     * Assume that the node n has a non-null right child.
     *
     * @param n
     */
    private fun findParentOfMaxChild(n: Node): Node {
        return n.right?.let { r -> r.right?.let { findParentOfMaxChild(r) } ?: n }
                ?: throw IllegalArgumentException("Right child must be non-null")

    }

    private fun removeSingleChildNode(node: Node, child: Node) {
        node.key = child.key
        node.left = child.left
        node.right = child.right
    }
}

