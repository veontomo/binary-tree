package com.baeldung.corekotlin

class Node(
        var key: Int,
        var left: Node? = null,
        var right: Node? = null) {

    /**
     * Return a node with given value. If no such node exists, return null.
     * @param value
     */
    fun findByValue(value: Int): Node? = when {
        this.key > value -> left?.findByValue(value)
        this.key < value -> right?.findByValue(value)
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

    /**
     * Remove the given value from the tree.
     *
     * After this operation the tree should contain no node with the value.
     *
     * If that value his not present, no action is performed.
     *
     * @param value
     */
    fun delete(value: Int) {
        deleteWithParent(value, this, null)

    }

    private fun deleteWithParent(value: Int, node: Node, parent: Node?) {
        if (node.key > value) {
            node.left?.let { deleteWithParent(value, it, this) }
        } else if (node.key < value) {
            node.right?.let { deleteWithParent(value, it, this) }
        } else {
            deleteNode(node, parent)
        }
    }

    private fun deleteNode(node: Node, parent: Node?) {
        val l = node.left
        val r = node.right
        if (parent == null) {
            if (l == null && r == null) {
                throw IllegalStateException("Can not remove the root node without children")
            }
            if (l != null && r == null) {
                node.key = l.key
                node.left = null
            } else if (l == null && r != null) {
                node.key = r.key
                node.right = null
            } else {
                val n = nodeWithMaxChild(node)
                node.key = n.key
                n.right = null
            }
        } else {
            if (l == null && r == null) {
                if (parent.left?.key == node.key){
                    parent.left = null
                } else if (parent.right?.key == node.key){
                    parent.right = null
                } else {
                    throw IllegalStateException("Parent does not contain the node")
                }
            } else if (l != null && r == null) {
                node.key = l.key
                node.left = null
            } else if (l == null && r != null) {
                node.key = r.key
                node.right = null
            } else {
                val n = nodeWithMaxChild(node)
                node.key = n.key
                n.right = null
            }


        }
    }

    private fun nodeWithMaxChild(node: Node): Node {

        return node.right?.let { r -> r.right?.let { nodeWithMaxChild(r) } ?: node }
                ?: throw IllegalArgumentException("The argument must have a right child")
    }

}

