(ns aoc-2022-clojure.day-09-test 
  (:require [aoc-2022-clojure.day-09 :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input "R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2")

(deftest rope-bridge-a-test
  (testing "Day 9 - Part 1"
    (is (= 13 (a input)))))

(def input2 "R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20")

(deftest rope-bridge-b-test
  (testing "Day 9 - Part 2"
    (is (= 36 (b input2)))))
