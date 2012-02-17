Purpose
=======

GestureListView is an experiment on how to achieve an effect like the one I described `here on StackOverflow <http://stackoverflow.com/questions/9309245/how-to-implement-android-listview-opening-gesture>`_.

Those interactions/gestures (call them whatever you like) are simply amazing and one can see them more and more in iPhone and iPad apps such as Twitter or Clear.

My point is to gather ideas on this in order to achieve such a feature on an Android ListView.

So please gentlemen, let's help the community.

Contributions
=============

The main goal of this repository is to be highly collaborative but a lot of path may lead to the success of this enterprise.

For this reason, if you want to contribute to this repo and not only work on your own fork, please follow this simple rule: Code in a dedicated branch.

We'll all see later what's the best solution and put that one onto master.

Branches
========

Branch: master
--------------

Kept clean of development experiments. Is intended to contain the final working solution and the documentation on each branch via the README file.

Branch: attemp-via-scale-gesture-detector
-----------------------------------------

This branch is a first experiment with a ScaleGestureDetector.

Idea:

1. The pinch gesture is somehow like a dragging gesture, we can get the X and Y coordinates of center of the two fingers on the screen.
2. Get the index position of the first visible item in the list
3. Get the index position of the last visible item in the list
4. Iterate from the first index to the last with the getChildAt function
5. For each child, call the getLocationOnScreen method to get coordinates of the current iterated item
6. After that, some comparison between the pinch gesture coordinates and each item coordinates might be done inside the loop to get the two items between which the new row must me inserted
7. Use the scale factor to grow and shrink the newly added item

Result: Messy ListView scroll side effect but the item is added and its height can be manipulated.

Branch: 
-------

Your words...

Last word
=========

Let's be awesome!
