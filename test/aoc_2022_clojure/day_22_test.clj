(ns aoc-2022-clojure.day-22-test 
  (:require [clojure.test :refer [deftest is testing]]))

(def input "        ...#
        .#..
        #...
        ....
...#.......#
........#...
..#....#....
..........#.
        ...#....
        .....#..
        .#......
        ......#.

10R5L5R10L4R5L5")

(deftest monkey-map-a-test
  (testing "Day 22 - Part 1"
    (is (= 6032 (a input)))))

(deftest monkey-map-b-test
  (testing "Day 22 - Part 2"
    (is (= nil (b input)))))