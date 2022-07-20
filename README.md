# Graph-in-Bubble-Shooter

### A simple implementation of graph algorithm in Bubble Shooter

## Introduction

Bubble Shooter is a classic game where the player shoot colored bubble at a field full of other bubbles. 
The goal is to eliminate all the bubbles on the field by shooting combinations of bubbles that have the same color.
The basic rules including:

<1> When the player matchs 3 bubbles, the bubbles will pop up and remove from the board.

<2> When any bubble is unattached from the root, it becomes "floater" and removed from the board.

## Graph Implementation

Let's start from the vertex of our graph:

The Bubble is simply a class that maintain all the vriable we need. We first set depth to -1 and discover to false, then we add all adjacent Bubbles to edges list.

```java
public class Bubble {
    public BubbleColor mBubbleColor;
    public int mDepth = -1;
    public boolean mDiscover = false;
    public final ArrayList<Bubble> mEdges = new ArrayList<>(6);
    // ...
}
```

### Breadth-First-Search

To find matched bubbles, we use BFS to search our graph. We start from the new bubble player shot, which is added to the graph after collide with other bubbles.
Then, we check the depth, if it is bigger or equal to 2 (start from 0), we simply remove those bubble depth is not -1.

```java
private void bfs(Bubble root, BubbleColor color) {
    Queue<Bubble> queue = new LinkedList<>();
    root.mDepth = 0;
    queue.offer(root);

    while (queue.size() > 0) {
        Bubble currentBubble = queue.poll();
        for (Bubble b : currentBubble.mEdges) {
            // Unvisited bubble
            if (b.mDepth == -1 && b.mBubbleColor == color) {
                b.mDepth = currentBubble.mDepth + 1;
                queue.offer(b);
            }
        }
    }
}
```

### Depth-First-Search

To find floaters, we use DFS to search our graph.

```java
private void dfs(Bubble bubble) {
    bubble.mDiscover = true;
    for (Bubble b : bubble.mEdges) {
        if (!b.mDiscover && b.mBubbleColor != BubbleColor.BLANK) {
            dfs(b);
        }
    }
}
```

First, we start from the bubble at first row as root.

```java
private void popFloater() {
    // We start dfs from root
    for (int i = 0; i < mCol; i++) {
        Bubble bubble = mBubbleArray[0][i];   // Bubble at row 0
        if (bubble.mBubbleColor != BubbleColor.BLANK) {
            // Search from root bubble with dfs
            dfs(bubble);
        }
    }
    // ...
}
```
Then, we simply remove all the bubble not being discoved. (Actually, set it to BLANK, not necessarily remove from our graph)

## Demo

<img src="https://user-images.githubusercontent.com/93536412/177979330-eb5e6b09-0928-4390-869f-f9d37f5cc32e.gif" width="200"> <img src="https://user-images.githubusercontent.com/93536412/177979793-13863f11-51c1-4b8f-80ca-58ce84188606.gif" width="200">

## Reference

Graph Algorithm base on "Introduction to Algorithms" by Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest and Clifford Stein

Game engine based on "Mastering Android Game Development" by Raul Pautals
