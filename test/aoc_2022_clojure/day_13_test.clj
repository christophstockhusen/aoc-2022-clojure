(ns aoc-2022-clojure.day-13-test 
  (:require [aoc-2022-clojure.day-13 :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input "[1,1,3,1,1]
[1,1,5,1,1]

[[1],[2,3,4]]
[[1],4]

[9]
[[8,7,6]]

[[4,4],4,4]
[[4,4],4,4,4]

[7,7,7,7]
[7,7,7]

[]
[3]

[[[]]]
[[]]

[1,[2,[3,[4,[5,6,7]]]],8,9]
[1,[2,[3,[4,[5,6,0]]]],8,9]")

(deftest distress-signal-a-test
  (testing "Day 13 - Part 1"
    (is (= 13 (a input)))))

(deftest distress-signal-b-test
  (testing "Day 13 - Part 2"
    (is (= 140 (b input)))))