(ns aoc-2022-clojure.day-12-test 
  (:require [aoc-2022-clojure.day-12 :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input "Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi")

(deftest hill-climbing-algorithm-a-test
  (testing "Day 12 - Part 1"
    (is (= 31 (a input)))))

(deftest hill-climbing-algorithm-b-test
  (testing "Day 12 - Part 2"
    (is (= 29 (b input)))))